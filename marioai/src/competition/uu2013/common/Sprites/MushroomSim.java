package competition.uu2013.common.Sprites;

import competition.uu2013.common.Map;

/**
 * Created with IntelliJ IDEA.
 * User: fluffy
 * Date: 27/01/14
 * Time: 17:01
 * To change this template use File | Settings | File Templates.
 */
public class MushroomSim extends EnemySim
{
    protected int life;
    protected boolean mayJump;
    protected int jumpTime;

    public MushroomSim(float _x, float _y, int _type)
    {
        super(_x, _y, _type);
        onGround =false;
        mayJump = false;
        jumpTime = 0;
        life = 0;
        facing = 1;
    }

    public MushroomSim clone()
    {
        MushroomSim n = new MushroomSim(this.x, this.y, this.type);
        n.x = this.x;
        n.y = this.y;
        n.ya = this.ya;
        n.xa = this.xa;
        n.facing = this.facing;
        n.type = this.type;
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
        n.dead = this.dead;
        n.life = this.life;
        n.mayJump = this.mayJump;
        n.jumpTime = this.jumpTime;
        return n;
    }

    public void collideCheck()
    {
        float xMarioD = marioSim.getX() - x;
        float yMarioD = marioSim.getY() - y;
        float w = 16;
        if (xMarioD > -16 && xMarioD < 16)
        {
            if (yMarioD > -height && yMarioD < marioSim.height())
            {
                marioSim.setMode(true, false);
            }
        }
    }

    public void move()
    {
        if (life < 9)
        {
            y--;
            life++;
            return;
        }
        float sideWaysSpeed = 1.75f;
        //        float sideWaysSpeed = onGround ? 2.5f : 1.2f;

        if (xa > 2)
        {
            facing = 1;
        }
        if (xa < -2)
        {
            facing = -1;
        }

        xa = facing * sideWaysSpeed;

        mayJump = (onGround);


        if (!move(xa, 0)) facing = -facing;
        onGround = false;
        move(0, ya);

        ya *= 0.85f;
        if (onGround)
        {
            xa *= GROUND_INERTIA;
        } else
        {
            xa *= AIR_INERTIA;
        }

        if (!onGround)
        {
            ya += 2;
        }
    }

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
            if (isBlocking(x + xa - width, y + ya, xa, 0)) collide = true;
            else if (isBlocking(x + xa + width, y + ya, xa, 0)) collide = true;
            else if (isBlocking(x + xa - width, y + ya + 1, xa, ya)) collide = true;
            else if (isBlocking(x + xa + width, y + ya + 1, xa, ya)) collide = true;
        }
        if (ya < 0)
        {
            if (isBlocking(x + xa, y + ya - height, xa, ya)) collide = true;
            else if (collide || isBlocking(x + xa - width, y + ya - height, xa, ya)) collide = true;
            else if (collide || isBlocking(x + xa + width, y + ya - height, xa, ya)) collide = true;
        }
        if (xa > 0)
        {
            if (isBlocking(x + xa + width, y + ya - height, xa, ya)) collide = true;
            if (isBlocking(x + xa + width, y + ya - height / 2, xa, ya)) collide = true;
            if (isBlocking(x + xa + width, y + ya, xa, ya)) collide = true;

            if (avoidCliffs && onGround && !Map.isBlocking((int) ((x + xa + width) / 16), (int) ((y) / 16 + 1), xa, 1))
                collide = true;
        }
        if (xa < 0)
        {
            if (isBlocking(x + xa - width, y + ya - height, xa, ya)) collide = true;
            if (isBlocking(x + xa - width, y + ya - height / 2, xa, ya)) collide = true;
            if (isBlocking(x + xa - width, y + ya, xa, ya)) collide = true;

            if (avoidCliffs && onGround && !Map.isBlocking((int) ((x + xa - width) / 16), (int) ((y) / 16 + 1), xa, 1))
                collide = true;
        }

        if (collide)
        {
            if (xa < 0)
            {
                x = (int) ((x - width) / 16) * 16 + width;
                this.xa = 0;
            }
            if (xa > 0)
            {
                x = (int) ((x + width) / 16 + 1) * 16 - width - 1;
                this.xa = 0;
            }
            if (ya < 0)
            {
                y = (int) ((y - height) / 16) * 16 + height;
                jumpTime = 0;
                this.ya = 0;
            }
            if (ya > 0)
            {
                y = (int) (y / 16 + 1) * 16 - 1;
                onGround = true;
            }
            return false;
        } else
        {
            x += xa;
            y += ya;
            return true;
        }
    }

    private boolean isBlocking(float _x, float _y, float xa, float ya)
    {
        int x = (int) (_x / 16);
        int y = (int) (_y / 16);
        if (x == (int) (this.x / 16) && y == (int) (this.y / 16)) return false;

        boolean blocking = Map.isBlocking(x, y, xa, ya);

        byte block = Map.getBlock(x, y);

        return blocking;
    }

    public void bumpCheck(int xTile, int yTile)
    {
        if (x + width > xTile * 16 && x - width < xTile * 16 + 16 && yTile == (int) ((y - 1) / 16))
        {
            facing = -marioSim.getFacing();
            ya = -10;
        }
    }
}
