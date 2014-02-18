package competition.uu2013.prototypes;

import ch.idsia.benchmark.mario.engine.GeneralizerLevelScene;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;

import java.io.*;
import java.util.ArrayList;


public class MapNode implements Serializable
{
    //------------------ STATIC -------------------------------------------------------------------//

    private static MapNode[][] nodeArray;
    private static int NodeID;
    private static int NODEARRRAY_LENGTH = 4096;
    private static int NODEARRRAY_HEIGHT = 250;
    private transient static int jumpCounter = 0;

    public static boolean nullArray()
    {
        return (nodeArray == null);
    }

    public static void initNodeArray()
    {
        NodeID = 0;

        nodeArray = new MapNode[NODEARRRAY_LENGTH][NODEARRRAY_HEIGHT];
    }

    public static void printNodeArray()
    {
        //System.out.println("--------------------------------------------------------------------------------------------------");
        for (int y = 0; y < NODEARRRAY_HEIGHT; y++)
        {
            for (int x = 0; x <NODEARRRAY_LENGTH ; x++)
            {
                if (nodeArray[x][y] != null)
                {
                    //System.out.print(nodeArray[x][y].countActions() + "\t");

                }
                else
                {
                    //System.out.print("N\t");
                }
            }
            System.out.println();
        }
        //System.out.println();
        //System.out.println("--------------------------------------------------------------------------------------------------");
        //System.out.println();
    }

    public static MapNode getNode(int x, int y, byte[][] _scene, int direction)
    {
        //System.out.println("Requesting Node: " + x + ":" +y);
        if (nodeArray[x][y] != null)
        {
            if (nodeArray[x][y].hasScene(direction, _scene))
            {
                //System.out.println("Node has scene");
                return nodeArray[x][y];
            }
            else
            {
                //System.out.println("No Scene, adding action");
                nodeArray[x][y].addAction(direction, _scene);
                return nodeArray[x][y];
            }
        }
        else
        {

            //System.out.println("Null Node, adding");
            nodeArray[x][y] = new MapNode(direction,_scene, x,y, NodeID);
            NodeID++;
            return nodeArray[x][y];
        }
    }

