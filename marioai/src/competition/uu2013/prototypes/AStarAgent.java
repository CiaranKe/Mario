package competition.uu2013.prototypes;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.environments.Environment;
import competition.uu2013.common.hueristics.AStarSearch;
import competition.uu2013.common.level.Enemy;
import competition.uu2013.common.level.Map;
import competition.uu2013.common.level.WorldSim;
import competition.uu2013.common.Sprites.MarioSim;

/**
 * AStarAgent, attempts to 'solve' Mario by searching through simulations
 * of the level using A*.
 */
public class AStarAgent extends MarioAIAgent implements Agent
{
    
    /** The search */
    private AStarSearch search;
    /** Workaround for known issue with jumping  */
    private int jumpCounter = 0;
    /** Was MArio on the ground in the previous frame? */
    private boolean wasOnGround = false;

    /**
     * Instantiates a new a star agent.
     */
    public AStarAgent()
    {
        super("AStarAgent");
        reset();
    }

    /** returns the next planned action for this agent
     * 
     * @see MarioAIAgent#getAction()
     */
    @Override
    public boolean [] getAction()
    {
    	//time recording
        long startTime = System.currentTimeMillis();
        //System.out.println("============================================================================================");
        //--------------------------------------------------------------------------------------------------
        //if this is the first turn, create our search and the working copy of the simulation 
        if (search == null)
        {
            search = new AStarSearch(new WorldSim(new MarioSim(marioFloatPos[0],marioFloatPos[1], 0.0F, 3.0F), new Enemy(), new Map()));
            search.updateSim(marioFloatPos[0], marioFloatPos[1], isMarioAbleToJump, isMarioOnGround, wasOnGround, isMarioAbleToShoot, marioStatus, this.enemiesFloatPos, this.levelScene, action);

        }
        else
        {
        	//Work around for issue with jumping
            if (isMarioOnGround && isMarioAbleToJump)
            {
                jumpCounter = 7;
            }

            //update the simulation
            //System.out.println("Updating sim");
            search.updateSim(marioFloatPos[0], marioFloatPos[1], isMarioAbleToJump, isMarioOnGround, wasOnGround, isMarioAbleToShoot, marioStatus, this.enemiesFloatPos, this.levelScene, action);
            //get the next action
            action = search.pathFind(marioFloatPos[0],marioFloatPos[1],startTime, false);
            //record the current on ground status
            this.wasOnGround = isMarioOnGround;
            //System.out.println("Completing: " + Action.nameAction(action));
        }
        //--------------------------------------------------------------------------------------------------
        //System.out.println("Method Time: " + (System.currentTimeMillis() - startTime));
        //System.out.println("============================================================================================");
        return action;
    }

    /**
     * Resets the action back to empty and removes the current search/simulation 
     * @see MarioAIAgent#reset()
     */
    @Override
    public void reset()
    {
    	this.search = null;
        action = new boolean[Environment.numberOfKeys];
    }
}
