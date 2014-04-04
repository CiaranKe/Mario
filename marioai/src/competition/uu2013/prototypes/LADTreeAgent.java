package competition.uu2013.prototypes;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;
import weka.classifiers.lazy.IBk;
import weka.core.Instance;
import weka.core.Instances;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: fluffy
 * Date: 30/03/14
 * Time: 20:02
 * To change this template use File | Settings | File Templates.
 */
public class LADTreeAgent implements Agent
{
    private IBk ladClass;
    private Instances data;
    private Environment observation;
    private String name;
    private float lastX;

    public LADTreeAgent()
    {
        this.setName("LADTreeAgent");
        this.lastX = 0.0F;
        reset();
    }

    private boolean[] buildAction(int id)
    {
        boolean[] newAction = new boolean[Environment.numberOfKeys];
        switch(id)
        {
            case 0:  //shoot
                newAction[Mario.KEY_JUMP] = false;
                newAction[Mario.KEY_LEFT] = false;
                newAction[Mario.KEY_RIGHT] = false;
                newAction[Mario.KEY_SPEED] = true;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                break;
            case 1:  //right
                newAction[Mario.KEY_JUMP] = false;
                newAction[Mario.KEY_LEFT] = false;
                newAction[Mario.KEY_RIGHT] = true;
                newAction[Mario.KEY_SPEED] = false;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                break;
            case 2:  //jump
                newAction[Mario.KEY_JUMP] = true;
                newAction[Mario.KEY_LEFT] = false;
                newAction[Mario.KEY_RIGHT] = false;
                newAction[Mario.KEY_SPEED] = false;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                break;
            case 3: //right jump
                newAction[Mario.KEY_JUMP] = true;
                newAction[Mario.KEY_LEFT] = false;
                newAction[Mario.KEY_RIGHT] = true;
                newAction[Mario.KEY_SPEED] = false;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                break;
            case 4:  //right speed
                newAction[Mario.KEY_JUMP] = false;
                newAction[Mario.KEY_LEFT] = false;
                newAction[Mario.KEY_RIGHT] = true;
                newAction[Mario.KEY_SPEED] = true;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                break;
            case 5: //left
                newAction[Mario.KEY_JUMP] = false;
                newAction[Mario.KEY_LEFT] = true;
                newAction[Mario.KEY_RIGHT] = false;
                newAction[Mario.KEY_SPEED] = false;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                break;
            case 6: //jump speed
                newAction[Mario.KEY_JUMP] = true;
                newAction[Mario.KEY_LEFT] = false;
                newAction[Mario.KEY_RIGHT] = false;
                newAction[Mario.KEY_SPEED] = true;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                break;
            case 7: //right jump speed
                newAction[Mario.KEY_JUMP] = true;
                newAction[Mario.KEY_LEFT] = false;
                newAction[Mario.KEY_RIGHT] = true;
                newAction[Mario.KEY_SPEED] = true;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                break;
            case 8: //LEFT_SPEED
                newAction[Mario.KEY_JUMP] = false;
                newAction[Mario.KEY_LEFT] = true;
                newAction[Mario.KEY_RIGHT] = false;
                newAction[Mario.KEY_SPEED] = true;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                break;
            case 9: //LEFT_JUMP
                newAction[Mario.KEY_JUMP] = true;
                newAction[Mario.KEY_LEFT] = true;
                newAction[Mario.KEY_RIGHT] = false;
                newAction[Mario.KEY_SPEED] = false;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                break;
            case 10: //LEFT_JUMP_SPEED
                newAction[Mario.KEY_JUMP] = true;
                newAction[Mario.KEY_LEFT] = true;
                newAction[Mario.KEY_RIGHT] = false;
                newAction[Mario.KEY_SPEED] = true;
                newAction[Mario.KEY_UP] = false;
                newAction[Mario.KEY_DOWN] = false;
                break;
        }

        return newAction;
    }

    public void reset()
    {
        try
        {
            this.openModel();
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private Instance createInstance(byte [] array)
    {
        double[] vals = new double[this.data.numAttributes()];

        for (int x = 0; x < array.length; x++)
        {
            vals[x] = array[x];
        }

        vals[this.data.numAttributes() -1] = Instance.missingValue();
        Instance newInstance = new Instance(1.0,vals);
        newInstance.setDataset(this.data);
        return newInstance;

    }

    private int classify(byte [][] observation)
    {
        byte [] array = new byte[this.data.numAttributes()];
        int counter = 0;

        for (int y = 0; y < observation.length; y++)
        {
            for (int x = 0; x < observation[y].length; x++)
            {
                array[counter] = observation[y][x];
                counter++;
            }
        }

        try
        {
            Instance i = this.createInstance(array);
            return (int) this.ladClass.classifyInstance(i);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return -1;
        }

    }


    public void openModel() throws Exception
    {
        String modelFileName = "C:\\testing\\IBkHuman.model";
        InputStream is = new FileInputStream(modelFileName);
        ObjectInputStream objectInputStream = new ObjectInputStream(is);
        ladClass = (IBk) objectInputStream.readObject();
        objectInputStream.close();

        BufferedReader reader = new BufferedReader(new FileReader("C:\\testing\\humanTraining2.arff"));
        data = new Instances(reader);
        reader.close();
        data.setClassIndex(data.numAttributes() - 1);
        ladClass.buildClassifier(data);
    }

    @Override
    public boolean[] getAction()
    {
        boolean [] action = new boolean[Environment.numberOfKeys];
        if (this.observation.getEnemiesObservationZ(2)[this.observation.getReceptiveFieldHeight()/2][(this.observation.getReceptiveFieldWidth()/2)+1] != 0)
        {
            if (this.observation.isMarioAbleToShoot())
            {
                action = this.buildAction(0);
            }
            else if (this.observation.isMarioAbleToJump())
            {
                action = this.buildAction(2);
            }
            else
            {
                action = this.buildAction(5);
            }
        }
        else
        {
            action = this.buildAction(this.classify(observation.getMergedObservationZZ(2,1)));
        }
        action[Mario.KEY_JUMP] = this.observation.isMarioAbleToJump() && action[Mario.KEY_JUMP];
        return  action;
    }

    @Override
    public void integrateObservation(Environment environment) {
        this.observation = environment;
    }

    @Override
    public void giveIntermediateReward(float intermediateReward) {
        //Not implemented
    }

    @Override
    public void setObservationDetails(int rfWidth, int rfHeight, int egoRow, int egoCol) {
        //Not implemented
    }

    @Override
    public String getName() {
        return "LADTreeAgent";
    }

    @Override
    public void setName(String _name) {
        this.name = _name;
    }
}