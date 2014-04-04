package competition.uu2013.common.hueristics;

import ch.idsia.benchmark.mario.engine.sprites.Mario;
import competition.uu2013.common.Sprites.MarioSim;
import competition.uu2013.common.Sprites.SpriteSim;
import competition.uu2013.common.level.Map;
import competition.uu2013.common.level.WorldSim;
import competition.uu2013.prototypes.Action;

import java.util.ArrayList;

/**
 * SearchNode. Contains a simulation of the game state with associated 
 * G and H costs, can be seached and compared to other nodes to find the best path to follow.   
 * Implements SortedListItem to allow comparison by SortedList
 */
public class SearchNode implements SortedListItem<SearchNode>
{

    /** Static ID counter for all nodes */
    private static int idCounter;
    /** The action for this node. */
    private boolean [] action;
    /** The current world sim. */
    private WorldSim sim;
    /** The predicted x location. */
    private float predX;
    /** The predicted y location. */
    private float predY;    
    /** The g cost for this node. */
    private int gCost;
    /** The h cost for this node. */
    private float hCost;
    /** The goal x location. */
    private static float goalX;
    /** The parent of this node. */
    private SearchNode parent;
    /** The successors of this node. */
    private SortedList<SearchNode> children;
    /** indicates that mario was hurt in this node. */
    private boolean blocked;
    /** The node id. */
    private int id;

    
    static /* initialiser */
    {
        idCounter =1;
    }


    /**
     * Instantiates a new search node.
     *
     * @param _id the id for the node
     * @param _sim world sim for the node
     */
    public SearchNode(int _id, WorldSim _sim)
    {
        this.id = _id;
        this.sim = _sim;
    }


    /**
     * REturns the nodes predicted xy values.
     *
     * @return the predicted xy values
     */
    public float [] getPredictedXY()
    {
        return new float[] {this.predX, this.predY};
    }

    /**
     * Updates the enclosed world simulation
     *
     * @param x the x location of mario
     * @param y the y location of mario
     * @param isMarioAbleToJump true if yes
     * @param isMarioOnGround true if yes
     * @param wasOnGround in last turn
     * @param isMarioAbleToShoot true if yes
     * @param marioStatus current state for mario
     * @param newEnemies the new enemies array
     * @param scene the levelscene array
     * @return true, if new enemy found
     */
    public boolean updateSim(float x, float y, boolean isMarioAbleToJump, boolean isMarioOnGround, boolean wasOnGround, boolean isMarioAbleToShoot, int marioStatus, float [] newEnemies, byte [][] scene)
    {
        return sim.syncLocation(x,y,isMarioAbleToJump,isMarioOnGround, wasOnGround,isMarioAbleToShoot,marioStatus,newEnemies,scene);
    }

    /**
     * Sets the action mario should complete for this node.
     *
     * @param _action the new action to complete
     */
    public void setAction(boolean[] _action)
    {
        this.action = _action;
    }

    /**
     * Gets the action completed by this node.
     *
     * @return the action completed on the node
     */
    public boolean[] getAction()
    {
        return this.action;
    }

    /**
     * Updates the simulation after applying the new action
     *
     * @param currentX the current x location of mario
     * @param currentY the current y location of mario
     */
    public void move(float currentX, float currentY)
    {
    	//update the simulation
        sim.move(this.action,currentX,currentY);
        //get the new X:Y location
        this.predX = sim.getMarioLocation()[0];
        this.predY = sim.getMarioLocation()[1];
    }

    /**
     * Sets the g cost for this node.
     *
     * @param _GCost the new g cost
     */
    public void setGCost(int _GCost)
    {
    	//cost is calculated in frames taken to reach the goal,
    	//so add one.
        this.gCost = _GCost + 1;
    }

    /**
     * Gets the g cost for this node.
     *
     * @return the g cost for this node
     */
    public int getGCost()
    {
        return this.gCost;
    }

    /**
     * Gets the f cost for this node.
     *
     * @return the f cost for this node
     */
    public float getFCost()
    {
        return this.gCost + this.hCost;
    }

