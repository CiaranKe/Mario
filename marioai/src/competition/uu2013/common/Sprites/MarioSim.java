package competition.uu2013.common.Sprites;

import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.engine.sprites.Sprite;
import ch.idsia.benchmark.mario.environments.Environment;
import competition.uu2013.common.level.Map;
import competition.uu2013.common.level.WorldSim;

public class MarioSim  extends SpriteSim  implements Cloneable
{

    public float lastX,lastY;          // max speed = 4.85 walking, 9.7 running
    private boolean big = true;
    private boolean fire = true;
    private boolean ducking = false;
    private int width;
    private int invulnerableTime = 0;
    private int jumpTime;
    private int facing = 1;
    private boolean onGround = false;
    private boolean wasOnGround = false;
    private boolean mayJump = false;
    private boolean sliding = false;
    private boolean dead = false;
    private float xJumpSpeed = 0;
    private float yJumpSpeed = 0;
    private boolean keys_last[];
    private boolean keys[];
    private WorldSim worldSim;
    public EnemySim carried;
    private float oldX;
    private float oldY;
    private float oldYa;
    private boolean oldWasOnGround;
    private boolean oldIsOnGround;
    private boolean hurt = false;
    private Mario marioActual;
    private boolean ableToShoot = true;
    private Map map;



    public static final int KEY_LEFT = 0;
    public static final int KEY_RIGHT = 1;
    public static final int KEY_DOWN = 2;
    public static final int KEY_JUMP = 3;
    public static final int KEY_SPEED = 4;
    public static final int KEY_UP = 5;


    public MarioSim(float _x, float _y, float _xa, float _ya)
    {
        lastX = x = _x;
        lastY = y = _y;
        xa = _xa;
        ya = _ya;
        width = 4;
        keys_last = new boolean[Environment.numberOfKeys];
    }

    @Override
    public MarioSim clone() throws CloneNotSupportedException
    {
        MarioSim m =  new MarioSim(this.x, this.y,this.xa, this.ya);
        m.x = this.x;
        m.y = this.y;
        m.xa = this.xa;
        m.ya = this.ya;
        m.facing = this.facing;
        m.type = this.type;
        m.lastX = this.lastX;
        m.lastY = this.lastY;
        m.big = this.big;
        m.fire = this.fire;
        m.width = this.width;
        m.invulnerableTime = this.invulnerableTime;
        m.jumpTime = this.jumpTime;
        m.facing = this.facing;
        m.onGround = this.onGround;
        m.wasOnGround = this.wasOnGround;
        m.mayJump = this.mayJump;
        m.sliding = this.sliding;
        m.dead = this.dead;
        m.xJumpSpeed = this.xJumpSpeed;
        m.yJumpSpeed = this.yJumpSpeed;
        m.keys_last = this.keys_last;
        m.keys = this.keys;
        m.carried = this.carried;
        m.oldX = this.oldX;
        m.oldY = this.oldY;
        m.oldYa = this.oldYa;
        m.oldWasOnGround = this.oldWasOnGround;
        m.oldIsOnGround = this.oldWasOnGround;
        m.ableToShoot = this.ableToShoot;

        return m;
    }


    @Override
    public float getX()
    {
        return this.x;
    }

    @Override
    public float getY()
    {
        return this.y;
    }
    public void setWorldSim(WorldSim _sim)
    {
        this.worldSim = _sim;
    }

    public void setMapSim(Map _map)
    {
        this.map = _map;
    }


    public int height()
    {
        return big ? 24 : 12;
    }

    public void move()
    {
        keys_last = keys;
        float sideWaysSpeed = keys[KEY_SPEED]  ? 1.2f : 0.6f;
        this.oldX = x;
        this.oldY = y;
        this.oldYa = this.ya;
        this.oldWasOnGround = wasOnGround;
        this.oldIsOnGround = onGround;

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


        //if (keys[KEY_SPEED] && ableToShoot && this.fire && worldSim.numFireBalls() < 2)
        //{
        //worldSim.addFireball(new FireBallSim(x + facing * 6, y - 20, Sprite.KIND_FIREBALL , facing));
        //}

        //ableToShoot = (worldSim.numFireBalls() < 2);

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
    }


