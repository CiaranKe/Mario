package competition.uu2013.prototypes;

import ch.idsia.benchmark.mario.environments.Environment;
import competition.uu2013.common.Action;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: fluffy
 * Date: 29/01/14
 * Time: 15:38
 * To change this template use File | Settings | File Templates.
 */
public class PixelNode implements  java.io.Serializable
{
    private HashMap<boolean[],PixelNode> children;
    private final int coins;
    private final float distance;
    private final int flowers;
    private final int hiddenBlocks;
    private final int totalKills;
    private final int killsFire;
    private final int killsShell;
    private final int killsStomp;
    private final float[] floatPos;
    private final int timeRemaining;
    private final int marioStatus;
    private final int mushrooms;
    private String id;

    public PixelNode(Environment environment, boolean[] lastAction)
    {

        this.coins = environment.getEvaluationInfo().coinsGained;
        this.mushrooms = environment.getEvaluationInfo().mushroomsDevoured;
        this.distance = environment.getEvaluationInfo().computeDistancePassed();
        this.flowers = environment.getEvaluationInfo().flowersDevoured;
        this.hiddenBlocks = environment.getEvaluationInfo().hiddenBlocksFound;
        this.totalKills = environment.getKillsTotal();
        this.killsFire =  environment.getKillsByFire();
        this.killsShell = environment.getKillsByShell();
        this.killsStomp = environment.getKillsByStomp();
        this.floatPos =  environment.getMarioFloatPos();
        this.timeRemaining = environment.getEvaluationInfo().timeLeft;
        this.marioStatus = environment.getMarioStatus();
        this.id = this.buildID(lastAction);

        this.children = new HashMap<boolean[], PixelNode>();
        //this.children.put(Action.createAction(Action.WAIT),null);
        this.children.put(Action.createAction(Action.JUMP),null);
        this.children.put(Action.createAction(Action.JUMP_SPEED),null);
        //this.children.put(Action.createAction(Action.UP),null);
        this.children.put(Action.createAction(Action.SHOOT),null);
        //this.children.put(Action.createAction(Action.DUCK),null);
        this.children.put(Action.createAction(Action.RIGHT),null);
        this.children.put(Action.createAction(Action.RIGHT_JUMP_SPEED),null);
        this.children.put(Action.createAction(Action.RIGHT_JUMP),null);
        this.children.put(Action.createAction(Action.RIGHT_SPEED),null);
        //this.children.put(Action.createAction(Action.LEFT),null);
        //this.children.put(Action.createAction(Action.LEFT_JUMP),null);
        //this.children.put(Action.createAction(Action.LEFT_JUMP_SPEED),null);
        //this.children.put(Action.createAction(Action.LEFT_SPEED),null);
    }

    public static PixelNode findNode(PixelNode pixelNode)
    {
        try
        {
            FileInputStream fis = new FileInputStream(new File("G:\\testing\\" + pixelNode.getId()+".ser"));
            ObjectInputStream oos = new ObjectInputStream(fis);

            PixelNode newPixelNode = (PixelNode) oos.readObject();
            return newPixelNode;
        }
        catch (FileNotFoundException fnf)
        {
            return null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public float getCost()
    {
        return -((this.distance/16) + (this.flowers * 64) + (this.marioStatus * 32) + (this.mushrooms * 58)
                + (this.coins * 16) + (this.hiddenBlocks * 24) + (this.totalKills * 42) + (this.killsStomp * 12)
                + (this.killsFire * 4) + (this.killsShell * 17) + (this.timeRemaining * 8));
    }

    public static PixelNode createNode(PixelNode pixelNode)
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(new File("G:\\testing\\" + pixelNode.getId() +".ser"));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(pixelNode);
            oos.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return pixelNode;
    }

    private String buildID(boolean[] lastAction)
    {
        StringBuilder sb = new StringBuilder();

        sb.append((int)this.floatPos[0] + "_" + (int)this.floatPos[1]+"_");
        for (int x = 0; x < lastAction.length; x++)
        {
            sb.append(lastAction[x]+"_");
        }

        return sb.toString();
    }

    public String getId()
    {
        return this.id;
    }

    public boolean[] getNextAction()
    {

        boolean [] nextAction = new boolean[Environment.numberOfKeys];
        float cost = 0;

        Iterator actionIterator = children.entrySet().iterator();

        while (actionIterator.hasNext())
        {
            Map.Entry nodeAction = (Map.Entry) actionIterator.next();
            if (nodeAction.getValue() == null)
            {
                nextAction = (boolean[]) nodeAction.getKey();
                break;
            }
            else
            {
                if (((PixelNode)nodeAction.getValue()).getCost() < cost)
                {
                    cost = ((PixelNode)nodeAction.getValue()).getCost();
                    nextAction = (boolean[]) nodeAction.getKey();
                }
            }
        }
        return nextAction;
    }

    public void appendChild(boolean[] action, PixelNode pixelNode)
    {
        children.put(action, pixelNode);
        PixelNode.createNode(this);
    }
}