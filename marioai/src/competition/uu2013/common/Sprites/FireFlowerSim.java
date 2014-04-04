/*
 * 
 */
package competition.uu2013.common.Sprites;

// TODO: Auto-generated Javadoc
/**
 * Created with IntelliJ IDEA.
 * User: fluffy
 * Date: 27/01/14
 * Time: 17:26
 * To change this template use File | Settings | File Templates.
 */
public class FireFlowerSim extends EnemySim implements Cloneable
{
    
    /** The life. */
    private int life;

    /**
     * Instantiates a new fire flower sim.
     *
     * @param _x the _x
     * @param _y the _y
     * @param _type the _type
     */
    public FireFlowerSim(float _x, float _y, int _type)
    {
        super(_x, _y, _type);

        height = 12;
        facing = 1;
        life = 0;
    }

    /* (non-Javadoc)
     * @see competition.uu2013.common.Sprites.EnemySim#clone()
     */
    @Override
    public FireFlowerSim clone() throws CloneNotSupportedException
    {
        FireFlowerSim n = (FireFlowerSim) super.clone();
        n.life = this.life;
        return n;
    }

    /* (non-Javadoc)
     * @see competition.uu2013.common.Sprites.EnemySim#collideCheck()
     */
    public void collideCheck()
    {
        float xMarioD = marioSim.getXLocation() - xLocation;
        float yMarioD = marioSim.getYLocation() - yLocation;
        float w = 16;
        if (xMarioD > -16 && xMarioD < 16)
        {
            if (yMarioD > -height && yMarioD < marioSim.getHeight())
            {
                marioSim.setMode(true, true);
            }
        }
    }

    /* (non-Javadoc)
     * @see competition.uu2013.common.Sprites.EnemySim#move()
     */
    public void move()
    {
        if (life < 9)
        {
            yLocation--;
            life++;
            return;
        }
    }

    /* (non-Javadoc)
     * @see competition.uu2013.common.Sprites.EnemySim#setXYLocation(float, float)
     */
    public void setXYLocation(float _x, float _y)
    {
        this.xLocation = _x;
        this.yLocation = _y;
    }

}
