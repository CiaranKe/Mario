package competition.uu2013.common.level;
import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.tools.MarioAIOptions;
import competition.uu2013.common.hueristics.SearchNode;

/**
 * Map attempts to mimic the behaviour of the level class in the game.
 */
public class Map implements Cloneable
{
    public static final int CELL_SIZE = 16;

    /** The level width. */
    public static int levelWidth;
    
    /** The level height. */
    public static int levelHeight;
    
    /** The map. */
    private byte [][] map;

    /** The tile behaviours. */
    public static byte[] TILE_BEHAVIORS = new byte[256];
    public static final int BIT_BLOCK_UPPER = 1;
    public static final int BIT_BLOCK_ALL = 2;
    public static final int BIT_BLOCK_LOWER = 4;
    public static final int BIT_SPECIAL = 8;
    public static final int BIT_BUMPABLE = 16;
    public static final int BIT_BREAKABLE = 32;
    public static final int BIT_PICKUPABLE = 64;
    public static final int BIT_ANIMATED = 128;



    /**
     * Sets the height and width of the map.
     *
     * @param levelHeight the level height
     * @param levelWidth the level width
     */
    public static void setMap(int levelHeight, int levelWidth)
    {
        Map.levelHeight = levelHeight;
        Map.levelWidth = levelWidth;
        SearchNode.setGoal(levelWidth*16);
        loadTiles();
    }

    /**
     * Instantiates a new map.
     */
    public Map()
    {
        this.map = new byte[levelWidth][levelHeight];
    }

    /**  
     * Clones the map for use in another simulation
     * 
     * @see java.lang.Object#clone()
     */
    public Map clone()
    {
        Map m = new Map();

        for (int x = 0; x < map.length; x++)
        {
            for (int y = 0; y < map[x].length; y++)
            {
                m.map[x][y] = map[x][y];
            }
        }


        return m;
    }


    /**
     * Returns the byte value of a map X:Y location
     *
     * @param x the x
     * @param y the y
     * @return the view at
     */
    public byte getViewAt(float x, float y)
    {
        int marioX = (int) x / Map.CELL_SIZE;
        int marioY = (int) y / Map.CELL_SIZE;

        if ((marioX < 0) || (marioX >= levelWidth) || (marioY < 0) || (marioY >= levelHeight))
        {
            return 0;
        }

        return map[marioX][marioY];
    }


    /**
     * Updates the map with the information from the levelScene array.
     *
     * @param scene the scene
     * @param marioXFloat Mario's x position
     * @param marioYFloat MArio's y position
     */
    public void setScene(byte[][] scene, float marioXFloat, float marioYFloat)
    {
    	//get marios map location
        int marioX = (int) marioXFloat / Map.CELL_SIZE;
        int marioY = (int) marioYFloat / Map.CELL_SIZE;
                
        //get the receptive field values
        int rfHeight = scene.length;
        int rfWidth = scene[0].length;
        
        //get the distance ahead,above,below and behind 
        int halfHeight = rfHeight /2;
        int halfWidth = rfWidth /2;

        //scene[height][width]  I keep forgetting this
        //map[height][width]
        
        /* THIS WAS HARD TO FIGURE OUT, but looks really simple :(  */ 
        for (int y = (marioY - halfHeight), yUpdate = 0; y < (marioY + halfHeight); y++, yUpdate++)
        {
            for (int x = (marioX - halfHeight), xUpdate = 0; x < (marioX + halfHeight); x++, xUpdate++)
            {
                if ((y> 0) && (x > 0) && (y < Map.levelHeight) && (x < Map.levelWidth))
                {
                    this.setBlock(x, y, scene[yUpdate][xUpdate]);
                }
            }
        }
        //Map.printMap(map,"Sim");
    }


    /**
     * Prints the map. used for debugging
     *
     * @param map the map
     */
    public static void printMap(Map map)
    {
        System.out.println("---------------------------------------------------------------------------");
        for (int y = 0; y < levelHeight; y++)
        {

            for (int x= 0; x < levelWidth; x++)
            {

                System.out.print(map.getViewAt(x,y) + "\t");
            }
            System.out.println();
        }
        System.out.println("---------------------------------------------------------------------------");
    }

    /**
     * Prints the current scene. used for debugging
     *
     * @param scene the scene to print
     * @param name the name of the scene
     */
    public static void printScene(byte[][] scene, String name)
    {

        int halfWidth = scene.length /2;
        System.out.println(name + "---------------------------------------------------------------------------");
        for (int x = 0; x < scene.length; x++)
        {
            for (int y = 0; y < scene[0].length; y++)
            {
                if (y == halfWidth && x == halfWidth)
                {
                    System.out.print("M\t");
                }
                else
                {
                    System.out.print(scene[x][y] + "\t");
                }
            }
            System.out.println();
        }
        System.out.println("---------------------------------------------------------------------------");
    }

