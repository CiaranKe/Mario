package competition.uu2013.prototypes;

import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;
import competition.uu2013.common.level.Map;
import competition.uu2013.common.level.WorldSim;

import java.util.ArrayList;

public class Action
{
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
    public static final int DUCK = 10;
    public static final int UP = 11;
    public static final int SHOOT = 12;
    public static final int WAIT = -1;
    public static final int ACTION_COUNT = 9;


    public static ArrayList<boolean[]> getPossibleActions(WorldSim sim)
    {
        ArrayList<boolean[]> actionList = new ArrayList<boolean[]>();


        if (sim.getMarioSim().mayJump() || sim.getMarioSim().getJumpTime() > 0)
        {
            actionList.add(createAction(Action.JUMP));
            actionList.add(createAction(Action.JUMP_SPEED));
            actionList.add(createAction(Action.LEFT_JUMP));
            actionList.add(createAction(Action.LEFT_JUMP_SPEED));
            actionList.add(createAction(Action.RIGHT_JUMP));
            actionList.add(createAction(Action.RIGHT_JUMP_SPEED));
        }

        actionList.add(createAction(Action.RIGHT));
        actionList.add(createAction(Action.RIGHT_SPEED));
        actionList.add(createAction(Action.LEFT));
        actionList.add(createAction(Action.LEFT_SPEED));
        actionList.add(createAction(Action.WAIT));
        return actionList;
    }



    public static boolean[] createAction(int action)
    {
        boolean [] newAction = new boolean[Environment.numberOfKeys];
        switch (action)
        {
            case JUMP:
                newAction[Mario.KEY_JUMP] = true;
                newAction[Mario.KEY_LEFT] = false;
                newAction[Mario.KEY_RIGHT] = false;
                newAction[Mario.KEY_SPEED] = false;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                break;
            case JUMP_SPEED:
                newAction[Mario.KEY_JUMP] = true;
                newAction[Mario.KEY_LEFT] = false;
                newAction[Mario.KEY_RIGHT] = false;
                newAction[Mario.KEY_SPEED] = true;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                break;
            case RIGHT:
                newAction[Mario.KEY_JUMP] = false;
                newAction[Mario.KEY_LEFT] = false;
                newAction[Mario.KEY_RIGHT] = true;
                newAction[Mario.KEY_SPEED] = false;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                break;
            case RIGHT_SPEED:
                newAction[Mario.KEY_JUMP] = false;
                newAction[Mario.KEY_LEFT] = false;
                newAction[Mario.KEY_RIGHT] = true;
                newAction[Mario.KEY_SPEED] = true;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                break;
            case RIGHT_JUMP:
                newAction[Mario.KEY_JUMP] = true;
                newAction[Mario.KEY_LEFT] = false;
                newAction[Mario.KEY_RIGHT] = true;
                newAction[Mario.KEY_SPEED] = false;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                break;
            case RIGHT_JUMP_SPEED:
                newAction[Mario.KEY_JUMP] = true;
                newAction[Mario.KEY_LEFT] = false;
                newAction[Mario.KEY_RIGHT] = true;
                newAction[Mario.KEY_SPEED] = true;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                break;
            case LEFT:
                newAction[Mario.KEY_JUMP] = false;
                newAction[Mario.KEY_LEFT] = true;
                newAction[Mario.KEY_RIGHT] = false;
                newAction[Mario.KEY_SPEED] = false;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                break;
            case LEFT_SPEED:
                newAction[Mario.KEY_JUMP] = false;
                newAction[Mario.KEY_LEFT] = true;
                newAction[Mario.KEY_RIGHT] = false;
                newAction[Mario.KEY_SPEED] = true;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                break;
            case LEFT_JUMP:
                newAction[Mario.KEY_JUMP] = true;
                newAction[Mario.KEY_LEFT] = true;
                newAction[Mario.KEY_RIGHT] = false;
                newAction[Mario.KEY_SPEED] = false;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                break;
            case LEFT_JUMP_SPEED:
                newAction[Mario.KEY_JUMP] = true;
                newAction[Mario.KEY_LEFT] = true;
                newAction[Mario.KEY_RIGHT] = false;
                newAction[Mario.KEY_SPEED] = true;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                break;
            case DUCK:
                newAction[Mario.KEY_JUMP] = false;
                newAction[Mario.KEY_LEFT] = false;
                newAction[Mario.KEY_RIGHT] = false;
                newAction[Mario.KEY_SPEED] = false;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = true;
                break;
            case UP:
                newAction[Mario.KEY_JUMP] = false;
                newAction[Mario.KEY_LEFT] = false;
                newAction[Mario.KEY_RIGHT] = false;
                newAction[Mario.KEY_SPEED] = false;
                newAction[Mario.KEY_UP] = true;
                newAction[Mario.KEY_DOWN] = false;
                break;
            case SHOOT:
                newAction[Mario.KEY_JUMP] = false;
                newAction[Mario.KEY_LEFT] = false;
                newAction[Mario.KEY_RIGHT] = false;
                newAction[Mario.KEY_SPEED] = true;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                break;
            default:
                newAction[Mario.KEY_JUMP] = false;
                newAction[Mario.KEY_LEFT] = false;
                newAction[Mario.KEY_RIGHT] = false;
                newAction[Mario.KEY_SPEED] = false;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                break;

        }
        //System.out.println("Generated: " + nameAction(newAction));
    return newAction;
    }

