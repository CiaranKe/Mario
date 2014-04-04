package competition.uu2013.common.hueristics;

import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;
import competition.uu2013.common.Sprites.SpriteSim;
import competition.uu2013.common.level.Map;
import competition.uu2013.common.level.WorldSim;
import competition.uu2013.prototypes.Action;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * THe Search class. Searches SortedList items using the A* algorithm to determine the best path. 
 * 
 * Generates the successors in advance for speed and limits the number of successors generated.
 */
public class AStarSearch
{

    /** The maximum number of successor generations */
    private static final int SEARCH_DEPTH = 3;
    /** Replan when a new enemy is found. */
    private boolean rePlanNewEnemy;
    /** replan if we lose sync with the game. */
    private boolean rePlanLostSync;
    /** The receptive field width. */
    private int sceneWidth;
    /** The receptive field height. */
    private int sceneHeight;
    /** The open list. */
    private SortedList<SearchNode> openList;
    /** The closed list. */
    private SortedList<SearchNode> closedList;
    /** The plan generated from this search. */
    private Stack<boolean[]> plan;
    /** The main simulation updated from the game state. */
    private SearchNode working;
    /** Added for the IBk Agent. */
    private boolean dontReversePlan;

    /**
     * Instantiates a new a star search.
     *
     * @param sim A simulation of the Game
     */
    public AStarSearch(WorldSim sim)
    {
        this.working = new SearchNode(1,sim);
        this.openList = new SortedList<SearchNode>();
        this.closedList = new SortedList<SearchNode>();
        this.plan = new Stack<boolean[]>();
        this.dontReversePlan = false;
    }


    /**
     * Generate children. Recursive method that generates SEARCH_DEPTH
     * generations of SortedListItems and caluclates the maximum distance
     * than can be travelled.
     *
     * @param currentNode the node to generate successors for
     * @param maxDepth the depth to generate
     * @param _x mario's x location
     * @param _y mario's y location
     * @return the maxium distance covered
     */
    private float  generateChildren(SearchNode currentNode, int maxDepth, float _x, float _y)
    {
        float maxX = 0;
        if (maxDepth > 0)
        {
        	//generate the successors
            maxX = currentNode.generateChildren(_x,_y,sceneWidth,sceneHeight);

            //for each successor
            for (SearchNode s : currentNode.getChildren())
            {
            	//generate its children with (SEARCH_DEPTH -1)
                float temp2 = this.generateChildren(s,--maxDepth,_x,_y);

                //calculate the distance covered
                if (temp2 > maxX)
                {
                    maxX = temp2;
                }
            }
            return maxX;
        }
        return -1;
    }


    /**
     * The main search function, uses A* to find the best path in the current scene
     *
     * @param _x Mario's X location
     * @param _y Mario's Y location
     * @param startTime the time the method started, no longer needed, used for output
     * @param _dontReversePlan should the returned plan be reversed (default is false). 
     * @return action to complete
     */
    public boolean[] pathFind(float _x, float _y, long startTime, boolean _dontReversePlan)
    {
        this.dontReversePlan = _dontReversePlan;
     
        //if we've lost sync, found a new enemy, or the current plan is empty
        if (rePlanNewEnemy || rePlanLostSync || plan.empty())
        {
        	//search
        	//System.out.println("Lost Sync: "+rePlanLostSync+", New Enemy: "+rePlanNewEnemy+", Empty plan: "+plan.empty());

        	//clear the current lists
            openList.clear();
            closedList.clear();
            plan.clear();
            //set the cost of the main sim
            working.setGCost(0);
            //add it to the open list
            openList.add(working);
            //reset the node counter
            SearchNode.resetCounter();
            SearchNode current = working;
            //this is the best we can do 
            SearchNode farthest = working;
            //get the goal distance and generate all the successors
            float goal = this.generateChildren(working, SEARCH_DEPTH, _x, _y);
            
            if ((goal - _x) < 1.0F)
            {
            	//be optimistic :)
                goal+=10;
            }
            //System.out.println("Goal is " + (goal - _x));
            //set the goal for all the nodes
            SearchNode.setGoal(goal);
            float farthestX = 0;
            int loopCounter = 1;
            //start the search
            while (openList.size() != 0)
            {
                //System.out.println("Iteration: " + loopCounter + " Open size: " + openList.size() + " Closed Size: " +closedList.size());
                if (current.isGoal())
                {
                    //System.out.println("Ran " + loopCounter + " Iterations, open size: " + openList.size() + ", Time taken: " + (System.currentTimeMillis() - startTime));
                    this.extractPlan(current);
                    return plan.pop();
                }
                //keep track of the farthest we've travelled
                if (current.getPredictedXY()[0] >= farthestX)
                {
                    farthest = current;
                    farthestX = current.getPredictedXY()[0];
                }
                //if the node has children
                if (current.getChildren() != null)
                {
                	//for each child
                    for (SearchNode n : current.getChildren())
                    {
                        boolean isBetter;

                        //if it's on the closed list
                        if (closedList.contains(n))
                        {
                        	//skip it
                            continue;
                        }
                        //if it's not blocked
                        if (!n.isBlocked())
                        {
                        	//and not on the open list
                            if (!openList.contains(n))
                            {
                            	//add it
                                openList.add(n);
                                isBetter = true;
                            }
                            //if its got a lower cost
                            else if (n.getFCost() < current.getFCost())
                            {
                            	//its better
                                isBetter = true;
                            }
                            else
                            {
                                isBetter = false;
                            }
                            if (isBetter)
                            {
                            	//set this node to be the new parent, and recalculate the costs
                                n.setParent(current);
                                n.setGCost(current.getGCost());
                                n.estimateHCost(current.getMarioSim().getMarioMode());
                            }
                        }
                    }
                }

                loopCounter++;
                //get the next node
                current = openList.getFirst();
                //System.out.println("Expanding node: "  + current.getID());
                //swap the current node to the closed list
                openList.remove(current);
                closedList.add(current);

            }
            if (!current.isGoal())
            {
            	//if we didn't reach the goal, use the farthest found instead
                //System.out.println("Didn't find goal");
                //System.out.println("Farthest found was : " + (farthestX - working.getPredictedXY()[0]) + " Pixels ahead");
                extractPlan(farthest);
            }
            //System.out.println("Ran " + loopCounter + " Iterations, open size: " + openList.size() + ", Time taken: " + (System.currentTimeMillis() - startTime));
        }
        try
        {
            return plan.pop();
        }
        catch (EmptyStackException emptyStackException)
        {
        	//shouldn't happen anymore 
            boolean [] failedSearch = new boolean[Environment.numberOfKeys];
            
            failedSearch[Mario.KEY_JUMP] = !working.getMarioSim().isMarioAbletoJump(); //seems to work!
            //hmmm, left or right?
            if (working.getMap().getViewAt(working.getMarioSim().getXLocation()+ Map.CELL_SIZE, working.getMarioSim().getYLocation()) != 0)
            {
                failedSearch[Mario.KEY_LEFT] = true;
            }
            else
            {
                failedSearch[Mario.KEY_RIGHT] = true;
            }
            //System.out.println("SEARCH FAILED!");
            return failedSearch;
        }
    }

