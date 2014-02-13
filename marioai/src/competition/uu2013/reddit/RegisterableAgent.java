package competition.uu2013.reddit;

import ch.idsia.agents.Agent;
import ch.idsia.agents.AgentsPool;
import ch.idsia.utils.wox.serial.Easy;

import java.util.IllegalFormatException;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy
 * Date: Apr 11, 2009
 * Time: 10:47:47 AM
 package ch.idsia.ai.agents;
 */
public class RegisterableAgent extends BasicAIAgent
{
    private static Agent currentAgent = null;

    public RegisterableAgent(String s)
    {
        super();
        this.registerAgent(this);
    }

    public static void registerAgent(Agent agent)
    {
        AgentsPool.addAgent(agent);
    }

    public static void setAgent(Agent agent) {
        currentAgent = agent;
    }

    public static void registerAgent(String agentWOXName) throws IllegalFormatException
    {
        registerAgent(load(agentWOXName));
    }

    public static Agent load (String name) {
        Agent agent;
        try {
            agent = (Agent) Class.forName (name).newInstance ();
        }
        catch (ClassNotFoundException e) {
            System.out.println (name + " is not a class name; trying to load a wox definition with that name.");
            agent = (Agent) Easy.load(name);
        }
        catch (Exception e) {
            e.printStackTrace ();
            agent = null;
            System.exit (1);
        }
        return agent;
    }

    public static Set<String> getAgentsNames()
    {
        return AgentsPool.getAgentsHashMap().keySet();
    }

    public static Agent getAgentByName(String agentName)
    {
        // There is only one case possible;
        Agent ret = AgentsPool.getAgentsHashMap().get(agentName);
        if (ret == null)
            ret = AgentsPool.getAgentsHashMap().get(agentName.split(":")[0]);
        return ret;
    }

    public static Agent getAgent()
    {
        return currentAgent;
    }

}
