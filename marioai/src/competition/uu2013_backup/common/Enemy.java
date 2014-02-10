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

    private static ArrayList<EnemySim> enemiesList;

    public Enemy() //Fight the power!!
    {

    }

    public static ArrayList<EnemySim> cloneEnemies()
    {
        ArrayList<EnemySim> enemiesListCopy = new ArrayList<EnemySim>();

        if (enemiesList != null)
        {
            for (EnemySim oldEnemy: enemiesList)
            {
                EnemySim enemySim = oldEnemy.clone();
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

    public static void setEnemies(float [] enemyObservation, float marioX, float marioY, int halfSceneWidth, int halfSceneHeight)
    {
        if (enemiesList == null)
        {
            enemiesList = new ArrayList<EnemySim>();
        }
        ArrayList<EnemySim> newEnemies = new ArrayList<EnemySim>();

        //System.out.println("lookAHead: " + lookAHead + " lookBelow: " + lookBelow);
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

        for (int x = 0; x < enemyObservation.length; x+=3)
        {
            EnemySim sim = Enemy.createEnemy((marioX + enemyObservation[x+1]), (marioY + enemyObservation[x+2]), (int)enemyObservation[x]);
            //check for existing enemy
            boolean foundOne = false;
            System.out.println("Actu: Type (" + Enemy.nameEnemy((int)enemyObservation[x])  + "):" + (int)enemyObservation[x] + " X: " + (marioX + enemyObservation[x+1]) + " Y: " + (marioY + enemyObservation[x+2]));

            //We can't see the map outside the fieldwith, so can't model enemy behaviour
            if (Enemy.withinScope(marioX, marioY,halfSceneWidth,halfSceneHeight,sim))
            {
                for (EnemySim e: enemiesList)
                {
                    //check the types match
                    if (e.getType() == sim.getType())
                    {
                        //update the sim position
                        //System.out.println("Prev: Type (" + Enemy.nameEnemy(e.getType())  + "):" + e.getType() + " X: "+ e.getX() + " Y: " + e.getY() + " XA: " + e.getXA() + " YA: " + e.getYA()  +" Facing" + e.getFacing());
                        e.move();
                        //System.out.println("Post: Type (" + Enemy.nameEnemy(e.getType()) + "):" + e.getType() + " X: " + e.getX() + " Y: " + e.getY() + " XA: " + e.getXA() + " YA: " + e.getYA()+" Facing" + e.getFacing());
                        //System.out.println("Type Match!");
                        float maxDelta = 2.01F * EnemySim.SIDE_WAY_SPEED; //two pixel diff

                        //is the enemy close enough?
                        if ((Math.abs(e.getX() - sim.getX()) < maxDelta) && (Math.abs(e.getY() - sim.getY()) < maxDelta))
                        {
                            //System.out.println("XY inside range");
                            //x position is off
                            if (Math.abs(e.getX() - sim.getX()) > 0)
                            {
                                //System.out.println("X is off");
                                e.setFacing(e.getFacing() * -1);
                                e.setXA((e.getAccurateX() - sim.getX()) * 0.89F);
                                e.setFacing((e.getAccurateX() - sim.getX()) * 0.89F > 0 ? 1 : -1);
                                e.setX(sim.getX());
                                foundOne = true;
                            }
                            //Y off by less than 8 and winged;
                            if ((Math.abs(e.getY() - sim.getY()) < 8) && e.isWinged())
                            {
                                //System.out.println("Winged");
                                e.setYA((sim.getY() - e.getAccurateY()) *0.95F +0.6F );
                                e.setY(sim.getY());
                                foundOne = true;
                            }
                            //not a winged creature
                            if ((Math.abs(e.getY() - sim.getY()) <=2) && (Math.abs(e.getY() - sim.getY()) !=0))
                            {
                                //System.out.println("Y off, <2");
                                if (Math.abs(e.getAccurateY() - sim.getY()) > 0)
                                {
                                    //System.out.println("Y Accurate off");
                                    e.setYA((e.getAccurateY() - sim.getY()) * 0.85F + 2);
                                    e.setY(sim.getY());
                                }
                                else
                                {
                                    //System.out.println("Updating YA");
                                    e.setYA(e.getYA() * 0.85F);
                                    e.setY(sim.getY());
                                }
                                foundOne = true;
                            }
                            if ((Math.abs(e.getX() - sim.getX()) == 0) && (Math.abs(e.getY() - sim.getY()) == 0))
                            {
                                foundOne = true;
                            }
                        }
                    }
                    if (foundOne)
                    {
                        //System.out.println("Updating!");
                        e.setAccurateY(sim.getY());
                        e.setAccurateX(sim.getX());
                        newEnemies.add(e);
                        break;
                    }
                }
                if(!foundOne)
                {
                    //System.out.println("Adding!");
                    sim.setXA(2);
                    sim.setAccurateY(sim.getY());
                    sim.setAccurateX(sim.getX());
                    sim.drop();
                    newEnemies.add(sim);
                }
            }
            else
            {
                //System.out.println("Can't model yet!");
            }

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
        enemiesList = newEnemies;
        //System.out.println("----------------------------------------------------------------------");
    }

    public static int getCount() {
        return Enemy.enemiesList.size();
    }


    public ArrayList<EnemySim> getEnemiesList()
    {
        return enemiesList;
    }

    public void moveEnemies(int moves)
    {
        ArrayList<EnemySim> temp = (ArrayList<EnemySim>) enemiesList.clone();

        for (int x = 0; x < moves; x++)
        {
            for (EnemySim s: temp)
            {
                s.move();
                //System.out.println((x+1) +"   : Type: " + s.getType() + " X: " + s.getX() + "Y: " + s.getY());
            }
        }
        //System.out.println("====================================================================");
    }

    public static EnemySim createEnemy(float _x, float _y, int _type)
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
