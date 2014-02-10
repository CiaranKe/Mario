package competition.uu2013.common.oldCode.commonCode;


import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.tools.MarioAIOptions;
import competition.uu2013.common.oldCode.LevelScene;
import competition.uu2013.common.oldCode.level.Level;

import java.math.BigDecimal;

public class Map
{
    public static final int CELL_SIZE = 16;

    public static int levelWidth;
    public static int levelHeight;
    private static LevelScene scene;
    private static Level map;
    private static float lastX;
    private static float lastY;

    private static int debugPos = 0;


    public static void setMap(int levelHeight, int levelWidth, MarioAIOptions options)
    {
        Map.levelHeight = levelHeight;
        Map.levelWidth = levelWidth;


        scene = new LevelScene();
        scene.reset(options);
        scene.level = new Level(levelWidth,levelHeight);
        Map.map = scene.level;
        scene.init(options);

    }

    public static float [] getActionCoOrdinates(boolean[] action, float marioX, float marioY, boolean ableToJump, boolean onGround, int marioState)
    {

        scene.mario.setAction(action);
        scene.tick();

        System.out.println(new BigDecimal(GlobalOptions.realX - scene.mario.x).toPlainString()+","+
                new BigDecimal(GlobalOptions.realXA - scene.mario.xa).toPlainString()+","+
                new BigDecimal(GlobalOptions.realY - scene.mario.y).toPlainString()+","+
                new BigDecimal(GlobalOptions.realYA - scene.mario.ya).toPlainString());
        if (scene.mario.x != marioX || scene.mario.y != marioY)
        {
            scene.mario.x = marioX;
            scene.mario.xa = (marioX - lastX) * 0.89F;
            //if (Math.abs(scene.mario.y - marioY) > 0.1F )
            //{
                scene.mario.ya = ((marioY - lastY) * 0.85F) +3;
            //}
            scene.mario.y = marioY;
        }

        lastX = marioX;
        lastY = marioY;
        return new float[] {scene.mario.x, scene.mario.y};
    }

    public static float [] setEnemies(float [] newEnemies, float marioXFloat, float marioYFloat)
    {
        scene.setEnemies(newEnemies,marioXFloat,marioYFloat);
        return scene.getEnemiesFloatPos();

    }

    public static byte getViewAt(float x, float y) {
        int marioX = (int) x / Map.CELL_SIZE;
        int marioY = (int) y / Map.CELL_SIZE;

        byte a = map.getBlock(marioX, marioY);
        if (a == 0)
        {
            return Map.getEnemies(marioX, marioY);
        }
        return a;
    }


    public static void setScene(byte[][] scene, float marioXFloat, float marioYFloat)
    {
        int marioX = (int) marioXFloat / Map.CELL_SIZE;
        int marioY = (int) marioYFloat / Map.CELL_SIZE;
        int rfHeight = scene.length;
        int rfWidth = scene[0].length;
        int halfHeight = rfHeight /2;
        int halfWidth = rfWidth /2;

        //scene[height][width]
        //map[height][width]
        for (int y = (marioY - halfHeight), yUpdate = 0; y < (marioY + halfHeight); y++, yUpdate++)
        {
            for (int x = (marioX - halfHeight), xUpdate = 0; x < (marioX + halfHeight); x++, xUpdate++)
            {
                if ((y> 0) && (x > 0) && (y < Map.levelHeight) && (x < Map.levelWidth))
                {
                    map.setBlock(x,y,scene[yUpdate][xUpdate]);
                }
            }
        }
        //Map.printMap();
    }

    public static byte getEnemies(int x, int y)
    {
        return scene.getEnemies(x,y);
    }


    public void addLine(float x0, float y0, float x1, float y1, int color)
    {
        if(GlobalOptions.PosSize < 400)
        {
            GlobalOptions.Pos[GlobalOptions.PosSize][0] = (int)x0;
            GlobalOptions.Pos[GlobalOptions.PosSize][1] = (int)y0;
            GlobalOptions.Pos[GlobalOptions.PosSize][2] = color;
            GlobalOptions.PosSize++;
            GlobalOptions.Pos[GlobalOptions.PosSize][0] = (int)x1;
            GlobalOptions.Pos[GlobalOptions.PosSize][1] = (int)y1;
            GlobalOptions.Pos[GlobalOptions.PosSize][2] = color;
            GlobalOptions.PosSize++;
        }
    }

    public boolean dangerOfGap(float landPositionX)
    {
        int landingCell = (int) landPositionX / Map.CELL_SIZE;

        if ((map.getBlock(landingCell, levelHeight -1) != 0) ||
            (map.getBlock(landingCell +1, levelHeight -1) != 0) ||
            (map.getBlock(landingCell -1, levelHeight -1) != 0))
        {
            return false;
        }

        return true;
    }

    public static int estimateCost (byte b)
    {
        if (b==0)
        {
            return 0;
        }
        if (b==-10)
        {
            return 99;
        }
        return 0;
    }



    private static void printMap()
    {
        System.out.println("---------------------------------------------------------------------------");
        for (int y = 0; y < levelHeight; y++)
        {

            for (int x= 0; x < levelWidth; x++)
            {

                System.out.print(map.getBlock(x, y) + "\t");
            }
            System.out.println();
        }
        System.out.println("---------------------------------------------------------------------------");
    }
}