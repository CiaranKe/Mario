package competition.uu2013.common.hueristics;

import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;
import competition.uu2013.common.Sprites.SpriteSim;
import competition.uu2013.common.level.Map;
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
            //working.setGoal(_x, _y, sceneWidth, sceneHeight);
            openList.add(working);
            SearchNode.resetCounter();
            SearchNode current = null;
            SearchNode farthest = working;
            float farthestX = 0;

            System.out.println("Goal was: " + (_x+(sceneWidth * 16)));

            while (openList.size() != 0  && (System.currentTimeMillis() - startTime) < 40)
            {

                current = openList.getFirst();
                System.out.println("Evaluating: " + current.toString());

                if (current.isGoal())
                {
                    System.out.println("Ran " + loopCounter + " Iterations, open size: " + openList.size() + ", Time taken: " + (System.currentTimeMillis() - startTime));
                    this.extractPlan(current);
                    return plan.pop();
                }

                openList.remove(current);
                closedList.add(current);

                if (current.getPredictedXY()[0] > farthestX)
                {
                    farthest = current;
                    farthestX = current.getPredictedXY()[0];
                }

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
                            n.estimateHCost(current.getMarioSim().getMarioMode());
                        }
                    }
                }
                loopCounter++;
            }

            if (!current.isGoal())
            {
                System.out.println("Didn't find goal");
                System.out.println("Farthest found was : " + (farthestX - working.getPredictedXY()[0]) + " Pixels ahead");
                extractPlan(farthest);
            }
            System.out.println("Ran " + loopCounter + " Iterations, open size: " + openList.size() + ", Time taken: " + (System.currentTimeMillis() - startTime));
        }
        try
        {
            return plan.pop();
        }
        catch (EmptyStackException emptyStackException)
        {
            boolean [] failedSearch = new boolean[Environment.numberOfKeys];
            failedSearch[Mario.KEY_JUMP] = !working.getMarioSim().isMarioAbletoJump();

            if (working.getMap().getViewAt(working.getMarioSim().getX()+ Map.CELL_SIZE, working.getMarioSim().getY()) != 0)
            {
                failedSearch[Mario.KEY_LEFT] = true;
            }
            else
            {
                failedSearch[Mario.KEY_RIGHT] = true;
            }

            return failedSearch;

        }
    }

    private void extractPlan(SearchNode _current)
    {
        System.out.print("Plan: ");
        Stack<boolean[]> tempStack = new Stack<boolean[]>();

        while (_current.getParent() !=null )
        {
            if (_current.getParent() == null)
            {
                break;
            }
            System.out.print(_current.getID() + ": " +Action.nameAction(_current.getAction()) + ", ");
            tempStack.push(_current.getAction());
            _current = _current.getParent();
        }
        System.out.println();
        plan.clear();
        while (!tempStack.empty())
        {
            plan.push(tempStack.pop());
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
