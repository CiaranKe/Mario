package competition.uu2013.common.level;

import ch.idsia.benchmark.mario.engine.sprites.Sprite;
import competition.uu2013.common.Sprites.*;

import java.util.ArrayList;

/**
 * Enemy class, contains a list of all the on screen 
 * enemies, and operations upon them.  Cloneable,
 * each instance of this class is contained
 * inside a WorldSim.
 * 
 */
public class Enemy implements Cloneable
{

    /** The enemies list. */
    private ArrayList<SpriteSim> enemiesList;


    /**
     * Instantiates a new enemy.
     */
    public Enemy() //Fight the power!!
    {
        enemiesList = new ArrayList<SpriteSim>();
    }

    /** 
     * Clones this class creating a copy of the 
     * current on screen enemies to work with
     * @see java.lang.Object#clone()
     */
    @Override
    public Enemy clone() throws CloneNotSupportedException
    {

        Enemy e = new Enemy();

        e.enemiesList = new ArrayList<SpriteSim>();

        if (enemiesList != null)
        {
            for (SpriteSim oldEnemy: enemiesList)
            {
                SpriteSim enemySim = oldEnemy.clone();
                e.enemiesList.add(enemySim.clone());
            }
        }
        return e;
    }

    /**
     * Clone enemies, returns a new copy of the enemies list, allowing destructive
     * operations without changing this instance's list.  Used for debugging.
     *
     * @return the array list
     * @throws CloneNotSupportedException the clone not supported exception
     */
    public ArrayList<SpriteSim> cloneEnemies() throws CloneNotSupportedException
    {
        ArrayList<SpriteSim> enemyCopy = new ArrayList<SpriteSim>();

        if (enemiesList != null)
        {
            for (SpriteSim oldEnemy: enemiesList)
            {
                SpriteSim enemySim = oldEnemy.clone();
                enemyCopy.add(enemySim.clone());
            }
        }

        return enemyCopy;
    }

    /**
     * Determines if an on screen enemy is within the receptive field. Allows the
     * sim to determine if the enemy can be accurately modelled
     *
     * @param marioX Mario's X location
     * @param marioY Mario's Y location
     * @param halfSceneWidth the distance mario can see ahead/behind
     * @param halfSceneHeight the distance mario can see up/down
     * @param sim the enemy to check
     * @return true, if in scope
     */
    public static boolean withinScope(float marioX, float marioY, int halfSceneWidth, int halfSceneHeight, EnemySim sim)
    {
        float lookAHead = marioX + (halfSceneWidth * 16);
        float lookBelow = marioY + (halfSceneHeight * 16);
        float lookBehind = marioX - (halfSceneWidth * 16);
        float lookAbove = marioY - (halfSceneHeight * 16);

        if ((sim.getXLocation() < lookAHead) && (sim.getYLocation() < lookBelow) && (sim.getXLocation() > lookBehind) && (sim.getYLocation() > lookAbove))
        {
            return true;
        }
        return false;
    }

