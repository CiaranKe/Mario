package competition.uu2013.common.hueristics;

import competition.uu2013.common.Sprites.MarioSim;
import competition.uu2013.common.level.WorldSim;
import competition.uu2013.prototypes.Action;

import java.util.ArrayList;
import java.util.Stack;

public class AStarSearchAttempt1
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

    public AStarSearchAttempt1(WorldSim _sim)
    {
        this.open = new SortedList<SearchNode>();
        this.closed = new ArrayList<SearchNode>();
        this.plan = new Stack<boolean[]>();
        this.open.add(this.working);
        this.sceneWidth = 11;
        this.sceneHeight = 11;
    }


    public boolean [] pathFind(float x, float y, long remainingTime)
    {
        /*
        long time = System.currentTimeMillis();
        boolean rePlan = rePlanLostSync || rePlanNewEnemy || !(plan.size() > 0);

        if(rePlan)
        {
            System.out.println("Replanning: Lost Sync: " + rePlanLostSync + ", New Enemy: " + rePlanNewEnemy + ", Plan empty (" + plan.size()  + "): " + !(plan.size() > 0) );

            int ticks = 0;
            plan.clear();
            closed.clear();
            open.clear();
            open.add(working);
            SearchNode current = null;
            SearchNode furthest = null;
            while ((open.size() != 0) && (ticks <= TICKS_TO_PLAN))
            {
                ticks++;
                current = open.getFirst();
                open.remove(current);
                if(!withinScope(x,current.getMarioSim()))
                {
                    break;
                }

                for (SearchNode s : current.generateNeighbors(x,y))
                {
                    //s.setCost(s.getCost() + current.getCost());
                    //System.out.println("Evaluating: " + Action.nameAction(s.getAction()) + " Cost: " + s.getCost());
                    open.add(s);
                    if (furthest == null || furthest.getMarioSim().getX() < s.getMarioSim().getX())
                    {
                        furthest = s;
                    }
                }
                closed.add(current);
            }
            this.extractPlan(furthest);
        }
        return plan.pop();
        */
        return null;
    }

    public void extractPlan(SearchNode _current)
    {
        /*
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
        */
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