    /**
     * Follows the pointers to parent nodes to extract a plan for
     * this scene
     *
     * @param _current the goal, or farthest node found
     */
    private void extractPlan(SearchNode _current)
    {
        //System.out.print("Plan: ");
        Stack<boolean[]> tempStack = new Stack<boolean[]>();

        //follow the parents up the pointers
        while (_current.getParent() !=null )
        {
        	//Stop when you reach the top (send me a postcard though)
            if (_current.getParent() == null || _current.getID() == 1)
            {
                break;
            }
            tempStack.push(_current.getAction());
            _current = _current.getParent();
        }
        //clear the current plan
        plan.clear();

        if (!this.dontReversePlan)
        {
        	//reverse the plan
            while (!tempStack.empty())
            {
                plan.push(tempStack.pop());
                System.out.print(_current.getID() + ": " +Action.nameAction(plan.peek()) + ", ");
            }
        }
        else
        {
        	//swap the stacks
            plan = tempStack;
        }
    }


    /**
     * Update the main simulation.
     *
     * @param x Mario's X location
     * @param y Mario's Y location
     * @param isMarioAbleToJump is he?
     * @param isMarioOnGround is he?
     * @param wasOnGround was mario on the ground in the previous frame
     * @param isMarioAbleToShoot is he?
     * @param marioStatus big,small,fire
     * @param newEnemies the new enemies
     * @param scene the levelScene array
     * @param _lastAction the last action completed
     */
    public void updateSim(float x, float y, boolean isMarioAbleToJump, boolean isMarioOnGround, boolean wasOnGround, boolean isMarioAbleToShoot, int marioStatus, float [] newEnemies, byte [][] scene, boolean [] _lastAction)
    {

        this.sceneWidth = scene[0].length / 2;
        this.sceneHeight = scene.length / 2;
        working.setAction(_lastAction);
        working.move(x, y);
        rePlanNewEnemy = working.updateSim(x,y,isMarioAbleToJump,isMarioOnGround, wasOnGround,isMarioAbleToShoot,marioStatus,newEnemies,scene);

        float [] predXY = working.getPredictedXY();

        rePlanLostSync = false;
        if (x != predXY[0] || y != predXY[1])
        {
            rePlanLostSync = true;
        }
    }

    /**
     * Gets the predicted x location of mario, used to ensure sync.
     *
     * @return the predicted x location
     */
    public float getPredictedX()
    {
        return working.getPredictedXY()[0];
    }

    /**
     * Gets the predicted y location of mario, used to ensure sync.
     *
     * @return the predicted y location
     */
    public float getPredictedY()
    {
        return working.getPredictedXY()[1];
    }

    /**
     * Gets the enemy sims, used for debugging.
     *
     * @return the enemy sims
     * @throws CloneNotSupportedException the clone not supported exception
     */
    public ArrayList<SpriteSim> getEnemySims() throws CloneNotSupportedException
    {
        return working.getEnemySims();
    }

    /**
     * Gets the plan.
     * 
     * Added for IBk agent
     *
     * @return the plan
     */
    public Stack<boolean[]> getPlan() 
    {
        return plan;
    }
}
