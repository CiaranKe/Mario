package competition.uu2013.prototypes;


import java.io.*;
import java.util.Stack;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;
import competition.uu2013.common.Sprites.MarioSim;
import competition.uu2013.common.hueristics.AStarSearch;
import competition.uu2013.common.level.Enemy;
import competition.uu2013.common.level.Map;
import competition.uu2013.common.level.WorldSim;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.Bagging;
import weka.classifiers.meta.MultiScheme;
import weka.classifiers.trees.NBTree;
import weka.core.Instance;
import weka.core.Instances;

public class IBkAgent extends MarioAIAgent implements Agent
{
    private Bagging nnClass;
    private Instances data;
    private String name;
    private AStarSearch search;
    private Stack<boolean[]> astarPlan;
    private boolean wasOnGround;
    private int jumpCounter;
    private float lastX;
    private int lastMode;
    private int stuck = 0;

    public IBkAgent(String name)
    {
        super(name);
    }

    public IBkAgent()
    {

        super("IBKAgent");
        this.setName("IBKAgent");
        this.astarPlan = new Stack<boolean[]>();
        this.reset();
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
        this.jumpCounter = 0;
        this.wasOnGround = false;
    }


    private void StoreInstance(byte [][] obs, String action)
    {

        StringBuilder s = new StringBuilder();
        for (int y = 0; y < obs.length; y++)
        {
            for(int x = 0; x < obs[y].length; x++)
            {
                if (y > (this.marioEgoCol-2) && y < (this.marioEgoCol+2))
                {
                    if (x > (this.marioEgoRow-2) && x > (this.marioEgoRow+2))
                    {
                        s.append(obs[y][x]+",");
                    }
                }
            }
        }
        s.append(Boolean.toString(isMarioAbleToJump).toUpperCase()+",");
        s.append(Boolean.toString(isMarioAbleToShoot).toUpperCase()+",");
        s.append(Boolean.toString(isMarioOnGround).toUpperCase()+",");
        s.append(action+"\n");
        try
        {
            FileWriter fos = new FileWriter("C:\\testing\\astarTraining.arff",true);
            fos.append(s.toString());
            fos.flush();
            fos.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        s = new StringBuilder();
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
                if (y > (this.marioEgoCol-2) && y < (this.marioEgoCol+2))
                {
                    if (x > (this.marioEgoRow-2) && x > (this.marioEgoRow+2))
                    {
                        array[counter] = observation[y][x];
                        counter++;
                    }
                }
            }
        }

        try
        {
            Instance i = this.createInstance(array);
            return (int) this.nnClass.classifyInstance(i);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return -1;
        }

    }


    public void openModel() throws Exception
    {
        String modelFileName = "C:\\testing\\IBk.model";
        InputStream is = new FileInputStream(modelFileName);
        ObjectInputStream objectInputStream = new ObjectInputStream(is);
        nnClass = (Bagging) objectInputStream.readObject();
        objectInputStream.close();

        BufferedReader reader = new BufferedReader(new FileReader("C:\\testing\\astarTraining.arff"));
        data = new Instances(reader);
        reader.close();
        data.setClassIndex(data.numAttributes() - 1);

        nnClass.buildClassifier(data);
    }

    @Override
    public boolean[] getAction()
    {
        //time recording
        long startTime = System.currentTimeMillis();
        //System.out.println("============================================================================================");
        //--------------------------------------------------------------------------------------------------
        //if this is the first turn, create our search and the working copy of the simulation
        if (search == null)
        {
            search = new AStarSearch(new WorldSim(new MarioSim(marioFloatPos[0],marioFloatPos[1], 0.0F, 3.0F), new Enemy(), new Map()));
            search.updateSim(marioFloatPos[0], marioFloatPos[1], isMarioAbleToJump, isMarioOnGround, wasOnGround, isMarioAbleToShoot, marioStatus, this.enemiesFloatPos, this.levelScene, action);

        }
        else
        {
            //Work around for issue with jumping
            if (isMarioOnGround && isMarioAbleToJump)
            {
                jumpCounter = 7;
            }

            //update the simulation
            //System.out.println("Updating sim");
            search.updateSim(marioFloatPos[0], marioFloatPos[1], isMarioAbleToJump, isMarioOnGround, wasOnGround, isMarioAbleToShoot, marioStatus, this.enemiesFloatPos, this.levelScene, action);
            //record the current on ground status
            this.wasOnGround = isMarioOnGround;
            //System.out.println("Completing: " + Action.nameAction(action));

            action = this.createAction(this.classify(this.mergedObservation));

            if (lastX == marioFloatPos[0])
            {
                //get the next action from the search instead
                action = search.pathFind(marioFloatPos[0],marioFloatPos[1],startTime, false);
                stuck++;
                if (stuck > 5)
                {
                    action = new boolean[Environment.numberOfKeys];
                    action[Mario.KEY_JUMP] = true;
                    stuck = 0;
                }
            }

            else if ((Math.abs(marioFloatPos[0] -lastX) > 1) && (this.lastMode >= marioMode))
            {
                this.StoreInstance(this.mergedObservation, "\'"+Action.nameAction(action)+"\'");
            }
            lastX = marioFloatPos[0];
            this.lastMode = marioMode;
        }


        //--------------------------------------------------------------------------------------------------
        //System.out.println("Method Time: " + (System.currentTimeMillis() - startTime));
        //System.out.println("============================================================================================");
        return action;
    }

    private boolean[] createAction(int classify)
    {

        boolean [] classifiedAction =  new boolean[Environment.numberOfKeys];

        switch (classify)
        {
            case 0: break;
            case 1: classifiedAction[Mario.KEY_RIGHT] =true; break;
            case 2: classifiedAction[Mario.KEY_RIGHT] = classifiedAction[Mario.KEY_SPEED] = true; break;
            case 3: classifiedAction[Mario.KEY_RIGHT] = classifiedAction[Mario.KEY_SPEED] = classifiedAction[Mario.KEY_JUMP] = true; break;
            case 4: classifiedAction[Mario.KEY_RIGHT] = classifiedAction[Mario.KEY_JUMP] = true; break;

        }

        return classifiedAction;
    }

    @Override
    public String getName() {
        return "IBkAgent";
    }

    @Override
    public void setName(String _name) {
        this.name = _name;
    }
}
