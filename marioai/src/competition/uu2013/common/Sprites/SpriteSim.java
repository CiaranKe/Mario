package competition.uu2013.common.Sprites;

import ch.idsia.benchmark.mario.engine.sprites.Sprite;
import competition.uu2013.common.level.Enemy;

public class SpriteSim implements Cloneable
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

    public SpriteSim clone() throws CloneNotSupportedException
    {
        SpriteSim n = new SpriteSim();
        n.x = this.x;
        n.y = this.y;
        n.xa = this.xa;
        n.ya = this.ya;
        n.facing = this.facing;
        n.type = this.type;
        return n;
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

    public void checkShellCollide(ShellSim shellSim)
    {

    }

    public boolean checkFireballCollide(FireBallSim fireBallSim)
    {
        return false;
    }

    public String toString()
    {
        return Enemy.nameEnemy(this.type) + "X: " + this.getX() + "Y: " + this.getY();
    }
}

