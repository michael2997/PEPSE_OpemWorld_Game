package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.util.ColorSupplier;
import pepse.world.Block;
import pepse.world.Terrain;
import pepse.world.trees.leaves.Leaf;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Tree {
    private GameObjectCollection gameObjects;
    private Vector2 windowDimension;

    private static final int BLOCK_SIZE = Block.SIZE;
    private static final int RANDOM_NUM = 5;
    private static final int TREE_TOP_DIAMETER = 3;
    private static final Color LEAF_COLOR = new Color(50, 200, 30);
    private Random random;

    private final int DEVIDOR2 = 2;
    private final int NINE = 9;
    private final int TEN = 10;
    private final int seed;


    public Tree(GameObjectCollection gameObjects, Vector2 windowDimension, int seed) {
        this.gameObjects = gameObjects;
        this.windowDimension = windowDimension;
        this.random = new Random(seed);
        this.seed = seed;

    }

    /**
     * find the closest mult to 30 to make sure it will synchrinize with the terrain
     * @param toFind x to find the closest mult
     * @param type if I want to finde the closest mult from up or down
     * @return
     */
    private int findClosestMult(int toFind, String type) {
        int toReturn = toFind;
        if (type.equals("min")) {
            while (true) {
                if (toReturn % Block.SIZE == 0) {
                    break;
                }
                toReturn--;
            }
        } else if (type.equals("max")) {
            while (true) {
                if (toReturn % Block.SIZE == 0) {
                    break;
                }
                toReturn++;
            }
        }
        return toReturn;
    }


    /**
     * create trees randomly in the range of the given coordinates
     * @param minX
     * @param maxX
     */
    public void createInRange(int minX, int maxX) {
        Block topBlock = null;
        int newMin = findClosestMult(minX, "min");
        int newMax = findClosestMult(maxX,"max");
        for (int i = 0; i < (newMax- newMin); i++) {
            if (i % BLOCK_SIZE == 0 && random.nextInt(BLOCK_SIZE/DEVIDOR2) == 1) {
                topBlock = TreeTrunk.create(gameObjects, windowDimension, new Vector2(newMin + i,
                        (int)Math.floor(Terrain.groundHeightAt(newMin + i * BLOCK_SIZE) )+
                                (int)Math.floor(NINE*windowDimension.y() / (TEN*Block.SIZE))*Block.SIZE
                                - BLOCK_SIZE));
                createLeavs(topBlock);
            }
        }
    }

    /**
     * create all the leavs for a single tree
     * @param topBlock the top block of the tree
     */
    private void createLeavs(Block topBlock) {
        Leaf leaf;
        int x = random.nextInt(TREE_TOP_DIAMETER);
        int diameter = TREE_TOP_DIAMETER + x;
        for (int i = -diameter; i < diameter; i++) {
            for (int j = -diameter; j < diameter; j++) {
                if (random.nextBoolean() && (i == diameter - 1 || j == diameter - 1)) {
                    continue;
                } else if (random.nextInt(RANDOM_NUM) == 1) {
                    continue;
                }
                leaf = new Leaf(new Vector2(topBlock.getCenter().x() + BLOCK_SIZE / DEVIDOR2 + j *
                        BLOCK_SIZE,topBlock.getCenter().y() + BLOCK_SIZE / DEVIDOR2 + i * BLOCK_SIZE),
                        new Vector2(BLOCK_SIZE, BLOCK_SIZE),
                        new RectangleRenderable(ColorSupplier.approximateColor(LEAF_COLOR)), gameObjects,
                        random);
                PepseGameManager.addToMap((int)(topBlock.getCenter().x()
                        + BLOCK_SIZE / DEVIDOR2 + j * BLOCK_SIZE), leaf);

            }
        }
    }

}
