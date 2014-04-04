/*
 * 
 */
package competition.uu2013.common.Sprites;

/**
 *  BullitSim. Copy of the BullitBill class from MAIB
 *  
 *  
 *  Added getter and setter methods in addition  to the clone method.
 */
public class BulletSim extends EnemySim implements Cloneable
{
    
    /**
     * Instantiates a new bullet sim.
     *
     * @param _x 
     * @param _y the _y
     * @param _type the _type
     */
    public BulletSim(float _x, float _y, int _type)
    {
        super(_x, _y, _type);
        facing = -1;
        width = 4;
        height = 24;
        dead = false;
        deadTime = 0;
    }

    /* (non-Javadoc)
     * @see competition.uu2013.common.Sprites.EnemySim#clone()
     */
    @Override
    public BulletSim clone() throws CloneNotSupportedException
    {
        BulletSim n = new BulletSim(this.xLocation, this.yLocation,this.simType);
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
        return n;
    }

    /* (non-Javadoc)
     * @see competition.uu2013.common.Sprites.EnemySim#collideCheck()
     */
    public void collideCheck()
    {
        if (dead) return;

        float xMarioD = marioSim.getXLocation() - xLocation;
        float yMarioD = marioSim.getYLocation() - yLocation;
        float w = 16;
        if (xMarioD > -16 && xMarioD < 16)
        {
            if (yMarioD > -height && yMarioD < marioSim.getHeight())
            {
                if (marioSim.getYa() > 0 && yMarioD <= 0 && (!marioSim.isOnGround() || !marioSim.wasOnGround()))
                {
                    marioSim.stomp(this);
                    dead = true;

                    xAcceleration = 0;
                    yAcceleration = 1;
                    deadTime = 100;
                } else
                {
                    marioSim.getHurt();
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see competition.uu2013.common.Sprites.EnemySim#move()
     */
    public void move()
    {
        if (xAcceleration > 0)
        {
            facing = 1;
        }
        else if (xAcceleration < 0)
        {
            facing = -1;
        }

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

            return;
        }

        float sideWaysSpeed = 4f;

        xAcceleration = facing * sideWaysSpeed;
        move(xAcceleration, 0);
    }

    /* (non-Javadoc)
     * @see competition.uu2013.common.Sprites.EnemySim#move(float, float)
     */
    public boolean move(float xa, float ya)
    {
        xLocation += xa;
        return true;
    }

    /**
     * Fireball collide check.
     *
     * @param fireball the fireball
     * @return true, if successful
     */
    public boolean fireballCollideCheck(FireBallSim fireball)
    {
        if (deadTime != 0) return false;

        float xD = fireball.getXLocation() - xLocation;
        float yD = fireball.getYLocation() - yLocation;

        if (xD > -16 && xD < 16)
        {
            if (yD > -height && yD < fireball.getHeight())
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Shell collide check.
     *
     * @param shell the shell
     * @return true, if successful
     */
    public boolean shellCollideCheck(ShellSim shell)
    {
        if (deadTime != 0) return false;

        float xD = shell.getXLocation() - xLocation;
        float yD = shell.getYLocation() - yLocation;

        if (xD > -16 && xD < 16)
        {
            if (yD > -height && yD < shell.getHeight())
            {
                dead = true;

                xAcceleration = 0;
                yAcceleration = 1;
                deadTime = 100;

                return true;
            }
        }
        return false;
    }

    /* (non-Javadoc)
     * @see competition.uu2013.common.Sprites.EnemySim#getHeight()
     */
    public int getHeight()
    {
        return this.height;
    }
}