    /**
     * returns the byte value of a particular cell
     *
     * @param x the x location
     * @param y the y location
     * @return the byte value at the location
     */
    public byte getBlock(int x, int y)
    {
        if (x < 0) x = 0;
        if (y < 0) return 0;
        if (x >= levelWidth) x = levelWidth - 1;
        if (y >= levelHeight) y = levelHeight - 1;
        return map[x][y];
    }

    /**
     * Sets the byte value of a particular cell
     *
     * @param x the x location
     * @param y the y location
     * @param b the new byte value
     */
    public void setBlock(int x, int y, byte b)
    {
        if (x < 0) return;
        if (y < 0) return;
        
        //No ArrayindecOutofBoundsEception! no!
        if (x >= levelWidth) return;
        if (y >= levelHeight) return;
        map[x][y] = b;
    }

    /**
     * Checks if an obstacle is at the cell location and if it blocks
     * movement
     *
     * @param x the x location
     * @param y the y location
     * @param ya the y acceleration
     * @return true, if blocking
     */
    public boolean isBlocking(int x, int y, float ya)
    {

        byte block = getBlock(x, y);
        boolean blocking = ((TILE_BEHAVIORS[block & 0xff]) & BIT_BLOCK_ALL) > 0;
        
        //Oh noes!!
        blocking |= (ya > 0) && ((TILE_BEHAVIORS[block & 0xff]) & BIT_BLOCK_UPPER) > 0;
        blocking |= (ya < 0) && ((TILE_BEHAVIORS[block & 0xff]) & BIT_BLOCK_LOWER) > 0;

        return blocking;
    }

