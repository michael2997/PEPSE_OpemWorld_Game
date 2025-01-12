package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.collisions.LayerManager;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Tree;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class PepseGameManager extends GameManager {


    private int TREE_LAYER = -170;
    private final int TWO = 2;
    private int LEAF_LAYER = -50;
    private float MULT_HALF = 0.5f;
    private float MULT_POINT_THREE = -0.3f;
    private Avatar avatar;
    private int minXworld = 0;
    private int maxXworld;
    private Tree treeManager;
    private Terrain terrainManager;
    private Vector2 windowDimensions;
    private static HashMap<Integer, ArrayList<GameObject>> objectsMap = new HashMap<>();
    private final int DAY_CYCLE_TIME = 30;
    private final Color HALO_COLOR = new Color(255, 255, 0, 20);
    private final int DEVIDE_2 = 2;
    private int screens[][] = new int[3][2];

    private HashMap<Vector2,Integer> reWorld = new HashMap<>();
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        windowDimensions = windowController.getWindowDimensions();
        maxXworld = (int) windowDimensions.x();
        windowController.setTargetFramerate(80);
        Sky.create(this.gameObjects(), windowDimensions, Layer.BACKGROUND);
        Night.create(this.gameObjects(), Layer.FOREGROUND, windowDimensions, DAY_CYCLE_TIME);
        GameObject sun = Sun.create(this.gameObjects(), Layer.BACKGROUND + 1, windowDimensions, DAY_CYCLE_TIME);
        SunHalo.setWindowDimensions(windowDimensions);
        SunHalo.create(this.gameObjects(), Layer.BACKGROUND + TWO, sun, HALO_COLOR);
        for (int i = -1; i < TWO; i++) {
            createScreen((int)(0+windowDimensions.x()*i),(int)(windowDimensions.x()+windowDimensions.x()*i) ,i+1);
        }
        avatar = Avatar.create(this.gameObjects(), Layer.DEFAULT,
                windowController.getWindowDimensions().mult(MULT_HALF), inputListener, imageReader);
        avatar.setWindowDimension(windowDimensions);
        this.setCamera(new Camera(avatar, new Vector2(0,
                windowController.getWindowDimensions().mult(MULT_POINT_THREE).y()),
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));
        avatar.physics().preventIntersectionsFromDirection(Vector2.ZERO);
    }

    /**
     * this function create all the objects in the give coordinates
     * @param idx each time we will keep 3 screens in an array, so this is the index in the array for the
     *           given screen
     */
    private void createScreen(int minX,int maxX,int idx ){
        Random random = new Random();
        int seed = random.nextInt();
        Vector2 toSearch = new Vector2(minX, maxX);
        if (reWorld.containsKey(toSearch)){
            seed = reWorld.get(toSearch);
        }
        else {
            reWorld.put(toSearch, seed);
        }
        terrainManager = new Terrain(this.gameObjects(), Layer.STATIC_OBJECTS, windowDimensions, seed);
        treeManager = new Tree(this.gameObjects(), windowDimensions, seed);
        treeManager.createInRange(minX, maxX);
        terrainManager.createInRange(minX, maxX);
        screens[idx] = new int[]{minX, maxX};
    }


    @Override
    public void update(float deltaTime){
        super.update(deltaTime);
        int oldMin = minXworld;
        int oldMax = maxXworld;
        if (avatar.getCenter().x() > maxXworld) {
            int  toDelet[] = screens[0];
            screens[0] = screens[1];
            screens[1] = screens[TWO];

            maxXworld = screens[1][1];
            minXworld = screens[1][0];
            createScreen(maxXworld, maxXworld+ (int)windowDimensions.x(), TWO);
            for (int i = toDelet[0]; i < toDelet[1]; i++) {
                if (objectsMap.containsKey(i)) {
                    for (GameObject obj : objectsMap.get(i)) {
                        gameObjects().removeGameObject(obj, Layer.STATIC_OBJECTS);
                        gameObjects().removeGameObject(obj, TREE_LAYER);
                        gameObjects().removeGameObject(obj, LEAF_LAYER);
                    }
                }
            }
        }
        if (avatar.getCenter().x()  < minXworld) {
            updateHelper();
        }
    }

    /**
     * helper to make the update func shorter
     */
    private void updateHelper(){
        int toDelete[] = screens[TWO];
        screens[TWO] = screens[1];
        screens[1] = screens[0];
        maxXworld = screens[1][1];
        minXworld = screens[1][0];
        createScreen( (int)(minXworld - windowDimensions.x()),minXworld, 0);
        for (int i = toDelete[0]; i < toDelete[1]; i++) {
            if (objectsMap.containsKey(i)) {
                for (GameObject obj : objectsMap.get(i)) {
                    gameObjects().removeGameObject(obj, Layer.STATIC_OBJECTS);
                    gameObjects().removeGameObject(obj, TREE_LAYER);
                    gameObjects().removeGameObject(obj, LEAF_LAYER);
                }
            }
        }
    }

    /**
     * add an object to the map of all the objects created so far
     * @param key key for the map
     * @param value for the map
     */
    public static void addToMap(int key, GameObject value) {
        if (objectsMap.containsKey(key)) {
            objectsMap.get(key).add(value);
        } else {
            objectsMap.put(key, new ArrayList<>());
            objectsMap.get(key).add(value);
        }
    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}