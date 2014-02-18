package competition.uu2013;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;
import competition.uu2013.common.hueristics.AStarSearch;
import competition.uu2013.common.hueristics.AStarSearchAttempt1;
import competition.uu2013.prototypes.Action;
import competition.uu2013.common.level.Enemy;
import competition.uu2013.common.level.Map;
import competition.uu2013.common.level.WorldSim;
import competition.uu2013.common.Sprites.MarioSim;

public class AStarAgent extends MarioAIAgent implements Agent
{
    private AStarSearch search;


    public AStarAgent()
    {
        super("AStarAgent");
        reset();
    }

    @Override
    public boolean [] getAction()
    {
        long startTime = System.currentTimeMillis();
        System.out.println("============================================================================================");
        //--------------------------------------------------------------------------------------------------
        if (search == null)
        {
            search = new AStarSearch(new WorldSim(new MarioSim(marioFloatPos[0],marioFloatPos[1], 0.0F, 3.0F), new Enemy(), new Map()));
            search.updateSim(marioFloatPos[0], marioFloatPos[1], isMarioAbleToJump, isMarioOnGround, isMarioAbleToShoot, (marioStatus == 1), this.enemiesFloatPos, this.levelScene, action);

        }
        else
        {
            System.out.println("Updating sim");
            search.updateSim(marioFloatPos[0], marioFloatPos[1], isMarioAbleToJump, isMarioOnGround, isMarioAbleToShoot, (marioStatus == 1), this.enemiesFloatPos, this.levelScene, action);
            action = search.pathFind(marioFloatPos[0],marioFloatPos[1],startTime);
            System.out.println("Completing: " + Action.nameAction(action));

        }
        //--------------------------------------------------------------------------------------------------
        System.out.println("Method Time: " + (System.currentTimeMillis() - startTime));
        System.out.println("============================================================================================");
        return action;
    }


    @Override
    public void reset()
    {

        action = new boolean[Environment.numberOfKeys];
    }



}
