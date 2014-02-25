package competition.uu2013.common.hueristics;

import ch.idsia.benchmark.mario.engine.sprites.Mario;
import competition.uu2013.common.Sprites.MarioSim;
import competition.uu2013.common.Sprites.SpriteSim;
import competition.uu2013.common.level.Map;
import competition.uu2013.common.level.WorldSim;
import competition.uu2013.prototypes.Action;

import java.util.ArrayList;

public class SearchNode implements SortedListItem<SearchNode>
{

    private static int idCounter;

    private boolean [] action;
    private WorldSim sim;
    private float predX;
    private float predY;
    private int gCost;
    private float hCost;
    private float goalX;
    private float goalY;
    private SearchNode parent;
    private boolean blocked;
    private int id;

    static
    {
        idCounter =1;
    }

    public SearchNode(int _id, WorldSim _sim)
    {
        this.id = _id;
        this.sim = _sim;
    }


    public float [] getPredictedXY()
    {
        return new float[] {this.predX, this.predY};
    }

    public boolean updateSim(float x, float y, boolean isMarioAbleToJump, boolean isMarioOnGround, boolean isMarioAbleToShoot, int marioStatus, float [] newEnemies, byte [][] scene)
    {
        return sim.syncLocation(x,y,isMarioAbleToJump,isMarioOnGround,isMarioAbleToShoot,marioStatus,newEnemies,scene);
    }

    public void setAction(boolean[] _action)
    {
        this.action = _action;
    }

    public boolean[] getAction()
    {
        return this.action;
    }

    public void move(float currentX, float currentY)
    {
        sim.move(this.action,currentX,currentY);
        this.predX = sim.getMarioLocation()[0];
        this.predY = sim.getMarioLocation()[1];
    }

    public void setGCost(int _GCost)
    {
        this.gCost = _GCost + 1;
    }

    public int getGCost()
    {
        return this.gCost;
    }

    public float getFCost()
    {
        return this.gCost + this.hCost;
    }

    public boolean isGoal()
    {
        return (this.sim.getMarioSim().getX() > this.goalX);
    }

    public void setGoal(float _x, float _y, int sceneWidth, int sceneHeight)
    {
        this.goalX =  (_x+(sceneWidth * 16));
    }

    public void setParent(SearchNode _parent)
    {
        this.parent = _parent;
    }

    public SearchNode getParent()
    {
        return this.parent;
    }


    public SortedList<SearchNode> generateChildren(float _x, float _y, int sceneWidth, int sceneHeight) {
        SortedList<SearchNode> children = new SortedList<SearchNode>();

        for (boolean[] action : Action.getPossibleActions(this.sim))
        {
            SearchNode s = new SearchNode(++idCounter,this.sim.clone());
            s.setGoal(_x, _y, sceneWidth, sceneHeight);
            s.setAction(action);
            s.setParent(this);
            s.setBlocked(this.sim.getMarioSim().getMarioMode());
            s.setGCost(this.gCost);
            s.move(this.sim.getMarioSim().getX(), this.sim.getMarioSim().getY());
            s.estimateHCost();
            children.add(s);
        }

        return children;
    }

    private void setBlocked(int oldMode)
    {
        blocked = false;
        int newMode = this.sim.getMarioSim().getMarioMode();
        if (!(newMode == oldMode) )
        {
            if (newMode < oldMode)
            {
                blocked = true;
            }
        }
    }

    public boolean isBlocked()
    {
        return this.blocked;
    }


    @Override
    public int compareTo(SearchNode element)
    {

        if (this.getFCost() > element.getFCost())
        {
            return 1;
        }
        else if (this.getFCost() < element.getFCost())
        {
            return -1;
        }
        else if (this.getFCost() == element.getFCost())
        {
            if (this.sim.getMarioSim().getX() > element.sim.getMarioSim().getX())
            {
                return -1;
            }
            else if (this.sim.getMarioSim().getX() < element.sim.getMarioSim().getX())
            {
                return 1;
            }
            else if (this.sim.getMarioSim().getX() == element.sim.getMarioSim().getX())
            {
                if (this.getAction()[Mario.KEY_JUMP] && !element.getAction()[Mario.KEY_JUMP])
                {
                    return -1;
                }
                else if (!this.getAction()[Mario.KEY_JUMP] && element.getAction()[Mario.KEY_JUMP])
                {
                    return 1;
                }
            }
        }
        return 0;
    }

    @Override
    public boolean matches(SearchNode element)
    {

        MarioSim currentSim = this.sim.getMarioSim();
        MarioSim otherSim = element.sim.getMarioSim();

        int diff = 2;

        if ((Math.abs(currentSim.getX() - otherSim.getX()) < diff) && (Math.abs(currentSim.getY() - otherSim.getY()) < diff))
        {
            return true;
        }
        return false;
    }

    public void estimateHCost()
    {
        float hMarioX = this.sim.getMarioSim().getX();
        float hMarioSpeed = this.sim.getMarioSim().getXA();
        int ticks = 0;

        while (hMarioX < goalX)
        {
            ticks++;
            hMarioX += hMarioSpeed;
            hMarioSpeed += 1.2F;
            hMarioSpeed *= 0.89F;
        }
        this.hCost= ticks;
    }

    public int getID()
    {
        return this.id;
    }

    public static void resetCounter()
    {
        SearchNode.idCounter = 1;
    }

    public ArrayList<SpriteSim> getEnemySims() throws CloneNotSupportedException
    {
        return sim.getEnemySims();
    }

    public float getHCost() {
        return this.hCost;
    }

    public float getGoal() {
        return goalX;
    }
}