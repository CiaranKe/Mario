package competition.uu2013.common.hueristics;

import competition.uu2013.common.Sprites.MarioSim;
import competition.uu2013.common.level.WorldSim;
import competition.uu2013.prototypes.Action;

import java.util.ArrayList;
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
    private final static int TICKS_TO_PLAN = 2;
    private ArrayList<SearchNode> closed;
    private SortedList<SearchNode> open;
    private Stack<boolean []> plan;
    private SearchNode working;
    private boolean rePlanNewEnemy;
    private boolean rePlanLostSync;
    private int sceneWidth;
    private int sceneHeight;


    public AStarSearch(WorldSim _sim)
    {
        this.open = new SortedList<SearchNode>();
        this.closed = new ArrayList<SearchNode>();
        this.plan = new Stack<boolean[]>();
        this.working = new SearchNode(1,null,_sim);
        this.sceneWidth = 11;
        this.sceneHeight = 11;
    }


    public boolean [] pathFind(float x, float y, long startTime)
    {

        if (rePlanLostSync || rePlanNewEnemy || plan.empty())
        {
            System.out.println("Lost Sync: "+rePlanLostSync+", New Enemy: "+rePlanNewEnemy+", Empty plan: "+plan.empty());
            open.clear();
            closed.clear();
            plan.clear();
            open.add(working);
            SearchNode current = working;
            SearchNode best = working;
            SearchNode furthest = working;
            working.setGoal(x,y);
            working.estimateCost();

            System.out.println("Goal is: " +(x + ((11 * 16)-16)));
            System.out.println("Open size: " + open.size()+ ", Found goal: " + current.isGoal() + ", Time left: " + (40 - (System.currentTimeMillis() - startTime)) );
            while ((open.size() != 0 && !current.isGoal()) )
            {
                System.out.println("Open size: " + open.size()+ ", Found goal: " + current.isGoal() + ", Time left: " + (40 - (System.currentTimeMillis() - startTime)) );
                if (open.size() > 20)
                {
                    open.prune(20);
                }
                current = open.getFirst();
                open.remove(current);
                closed.add(current);
                for (SearchNode n : current.generateNeighbors(x,y))
                {
                    if (this.isVisted(n))
                    {
                        open.remove(n);
                        closed.add(n);
                    }
                    else
                    {
                        if (!open.contains(n))
                        {
                            open.add(n);
                        }
                        if ((n.getCost() > best.getCost()) || (n.getCost() >= best.getCost() && n.getMarioSim().getX() > best.getMarioSim().getX()) )
                        {
                            best = n;
                        }
                        if (n.getMarioSim().getX() > furthest.getMarioSim().getX())
                        {
                            furthest = n;
                        }
                    }
                }
                System.out.println("==================================== Open size: " + open.size());
            }

            if (!best.isGoal() || !current.isGoal())
            {
                best = furthest;
                System.out.println("Didn't find goal");
            }
            this.extractPlan(best);
        }
        System.out.println("==================================== Plan size: " + plan.size());
        return plan.pop();
    }

    private boolean isVisted(SearchNode n)
    {
        int diff = 2;

        for (SearchNode c : closed)
        {
            if ((Math.abs(n.getMarioSim().getX() - c.getMarioSim().getX()) < diff)&&(Math.abs(n.getMarioSim().getY() - c.getMarioSim().getY()) < diff))
            {
                return true;
            }
        }
        return false;
    }


    public void extractPlan(SearchNode _current)
    {
        while (_current != null)
        {
            if (_current.getParent() == null)
            {
                break;
            }
            System.out.print("Adding " + Action.nameAction(_current.getAction()) + " Score: " + _current.getCost() + ", ");
            plan.push(_current.getAction());
            _current = _current.getParent();
        }
        System.out.println();
    }


    public void updateSim(float x, float y, boolean isMarioAbleToJump, boolean isMarioOnGround, boolean isMarioAbleToShoot, boolean marioStatus, float [] newEnemies, byte [][] scene, boolean [] _lastAction)
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

    public boolean withinScope(float marioX, MarioSim sim)
    {
        float lookAHead = marioX + (sceneWidth * 16);

        if ((sim.getX() < lookAHead))
        {
            return true;
        }
        return false;
    }
}
