package competition.uu2013.common.Sprites;

public class BulletSim extends EnemySim
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

    public BulletSim Clone()
    {
        BulletSim n = new BulletSim(this.x, this.y, this.type);
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
