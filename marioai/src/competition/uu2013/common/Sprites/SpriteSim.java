/*
 * 
 */
package competition.uu2013.common.Sprites;

import ch.idsia.benchmark.mario.engine.sprites.Sprite;
import competition.uu2013.common.level.Enemy;

// TODO: Auto-generated Javadoc
/**
 * The Class SpriteSim.
 */
public class SpriteSim implements Cloneable
{
    
    /** The Constant AIR_INERTIA. */
    public static final float AIR_INERTIA = 0.85F;
    
    /** The Constant GROUND_INERTIA. */
    public static final float GROUND_INERTIA = 0.89F;
    
    /** The wind coeff. */
    protected static float windCoeff = 0;
    
    /** The ice coeff. */
    protected static float iceCoeff = 0;
    
    /** The y acceleration. */
    protected float xLocation,yLocation,xAcceleration,yAcceleration;
    
    /** The facing. */
    protected int facing;
    
    /** The sim type. */
    protected int simType;

    /**
     * Gets the height.
     *
     * @return the height
     */
    public int getHeight()
    {
        return -1;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public int getType()
    {
        return this.simType;
    }

    /**
     * Gets the x location.
     *
     * @return the x location
     */
    public float getXLocation()
    {
        return xLocation;
    }

    /**
     * Gets the y location.
     *
     * @return the y location
     */
    public float getYLocation()
    {
        return yLocation;
    }

    /**
     * Sets the xy location.
     *
     * @param _x the _x
     * @param _y the _y
     */
    public void setXYLocation(float _x, float _y)
    {

    }

    /**
     * Gets the facing.
     *
     * @return the facing
     */
    public int getFacing() {
        return facing;
    }

    /**
     * Bump check.
     *
     * @param x the x
     * @param y the y
     */
    public void bumpCheck(int x, int y)
    {

    }

    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    public SpriteSim clone() throws CloneNotSupportedException
    {
        SpriteSim n = new SpriteSim();
        n.xLocation = this.xLocation;
        n.yLocation = this.yLocation;
        n.xAcceleration = this.xAcceleration;
        n.yAcceleration = this.yAcceleration;
        n.facing = this.facing;
        n.simType = this.simType;
        return n;
    }

    /**
     * Move.
     */
    public void move()
    {

    }


    /**
     * Collide check.
     */
    public void collideCheck()
    {

    }

    /**
     * Ice scale.
     *
     * @param ice the ice
     * @return the float
     */
    public float iceScale(final float ice)
    {
        return ice;
    }

    /**
     * Wind scale.
     *
     * @param wind the wind
     * @param facing the facing
     * @return the float
     */
    public float windScale(final float wind, int facing)
    {
        return facing == 1 ? wind : -wind;
    }

    /**
     * Check shell collide.
     *
     * @param shellSim the shell sim
     */
    public void checkShellCollide(ShellSim shellSim)
    {

    }

    /**
     * Check fireball collide.
     *
     * @param fireBallSim the fire ball sim
     * @return true, if successful
     */
    public boolean checkFireballCollide(FireBallSim fireBallSim)
    {
        return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return Enemy.nameEnemy(this.simType) + "X: " + this.getXLocation() + "Y: " + this.getYLocation();
    }
}

