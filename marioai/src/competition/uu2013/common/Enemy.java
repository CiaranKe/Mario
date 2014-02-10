package competition.uu2013.common;

import ch.idsia.benchmark.mario.engine.sprites.Sprite;
import competition.uu2013.common.Sprites.*;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: fluffy
 * Date: 27/01/14
 * Time: 15:22
 * To change this template use File | Settings | File Templates.
 */
public class Enemy
{

    private static ArrayList<SpriteSim> enemiesList;

    public Enemy() //Fight the power!!
    {
        enemiesList = new ArrayList<SpriteSim>();
    }

    public ArrayList<SpriteSim> cloneEnemies()
    {
        ArrayList<SpriteSim> enemiesListCopy = new ArrayList<SpriteSim>();

        if (enemiesList != null)
        {
            for (SpriteSim oldEnemy: enemiesList)
            {
                SpriteSim enemySim = oldEnemy.clone();
                enemiesListCopy.add(enemySim);
            }
        }
        return enemiesListCopy;
    }

    public static boolean withinScope(float marioX, float marioY, int halfSceneWidth, int halfSceneHeight, EnemySim sim)
    {
        float lookAHead = marioX + (halfSceneWidth * 16);
        float lookBelow = marioY + (halfSceneHeight * 16);
        float lookBehind = marioX - (halfSceneWidth * 16);
        float lookAbove = marioY - (halfSceneHeight * 16);

        if ((sim.getX() < lookAHead) && (sim.getY() < lookBelow) && (sim.getX() > lookBehind) && (sim.getY() > lookAbove))
        {
            return true;
        }
        return false;
    }

