package competition.uu2013.common;

import competition.uu2013.common.Map;

public class MarioSim  extends SpriteSim
{

    public float x,y,xa,ya,lastX,lastY;          // max speed = 4.85 walking, 9.7 running
    private boolean big = true;
    private boolean fire = true;
    private int invulnerableTime = 0;
    private int jumpTime;
    private int facing = 1;
    private boolean onGround = false;
    private boolean mayJump = false;
    private boolean sliding = false;
    private boolean dead = false;
    private float xJumpSpeed = 0;
    private float yJumpSpeed = 0;


    private SpriteSim[] enemies;

    public static final int KEY_LEFT = 0;
    public static final int KEY_RIGHT = 1;
    public static final int KEY_DOWN = 2;
    public static final int KEY_JUMP = 3;
    public static final int KEY_SPEED = 4;
    public static final int KEY_UP = 5;

    public static int ACT_SPEED = 1;
    public static int ACT_RIGHT = 2;
    public static int ACT_LEFT = 4;
    public static int ACT_JUMP = 8;
    public static int[] jumpstep_table = {0,1,2,4,7};



    public MarioSim(float _x, float _y, float _xa, float _ya)
    {
        x = _x;
        y = _y;
        xa = _xa;
        ya = _ya;
    }

    public float getLastX()
    {
        return this.lastX;
    }

    public float getLastY()
    {
        return this.lastY;
    }

    public int height()
    {
        return big ? 24 : 12;
    }

    public void move(boolean [] keys, float accurateX, float accurateY)
    {
        lastX = accurateX;
        lastY = accurateY;
        boolean ducking = false;
        float sideWaysSpeed = keys[KEY_SPEED]  ? 1.2f : 0.6f;

        if (xa > 2)
        {
            facing = 1;
        }
        if (xa < -2)
        {
            facing = -1;
        }


        if (keys[KEY_JUMP] || (jumpTime < 0 && !onGround && !sliding))
        {
            if (jumpTime < 0)
            {
                xa = xJumpSpeed;
                ya = -jumpTime * yJumpSpeed;
                jumpTime++;
            } else if (onGround && mayJump)
            {
                xJumpSpeed = 0;
                yJumpSpeed = -1.9f;
                jumpTime = 7;
                ya = jumpTime * yJumpSpeed;
                onGround = false;
                sliding = false;
            } else if (sliding && mayJump)
            {
                xJumpSpeed = -facing * 6.0f;
                yJumpSpeed = -2.0f;
                jumpTime = -6;
                xa = xJumpSpeed;
                ya = -jumpTime * yJumpSpeed;
                onGround = false;
                sliding = false;
                facing = -facing;
            } else if (jumpTime > 0)
            {
                xa += xJumpSpeed;
                ya = jumpTime * yJumpSpeed;
                jumpTime--;
            }
        } else
        {
            jumpTime = 0;
        }

        if (keys[KEY_LEFT] && !ducking)
        {
            if (facing == 1) sliding = false;
            xa -= sideWaysSpeed;
            if (jumpTime >= 0) facing = -1;
        }

        if (keys[KEY_RIGHT] && !ducking)
        {
            if (facing == -1) sliding = false;
            xa += sideWaysSpeed;
            if (jumpTime >= 0) facing = 1;
        }

        if ((!keys[KEY_LEFT] && !keys[KEY_RIGHT]) || ducking || ya < 0 || onGround)
        {
            sliding = false;
        }



        mayJump = (onGround || sliding) && !keys[KEY_JUMP];


        if (Math.abs(xa) < 0.5f)
        {
            xa = 0;
        }

        if (sliding)
        {
            ya *= 0.5f;
        }

        onGround = false;
        move(xa, 0);
        move(0, ya);

        if (x < 0)
        {
            x = 0;
            xa = 0;
        }

        ya *= AIR_INERTIA;
        xa *= GROUND_INERTIA;

        if (!onGround)
        {
            ya += 3;
        }



        //System.out.println("move: (xa,ya)5 = " + xa + "," + ya);
    }

    private boolean move(float xa, float ya)
    {
        while (xa > 8) {
            if (!move(8, 0)) return false;
            xa -= 8;
        }
        while (xa < -8) {
            if (!move(-8, 0)) return false;
            xa += 8;
        }
        while (ya > 8) {
            if (!move(0, 8)) return false;
            ya -= 8;
        }
        while (ya < -8) {
            if (!move(0, -8)) return false;
            ya += 8;
        }

        boolean collide = false;
        int width = 4;
        int height = big ? 24 : 12;
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
            sliding = true;
            if (isBlocking(x + xa + width, y + ya - height, xa, ya)) collide = true;
            else sliding = false;
            if (isBlocking(x + xa + width, y + ya - height / 2, xa, ya)) collide = true;
            else sliding = false;
            if (isBlocking(x + xa + width, y + ya, xa, ya)) collide = true;
            else sliding = false;
        }
        if (xa < 0)
        {
            sliding = true;
            if (isBlocking(x + xa - width, y + ya - height, xa, ya)) collide = true;
            else sliding = false;
            if (isBlocking(x + xa - width, y + ya - height / 2, xa, ya)) collide = true;
            else sliding = false;
            if (isBlocking(x + xa - width, y + ya, xa, ya)) collide = true;
            else sliding = false;
        }

        if (collide)
        {
            if (xa < 0) {
                x = (int) ((x - width) / 16) * 16 + width;
                this.xa = 0;
            } else if (xa > 0) {
                x = (int) ((x + width) / 16 + 1) * 16 - width - 1;
                this.xa = 0;
            }

            if (ya < 0) {
                y = (int) ((y - height) / 16) * 16 + height;
                jumpTime = 0;
                this.ya = 0;
            } else if (ya > 0) {
                y = (int) ((y - 1) / 16 + 1) * 16 - 1;
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
        int x = (int) (_x / 16); // block's quantized pos
        int y = (int) (_y / 16);

        int Mx = (int) (this.x / 16); // mario's quantized pos
        int My = (int) (this.y / 16);
        if (x == Mx && y == My) return false;

        boolean blocking = Map.isBlocking(x,y,xa,ya);

        byte block = Map.getBlock(x,y);

        if(block == 34) { // coin


            Map.setBlock(x,y,(byte)0);
            return false;
        }

        //if (blocking && ya < 0)
            //ws = ws.bump(x, y, big);

        return blocking;
    }

    public void stomp(SpriteSim enemy)
    {
        float targetY = enemy.y - enemy.height() / 2;
        move(0, targetY - y);

        xJumpSpeed = 0;
        yJumpSpeed = -1.9f;
        jumpTime = 8;
        ya = jumpTime * yJumpSpeed;
        onGround = false;
        sliding = false;
        invulnerableTime = 1;
    }

    public void getHurt()
    {
        if (invulnerableTime > 0) return;

        if (big) {
            if (fire) {
                fire = false;
            } else {
                big = false;
            }
            invulnerableTime = 32;
        } else {
            dead = true;
        }
        dead = true;
    }

    public void syncLocation(float _x, float _y, boolean _mayJump, boolean _onGround, boolean _fire, boolean _big)
    {
        if ((_x != x)|| (_y !=y))
        {
            x = _x;
            y = _y;
            xa = (x - lastX) * GROUND_INERTIA;
            ya = (y - lastY) * AIR_INERTIA;
        }

        this.mayJump = _mayJump;
        this.onGround = _onGround;
        this.big = _big;
        this.fire = _fire;

    }
}
