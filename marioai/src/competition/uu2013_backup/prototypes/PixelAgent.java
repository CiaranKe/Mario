package competition.uu2013.prototypes;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.environments.Environment;

/**
 * Created with IntelliJ IDEA.
 * User: fluffy
 * Date: 31/01/14
 * Time: 20:12
 * To change this template use File | Settings | File Templates.
 */
public class PixelAgent implements Agent
{
    protected String Name = "PixelAgent";
    private boolean [] lastAction;
    private PixelNode current;
    private Environment env;


    public boolean[] getAction()
    {
        //System.out.println("Using" + current.getId());
        lastAction = current.getNextAction();
        //System.out.println("Location (X/Y): " + (int)(env.getMarioFloatPos()[0]/16) + " : " + (int)(env.getCreaturesFloatPos()[1]/16) + " Action: " +
        //        "Jump: " + lastAction[Mario.KEY_JUMP]  +
        //        " Down: " + lastAction[Mario.KEY_DOWN] +
        //        " Right: " + lastAction[Mario.KEY_RIGHT] +
        //        " Up: " + lastAction[Mario.KEY_UP] +
        //        " Left: " + lastAction[Mario.KEY_LEFT] +
        //        " Speed: " + lastAction[Mario.KEY_SPEED]);
        return lastAction;
    }

    public void integrateObservation(Environment environment)
    {
        long startTime = System.currentTimeMillis();
        this.env = environment;

        if (lastAction == null)
        {
            lastAction = new boolean[Environment.numberOfKeys];
        }

        PixelNode pixelNode = new PixelNode(environment, lastAction);
        if (this.current != null)
        {
            this.current.appendChild(lastAction, pixelNode);
        }
        this.current = PixelNode.findNode(pixelNode);
        if (this.current == null)
        {
            this.current = PixelNode.createNode(pixelNode);
        }
        if (System.currentTimeMillis() - startTime > 40)
        {
            System.out.println("Method time: " + (System.currentTimeMillis() - startTime));
        }
    }


    @Override
    public void giveIntermediateReward(float intermediateReward) {

    }

    public void reset()
    {

    }

    @Override
    public void setObservationDetails(int rfWidth, int rfHeight, int egoRow, int egoCol)
    {

    }


    public String getName() { return Name; }

    public void setName(String Name) { this.Name = Name; }



}
