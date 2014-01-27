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
    private ArrayList<EnemySim> enemiesListCopy;

    public Enemy()
    {
        this.enemiesListCopy = new ArrayList<EnemySim>();
    }

    public Enemy clone(MarioSim marioSim)
    {
        Enemy e = new Enemy();

        for (EnemySim oldEnemy: enemiesList)
        {
            EnemySim enemySim = oldEnemy.clone();
            enemySim.setMarioSim(marioSim);
            e.enemiesListCopy.add(enemySim);
        }
        return e;
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
            EnemySim sim = Enemy.createEnemy(( marioX + enemyObservation[x+1]), ( marioY + enemyObservation[x+2]), (int) enemyObservation[x] );

            for (EnemySim s: enemiesList)
            {
                if (s.compareTo(sim) == 0)
                {
                    if (s.getY() != sim.getY() || s.getX() != sim.getX())
                    {
                        s.setXY(sim.getX(), sim.getY());
                    }
                    sim = s;
                }
                else
                {
                    sim.drop();
                }
            }
            sim.move();
            System.out.println("Type: " + sim.getType() + " X Diff:" + (sim.getX() - ( marioX + enemyObservation[x+1])) + " Y Diff: " + (sim.getY() - ( marioY + enemyObservation[x+2])));
            newEnemies.add(sim);
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

    public void checkFireballCollide(FireBallSim fireBallSim)
    {
        for (EnemySim e : enemiesListCopy)
        {
            e.checkFireballCollide(FireBallSim);
        }
    }

    public void checkShellCollide(ShellSim shellSim)
    {
        for (EnemySim e: enemiesListCopy)
        {
            e.checkShellCollide(shellSim);
        }
    }

    public void checkCollide(MarioSim marioSim)
    {
        for (EnemySim e:enemiesListCopy)
        {
            e.collideCheck();
        }
    }
}
