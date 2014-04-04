/*
 * 
 */
package competition.uu2013.common.Sprites;

import competition.uu2013.common.Sprites.EnemySim;

// TODO: Auto-generated Javadoc
/**
 * Created with IntelliJ IDEA.
 * User: fluffy
 * Date: 27/01/14
 * Time: 14:17
 * To change this template use File | Settings | File Templates.
 */
public class WaveSim extends EnemySim implements Cloneable
{

    /** The amplitude. */
    private float amplitude = 10f;
    
    /** The last sin. */
    private float lastSin;
    
    /** The side way counter. */
    private int sideWayCounter = 0;
    
    /** The yaa. */
    private float yaa;

    /**
     * Instantiates a new wave sim.
     *
     * @param _x the _x
     * @param _y the _y
     * @param _type the _type
     */
    public WaveSim(float _x, float _y, int _type)
    {
        super(_x, _y, _type);
        yaa = 2.0F;
    }

    /* (non-Javadoc)
     * @see competition.uu2013.common.Sprites.EnemySim#clone()
     */
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

    /* (non-Javadoc)
     * @see competition.uu2013.common.Sprites.EnemySim#move()
     */
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
                xLocation += xAcceleration;
                yLocation += yAcceleration;
                yAcceleration *= 0.95;
                yAcceleration += 1;
            }
            return;
        }


        float sideWaysSpeed = onGround ? 1.75f : 0.55f;

        if (xAcceleration > 2)
        {
            facing = 1;
        }
        if (xAcceleration < -2)
        {
            facing = -1;
        }

        xAcceleration = facing * sideWaysSpeed;

        if (!move(xAcceleration, 0)) facing = -facing;
        onGround = false;
        if (winged)
        {
            float curSin = (float) Math.sin(xLocation /10);
            yAcceleration = (curSin - lastSin) * amplitude;
            lastSin = curSin;
            sideWayCounter++;
        }
        move(0, yAcceleration);

        if (sideWayCounter >= 100)
        {
            sideWayCounter = 0;
            facing *= -1;
        }

        yAcceleration *= winged ? 0.95 : 0.85f;
        if (onGround)
        {
            xAcceleration *= GROUND_INERTIA;
        } else
        {
            xAcceleration *= AIR_INERTIA;
        }

        if (!onGround && !winged)
        {
            yAcceleration += yaa;
        }
    }
}
