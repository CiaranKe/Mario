package competition.uu2013.common.level;

import competition.uu2013.common.Sprites.*;

import java.util.ArrayList;


public class WorldSim implements Cloneable
{

    MarioSim marioSim;
    Enemy enemySims;
    Map mapSim;

    public WorldSim(MarioSim _marioSim, Enemy _enemySims, Map _mapSim)
    {
        this.marioSim = _marioSim;
        this.enemySims = _enemySims;
        this.mapSim = _mapSim;
        this.marioSim.setWorldSim(this);
        this.marioSim.setMapSim(this.mapSim);
    }

    public WorldSim clone()
    {
        try
        {
            WorldSim w = new WorldSim(this.marioSim.clone(), this.enemySims.clone(), this.mapSim.clone());
            w.marioSim.setMapSim(w.mapSim);
            w.marioSim.setWorldSim(w);

            for (SpriteSim e : w.enemySims.getEnemiesList())
            {
                ((EnemySim)e).setMapSim(w.mapSim);
                ((EnemySim)e).setWorldSim(w);
                ((EnemySim)e).setMarioSim(w.marioSim);
            }

            return w;
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }
        return null;
    }


    public ArrayList<SpriteSim> getEnemySims() throws CloneNotSupportedException
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
        allSims.addAll(enemySims.getEnemiesList());


        marioSim.setKeys(_keys, cuurentX, currentY);

        for (SpriteSim e: allSims)
        {
            e.move();

        }

        for (SpriteSim e: allSims)
        {
            e.collideCheck();

            if (e instanceof ShellSim)
            {
                this.checkShellCollide((ShellSim)e, allSims);
            }
            if (e instanceof FireBallSim)
            {
                if (this.checkFireballCollide((FireBallSim)e, allSims))
                {
                    ((FireBallSim) e).die();
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
        byte block = mapSim.getBlock(x, y);
        if (((Map.TILE_BEHAVIORS[block & 0xff]) & Map.BIT_PICKUPABLE) > 0)
        {
            mapSim.setBlock(x, y, (byte) 0);
        }

        for (SpriteSim sprite : enemySims.getEnemiesList())
        {
            sprite.bumpCheck(x, y);
        }
    }

    public void  bump(int x, int y, boolean canBreakBricks)
    {
        byte block = mapSim.getBlock(x, y);

        if ((Map.TILE_BEHAVIORS[block & 0xff] & Map.BIT_BUMPABLE) > 0)
        {
            bumpInto(x, y - 1);
        }

        if ((Map.TILE_BEHAVIORS[block & 0xff] & Map.BIT_BREAKABLE) > 0)
        {
            bumpInto(x, y - 1);
            if (canBreakBricks)
            {
                mapSim.setBlock(x, y, (byte) 0);
            }
        }
    }

    public float[] getMarioLocation()
    {
        return new float[] {marioSim.getX(), marioSim.getY()};
    }

    public boolean syncLocation(float x, float y, boolean isMarioAbleToJump, boolean isMarioOnGround, boolean wasOnGround, boolean isMarioAbleToShoot, int marioStatus, float [] newEnemies, byte [][] scene)
    {
        boolean newEnemy;

        this.marioSim.syncLocation(x, y, isMarioAbleToJump, isMarioOnGround, wasOnGround, isMarioAbleToShoot, marioStatus);
        this.mapSim.setScene(scene,x,y);
        newEnemy = this.enemySims.setEnemies(newEnemies,x,y, (scene[0].length / 2));



        for (SpriteSim sim : enemySims.getEnemiesList())
        {
            ((EnemySim)sim).setMarioSim(this.marioSim);
            ((EnemySim)sim).setWorldSim(this);
            ((EnemySim)sim).setMapSim(this.mapSim);
        }

        return newEnemy;
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

    public Map getMap() {
        return mapSim;
    }

    public MarioSim getMarioSim() {
        return marioSim;
    }

    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(" Mario {" + marioSim.toString() + "} ");
        stringBuilder.append("Enemies {");
        int enemyCounter = 1;
        for (SpriteSim e: enemySims.getEnemiesList())
        {
            stringBuilder.append(enemyCounter + ": " +  e.toString() + "; ");
        }
        stringBuilder.append("}");

        return stringBuilder.toString();
    }
}
