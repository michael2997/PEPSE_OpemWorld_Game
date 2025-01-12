package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Night {

    private static float FINAL_VALUE = 0.5f;
    private static int DEVIDOR =2;

    /**
     * create the night object
     */
    public static GameObject create(GameObjectCollection gameObjects, int layer, Vector2 windowDimensions,
                                                                                        float cycleLength){

        GameObject night = new GameObject(Vector2.ZERO, windowDimensions, new RectangleRenderable(Color.BLACK));
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(night, layer);
        night.setTag("night");

        new Transition<Float>(night, night.renderer()::setOpaqueness, 0f,
                FINAL_VALUE, Transition.CUBIC_INTERPOLATOR_FLOAT, cycleLength/DEVIDOR,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null );

        return night;

    }

}
