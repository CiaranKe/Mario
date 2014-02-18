package competition.uu2013.common.Sprites;

import competition.uu2013.common.level.Map;

/**
 * Created with IntelliJ IDEA.
 * User: fluffy
 * Date: 27/01/14
 * Time: 14:36
 * To change this template use File | Settings | File Templates.
 */
public class FireBallSim extends EnemySim implements Cloneable
{

    private static float FIREBALL_GROUND_INERTIA = 0.89f;
    private static float FIREBALL_AIR_INERTIA = 0.89f;

    public FireBallSim(float _x, float _y, int _type, int dir)
    {
        super(_x, _y, _type);

        this.x = x;
        this.y = y;

        height = 8;
        ya = 4;
        facing = dir;
    }

    @Override
    public FireBallSim clone() throws CloneNotSupportedException
    {
        FireBallSim n = (FireBallSim) super.clone();

        return n;
    }

    public void move()
    {
        if (xa > 0)
        {
            facing = 1;
        }
        else if (xa < 0)
        {
            facing = -1;
        }

        if (deadTime > 0)
        {
            return;
        }

        float sideWaysSpeed = 8f;
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

        //TODO:
        //Enemy.checkFireballCollide(this);

        if (!move(xa, 0))
        {
            die();
        }

        onGround = false;
        move(0, ya);
        if (onGround) ya = -10;

        ya *= 0.95f;
        if (onGround)
        {
            xa *= FIREBALL_GROUND_INERTIA;
        } else
        {
            xa *= FIREBALL_AIR_INERTIA;
        }

        if (!onGround)
        {
            ya += 1.5;
        }

        System.out.println("Simmed Fireball: X:" + x + " Y: " + y );
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

            if (avoidCliffs && onGround && !map.isBlocking((int) ((x + xa + width) / 16), (int) ((y) / 16 + 1)))
                collide = true;
        }
        if (xa < 0)
        {
            if (isBlocking(x + xa - width, y + ya - height, xa, ya)) collide = true;
            if (isBlocking(x + xa - width, y + ya - height / 2, xa, ya)) collide = true;
            if (isBlocking(x + xa - width, y + ya, xa, ya)) collide = true;

            if (avoidCliffs && onGround && !map.isBlocking((int) ((x + xa - width) / 16), (int) ((y) / 16 + 1)))
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

        boolean blocking = map.isBlocking(x, y);

        byte block = map.getBlock(x, y);

        return blocking;
    }

    public void die()
    {
        dead = true;

        xa = -facing * 2;
        ya = -5;
        deadTime = 100;
        System.out.println("Simmed Fireball died!");
    }
}
