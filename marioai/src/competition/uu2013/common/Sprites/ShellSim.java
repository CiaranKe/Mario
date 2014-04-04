/*
 * 
 */
package competition.uu2013.common.Sprites;

import competition.uu2013.common.level.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class ShellSim.
 */
public class ShellSim extends EnemySim implements Cloneable
{
    
    /** The yaa. */
    private float yaa;
    
    /** The carried. */
    public boolean carried;

    /**
     * Instantiates a new shell sim.
     *
     * @param _x the _x
     * @param _y the _y
     * @param _type the _type
     */
    public ShellSim(float _x, float _y, int _type)
    {
        super(_x, _y, _type);
        height = 12;
        facing = 0;
        deadTime = 0;

        yAcceleration = -5;

        yaa = 2.0F;
    }

    /* (non-Javadoc)
     * @see competition.uu2013.common.Sprites.EnemySim#clone()
     */
    @Override
    public ShellSim clone() throws CloneNotSupportedException
    {
        ShellSim n = new ShellSim(this.xLocation,this.yLocation, this.simType);
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
        n.yaa = this.yaa;
        n.carried = this.carried;
        return n;
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
            if (yD > -height && yD < fireball.height)
            {
                if (facing != 0)
                {
                    return true;
                }

                xAcceleration = fireball.getFacing() * 2;
                yAcceleration = -5;
                deadTime = 100;
                return true;
            }
        }
        return false;
    }

    /* (non-Javadoc)
     * @see competition.uu2013.common.Sprites.EnemySim#collideCheck()
     */
    public void collideCheck()
    {
        if (carried || dead || deadTime > 0)
        {
            return;
        }

        float xMarioD = marioSim.getXLocation() - xLocation;
        float yMarioD = marioSim.getYLocation() - yLocation;
        float w = 16;
        if (xMarioD > -w && xMarioD < w)
        {
            if (yMarioD > -height && yMarioD < marioSim.getHeight())
            {
                if (marioSim.getYa() > 0 && yMarioD <= 0 && (!marioSim.isOnGround() || !marioSim.wasOnGround()))
                {
                    marioSim.stomp(this);
                    if (facing != 0)
                    {
                        xAcceleration = 0;
                        facing = 0;
                    } else
                    {
                        facing = marioSim.getFacing();
                    }
                } else
                {
                    if (facing != 0)
                    {
                        marioSim.getHurt();
                    } else
                    {
                        facing = marioSim.getFacing();
                    }
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see competition.uu2013.common.Sprites.EnemySim#move()
     */
    public void move()
    {
        if (carried)
        {

            //TODO:
            //Enemy.checkShellCollide(this);
            return;
        }

        if (deadTime > 0)
        {
            deadTime--;

            if (deadTime == 0)
            {
                deadTime = 1;
            }

            xLocation += xAcceleration;
            yLocation += yAcceleration;
            yAcceleration *= 0.95;
            yAcceleration += 1;

            return;
        }

        float sideWaysSpeed = 11f;
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

        if (facing != 0)
        {
            //TODO:
            //Enemy.checkShellCollide(this);
        }

        if (!move(xAcceleration, 0))
        {
            facing = -facing;
        }
        onGround = false;
        move(0, yAcceleration);

        yAcceleration *= 0.85f;
        if (onGround)
        {
            xAcceleration *= GROUND_INERTIA;
        } else
        {
            xAcceleration *= AIR_INERTIA;
        }

        if (!onGround)
        {
            yAcceleration += yaa;
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

//        byte block = levelScene.level.getBlock(x, y);

        worldSim.bump(x, y, true);

        return blocking;
    }

    /* (non-Javadoc)
     * @see competition.uu2013.common.Sprites.SpriteSim#bumpCheck(int, int)
     */
    public void bumpCheck(int xTile, int yTile)
    {
        if (xLocation + width > xTile * 16 && xLocation - width < xTile * 16 + 16 && yTile == (int) ((yLocation - 1) / 16))
        {
            facing = -marioSim.getFacing();
            yAcceleration = -10;
        }
    }

    /**
     * Die.
     */
    public void die()
    {
        dead = true;

        carried = false;

        xAcceleration = -facing * 2;
        yAcceleration = -5;
        deadTime = 100;
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
            if (yD > -height && yD < shell.height)
            {
                die();
                shell.die();
                return true;
            }
        }
        return false;
    }


    /**
     * Release.
     */
    public void release()
    {
        carried = false;
        facing = marioSim.getFacing();
        xLocation += facing * 8;
    }
}