    private boolean move(float xa, float ya)
    {
        while (xa > 8) {
            if (!move(8, 0))
            {
                return false;
            }
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

        if (ya > 0)
        {
            if (isBlocking(x + xa - width, y + ya, xa, 0)) collide = true;
            else if (isBlocking(x + xa + width, y + ya, xa, 0)) collide = true;
            else if (isBlocking(x + xa - width, y + ya + 1, xa, ya)) collide = true;
            else if (isBlocking(x + xa + width, y + ya + 1, xa, ya)) collide = true;
        }
        if (ya < 0)
        {
            if (isBlocking(x + xa, y + ya - this.height(), xa, ya)) collide = true;
            else if (collide || isBlocking(x + xa - width, y + ya - height(), xa, ya)) collide = true;
            else if (collide || isBlocking(x + xa + width, y + ya - height(), xa, ya)) collide = true;
        }
        if (xa > 0)
        {
            sliding = true;
            if (isBlocking(x + xa + width, y + ya - height(), xa, ya)) collide = true;
            else sliding = false;
            if (isBlocking(x + xa + width, y + ya - height() / 2, xa, ya)) collide = true;
            else sliding = false;
            if (isBlocking(x + xa + width, y + ya, xa, ya)) collide = true;
            else sliding = false;
        }
        if (xa < 0)
        {
            sliding = true;
            if (isBlocking(x + xa - width, y + ya - height(), xa, ya)) collide = true;
            else sliding = false;
            if (isBlocking(x + xa - width, y + ya - height() / 2, xa, ya)) collide = true;
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
                y = (int) ((y - height()) / 16) * 16 + height();
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
        int x = (int) (_x / 16);
        int y = (int) (_y / 16);
        if (x == (int) (this.x / 16) && y == (int) (this.y / 16)) return false;

        boolean blocking = map.isBlocking(x, y, ya);

        byte block = map.getBlock(x, y);


        if (((map.TILE_BEHAVIORS[block & 0xff]) & map.BIT_PICKUPABLE) > 0)
        {
            Mario.gainCoin();
            map.setBlock(x, y, (byte) 0);
        }

        if (blocking && ya < 0)
        {
            worldSim.bump(x, y, big);
        }
        return blocking;
    }

    public void stomp(SpriteSim enemy)
    {
        //System.out.println(" PREDICTED STOMP!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        //System.out.println("T: " + enemy.type + " X: " + enemy.x + " Y: " + enemy.y + "Height: " + enemy.height());

        float targetY = enemy.y - enemy.height() / 2;
        move(0, targetY - y);

        xJumpSpeed = 0;
        yJumpSpeed = -1.9f;
        jumpTime = 8;
        ya = jumpTime * yJumpSpeed;
        onGround = false;
        sliding = false;
        invulnerableTime = 1;
        //System.out.println("XA: " + xa + " YA: " + ya );
    }

    public void stomp(boolean [] keys, final ShellSim shell)
    {
        if (keys[KEY_SPEED] && shell.facing == 0)
        {
            carried = shell;
            shell.carried = true;
        } else
        {
            float targetY = shell.y - shell.height / 2;
            move(0, targetY - y);

            xJumpSpeed = 0;
            yJumpSpeed = -1.9f;
            jumpTime = 8;
            ya = jumpTime * yJumpSpeed;
            onGround = false;
            sliding = false;
            invulnerableTime = 1;
        }
    }

    public void getHurt()
    {
        hurt = true;
        if (invulnerableTime > 0) return;

        if (big)
        {
            if (fire)
            {
                fire = false;
            }
            else
            {
                big = false;
            }
            invulnerableTime = 32;
        }
        else
        {
            dead = true;
        }
    }

    public boolean isDead()
    {
        return this.dead;
    }

    public void syncLocation(float _x, float _y, boolean _mayJump, boolean _onGround, boolean _wasOnGround, boolean _fire, int _big)
    {
        if ((_x != x)|| (_y !=y))
        {
            x = _x;
            y = _y;
            xa = (x - lastX) * GROUND_INERTIA;
            ya = (y - lastY) * AIR_INERTIA + 3.0F;
        }

        this.mayJump = _mayJump;
        wasOnGround = _wasOnGround;
        this.onGround = _onGround;
        this.big = (_big > 0);
        this.fire = (_big == 2);
        ableToShoot = _fire;

    }

    public float getYa()
    {
        return this.ya;
    }

    public boolean isOnGround()
    {
        return onGround;
    }

    public boolean wasOnGround()
    {
        return wasOnGround;
    }

    public void setMode(boolean _big, boolean _fire)
    {
        this.big = _big;
        this.fire = _fire;
    }

    public void setKeys(boolean[] _keys, float accurateX, float accurateY)
    {
        keys = _keys;
        lastX = accurateX;
        lastY = accurateY;
    }

    public float getOldX() {
        return oldX;
    }

    public float getOldY() {
        return oldY;
    }

    public float getOldYa() {
        return oldYa;
    }

    public boolean oldWasOnGround()
    {
        return this.oldWasOnGround;
    }

    public boolean oldIsOnGroundOld()
    {
        return this.oldIsOnGround;
    }

    public float getXA() {
        return xa;
    }

    public int getMarioMode()
    {
        if (this.fire)
        {
            return 2;
        }
        if (this.big)
        {
            return 1;
        }
        return 0;
    }

    public boolean isMarioAbletoJump() {
        return mayJump;
    }

    public int getJumpTime() {
        return jumpTime;
    }

    public boolean mayJump()
    {
        return mayJump;
    }

    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("X: " + this.getX() + ", Y: " + this.getY());
        stringBuilder.append(", Status: ");
        if (dead) stringBuilder.append("DEAD");
        else if (fire) stringBuilder.append("FIRE");
        else if (big)stringBuilder.append("BIG");
        else stringBuilder.append(" SMALL ");
        stringBuilder.append(", MayJump: " + mayJump());
        stringBuilder.append(", JumpTime: " + jumpTime);
        stringBuilder.append(", mayShoot: " + ableToShoot);
        stringBuilder.append(", onGround: " + onGround);
        stringBuilder.append(", wasOnGround: " + wasOnGround);
        return stringBuilder.toString();
    }

    public float getYA() {
        return this.ya;
    }

    public boolean isHurt() {
        return hurt;
    }
}
