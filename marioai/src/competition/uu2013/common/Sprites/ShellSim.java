package competition.uu2013.common.Sprites;

import competition.uu2013.common.Enemy;
import competition.uu2013.common.Map;

public class ShellSim extends EnemySim
{
    private float yaa;
    public boolean carried;

    public ShellSim(float _x, float _y, int _type)
    {
        super(_x, _y, _type);
        height = 12;
        facing = 0;
        deadTime = 0;

        ya = -5;

        yaa = 2.0F;
    }

    public ShellSim clone()
    {
        ShellSim n = new ShellSim(this.x, this.y, this.type);
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
        n.yaa = this.yaa;
        n.carried = this.carried;
        return n;
    }



    public boolean fireballCollideCheck(FireBallSim fireball)
    {
        if (deadTime != 0) return false;

        float xD = fireball.getX() - x;
        float yD = fireball.getY() - y;

        if (xD > -16 && xD < 16)
        {
            if (yD > -height && yD < fireball.height)
            {
                if (facing != 0)
                {
                    return true;
                }

                xa = fireball.getFacing() * 2;
                ya = -5;
                deadTime = 100;
                return true;
            }
        }
        return false;
    }

    public void collideCheck()
    {
        if (carried || dead || deadTime > 0)
        {
            return;
        }

        float xMarioD = marioSim.getX() - x;
        float yMarioD = marioSim.getY() - y;
        float w = 16;
        if (xMarioD > -w && xMarioD < w)
        {
            if (yMarioD > -height && yMarioD < marioSim.height())
            {
                if (marioSim.getYa() > 0 && yMarioD <= 0 && (!marioSim.isOnGround() || !marioSim.wasOnGround()))
                {
                    marioSim.stomp(this);
                    if (facing != 0)
                    {
                        xa = 0;
                        facing = 0;
                    } else
                    {
                        facing = marioSim.getFacing();
                    }
                } else
                {
                    if (facing != 0)
                    {
                        marioSim.getHurt();
                    } else
                    {
                        facing = marioSim.getFacing();
                    }
                }
            }
        }
    }

    public void move()
    {
        if (carried)
        {

            //TODO:
            //Enemy.checkShellCollide(this);
            return;
        }

        if (deadTime > 0)
        {
            deadTime--;

            if (deadTime == 0)
            {
                deadTime = 1;
            }

            x += xa;
            y += ya;
            ya *= 0.95;
            ya += 1;

            return;
        }

        float sideWaysSpeed = 11f;
        //        float sideWaysSpeed = onGround ? 2.5f : 1.2f;

        if (xa > 2)
        {
            facing = 1;
        }
        if (xa < -2)
        {
            facing = -1;
        }

        xa = facing * sideWaysSpeed;

        if (facing != 0)
        {
            //TODO:
            //Enemy.checkShellCollide(this);
        }

        if (!move(xa, 0))
        {
            facing = -facing;
        }
        onGround = false;
        move(0, ya);

        ya *= 0.85f;
        if (onGround)
        {
            xa *= GROUND_INERTIA;
        } else
        {
            xa *= AIR_INERTIA;
        }

        if (!onGround)
        {
            ya += yaa;
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

        boolean blocking = Map.isBlocking(x, y, xa, ya);

//        byte block = levelScene.level.getBlock(x, y);

        moveSim.bump(x, y, true);

        return blocking;
    }

    public void bumpCheck(int xTile, int yTile)
    {
        if (x + width > xTile * 16 && x - width < xTile * 16 + 16 && yTile == (int) ((y - 1) / 16))
        {
            facing = -marioSim.getFacing();
            ya = -10;
        }
    }

    public void die()
    {
        dead = true;

        carried = false;

        xa = -facing * 2;
        ya = -5;
        deadTime = 100;
    }

    public boolean shellCollideCheck(ShellSim shell)
    {
        if (deadTime != 0) return false;

        float xD = shell.getX() - x;
        float yD = shell.getY() - y;

        if (xD > -16 && xD < 16)
        {
            if (yD > -height && yD < shell.height)
            {
                die();
                shell.die();
                return true;
            }
        }
        return false;
    }


    public void release()
    {
        carried = false;
        facing = marioSim.getFacing();
        x += facing * 8;
    }
}
