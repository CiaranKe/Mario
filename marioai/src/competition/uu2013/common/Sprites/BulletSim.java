package competition.uu2013.common.Sprites;

public class BulletSim extends EnemySim implements Cloneable
{
    public BulletSim(float _x, float _y, int _type)
    {
        super(_x, _y, _type);
        facing = -1;
        width = 4;
        height = 24;
        dead = false;
        deadTime = 0;
    }

    @Override
    public BulletSim clone() throws CloneNotSupportedException
    {
        BulletSim n = (BulletSim) super.clone();
        return n;
    }

    public void collideCheck()
    {
        if (dead) return;

        float xMarioD = marioSim.getX() - x;
        float yMarioD = marioSim.getY() - y;
        float w = 16;
        if (xMarioD > -16 && xMarioD < 16)
        {
            if (yMarioD > -height && yMarioD < marioSim.height())
            {
                if (marioSim.getYa() > 0 && yMarioD <= 0 && (!marioSim.isOnGround() || !marioSim.wasOnGround()))
                {
                    marioSim.stomp(this);
                    dead = true;

                    xa = 0;
                    ya = 1;
                    deadTime = 100;
                } else
                {
                    marioSim.getHurt();
                }
            }
        }
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
            deadTime--;

            if (deadTime == 0)
            {
                deadTime = 1;
                return;
            }

            x += xa;
            y += ya;
            ya *= 0.95;
            ya += 1;

            return;
        }

        float sideWaysSpeed = 4f;

        xa = facing * sideWaysSpeed;
        move(xa, 0);
    }

    public boolean move(float xa, float ya)
    {
        x += xa;
        return true;
    }

    public boolean fireballCollideCheck(FireBallSim fireball)
    {
        if (deadTime != 0) return false;

        float xD = fireball.getX() - x;
        float yD = fireball.getY() - y;

        if (xD > -16 && xD < 16)
        {
            if (yD > -height && yD < fireball.height())
            {
                return true;
            }
        }
        return false;
    }

    public boolean shellCollideCheck(ShellSim shell)
    {
        if (deadTime != 0) return false;

        float xD = shell.getX() - x;
        float yD = shell.getY() - y;

        if (xD > -16 && xD < 16)
        {
            if (yD > -height && yD < shell.height())
            {
                dead = true;

                xa = 0;
                ya = 1;
                deadTime = 100;

                return true;
            }
        }
        return false;
    }

    public int height()
    {
        return this.height();
    }
}
