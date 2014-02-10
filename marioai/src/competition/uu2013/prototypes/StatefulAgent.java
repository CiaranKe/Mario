package competition.uu2013.prototypes;

import ch.idsia.agents.Agent;
import ch.idsia.agents.controllers.BasicMarioAIAgent;
import ch.idsia.benchmark.mario.engine.GeneralizerLevelScene;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.engine.sprites.Sprite;

/**
 * Created with IntelliJ IDEA.
 * User: fluffy
 * Date: 01/02/14
 * Time: 18:12
 * To change this template use File | Settings | File Templates.
 */
public class StatefulAgent extends BasicMarioAIAgent implements Agent
{
    private int jumpCounter;
    private int trueJumpCounter;
    private int speedCounter;
    private int defendCounter;
    private StateMachine sm;

    public StatefulAgent()
    {
        super("Stateful Agent");

        speedCounter = 0;
        trueJumpCounter = 0;
        defendCounter = 0;
        sm = new StateMachine(5);
    }

    public void reset()
    {
        action[Mario.KEY_SPEED] = false;
        action[Mario.KEY_RIGHT] = false;
        speedCounter = 0;
        trueJumpCounter = 0;
        defendCounter = 0;
    }


    public boolean[] getAction() {
        action[Mario.KEY_RIGHT] = false;
        action[Mario.KEY_LEFT] = false;
        action[Mario.KEY_JUMP] = false;
        action[Mario.KEY_SPEED] = false;

        sm.setNextState(mergedObservation, enemies);
        sm.getAction(mergedObservation, action, enemies);
        if (sm.getCurrentState() != sm.getLastState()) {
            System.out.println(sm.getCurrentState());
            System.out.println("JUMP?: " + isMarioAbleToJump);
        }

        return action;
    }

    class StateMachine
    {
        private int searchSensitivity;
        private int currentState;
        private int lastState;

        private int DEFEND = 1;
        private int LOOT = 2;
        private int RUN = 3;
        private int OBSTACLE = 4;
        private int GAP = 5;
        private int BACK = 6;
        private int SHOOT = 7;
        private int DEFEND_JUMP = 8;
        private int LOOT_BOX = 9;

        public StateMachine(int _sens)
        {
            currentState = RUN;
            lastState = 0;
            searchSensitivity = _sens;
        }


        public void setNextState(byte[][] mergedObservation, byte[][] enemies)
        {
            lastState = currentState;
            for (int x = 9; x < 11+searchSensitivity; x++)
            {
                if ((enemies[11][x] != 0 && enemies[11][x] != Sprite.KIND_COIN_ANIM) || (enemies[9][11] != 0 && enemies[9][11] != Sprite.KIND_COIN_ANIM))
                {
                    currentState = DEFEND;
                    return;
                }

            }
            for (int x = 8; x < 11; x++)
            {
                for (int y = 8; y < 13; y++)
                {
                    if (mergedObservation[x][y] == 21)
                    {
                        currentState = LOOT;
                        return;
                    }
                }
            }
            currentState = RUN;
        }

        public void getAction(byte[][] mergedObservation, boolean[] action, byte[][] enemies)
        {
            if(currentState == DEFEND)
            {
                defend(enemies, action);
                if(defendCounter > 5)
                {
                    defendCounter = 0;
                    action[Mario.KEY_LEFT] = false;
                }
            }
            else
            {
                if(currentState == LOOT)
                {
                    getCoins(mergedObservation, enemies);
                }
                else
                {
                    keepGoing(mergedObservation, action);
                }
            }
            if (trueJumpCounter > 16)
            {
                trueJumpCounter = 0;
                action[Mario.KEY_JUMP] = false;
            }
        }

