package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class SunHalo {

    private static Vector2 HALO_SIZE=  new Vector2(200,200);
    private static int COS_MULT = 200;
    private static int SIN_MULT = 450;
    private static float HALF = 0.5f;
    private static int TRANSITION_TIME = 30;
    private static Vector2 VECTOR_STRECH = new Vector2(150,150);
    private static int TWO = 2;
    private static Vector2 windowDimensions;

public static GameObject create(GameObjectCollection gameObjects, int layer, GameObject sun, Color color)

    {
        GameObject sunHalo = new GameObject(sun.getTopLeftCorner(), HALO_SIZE, new OvalRenderable(color));
        sunHalo.setCenter(sun.getCenter());
        sunHalo.setCoordinateSpace((CoordinateSpace.CAMERA_COORDINATES));
        sunHalo.setTag("sunHalo");
        gameObjects.addGameObject(sunHalo,layer);
        new Transition<Float>(sunHalo, (Float val)->{
            sunHalo.setCenter(new Vector2((float) Math.cos(val)*COS_MULT,
                    (float) Math.sin(val)*SIN_MULT).add(windowDimensions.mult(HALF).add(VECTOR_STRECH)));},
                (float)(Math.PI+2), (float) (Math.PI+2+(2*Math.PI)),
                Transition.LINEAR_INTERPOLATOR_FLOAT,TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_LOOP,null );
        return sunHalo;
    }

    public static void setWindowDimensions(Vector2 windowDimension){
        windowDimensions = windowDimension;

    }

}