    /**
     * Updates the locations of on screen enemies, attempts to guess the enemies 
     * X and Y acceleration.  
     *
     * @param enemies the enemy location array
     * @param marioX mario's x location
     * @param marioY mario's y location
     * @param sceneWidth the scene width
     * @param _sim the current marioSim
     * @return true, if a new enemy found or lost sync with an enemy position (search re-plans if true)
     */
    public boolean setEnemies(float [] enemies, float marioX, float marioY, int sceneWidth, MarioSim _sim)
    {
    	//our new enemy list
        ArrayList<SpriteSim> newSprites = new ArrayList<SpriteSim>();
        boolean newEnemiesFound = false;

        //parse over the list of enemies
        for (int i = 0; i < enemies.length; i += 3)
        {
        	//create a Sprite for each instance, (positions are relative to mario so we need to add this to get the true value)
            SpriteSim newEnemy = this.createEnemy((marioX + enemies[i+1]),(marioY + enemies[i+2]),(int)enemies[i], _sim);
            
            //ignore flowers
            if (newEnemy.getType() == -1 || newEnemy.getType() == Sprite.KIND_FIRE_FLOWER)
            {
            	continue;
            }
                
            //Enemies don't move more than 2px per frame (I hope), so they should be in this distance.
            float maxDelta = 2.01f * 1.75f;
            boolean enemyFound = false;
            
            //iterate over each of the current enemies
            for (SpriteSim sprite:enemiesList)
            {
                //does this enemy match the type? and are they within range?
                if (sprite.getType() == newEnemy.getType()
                        && Math.abs(sprite.getXLocation() - newEnemy.getXLocation()) < maxDelta
                        && ((Math.abs(sprite.getYLocation() - newEnemy.getYLocation()) < maxDelta) || sprite.getType() == Sprite.KIND_ENEMY_FLOWER))
                {
                	//has the enemy moved?
                    if (Math.abs(sprite.getXLocation() - newEnemy.getXLocation()) > 0)
                    {
                    	//set the enemies direction
                        ((EnemySim)sprite).setFacing(sprite.getFacing() * -1);
                        newEnemiesFound = true;
                        ((EnemySim) sprite).setX(newEnemy.getXLocation());
                    }
                    //update the position of flowers
                    if ((sprite.getYLocation() - newEnemy.getYLocation()) != 0 && sprite.getType() == Sprite.KIND_ENEMY_FLOWER)
                    {
                        ((EnemySim) sprite).setYA((newEnemy.getYLocation() - ((EnemySim) sprite).getAccurateY()) * 0.89f /* MAGIC NUMBER */ );
                        ((EnemySim) sprite).setY(newEnemy.getYLocation());
                    }
                    enemyFound = true;
                }
                //X is correct, Y is wrong, this is a flying enemy
                if (sprite.getType() == newEnemy.getType() &&  (sprite.getXLocation() - newEnemy.getXLocation()) == 0 && (sprite.getYLocation() - newEnemy.getYLocation()) != 0
                        &&  Math.abs(sprite.getYLocation() - newEnemy.getYLocation()) < 8 && sprite.getType() != Sprite.KIND_SHELL &&
                        sprite.getType() != Sprite.KIND_BULLET_BILL && ((EnemySim) sprite).isWinged())
                {
                	
                    ((EnemySim) sprite).setYA((newEnemy.getYLocation() - ((EnemySim) sprite).getAccurateY()) * 0.95f + 0.6f /* MAGIC NUMBER */); 
                    ((EnemySim) sprite).setY(newEnemy.getYLocation());
                    ((EnemySim)sprite).setKnownYA(true);
                    enemyFound = true;
                    newEnemiesFound = true;
                }
                //enemy falling off a ledge
                if (sprite.getType() == newEnemy.getType() && (sprite.getXLocation() - newEnemy.getXLocation()) == 0 && (sprite.getYLocation() - newEnemy.getYLocation()) != 0 &&
                        Math.abs(sprite.getYLocation() - newEnemy.getYLocation()) <= 2 && ((EnemySim)sprite).isYAUnknown() && ((EnemySim) sprite).getAccurateY() != 0)
                {

                    ((EnemySim) sprite).setYA(newEnemy.getYLocation() - ((EnemySim) sprite).getAccurateY() * 0.85f + 2 /* MAGIC NUMBER */); 
                    ((EnemySim) sprite).setY(newEnemy.getYLocation());

                    ((EnemySim) sprite).setKnownYA(false);
                    enemyFound = true;
                }
                //Existing enemy found
                if (enemyFound)
                {
                    newSprites.add(sprite);
                    ((EnemySim) sprite).setAccurateXLocation(newEnemy.getXLocation());
                    ((EnemySim) sprite).setAccurateYLocation(newEnemy.getYLocation());

                    if (newEnemiesFound && ((EnemySim) sprite).withinScope(marioX,marioY, sceneWidth, sceneWidth))
                    {
                        //System.out.println("LOST ENEMY SYNC!");
                    }
                    else
                    {
                        newEnemiesFound = false;
                    }
                    if (((EnemySim) sprite).newWithinScope(marioX,marioY,sceneWidth,sceneWidth))
                    {
                        //System.out.println("ENEMY IN SCOPE!");
                        newEnemiesFound = true;
                    }
                    break;
                }
            }
            //found a new enemy
            if (!enemyFound)
            {
                //System.out.println("NEW ENEMY!");
                newEnemiesFound = true;
                    // Add new enemy to the system.
                ((EnemySim)newEnemy).setXA(2);
                ((EnemySim)newEnemy).setAccurateXLocation(newEnemy.getXLocation());
                ((EnemySim)newEnemy).setAccurateYLocation(newEnemy.getYLocation());
                ((EnemySim)newEnemy).newWithinScope(marioX,marioY,sceneWidth,sceneWidth);
                newSprites.add(newEnemy);
            }
        }
        //replace our current enemies list
        enemiesList = newSprites;
        return newEnemiesFound;
    }

