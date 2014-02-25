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
            System.out.println("Updating sim");
            search.updateSim(marioFloatPos[0], marioFloatPos[1], isMarioAbleToJump, isMarioOnGround, isMarioAbleToShoot, marioStatus, this.enemiesFloatPos, this.levelScene, action);
            action = search.pathFind(marioFloatPos[0],marioFloatPos[1],startTime);
            System.out.println("Completing: " + Action.nameAction(action));



            System.out.println("Diff X: " + new BigDecimal(marioFloatPos[0] - search.getPredictedX() ).toPlainString()
                    + " Diff Y: " + new BigDecimal(marioFloatPos[1] - search.getPredictedY() ).toPlainString());
            System.out.println("X: Actual: " + marioFloatPos[0] + " Predicted: " + search.getPredictedX() + " MAP: " + ((int)marioFloatPos[0]/16 ) );
            System.out.println("Y: Actual: " + marioFloatPos[1] + " Predicted: " + search.getPredictedX() + " MAP: " + ((int)marioFloatPos[1]/16 ) );
            System.out.println("----------");

            try
            {
                ArrayList<SpriteSim> enemySims = search.getEnemySims();
                float marioX = this.marioFloatPos[0];
                float marioY = this.marioFloatPos[1];
                int halfSceneWidth = (this.levelScene[0].length / 2);
                int halfSceneHeight = (this.levelScene.length / 2);

                System.out.println("Enemy Count " + (this.enemiesFloatPos.length/3) + " Simmed: " + enemySims.size());
                boolean matched = false;

                System.out.println("------------");
                for (SpriteSim sim : enemySims)
                {
                    System.out.println("Simmed: " + Enemy.nameEnemy(sim.getType()) + " @ " +sim.getX()+" : "+sim.getY());
                }
                System.out.println("------------");
                for (int  x = 0; x < this.enemiesFloatPos.length; x+=3 )
                {
                    EnemySim simN = new EnemySim(( marioX + enemiesFloatPos[x+1]), ( marioY + enemiesFloatPos[x+2]), (int)enemiesFloatPos[x]);
                    System.out.println("Actual ("+ Enemy.withinScope(marioX, marioY, halfSceneWidth, halfSceneHeight, simN) +") : " + Enemy.nameEnemy(simN.getType()) + " @ " +simN.getX()+" : "+simN.getY() );
                }
                System.out.println("------------");

                System.out.println("Actual: " + "X: " + marioFloatPos[0] + " Y: " + marioFloatPos[1]);


                System.out.println("Predicted: Mario @ "  + search.getPredictedX() + " : " + search.getPredictedY() );
                for (SpriteSim sim : search.getEnemySims())
                {
                    System.out.println("Predicted: " + Enemy.nameEnemy(sim.getType()) + " @ " +sim.getX()+" : "+sim.getY());
                }
            }
            catch (CloneNotSupportedException e)
            {
                e.printStackTrace();
            }

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
