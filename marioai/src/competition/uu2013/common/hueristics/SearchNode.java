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
    private static float goalX;

    private SearchNode parent;
    private SortedList<SearchNode> children;
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
        return (this.sim.getMarioSim().getX() > this.goalX-1);
    }
    public void setParent(SearchNode _parent)
    {
        this.parent = _parent;
    }

    public SearchNode getParent()
    {
        return this.parent;
    }


    public float generateChildren(float _x, float _y, int sceneWidth, int sceneHeight)
    {
        this.children = new SortedList<SearchNode>();
        float maxX = 0;

        for (boolean[] action : Action.getPossibleActions(this.sim))
        {
            SearchNode s = new SearchNode(++idCounter,this.sim.clone());
            //s.setGoal(_x, _y, sceneWidth, sceneHeight);
            s.setAction(action);
            s.setParent(this);
            s.setBlocked(this.sim.getMarioSim());
            s.setGCost(this.gCost);
            s.move(this.getMarioSim().getX(), this.getMarioSim().getY());
            s.estimateHCost(this.sim.getMarioSim().getMarioMode());
            children.add(s);
            //System.out.println("Created: " + s.toString());
            if (s.getPredictedXY()[0] > maxX)
            {
                maxX = s.getPredictedXY()[0];
            }
        }
        return maxX;
    }



    private void setBlocked(MarioSim oldSim)
    {
        blocked = false;
        if (this.getMarioSim().isDead())
        {
            blocked = true;
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
        else
        {
            if (this.sim.getMarioSim().getX() > element.sim.getMarioSim().getX())
            {
                return -1;
            }
            else if (this.sim.getMarioSim().getX() < element.sim.getMarioSim().getX())
            {
                return 1;
            }
            else
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

    public void estimateHCost(int oldMarioMode)
    {
        float hMarioX = this.sim.getMarioSim().getX();
        float hMarioY = this.sim.getMarioSim().getY();
        float hMarioSpeedX = this.sim.getMarioSim().getXA();
        float hMarioSpeedY = this.sim.getMarioSim().getYA();
        int ticksX = 0;
        int ticksY = 0;
        int penalty = 0;

        while (hMarioX < goalX)
        {
            ticksX++;
            hMarioX += hMarioSpeedX;
            hMarioSpeedX += (this.action[Mario.KEY_SPEED]) ? 1.2F : 0.6 ;
            hMarioSpeedX *= 0.89F;
        }

        while (hMarioY > 0)
        {
            ticksY++;
            hMarioY += hMarioSpeedY;
            hMarioSpeedY -=3;
            hMarioSpeedY *= 0.89F;
        }

        if ((this.sim.getMap().getViewAt(this.getMarioSim().getX()+this.getMarioSim().getXA(), this.getMarioSim().getY()) != 0)||
            (this.sim.getMap().getViewAt(this.getMarioSim().getX()+this.getMarioSim().getXA(), this.getMarioSim().getY()) != 2))
        {
            penalty+=2;
        }

        if (this.sim.getMarioSim().getMarioMode() < oldMarioMode)
        {
            penalty += 600;
            parent.setPenalty(500);
        }

        if (this.sim.getMarioSim().isDead())
        {
            penalty += Integer.MAX_VALUE-5000;
            parent.setPenalty(Integer.MAX_VALUE-6000);
        }

        this.hCost= ticksX + ticksY + penalty;
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


    public MarioSim getMarioSim()
    {
        return sim.getMarioSim();
    }

    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(" Node: "+this.getID());
        if (this.parent != null)
        {
            stringBuilder.append(", Parent: " + parent.getID());
        }
        stringBuilder.append(", Cost: "+this.getFCost() + "(" + this.getGCost() + "," + this.hCost + ")");
        stringBuilder.append(", Action: " +Action.nameAction(this.getAction()));
        stringBuilder.append(", Blocked: " + this.blocked);
        stringBuilder.append(", sim Details: {" +  this.sim.toString() + "}");

        return stringBuilder.toString();
    }

    public static void setGoal(float goal)
    {
        goalX = goal;
    }

    public Map getMap()
    {
        return this.sim.getMap();
    }

    public SortedList<SearchNode> getChildren() {
        return children;
    }

    public void setPenalty(int penalty) {
        this.hCost += penalty;
    }
}