package competition.uu2013;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;
import competition.uu2013.common.Enemy;
import competition.uu2013.common.Map;
import competition.uu2013.common.Sprites.SpriteSim;
import competition.uu2013.common.WorldSim;
import competition.uu2013.common.Sprites.EnemySim;
import competition.uu2013.common.Sprites.MarioSim;

import java.math.BigDecimal;
import java.util.ArrayList;

public class TestingAgent extends MarioAIAgent implements Agent
{

    private WorldSim worldSim;
    private float lastPredX = 32.0F;
    private float lastPredY = 35.0F;

    public TestingAgent()
    {
        super("TestingAgent");
        reset();
    }

    @Override
    public boolean [] getAction()
    {

        //TODO: Stomps are two turns ahead instead of one! WTF???
        //TODO: Fireballs
        //TODO: Hidden blocks, knock our positioning off.

        long startTime = System.currentTimeMillis();
        //--------------------------------------------------------------------------------------------------
        if (worldSim == null)
        {
            worldSim = new WorldSim(new MarioSim(marioFloatPos[0],marioFloatPos[1], 0.0F, 0.0F), new Enemy(), new Map());
            worldSim.syncLocation(marioFloatPos[0], marioFloatPos[1], isMarioAbleToJump, isMarioOnGround, isMarioAbleToShoot, (marioStatus == 1), this.enemiesFloatPos, this.levelScene);
        }
        else
        {
            //Update the world view.
            worldSim.syncLocation(marioFloatPos[0], marioFloatPos[1], isMarioAbleToJump, isMarioOnGround, isMarioAbleToShoot, (marioStatus == 1), this.enemiesFloatPos, this.levelScene);


            boolean synced = true;

            System.out.println("Diff X: " + new BigDecimal(marioFloatPos[0] - lastPredX ).toPlainString()
                    + " Diff Y: " + new BigDecimal(marioFloatPos[1] - lastPredY ).toPlainString());
            //TEST MARIO POSITION
            if ((Math.abs(marioFloatPos[0] - lastPredX ) > 1) || (Math.abs(marioFloatPos[1] - lastPredY ) > 1))
            {

                Map.printMap();
                Map.printScene(this.mergedObservation, "Merged");
                System.out.println("Diff X: " + new BigDecimal(marioFloatPos[0] - lastPredX ).toPlainString()
                        + " Diff Y: " + new BigDecimal(marioFloatPos[1] - lastPredY ).toPlainString());
                System.out.println("X: Actual: " + marioFloatPos[0] + " Predicted: " + lastPredX + " MAP: " + ((int)marioFloatPos[0]/16 ) );
                System.out.println("Y: Actual: " + marioFloatPos[1] + " Predicted: " + lastPredY + " MAP: " + ((int)marioFloatPos[1]/16 ) );
                System.out.println("----------");
                synced = false;
            }

            //TEST ENEMY POSITIONS
            ArrayList<SpriteSim> enemySims = worldSim.getEnemySims();

            float marioX = this.marioFloatPos[0];
            float marioY = this.marioFloatPos[1];
            int halfSceneWidth = (this.levelScene[0].length / 2);
            int halfSceneHeight = (this.levelScene.length / 2);

            System.out.println("Enemy Count " + (this.enemiesFloatPos.length/3) + " Simmed: " + worldSim.countEnemies());
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





            action[Mario.KEY_SPEED] = action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
            action[Mario.KEY_RIGHT] = true;


            worldSim.move(action, marioFloatPos[0], marioFloatPos[1]);
            lastPredX = worldSim.getMarioLocation()[0];
            lastPredY = worldSim.getMarioLocation()[1];


            System.out.println("Predicted: Mario @ "  + lastPredX + " : " + lastPredY );
            for (SpriteSim sim : worldSim.getEnemySims())
            {
                System.out.println("Predicted: " + Enemy.nameEnemy(sim.getType()) + " @ " +sim.getX()+" : "+sim.getY());
            }

            if (!synced)
            {
                System.out.println("sync failed");
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
