package competition.uu2013.prototypes;

import ch.idsia.agents.Agent;
import ch.idsia.agents.controllers.BasicMarioAIAgent;
import ch.idsia.benchmark.mario.environments.Environment;

import java.util.ArrayList;


public class LearningAgent extends BasicMarioAIAgent implements Agent
{
    public MapNode currentMapNode;
    public MapNode lastMapNode;
    private ArrayList<Action> plan;
    private float lastX = 0.0F;
    private float lastY = 0.0F;
    private int lastMode;

    public LearningAgent()
    {
        super("LearningAgent");
    }

    public LearningAgent(String s)
    {
        super(s);
    }

    public boolean[] getAction()
    {
        return this.action;
    }

    public void integrateObservation(Environment environment)
    {
        float xPos = environment.getMarioFloatPos()[0];
        byte [][] scene = environment.getMergedObservationZZ(0,0);
        int direction = (xPos > lastX) ? 1 : -1;
        int x = (int) xPos;
        int y = (int) environment.getMarioFloatPos()[1];
        //System.out.println("X:Y" +x+":"+y);

        if (currentMapNode != null)
        {
            lastMapNode = currentMapNode;
        }

        currentMapNode = MapNode.getNode(x,y, scene, direction);
        if (lastMapNode != null)
        {
           lastMapNode.setScore(environment, lastX, lastY, lastMode ,currentMapNode.getLocation());
        }
        this.action = currentMapNode.getNextAction(scene, direction, environment.isMarioAbleToJump());
        this.lastX = xPos;
        this.lastY = environment.getMarioFloatPos()[1];;
        this.lastMode = environment.getMarioMode();
        if (currentMapNode.hasChildren())
        {
            currentMapNode.getLocations(scene);
        }
    }

    public void reset()
    {
        if (MapNode.nullArray())
        {
            MapNode.loadArray();
        }
        else
        {
            MapNode.storeArray();
            //MapNode.printNodeArray();
        }
    }
}
