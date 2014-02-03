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

    public static void setEnemies(float [] enemyObservation, float marioX, float marioY)
    {
        if (enemiesList == null)
        {
            enemiesList = new ArrayList<EnemySim>();
        }
        ArrayList<EnemySim> newEnemies = new ArrayList<EnemySim>();

        for (int x = 0; x < enemyObservation.length; x+=3)
        {
            EnemySim sim = Enemy.createEnemy((marioX + enemyObservation[x+1]), (marioY + enemyObservation[x+2]), (int)enemyObservation[x]);
            //check for existing enemy
            boolean foundOne = false;
            //System.out.println("Actu: Type (" + Enemy.nameEnemy((int)enemyObservation[x])  + "):" + (int)enemyObservation[x] + " X: " + (marioX + enemyObservation[x+1]) + " Y: " + (marioY + enemyObservation[x+2]));

            for (EnemySim e: enemiesList)
            {
                //System.out.println("Prev: Type (" + Enemy.nameEnemy(e.getType())  + "):" + e.getType() + " X: "+ e.getX() + " Y: " + e.getY() + " XA: " + e.getXA() + " YA: " + e.getYA() );
                e.move();
                //System.out.println("Post: Type (" + Enemy.nameEnemy(e.getType())  + "):" + e.getType() + " X: "+ e.getX() + " Y: " + e.getY() + " XA: " + e.getXA() + " YA: " + e.getYA() );
                //is it the same kind and close enough?
                if (e.getType() == sim.getType())
                {
                    float maxDelta = 2.01F * EnemySim.SIDE_WAY_SPEED; //two pixel diff

                    //System.out.println("X Match (Delta): " + (Math.abs(e.getX() - sim.getX()) < maxDelta));
                    //System.out.println("Y Match (Delta): " + (Math.abs(e.getY() - sim.getY()) < maxDelta));
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
                            e.setYA((sim.getY() - e.getAccurateY()) *0.85F);
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
                    //System.out.println("Updating!");
                    e.setAccurateY(sim.getY());
                    newEnemies.add(e);
                }
            }
            if(!foundOne)
            {
                //System.out.println("Adding!");
                sim.setXA(2);
                sim.setAccurateY(sim.getY());
                sim.drop();
                //sim.move();
                newEnemies.add(sim);
            }
        }
        enemiesList = newEnemies;
        //System.out.println("----------------------------------------------------------------------");
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
