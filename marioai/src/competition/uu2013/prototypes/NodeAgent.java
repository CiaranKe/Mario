package competition.uu2013.prototypes;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.environments.Environment;


/**
 * The Class NodeAgent.  Generates actions at random and scores them based
 * on how far the action moves Mario and whether or not he was hurt.  This
 * agent discretises the level into nodes of 16*16 pixels. The nodes are 
 * Serialised into a file that is loaded and saved between levels so as
 * to comply with the 40ms time limit.  
 * 
 * 
 * @author Ciaran Kearney
 * @version 1.0
 * @since 23/12/2014
 */
public class NodeAgent extends MarioAIAgent implements Agent
{
    
    /** Stores the map node currently in use */
    public MapNode currentMapNode;
    
    /** Stores the preciously used map node. */
    public MapNode lastMapNode;
    
    /** Mario's last x Position. */
    private float lastXPosition;
    
    /** Mario's last Y position. */
    private float lastYPosition;
    
    /** The last state MArio was in (big,small,fire). */
    private int lastMode;

    /**
     * Instantiates a new node agent.
     */
    public NodeAgent()
    {
        super("NodeAgent");
        lastXPosition = 0.0F;
        lastYPosition = 0.0F;
    }

    /**
     * Instantiates a new node agent.
     *
     * @param s the name of the Agent
     */
    public NodeAgent(String s)
    {
        super(s);
    }

    /** 
     * Does nothing, all of the work in this agent takes place in the integrateObservation method.
     * the value of action is set there.
     * 
     * @return the action selected by this agent
     * @see MarioAIAgent#getAction()
     */
    public boolean[] getAction()
    {
        return this.action;
    }

    /** 
     * @param environment The current on screen information. 
     * @see MarioAIAgent#integrateObservation(ch.idsia.benchmark.mario.environments.Environment)
     */
    public void integrateObservation(Environment environment)
    {
        //Get mario's x position
    	float xPos = environment.getMarioFloatPos()[0];
    	//get the on screen enemies and obstacles
        byte [][] scene = environment.getMergedObservationZZ(0,0);
        //determine the direction mario is facing.
        int direction = (xPos > lastXPosition) ? 1 : -1;
        
        //store Mario's current on screen position
        int x = (int) xPos;
        int y = (int) environment.getMarioFloatPos()[1];
        
        //if this isn't the first time this method has been called.
        if (currentMapNode != null)
        {
        	//store the last visited node
            lastMapNode = currentMapNode;
        }

        // and select the next node to assess
        currentMapNode = MapNode.getNode(x, y, scene, direction);
        //if we've already been to a node.
        if (lastMapNode != null)
        {
        	//score it.
           lastMapNode.setScore(environment, lastXPosition, lastYPosition, lastMode ,currentMapNode.getLocation());
        }
        //select the next action for the current node
        this.action = currentMapNode.getNextAction(scene, direction, environment.isMarioAbleToJump());
        
        this.lastXPosition = xPos;
        this.lastYPosition = environment.getMarioFloatPos()[1];;
        this.lastMode = environment.getMarioMode();
        //if this node already has children
        if (currentMapNode.hasChildren())
        {
        	//assess the children
            currentMapNode.getLocations(scene);
        }
    }

    /** 
     * Loads and saves the array of MapeNodes, this method is called
     * outside the time bound so speed of operation is unimportant. 
     * 
     * @see MarioAIAgent#reset()
     */
    public void reset()
    {
        if (MapNode.nullArray())
        {
            MapNode.loadArray();
        }
        else
        {
            MapNode.storeArray();
        }
    }
}
