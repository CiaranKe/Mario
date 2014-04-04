package competition.uu2013.common.level;

import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;

import java.util.ArrayList;

/**
 * Action class, generates and names actions for Mario
 */
public class Action
{
	/* Action Identifiers */
    public static final int JUMP = 0;
    public static final int JUMP_SPEED = 1;
    public static final int RIGHT = 2;
    public static final int RIGHT_SPEED = 3;
    public static final int RIGHT_JUMP = 4;
    public static final int RIGHT_JUMP_SPEED = 5;
    public static final int LEFT = 6;
    public static final int LEFT_SPEED = 7;
    public static final int LEFT_JUMP = 8;
    public static final int LEFT_JUMP_SPEED = 9;
    public static final int ACTION_COUNT = 9;

    /**
     * Returns an ArrayList of available actions for Mario based on the current Map information
     *
     * @param mayJump is MArio able to jump
     * @param marioX Mario's X location
     * @param marioY Mario's Y location
     * @param map the current map
     * @return the list of possible actions
     */
    public static ArrayList<boolean[]> getPossibleActions(boolean mayJump, float marioX, float marioY, Map map)
    {
        ArrayList<boolean[]> actionList  = new ArrayList<boolean[]>();

        if (mayJump)
        {
            actionList.add(createAction(Action.JUMP));
            actionList.add(createAction(Action.JUMP_SPEED));
            actionList.add(createAction(Action.LEFT_JUMP));
            actionList.add(createAction(Action.LEFT_JUMP_SPEED));
            actionList.add(createAction(Action.RIGHT_JUMP));
            actionList.add(createAction(Action.RIGHT_JUMP_SPEED));
        }
        if (map.getViewAt(marioX + Map.CELL_SIZE, marioY) == 0)
        {
            actionList.add(createAction(Action.RIGHT));
            actionList.add(createAction(Action.RIGHT_SPEED));
        }
        if (map.getViewAt(marioX - Map.CELL_SIZE, marioY) == 0)
        {
            actionList.add(createAction(Action.LEFT));
            actionList.add(createAction(Action.LEFT_SPEED));
        }
        return actionList;
    }


    /**
     * Creates an action based on the passed in id
     *
     * @param action the id of the action to create
     * @return the boolean[] the action
     */
    private static boolean[] createAction(int action)
    {
        boolean [] newAction = new boolean[Environment.numberOfKeys];
        switch (action)
        {
            case JUMP:
                newAction[Mario.KEY_JUMP] = true; newAction[Mario.KEY_LEFT] = false; 
                newAction[Mario.KEY_RIGHT] = false; newAction[Mario.KEY_SPEED] = false;
                newAction[Mario.KEY_UP] = false; newAction[Mario.KEY_DOWN] = false;
                break;
            case JUMP_SPEED:
                newAction[Mario.KEY_JUMP] = true; newAction[Mario.KEY_LEFT] = false; 
                newAction[Mario.KEY_RIGHT] = false; newAction[Mario.KEY_SPEED] = true;
                newAction[Mario.KEY_UP] = false; newAction[Mario.KEY_DOWN] = false; 
                break;
            case RIGHT:
                newAction[Mario.KEY_JUMP] = false; newAction[Mario.KEY_LEFT] = false;
                newAction[Mario.KEY_RIGHT] = true; newAction[Mario.KEY_SPEED] = false;
                newAction[Mario.KEY_UP] = false; newAction[Mario.KEY_DOWN] = false;
                break;
            case RIGHT_SPEED:
                newAction[Mario.KEY_JUMP] = false; newAction[Mario.KEY_LEFT] = false;
                newAction[Mario.KEY_RIGHT] = true; newAction[Mario.KEY_SPEED] = true;
                newAction[Mario.KEY_UP] = false; newAction[Mario.KEY_DOWN] = false;
                break;
            case RIGHT_JUMP:
                newAction[Mario.KEY_JUMP] = true; newAction[Mario.KEY_LEFT] = false;
                newAction[Mario.KEY_RIGHT] = true; newAction[Mario.KEY_SPEED] = false;
                newAction[Mario.KEY_UP] = false; newAction[Mario.KEY_DOWN] = false;
                break;
            case RIGHT_JUMP_SPEED:
                newAction[Mario.KEY_JUMP] = true; newAction[Mario.KEY_LEFT] = false;
                newAction[Mario.KEY_RIGHT] = true; newAction[Mario.KEY_SPEED] = true;
                newAction[Mario.KEY_UP] = false; newAction[Mario.KEY_DOWN] = false;
                break;
            case LEFT:
                newAction[Mario.KEY_JUMP] = false; newAction[Mario.KEY_LEFT] = true;
                newAction[Mario.KEY_RIGHT] = false; newAction[Mario.KEY_SPEED] = false;
                newAction[Mario.KEY_UP] = false; newAction[Mario.KEY_DOWN] = false;
                break;
            case LEFT_SPEED:
                newAction[Mario.KEY_JUMP] = false; newAction[Mario.KEY_LEFT] = true;
                newAction[Mario.KEY_RIGHT] = false; newAction[Mario.KEY_SPEED] = true;
                newAction[Mario.KEY_UP] = false; newAction[Mario.KEY_DOWN] = false;
                break;
            case LEFT_JUMP:
                newAction[Mario.KEY_JUMP] = true; newAction[Mario.KEY_LEFT] = true;
                newAction[Mario.KEY_RIGHT] = false; newAction[Mario.KEY_SPEED] = false;
                newAction[Mario.KEY_UP] = false; newAction[Mario.KEY_DOWN] = false;
                break;
            case LEFT_JUMP_SPEED:
                newAction[Mario.KEY_JUMP] = true; newAction[Mario.KEY_LEFT] = true;
                newAction[Mario.KEY_RIGHT] = false; newAction[Mario.KEY_SPEED] = true;
                newAction[Mario.KEY_UP] = false; newAction[Mario.KEY_DOWN] = false;
                break;
        }
        //System.out.println("Generated: " + nameAction(newAction));
        return newAction;
    }

