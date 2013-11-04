package ch.idsia.gui;

/**
 * Created with IntelliJ IDEA.
 * User: Ciaran Kearney
 * Date: 04/11/13
 */
public class StringLookup
{
    //options

    //Agent name
    public static final String AGENT_NAME_TEXT = "Agent to run";
    public static final String AGENT_NAME_VAL = "-ag ";


    //map zoom level 0,1,2: default 1
    public static final String MAP_ZOOM_LEVEL_TEXT = "Map zoom level";
    public static final String MAP_ZOOM_LEVEL_VAL = "-zm ";

    //enemy zoom level 0,1,2 default 1
    public static final String ENEMY_ZOOM_LEVEL_TEXT = "Enemy Zoom level";
    public static final String ENEMY_ZOOM_LEVEL_VAL = "-ze ";

    //visualisation , on off, default off
    public static final String ENABLE_VISUALISATION_TEXT = "Enable Visualisation";
    public static final String ENABLE_VISUALISATION_VAL = "-vis ";

    //on top of other windows default off
    public static final String ON_TOP_TEXT = "Run on top of other Windows";
    public static final String ON_TOP_VAL = "-vaot ";

    //time limit Mario seconds: default: 200
    public static final String TIME_LIMIT_TEXT = "Time Limit";
    public static final String TIME_LIMIT_VAL = "-tl ";

    //Infinite time per level, default off
    public static final String INFINITE_TIME_TEXT = "Infinite Time";
    public static final String INFINITE_TIME_VAL = "-t ";

    //stop when first win obtained, default off
    public static final String STOP_ON_WIN_TEXT = "Stop on first win";
    public static final String STOP_ON_WIN_VAL = "-ssiw ";

    //Freeze animations and disable interactions, default off
    public static final String DISABLE_INTERACTIONS_TEXT = "Disable Enemy Interactions";
    public static final String DISABLE_INTERACTIONS_VAL = "-pw ";

    //Enable Power Restoration using speed action, default off
    public static final String POWER_RESTORE_TEXT = "Enable power restore";
    public static final String POWER_RESTORE_VAL = "-pr ";

    //exit on simulation end, default on
    public static final String EXIT_ON_SIMULATION_END_TEXT = "Exit when complete";
    public static final String EXIT_ON_SIMULATION_END_VAL = "-ewf ";

    //enable Game viewer, default off
    public static final String ENABLE_GAME_VIEWER_TEXT = "Enable game viewer";
    public static final String ENABLE_GAME_VIEWER_VAL = "-gv ";

    //Continuous updates to Game Viewer, default off
    public static final String UPDATE_GAME_VIEW_TEXT = "Continuous updates to game viewer";
    public static final String UPDATE_GAME_VIEW_VAL = "-gvc ";

    //Mario Mode 0,1,2 default 0
    public static final String MARIO_MODE_TEXT ="Mario Mode";
    public static final String MARIO_MODE_VAL ="-mm ";

    //Change FPS, 1-100, default 24
    public static final String CHANGE_FPS_TEXT = "Frames Per second";
    public static final String CHANGE_FPS_VAL = "-fps ";

    //MatLab output file, default ""
    public static final String MATLAB_OUTPUT_TEXT = "Matlab output filename";
    public static final String MATLAB_OUTPUT_VAL = "-m ";

    //level type 0,1,2 default 0
    public static final String LEVEL_TYPE_TEXT = "Level type";
    public static final String LEVEL_TYPE_VAL = "-lt ";

    //random seed 1 - MaxInt, default 1
    public static final String RANDOM_SEED_TEXT = "Random seed";
    public static final String RANDOM_SEED_VAL = "-ls ";

    //Level length 1 - 4096, default 320
    public static final String LEVEL_LENGTH_TEXT = "Level length";
    public static final String LEVEL_LENGTH_VAL = "-ll ";

    //Level height 15 - 20, default 15
    public static final String LEVEL_HEIGHT_TEXT = "Level height";
    public static final String LEVEL_HEIGHT_VAL = "-lh ";

    //Level Difficulty 0 - 30, default 0
    public static final String LEVEL_DIFFICULTY_TEXT = "Level difficulty";
    public static final String LEVEL_DIFFICULTY_VAL = "-ld ";

    //Dead Ends, on off, default off
    public static final String LEVEL_DEAD_ENDS_TEXT  = "Enable dead ends";
    public static final String LEVEL_DEAD_ENDS_VAL  = "-lde ";

    //Cannons on off, default on
    public static final String ENABLE_CANNONS_TEXT  = "Enable cannons";
    public static final String ENABLE_CANNONS_VAL  = "-lc ";

    //Hills, on off, default on
    public static final String ENABLE_HILLS_TEXT  = "Enable hills";
    public static final String ENABLE_HILLS_VAL  = "-lhs ";

    //Tubes, on off, default on
    public static final String ENABLE_TUBES_TEXT = "Enable tubes";
    public static final String ENABLE_TUBES_VAL = "-ltb ";

    //Gaps, on off, default on
    public static final String ENABLE_GAPS_TEXT = "Enable gaps";
    public static final String ENABLE_GAPS_VAL = "-lg ";

    //Hidden Blocks, on off, default off
    public static final String ENABLE_HIDDEN_BLOCKS_TEXT = "Enable hidden blocks";
    public static final String ENABLE_HIDDEN_BLOCKS_VAL = "-lhb ";

    //Enable Enemies, on off, default on
    public static final String ENABLE_ENEMIES_TEXT = "Enable enemies";
    public static final String ENABLE_ENEMIES_VAL = "-le ";

    //Enable Blocks, on off, default on
    public static final String ENABLE_BLOCKS_TEXT = " ";
    public static final String ENABLE_BLOCKS_VAL = "-lb ";

    //Enable Coins, on off, default on
    public static final String ENABLE_COINS_TEXT = " ";
    public static final String ENABLE_COINS_VAL = "-lco ";



    //parameters
    public static final String ON = "On";
    public static final String OFF= "Off";
    public static final String MOST_DETAILED_TEXT = "Most Detail";
    public static final String ZOOM_MOST_DETAILED_VAL = "0";
    public static final String MEDIUM_DETAIL_TEXT = "Medium Detail";
    public static final String ZOOM_MEDIUM_DETAIL_VAL = "1";
    public static final String LEAST_DETAIL_TEXT = "Least Detail";
    public static final String ZOOM_LOW_DETAIL_VAL = "2";
    public static final String MARIO_MODE_SMALL_TEXT = "Small";
    public static final String MARIO_MODE_SMALL_VAL = "0";
    public static final String MARIO_MODE_BIG_TEXT = "Big";
    public static final String MARIO_MODE_BIG_VAL = "1";
    public static final String MARIO_MODE_FIRE_TEXT = "Fire";
    public static final String MARIO_MODE_FIRE_VAL = "2";
    public static final String LEVEL_TYPE_OVERGROUND_TEXT = "Overground";
    public static final String LEVEL_TYPE_OVERGROUND_VAL = "0";
    public static final String LEVEL_TYPE_UNDERGROUND_TEXT = "Underground";
    public static final String LEVEL_TYPE_UNDERGROUND_VAL = "1";
    public static final String LEVEL_TYPE_CASTLE_TEXT = "Castle";
    public static final String LEVEL_TYPE_CASTLE_VAL = "2";
}
