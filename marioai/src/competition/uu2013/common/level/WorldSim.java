package competition.uu2013.common.level;
import competition.uu2013.common.Sprites.*;
import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * WorldSim, container for the Enemy, Map and Mario simulations
 * minics the functionality of the LevelScene class.
 *
 * @author Ciaran Kearney
 */
public class WorldSim implements Cloneable
{
    
    /** The mario simulation. */
    MarioSim marioSim;
    
    /** The enemy sims. */
    Enemy enemySims;
    
    /** The map simulation. */
    Map mapSim;

    /**
     * Instantiates a new world sim.
     *
     * @param _marioSim the mario sim
     * @param _enemySims the enemy sims
     * @param _mapSim the map sim
     */
    public WorldSim(MarioSim _marioSim, Enemy _enemySims, Map _mapSim)
    {
        this.marioSim = _marioSim;
        this.enemySims = _enemySims;
        this.mapSim = _mapSim;
        //set the map and world for our mario sim
        this.marioSim.setWorldSim(this);
        this.marioSim.setMapSim(this.mapSim);
    }

    /** Clones the entire simulation to model potential moves
     * 
     * @see java.lang.Object#clone()
     */
    public WorldSim clone()
    {
        try
        {
        	//create the new sim
            WorldSim w = new WorldSim(this.marioSim.clone(), this.enemySims.clone(), this.mapSim.clone());
            //update mario
            w.marioSim.setMapSim(w.mapSim);
            w.marioSim.setWorldSim(w);

            //update the enemies
            for (SpriteSim e : w.enemySims.getEnemiesList())
            {
                ((EnemySim)e).setMapSim(w.mapSim);
                ((EnemySim)e).setWorldSim(w);
                ((EnemySim)e).setMarioSim(w.marioSim);
            }
            //return the simulation
            return w;
        }
        //never reached
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Returns a copy of this worlds enemy simulations, used for debugging
     *
     * @return the enemy sims
     * @throws CloneNotSupportedException the clone not supported exception
     */
    public ArrayList<SpriteSim> getEnemySims() throws CloneNotSupportedException
    {
        return this.enemySims.cloneEnemies();
    }

    /**
     * returns the number of enemies in this simulation
     *
     * @return the int
     */
    public int countEnemies()
    {
        return this.enemySims.getCount();
    }

    /**
     * Moves the simulation forward one frame after applying the specified action
     *
     * @param _action the action to apply
     * @param _currentX mario's current x location
     * @param _currentY marios current y location
     */
    public void move(boolean[] _action, float _currentX, float _currentY)
    {
    	//get all the on screen sprites
        ArrayList<SpriteSim> allSims = new ArrayList<SpriteSim>();
        
        allSims.add(marioSim);
        allSims.addAll(enemySims.getEnemiesList());

        //set the action to test
        marioSim.setKeys(_action, _currentX, _currentY);

        //move All the things
        for (SpriteSim e: allSims)
        {
            e.move();

        }

        //check who bumped into what
        for (SpriteSim e: allSims)
        {
            e.collideCheck();

            //if there are any shells, did they hit something?
            if (e instanceof ShellSim)
            {
                this.checkShellCollide((ShellSim)e, allSims);
            }
            //did the fireballs hit anything?
            if (e instanceof FireBallSim)
            {
                if (this.checkFireballCollide((FireBallSim)e, allSims))
                {
                	//remove the fireball
                    ((FireBallSim) e).die();
                }
            }
        }
    }

    /**
     * Checks if a fireball collided with a Sprite, hurts the sprite if true
     *
     * @param fireBallSim the fireball
     * @param allSims  all the sims
     * @return true, if successful
     */
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

    /**
     * Checks if a shell collided with a sim, hursts the sim if true
     *
     * @param shellSim the shell sim
     * @param allSims the all sims
     */
    public void checkShellCollide(ShellSim shellSim, ArrayList<SpriteSim> allSims)
    {
        for (SpriteSim e: allSims)
        {
            e.checkShellCollide(shellSim);
        }
    }

    /**
     * Destructively edits the map whenever a Sprite bumps into an obstacle.
     *
     * @param x location
     * @param y location
     */
    public void bumpInto(int x, int y)
    {
    	//get the block
        byte block = mapSim.getBlock(x, y);
        
        //if the block is breakable
        if (((Map.TILE_BEHAVIORS[block & 0xff]) & Map.BIT_PICKUPABLE) > 0)
        {
        	//remove it
            mapSim.setBlock(x, y, (byte) 0);
        }

        //Has anything else collided with the map?
        for (SpriteSim sprite : enemySims.getEnemiesList())
        {
            sprite.bumpCheck(x, y);
        }
    }

    /**
     * Destructively edits the map whenever Mario bumps into an obstacle..
     *
     * @param x the x location
     * @param y the y location
     * @param canBreakBricks can they break bricks
     */
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

    /**
     * Gets mario's location.
     *
     * @return mario location
     */
    public float[] getMarioLocation()
    {
        return new float[] {marioSim.getXLocation(), marioSim.getYLocation()};
    }

    /**
     * Syncs the simulation up with the information retrieved from the game
     *
     * @param x Mario's x location
     * @param y Mario's y location
     * @param isMarioAbleToJump can mario jump?
     * @param isMarioOnGround  is mario on the ground?
     * @param wasOnGround was mario on the ground in the last frame?
     * @param isMarioAbleToShoot is mario able to shoot?
     * @param marioStatus mario's state
     * @param newEnemies the new enemies
     * @param scene the levelScene array
     * @return true, if new enemies found
     */
    public boolean syncLocation(float x, float y, boolean isMarioAbleToJump, boolean isMarioOnGround, boolean wasOnGround, boolean isMarioAbleToShoot, int marioStatus, float [] newEnemies, byte [][] scene)
    {
        boolean newEnemy;

        //update mario
        this.marioSim.syncLocation(x, y, isMarioAbleToJump, isMarioOnGround, wasOnGround, isMarioAbleToShoot, marioStatus);
        //update the map
        this.mapSim.setScene(scene,x,y);
        //update the enemies list
        newEnemy = this.enemySims.setEnemies(newEnemies,x,y, (scene[0].length / 2), this.marioSim);
        
        //set all the sims to use this world simulation
        for (SpriteSim sim : enemySims.getEnemiesList())
        {
            ((EnemySim)sim).setMarioSim(this.marioSim);
            ((EnemySim)sim).setWorldSim(this);
            ((EnemySim)sim).setMapSim(this.mapSim);
        }

        return newEnemy;
    }

    /**
     * Gets mario's x acceleration
     *
     * @return  mario's x acceleration
     */
    public Object getMarioXA() 
    {
        return marioSim.getXA();
    }

    /**
     * Gets the mario y acceleration.
     *
     * @return the mario's y acceleration
     */
    public Object getMarioYA() 
    {
        return marioSim.getYa();
    }

    /**
     * is mario on the ground in this sim
     *
     * @return true, if on ground
     */
    public boolean getOnGround() 
    {
        return marioSim.isOnGround();
    }

    /**
     * was mario previously on the ground in this sim
     *
     * @return true, if previously on ground
     */
    public boolean getWasOnGround() 
    {
        return marioSim.wasOnGround();
    }

    /**
     * Gets the map.
     *
     * @return the map
     */
    public Map getMap() 
    {
        return mapSim;
    }

    /**
     * Gets the mario sim.
     *
     * @return the mario sim
     */
    public MarioSim getMarioSim() 
    {
        return marioSim;
    }

    /** Returns a string representation of this simulation, used for debugging
     * @see java.lang.Object#toString()
     */
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