    public boolean setEnemies(float [] enemies, float marioX, float marioY, int halfSceneWidth, int halfSceneHeight)
    {

        /*
        if (!winged)
        {
	        xa = (lastX - x) *0.89F;
	        if ((lastY - y) > 0)
	        {
		        ya = lastY - y) * 0.85F +2;
	        }
	        else
	        {
		        ya = lastYA * 0.85F;
	        }
        }
        else
        {
	        ya = (y - lastY) *0.89F + 0.6F;
        }
        */
        ArrayList<SpriteSim> newSprites = new ArrayList<SpriteSim>();


        boolean requireReplanning = false;

        for (int i = 0; i < enemies.length; i += 3)
        {
            SpriteSim newEnemy = this.createEnemy((marioX + enemies[i+1]),(marioY + enemies[i+2]),(int)enemies[i]);
            System.out.println("Actu: Type (" + this.nameEnemy((int)enemies[i])  + "):" + (int)enemies[i] + " X: " + (marioX + enemies[i+1]) + " Y: " + (marioY + enemies[i+2]));


            if (newEnemy.getType() == -1 || newEnemy.getType() == 15)
                continue;

            // is there already an enemy here?
            float maxDelta = 2.01f * 1.75f;
            boolean enemyFound = false;
            for (SpriteSim sprite:enemiesList)
            {
                System.out.println("Sim: Type (" + Enemy.nameEnemy(sprite.getType()) + "):" + sprite.getType() + " X: " + sprite.getX() + " Y: " + sprite.getY() + " XA: " + ((EnemySim) sprite).getXA() + " YA: " + ((EnemySim) sprite).getYA() + " Facing" + ((EnemySim) sprite).getFacing());
                // check if object is of same kind and close enough
                if (sprite.getType() == newEnemy.getType()
                        && Math.abs(sprite.getX() - newEnemy.getX()) < maxDelta
                        && ((Math.abs(sprite.getY() - newEnemy.getY()) < maxDelta) || sprite.getType() == Sprite.KIND_ENEMY_FLOWER))
                {
                    if (Math.abs(sprite.getX() - newEnemy.getX()) > 0)
                    {
                        ((EnemySim)sprite).setFacing(sprite.getFacing() * -1);
                        requireReplanning = true;
                        ((EnemySim) sprite).setX(newEnemy.getX());
                    }
                    if ((sprite.getY() - newEnemy.getY()) != 0 && sprite.getType() == Sprite.KIND_ENEMY_FLOWER)
                    {
                        ((EnemySim) sprite).setYA((newEnemy.getY() - ((EnemySim) sprite).getAccurateY()) * 0.89f);//+= sprite.y - y;
                        ((EnemySim) sprite).setY(newEnemy.getY());
                    }
                    enemyFound = true;
                }

                if (sprite.getType() == newEnemy.getType() &&  (sprite.getX() - newEnemy.getX()) == 0 && (sprite.getY() - newEnemy.getY()) != 0
                        &&  Math.abs(sprite.getY() - newEnemy.getY()) < 8 && sprite.getType() != Sprite.KIND_SHELL &&
                        sprite.getType() != Sprite.KIND_BULLET_BILL && ((EnemySim) sprite).isWinged())
                {
                    // x accurate but y wrong. flying thing

                    ((EnemySim) sprite).setYA((newEnemy.getY() - ((EnemySim) sprite).getAccurateY()) * 0.95f + 0.6f); // / 0.89f;
                    ((EnemySim) sprite).setY(newEnemy.getY());
                    ((EnemySim)sprite).setKnownYA(true);
                    enemyFound = true;
                    requireReplanning = true;
                }
                if (sprite.getType() == newEnemy.getType() && (sprite.getX() - newEnemy.getX()) == 0 && (sprite.getY() - newEnemy.getY()) != 0 &&
                        Math.abs(sprite.getY() - newEnemy.getY()) <= 2 && ((EnemySim)sprite).isYAUnknown() && ((EnemySim) sprite).getAccurateY() != 0)
                {
                    // should be not winged, falling down a cliff
                    ((EnemySim) sprite).setYA(newEnemy.getY() - ((EnemySim) sprite).getAccurateY() * 0.85f + 2); // / 0.89f;
                    ((EnemySim) sprite).setY(newEnemy.getY());

                    ((EnemySim) sprite).setKnownYA(false);
                    enemyFound = true;
                }

                if (enemyFound)
                {
                    System.out.println("Updating!");
                    newSprites.add(sprite);
                    ((EnemySim) sprite).setAccurateX(newEnemy.getX());
                    ((EnemySim) sprite).setAccurateY(newEnemy.getY());
                    break;
                }
            }
            // didn't find a close enemy in our representation of the world,
            // create a new one.
            if (!enemyFound)
            {
                System.out.println("Adding!");
                requireReplanning = true;
                    // Add new enemy to the system.
                ((EnemySim)newEnemy).setXA(2);
                ((EnemySim)newEnemy).setAccurateX(newEnemy.getX());
                ((EnemySim)newEnemy).setAccurateY(newEnemy.getY());
                newSprites.add(newEnemy);
            }
        }
        //newSprites.add(mario);

        // add fireballs
        for (SpriteSim sprite:newSprites)
        {
            if (sprite.getType() == Sprite.KIND_FIREBALL)
                newSprites.add(sprite);
        }
        enemiesList = newSprites;
        return requireReplanning;

        /*
        EnemySim sim = this.createEnemy((marioX + enemyObservation[x+1]), (marioY + enemyObservation[x+2]), (int)enemyObservation[x]);
            //check for existing enemy
            boolean foundOne = false;
            System.out.println("Actu: Type (" + this.nameEnemy((int)enemyObservation[x])  + "):" + (int)enemyObservation[x] + " X: " + (marioX + enemyObservation[x+1]) + " Y: " + (marioY + enemyObservation[x+2]));

            for (EnemySim e: enemiesList)
            {
                //check the types match
                if (e.getType() == sim.getType())
                {
                    //update the sim position
                    System.out.println("Sim: Type (" + Enemy.nameEnemy(e.getType())  + "):" + e.getType() + " X: "+ e.getX() + " Y: " + e.getY() + " XA: " + e.getXA() + " YA: " + e.getYA()  +" Facing" + e.getFacing());
                    float maxDelta = 2.01F * EnemySim.SIDE_WAY_SPEED; //two pixel diff

                    //is the enemy close enough?
                    if ((Math.abs(e.getX() - sim.getX()) == 0) && (Math.abs(e.getY() - sim.getY()) == 0))
                    {
                        System.out.println("Exact Match");
                        foundOne = true;
                    }

                    else if ((Math.abs(e.getX() - sim.getX()) < maxDelta) && (Math.abs(e.getY() - sim.getY()) < maxDelta))
                    {
                        System.out.println("XY inside range");
                        //x position is off
                        if (Math.abs(e.getX() - sim.getX()) > 0)
                        {
                            System.out.println("X is off");
                            e.setFacing(e.getFacing() * -1);
                            e.setXA((e.getAccurateX() - sim.getX()) * 0.89F);
                            e.setFacing((e.getAccurateX() - sim.getX()) * 0.89F > 0 ? 1 : -1);
                            e.setX(sim.getX());
                            foundOne = true;
                        }
                        //Y off by less than 8 and winged;
                        if ((Math.abs(e.getY() - sim.getY()) < 8) && e.isWinged())
                        {
                            System.out.println("Winged");
                            e.setYA((sim.getY() - e.getAccurateY()) *0.95F +0.6F );
                            e.setY(sim.getY());
                            foundOne = true;
                        }
                        //not a winged creature
                        if ((Math.abs(e.getY() - sim.getY()) <=2) && (Math.abs(e.getY() - sim.getY()) !=0))
                        {
                            System.out.println("Y off, <2");
                            if (Math.abs(e.getAccurateY() - sim.getY()) > 0)
                            {
                                System.out.println("Y Accurate off");
                                e.setYA((e.getAccurateY() - sim.getY()) * 0.85F + 2);
                                e.setY(sim.getY());
                            }
                            else
                            {
                                System.out.println("Updating YA");
                                e.setYA(e.getYA() * 0.85F);
                                e.setY(sim.getY());
                            }
                            foundOne = true;
                        }

                    }
                }
                if (foundOne)
                {
                    System.out.println("Updating!");
                    e.setAccurateY(sim.getY());
                    e.setAccurateX(sim.getX());
                    newEnemies.add(e);
                    break;
                }
            }
            if(!foundOne)
            {
                System.out.println("Adding!");
                sim.setXA(2);
                sim.setAccurateY(sim.getY());
                sim.setAccurateX(sim.getX());
                sim.drop();
                newEnemies.add(sim);
            }
            */

            /*
            for (EnemySim e: enemiesList)
                {
                e.move();
                System.out.println("Post: Type (" + Enemy.nameEnemy(e.getType())  + "):" + e.getType() + " X: "+ e.getX() + " Y: " + e.getY() + " XA: " + e.getXA() + " YA: " + e.getYA() );
                if (e.getType() == sim.getType())
                {
                    float maxDelta = 2.01F * EnemySim.SIDE_WAY_SPEED; //two pixel diff

                    System.out.println("X Match (Delta): " + (Math.abs(e.getX() - sim.getX()) < maxDelta));
                    System.out.println("Y Match (Delta): " + (Math.abs(e.getY() - sim.getY()) < maxDelta));
                    if ((Math.abs(e.getX() - sim.getX()) < maxDelta) && (Math.abs(e.getY() - sim.getY()) < maxDelta))
                    {
                        //System.out.println("X is off by: " + Math.abs(e.getX() - sim.getX()));
                        if (Math.abs(e.getX() - sim.getX()) > 0 )
                        {
                            e.setFacing(e.getFacing() * -1);
                            e.setX(sim.getX());
                            foundOne = true;
                        }

                        //System.out.println("Exact X match: " + (Math.abs(e.getX() - sim.getX())== 0));
                        //System.out.println("Y Doesn't match:" + (Math.abs(e.getY() - sim.getY()) != 0));
                        //System.out.println("Y off by less than 10:" + (Math.abs(e.getY() - sim.getY()) < 10));
                        //System.out.println("Winged: " +e.isWinged());
                        if ((Math.abs(e.getX() - sim.getX())== 0) && ((Math.abs(e.getY() - sim.getY()) != 0) || (Math.abs(e.getY() - sim.getY()) < 8)) && e.isWinged())
                        {
                            e.setYA((sim.getY() - e.getAccurateY()) *0.95F +0.6F );
                            e.setY(sim.getY());
                            foundOne = true;
                        }

                        //System.out.println("Perfect X match: " +(Math.abs(e.getX() - sim.getX())== 0));
                        //System.out.println("Y doesn't match: " + (Math.abs(e.getY() - sim.getY()) != 0));
                        //System.out.println("Y off by less than 2: " +(Math.abs(e.getY() - sim.getY()) <=2));
                        //System.out.println("Accurate Y set: " + (e.getAccurateY() !=0));
                        //System.out.println("YA is unknown: " + e.isYAUnknown());
                        if ((Math.abs(e.getX() - sim.getX())== 0) && (Math.abs(e.getY() - sim.getY()) != 0) && (Math.abs(e.getY() - sim.getY()) <=2) && (e.getAccurateY() !=0) && e.isYAUnknown())
                        {
                            float newYA;

                            if (sim.getY() - e.getAccurateY() > 0)
                            {
                                newYA = (sim.getY() - e.getAccurateY()) * 0.89F + 2;
                            }
                            else
                            {
                                newYA = e.getYA() * 0.85F;
                            }

                            e.setYA(newYA);
                            e.setY(sim.getY());
                            e.setKnownYA();
                            foundOne = true;
                        }
                        if ((Math.abs(e.getX() - sim.getX()) == 0 ) && (Math.abs(e.getY() - sim.getY()) == 0))
                        {
                            foundOne = true;
                        }
                    }
                }

                if (foundOne)
                {
                    // XA = (X - lastX) * 0.89F
                    System.out.println("Updating!");
                    e.setAccurateY(sim.getY());
                    e.setAccurateX(sim.getX());
                    newEnemies.add(e);
                }
            }
            if(!foundOne)
            {
                System.out.println("Adding!");
                sim.setXA(2);
                sim.setAccurateY(sim.getY());
                sim.setAccurateX(sim.getX());
                sim.drop();
                //sim.move();
                newEnemies.add(sim);
            }

             */
    }

    public int getCount() {
        return Enemy.enemiesList.size();
    }


    public ArrayList<SpriteSim> getEnemiesList()
    {
        return enemiesList;
    }


    public EnemySim createEnemy(float _x, float _y, int _type)
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
                return new EnemyFlowerSim(_x, _y, _type);
            case Sprite.KIND_BULLET_BILL:
                return new BulletSim(_x, _y, _type);
            case Sprite.KIND_WAVE_GOOMBA:
                return new WaveSim(_x, _y, _type);
            case Sprite.KIND_FIREBALL:
                return new FireBallSim(_x,_y,_type);
            case Sprite.KIND_SHELL:
                return new ShellSim(_x, _y, _type);
            default:
                return new EnemySim(_x, _y, _type);
        }
    }

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
        }

        return enemyName;
    }
}
