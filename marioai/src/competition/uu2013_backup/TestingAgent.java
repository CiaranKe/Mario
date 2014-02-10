/*
 * Copyright (c) 2009-2010, Sergey Karakovskiy and Julian Togelius
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Mario AI nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package competition.uu2013;

import ch.idsia.agents.Agent;
import ch.idsia.agents.controllers.BasicMarioAIAgent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;
import competition.uu2013.common.Enemy;
import competition.uu2013.common.Map;
import competition.uu2013.common.MoveSim;
import competition.uu2013.common.Sprites.EnemySim;
import competition.uu2013.common.Sprites.MarioSim;

import java.math.BigDecimal;
import java.util.ArrayList;

public class TestingAgent extends MarioAIAgent implements Agent
{

    private MoveSim ms;
    private int totalKills = 0;
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
        long startTime = System.currentTimeMillis();
        //--------------------------------------------------------------------------------------------------
        if (ms == null)
        {
            MarioSim marioSim = new MarioSim(marioFloatPos[0],marioFloatPos[1], 0.0F, 0.0F);
            ms = new MoveSim(marioSim, Enemy.cloneEnemies());
            ms.syncLocation(marioFloatPos[0], marioFloatPos[1], isMarioAbleToJump, isMarioOnGround, isMarioAbleToShoot, (marioStatus == 1), Enemy.cloneEnemies());
        }
        else
        {

            //Update the world view.
            Map.setScene(this.levelScene, this.marioFloatPos[0], this.marioFloatPos[1]);
            Enemy.setEnemies(this.enemiesFloatPos, this.marioFloatPos[0], this.marioFloatPos[1], (this.levelScene[0].length / 2), (this.levelScene.length / 2));
            ms.syncLocation(marioFloatPos[0], marioFloatPos[1], isMarioAbleToJump, isMarioOnGround, isMarioAbleToShoot, (marioStatus == 1), Enemy.cloneEnemies());


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
            ArrayList<EnemySim> enemySims = ms.getEnemySims();

            float marioX = this.marioFloatPos[0];
            float marioY = this.marioFloatPos[1];
            int halfSceneWidth = (this.levelScene[0].length / 2);
            int halfSceneHeight = (this.levelScene.length / 2);

            float lookAHead = marioX + (halfSceneWidth * 16);
            float lookBelow = marioY + (halfSceneHeight * 16);
            float lookBehind = marioX - (halfSceneWidth * 16);
            float lookAbove = marioY - (halfSceneHeight * 16);

            System.out.println("Enemy Count " + (this.enemiesFloatPos.length/3) + " Simmed: " + Enemy.getCount());
            boolean matched = false;
            for (int  x = 0; x < this.enemiesFloatPos.length; x+=3 )
            {
                for (EnemySim sim : enemySims)
                {
                    if (sim.getX() == (marioX + enemiesFloatPos[x+1]) && sim.getY() == ( marioY + enemiesFloatPos[x+2]))
                    {
                        System.out.println("Matched: " + Enemy.nameEnemy(sim.getType()) + " @ " +sim.getX()+" : "+sim.getY());
                        matched = true;
                        break;
                    }
                }
                if (!matched)
                {
                    EnemySim sim = new EnemySim(( marioX + enemiesFloatPos[x+1]), ( marioY + enemiesFloatPos[x+2]), (int)enemiesFloatPos[x]);
                    System.out.println("Unmatched ("+ Enemy.withinScope(marioX, marioY, halfSceneWidth, halfSceneHeight, sim) +") : " + Enemy.nameEnemy(sim.getType()) + " @ " +sim.getX()+" : "+sim.getY() );
                    System.out.println("------------");
                }
            }




            action[Mario.KEY_SPEED] = action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
            action[Mario.KEY_RIGHT] = true;


            ms.move(action, marioFloatPos[0], marioFloatPos[1]);
            lastPredX = ms.getMarioLocation()[0];
            lastPredY = ms.getMarioLocation()[1];

            System.out.println("Predicted: Mario @ "  + lastPredX + " : " + lastPredY );
            for (EnemySim sim : ms.getEnemySims())
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
        Map.loadBehaviours();
        action = new boolean[Environment.numberOfKeys];
    }



}
