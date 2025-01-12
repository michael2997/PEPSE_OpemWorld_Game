package pepse.world.trees.leaves;

import danogl.GameObject;
import danogl.util.Vector2;

/**
 * a class for the collback of falling leaf
 */
public class LeafFall implements Runnable{
    private final GameObject leaf;
    private final Vector2 VELOCITY = new Vector2(0,50);


    public LeafFall(GameObject leaf){
        this.leaf = leaf;
    }

    public void run() {
        leaf.setVelocity(VELOCITY);
    }
}
