package competition.uu2013.common;

import competition.uu2013.common.Sprites.*;

import java.util.ArrayList;


public class WorldSim implements Cloneable
{

    MarioSim marioSim;
    Enemy enemySims;
    Map mapSim;
    private ArrayList<FireBallSim> fireBallSims;


    public WorldSim(MarioSim _marioSim, Enemy _enemySims, Map _mapSim)
    {
        this.marioSim = _marioSim;
        this.enemySims = _enemySims;
        this.mapSim= _mapSim;
        this.marioSim.setWorldSim(this);
        this.fireBallSims = new ArrayList<FireBallSim>();
    }

    public ArrayList<SpriteSim> getEnemySims()
    {
        return this.enemySims.cloneEnemies();
    }

    public int countEnemies()
    {
        return this.enemySims.getCount();
    }

    public void move(boolean[] _keys, float cuurentX, float currentY)
    {

        ArrayList<SpriteSim> allSims = new ArrayList<SpriteSim>();

        allSims.add(marioSim);
        allSims.addAll(fireBallSims);
        allSims.addAll(enemySims.getEnemiesList());


        marioSim.setKeys(_keys, cuurentX, currentY);

        for (SpriteSim e: allSims)
        {
            e.move();

        }

        for (SpriteSim e: allSims)
        {
            e.collideCheck();
        }
        for (SpriteSim e: allSims)
        {
            if (e instanceof ShellSim)
            {
                this.checkShellCollide((ShellSim)e, allSims);
            }
            if (e instanceof FireBallSim)
            {
                if (this.checkFireballCollide((FireBallSim)e, allSims))
                {
                    ((FireBallSim) e).die();
                    fireBallSims.remove(e);
                }
            }
        }
    }



    public boolean checkFireballCollide(FireBallSim fireBallSim, ArrayList<SpriteSim> allSims)
    {
        for (SpriteSim e : allSims)
        {
            if (e.checkFireballCollide(fireBallSim))
            {
                return true;
            }
        }
        return false;
    }

    public void checkShellCollide(ShellSim shellSim, ArrayList<SpriteSim> allSims)
    {
        for (SpriteSim e: allSims)
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

        for (SpriteSim sprite : enemySims.getEnemiesList())
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

    public void syncLocation(float x, float y, boolean isMarioAbleToJump, boolean isMarioOnGround, boolean isMarioAbleToShoot, boolean marioStatus, float [] newEnemies, byte [][] scene)
    {
        this.marioSim.syncLocation(x, y, isMarioAbleToJump, isMarioOnGround, isMarioAbleToShoot, marioStatus);
        this.mapSim.setScene(scene,x,y);
        this.enemySims.setEnemies(newEnemies,x,y,(scene[0].length / 2), (scene.length / 2));

        for (SpriteSim sim : enemySims.getEnemiesList())
        {
            ((EnemySim)sim).setMarioSim(this.marioSim);
            ((EnemySim)sim).setWorldSim(this);
        }

    }

    public Object getMarioXA() {
        return marioSim.getXA();
    }

    public Object getMarioYA() {
        return marioSim.getYa();
    }

    public boolean getOnGround() {
        return marioSim.isOnGround();
    }

    public boolean getWasOnGround() {
        return marioSim.wasOnGround();
    }

    public int numFireBalls()
    {
        return this.fireBallSims.size();
    }

    public void addFireball(FireBallSim fireBallSim)
    {
        fireBallSim.setWorldSim(this);
        fireBallSim.setMarioSim(this.marioSim);
        this.fireBallSims.add(fireBallSim);
        fireBallSim.move();
    }
}
