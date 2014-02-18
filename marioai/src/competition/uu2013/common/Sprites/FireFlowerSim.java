package competition.uu2013.common.Sprites;

/**
 * Created with IntelliJ IDEA.
 * User: fluffy
 * Date: 27/01/14
 * Time: 17:26
 * To change this template use File | Settings | File Templates.
 */
public class FireFlowerSim extends EnemySim implements Cloneable
{
    private int life;

    public FireFlowerSim(float _x, float _y, int _type)
    {
        super(_x, _y, _type);

        height = 12;
        facing = 1;
        life = 0;
    }

    @Override
    public FireFlowerSim clone() throws CloneNotSupportedException
    {
        FireFlowerSim n = (FireFlowerSim) super.clone();
        n.life = this.life;
        return n;
    }

    public void collideCheck()
    {
        float xMarioD = marioSim.getX() - x;
        float yMarioD = marioSim.getY() - y;
        float w = 16;
        if (xMarioD > -16 && xMarioD < 16)
        {
            if (yMarioD > -height && yMarioD < marioSim.height())
            {
                marioSim.setMode(true, true);
            }
        }
    }

    public void move()
    {
        if (life < 9)
        {
            y--;
            life++;
            return;
        }
    }

    public void setXY(float _x, float _y)
    {
        this.x = _x;
        this.y = _y;
    }

}
