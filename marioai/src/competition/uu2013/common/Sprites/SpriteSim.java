package competition.uu2013.common.Sprites;

import ch.idsia.benchmark.mario.engine.sprites.Sprite;

/**
 * Created with IntelliJ IDEA.
 * User: fluffy
 * Date: 22/01/14
 * Time: 16:29
 * To change this template use File | Settings | File Templates.
 */
public abstract class SpriteSim
{
    public static final float AIR_INERTIA = 0.85F;
    public static final float GROUND_INERTIA = 0.89F;
    protected static float windCoeff = 0;
    protected static float iceCoeff = 0;
    protected float x,y,xa,ya;
    protected int facing;
    protected int type;

    public int height()
    {
        return -1;
    }

    public int getType()
    {
        return this.type;
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public void setXY(float _x, float _y)
    {

    }

    public int getFacing() {
        return facing;
    }

    public void bumpCheck(int x, int y)
    {

    }

    public SpriteSim clone()
    {
         return this;
    }

    public void move()
    {

    }


    public void collideCheck()
    {

    }

    public float iceScale(final float ice)
    {
        return ice;
    }

    public float windScale(final float wind, int facing)
    {
        return facing == 1 ? wind : -wind;
    }
}