    /**
     * Checks if this node has reached the goal.
     *
     * @return true, if goal reached
     */
    public boolean isGoal()
    {
    	//within one pixel is acceptable
        return (this.sim.getMarioSim().getXLocation() > this.goalX-1);
    }
    
    /**
     * Sets the parent for this node.
     *
     * @param _parent the new parent
     */
    public void setParent(SearchNode _parent)
    {
        this.parent = _parent;
    }

    /**
     * Gets the parent for this node.
     *
     * @return the parent node
     */
    public SearchNode getParent()
    {
        return this.parent;
    }


    /**
     * Generate the successor nodes, and calculate the maximum distance 
     * that can be travelled by them
     *
     * @param _x the x location for mario
     * @param _y the y location for mario
     * @param sceneWidth the receptive field width
     * @param sceneHeight the receptive field height
     * @return the maximum distance that can be travelled
     */
    public float generateChildren(float _x, float _y, int sceneWidth, int sceneHeight)
    {
        this.children = new SortedList<SearchNode>();
        float maxX = 0;

        for (boolean[] action : Action.getPossibleActions(this.sim))
        {
            SearchNode s = new SearchNode(++idCounter,this.sim.clone());
            s.setAction(action);
            s.setParent(this);
            s.setBlocked(this.sim.getMarioSim());
            s.setGCost(this.gCost);
            s.move(this.getMarioSim().getXLocation(), this.getMarioSim().getYLocation());
            s.estimateHCost(this.sim.getMarioSim().getMarioMode());
            children.add(s);
            //System.out.println("Created: " + s.toString());
            if (s.getPredictedXY()[0] > maxX)
            {
                maxX = s.getPredictedXY()[0];
            }
        }
        return maxX;
    }



    /**
     * Sets the node to blocked if mario has moved to a lower state in this
     * node.
     *
     * @param oldSim the last state of mario
     */
    private void setBlocked(MarioSim oldSim)
    {
        blocked = false;
        if (this.getMarioSim().isDead() || (this.getMarioSim().getMarioMode() < oldSim.getMarioMode()))
        {
            blocked = true;
        }
    }

    /**
     * Checks if this node is blocked.
     *
     * @return true, if blocked
     */
    public boolean isBlocked()
    {
        return this.blocked;
    }


    /** 
     * Compares this SearchNode to the passed on object.  Rates nodes on:
     * 
     * Heuristic cost,
     * Distance traveled
     * Whether mario was jumping (y locations)
     * in that order 
     * 
     * @see competition.uu2013.common.hueristics.SortedListItem#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(SearchNode element)
    {

        if (this.getFCost() > element.getFCost())
        {
            return 1;
        }
        else if (this.getFCost() < element.getFCost())
        {
            return -1;
        }
        else
        {
            if (this.sim.getMarioSim().getXLocation() > element.sim.getMarioSim().getXLocation())
            {
                return -1;
            }
            else if (this.sim.getMarioSim().getXLocation() < element.sim.getMarioSim().getXLocation())
            {
                return 1;
            }
            else
            {
                if (this.getAction()[Mario.KEY_JUMP] && !element.getAction()[Mario.KEY_JUMP])
                {
                    return -1;
                }
                else if (!this.getAction()[Mario.KEY_JUMP] && element.getAction()[Mario.KEY_JUMP])
                {
                    return 1;
                }
            }
        }
        return 0;
    }

    /** 
     * Used to assess if a node is on the closed list. Discretises the level
     * into 2*2 pixel grid and if an action leads to the same position in the 
     * grid then it is in the visited list
     * 
     * @see competition.uu2013.common.hueristics.SortedListItem#matches(java.lang.Object)
     */
    @Override
    public boolean matches(SearchNode element)
    {

        MarioSim currentSim = this.sim.getMarioSim();
        MarioSim otherSim = element.sim.getMarioSim();

        int diff = 2;

        if ((Math.abs(currentSim.getXLocation() - otherSim.getXLocation()) < diff) && (Math.abs(currentSim.getYLocation() - otherSim.getYLocation()) < diff))
        {
            return true;
        }
        return false;
    }

