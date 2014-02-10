package competition.uu2013.common;

import competition.uu2013.common.Sprites.*;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: fluffy
 * Date: 02/02/14
 * Time: 19:12
 * To change this template use File | Settings | File Templates.
 */
public class MoveSim implements Cloneable
{

    MarioSim marioSim;
    ArrayList<EnemySim> enemySims;

    public MoveSim()
    {

    }

    public MoveSim(MarioSim _marioSim, ArrayList<EnemySim> _enemySims)
    {
        this.marioSim = _marioSim;
        this.enemySims = _enemySims;

        for (EnemySim enemySim: _enemySims)
        {
            enemySim.setMarioSim(this.marioSim);
            enemySim.setMoveSim(this);
        }
        this.marioSim.setMoveSim(this);
    }

    public MoveSim clone()
    {
        MoveSim m = new MoveSim();
        m.marioSim = this.marioSim;
        m.enemySims = new ArrayList<EnemySim>();

        for (EnemySim oldEnemy: this.enemySims)
        {
            EnemySim enemySim = oldEnemy.clone();
            m.enemySims.add(enemySim);
        }
        return m;
    }

    public ArrayList<EnemySim> getEnemySims()
    {
        return this.enemySims;
    }

    public int countEnemies()
    {
        return this.enemySims.size();
    }

    public void move(boolean[] _keys, float cuurentX, float currentY)
    {

        marioSim.move(_keys, cuurentX, currentY);
        for (SpriteSim e: enemySims)
        {
            e.move();

        }
        marioSim.collideCheck();
        for (SpriteSim e: enemySims)
        {
            e.collideCheck();
        }
        for (SpriteSim e: enemySims)
        {
            if (e instanceof ShellSim)
            {
                this.checkShellCollide((ShellSim)e);
            }
            if (e instanceof FireBallSim)
            {
                if (this.checkFireballCollide((FireBallSim)e))
                {
                    ((FireBallSim) e).die();
                }
            }
        }
    }



    public boolean checkFireballCollide(FireBallSim fireBallSim)
    {
        for (EnemySim e : enemySims)
        {
            if (e.checkFireballCollide(fireBallSim))
            {
                return true;
            }
        }
        return false;
    }

    public void checkShellCollide(ShellSim shellSim)
    {
        for (EnemySim e: enemySims)
        {
            e.checkShellCollide(shellSim);
        }
    }

    public void bumpInto(int x, int y)
    {
        byte block = Map.getBlock(x, y);
        if (((Map.TILE_BEHAVIORS[block & 0xff]) & Map.BIT_PICKUPABLE) > 0)
        {
            Map.setBlock(x, y, (byte) 0);
        }

        for (SpriteSim sprite : enemySims)
        {
            sprite.bumpCheck(x, y);
        }
    }

    public void  bump(int x, int y, boolean canBreakBricks)
    {
        byte block = Map.getBlock(x, y);

        if ((Map.TILE_BEHAVIORS[block & 0xff] & Map.BIT_BUMPABLE) > 0)
        {
            bumpInto(x, y - 1);
        }

        if ((Map.TILE_BEHAVIORS[block & 0xff] & Map.BIT_BREAKABLE) > 0)
        {
            bumpInto(x, y - 1);
            if (canBreakBricks)
            {
                Map.setBlock(x, y, (byte) 0);
            }
        }
    }

    public float[] getMarioLocation()
    {
        return new float[] {marioSim.getX(), marioSim.getY()};
    }

    public void syncLocation(float x, float y, boolean isMarioAbleToJump, boolean isMarioOnGround, boolean isMarioAbleToShoot, boolean marioStatus, ArrayList<EnemySim> _enemySims)
    {
        marioSim.syncLocation(x, y, isMarioAbleToJump, isMarioOnGround, isMarioAbleToShoot, marioStatus);
        this.enemySims = _enemySims;

        for(EnemySim e: enemySims)
        {
            e.setMarioSim(this.marioSim);
            e.setMoveSim(this);
        }
    }
}
