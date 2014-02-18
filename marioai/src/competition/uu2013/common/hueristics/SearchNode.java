package competition.uu2013.common.hueristics;

import ch.idsia.benchmark.mario.engine.sprites.Mario;
import competition.uu2013.common.Sprites.MarioSim;
import competition.uu2013.common.level.Action;
import competition.uu2013.common.level.WorldSim;

import java.util.ArrayList;

public class SearchNode  implements Comparable<SearchNode>
{
    final int BEFORE = -1;
    final int EQUAL = 0;
    final int AFTER = 1;


    private SearchNode parent;
    private WorldSim sim;
    private boolean [] action;
    private int id;
    private static int idCounter;
    private float gCost;
    private float hCost;
    private float predX;
    private float predY;
    private float goalX;
    private float goalY;

    static
    {
        idCounter = 1;
    }

    public SearchNode(int _id, SearchNode _parent, WorldSim _sim)
    {
        this.id = _id;
        this.parent = _parent;
        gCost = 0;
        if (_parent != null)
        {
            gCost = parent.gCost;
        }
        this.sim = _sim;
    }

    @Override
    public int compareTo(SearchNode s)
    {
        if (this.getCost() > s.getCost())
        {
            return AFTER;
        }
        else if (this.getCost() < s.getCost())
        {
            return BEFORE;
        }
        return EQUAL;
    }

    public int getId()
    {
        return this.id;
    }

    public boolean [] getAction()
    {
        return this.action;
    }

    public void move(float currentX, float currentY)
    {
        sim.move(this.action,currentX,currentY);
        this.predX = sim.getMarioLocation()[0];
        this.predY = sim.getMarioLocation()[1];
    }

    public float [] getPredictedXY()
    {
        return new float[] {this.predX, this.predY};
    }

    public boolean updateSim(float x, float y, boolean isMarioAbleToJump, boolean isMarioOnGround, boolean isMarioAbleToShoot, boolean marioStatus, float [] newEnemies, byte [][] scene)
    {
        return sim.syncLocation(x,y,isMarioAbleToJump,isMarioOnGround,isMarioAbleToShoot,marioStatus,newEnemies,scene);
    }

    public MarioSim getMarioSim() {
        return sim.getMarioSim();
    }

    public static int debugPos;

    public ArrayList<SearchNode> generateNeighbors(float _x, float _y)
    {
        MarioSim marioSim = getMarioSim();
        ArrayList<SearchNode> newNodes = new ArrayList<SearchNode>();

        for (boolean[] a: Action.getPossibleActions(marioSim.isMarioAbletoJump(), marioSim.getX(), marioSim.getY(), this.sim.getMap(),this))
        {
            if (!Action.ignore(a,this.sim))
            {
                SearchNode s = new SearchNode(++idCounter,this,this.sim.clone());
                s.setGoal(_x,_y);
                s.setAction(a);
                s.gCost = gCost + 1;
                float x = s.getMarioSim().getX();
                s.move(this.getMarioSim().getX(), this.getMarioSim().getY());
                s.estimateCost();
                newNodes.add(s);
                System.out.println("Created (" + s.getId()+") : Parent : " +s.getParent().getId() + ", Action: "   + Action.nameAction(a) + ", Score: " + s.getCost() + ", X: " +s.getMarioSim().getX() + ", Y: " + s.getMarioSim().getY() );
            }
        }
        return newNodes;
    }

    public void setGoal(float _x, float _y)
    {
        this.goalX = _x + (11 * 16);
        this.goalY = _y - (11  * 16);
    }

    public boolean isGoal()
    {
        if (this.getMarioSim().getX() > (this.goalX - 32))
        {
            return true;
        }
        return false;
    }

    public void setAction(boolean[] _action)
    {
        this.action = _action;
    }

    public SearchNode getParent() {
        return parent;
    }

    public void setCost(float cost)
    {
        this.hCost = cost;
    }

    protected void estimateCost()
    {
        /*
            //Euclidean Distance
            dx = targetX - currentX;
            dy = targetY - currentY;
            heuristic = sqrt((dx*dx)+(dy*dy));


            //Manhattan Distance
            dx = abs(targetX - currentX);
            dy = abs(targetY - currentY);
            heuristic = dx+dy;


        MarioSim marioSim = this.getMarioSim();

        float distX = this.goalX - marioSim.getX();
        float distY = this.goalY - marioSim.getY();
        this.cost = Math.sqrt((distX*distX)+(distY*distY));
        */

        MarioSim marioSim = this.getMarioSim();

        if (marioSim.isDead())
        {
            this.hCost = Float.POSITIVE_INFINITY;
            return;
        }

        //taken from GIC2009Competition.pdf
        for (float x = marioSim.getX(), xa= marioSim.getXA(); x < this.goalX;)
        {
            xa+= 1.2;
            x+=xa;
            xa*=0.89F;
            this.hCost++;
        }
        this.hCost = ((marioSim.getFacing() < 0) ? this.hCost : (this.hCost*2));
    }

    public float getCost()
    {
        return this.gCost + hCost;
    }
}
