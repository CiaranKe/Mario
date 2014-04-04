package competition.uu2013.prototypes;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.GeneralizerLevelScene;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.engine.sprites.Sprite;

/**
 *   Stateful Agent, selects actions for MArio based on which of the 8 identified states he is in.
 *
 *  @author Ciaran Kearney
 *  @version 1.0
 *  @since 15/01/2014
 */
public class StatefulAgent extends MarioAIAgent implements Agent
{

    /** jump counter. */
    private int jumpCounter;

    /** frames defending. */
    private int defenceCounter;

    /** The State Machine. */
    private StateMachine sm;

    /**
     * Instantiates a new stateful agent.
     */
    public StatefulAgent()
    {
        super("StatefulAgent");
        sm = new StateMachine(3);
    }

    /**
     * Called between levels. reverts to empty action
     * @see MarioAIAgent#reset()
     */
    public void reset()
    {
        action[Mario.KEY_SPEED] = false;
        action[Mario.KEY_RIGHT] = false;
        jumpCounter = 0;
    }


    /**
     * Returns the action for this agent
     * @see MarioAIAgent#getAction()
     */
    public boolean[] getAction()
    {
        //reset all actions to false
        action[Mario.KEY_RIGHT] = false;
        action[Mario.KEY_LEFT] = false;
        action[Mario.KEY_JUMP] = false;
        action[Mario.KEY_SPEED] = false;
        action[Mario.KEY_UP] = false;
        action[Mario.KEY_DOWN] = false;

        //get the next state
        sm.setNextState(mergedObservation, enemies);
        //get the text action
        sm.getAction(mergedObservation, action, enemies);
        return action;
    }

    /**
     * StateMachiue, tansitions Mario between one of eight states and selects
     * action based on state.
     */
    class StateMachine
    {

        /** The distance to look ahead */
        private final int LOOKAHEAD_DISTANCE;

        /** The current state. */
        private State cState;


        /**
         * Instantiates a new state machine.
         *
         * @param _sens the distance to look ahead
         */
        public StateMachine(int _sens)
        {
            cState = State.RUNNING;
            LOOKAHEAD_DISTANCE = _sens;
        }


        /**
         * Sets the next state.
         *
         * @param mergedObservation the merged observation
         * @param enemies the enemies observation
         */
        public void setNextState(byte[][] mergedObservation, byte[][] enemies)
        {
            //check for enemies ahead
            for (int x = (marioEgoCol-2); x < marioEgoCol+ LOOKAHEAD_DISTANCE; x++)
            {
                if ((enemies[marioEgoRow][x] != Sprite.KIND_NONE && enemies[marioEgoRow][x] != Sprite.KIND_COIN_ANIM) ||
                        (enemies[marioEgoRow-2][marioEgoCol] != Sprite.KIND_NONE
                                && enemies[marioEgoRow-2][marioEgoCol] != Sprite.KIND_COIN_ANIM))
                {
                    //if found, defend
                    cState = State.DEFENDING;
                    return;
                }

            }
            //otherwise check for coins
            for (int x = (marioEgoCol-3); x < marioEgoCol; x++)
            {
                for (int y = (marioEgoRow-3); y < (marioEgoRow+2); y++)
                {
                    if (mergedObservation[x][y] == Sprite.KIND_COIN_ANIM)
                    {
                        //if found, we are looting
                        cState = State.LOOTING;
                        return;
                    }
                }
            }
            //if no coins or enemies, keep moving
            cState = State.RUNNING;
        }

        /**
         * Selects Mario's action based on the current state.
         *
         * @param mergedObservation the merged observation
         * @param action the action array
         * @param enemies the enemies observation
         */
        public void getAction(byte[][] mergedObservation, boolean[] action, byte[][] enemies)
        {
            //if we're defending
            if(cState == State.DEFENDING)
            {
                //if we've been defending for move than 5 frames
                defend(enemies, action);
                if(defenceCounter > 5)
                {
                    //stop moving left
                    defenceCounter = 0;
                    action[Mario.KEY_LEFT] = false;
                }
            }
            else //we're not defending
            {
                //if we're looting
                if(cState == State.LOOTING)
                {
                    //select the best action for looting
                    getCoins(mergedObservation);
                }
                else
                {
                    //we're running
                    keepGoing(mergedObservation, action);
                }
            }
            //if we've been jumping too long
            if (jumpCounter > 16)
            {
                //stop pressing the jump button
                jumpCounter = 0;
                action[Mario.KEY_JUMP] = false;
            }
        }

