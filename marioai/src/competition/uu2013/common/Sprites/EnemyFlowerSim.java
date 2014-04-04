/*
 * 
 */
package competition.uu2013.common.Sprites;

import competition.uu2013.common.Sprites.EnemySim;
import competition.uu2013.common.level.WorldSim;

// TODO: Auto-generated Javadoc
/**
 * Created with IntelliJ IDEA.
 * User: fluffy
 * Date: 27/01/14
 * Time: 14:15
 * To change this template use File | Settings | File Templates.
 */
public class EnemyFlowerSim extends EnemySim implements Cloneable
{
    
    /** The y start. */
    private int yStart;
    
    /** The jump time. */
    private int jumpTime = 0;

    /**
     * Instantiates a new enemy flower sim.
     *
     * @param _x the _x
     * @param _y the _y
     * @param _type the _type
     * @param _sim the _sim
     */
    public EnemyFlowerSim(float _x, float _y, int _type, MarioSim _sim)
    {
        super(_x, _y, _type);

        this.height = 12;
        this.width = 2;
        this.marioSim = _sim;

        yStart = (int)_y/16;
        yAcceleration = -8;

        this.yLocation -= 1;

        for (int i = 0; i < 4; i++)
        {
            move();
        }

    }

    /* (non-Javadoc)
     * @see competition.uu2013.common.Sprites.EnemySim#clone()
     */
    @Override
    public EnemyFlowerSim clone() throws CloneNotSupportedException
    {
        EnemyFlowerSim n = new EnemyFlowerSim(this.xLocation, this.yLocation,this.simType,this.marioSim);
        n.xLocation = this.xLocation;
        n.yLocation = this.yLocation;
        n.xAcceleration = this.xAcceleration;
        n.yAcceleration = this.yAcceleration;
        n.facing = this.facing;
        n.simType = this.simType;
        n.lastX = this.lastX;
        n.lastY = this.lastY;
        n.height = this.height;
        n.width = this.width;
        n.avoidCliffs = this.avoidCliffs;
        n.winged = this.winged;
        n.onGround = this.onGround;
        n.seen = this.seen;
        n.flyDeath = this.flyDeath;
        n.deadTime = this.deadTime;
        n.noFireballDeath = this.noFireballDeath;
        n.dead = this.dead;
        n.accurateY = this.accurateY;
        n.accurateX = this.accurateX;
        n.yaUnknown = this.yaUnknown;
        n.firstMove = this.firstMove;
        n.oldX = this.oldX;
        n.oldY = this.oldY;
        n.alreadyInScope = this.alreadyInScope;
        n.yStart = this.yStart;
        n.jumpTime = this.jumpTime;
        return n;
    }

    /* (non-Javadoc)
     * @see competition.uu2013.common.Sprites.EnemySim#move()
     */
    public void move()
    {
        if (deadTime > 0)
        {
            deadTime--;

            if (deadTime == 0)
            {
                deadTime = 1;
                return;
            }

            xLocation += xAcceleration;
            yLocation += yAcceleration;
            yAcceleration *= 0.95;
            yAcceleration += 1;
        }

        if (yLocation >= yStart)
        {
            yLocation = yStart;

            int xd = (int) (Math.abs(this.marioSim.getXLocation() - xLocation));
            jumpTime++;
            if (jumpTime > 40 && xd > 24)
            {
                yAcceleration = -8;
            } else
            {
                yAcceleration = 0;
            }
        }
        else
        {
            jumpTime = 0;
        }

        yLocation += yAcceleration;
        yAcceleration *= 0.9;
        yAcceleration += 0.1f;
    }

}
