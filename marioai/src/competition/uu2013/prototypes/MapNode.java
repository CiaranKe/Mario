package competition.uu2013.prototypes;

import ch.idsia.benchmark.mario.engine.GeneralizerLevelScene;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;

import java.io.*;
import java.util.ArrayList;


/**
 * The Class MapNode. contains both static and instance methods/properties.  Stores a collection of of actions 
 * and scores for those actions along with the current on screen activity.  Each MapNode can contain multiple
 * actions, scenes and directions allowing it to be used over multiple levels.
 * 
 * @author Ciaran Kearney
 * @version 1.0
 * @since 23/12/2013
 */
public class MapNode implements Serializable
{
    //------------------ STATIC METHODS AND VARIABLES -----------------------------------------------------------//

    /** Array of mapnodes that have been visted by the agent, 
     * these should eventually have a M:1 relationship to all cells on the level */
    private static MapNode[][] nodeArray;
    
    /** A counter for the nodes, each node has a unique ID */
    private static int NodeID = 0;
    
    /** The maximum length of the node array. */
    private static final int NODEARRRAY_LENGTH = 4096;
    
    /** The maximum height of the node array. */
    private static final int NODEARRRAY_HEIGHT = 250 ;
    
    /** A counter for how log Mario has been jumping  */
    private transient static int jumpCounter = 0;
    
    private static final String NODE_ARRAY_FILE_NAME = "G:\\t2\\nodeArray.ser";
    

    /**
     * Checks whether the node array is currently empty
     *
     * @return true, if empty
     */
    public static boolean nullArray()
    {
        return (nodeArray == null);
    }

    /**
     * Initialises the node array
     */
    public static void initNodeArray()
    {
        nodeArray = new MapNode[NODEARRRAY_LENGTH][NODEARRRAY_HEIGHT];
    }