        private void keepGoing(byte[][] mergedObservation, boolean[] action)
        {
            for (int x = 12; x < 12+searchSensitivity;x++)
            {
                if (mergedObservation[11][x] == GeneralizerLevelScene.UNBREAKABLE_BRICK ||
                        mergedObservation[11][x] == GeneralizerLevelScene.BORDER_HILL ||
                        mergedObservation[11][x] == GeneralizerLevelScene.BORDER_CANNOT_PASS_THROUGH ||
                        mergedObservation[11][x] == GeneralizerLevelScene.BREAKABLE_BRICK ||
                        mergedObservation[11][x] == GeneralizerLevelScene.BRICK ||
                        mergedObservation[11][x] == GeneralizerLevelScene.CANNON_MUZZLE ||
                        mergedObservation[11][x] == GeneralizerLevelScene.CANNON_TRUNK ||
                        mergedObservation[11][x] == GeneralizerLevelScene.FLOWER_POT ||
                        mergedObservation[11][x] == GeneralizerLevelScene.FLOWER_POT_OR_CANNON )
                {

                    currentState = OBSTACLE;
                    action[Mario.KEY_RIGHT] = true;
                    action[Mario.KEY_JUMP] = true;
                    trueJumpCounter++;
                    return;
                }
            }
            if (mergedObservation[12][12] == 0 && mergedObservation[13][12] == 0 &&
                    mergedObservation[12][13] == 0 && mergedObservation[13][13] == 0 &&
                    mergedObservation[12][14] == 0 && mergedObservation[13][14] == 0)
            {
                if(mergedObservation[12][15] != 0)
                {
                    currentState = GAP;
                    action[Mario.KEY_RIGHT] = true;
                    action[Mario.KEY_SPEED] = true;
                    speedCounter++;
                    action[Mario.KEY_JUMP] = true;
                    trueJumpCounter++;
                    return;
                }
            }

            if((mergedObservation[12][13] == 0 && mergedObservation[13][13] == 0 && mergedObservation[12][14]!= 0) && isMarioAbleToJump)
            {
                currentState = GAP;
                action[Mario.KEY_RIGHT] = true;
                action[Mario.KEY_JUMP] = true;
                trueJumpCounter++;
                return;
            }

            action[Mario.KEY_RIGHT] = true;
            currentState = RUN;
        }

        private void defend(byte[][] enemies, boolean[] action)
        {
            for (int x = 0; x < 21; x++)
            {
                if (enemies[x][11] !=0 && enemies[x][11] != Sprite.KIND_FIREBALL)
                {
                    action[Mario.KEY_LEFT] = true;
                    defendCounter++;
                    action[Mario.KEY_JUMP] = true;
                    trueJumpCounter++;
                    currentState = BACK;
                    return;
                }
            }

            for (int x = 8; x<14;x++ )
            {
                if((enemies[11][x] !=0 && enemies[11][x] != Sprite.KIND_FIREBALL) && Mario.fire)
                {
                    if (isMarioAbleToShoot)
                    {
                        action[Mario.KEY_SPEED] = true;
                        speedCounter++;
                        currentState = SHOOT;
                        return;
                    }
                }

                if(!Mario.fire && (enemies[11][x] != 0 && enemies[11][x] != Sprite.KIND_FIREBALL))
                {
                    action[Mario.KEY_JUMP] = true;
                    trueJumpCounter++;
                    action[Mario.KEY_RIGHT] = true;
                    currentState = DEFEND_JUMP;
                    return;
                }
            }
        }

        private void getCoins(byte[][] mergedObservation, byte[][] enemies)
        {
            for(int x = 8; x <= 11; x++)
            {
                for (int y = 0; y < 13; y++)
                {
                    if(mergedObservation[x][y] == 21)
                    {
                        currentState = LOOT_BOX;

                        if (y == 8 || y ==9 || y==10)
                        {
                            action[Mario.KEY_JUMP] = true;
                            trueJumpCounter++;
                        }
                        if(x<11)
                        {
                            action[Mario.KEY_LEFT] = true;
                            action[Mario.KEY_RIGHT] = false;
                        }
                        else
                        {
                            action[Mario.KEY_RIGHT] = true;
                            action[Mario.KEY_LEFT] = false;
                        }
                        return;
                    }
                }
            }
        }


        public int getCurrentState()
        {
            return currentState;
        }

        public int getLastState()
        {
            return lastState;
        }
    }

}
