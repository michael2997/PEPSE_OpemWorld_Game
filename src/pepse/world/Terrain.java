package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.util.ColorSupplier;


import java.util.ArrayList;

import java.awt.Color;
import java.util.Random;

import static java.lang.Math.sin;
import static java.lang.Math.sinh;

public class Terrain {

    private final GameObjectCollection collection;
    private final int GROUNDLAYER;
    private final float groundHeightAtX0;
    public ArrayList<Block> blocks;
    private static final int TERRAIN_DEPTH = 8;
    private static final int seed=5;
    private static final int NINE = 9;
    private static final int TEN = 10;
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);

    public Terrain(GameObjectCollection gameObjects,
                   int groundLayer, Vector2 windowDimensions,
                   int seed) {
        collection = gameObjects;
        GROUNDLAYER = groundLayer;
        blocks = new ArrayList<>();
        groundHeightAtX0 = (int)Math.floor(NINE*windowDimensions.y() / (TEN*Block.SIZE))*Block.SIZE;
    }

    /**
     * prlin noise function
     */
    public static float groundHeightAt(float x) {
        Random random = new Random(seed);
        float ret = (float) (random.nextFloat()*Math.sin(Math.sin(x*random.nextFloat())*Math.sin(x))+
                random.nextFloat()*sin(random.nextFloat()*sin(random.nextFloat()*x*Math.sin(x))));
        return ret;
    }



    /**
     * find the closest mult to 30 to make sure it will synchrinize with the block size
     * @param toFind x to find the closest mult
     * @param type if I want to finde the closest mult from up or down
     * @return
     */
    private int findClosestMult(int toFind, String type) {
        int toReturn = toFind;
        if (type == "min") {
            while (true) {
                if (toReturn % Block.SIZE == 0) {
                    break;
                }
                toReturn--;
            }
        } else if (type == "max") {
            while (true) {
                if (toReturn % Block.SIZE == 0) {
                    break;
                }
                toReturn++;
            }
        }
        return toReturn;
    }


    public void createInRange (int minX, int maxX){
        int howManyBlocks = (int) (Math.floor((maxX - minX) / Block.SIZE) + 2);
        int newMin = findClosestMult(minX, "min");
        int newMax = findClosestMult(maxX,"max");
        for (int i = 0; i < howManyBlocks; i++) {
            for (int j = 0; j < TERRAIN_DEPTH; j++) {
                Block block = new Block(new Vector2((newMin+i*Block.SIZE),
                        (int)Math.floor(groundHeightAt(newMin+i)/Block.SIZE)*Block.SIZE+
                        j*Block.SIZE+groundHeightAtX0),
                        new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR)));
                block.setTag("ground");
                PepseGameManager.addToMap((newMin+i*Block.SIZE), block);
                collection.addGameObject(block, Layer.STATIC_OBJECTS);
                blocks.add(block);
            }
        }
    }
}
