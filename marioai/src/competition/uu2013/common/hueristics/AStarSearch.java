package competition.uu2013.common.hueristics;

import competition.uu2013.common.Sprites.MarioSim;
import competition.uu2013.common.Sprites.SpriteSim;
import competition.uu2013.common.level.WorldSim;
import competition.uu2013.prototypes.Action;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 * User: fluffy
 * Date: 18/02/14
 * Time: 15:00
 * To change this template use File | Settings | File Templates.
 */
public class AStarSearch
{

    private boolean rePlanNewEnemy;
    private boolean rePlanLostSync;
    private int sceneWidth;
    private int sceneHeight;
    private SortedList<SearchNode> openList;
    private SortedList<SearchNode> closedList;
    private Stack<boolean[]> plan;
    private SearchNode working;

    public AStarSearch(WorldSim sim)
    {
        this.working = new SearchNode(1,sim);
        this.openList = new SortedList<SearchNode>();
        this.closedList = new SortedList<SearchNode>();
        this.plan = new Stack<boolean[]>();
    }


    //TODO: Block parent that caused us to get hurt.


    public boolean[] pathFind(float _x, float _y, long startTime)
    {
        if (rePlanNewEnemy || rePlanLostSync || plan.empty())
        {
            System.out.println("Lost Sync: "+rePlanLostSync+", New Enemy: "+rePlanNewEnemy+", Empty plan: "+plan.empty());

            int loopCounter = 1;
            openList.clear();
            closedList.clear();
            plan.clear();
            working.setGCost(0);
            working.setGoal(_x, _y, sceneWidth, sceneHeight);
            openList.add(working);
            SearchNode.resetCounter();
            SearchNode current = null;
            SearchNode farthest = working;

            System.out.println("Goal was: " + (_x+(sceneWidth * 16)));

            while (openList.size() != 0  && (System.currentTimeMillis() - startTime) < 39)
            {
                current = openList.getFirst();
                try
                {
                    System.out.println("Node:"+ current.getID() + ", Parent: "+ current.getParent().getID() + ", Score: " + current.getGCost()+","+current.getHCost()+","+current.getFCost() +  ", Action"+ Action.nameAction(current.getAction()) + ", Blocked: " + current.isBlocked() + ", Goal: " + current.getGoal() +", X: " + current.getPredictedXY()[0]);
                }
                catch (NullPointerException e)
                {
                    System.out.println("Node:"+ current.getID() + ", Score: " + current.getFCost() +  ", Action"+ Action.nameAction(current.getAction()) + ", Goal: " + current.getGoal() +", X: " + current.getPredictedXY()[0]);
                }


                if (current.isGoal())
                {
                    System.out.println("Ran " + loopCounter + " Iterations, open size: " + openList.size() + ", Time taken: " + (System.currentTimeMillis() - startTime));
                    this.extractPlan(current);
                    return plan.pop();
                }

                openList.remove(current);
                closedList.add(current);

                for (SearchNode n : current.generateChildren(_x,_y,sceneWidth,sceneHeight))
                {
                    boolean isBetter;

                    if (closedList.contains(n))
                    {
                        continue;
                    }
                    if (!n.isBlocked())
                    {
                        if (!openList.contains(n))
                        {
                            openList.add(n);
                            isBetter = true;
                        }
                        else if (n.getFCost() < current.getFCost())
                        {
                            isBetter = true;
                        }
                        else
                        {
                            isBetter = false;
                        }
                        if (isBetter)
                        {
                            n.setParent(current);
                            n.setGCost(current.getGCost());
                            n.estimateHCost();
                        }

                        if (farthest.getPredictedXY()[0] > current.getPredictedXY()[0])
                        {
                            farthest = current;
                        }
                    }
                }
                loopCounter++;
            }

            if (!current.isGoal())
            {
                System.out.println("Didn't find goal");
            }
            System.out.println("Ran " + loopCounter + " Iterations, open size: " + openList.size() + ", Time taken: " + (System.currentTimeMillis() - startTime));
        }
        try
        {
            return plan.pop();
        }
        catch (EmptyStackException e)
        {
            return new boolean[6];
        }
    }

    private void extractPlan(SearchNode current)
    {
        System.out.print("Plan: ");
        while (current != null)
        {
            if (current.getParent() == null)
            {
                break;
            }
            plan.push(current.getAction());
            System.out.print(Action.nameAction(current.getAction()) + ",");
            current = current.getParent();
        }
    }


    public void updateSim(float x, float y, boolean isMarioAbleToJump, boolean isMarioOnGround, boolean isMarioAbleToShoot, int marioStatus, float [] newEnemies, byte [][] scene, boolean [] _lastAction)
    {

        this.sceneWidth = scene[0].length / 2;
        this.sceneHeight = scene.length / 2;
        working.setAction(_lastAction);
        working.move(x, y);
        rePlanNewEnemy = working.updateSim(x,y,isMarioAbleToJump,isMarioOnGround,isMarioAbleToShoot,marioStatus,newEnemies,scene);

        float [] predXY = working.getPredictedXY();

        rePlanLostSync = false;
        if (x != predXY[0] || y != predXY[1])
        {
            rePlanLostSync = true;
        }
    }

    public float getPredictedX()
    {
        return working.getPredictedXY()[0];
    }

    public float getPredictedY()
    {
        return working.getPredictedXY()[1];
    }

    public ArrayList<SpriteSim> getEnemySims() throws CloneNotSupportedException
    {
        return working.getEnemySims();
    }
}
