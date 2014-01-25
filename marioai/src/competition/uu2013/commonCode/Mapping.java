package competition.uu2013.commonCode;

import competition.uu2013.common.compcode.LevelScene;
import competition.uu2013.common.compcode.sprites.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: fluffy
 * Date: 30/12/13
 * Time: 16:09
 * To change this template use File | Settings | File Templates.
 */
public class Mapping
{
    private static final int CELL_SIZE = 16;

    private LevelScene scene;
    private static int levelHeight;
    private static int levelWidth;

    public static void setMap(int newLevelHeight, int newLevelWidth)
    {
        levelHeight = newLevelHeight;
        levelWidth = newLevelWidth;
    }

    public void setLevelScene(byte[][] levelScene, float[] enemiesFloatPos, float [] marioFloatPos)
    {
        int marioX = (int) marioFloatPos[0] / CELL_SIZE;
        int marioY = (int) marioFloatPos[1] / CELL_SIZE;
        int rfHeight = levelScene.length;
        int rfWidth = levelScene[0].length;
        int halfHeight = rfHeight /2;
        int halfWidth = rfWidth /2;

        //scene[height][width]
        //map[height][width]
        for (int heightIterator = (marioY - halfHeight), xUpdate = 0; heightIterator < (marioY + halfHeight); heightIterator++, xUpdate++)
        {
            for (int lengthIterator = (marioX - halfWidth), yUpdate = 0; lengthIterator < (marioX + halfWidth); lengthIterator++, yUpdate++)
            {
                if ((heightIterator> 0) && (lengthIterator > 0) && (heightIterator < levelHeight) && (lengthIterator < levelWidth))
                {
                    scene.level.setBlock(lengthIterator,heightIterator,levelScene[yUpdate][xUpdate]);
                }
            }
        }
        this.setEnemies(enemiesFloatPos);
    }

    private void setEnemies(float[] enemiesFloatPos)
    {
        List<Sprite> newEnemies = new ArrayList<Sprite>();

        // array contains <type, xLocation, yLocation>, <type, xLocation, yLocation>
        for (int enemyIterator = 0; enemyIterator < enemiesFloatPos.length; enemyIterator+=3)
        {
            int enemyKind = (int) enemiesFloatPos[enemyIterator];
            float xPosition = enemiesFloatPos[enemyIterator+1];
            float yPosition = enemiesFloatPos[enemyIterator+2];

            if ((enemyKind == Sprite.KIND_NONE) && (enemyKind == Sprite.KIND_FIRE_FLOWER))
            {
                continue;
            }

            int enemyType = Sprite.KIND_NONE;
            boolean wingedEnemy = false;
            switch (enemyKind)
            {
                case(Sprite.KIND_BULLET_BILL):
                    enemyType = -2;
                    break;
                case(Sprite.KIND_GOOMBA):
                    enemyType = Enemy.IN_FILE_POS_GOOMBA;
                    break;
                case(Sprite.KIND_SHELL):
                    enemyType = Enemy.KIND_SHELL;
                    break;
                case(Sprite.KIND_GOOMBA_WINGED):
                    enemyType = Enemy.IN_FILE_POS_GOOMBA;
                    wingedEnemy = true;
                    break;
                case(Sprite.KIND_GREEN_KOOPA):
                    enemyType = Enemy.IN_FILE_POS_GREEN_KOOPA;
                    break;
                case(Sprite.KIND_GREEN_KOOPA_WINGED):
                    enemyType = Enemy.IN_FILE_POS_GREEN_KOOPA;
                    wingedEnemy = true;
                    break;
                case(Sprite.KIND_RED_KOOPA):
                    enemyType = Enemy.KIND_RED_KOOPA;
                    break;
                case(Sprite.KIND_RED_KOOPA_WINGED):
                    enemyType = Enemy.IN_FILE_POS_RED_KOOPA;
                    wingedEnemy = true;
                    break;
                case(Sprite.KIND_SPIKY):
                    enemyType = Enemy.IN_FILE_POS_SPIKY;
                    break;
                case(Sprite.KIND_SPIKY_WINGED):
                    enemyType = Enemy.IN_FILE_POS_SPIKY;
                    wingedEnemy = true;
                    break;
                case(Sprite.KIND_ENEMY_FLOWER):
                    enemyType = Enemy.IN_FILE_POS_FLOWER;
                    break;
                case(Sprite.KIND_WAVE_GOOMBA):
                    enemyType = Enemy.POSITION_WAVE_GOOMBA;
                    break;
            }
            if (enemyType == Sprite.KIND_NONE)
            {
                continue;
            }

            float maximumMovement = 2.01F * Enemy.ENEMIES_SIDEWAYS_SPEED;

        }
    }

    public void tick()
    {
        scene.tick();
    }
}
