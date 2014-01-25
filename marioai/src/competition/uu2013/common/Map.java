package competition.uu2013.common;


import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.tools.MarioAIOptions;

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
    private static ArrayList<EnemySim> enemiesList;


    private static int debugPos = 0;


    public static void setMap(int levelHeight, int levelWidth, MarioAIOptions options)
    {
        Map.levelHeight = levelHeight;
        Map.levelWidth = levelWidth;

        map = new byte[levelWidth][levelHeight];
    }


    public static void setEnemies(float [] enemyObservation, float marioX, float marioY)
    {
        if (enemiesList == null)
        {
            enemiesList = new ArrayList<EnemySim>();
        }
        ArrayList<EnemySim> newEnemies = new ArrayList<EnemySim>();
        for (int x = 0; x < enemyObservation.length; x+=3)
        {
            System.out.println("====================================================================");
            System.out.println("Orig: Type: " + (byte) enemyObservation[x] +" X: " + ( marioX + enemyObservation[x+1]) + " Y: " + ( marioY + enemyObservation[x+2]));
            EnemySim sim = new EnemySim(( marioX + enemyObservation[x+1]), ( marioY + enemyObservation[x+2]), (int) enemyObservation[x] );

            for (EnemySim s: enemiesList)
            {
                if (s.compareTo(sim) == 0)
                {
                    s.setXY(sim.getX(), sim.getY());
                    sim = s;
                }
            }
            sim.move();
            newEnemies.add(sim);
        }
        enemiesList = newEnemies;
        System.out.println("----------------------------------------------------------------------");
    }

    public static void moveEnemies(int moves)
    {
        ArrayList<EnemySim> temp = (ArrayList<EnemySim>) enemiesList.clone();

        for (int x = 0; x < moves; x++)
        {
            for (EnemySim s: temp)
            {
                s.move();
                System.out.println((x+1) +"   : Type: " + s.getType() + " X: " + s.getX() + "Y: " + s.getY());
            }
        }
        System.out.println("====================================================================");
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
        //Map.printMap();
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
        byte block = getBlock(x,y);

        //mario or coin
        if ((block == MARIO) || (block == COIN))
        {
            return false;
        }
        if (block == PLATFORM)
        {
            return (ya > 0);
        }
        return block != 0;
    }
}