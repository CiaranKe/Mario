
package competition.uu2013.prototypes;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;


/**
 * The Class RandomAgent. An agent that that selects it's actions based on the values of a randomly generated variable
 * 
 * @author Ciaran Kearney
 * @version 1.0
 * @since 15/12/2013
 */
public class RandomAgent implements Agent
{

	
    private String name; //NAme for this Agent displayed on screen.

    /**
     * Instantiates a new random agent, and sets the agent name.
     */
    public RandomAgent()
    {
        this.name = "RandomAgent";
    }


    /**
     * Returns the action selected by this agent
     * 
     * @see ch.idsia.agents.Agent#getAction()
     */
    @Override
    public boolean[] getAction ()
    {
    	//create an empty action
        boolean  [] action = new boolean[Environment.numberOfKeys];

        //generate a random vaiable
        int num = (int) (Math.random() * 100);
        
        //create the action, prefer actions that move Mario to the right. 
        action[Mario.KEY_JUMP] = num > 30;
        action[Mario.KEY_RIGHT] = num > 10;
        action[Mario.KEY_LEFT] = num < 10;
        action[Mario.KEY_SPEED] = num > 50;
        action[Mario.KEY_DOWN] = num > 90;
        System.out.println("Number: " + num);

        return action;
    }

    /** 
     *  Reads the values provided by the environment interface. 
     *  This agent does nothing with these
     * 
     * @see ch.idsia.agents.Agent#integrateObservation(ch.idsia.benchmark.mario.environments.Environment)
     */
    @Override
    public void integrateObservation(Environment environment)
    {
        //does nothing
    }

    /** 
     * Not implemented in this agent.
     * 
     * @see ch.idsia.agents.Agent#giveIntermediateReward(float)
     */
    @Override
    public void giveIntermediateReward(float intermediateReward)
    {
        //does nothing
    }

    /** 
     * Called at the start of the level. this agent does not implement this method.
     * 
     * @see ch.idsia.agents.Agent#reset()
     */
    @Override
    public void reset() 
    {
        //does nothing
    }

    /** 
     * Not implemented
     * @see ch.idsia.agents.Agent#setObservationDetails(int, int, int, int)
     */
    @Override
    public void setObservationDetails(int rfWidth, int rfHeight, int egoRow, int egoCol)
    {
        //does nothing
    }

    /** 
     * Returns the name of the agent
     * @see ch.idsia.agents.Agent#getName()
     */
    @Override
    public String getName()
    {
        return this.name;
    }

    /** 
     *  Sets the name of the agent
     * @see ch.idsia.agents.Agent#setName(java.lang.String)
     */
    @Override
    public void setName(String _name)
    {
        this.name = _name;
    }
}