    /**
     * Names an action based on it's current values.
     *
     * @param _action the action array
     * @return the string the name of that action
     */
    public static String nameAction(boolean[] _action)
    {
        String value ="";

        if (_action[Mario.KEY_JUMP] == true && _action[Mario.KEY_LEFT] == false && _action[Mario.KEY_RIGHT] == false && _action[Mario.KEY_SPEED] == false && _action[Mario.KEY_UP] == false && _action[Mario.KEY_DOWN] == false)
        {
            value = " JUMP";
        }
        if (_action[Mario.KEY_JUMP] == true && _action[Mario.KEY_LEFT] == false && _action[Mario.KEY_RIGHT] == false && _action[Mario.KEY_SPEED] == true && _action[Mario.KEY_UP] == false && _action[Mario.KEY_DOWN] == false)
        {
            value = " JUMP_SPEED";
        }
        if (_action[Mario.KEY_JUMP] == false && _action[Mario.KEY_LEFT] == false && _action[Mario.KEY_RIGHT] == true && _action[Mario.KEY_SPEED] == false && _action[Mario.KEY_UP] == false && _action[Mario.KEY_DOWN] == false)
        {
            value = " RIGHT";
        }
        if (_action[Mario.KEY_JUMP] == false && _action[Mario.KEY_LEFT] == false && _action[Mario.KEY_RIGHT] == true && _action[Mario.KEY_SPEED] == true && _action[Mario.KEY_UP] == false && _action[Mario.KEY_DOWN] == false)
        {
            value = " RIGHT_SPEED";
        }
        if (_action[Mario.KEY_JUMP] == true && _action[Mario.KEY_LEFT] == false && _action[Mario.KEY_RIGHT] == true && _action[Mario.KEY_SPEED] == false && _action[Mario.KEY_UP] == false && _action[Mario.KEY_DOWN] == false)
        {
            value = " RIGHT_JUMP";
        }
        if (_action[Mario.KEY_JUMP] == true && _action[Mario.KEY_LEFT] == false && _action[Mario.KEY_RIGHT] == true && _action[Mario.KEY_SPEED] == true && _action[Mario.KEY_UP] == false && _action[Mario.KEY_DOWN] == false)
        {
            value = " RIGHT_JUMP_SPEED";
        }
        if (_action[Mario.KEY_JUMP] == false && _action[Mario.KEY_LEFT] == true && _action[Mario.KEY_RIGHT] == false && _action[Mario.KEY_SPEED] == false && _action[Mario.KEY_UP] == false && _action[Mario.KEY_DOWN] == false)
        {
            value = " LEFT";
        }
        if (_action[Mario.KEY_JUMP] == false && _action[Mario.KEY_LEFT] == true && _action[Mario.KEY_RIGHT] == false && _action[Mario.KEY_SPEED] == true && _action[Mario.KEY_UP] == false && _action[Mario.KEY_DOWN] == false)
        {
            value = " LEFT_SPEED";
        }
        if (_action[Mario.KEY_JUMP] == true && _action[Mario.KEY_LEFT] == true && _action[Mario.KEY_RIGHT] == false && _action[Mario.KEY_SPEED] == false && _action[Mario.KEY_UP] == false && _action[Mario.KEY_DOWN] == false)
        {
            value = " LEFT_JUMP";
        }
        if (_action[Mario.KEY_JUMP] == true && _action[Mario.KEY_LEFT] == true && _action[Mario.KEY_RIGHT] == false && _action[Mario.KEY_SPEED] == true && _action[Mario.KEY_UP] == false && _action[Mario.KEY_DOWN] == false)
        {
            value = " LEFT_JUMP_SPEED";
        }
        return value;
    }
}