    public static String nameAction(boolean[] _action)
    {
        String value ="";

        if (_action[Mario.KEY_JUMP] == true && _action[Mario.KEY_LEFT] == false && _action[Mario.KEY_RIGHT] == false && _action[Mario.KEY_SPEED] == false && _action[Mario.KEY_UP] == false && _action[Mario.KEY_DOWN] == false)
        {
            value = "JUMP";
        }
        else if (_action[Mario.KEY_JUMP] == true && _action[Mario.KEY_LEFT] == false && _action[Mario.KEY_RIGHT] == false && _action[Mario.KEY_SPEED] == true && _action[Mario.KEY_UP] == false && _action[Mario.KEY_DOWN] == false)
        {
            value = "JUMP_SPEED";
        }
        else if (_action[Mario.KEY_JUMP] == false && _action[Mario.KEY_LEFT] == false && _action[Mario.KEY_RIGHT] == true && _action[Mario.KEY_SPEED] == false && _action[Mario.KEY_UP] == false && _action[Mario.KEY_DOWN] == false)
        {
            value = "RIGHT";
        }
        else if (_action[Mario.KEY_JUMP] == false && _action[Mario.KEY_LEFT] == false && _action[Mario.KEY_RIGHT] == true && _action[Mario.KEY_SPEED] == true && _action[Mario.KEY_UP] == false && _action[Mario.KEY_DOWN] == false)
        {
            value = "RIGHT_SPEED";
        }
        else if (_action[Mario.KEY_JUMP] == true && _action[Mario.KEY_LEFT] == false && _action[Mario.KEY_RIGHT] == true && _action[Mario.KEY_SPEED] == false && _action[Mario.KEY_UP] == false && _action[Mario.KEY_DOWN] == false)
        {
            value = "RIGHT_JUMP";
        }
        else if (_action[Mario.KEY_JUMP] == true && _action[Mario.KEY_LEFT] == false && _action[Mario.KEY_RIGHT] == true && _action[Mario.KEY_SPEED] == true && _action[Mario.KEY_UP] == false && _action[Mario.KEY_DOWN] == false)
        {
            value = "RIGHT_JUMP_SPEED";
        }
        else if (_action[Mario.KEY_JUMP] == false && _action[Mario.KEY_LEFT] == true && _action[Mario.KEY_RIGHT] == false && _action[Mario.KEY_SPEED] == false && _action[Mario.KEY_UP] == false && _action[Mario.KEY_DOWN] == false)
        {
            value = "LEFT";
        }
        else if (_action[Mario.KEY_JUMP] == false && _action[Mario.KEY_LEFT] == true && _action[Mario.KEY_RIGHT] == false && _action[Mario.KEY_SPEED] == true && _action[Mario.KEY_UP] == false && _action[Mario.KEY_DOWN] == false)
        {
            value = "LEFT_SPEED";
        }
        else if (_action[Mario.KEY_JUMP] == true && _action[Mario.KEY_LEFT] == true && _action[Mario.KEY_RIGHT] == false && _action[Mario.KEY_SPEED] == false && _action[Mario.KEY_UP] == false && _action[Mario.KEY_DOWN] == false)
        {
            value = "LEFT_JUMP";
        }
        else if (_action[Mario.KEY_JUMP] == true && _action[Mario.KEY_LEFT] == true && _action[Mario.KEY_RIGHT] == false && _action[Mario.KEY_SPEED] == true && _action[Mario.KEY_UP] == false && _action[Mario.KEY_DOWN] == false)
        {
            value = "LEFT_JUMP_SPEED";
        }

        else
        {
            value = "WAIT";
        }
        if (value.length() > 15)
        {
            return value;
        }
        else
        {
            return String.format("%-16s", value);
        }
    }
}
