package competition.uu2013.common.Sprites;

import competition.uu2013.common.Sprites.EnemySim;

/**
 * Created with IntelliJ IDEA.
 * User: fluffy
 * Date: 27/01/14
 * Time: 14:15
 * To change this template use File | Settings | File Templates.
 */
public class EnemyFlowerSim extends EnemySim implements Cloneable
{
    private int yStart;
    private int jumpTime = 0;

    public EnemyFlowerSim(float _x, float _y, int _type)
    {
        super(_x, _y, _type);

        this.height = 12;
        this.width = 2;

        yStart = (int)_y/16;
        ya = -8;

        this.y -= 1;

        for (int i = 0; i < 4; i++)
        {
            move();
        }
    }

    @Override
    public EnemyFlowerSim clone() throws CloneNotSupportedException
    {
        EnemyFlowerSim n = (EnemyFlowerSim) super.clone();
        n.yStart = this.yStart;
        n.jumpTime = this.jumpTime;
        return n;
    }

    public void move()
    {
        if (deadTime > 0)
        {
            deadTime--;

            if (deadTime == 0)
            {
                deadTime = 1;
                return;
            }

            x += xa;
            y += ya;
            ya *= 0.95;
            ya += 1;
        }

        if (y >= yStart)
        {
            y = yStart;

            int xd = (int) (Math.abs(this.marioSim.getX() - x));
            jumpTime++;
            if (jumpTime > 40 && xd > 24)
            {
                ya = -8;
            } else
            {
                ya = 0;
            }
        }
        else
        {
            jumpTime = 0;
        }

        y += ya;
        ya *= 0.9;
        ya += 0.1f;
    }

}
