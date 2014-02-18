package competition.uu2013.common.Sprites;

import competition.uu2013.common.Sprites.EnemySim;

/**
 * Created with IntelliJ IDEA.
 * User: fluffy
 * Date: 27/01/14
 * Time: 14:17
 * To change this template use File | Settings | File Templates.
 */
public class WaveSim extends EnemySim implements Cloneable
{

    private float amplitude = 10f;
    private float lastSin;
    private int sideWayCounter = 0;
    private float yaa;

    public WaveSim(float _x, float _y, int _type)
    {
        super(_x, _y, _type);
        yaa = 2.0F;
    }

    @Override
    public WaveSim clone()  throws CloneNotSupportedException
    {
        WaveSim n = (WaveSim) super.clone();
        n.amplitude = this.amplitude;
        n.lastSin = this.lastSin;
        n.sideWayCounter = this.sideWayCounter;
        n.yaa = this.yaa;
        return n;
    }

    public void move()
    {
        if (deadTime > 0)
        {
            deadTime--;

            if (deadTime == 0)
            {
                deadTime = 1;
            }

            if (flyDeath)
            {
                x += xa;
                y += ya;
                ya *= 0.95;
                ya += 1;
            }
            return;
        }


        float sideWaysSpeed = onGround ? 1.75f : 0.55f;

        if (xa > 2)
        {
            facing = 1;
        }
        if (xa < -2)
        {
            facing = -1;
        }

        xa = facing * sideWaysSpeed;

        if (!move(xa, 0)) facing = -facing;
        onGround = false;
        if (winged)
        {
            float curSin = (float) Math.sin(x /10);
            ya = (curSin - lastSin) * amplitude;
            lastSin = curSin;
            sideWayCounter++;
        }
        move(0, ya);

        if (sideWayCounter >= 100)
        {
            sideWayCounter = 0;
            facing *= -1;
        }

        ya *= winged ? 0.95 : 0.85f;
        if (onGround)
        {
            xa *= GROUND_INERTIA;
        } else
        {
            xa *= AIR_INERTIA;
        }

        if (!onGround && !winged)
        {
            ya += yaa;
        }
    }
}
