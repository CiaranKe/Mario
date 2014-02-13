package competition.uu2013.reddit;


import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.environments.Environment;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy
 * Date: Apr 25, 2009
 * Time: 12:30:41 AM
 * Package: ch.idsia.ai.agents.ai;
 */
public class BasicAIAgent implements Agent
{
    protected boolean action[] = new boolean[Environment.numberOfKeys];
    protected String name = "Instance of BasicAIAgent. Change this name";

    public void reset()
    {
        action = new boolean[Environment.numberOfKeys];// Empty action
    }

    @Override
    public void setObservationDetails(int rfWidth, int rfHeight, int egoRow, int egoCol) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean[] getAction()
    {
        return new boolean[Environment.numberOfKeys]; // Empty action
    }

    @Override
    public void integrateObservation(Environment environment) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void giveIntermediateReward(float intermediateReward) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public String getName() {        return name;    }

    public void setName(String Name) { this.name = Name;    }
}