    /**
     * Load the tile behaviours from the games resources file.
     */
    private static void loadTiles()
    {
    	/* Hard coded to increase speed
    	try
        {
            DataInputStream dis = new DataInputStream(LevelScene.class.getResourceAsStream("resources/tiles.dat"));
            dis.readFully(TILE_BEHAVIORS);
        }
        catch (Exception e)
        {
            System.out.println("Damn!");
        }
        for (int x = 0; x < TILE_BEHAVIORS.length; x++)
        {
            System.out.println("TILE_BEHAVIORS[" + x + "] =" + TILE_BEHAVIORS[x] + ";");
        }
        */
    	
    	TILE_BEHAVIORS[0] =0; TILE_BEHAVIORS[1] =20; TILE_BEHAVIORS[2] =28; TILE_BEHAVIORS[3] =0; TILE_BEHAVIORS[4] =-126; 
    	TILE_BEHAVIORS[5] =-126; TILE_BEHAVIORS[6] =-126; TILE_BEHAVIORS[7] =-126; TILE_BEHAVIORS[8] =2; TILE_BEHAVIORS[9] =2; 
    	TILE_BEHAVIORS[10] =2; TILE_BEHAVIORS[11] =2; TILE_BEHAVIORS[12] =2; TILE_BEHAVIORS[13] =0; TILE_BEHAVIORS[14] =-118; 
    	TILE_BEHAVIORS[15] =0; TILE_BEHAVIORS[16] =-94; TILE_BEHAVIORS[17] =-110; TILE_BEHAVIORS[18] =-102; TILE_BEHAVIORS[19] =-94; 
    	TILE_BEHAVIORS[20] =-110; TILE_BEHAVIORS[21] =-110; TILE_BEHAVIORS[22] =-102; TILE_BEHAVIORS[23] =-110; TILE_BEHAVIORS[24] =2; 
    	TILE_BEHAVIORS[25] =0; TILE_BEHAVIORS[26] =2; TILE_BEHAVIORS[27] =2; TILE_BEHAVIORS[28] =2; TILE_BEHAVIORS[29] =0; 
    	TILE_BEHAVIORS[30] =2; TILE_BEHAVIORS[31] =0; TILE_BEHAVIORS[32] =-64; TILE_BEHAVIORS[33] =-64; TILE_BEHAVIORS[34] =-64; 
    	TILE_BEHAVIORS[35] =-64; TILE_BEHAVIORS[36] =0; TILE_BEHAVIORS[37] =0; TILE_BEHAVIORS[38] =0; TILE_BEHAVIORS[39] =0; 
    	TILE_BEHAVIORS[40] =2; TILE_BEHAVIORS[41] =2; TILE_BEHAVIORS[42] =0; TILE_BEHAVIORS[43] =0; TILE_BEHAVIORS[44] =0; 
    	TILE_BEHAVIORS[45] =0; TILE_BEHAVIORS[46] =2; TILE_BEHAVIORS[47] =0; TILE_BEHAVIORS[48] =0; TILE_BEHAVIORS[49] =0; 
    	TILE_BEHAVIORS[50] =0; TILE_BEHAVIORS[51] =0; TILE_BEHAVIORS[52] =0; TILE_BEHAVIORS[53] =0; TILE_BEHAVIORS[54] =0; 
    	TILE_BEHAVIORS[55] =0; TILE_BEHAVIORS[56] =2; TILE_BEHAVIORS[57] =2; TILE_BEHAVIORS[58] =0; TILE_BEHAVIORS[59] =0; 
    	TILE_BEHAVIORS[60] =0; TILE_BEHAVIORS[61] =0; TILE_BEHAVIORS[62] =0; TILE_BEHAVIORS[63] =0; TILE_BEHAVIORS[64] =0; 
    	TILE_BEHAVIORS[65] =0; TILE_BEHAVIORS[66] =0; TILE_BEHAVIORS[67] =0; TILE_BEHAVIORS[68] =0; TILE_BEHAVIORS[69] =0; 
    	TILE_BEHAVIORS[70] =0; TILE_BEHAVIORS[71] =0; TILE_BEHAVIORS[72] =0; TILE_BEHAVIORS[73] =0; TILE_BEHAVIORS[74] =0; 
    	TILE_BEHAVIORS[75] =0; TILE_BEHAVIORS[76] =0; TILE_BEHAVIORS[77] =0; TILE_BEHAVIORS[78] =0; TILE_BEHAVIORS[79] =0; 
    	TILE_BEHAVIORS[80] =0; TILE_BEHAVIORS[81] =0; TILE_BEHAVIORS[82] =0; TILE_BEHAVIORS[83] =0; TILE_BEHAVIORS[84] =0; 
    	TILE_BEHAVIORS[85] =0; TILE_BEHAVIORS[86] =0; TILE_BEHAVIORS[87] =0; TILE_BEHAVIORS[88] =0; TILE_BEHAVIORS[89] =0; 
    	TILE_BEHAVIORS[90] =0; TILE_BEHAVIORS[91] =0; TILE_BEHAVIORS[92] =0; TILE_BEHAVIORS[93] =0; TILE_BEHAVIORS[94] =0; 
    	TILE_BEHAVIORS[95] =0; TILE_BEHAVIORS[96] =0; TILE_BEHAVIORS[97] =0; TILE_BEHAVIORS[98] =0; TILE_BEHAVIORS[99] =0; 
    	TILE_BEHAVIORS[100] =0; TILE_BEHAVIORS[101] =0; TILE_BEHAVIORS[102] =0; TILE_BEHAVIORS[103] =0; TILE_BEHAVIORS[104] =0; 
    	TILE_BEHAVIORS[105] =0; TILE_BEHAVIORS[106] =0; TILE_BEHAVIORS[107] =0; TILE_BEHAVIORS[108] =0; TILE_BEHAVIORS[109] =0; 
    	TILE_BEHAVIORS[110] =0; TILE_BEHAVIORS[111] =0; TILE_BEHAVIORS[112] =0; TILE_BEHAVIORS[113] =0; TILE_BEHAVIORS[114] =0; 
    	TILE_BEHAVIORS[115] =0; TILE_BEHAVIORS[116] =0; TILE_BEHAVIORS[117] =0; TILE_BEHAVIORS[118] =0; TILE_BEHAVIORS[119] =0; 
    	TILE_BEHAVIORS[120] =0; TILE_BEHAVIORS[121] =0; TILE_BEHAVIORS[122] =0; TILE_BEHAVIORS[123] =0; TILE_BEHAVIORS[124] =0; 
    	TILE_BEHAVIORS[125] =0; TILE_BEHAVIORS[126] =0; TILE_BEHAVIORS[127] =0; TILE_BEHAVIORS[128] =2; TILE_BEHAVIORS[129] =2; 
    	TILE_BEHAVIORS[130] =2; TILE_BEHAVIORS[131] =0; TILE_BEHAVIORS[132] =1; TILE_BEHAVIORS[133] =1; TILE_BEHAVIORS[134] =1; 
    	TILE_BEHAVIORS[135] =0; TILE_BEHAVIORS[136] =2; TILE_BEHAVIORS[137] =2; TILE_BEHAVIORS[138] =2; TILE_BEHAVIORS[139] =0; 
    	TILE_BEHAVIORS[140] =2; TILE_BEHAVIORS[141] =2; TILE_BEHAVIORS[142] =2; TILE_BEHAVIORS[143] =0; TILE_BEHAVIORS[144] =2; 
    	TILE_BEHAVIORS[145] =2; TILE_BEHAVIORS[146] =2; TILE_BEHAVIORS[147] =0; TILE_BEHAVIORS[148] =0; TILE_BEHAVIORS[149] =0; 
    	TILE_BEHAVIORS[150] =0; TILE_BEHAVIORS[151] =0; TILE_BEHAVIORS[152] =2; TILE_BEHAVIORS[153] =2; TILE_BEHAVIORS[154] =2; 
    	TILE_BEHAVIORS[155] =0; TILE_BEHAVIORS[156] =2; TILE_BEHAVIORS[157] =2; TILE_BEHAVIORS[158] =2; TILE_BEHAVIORS[159] =0; 
    	TILE_BEHAVIORS[160] =2; TILE_BEHAVIORS[161] =2; TILE_BEHAVIORS[162] =2; TILE_BEHAVIORS[163] =0; TILE_BEHAVIORS[164] =0; 
    	TILE_BEHAVIORS[165] =0; TILE_BEHAVIORS[166] =0; TILE_BEHAVIORS[167] =0; TILE_BEHAVIORS[168] =2; TILE_BEHAVIORS[169] =2; 
    	TILE_BEHAVIORS[170] =2; TILE_BEHAVIORS[171] =0; TILE_BEHAVIORS[172] =2; TILE_BEHAVIORS[173] =2; TILE_BEHAVIORS[174] =2; 
    	TILE_BEHAVIORS[175] =0; TILE_BEHAVIORS[176] =2; TILE_BEHAVIORS[177] =2; TILE_BEHAVIORS[178] =2; TILE_BEHAVIORS[179] =0; 
    	TILE_BEHAVIORS[180] =1; TILE_BEHAVIORS[181] =1; TILE_BEHAVIORS[182] =1; TILE_BEHAVIORS[183] =0; TILE_BEHAVIORS[184] =0; 
    	TILE_BEHAVIORS[185] =0; TILE_BEHAVIORS[186] =0; TILE_BEHAVIORS[187] =0; TILE_BEHAVIORS[188] =0; TILE_BEHAVIORS[189] =0; 
    	TILE_BEHAVIORS[190] =0; TILE_BEHAVIORS[191] =0; TILE_BEHAVIORS[192] =0; TILE_BEHAVIORS[193] =0; TILE_BEHAVIORS[194] =0; 
    	TILE_BEHAVIORS[195] =0; TILE_BEHAVIORS[196] =0; TILE_BEHAVIORS[197] =0; TILE_BEHAVIORS[198] =0; TILE_BEHAVIORS[199] =0; 
    	TILE_BEHAVIORS[200] =0; TILE_BEHAVIORS[201] =0; TILE_BEHAVIORS[202] =0; TILE_BEHAVIORS[203] =0; TILE_BEHAVIORS[204] =0; 
    	TILE_BEHAVIORS[205] =0; TILE_BEHAVIORS[206] =0; TILE_BEHAVIORS[207] =0; TILE_BEHAVIORS[208] =0; TILE_BEHAVIORS[209] =0; 
    	TILE_BEHAVIORS[210] =0; TILE_BEHAVIORS[211] =0; TILE_BEHAVIORS[212] =0; TILE_BEHAVIORS[213] =0; TILE_BEHAVIORS[214] =0; 
    	TILE_BEHAVIORS[215] =0; TILE_BEHAVIORS[216] =0; TILE_BEHAVIORS[217] =0; TILE_BEHAVIORS[218] =0; TILE_BEHAVIORS[219] =0; 
    	TILE_BEHAVIORS[220] =0; TILE_BEHAVIORS[221] =0; TILE_BEHAVIORS[222] =0; TILE_BEHAVIORS[223] =0; TILE_BEHAVIORS[224] =1; 
    	TILE_BEHAVIORS[225] =1; TILE_BEHAVIORS[226] =1; TILE_BEHAVIORS[227] =0; TILE_BEHAVIORS[228] =0; TILE_BEHAVIORS[229] =0; 
    	TILE_BEHAVIORS[230] =0; TILE_BEHAVIORS[231] =0; TILE_BEHAVIORS[232] =0; TILE_BEHAVIORS[233] =0; TILE_BEHAVIORS[234] =0;
    	TILE_BEHAVIORS[235] =0; TILE_BEHAVIORS[236] =0; TILE_BEHAVIORS[237] =0; TILE_BEHAVIORS[238] =0; TILE_BEHAVIORS[239] =0; 
    	TILE_BEHAVIORS[240] =0; TILE_BEHAVIORS[241] =0; TILE_BEHAVIORS[242] =0; TILE_BEHAVIORS[243] =0; TILE_BEHAVIORS[244] =0; 
    	TILE_BEHAVIORS[245] =0; TILE_BEHAVIORS[246] =0; TILE_BEHAVIORS[247] =0; TILE_BEHAVIORS[248] =0; TILE_BEHAVIORS[249] =0; 
    	TILE_BEHAVIORS[250] =0; TILE_BEHAVIORS[251] =0; TILE_BEHAVIORS[252] =0; TILE_BEHAVIORS[253] =0; TILE_BEHAVIORS[254] =0; 
    	TILE_BEHAVIORS[255] =0;
    }
}