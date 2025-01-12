package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.Color;
import java.util.function.Consumer;

public class Sun {

    private static Vector2 SUN_SIZE = new Vector2(100,100);
    private static int TRANSITION_TIME = 30;
    private static int DEVIDOR3 = 3;
    private static int DEVIDOR2 = 2;
    private static int COS_MULT = 200;
    private static int SIN_MULT = 450;
    private static float HALF = 0.5f;
    private static Vector2 VECTOR_STRECH = new Vector2(150,150);
    private static int TWO = 2;



    public static GameObject create(GameObjectCollection gameObjects, int layer, Vector2 windowDimensions,
                                    float cycleLength){
        GameObject sun = new GameObject(new Vector2(windowDimensions.x()/DEVIDOR3,
                windowDimensions.y()/DEVIDOR3), SUN_SIZE, new OvalRenderable(Color.YELLOW));
        sun.setCoordinateSpace((CoordinateSpace.CAMERA_COORDINATES));
        sun.setTag("sun");
        gameObjects.addGameObject(sun, layer);
        new Transition<Float>(sun, (Float val)->{
            sun.setCenter(new Vector2((float) Math.cos(val)*COS_MULT,(float) Math.sin(val)*SIN_MULT)
                    .add(windowDimensions.mult(0.5f).add(VECTOR_STRECH)));},
                (float)(Math.PI+TWO), (float) (Math.PI+TWO+(DEVIDOR2*Math.PI)),
                Transition.LINEAR_INTERPOLATOR_FLOAT,TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_LOOP,null );
        return sun;



    }
}
