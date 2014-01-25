package competition.uu2013.common;

import ch.idsia.benchmark.mario.engine.sprites.Sprite;

/**
 * Created with IntelliJ IDEA.
 * User: fluffy
 * Date: 22/01/14
 * Time: 16:29
 * To change this template use File | Settings | File Templates.
 */
public class SpriteSim
{
    public static final float AIR_INERTIA = 0.85F;
    public static final float GROUND_INERTIA = 0.89F;
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
        this.x = _x;
        this.y = _y;
    }
}