    /**
     * Gets the number of on screen enemies.
     *
     * @return the count
     */
    public int getCount() 
    {
        return enemiesList.size();
    }


    /**
     * Returns this instances copy of the enemy list.  
     * <b> changing will affect this instance </b>
     *
     * @return the enemies list
     */
    public ArrayList<SpriteSim> getEnemiesList()
    {
        return enemiesList;
    }


    /**
     * Creates a new EnemySim based on the type value.
     *
     * @param _x the x location of the enemy
     * @param _y the y location of the enemy
     * @param _type the type of the enemy
     * @param _sim the current MarioSim
     * @return a new EnemySim
     */
    public EnemySim createEnemy(float _x, float _y, int _type, MarioSim _sim)
    {
        switch (_type)
        {
            case Sprite.KIND_MUSHROOM:
                return new MushroomSim(_x, _y, _type);
            case Sprite.KIND_GREEN_MUSHROOM:
                return new GreenMushroomSim(_x, _y, _type);
            case Sprite.KIND_FIRE_FLOWER:
                return new FireFlowerSim(_x, _y, _type);
            case Sprite.KIND_ENEMY_FLOWER:
                return new EnemyFlowerSim(_x, _y, _type, _sim);
            case Sprite.KIND_BULLET_BILL:
                return new BulletSim(_x, _y, _type);
            case Sprite.KIND_WAVE_GOOMBA:
                return new WaveSim(_x, _y, _type);
            case Sprite.KIND_SHELL:
                return new ShellSim(_x, _y, _type);
            case Sprite.KIND_FIREBALL:
                return new FireBallSim(_x,_y,_type, -1);
            default:
                return new EnemySim(_x, _y, _type);
        }
    }

    /**
     * Returns a string representation of the enemy type supplied
     *
     * @param _type the type to name
     * @return the enemy name
     */
    public static String nameEnemy(int _type)
    {
        String enemyName = "Unknown";

        switch (_type)
        {
            case Sprite.KIND_MARIO:
                return "Mario";
            case Sprite.KIND_GOOMBA:
                return "Goomba";
            case Sprite.KIND_GOOMBA_WINGED:
                return "Goomba Winged";
            case Sprite.KIND_RED_KOOPA:
                return "Red Koopa";
            case Sprite.KIND_RED_KOOPA_WINGED:
                return "Red Koopa Winged";
            case Sprite.KIND_GREEN_KOOPA:
                return "Green Koopa";
            case Sprite.KIND_GREEN_KOOPA_WINGED:
                return "Green Koopa Winged";
            case Sprite.KIND_SPIKY:
                return "Spiky";
            case Sprite.KIND_SPIKY_WINGED:
                return "Spiky Winged";
            case Sprite.KIND_BULLET_BILL:
                return "Bullet";
            case Sprite.KIND_ENEMY_FLOWER:
                return "Flower";
            case Sprite.KIND_SHELL:
                return "Shell";
            case Sprite.KIND_MUSHROOM:
                return "Mushroom";
            case Sprite.KIND_FIRE_FLOWER:
                return "Power up Flower";
            case Sprite.KIND_GREEN_MUSHROOM:
                return "Green mushroom";
            case Sprite.KIND_PRINCESS:
                return "Princess";
            case Sprite.KIND_FIREBALL:
                return "Fireball";
        }
        return enemyName;
    }
}
