package competition.uu2013.common.Sprites;

import competition.uu2013.common.level.Map;

// TODO: Auto-generated Javadoc
/**
 * Created with IntelliJ IDEA.
 * User: fluffy
 * Date: 27/01/14
 * Time: 14:36
 * To change this template use File | Settings | File Templates.
 */
public class FireBallSim extends EnemySim implements Cloneable
{

    /** The fireball ground inertia. */
    private static float FIREBALL_GROUND_INERTIA = 0.89f;
    
    /** The fireball air inertia. */
    private static float FIREBALL_AIR_INERTIA = 0.89f;

    /**
     * Instantiates a new fire ball sim.
     *
     * @param _x the _x
     * @param _y the _y
     * @param _type the _type
     * @param dir the dir
     */
    public FireBallSim(float _x, float _y, int _type, int dir)
    {
        super(_x, _y, _type);

        this.xLocation = _x;
        this.yLocation = _y;

        height = 8;
        yAcceleration = 5.3F;
        xAcceleration = 7.12F;
        facing = dir;
    }

    /* (non-Javadoc)
     * @see competition.uu2013.common.Sprites.EnemySim#clone()
     */
    @Override
    public FireBallSim clone() throws CloneNotSupportedException
    {
        FireBallSim n = new FireBallSim(this.xLocation, this.yLocation, this.simType, this.facing);
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
        return n;
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
            return;
        }

        float sideWaysSpeed = 8f;
        //        float sideWaysSpeed = onGround ? 2.5f : 1.2f;

        if (xAcceleration > 2)
        {
            facing = 1;
        }
        if (xAcceleration < -2)
        {
            facing = -1;
        }

        xAcceleration = facing * sideWaysSpeed;

        //TODO:
        //Enemy.checkFireballCollide(this);

        if (!move(xAcceleration, 0))
        {
            die();
        }

        onGround = false;
        move(0, yAcceleration);
        if (onGround) yAcceleration = -10;

        yAcceleration *= 0.95f;
        if (onGround)
        {
            xAcceleration *= FIREBALL_GROUND_INERTIA;
        } else
        {
            xAcceleration *= FIREBALL_AIR_INERTIA;
        }

        if (!onGround)
        {
            yAcceleration += 1.5;
        }
    }

    /* (non-Javadoc)
     * @see competition.uu2013.common.Sprites.EnemySim#move(float, float)
     */
    public boolean move(float xa, float ya)
    {
        while (xa > 8)
        {
            if (!move(8, 0)) return false;
            xa -= 8;
        }
        while (xa < -8)
        {
            if (!move(-8, 0)) return false;
            xa += 8;
        }
        while (ya > 8)
        {
            if (!move(0, 8)) return false;
            ya -= 8;
        }
        while (ya < -8)
        {
            if (!move(0, -8)) return false;
            ya += 8;
        }

        boolean collide = false;
        if (ya > 0)
        {
            if (isBlocking(xLocation + xa - width, yLocation + ya, xa, 0)) collide = true;
            else if (isBlocking(xLocation + xa + width, yLocation + ya, xa, 0)) collide = true;
            else if (isBlocking(xLocation + xa - width, yLocation + ya + 1, xa, ya)) collide = true;
            else if (isBlocking(xLocation + xa + width, yLocation + ya + 1, xa, ya)) collide = true;
        }
        if (ya < 0)
        {
            if (isBlocking(xLocation + xa, yLocation + ya - height, xa, ya)) collide = true;
            else if (collide || isBlocking(xLocation + xa - width, yLocation + ya - height, xa, ya)) collide = true;
            else if (collide || isBlocking(xLocation + xa + width, yLocation + ya - height, xa, ya)) collide = true;
        }
        if (xa > 0)
        {
            if (isBlocking(xLocation + xa + width, yLocation + ya - height, xa, ya)) collide = true;
            if (isBlocking(xLocation + xa + width, yLocation + ya - height / 2, xa, ya)) collide = true;
            if (isBlocking(xLocation + xa + width, yLocation + ya, xa, ya)) collide = true;

            if (avoidCliffs && onGround && !map.isBlocking((int) ((xLocation + xa + width) / 16), (int) ((yLocation) / 16 + 1),ya))
                collide = true;
        }
        if (xa < 0)
        {
            if (isBlocking(xLocation + xa - width, yLocation + ya - height, xa, ya)) collide = true;
            if (isBlocking(xLocation + xa - width, yLocation + ya - height / 2, xa, ya)) collide = true;
            if (isBlocking(xLocation + xa - width, yLocation + ya, xa, ya)) collide = true;

            if (avoidCliffs && onGround && !map.isBlocking((int) ((xLocation + xa - width) / 16), (int) ((yLocation) / 16 + 1),ya))
                collide = true;
        }

        if (collide)
        {
            if (xa < 0)
            {
                xLocation = (int) ((xLocation - width) / 16) * 16 + width;
                this.xAcceleration = 0;
            }
            if (xa > 0)
            {
                xLocation = (int) ((xLocation + width) / 16 + 1) * 16 - width - 1;
                this.xAcceleration = 0;
            }
            if (ya < 0)
            {
                yLocation = (int) ((yLocation - height) / 16) * 16 + height;
                this.yAcceleration = 0;
            }
            if (ya > 0)
            {
                yLocation = (int) (yLocation / 16 + 1) * 16 - 1;
                onGround = true;
            }
            return false;
        } else
        {
            xLocation += xa;
            yLocation += ya;
            return true;
        }
    }

    /**
     * Checks if is blocking.
     *
     * @param _x the _x
     * @param _y the _y
     * @param xa the xa
     * @param ya the ya
     * @return true, if is blocking
     */
    private boolean isBlocking(float _x, float _y, float xa, float ya)
    {
        int x = (int) (_x / 16);
        int y = (int) (_y / 16);
        if (x == (int) (this.xLocation / 16) && y == (int) (this.yLocation / 16)) return false;

        boolean blocking = map.isBlocking(x, y,ya);

        byte block = map.getBlock(x, y);

        return blocking;
    }

    /**
     * Die.
     */
    public void die()
    {
        dead = true;

        xAcceleration = -facing * 2;
        yAcceleration = -5;
        deadTime = 100;
    }

    /* (non-Javadoc)
     * @see competition.uu2013.common.Sprites.EnemySim#checkFireballCollide(competition.uu2013.common.Sprites.FireBallSim)
     */
    @Override
    public boolean checkFireballCollide(FireBallSim fireBallSim)
    {
        return false;
    }
}
