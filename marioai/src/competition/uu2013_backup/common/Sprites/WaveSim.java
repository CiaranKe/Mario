package competition.uu2013.common.Sprites;

import competition.uu2013.common.Sprites.EnemySim;

/**
 * Created with IntelliJ IDEA.
 * User: fluffy
 * Date: 27/01/14
 * Time: 14:17
 * To change this template use File | Settings | File Templates.
 */
public class WaveSim extends EnemySim
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

    public WaveSim clone()
    {
        WaveSim n = new WaveSim(this.x, this.y, this.type);
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
        n.amplitude = this.amplitude;
        n.lastSin = this.lastSin;
        n.sideWayCounter = this.sideWayCounter;
        n.yaa =  this.yaa;
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
