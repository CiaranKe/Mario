/*
 * 
 */
package competition.uu2013.common.Sprites;

import competition.uu2013.common.level.Map;

// TODO: Auto-generated Javadoc
/**
 * Created with IntelliJ IDEA.
 * User: fluffy
 * Date: 27/01/14
 * Time: 17:13
 * To change this template use File | Settings | File Templates.
 */
public class GreenMushroomSim  extends MushroomSim  implements Cloneable
{

    /**
     * Instantiates a new green mushroom sim.
     *
     * @param _x the _x
     * @param _y the _y
     * @param _type the _type
     */
    public GreenMushroomSim(float _x, float _y, int _type)
    {
        super(_x, _y, _type);

        height = 12;
        facing = 1;
        life = 0;
    }

    /* (non-Javadoc)
     * @see competition.uu2013.common.Sprites.MushroomSim#clone()
     */
    @Override
    public GreenMushroomSim clone() throws CloneNotSupportedException
    {
        GreenMushroomSim n = (GreenMushroomSim) super.clone();
        return n;
    }

    /* (non-Javadoc)
     * @see competition.uu2013.common.Sprites.MushroomSim#collideCheck()
     */
    public void collideCheck()
    {
        float xMarioD = marioSim.getXLocation() - xLocation;
        float yMarioD = marioSim.getYLocation() - yLocation;
        float w = 16;
        if (xMarioD > -16 && xMarioD < 16)
        {
            if (yMarioD > -height && yMarioD < marioSim.getHeight())
            {
                marioSim.getHurt();
            }
        }
    }

    /* (non-Javadoc)
     * @see competition.uu2013.common.Sprites.MushroomSim#move()
     */
    public void move()
    {
        if (life < 9)
        {
            yLocation--;
            life++;
            return;
        }
        float sideWaysSpeed = 1.75f;
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

        mayJump = (onGround);

        if (!move(xAcceleration, 0)) facing = -facing;
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
            yAcceleration += 2;
        }
    }

    /* (non-Javadoc)
     * @see competition.uu2013.common.Sprites.MushroomSim#move(float, float)
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
                jumpTime = 0;
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

    /* (non-Javadoc)
     * @see competition.uu2013.common.Sprites.MushroomSim#bumpCheck(int, int)
     */
    public void bumpCheck(int xTile, int yTile)
    {
        if (xLocation + width > xTile * 16 && xLocation - width < xTile * 16 + 16 && yTile == (int) ((yLocation - 1) / 16))
        {
            facing = -marioSim.getFacing();
            yAcceleration = -10;
        }
    }
}