        /**
         * Selects an action for the running state based on what's around us.
         *
         * @param mergedObservation the merged observation
         * @param action the action
         */
        private void keepGoing(byte[][] mergedObservation, boolean[] action)
        {

            for (int x = (marioEgoRow+1); x < (marioEgoRow+1)+ LOOKAHEAD_DISTANCE;x++)
            {
                //if there's an obstacle
                if (mergedObservation[marioEgoRow][x] == GeneralizerLevelScene.UNBREAKABLE_BRICK ||
                        mergedObservation[marioEgoRow][x] == GeneralizerLevelScene.BORDER_HILL ||
                        mergedObservation[marioEgoRow][x] == GeneralizerLevelScene.BORDER_CANNOT_PASS_THROUGH ||
                        mergedObservation[marioEgoRow][x] == GeneralizerLevelScene.BREAKABLE_BRICK ||
                        mergedObservation[marioEgoRow][x] == GeneralizerLevelScene.BRICK ||
                        mergedObservation[marioEgoRow][x] == GeneralizerLevelScene.CANNON_MUZZLE ||
                        mergedObservation[marioEgoRow][x] == GeneralizerLevelScene.CANNON_TRUNK ||
                        mergedObservation[marioEgoRow][x] == GeneralizerLevelScene.FLOWER_POT ||
                        mergedObservation[marioEgoRow][x] == GeneralizerLevelScene.FLOWER_POT_OR_CANNON )
                {

                    //jump
                    cState = State.OBSTACLE_FOUND;
                    action[Mario.KEY_RIGHT] = true;
                    action[Mario.KEY_JUMP] = true;
                    jumpCounter++;
                    return;
                }
            }
            //if there's clear air in front of us
            if (mergedObservation[marioEgoRow+1][marioEgoCol+1] == Sprite.KIND_NONE && mergedObservation[marioEgoRow+1][marioEgoCol+1] == Sprite.KIND_NONE &&
                    mergedObservation[marioEgoRow+1][marioEgoCol+2] == Sprite.KIND_NONE && mergedObservation[marioEgoRow+2][marioEgoCol+2] == Sprite.KIND_NONE &&
                    mergedObservation[marioEgoRow+1][marioEgoCol+3] == Sprite.KIND_NONE && mergedObservation[marioEgoRow+2][marioEgoCol+3] == Sprite.KIND_NONE)
            {
                //and a block 3 cells away
                if(mergedObservation[marioEgoRow+1][marioEgoCol+4] != Sprite.KIND_NONE)
                {
                    //we've found a gap
                    cState = State.GAP_FOUND;
                    action[Mario.KEY_RIGHT] = true;
                    action[Mario.KEY_SPEED] = true;
                    action[Mario.KEY_JUMP] = true;
                    jumpCounter++;
                    return;
                }
            }

            //2 cell gap, no need for speed
            if((mergedObservation[marioEgoRow+1][marioEgoCol+2] == Sprite.KIND_NONE && mergedObservation[marioEgoRow+2][marioEgoCol+2] == Sprite.KIND_NONE &&
                    mergedObservation[marioEgoRow+1][marioEgoCol+3]!= Sprite.KIND_NONE) && isMarioAbleToJump)
            {
                cState = State.GAP_FOUND;
                action[Mario.KEY_RIGHT] = true;
                action[Mario.KEY_JUMP] = true;
                jumpCounter++;
                return;
            }

            //no obstacles or gaps? just keep moving right
            action[Mario.KEY_RIGHT] = true;
            cState = State.RUNNING;
        }

        /**
         * Defend.
         *
         * @param enemies the enemies
         * @param action the action
         */
        private void defend(byte[][] enemies, boolean[] action)
        {
            //we're defending
            for (int x = 0; x < receptiveFieldWidth; x++)
            {
                //if there's an enemy ahead and it's not a fireball
                if (enemies[x][marioEgoCol] !=Sprite.KIND_NONE && enemies[x][marioEgoCol] != Sprite.KIND_FIREBALL)
                {
                    //run away
                    action[Mario.KEY_LEFT] = true;
                    defenceCounter++;
                    action[Mario.KEY_JUMP] = true;
                    jumpCounter++;
                    cState = State.GO_BACK;
                    return;
                }
            }

            for (int x = 8; x<14;x++ )
            {
                //enemy ahead and we can shoot??  KILL IT WITH FIRE!!!
                if((enemies[marioEgoRow][x] !=Sprite.KIND_NONE && enemies[marioEgoRow][x] != Sprite.KIND_FIREBALL) && Mario.fire)
                {
                    if (isMarioAbleToShoot)
                    {
                        action[Mario.KEY_SPEED] = true;
                        cState = State.SHOOTING;
                        return;
                    }
                }
                //can't shoot, but we can jump on it.
                if(!Mario.fire && (enemies[marioEgoRow][x] != Sprite.KIND_NONE && enemies[marioEgoRow][x] != Sprite.KIND_FIREBALL))
                {
                    action[Mario.KEY_JUMP] = true;
                    jumpCounter++;
                    action[Mario.KEY_RIGHT] = true;
                    cState = State.DEFEND_JUMP;
                    return;
                }
            }
        }

        /**
         *  Slect an action for the looting state
         *
         * @param mergedObservation the merged observation
         */
        private void getCoins(byte[][] mergedObservation)
        {
            for(int x = (marioEgoRow-3); x <= marioEgoRow; x++)
            {
                for (int y = 0; y < marioEgoCol+2; y++)
                {
                    if(mergedObservation[x][y] == Sprite.KIND_COIN_ANIM)
                    {

                        //block types?  above us?
                        if (y == 8 || y ==9 || y==10)
                        {
                            action[Mario.KEY_JUMP] = true;
                            jumpCounter++;
                        }
                        //tis behind us?
                        if(x<marioEgoRow)
                        {
                            action[Mario.KEY_LEFT] = true;
                            action[Mario.KEY_RIGHT] = false;
                        }
                        //I'm just gonna keep walking
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
    }

    /**
     * Enumeration of possible states for Mario
     */
    private enum State
    {
        DEFENDING,
        LOOTING,
        RUNNING,
        OBSTACLE_FOUND,
        GAP_FOUND,
        GO_BACK,
        SHOOTING,
        DEFEND_JUMP
    }

}
