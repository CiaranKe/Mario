package competition.uu2013.common.Sprites;

import ch.idsia.benchmark.mario.engine.sprites.Sprite;
import competition.uu2013.common.Map;

/**
 * Created with IntelliJ IDEA.
 * User: fluffy
 * Date: 24/01/14
 * Time: 10:56
 * To change this template use File | Settings | File Templates.
 */
public class EnemySim extends SpriteSim implements Comparable
{

    private final float SIDE_WAY_SPEED = 1.75f;
    protected float lastX, lastY;
    protected int height;
    protected int width;
    protected boolean avoidCliffs; //red koopa
    protected boolean winged;
    protected boolean onGround;
    protected boolean seen;
    protected boolean flyDeath; //bumpCheck, fireballCollide, shellCollide
    protected int deadTime;
    protected boolean noFireballDeath;
    protected boolean dead;
    protected MarioSim marioSim;

    public EnemySim(float _x, float _y, int _type )
    {
        this.x = _x;
        this.y = _y;
        this.type = _type;
        this.deadTime = 0;
        this.dead = false;
        this.onGround = false;
        this.flyDeath = false;
        this.avoidCliffs = false;
        this.width = 4;
        this.height = 24;
        this.facing = -1;
        this.seen = false;
        this.noFireballDeath = false;

        switch (this.type)
        {
            case Sprite.KIND_GOOMBA:
                break;
            case Sprite.KIND_GOOMBA_WINGED:
                this.winged = true;
                break;
            case Sprite.KIND_RED_KOOPA:
                this.avoidCliffs = true;
                break;
            case Sprite.KIND_RED_KOOPA_WINGED:
                this.avoidCliffs = true;
                this.winged = true;
                break;
            case Sprite.KIND_GREEN_KOOPA:
                break;
            case Sprite.KIND_GREEN_KOOPA_WINGED:
                this.winged = true;
                break;
            case Sprite.KIND_SPIKY:
                this.noFireballDeath = true;
                break;
            case Sprite.KIND_SPIKY_WINGED:
                this.noFireballDeath = true;
                this.winged = true;
                break;
            case Sprite.KIND_ENEMY_FLOWER:
                break;
            case Sprite.KIND_WAVE_GOOMBA:
                this.winged = true;
                break;
        }

    }

    public EnemySim clone()
    {
        EnemySim n = new EnemySim(this.x, this.y, this.type);
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
        return n;
    }

    public void setMarioSim(MarioSim _marioSim)
    {
        this.marioSim = _marioSim;
    }

    public int height ()
    {
        return this.height;
    }

    @Override
    public void setXY(float _x, float _y)
    {
        this.lastX = this.x;
        this.lastY = this.y;
        this.x = _x;
        this.y = _y;
        this.xa = this.x - this.lastX;
        this.ya = (this.y - this.lastY);

        if (this.xa == 0)
        {
            deadTime = 9;
        }
    }

    public void setXY(float _x, float _y, float _xa, float _ya)
    {
        this.lastX = this.x;
        this.lastY = this.y;
        this.x = _x;
        this.y = _y;
        this.xa = _xa;
        this.ya = _ya;

        if (this.xa == 0)
        {
            deadTime = 9;
        }
    }

    public void drop()
    {
        if (this.winged && !this.seen)
        {
            this.seen = true;
            this.ya = 0.6F;
            this.xa = -1.5575F;
        }
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

            if (flyDeath)
            {
                x += xa;
                y += ya;
                ya *= 0.95;
                ya += 1;
            }
            return;
        }

        if (xa > 2)
            facing = 1;
        else if (xa < -2)
            facing = -1;

        xa = facing * SIDE_WAY_SPEED;

        if (!move(xa, 0)) facing = -facing;
        onGround = false;
        move(0, ya);

        ya *= winged ? 0.95f : 0.85f;
        xa *= GROUND_INERTIA;


