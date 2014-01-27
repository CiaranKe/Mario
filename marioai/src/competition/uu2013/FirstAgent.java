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
import competition.uu2013.common.Sprites.MarioSim;

public class FirstAgent extends BasicMarioAIAgent implements Agent
{

    private MarioSim marioSim;
    private int totalKills = 0;

    public FirstAgent()
    {
        super("FirstAgent");
        reset();
    }

    @Override
    public boolean [] getAction()
    {
        long startTime = System.currentTimeMillis();
        //--------------------------------------------------------------------------------------------------
        if (marioSim == null)
        {
            marioSim = new MarioSim(marioFloatPos[0],marioFloatPos[1], 0.0F, 0.0F);
            marioSim.syncLocation(marioFloatPos[0], marioFloatPos[1], isMarioAbleToJump, isMarioOnGround, isMarioAbleToShoot, (marioStatus == 1));
            Map.loadBehaviours();
        }
        else
        {
            action[Mario.KEY_SPEED] = action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
            action[Mario.KEY_RIGHT] = true;

            //System.out.println("Diff X: " + new BigDecimal(marioFloatPos[0] - marioSim.x ).toPlainString()
            //        + " Diff Y: " + new BigDecimal(marioFloatPos[1] - marioSim.y ).toPlainString());
            Map.setScene(this.levelScene, this.marioFloatPos[0], this.marioFloatPos[1]);
            Enemy.setEnemies(this.enemiesFloatPos, this.marioFloatPos[0], this.marioFloatPos[1]);
            //Map.moveEnemies(3);
            marioSim.syncLocation(marioFloatPos[0], marioFloatPos[1], isMarioAbleToJump, isMarioOnGround, isMarioAbleToShoot, (marioStatus == 1));
            marioSim.move(action, marioFloatPos[0], marioFloatPos[1]);
        }



        //--------------------------------------------------------------------------------------------------
        //System.out.println("Method Time: " + (System.currentTimeMillis() - startTime));
        return action;
    }


    @Override
    public void reset()
    {
        action = new boolean[Environment.numberOfKeys];
    }



}
