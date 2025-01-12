package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.ScheduledTask;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.*;
import danogl.util.Vector2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Avatar extends GameObject {
    private static Renderable[] IDLE_WALK;
    private static Renderable[] IDLE_FLY;
    private static Renderable[] IDLE_STATIC;
    private static int TREE_LAYER = -170;
    private float ENERGY = 100;
    private static final float VELOCITY_X = 300;
    private static final float VELOCITY_Y = -300;
    private static final float GRAVITY = 500;
    private final float ENERGY_ADD = 0.5f;
    private final float TIME_CLIPS = 0.2f;
    private final int MAX_ENERGY = 100;
    private UserInputListener inputListener;
    private Vector2 windowDimension;
    private int CUR_POS;
    private static GameObjectCollection gameObject;


    public static Avatar create(GameObjectCollection gameObjects, int layer, Vector2 topLeftCorner,
                                UserInputListener inputListener, ImageReader imageReader) {
        ImageRenderable IDLE_STATIC1 = imageReader.readImage("assets/idle_0.png", true);
        setImg(imageReader);
        Avatar avatar = new Avatar(topLeftCorner, inputListener, new Vector2(IDLE_STATIC1.width(),
                IDLE_STATIC1.height()), gameObjects);
        gameObject = gameObjects;
        gameObjects.addGameObject(avatar);
        gameObjects.layers().shouldLayersCollide(TREE_LAYER, Layer.DEFAULT, true);
        return avatar;
    }

    private static void setImg(ImageReader imageReader) {
        ImageRenderable IDLE_STATIC1 = imageReader.readImage("assets/idle_0.png", true);
        Renderable IDLE_STATIC2 = imageReader.readImage("assets/idle_1.png", true);
        Renderable IDLE_STATIC3 = imageReader.readImage("assets/idle_2.png", true);
        Renderable IDLE_Static4 = imageReader.readImage("assets/idle_3.png", true);
        IDLE_STATIC = new Renderable[]{IDLE_STATIC1, IDLE_STATIC2, IDLE_STATIC3, IDLE_Static4};


        Renderable IDLE_WALK1 = imageReader.readImage("assets/run_0.png", true);
        Renderable IDLE_WALK2 = imageReader.readImage("assets/run_1.png", true);
        Renderable IDLE_WALK3 = imageReader.readImage("assets/run_2.png", true);
        Renderable IDLE_WALK4 = imageReader.readImage("assets/run_3.png", true);
        IDLE_WALK = new Renderable[]{IDLE_WALK1, IDLE_WALK2, IDLE_WALK3, IDLE_WALK4};


        Renderable IDLE_FLY1 = imageReader.readImage("assets/swim_0.png", true);
        Renderable IDLE_FLY2 = imageReader.readImage("assets/swim_1.png", true);
        Renderable IDLE_FLY3 = imageReader.readImage("assets/swim_2.png", true);
        Renderable IDLE_FLY4 = imageReader.readImage("assets/swim_3.png", true);
        IDLE_FLY = new Renderable[]{IDLE_FLY1, IDLE_FLY2, IDLE_FLY3, IDLE_FLY4};
    }




    public Avatar(Vector2 pos, UserInputListener inputListener,
                  Vector2 dim, GameObjectCollection gameObjects) {


        super(pos, dim, IDLE_STATIC[0]);
        this.renderer().setRenderable(new AnimationRenderable(IDLE_STATIC, TIME_CLIPS));
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        this.inputListener = inputListener;
        CUR_POS = 0;
    }

    public void setWindowDimension(Vector2 windowDimension){
        this.windowDimension = windowDimension;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.setVelocity(Vector2.ZERO);
    }

    private void changeAnimation(int pos){
        if (CUR_POS != 1 && pos == 1){
            this.renderer().setRenderable(new AnimationRenderable(IDLE_WALK, TIME_CLIPS));
            CUR_POS = 1;
        }
        if (CUR_POS != 2 && pos == 2){
            this.renderer().setRenderable(new AnimationRenderable(IDLE_FLY, TIME_CLIPS));
            CUR_POS = 2;
        }
        if (CUR_POS != 0 && pos == 0){
            this.renderer().setRenderable(new AnimationRenderable(IDLE_STATIC, TIME_CLIPS));
            CUR_POS = 0;
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;
        gameObject.layers().shouldLayersCollide(Layer.DEFAULT,Layer.STATIC_OBJECTS, true);

        int pos = 0;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            xVel -= VELOCITY_X;
            this.renderer().setIsFlippedHorizontally(true);
            pos = 1;
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            xVel += VELOCITY_X;
            this.renderer().setIsFlippedHorizontally(false);
            pos = 1;
        }
        transform().setVelocityX(xVel);
        if (inputListener.isKeyPressed(KeyEvent.VK_SHIFT) &&
                inputListener.isKeyPressed(KeyEvent.VK_SPACE) && ENERGY > 0) {
            transform().setVelocityY(-VELOCITY_X);
            ENERGY -= ENERGY_ADD;
            pos = 2;
        }
        if (getVelocity().y() == 0 && ENERGY < MAX_ENERGY) {
            ENERGY += ENERGY_ADD;
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0)
            transform().setVelocityY(VELOCITY_Y);
        changeAnimation(pos);
        if (this.getCenter().y() > (int)Math.floor(Terrain.groundHeightAt(this.getCenter().x())/Block.SIZE)*Block.SIZE+
                Block.SIZE+(int)Math.floor(9*windowDimension.y() / (10*Block.SIZE))*Block.SIZE){
            gameObject.layers().shouldLayersCollide(Layer.DEFAULT,Layer.STATIC_OBJECTS, false);
            transform().setVelocityY(VELOCITY_Y);
        }
    }
}