    /**
     * Estimates the h cost for this node.
     * 
     * THis is done by estimating the number of frames it would take for mario
     * to run to the goal position from here. Underestimates the actual cost, so is
     * admissible.
     *
     * @param oldMarioMode the old mario mode
     */
    public void estimateHCost(int oldMarioMode)
    {
        float hMarioX = this.sim.getMarioSim().getXLocation();
        float hMarioY = this.sim.getMarioSim().getYLocation();
        float hMarioSpeedX = this.sim.getMarioSim().getXA();
        float hMarioSpeedY = this.sim.getMarioSim().getYA();
        
        int ticksX = 0;
        int ticksY = 0;
        int penalty = 0;

        
        while (hMarioX < goalX)
        {
            ticksX++;
            hMarioX += hMarioSpeedX;
            hMarioSpeedX += (this.action[Mario.KEY_SPEED]) ? 0.6 : 1.2 ;
            hMarioSpeedX *= 0.89F;
        }

        while (hMarioY > 0)
        {
            ticksY++;
            hMarioY += hMarioSpeedY;
            hMarioSpeedY -=3;
            hMarioSpeedY *= 0.89F;
        }

        //don't run into things
        if ((this.sim.getMap().getViewAt(this.getMarioSim().getXLocation()+this.getMarioSim().getXA(), this.getMarioSim().getYLocation()) != 0)||
            (this.sim.getMap().getViewAt(this.getMarioSim().getXLocation()+this.getMarioSim().getXA(), this.getMarioSim().getYLocation()) != 2))
        {
            penalty+=2; //chosen arbitrarily 
        }

        if (this.sim.getMarioSim().getMarioMode() < oldMarioMode)
        {
            penalty += 600; //chosen arbitrarily
            parent.setPenalty(500); //penalise the parent too, we don't want to come down this path
        }
        //we died, STAY AWAY FROM THIS NODE MOFO!
        if (this.sim.getMarioSim().isDead())
        {
            penalty += Integer.MAX_VALUE-5000;
            parent.setPenalty(Integer.MAX_VALUE-6000);
        }
        //set the hcost
        this.hCost= ticksX + ticksY + penalty;
    }

    /**
     * Gets the id for this node.
     *
     * @return the id
     */
    public int getID()
    {
        return this.id;
    }

    /**
     * Reset the node counter, done on each turn so node ids are unique per frame .
     */
    public static void resetCounter()
    {
        SearchNode.idCounter = 1;
    }

    /**
     * Gets the enemy sims for this node. Used to debug
     *
     * @return the enemy sims
     * @throws CloneNotSupportedException the clone not supported exception
     */
    public ArrayList<SpriteSim> getEnemySims() throws CloneNotSupportedException
    {
        return sim.getEnemySims();
    }


    /**
     * Gets the mario sim for this node.
     *
     * @return the mario sim
     */
    public MarioSim getMarioSim()
    {
        return sim.getMarioSim();
    }

    /** 
     * Returns a string representation of the node, used for debugging
     * 
     * @return string representation of the node
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(" Node: "+this.getID());
        if (this.parent != null)
        {
            stringBuilder.append(", Parent: " + parent.getID());
        }
        stringBuilder.append(", Cost: "+this.getFCost() + "(" + this.getGCost() + "," + this.hCost + ")");
        stringBuilder.append(", Action: " +Action.nameAction(this.getAction()));
        stringBuilder.append(", Blocked: " + this.blocked);
        stringBuilder.append(", sim Details: {" +  this.sim.toString() + "}");

        return stringBuilder.toString();
    }

    
    /**
     * Sets the goal for this node.
     *
     * @param goal the new goal location
     */
    public static void setGoal(float goal)
    {
        goalX = goal;
    }

    /**
     * Gets the map for this node. Used for debugging
     *
     * @return the map for this node
     */
    public Map getMap()
    {
        return this.sim.getMap();
    }

    /**
     * Gets the nodes successors.
     *
     * @return the children
     */
    public SortedList<SearchNode> getChildren() 
    {
        return children;
    }

    /**
     * Sets a penalty on the node. usually because a 
     * successor caused mario to get hurt
     *
     * @param penalty the new penalty
     */
    public void setPenalty(int penalty) 
    {
        this.hCost += penalty;
    }
}