    public static void loadArray()
    {
        try
        {
            FileInputStream fis = new FileInputStream(new File("G:\\t2\\nodeArray.ser"));
            ObjectInputStream oos = new ObjectInputStream(fis);

            nodeArray = (MapNode[][]) oos.readObject();
            MapNode.printNodeArray();
        }
        catch (FileNotFoundException fnf)
        {
            MapNode.initNodeArray();
            //System.out.println("Couldn't find it");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void storeArray()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(new File("G:\\t2\\nodeArray.ser"));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(nodeArray);
            oos.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //----------------- INSTANCE ---------------------------------------------------------------------//


    private ArrayList<ActionNode> actionNodeList;
    private ActionNode lastActionNode;
    private int [] location;
    private int ID;
    private int stuckCounter;


    public int countActions()
    {
        if (actionNodeList == null)
        {
            return 0;
        }
        return actionNodeList.size();
    }

    public MapNode(int _direction, byte[][] _scene, int x, int y, int _newID)
    {
        this.location = new int[2];
        this.location[0] = x;
        this.location[1] = y;
        this.ID = _newID;

        if(actionNodeList == null)
        {
            actionNodeList = new ArrayList<ActionNode>();
            actionNodeList.add(new ActionNode(_direction, _scene));
        }
    }

    public int getID()
    {
        return this.ID;
    }

    public int [] getLocation()
    {
        return this.location;
    }

    public boolean[] getNextAction(byte[][] _scene, int direction, boolean canJump)
    {
        int count = 0;
        boolean[] nextAction = new boolean[Environment.numberOfKeys];


        for (ActionNode a : actionNodeList)
        {
            //System.out.println("Looking for action!");
            if (a.getDirection() == direction && a.compareScene(_scene))
            {
                //System.out.println("found existing action!");
                nextAction = a.getBestAction(_scene);
                lastActionNode = a;
                count++;
                //return nextAction;
            }
        }

        if (count > 1)
        {
            //System.out.println("Found " + count + " actions");
        }
        if (nextAction[Mario.KEY_JUMP] == true)
        {
            jumpCounter++;
        }
        if (jumpCounter > 16)
        {
            nextAction[Mario.KEY_JUMP] = false;
            jumpCounter =0;
        }

        //System.out.println("CanJump: " + canJump + " JumpCounter: " + jumpCounter);
        return nextAction;
    }

    public void setScore(Environment environment, float lastX, float lastY, int lastMode, int[] child)
    {
        int stuckPenalty=0;
        /*
        float oldScore = -((last.getEvaluationInfo().flowersDevoured * 64) + (last.getMarioStatus() * 32) +
                (last.getEvaluationInfo().mushroomsDevoured * 58) +
                (last.getEvaluationInfo().coinsGained * 16) +
                (last.getEvaluationInfo().hiddenBlocksFound * 24) +
                (last.getKillsTotal() * 42) + (last.getKillsByStomp() * 12) +
                (last.getKillsByFire() * 4) + (last.getKillsByShell() * 17) +
                (last.getEvaluationInfo().timeLeft * 8));

        float newscore = -((environment.getEvaluationInfo().flowersDevoured * 64) + (environment.getMarioStatus() * 32) +
                (environment.getEvaluationInfo().mushroomsDevoured * 58) +
                (environment.getEvaluationInfo().coinsGained * 16) +
                (environment.getEvaluationInfo().hiddenBlocksFound * 24) +
                (environment.getKillsTotal() * 42) + (environment.getKillsByStomp() * 12) +
                (environment.getKillsByFire() * 4) + (environment.getKillsByShell() * 17) +
                (environment.getEvaluationInfo().timeLeft * 8));
        */

        if ((Math.abs(environment.getMarioFloatPos()[0] - lastX) < 1.21F) && (environment.getMarioFloatPos()[1] == lastY))
        {
            //System.out.println("STTTTTTUUUUUUUUUUUUUUCCCCCCCKKKKK!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            stuckCounter++;
        }
        if (stuckCounter > 5)
        {
            stuckCounter = 0;
            stuckPenalty = -30;
        }

        float xScore = (environment.getMarioFloatPos()[0]*2) - lastX;
        float yScore = -(environment.getMarioFloatPos()[1])- -(lastY*2);
        //System.out.println("Y: " + environment.getMarioFloatPos()[1] + " last Y: " + lastY);
        //System.out.println("X: " + environment.getMarioFloatPos()[0] + " last Y: " + lastX);
        //System.out.println("Mode: " + (environment.getMarioMode()*32) + " last Mode: " + (lastMode*32));
        float mode = (environment.getMarioMode() * 32) - (lastMode * 32);
        float newScore = (xScore*2) + yScore + mode;
        lastActionNode.setScore(newScore+stuckPenalty, child);
    }

    public void addAction(int _direction, byte[][] _scene)
    {
        actionNodeList.add(new ActionNode(_direction, _scene));
    }

    public boolean hasChildren()
    {
        boolean children = false;

        for (ActionNode a : actionNodeList)
        {
            if(a.hasChild())
            {
                children = true;
            }
        }
        return children;
    }




    private boolean hasScene(int direction, byte[][] _scene)
    {
        boolean hasScene = false;
        for(ActionNode a: actionNodeList)
        {
           if ((a.getDirection() == direction ) && a.compareScene(_scene))
            {

                hasScene = true;
            }
        }
        return hasScene;
    }

    public void getLocations(byte[][] _scene)
    {
        //System.out.println("Node ("+ this.getID()  +  "): " + this.getLocation()[0] + ":" + this.getLocation()[1]);

        for (ActionNode a : actionNodeList)
        {
            //System.out.println("Direction: " + a.getDirection());
            int  [][] children = a.getChildren();
            boolean [][] actions = a.getActions();
            float [] scores = a.getScores();

            if (a.compareScene(_scene))
            {
                for (int x = 0; x < ActionNode.ACTION_COUNT; x++)
                {
                    //System.out.print("ActionNode " + ActionNode.nameAction(actions[x]));
                    if (children[x] != null)
                    {
                        //System.out.print(" Leads to (" + children[x].getID() + "): " + children[x].getLocation()[0] + ":" + children[x].getLocation()[1] +"  Score: " + scores[x]);
                    }
                    else
                    {
                        //System.out.print(" has not been tried");
                    }
                    //System.out.println();
                }
            }
        }
    }
}

class ActionNode implements Serializable
{

    private static final int RIGHT = 0;
    private static final int RIGHT_SPEED = 1;
    private static final int RIGHT_JUMP = 2;
    private static final int RIGHT_JUMP_SPEED = 3;
    private static final int LEFT = 4;
    private static final int LEFT_SPEED = 5;
    private static final int LEFT_JUMP = 6;
    public static final int LEFT_JUMP_SPEED = 7;
    public static final int ACTION_COUNT = 8;

    private int lastAction;
    private boolean [][] action;
    private int [][] actionChildLocation;
    private float[] score;
    private int direction;
    private byte[][] scene;

    public ActionNode(int _direction, byte[][] _scene)
    {
        this.scene = _scene;
        this.direction = _direction;
        this.score = new float[ACTION_COUNT];
        this.actionChildLocation = new int[ACTION_COUNT][];
        this.action = new boolean[ACTION_COUNT][];
        for (int x = 0; x < ACTION_COUNT; x++)
        {
            score[x] = 0;
            action[x] = createAction(x);
        }
    }

    public boolean hasChild()
    {
        for (int [] c : actionChildLocation)
        {
            if (c != null)
            {
                return true;
            }
        }
        return false;
    }

    public boolean[][] getActions()
    {
        return this.action;
    }

    public float [] getScores()
    {
        return this.score;
    }

    public void setScore(float _score, int[]  _child)
    {
        //System.out.println("Setting score: " + _score);
        this.score[lastAction] = _score;
        this.actionChildLocation[lastAction] = _child;
    }

    public int getDirection()
    {
        return this.direction;
    }

    public boolean compareScene(byte[][] newScene)
    {
        for (int x = 0; x < scene.length; x++)
        {
            for (int y = 0; y < scene[x].length; y++)
            {
                if ((int)scene[x][y] != (int)newScene[x][y])
                {
                    //System.out.println("Failed on:  " + scene[x][y] + " != " + newScene[x][y]);
                    return false;
                }
            }
        }
        return true;
    }

    public boolean[] getBestAction(byte [][] _scene)
    {
        for (int x = 11; x < 13; x++)
        {
            if (_scene[11][x] == GeneralizerLevelScene.UNBREAKABLE_BRICK ||
                    _scene[11][x] == GeneralizerLevelScene.BORDER_HILL ||
                    _scene[11][x] == GeneralizerLevelScene.BORDER_CANNOT_PASS_THROUGH ||
                    _scene[11][x] == GeneralizerLevelScene.BREAKABLE_BRICK ||
                    _scene[11][x] == GeneralizerLevelScene.BRICK ||
                    _scene[11][x] == GeneralizerLevelScene.CANNON_MUZZLE ||
                    _scene[11][x] == GeneralizerLevelScene.CANNON_TRUNK ||
                    _scene[11][x] == GeneralizerLevelScene.FLOWER_POT ||
                    _scene[11][x] == GeneralizerLevelScene.FLOWER_POT_OR_CANNON )
            {
                    lastAction = ActionNode.RIGHT_JUMP;
                    return ActionNode.createAction(ActionNode.RIGHT_JUMP);
            }
        }
        float bestScore = -1;
        boolean [] nextAction = new boolean[Environment.numberOfKeys];

        //System.out.println("----------------------------CHOOSING--------------------------------------------------------------");
        for (int x = 0; x < ACTION_COUNT; x++)
        {
            if (actionChildLocation[x] == null)
            {
                lastAction = x;
                //System.out.println("ActionNode: " + ActionNode.nameAction(action[lastActionNode]) + "Not tried" );
                nextAction =  action[x];
                break;
            }
            else
            {
                if (score[x] > bestScore)
                {
                    bestScore = score[x];
                    lastAction = x;
                    nextAction = action[x];
                    //System.out.println(ActionNode.nameAction(action[x]) +": Score is: " + score[x] + "SELECTED");
                }
                else
                {
                    //System.out.println(ActionNode.nameAction(action[x]) +": Score is: " + score[x]  + " less than :" + bestScore);
                }
            }
        }
        //System.out.println("--------- SELECTED " + ActionNode.nameAction(nextAction) + "--------------------------------------" );
        return new boolean[] { nextAction[0],nextAction[1],nextAction[2],nextAction[3],nextAction[4],nextAction[5] };
    }

    public static boolean[] createAction(int actionID)
    {
        boolean [] newAction = new boolean[Environment.numberOfKeys];
        switch (actionID)
        {
            //case JUMP:
            //    newAction[Mario.KEY_JUMP] = true;
            //    newAction[Mario.KEY_LEFT] = false;
            //    newAction[Mario.KEY_RIGHT] = false;
            //    newAction[Mario.KEY_SPEED] = false;
            //    newAction[Mario.KEY_UP] = false;
            //    newAction[Mario.KEY_DOWN] = false;
                //System.out.println("Creating: JUMP");
            //    return newAction;
            //case JUMP_SPEED:
            //    newAction[Mario.KEY_JUMP] = true;
            //    newAction[Mario.KEY_LEFT] = false;
            //    newAction[Mario.KEY_RIGHT] = false;
            //    newAction[Mario.KEY_SPEED] = true;
            //    newAction[Mario.KEY_UP] = false;
            //    newAction[Mario.KEY_DOWN] = false;
            //    //System.out.println("Creating: JUMP_SPEED");
            //    return newAction;
            case RIGHT:
                newAction[Mario.KEY_JUMP] = false;
                newAction[Mario.KEY_LEFT] = false;
                newAction[Mario.KEY_RIGHT] = true;
                newAction[Mario.KEY_SPEED] = false;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                //System.out.println("Creating: RIGHT");
                return newAction;
            case RIGHT_SPEED:
                newAction[Mario.KEY_JUMP] = false;
                newAction[Mario.KEY_LEFT] = false;
                newAction[Mario.KEY_RIGHT] = true;
                newAction[Mario.KEY_SPEED] = true;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                //System.out.println("Creating: RIGHT_SPEED");
                return newAction;
            case RIGHT_JUMP:
                newAction[Mario.KEY_JUMP] = true;
                newAction[Mario.KEY_LEFT] = false;
                newAction[Mario.KEY_RIGHT] = true;
                newAction[Mario.KEY_SPEED] = false;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                //System.out.println("Creating: RIGHT_JUMP");
                return newAction;
            case RIGHT_JUMP_SPEED:
                newAction[Mario.KEY_JUMP] = true;
                newAction[Mario.KEY_LEFT] = false;
                newAction[Mario.KEY_RIGHT] = true;
                newAction[Mario.KEY_SPEED] = true;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                //System.out.println("Creating: RIGHT_JUMP_SPEED");
                return newAction;
            case LEFT:
                newAction[Mario.KEY_JUMP] = false;
                newAction[Mario.KEY_LEFT] = true;
                newAction[Mario.KEY_RIGHT] = false;
                newAction[Mario.KEY_SPEED] = false;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                //System.out.println("Creating: LEFT");
                return newAction;
            case LEFT_SPEED:
                newAction[Mario.KEY_JUMP] = false;
                newAction[Mario.KEY_LEFT] = true;
                newAction[Mario.KEY_RIGHT] = false;
                newAction[Mario.KEY_SPEED] = true;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                //System.out.println("Creating: LEFT_SPEED");
                return newAction;
            case LEFT_JUMP:
                newAction[Mario.KEY_JUMP] = true;
                newAction[Mario.KEY_LEFT] = true;
                newAction[Mario.KEY_RIGHT] = false;
                newAction[Mario.KEY_SPEED] = false;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                //System.out.println("Creating: LEFT_JUMP");
                return newAction;
            case LEFT_JUMP_SPEED:
                newAction[Mario.KEY_JUMP] = true;
                newAction[Mario.KEY_LEFT] = true;
                newAction[Mario.KEY_RIGHT] = false;
                newAction[Mario.KEY_SPEED] = true;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                //System.out.println("Creating: LEFT_JUMP_SPEED");
                return newAction;

        }
        return newAction;
    }

    public void setLastAction(int _action)
    {
        this.lastAction = _action;
    }

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

    public int[][] getChildren()
    {
        return actionChildLocation;
    }
}
