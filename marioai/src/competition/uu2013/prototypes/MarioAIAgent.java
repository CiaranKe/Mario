package competition.uu2013.prototypes;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.environments.Environment;


/**
 *  MarioAIAgent, does nothing by default, should be extended by other classes to avoid wasting time re-implementing 
 *  calls to the environment interface.
 *  
 *  @author Ciaran Kearney
 *  @version 1.0 
 *  @since 18/12/2013
 */
public class MarioAIAgent implements Agent
{
    
    /** action, always empty. */
    protected boolean [] action;
    
    /** The name of the agent. */
    protected String name;
    
    /** array of bytes showing the obstacles around Mario. */
    protected byte[][] levelScene;
    
    /** array of bytes showing the enemies around Mario. */
    protected byte[][] enemies;
    
    /** Combination of the levelScene and enemyScene arrays. */
    protected byte[][] mergedObservation;

    /** array of Mario's X:Y co-ordinates */
    protected float[] marioFloatPos;
    
    /** Array of on screen enemies and their X:Y co-ordinates  */
    protected float[] enemiesFloatPos;

    /** Information about mario's status within the level. */
    protected int[] marioState;

    /** local copy of the environment interface */
    protected Environment environment;

    /** MArio's state (won,dead,running). */
    protected int marioStatus;
    
    /** Mario's mode (big, small, fire) */
    protected int marioMode;
    
    /** is mario on ground. */
    protected boolean isMarioOnGround;
    
    /** is mario able to jump. */
    protected boolean isMarioAbleToJump;
    
    /** is mario able to shoot. */
    protected boolean isMarioAbleToShoot;
    
    /**  is mario carrying. a shell */
    protected boolean isMarioCarrying;
    
    /** The total kills for the level */
    protected int getKillsTotal;
    
    /** The kills by fire for the level. */
    protected int getKillsByFire;
    
    /** The kills by stomp for the level. */
    protected int getKillsByStomp;
    
    /** The kills by shell for the level. */
    protected int getKillsByShell;

    /** The receptive field width around mario. */
    protected int receptiveFieldWidth;
    
    /** The receptive field height around Mario. */
    protected int receptiveFieldHeight;
    
    /** The current row of MArio in the receptive field. */
    protected int marioEgoRow;
    
    /** The current column of mario in the receptive field */
    protected int marioEgoCol;
    
    /** The time left in the level. */
    protected int timeLeft;
    
    /** the zoom level for the scene. */
    protected int zLevelScene = 1;
    
    /** the zoom level for the enemies. */
    protected int zLevelEnemies = 0;

    /**
     * Instantiates a new MarioAI agent.
     *
     * @param s the name of the agent
     */
    public MarioAIAgent(String s)
    {
    	name = s;
        setName(s);
        action = new boolean[Environment.numberOfKeys];
        enemiesFloatPos = null;
        marioFloatPos = null;
        marioState = null;
    }

    /** 
     * Returns an empty action 
     * 
     * @return an empty action
     * 
     * @see ch.idsia.agents.Agent#getAction()
     */
    public boolean[] getAction()
    {
        return new boolean[Environment.numberOfKeys]; //empty action

    }

    /** 
     * Takes information from the environment interface and places the information into variables
     * 
     * @param The environment interface, informartion about the current level
     * 
     * @see ch.idsia.agents.Agent#integrateObservation(ch.idsia.benchmark.mario.environments.Environment)
     */
    public void integrateObservation(Environment environment)
    {

        levelScene = environment.getLevelSceneObservationZ(zLevelScene);
        enemies = environment.getEnemiesObservationZ(zLevelEnemies);
        mergedObservation = environment.getMergedObservationZZ(1, 0);

        this.marioFloatPos = environment.getMarioFloatPos();
        this.enemiesFloatPos = environment.getEnemiesFloatPos();
        this.marioState = environment.getMarioState();

        receptiveFieldWidth = environment.getReceptiveFieldWidth();
        receptiveFieldHeight = environment.getReceptiveFieldHeight();
        timeLeft = environment.getEvaluationInfo().timeLeft;
        this.environment = environment;

        marioStatus = marioState[0];
        marioMode = marioState[1];
        isMarioOnGround = marioState[2] == 1;
        isMarioAbleToJump = marioState[3] == 1;
        isMarioAbleToShoot = marioState[4] == 1;
        isMarioCarrying = marioState[5] == 1;
        getKillsTotal = marioState[6];
        getKillsByFire = marioState[7];
        getKillsByStomp = marioState[8];
        getKillsByShell = marioState[9];
    }

    /** Not implemented
     * @see ch.idsia.agents.Agent#giveIntermediateReward(float)
     */
    public void giveIntermediateReward(float intermediateReward)
    {

    }

    /** not implemented 
     * @see ch.idsia.agents.Agent#reset()
     */
    public void reset()
    {
        action = new boolean[Environment.numberOfKeys];// Empty action
    }

    /** Not implemented 
     * 
     * @see ch.idsia.agents.Agent#setObservationDetails(int, int, int, int)
     */
    public void setObservationDetails(final int rfWidth, final int rfHeight, final int egoRow, final int egoCol)
    {
        receptiveFieldWidth = rfWidth;
        receptiveFieldHeight = rfHeight;

        marioEgoRow = egoRow;
        marioEgoCol = egoCol;
    }


    /** returns the current agent's name
     * 
     * @see ch.idsia.agents.Agent#getName()
     */
    public String getName() 
    { 
    	return name; 
    }

    /** set's the current agent's name 
     * @see ch.idsia.agents.Agent#setName(java.lang.String)
     */
    public void setName(String Name) 
    { 
    	this.name = Name; 
    }
}
