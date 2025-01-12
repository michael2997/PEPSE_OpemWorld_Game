package pepse.world.trees.leaves;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.Terrain;

import java.awt.*;
import java.util.Random;

public class Leaf extends GameObject {
    private static Vector2 INITIAL_SIZE = new Vector2(30, 30);
    private static Vector2 FINAL_SIZE = new Vector2(27, 27);
    private static final int LEAVSLAYER = -50;
    private static final int FADETIME = 30;
    private Vector2 dimensions;
    private Renderable renderable;
    private static final int WAIT_TIME_TO_DEATH = 500;
    private static final int WAIT_TO_FADE = 10;
    private static final float LEAF_MOVMENT = 10f;
    private static final int WAIT_TIME_TO_MOVE = 300;
    private static final float TRANSITION_TIME = 0.5f;
    private Random rand;


    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Leaf(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                GameObjectCollection gameObjects, Random random) {
        super(topLeftCorner, dimensions, renderable);
        rand = random;
        this.dimensions = dimensions;
        this.renderable = renderable;
        createSingleLeaf(this, topLeftCorner, gameObjects);
        gameObjects.layers().shouldLayersCollide(LEAVSLAYER, LEAVSLAYER, false);
        gameObjects.layers().shouldLayersCollide(Layer.STATIC_OBJECTS, LEAVSLAYER, true);
    }

    /**
     * create a single leaf
     */
    private void createSingleLeaf(GameObject leaf, Vector2 topLeftCorner, GameObjectCollection gameObjects) {
        //schedual the leaf movements
        float waitTimeToMove = (float) rand.nextInt(WAIT_TIME_TO_MOVE)/1000;
        LeafMovements leafMovements = new LeafMovements(leaf);
        new ScheduledTask(leaf, waitTimeToMove, true, leafMovements::run);

        //schedual the leaf fall
        float waitTimeToDeath = rand.nextInt(WAIT_TIME_TO_DEATH);
        LeafFall fall = new LeafFall(leaf);
        new ScheduledTask(leaf, waitTimeToDeath, false, fall::run);
        new ScheduledTask(leaf, waitTimeToDeath, false, () -> {
            this.renderer().fadeOut(FADETIME,
                    () -> {
                        new ScheduledTask(
                                this, WAIT_TO_FADE, false, () -> {
                            new Leaf(topLeftCorner, dimensions,
                                    renderable, gameObjects, rand);
                        });
                    });
        });
        gameObjects.addGameObject(leaf, LEAVSLAYER);
    }


    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.setVelocity(Vector2.ZERO);
    }


}
