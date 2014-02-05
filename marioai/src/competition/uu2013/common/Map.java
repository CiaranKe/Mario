package competition.uu2013.common;


import ch.idsia.benchmark.mario.engine.GeneralizerLevelScene;
import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.benchmark.mario.engine.LevelScene;
import ch.idsia.tools.MarioAIOptions;
import competition.uu2013.common.Sprites.EnemySim;

import java.io.DataInputStream;
import java.util.ArrayList;

public class Map
{
    public static final int CELL_SIZE = 16;
    public static final byte MARIO = 1;
    public static final byte COIN = 34;
    public static final byte PLATFORM = -11;

    public static int levelWidth;
    public static int levelHeight;
    private static byte [][] map;
    private static byte [][] data;

    public static byte[] TILE_BEHAVIORS = new byte[256];
    public static final int BIT_BLOCK_UPPER = 1 << 0;
    public static final int BIT_BLOCK_ALL = 1 << 1;
    public static final int BIT_BLOCK_LOWER = 1 << 2;
    public static final int BIT_SPECIAL = 1 << 3;
    public static final int BIT_BUMPABLE = 1 << 4;
    public static final int BIT_BREAKABLE = 1 << 5;
    public static final int BIT_PICKUPABLE = 1 << 6;
    public static final int BIT_ANIMATED = 1 << 7;



    public static void setMap(int levelHeight, int levelWidth, MarioAIOptions options)
    {
        Map.levelHeight = levelHeight;
        Map.levelWidth = levelWidth;

        map = new byte[levelWidth][levelHeight];
        data = new byte[levelWidth][levelHeight];
    }


    public static byte getViewAt(float x, float y) {
        int marioX = (int) x / Map.CELL_SIZE;
        int marioY = (int) y / Map.CELL_SIZE;

        return map[marioX][marioY];
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
                    Map.setBlock(x,y,scene[yUpdate][xUpdate]);
                }
            }
        }
        //Map.printMap(map,"Sim");
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

        if ((map[landingCell][levelHeight -1] != 0) ||
            (map[landingCell +1][levelHeight -1] != 0) ||
            (map[landingCell -1][levelHeight -1] != 0))
        {
            return false;
        }

        return true;
    }

    public static void printMap()
    {
        System.out.println("---------------------------------------------------------------------------");
        for (int y = 0; y < levelHeight; y++)
        {

            for (int x= 0; x < levelWidth; x++)
            {

                System.out.print(map[x][y] + "\t");
            }
            System.out.println();
        }
        System.out.println("---------------------------------------------------------------------------");
    }

    public static void printScene(byte[][] scene, String name)
    {
        System.out.println(name + "---------------------------------------------------------------------------");
        for (int x = 0; x < scene.length; x++)
        {
            for (int y = 0; y < scene[0].length; y++)
            {
                System.out.print(scene[x][y] + "\t");
            }
            System.out.println();
        }
        System.out.println("---------------------------------------------------------------------------");
    }


    public static void CompareMap(byte[][] _map, String name)
    {
        System.out.println(name + "---------------------------------------------------------------------------");
        for (int y = 0; y < levelHeight; y++)
        {

            for (int x= 0; x < levelWidth; x++)
            {
                boolean sim = (map[x][y] !=0);
                boolean act = (_map[x][y] !=0);

                if (sim == act)
                {
                    System.out.print("M\t");
                }
                else
                {
                    System.out.print(_map[x][y]+"\t");
                }

            }
            System.out.println();
        }
        System.out.println("---------------------------------------------------------------------------");
    }

    public static byte getBlock(int x, int y)
    {
        if (x < 0) x = 0;
        if (y < 0) return 0;
        if (x >= levelWidth) x = levelWidth - 1;
        if (y >= levelHeight) y = levelHeight - 1;
        return map[x][y];
    }

    public static void setBlock(int x, int y, byte b)
    {
        if (x < 0) return;
        if (y < 0) return;
        if (x >= levelWidth) return;
        if (y >= levelHeight) return;
        map[x][y] = b;
    }

    public static boolean isBlocking(int x, int y, float xa, float ya)
    {

        byte block = getBlock(x, y);
        return block != 0;
    }

    public static void loadBehaviours()
    {
        try
        {
            DataInputStream dis = new DataInputStream(LevelScene.class.getResourceAsStream("resources/tiles.dat"));
            dis.readFully(TILE_BEHAVIORS);
        }
        catch (Exception e)
        {
            System.out.println("Damn!");
        }
        /*
        for (int x = 0; x < TILE_BEHAVIORS.length; x++)
        {
            System.out.println("TILE_BEHAVIORS[" + x + "] =" + TILE_BEHAVIORS[x] + ";");
        }
        */
    }
}