        if (!onGround)
        {
            if (winged)
            {
                ya += 0.6f * 1;
            } else
            {
                ya += 2;
            }
        }
        else if (winged)
        {
            ya = -10;
        }
    }

    public boolean move(float xa, float ya)
    {

        while (xa > 8)
        {
            if (!move(8, 0)) return false;
            xa -= 8;
        }
        while (xa < -8)
        {
            if (!move(-8, 0)) return false;
            xa += 8;
        }
        while (ya > 8)
        {
            if (!move(0, 8)) return false;
            ya -= 8;
        }
        while (ya < -8)
        {
            if (!move(0, -8)) return false;
            ya += 8;
        }


        boolean collide = false;
        if (ya > 0)
        {
            if (isBlocking(x + xa - width, y + ya, xa, 0)) collide = true;
            else if (isBlocking(x + xa + width, y + ya, xa, 0)) collide = true;
            else if (isBlocking(x + xa - width, y + ya + 1, xa, ya)) collide = true;
            else if (isBlocking(x + xa + width, y + ya + 1, xa, ya)) collide = true;
        }
        if (ya < 0)
        {
            if (isBlocking(x + xa, y + ya - height, xa, ya)) collide = true;
            else if (collide || isBlocking(x + xa - width, y + ya - height, xa, ya)) collide = true;
            else if (collide || isBlocking(x + xa + width, y + ya - height, xa, ya)) collide = true;
        }
        if (xa > 0)
        {
            if (isBlocking(x + xa + width, y + ya - height, xa, ya)) collide = true;
            if (isBlocking(x + xa + width, y + ya - height / 2, xa, ya)) collide = true;
            if (isBlocking(x + xa + width, y + ya, xa, ya)) collide = true;

            if (avoidCliffs && onGround && !Map.isBlocking((int) ((x + xa + width) / 16), (int) ((y) / 16 + 1), xa, 1))
                collide = true;
        }
        if (xa < 0)
        {
            if (isBlocking(x + xa - width, y + ya - height, xa, ya)) collide = true;
            if (isBlocking(x + xa - width, y + ya - height / 2, xa, ya)) collide = true;
            if (isBlocking(x + xa - width, y + ya, xa, ya)) collide = true;

            if (avoidCliffs && onGround && !Map.isBlocking((int) ((x + xa - width) / 16), (int) ((y) / 16 + 1), xa, 1))
                collide = true;
        }

        if (collide)
        {
            if (xa < 0)
            {
                x = (int) ((x - width) / 16) * 16 + width;
                this.xa = 0;
            }
            if (xa > 0)
            {
                x = (int) ((x + width) / 16 + 1) * 16 - width - 1;
                this.xa = 0;
            }
            if (ya < 0)
            {
                y = (int) ((y - height) / 16) * 16 + height;
//                jumpTime = 0;
                this.ya = 0;
            }
            if (ya > 0)
            {
                y = (int) (y / 16 + 1) * 16 - 1;
                onGround = true;
            }
            return false;
        }
        else
        {

            x += xa;
            y += ya;
            return true;
        }
    }

    private boolean isBlocking(float _x, float _y, float xa, float ya)
    {
        int x = (int) (_x / 16);
        int y = (int) (_y / 16);
        if (x == (int) (this.x / 16) && y == (int) (this.y / 16))
        {
            return false;
        }

        return Map.isBlocking(x, y, xa, ya);
    }

    public int compareTo(Object o)
    {
        EnemySim oldSim = (EnemySim) o;

        if (this.type != oldSim.getType() )
        {
            return -1;
        }

        boolean xMatch = false;
        boolean yMatch = false;

        //allow a 2 pixel diff for movement.
        float maxDelta = 2.01f * SIDE_WAY_SPEED;

        if (Math.abs(oldSim.getX() - this.x) < maxDelta)
        {
            xMatch = true;
        }

        if (Math.abs(oldSim.getY() - this.y) < (maxDelta + 10.0F))
        {
            yMatch = true;
        }

        if (xMatch && yMatch)
        {
            return 0;
        }
        return -1;
    }

    public void bumpCheck(int xTile, int yTile, MarioSim marioSim)
    {
        if (deadTime != 0)
        {
            return;
        }

        if (x + width > xTile * 16 && x - width < xTile * 16 + 16 && yTile == (int) ((y - 1) / 16))
        {
            xa = -marioSim.getFacing() * 2;
            ya = -5;
            flyDeath = true;
            deadTime = 100;
            winged = false;
        }
    }

    public final boolean spiky()
    {
        switch (type)
        {
            case Sprite.KIND_ENEMY_FLOWER:
            case Sprite.KIND_SPIKY:
            case Sprite.KIND_SPIKY_WINGED:
                return true;
        }
        return false;
    }

    public void collideCheck()
    {
        if (deadTime != 0)
        {
            return;
        }

        float xMarioD = marioSim.getX() - x;
        float yMarioD = marioSim.getY() - y;
        float height = this.height();


        if (xMarioD > -width*2-4 && xMarioD < width*2+4) {
            if (yMarioD > -height && yMarioD < marioSim.height()) {
                if (!spiky() && marioSim.getYa() > 0 && yMarioD <= 0 && (!marioSim.isOnGround() || !!marioSim.wasOnGround()))
                {
                    marioSim.stomp(this);
                }
                else
                {
                    marioSim.getHurt();
                }
            }
        }
    }

    public boolean fireballCollideCheck(FireBallSim fireball)
    {
        if (deadTime != 0)
        {
            return false;
        }

        float xD = fireball.getX() - x;
        float yD = fireball.getY() - y;

        if (xD > -16 && xD < 16)
        {
            if (yD > -height && yD < fireball.height)
            {
                if (noFireballDeath)
                {
                    return true;
                }

                xa = fireball.facing * 2;
                ya = -5;
                flyDeath = true;
                this.dead = true;
                deadTime = 100;
                winged = false;
                return true;
            }
        }
        return false;
    }
}
