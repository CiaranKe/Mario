package competition.uu2013;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;
import competition.uu2013.common.Sprites.EnemySim;
import competition.uu2013.common.Sprites.SpriteSim;
import competition.uu2013.common.hueristics.AStarSearch;
import competition.uu2013.common.hueristics.AStarSearchAttempt1;
import competition.uu2013.prototypes.Action;
import competition.uu2013.common.level.Enemy;
import competition.uu2013.common.level.Map;
import competition.uu2013.common.level.WorldSim;
import competition.uu2013.common.Sprites.MarioSim;

import java.math.BigDecimal;
import java.util.ArrayList;

public class AStarAgent extends MarioAIAgent implements Agent
{
    private AStarSearch search;
    private int jumpCounter = 0;


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
            search.updateSim(marioFloatPos[0], marioFloatPos[1], isMarioAbleToJump, isMarioOnGround, isMarioAbleToShoot, marioStatus, this.enemiesFloatPos, this.levelScene, action);

        }
        else
        {
            for (int  x = 0; x < this.enemiesFloatPos.length; x+=3 )
            {
                EnemySim simN = new EnemySim(( marioFloatPos[0] + enemiesFloatPos[x+1]), ( marioFloatPos[0] + enemiesFloatPos[x+2]), (int)enemiesFloatPos[x]);
                System.out.println("Actual ("+ Enemy.withinScope(marioFloatPos[0], marioFloatPos[1], 11, 11, simN) +") : " + Enemy.nameEnemy(simN.getType()) + " @ " +simN.getX()+" : "+simN.getY() );
            }

            if (isMarioOnGround && isMarioAbleToJump)
            {
                jumpCounter = 7;
            }


            System.out.println("Updating sim");
            search.updateSim(marioFloatPos[0], marioFloatPos[1], isMarioAbleToJump, isMarioOnGround, isMarioAbleToShoot, marioStatus, this.enemiesFloatPos, this.levelScene, action);
            action = search.pathFind(marioFloatPos[0],marioFloatPos[1],startTime);
            if (action[Mario.KEY_JUMP])
            {
                jumpCounter--;
            }
            if (action[Mario.KEY_JUMP] && jumpCounter <= 0)
            {
                action[Mario.KEY_JUMP] = false;
            }
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
