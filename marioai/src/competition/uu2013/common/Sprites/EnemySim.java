package competition.uu2013.common.Sprites;

import ch.idsia.benchmark.mario.engine.sprites.Sprite;
import competition.uu2013.common.level.Map;
import competition.uu2013.common.level.WorldSim;

/**
 * Created with IntelliJ IDEA.
 * User: fluffy
 * Date: 24/01/14
 * Time: 10:56
 * To change this template use File | Settings | File Templates.
 */
public class EnemySim extends SpriteSim implements Comparable
{

    public static final float SIDE_WAY_SPEED = 1.75f;
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
    protected WorldSim worldSim;
    protected float accurateY;
    protected float accurateX;
    protected boolean yaUnknown;
    protected boolean firstMove;
    protected float oldX;
    protected float oldY;
    protected Map map;

    public EnemySim(float _x, float _y, int _type )
    {
        this.x = _x;
        this.y = _y;
        this.firstMove = true;
        this.accurateY = 0;
        this.accurateX = 0;
        this.yaUnknown = true;
        this.type = _type;
        this.deadTime = 0;
        this.dead = false;
        this.flyDeath = false;
        this.avoidCliffs = false;
        this.width = 4;
        this.height = 24;
        this.facing = -1;
        this.seen = false;
        this.noFireballDeath = false;
        this.accurateY = 0;

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

    @Override
    public EnemySim clone()  throws CloneNotSupportedException
    {
        EnemySim n = new EnemySim(this.x, this.y, this.type);
        n.x = this.x;
        n.y = this.y;
        n.xa = this.xa;
        n.ya = this.ya;
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
        n.noFireballDeath = this.noFireballDeath;
        n.dead = this.dead;
        n.accurateY = this.accurateY;
        n.accurateX = this.accurateX;
        n.yaUnknown = this.yaUnknown;
        n.firstMove = this.firstMove;
        n.oldX = this.oldX;
        n.oldY = this.oldY;
        return n;
    }

    public boolean isWinged()
    {
        return this.winged;
    }

    public void setWorldSim(WorldSim _worldSim)
    {
        this.worldSim = _worldSim;
    }

    public void setMarioSim(MarioSim _marioSim)
    {
        this.marioSim = _marioSim;
    }

    public void setMapSim (Map _map)
    {
        this.map = _map;
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

    public float getXA()
    {
        return this.xa;
    }

    public float getYA()
    {
        return this.ya;
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
            //this.y -=  10.0F;
            this.ya = -10.F;
        }
        else if (!this.winged && !this.seen)
        {
            this.seen = true;
            this.xa = 2;
            this.ya = 1.7F;
            this.onGround = true;

        }
    }

    public void move()
    {
        this.oldX = x;
        this.oldY = y;

        if (xa == -1.5575F && ya == 1.7F)
        {
            System.out.println();
        }
        if (deadTime > 0)
        {
            deadTime--;

            if (deadTime == 0)
            {
                deadTime = 1;
                //spriteContext.removeSprite(this);
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

        float sideWaysSpeed = 1.75f;
        //        float sideWaysSpeed = onGround ? 2.5f : 1.2f;

        if (xa > 2)
            facing = 1;
        else if (xa < -2)
            facing = -1;

        xa = facing * sideWaysSpeed;
//    xa += facing == 1 ? -wind : wind;
//        mayJump = (onGround);



        if (!move(xa, 0)) facing = -facing;
        onGround = false;
        move(0, ya);



        ya *= winged ? 0.95f : 0.85f;
        if (onGround)
        {
            xa *= (GROUND_INERTIA + windScale(windCoeff, facing) + iceScale(iceCoeff));
        } else
        {
            xa *= (AIR_INERTIA + windScale(windCoeff, facing) + iceScale(iceCoeff));
        }

        if (!onGround)
        {
            if (winged)
            {
                ya += 0.6f * 1.0F;
            } else
            {
                ya += 2.0F;
            }
        } else if (winged)
        {
            ya = -10;
        }
        //System.out.println("Sim\t: X:\t"+x + " XA:\t" +xa + " Y:\t" +y + " YA:\t" + ya + " OnGround: " + onGround);
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

            if (avoidCliffs && onGround && !map.isBlocking((int) ((x + xa + width) / 16), (int) ((y) / 16 + 1)))
                collide = true;
        }
        if (xa < 0)
        {
            if (isBlocking(x + xa - width, y + ya - height, xa, ya)) collide = true;
            if (isBlocking(x + xa - width, y + ya - height / 2, xa, ya)) collide = true;
            if (isBlocking(x + xa - width, y + ya, xa, ya)) collide = true;

            if (avoidCliffs && onGround && !map.isBlocking((int) ((x + xa - width) / 16), (int) ((y) / 16 + 1)))
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
        } else
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
        if (x == (int) (this.x / 16) && y == (int) (this.y / 16)) return false;

        boolean blocking = map.isBlocking(x, y);

//        byte block = levelScene.level.getBlock(x, y);

        return blocking;
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

    public final boolean canKill()
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

    @Override
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
                if (!canKill() && marioSim.getYa() > 0 && yMarioD <= 0 && (!marioSim.wasOnGround() || !marioSim.isOnGround()))
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

    public void checkShellCollide(ShellSim shell)
    {
        if (deadTime != 0)
        {
            return;
        }

        float xD = shell.x - x;
        float yD = shell.y - y;

        if (xD > -16 && xD < 16)
        {
            if (yD > -height && yD < shell.height)
            {
                xa = shell.facing * 2;
                ya = -5;
                flyDeath = true;
                deadTime = 100;
                winged = false;
            }
        }
    }

    public boolean checkFireballCollide(FireBallSim fireBallSim)
    {
        if (deadTime != 0)
        {
            return false;
        }

        float xD = fireBallSim.x - x;
        float yD = fireBallSim.y - y;

        if (xD > -16 && xD < 16)
        {
            if (yD > -height && yD < fireBallSim.height)
            {
                if (noFireballDeath)
                {
                    return false;
                }

                xa = fireBallSim.facing * 2;
                ya = -5;
                flyDeath = true;
                deadTime = 100;
                winged = false;
                return true;
            }
        }
        return false;
    }

    public void setFacing(int _facing)
    {
        this.facing = _facing;
    }

    public void setX(float _x)
    {
        this.x = _x;
    }

    public void setYA(float _ya)
    {
        this.ya = _ya;
    }

    public void setY(float _y)
    {
        this.y = _y;
    }

    public float getAccurateY()
    {
        return accurateY;
    }

    public boolean isYAUnknown()
    {
        return yaUnknown;
    }

    public void setKnownYA(boolean known)
    {
        this.yaUnknown = known;
    }

    public void setAccurateY(float _accurateY)
    {
        this.accurateY = _accurateY;
    }

    public void setXA(float _xa)
    {
        this.xa = _xa;
    }

    public void setAccurateX(float _accurateX)
    {
        this.accurateX = _accurateX;
    }

    public float getAccurateX()
    {
        return accurateX;
    }
}
