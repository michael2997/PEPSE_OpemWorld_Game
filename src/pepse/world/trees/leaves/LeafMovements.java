package pepse.world.trees.leaves;


import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.util.Vector2;

import java.util.function.Consumer;

/**
 * a class for the collback of moving leaf
 */
public class LeafMovements implements Runnable {
    private GameObject LEAF;
    private static danogl.util.Vector2 INITIAL_SIZE = new danogl.util.Vector2(30, 30);
    private static danogl.util.Vector2 FINAL_SIZE = new danogl.util.Vector2(27, 27);
    private final float TRANSITION_TIME = 0.5f;
    public LeafMovements(GameObject leaf) {
        LEAF = leaf;
    }


    @Override
    public void run() {
        new Transition<java.lang.Float>(LEAF, LEAF.renderer()::setRenderableAngle, 0f,
                10f, Transition.LINEAR_INTERPOLATOR_FLOAT, TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        new Transition<danogl.util.Vector2>(LEAF, LEAF::setDimensions, INITIAL_SIZE,
                FINAL_SIZE, Transition.LINEAR_INTERPOLATOR_VECTOR, TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
    }
}