    /**
     * Prints the node array. This method was primarily used for testing and debugging
     * 
     */
    public static void printNodeArray()
    {
        System.out.println("--------------------------------------------------------------------------------------------------");
        for (int y = 0; y < NODEARRRAY_HEIGHT; y++)
        {
            for (int x = 0; x <NODEARRRAY_LENGTH ; x++)
            {
                if (nodeArray[x][y] != null)
                {
                    System.out.print(nodeArray[x][y].countActions() + "\t");

                }
                else
                {
                    System.out.print("N\t");
                }
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("--------------------------------------------------------------------------------------------------");
        System.out.println();
    }

    /**
     * Returns a node from the array matching the X:Y location specified and both 
     * the direction mario is facing and the current scene. If no such no exists, 
     * a new node is created and returned.
     *
     * @param _x the x location of the node on the level
     * @param _y the y location of the node on the level.
     * @param _scene the current levelScene or mergedObservation on the level. 
     * @param direction the direction mario is facing
     * @return the node matching the parameters specified, or a new node.
     */
    public static MapNode getNode(int _x, int _y, byte[][] _scene, int direction)
    {
    	//if the node exists
        //System.out.println("Requesting Node: " + x + ":" +y);
        if (nodeArray[_x][_y] != null)
        {
        	//and the scene matches
            if (nodeArray[_x][_y].hasScene(direction, _scene))
            {
            	//return this node
                //System.out.println("Node has scene");
                return nodeArray[_x][_y];
            }
            else //the scene doesn't match            	
            {
            	
            	//add a new action to this node and return it.
                //System.out.println("No Scene, adding action");
                nodeArray[_x][_y].addAction(direction, _scene);
                return nodeArray[_x][_y];
            }
        }
        else //no node exists
        {
        	//return a new node
            //System.out.println("Null Node, adding");
            nodeArray[_x][_y] = new MapNode(direction,_scene, _x,_y, NodeID);
            NodeID++;
            return nodeArray[_x][_y];
        }
    }

    /**
     *  Load an existing node array from disk
     */
    public static void loadArray()
    {
        try
        {
            FileInputStream fis = new FileInputStream(new File(NODE_ARRAY_FILE_NAME));
            ObjectInputStream oos = new ObjectInputStream(fis);

            nodeArray = (MapNode[][]) oos.readObject();
            MapNode.printNodeArray();
        }
        catch (FileNotFoundException fnf)
        {
        	//none exist, load a new one
            MapNode.initNodeArray();
            //System.out.println("Couldn't find it");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Store the current node array to disk
     */
    public static void storeArray()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(new File(NODE_ARRAY_FILE_NAME));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(nodeArray);
            oos.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //----------------- INSTANCE METHODS AND VARIABLES ---------------------------------------------------------------------//


    /** The list of actions and scores stored with each node in the array */
    private ArrayList<ActionNode> actionNodeList;
    
    /** The last action completed on this node */
    private ActionNode lastActionNode;
    
    /** The location of the node in the array. */
    private int [] location;
    
    /** The nodes ID. */
    private int ID;
    
    /** the number of actions for which MArio has been in this node. */
    private int stuckCounter;

    
    /**
     * Instantiates a new map node.
     *
     * @param _direction the direction mario is facing
     * @param _scene the current scene in the level
     * @param x the x location of the node
     * @param y the y location of the node
     * @param _newID the new node's ID
     */
    public MapNode(int _direction, byte[][] _scene, int x, int y, int _newID)
    {
        this.location = new int[2];
        this.location[0] = x;
        this.location[1] = y;
        this.ID = _newID;

        //create a new action node for this map node
        if(actionNodeList == null)
        {
            actionNodeList = new ArrayList<ActionNode>();
            actionNodeList.add(new ActionNode(_direction, _scene));
        }
    }

    /**
     * Returns the number of actions stored with the node
     *
     * @return actions store in node
     */
    public int countActions()
    {
        if (actionNodeList == null)
        {
            return 0;
        }
        return actionNodeList.size();
    }


    /**
     * Returns the ID of the current node
     *
     * @return the node's id
     */
    public int getID()
    {
        return this.ID;
    }

    /**
     * Gets the X:Y location of the node.
     *
     * @return an array of the node's X and Y locations
     */
    public int [] getLocation()
    {
        return this.location;
    }

    /**
     * returns best action for this node, or an untried action if any still exist.
     *
     * @param _scene the current scene on the level.
     * @param direction the direction mario is facing
     * @param canJump the current abiltiy of Mario to jump
     * 
     * @return the next action
     */
    public boolean[] getNextAction(byte[][] _scene, int direction, boolean canJump)
    {
        int count = 0;
        boolean[] nextAction = new boolean[Environment.numberOfKeys];

        //iterate over each action for the node
        for (ActionNode a : actionNodeList)
        {
        	//if the scene and directions match
            //System.out.println("Looking for action!");
            if (a.getDirection() == direction && a.compareScene(_scene))
            {
            	//select the best action from the action list
                //System.out.println("found existing action!");
                nextAction = a.getBestAction(_scene);
                //set the last action sent
                lastActionNode = a;
                count++;
                //return nextAction;
            }
        }

        if (count > 1)
        {
            //System.out.println("Found " + count + " actions");
        }
        //count if MArio is jumping or not
        if (nextAction[Mario.KEY_JUMP] == true)
        {
            jumpCounter++;
        }
        //if the jump counter is over 16
        if (jumpCounter > 16)
        {
        	//mario cannot jump
            nextAction[Mario.KEY_JUMP] = false;
            jumpCounter =0;
        }

        //System.out.println("CanJump: " + canJump + " JumpCounter: " + jumpCounter);
        return nextAction;
    }

    /**
     * Sets the score.
     *
     * @param environment the environment
     * @param lastX the last x
     * @param lastY the last y
     * @param lastMode the last mode
     * @param child the child
     */
    public void setScore(Environment environment, float lastX, float lastY, int lastMode, int[] child)
    {
    	//penalise nodes where mario gets stuck
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
        //if mario hasn't moved more than 1.2 pxiels
        if ((Math.abs(environment.getMarioFloatPos()[0] - lastX) < 1.21F) && (environment.getMarioFloatPos()[1] == lastY))
        {
        	//update the stuck counter
            //System.out.println("STTTTTTUUUUUUUUUUUUUUCCCCCCCKKKKK!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            stuckCounter++;
        }
        //if mario hasn't moved for 5 frames
        if (stuckCounter > 5)
        {
        	//this is a bad node/action combination
            stuckCounter = 0;
            stuckPenalty = -30;
        }
        
        //nodes are scored on the amount mario moves up and forward
        
        float xScore = (environment.getMarioFloatPos()[0]*2) - lastX;
        float yScore = -(environment.getMarioFloatPos()[1])- -(lastY*2);
        //System.out.println("Y: " + environment.getMarioFloatPos()[1] + " last Y: " + lastY);
        //System.out.println("X: " + environment.getMarioFloatPos()[0] + " last Y: " + lastX);
        //System.out.println("Mode: " + (environment.getMarioMode()*32) + " last Mode: " + (lastMode*32));
        
        //apply a modifier if mario got hurt
        float mode = (environment.getMarioMode() * 32) - (lastMode * 32);
        float newScore = (xScore*2) + yScore + mode;
        //set the score for the action and apply the stuck penalty
        lastActionNode.setScore(newScore+stuckPenalty, child);
    }

    /**
     * Adds an action/score combo to this map node
     *
     * @param _direction the direction mario is facing
     * @param _scene the current level scene
     */
    public void addAction(int _direction, byte[][] _scene)
    {
        actionNodeList.add(new ActionNode(_direction, _scene));
    }

    /**
     * determines if the current map node has children.  (nodes that can be reached
     * from this one).
     *
     * @return true, if children exist
     */
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




    /**
     * Checks if the currebt level scene matches any previously visited nodes.
     *
     * @param direction the direction mario is facing
     * @param _scene the current scene in the level
     * @return true, if a match is found
     */
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
    

    /**
     * Prints the locations of child nodes.
     *
     * @param _scene the current level scene
     */
    public void getLocations(byte[][] _scene)
    {
    	
        System.out.println("Node ("+ this.getID()  +  "): " + this.getLocation()[0] + ":" + this.getLocation()[1]);

        //iterate over ach action in the list
        for (ActionNode a : actionNodeList)
        {
            //System.out.println("Direction: " + a.getDirection());
            
        	//store the locations of the child
        	int  [][] children = a.getChildren();
            //store the actions for the child
        	boolean [][] actions = a.getActions();
        	//store the score for the child
            float [] scores = a.getScores();

            //if the scene matches
            if (a.compareScene(_scene))
            {
                for (int x = 0; x < ActionNode.ACTION_COUNT; x++)
                {
                    System.out.print("ActionNode " + ActionNode.nameAction(actions[x]));
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

/**
 * Stores an action along with the score for that action and the MapNode reched by it.
 * 
 * @author Ciaran Kearney
 * @version 1.0
 * @since 23/12/2013
 *
 */
class ActionNode implements Serializable
{

	/*  IDs for the possible actions */
    private static final int RIGHT = 0;
    private static final int RIGHT_SPEED = 1;
    private static final int RIGHT_JUMP = 2;
    private static final int RIGHT_JUMP_SPEED = 3;
    private static final int LEFT = 4;
    private static final int LEFT_SPEED = 5;
    private static final int LEFT_JUMP = 6;
    public static final int LEFT_JUMP_SPEED = 7;
    public static final int ACTION_COUNT = 8;

    /* the last action performed */
    private int lastAction;
    
    /* array of actiopns for this node */ 
    private boolean [][] action;
    /* locations reached by that action */
    private int [][] actionChildLocation;
    /* the score for that action */
    private float[] score;
    
    /* the direction mario was facing for this node */
    private int direction;
     /* the scene stored for this node */
    private byte[][] scene;

    /**
     * Instantiates a new action node
     * 
     * @param _direction the direction mario is facing
     * @param _scene the current scene for the level
     */
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

    /**
     *  Checks if any of the actions from this node have children
     *  
     * @return true, if children found
     */
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
    
    /**
     *  Returns the list of actions available from this node
     * @return actions available from this node
     */
    public boolean[][] getActions()
    {
        return this.action;
    }
    
    /**
     *  Returns the scores for the actions possible from this node
     * @return an array of scores for the actions possible from this node
     */
    public float [] getScores()
    {
        return this.score;
    }

    /**
     * Sets the score and the child location for the last action
     * attempted from this node
     * 
     * @param _score the score for the action
     * @param _child the location of the child node
     */
    public void setScore(float _score, int[]  _child)
    {
        //System.out.println("Setting score: " + _score);
        this.score[lastAction] = _score;
        this.actionChildLocation[lastAction] = _child;
    }
    
    /**
     *  Returns the direction mario was facing when this node was 
     *  created
     * @return
     */
    public int getDirection()
    {
        return this.direction;
    }

    /**
     * Compares the passed in scene with the one currently stored for this
     * node 
     * @param newScene the current level scene
     * @return true, if scenes match
     */
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

    /** 
     * Returns the highest scoring action for this node, or an untried action.
     * if the cell in front of mario is an obstacle, it returns a jump command.
     *   
     * @param _scene the current level scene
     * @return the next action to attempt
     */
    public boolean[] getBestAction(byte [][] _scene)
    {
    	//check the cells in front of Mario
        for (int x = 11; x < 13; x++)
        {
        	//if an obstacle exists
            if (_scene[12][x] == GeneralizerLevelScene.UNBREAKABLE_BRICK ||
                    _scene[11][x] == GeneralizerLevelScene.BORDER_HILL ||
                    _scene[11][x] == GeneralizerLevelScene.BORDER_CANNOT_PASS_THROUGH ||
                    _scene[11][x] == GeneralizerLevelScene.BREAKABLE_BRICK ||
                    _scene[11][x] == GeneralizerLevelScene.BRICK ||
                    _scene[11][x] == GeneralizerLevelScene.CANNON_MUZZLE ||
                    _scene[11][x] == GeneralizerLevelScene.CANNON_TRUNK ||
                    _scene[11][x] == GeneralizerLevelScene.FLOWER_POT ||
                    _scene[11][x] == GeneralizerLevelScene.FLOWER_POT_OR_CANNON )
            {
            		//jump over it
                    lastAction = ActionNode.RIGHT_JUMP;
                    return ActionNode.createAction(ActionNode.RIGHT_JUMP);
            }
        }
        
        /* set the lowest score to -1 */
        float bestScore = -1;
        /* create an empty action*/
        boolean [] nextAction = new boolean[Environment.numberOfKeys];

        //System.out.println("----------------------------CHOOSING--------------------------------------------------------------");
        for (int x = 0; x < ACTION_COUNT; x++)
        {
        	//if this action has no children
            if (actionChildLocation[x] == null)
            {
                lastAction = x;
                //System.out.println("ActionNode: " + ActionNode.nameAction(action[lastActionNode]) + "Not tried" );
                
                //chose this next
                nextAction =  action[x];
                break;
            }
            else
            {
            	//if the action has a higher score than the currently selected action
                if (score[x] > bestScore)
                {
                	//it is now the best action
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
        //return the best action found
        return new boolean[] { nextAction[0],nextAction[1],nextAction[2],nextAction[3],nextAction[4],nextAction[5] };
    }

    /**
     *  Creates an action based on the passed in iteger value
     * @param actionID the ID of the action to create
     * 
     * @return a boolean array representing the action to try
     */
    public static boolean[] createAction(int actionID)
    {
        boolean [] newAction = new boolean[Environment.numberOfKeys];
        switch (actionID)
        {
            case RIGHT:
                newAction[Mario.KEY_JUMP] = false;
                newAction[Mario.KEY_LEFT] = false;
                newAction[Mario.KEY_RIGHT] = true;
                newAction[Mario.KEY_SPEED] = false;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                System.out.println("Creating: RIGHT");
                return newAction;
            case RIGHT_SPEED:
                newAction[Mario.KEY_JUMP] = false;
                newAction[Mario.KEY_LEFT] = false;
                newAction[Mario.KEY_RIGHT] = true;
                newAction[Mario.KEY_SPEED] = true;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                System.out.println("Creating: RIGHT_SPEED");
                return newAction;
            case RIGHT_JUMP:
                newAction[Mario.KEY_JUMP] = true;
                newAction[Mario.KEY_LEFT] = false;
                newAction[Mario.KEY_RIGHT] = true;
                newAction[Mario.KEY_SPEED] = false;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                System.out.println("Creating: RIGHT_JUMP");
                return newAction;
            case RIGHT_JUMP_SPEED:
                newAction[Mario.KEY_JUMP] = true;
                newAction[Mario.KEY_LEFT] = false;
                newAction[Mario.KEY_RIGHT] = true;
                newAction[Mario.KEY_SPEED] = true;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                System.out.println("Creating: RIGHT_JUMP_SPEED");
                return newAction;
            case LEFT:
                newAction[Mario.KEY_JUMP] = false;
                newAction[Mario.KEY_LEFT] = true;
                newAction[Mario.KEY_RIGHT] = false;
                newAction[Mario.KEY_SPEED] = false;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                System.out.println("Creating: LEFT");
                return newAction;
            case LEFT_SPEED:
                newAction[Mario.KEY_JUMP] = false;
                newAction[Mario.KEY_LEFT] = true;
                newAction[Mario.KEY_RIGHT] = false;
                newAction[Mario.KEY_SPEED] = true;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                System.out.println("Creating: LEFT_SPEED");
                return newAction;
            case LEFT_JUMP:
                newAction[Mario.KEY_JUMP] = true;
                newAction[Mario.KEY_LEFT] = true;
                newAction[Mario.KEY_RIGHT] = false;
                newAction[Mario.KEY_SPEED] = false;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                System.out.println("Creating: LEFT_JUMP");
                return newAction;
            case LEFT_JUMP_SPEED:
                newAction[Mario.KEY_JUMP] = true;
                newAction[Mario.KEY_LEFT] = true;
                newAction[Mario.KEY_RIGHT] = false;
                newAction[Mario.KEY_SPEED] = true;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                System.out.println("Creating: LEFT_JUMP_SPEED");
                return newAction;

        }
        return newAction;
    }

    /** 
     * sets the id of the last action attempted
     * @param _action the id of the action
     */
    public void setLastAction(int _action)
    {
        this.lastAction = _action;
    }
    
    /**
     *  Names an action based on its boolean values
     *  
     * @param _action the action to name
     * @return string representation of the action.
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
    
    /**
     * Returns all the children from this node
     * 
     * @return the children from this node
     */
    public int[][] getChildren()
    {
        return actionChildLocation;
    }
}
