package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;

public class TreeTrunk {
    private static final int TREELAYER = -170;
    private static final Color TREE_COLOR = new Color(100,50,20);
    private static final int TREE_HEIGHT = 10;

    /**
     * create a single tree trunk
     */
    public static Block create(GameObjectCollection gameObjects, Vector2 windowDimensions,
                                    Vector2 topLeftCorner){
        Block lastBlock = null;
        for (int i = 0; i < TREE_HEIGHT; i++) {
            Block singleBlock = new Block(new Vector2(topLeftCorner.x(), topLeftCorner.y()-i*Block.SIZE)
                    , new RectangleRenderable(ColorSupplier.approximateColor(TREE_COLOR)));
            gameObjects.addGameObject(singleBlock,TREELAYER);
            PepseGameManager.addToMap((int) topLeftCorner.x(), singleBlock);
            lastBlock = singleBlock;
        }

        return lastBlock;

    }

    
}
