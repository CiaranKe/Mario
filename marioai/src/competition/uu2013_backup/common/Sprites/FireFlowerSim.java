package competition.uu2013.common.Sprites;

/**
 * Created with IntelliJ IDEA.
 * User: fluffy
 * Date: 27/01/14
 * Time: 17:26
 * To change this template use File | Settings | File Templates.
 */
public class FireFlowerSim extends EnemySim
{
    private int life;

    public FireFlowerSim(float _x, float _y, int _type)
    {
        super(_x, _y, _type);

        height = 12;
        facing = 1;
        life = 0;
    }

    public FireFlowerSim clone()
    {
        FireFlowerSim n = new FireFlowerSim(this.x, this.y, this.type);
        n.x = this.x;
        n.y = this.y;
        n.ya = this.ya;
        n.xa = this.xa;
        n.facing = this.facing;
        n.type = this.type;
        n.lastX = this.lastX;
        n.lastY = this.lastY;
        n.height = this.height;
        n.width = this.width;
        n.avoidCliffs = this.avoidCliffs;
        n.winged = this.winged;
        n.onGround = this.onGround;
        n.seen = this.seen;
        n.flyDeath = this.flyDeath;
        n.deadTime = this.deadTime;
        n.dead = this.dead;
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
