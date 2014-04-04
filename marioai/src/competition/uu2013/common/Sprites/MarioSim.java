/*
 * 
 */
package competition.uu2013.common.Sprites;

import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.engine.sprites.Sprite;
import ch.idsia.benchmark.mario.environments.Environment;
import competition.uu2013.common.level.Map;
import competition.uu2013.common.level.WorldSim;

// TODO: Auto-generated Javadoc
/**
 * The Class MarioSim.
 */
public class MarioSim  extends SpriteSim  implements Cloneable
{

    /** The last y. */
    public float lastX,lastY;          // max speed = 4.85 walking, 9.7 running
    
    /** The big. */
    private boolean big = true;
    
    /** The fire. */
    private boolean fire = true;
    
    /** The ducking. */
    private boolean ducking = false;
    
    /** The width. */
    private int width;
    
    /** The invulnerable time. */
    private int invulnerableTime = 0;
    
    /** The jump time. */
    private int jumpTime;
    
    /** The facing. */
    private int facing = 1;
    
    /** The on ground. */
    private boolean onGround = false;
    
    /** The was on ground. */
    private boolean wasOnGround = false;
    
    /** The may jump. */
    private boolean mayJump = false;
    
    /** The sliding. */
    private boolean sliding = false;
    
    /** The dead. */
    private boolean dead = false;
    
    /** The x jump speed. */
    private float xJumpSpeed = 0;
    
    /** The y jump speed. */
    private float yJumpSpeed = 0;
    
    /** The keys_last. */
    private boolean keys_last[];
    
    /** The keys. */
    private boolean keys[];
    
    /** The world sim. */
    private WorldSim worldSim;
    
    /** The carried. */
    public EnemySim carried;
    
    /** The old x. */
    private float oldX;
    
    /** The old y. */
    private float oldY;
    
    /** The old ya. */
    private float oldYa;
    
    /** The old was on ground. */
    private boolean oldWasOnGround;
    
    /** The old is on ground. */
    private boolean oldIsOnGround;
    
    /** The hurt. */
    private boolean hurt = false;
    
    /** The mario actual. */
    private Mario marioActual;
    
    /** The able to shoot. */
    private boolean ableToShoot = true;
    
    /** The map. */
    private Map map;



    /** The Constant KEY_LEFT. */
    public static final int KEY_LEFT = 0;
    
    /** The Constant KEY_RIGHT. */
    public static final int KEY_RIGHT = 1;
    
    /** The Constant KEY_DOWN. */
    public static final int KEY_DOWN = 2;
    
    /** The Constant KEY_JUMP. */
    public static final int KEY_JUMP = 3;
    
    /** The Constant KEY_SPEED. */
    public static final int KEY_SPEED = 4;
    
    /** The Constant KEY_UP. */
    public static final int KEY_UP = 5;


    /**
     * Instantiates a new mario sim.
     *
     * @param _x the _x
     * @param _y the _y
     * @param _xa the _xa
     * @param _ya the _ya
     */
    public MarioSim(float _x, float _y, float _xa, float _ya)
    {
        lastX = xLocation = _x;
        lastY = yLocation = _y;
        xAcceleration = _xa;
        yAcceleration = _ya;
        width = 4;
        keys_last = new boolean[Environment.numberOfKeys];
    }

    /* (non-Javadoc)
     * @see competition.uu2013.common.Sprites.SpriteSim#clone()
     */
    @Override
    public MarioSim clone() throws CloneNotSupportedException
    {
        MarioSim m =  new MarioSim(this.xLocation, this.yLocation,this.xAcceleration, this.yAcceleration);
        m.xLocation = this.xLocation;
        m.yLocation = this.yLocation;
        m.xAcceleration = this.xAcceleration;
        m.yAcceleration = this.yAcceleration;
        m.facing = this.facing;
        m.simType = this.simType;
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


    /* (non-Javadoc)
     * @see competition.uu2013.common.Sprites.SpriteSim#getXLocation()
     */
    @Override
    public float getXLocation()
    {
        return this.xLocation;
    }

    /* (non-Javadoc)
     * @see competition.uu2013.common.Sprites.SpriteSim#getYLocation()
     */
    @Override
    public float getYLocation()
    {
        return this.yLocation;
    }
    
    /**
     * Sets the world sim.
     *
     * @param _sim the new world sim
     */
    public void setWorldSim(WorldSim _sim)
    {
        this.worldSim = _sim;
    }

    /**
     * Sets the map sim.
     *
     * @param _map the new map sim
     */
    public void setMapSim(Map _map)
    {
        this.map = _map;
    }


    /* (non-Javadoc)
     * @see competition.uu2013.common.Sprites.SpriteSim#getHeight()
     */
    public int getHeight()
    {
        return big ? 24 : 12;
    }

    /* (non-Javadoc)
     * @see competition.uu2013.common.Sprites.SpriteSim#move()
     */
    public void move()
    {
        keys_last = keys;
        float sideWaysSpeed = keys[KEY_SPEED]  ? 1.2f : 0.6f;
        this.oldX = xLocation;
        this.oldY = yLocation;
        this.oldYa = this.yAcceleration;
        this.oldWasOnGround = wasOnGround;
        this.oldIsOnGround = onGround;

        if (xAcceleration > 2)
        {
            facing = 1;
        }
        if (xAcceleration < -2)
        {
            facing = -1;
        }


        if (keys[KEY_JUMP] || (jumpTime < 0 && !onGround && !sliding))
        {
            if (jumpTime < 0)
            {
                xAcceleration = xJumpSpeed;
                yAcceleration = -jumpTime * yJumpSpeed;
                jumpTime++;
            } else if (onGround && mayJump)
            {
                xJumpSpeed = 0;
                yJumpSpeed = -1.9f;
                jumpTime = 7;
                yAcceleration = jumpTime * yJumpSpeed;
                onGround = false;
                sliding = false;
            } else if (sliding && mayJump)
            {
                xJumpSpeed = -facing * 6.0f;
                yJumpSpeed = -2.0f;
                jumpTime = -6;
                xAcceleration = xJumpSpeed;
                yAcceleration = -jumpTime * yJumpSpeed;
                onGround = false;
                sliding = false;
                facing = -facing;
            } else if (jumpTime > 0)
            {
                xAcceleration += xJumpSpeed;
                yAcceleration = jumpTime * yJumpSpeed;
                jumpTime--;
            }
        } else
        {
            jumpTime = 0;
        }

        if (keys[KEY_LEFT] && !ducking)
        {
            if (facing == 1) sliding = false;
            xAcceleration -= sideWaysSpeed;
            if (jumpTime >= 0) facing = -1;
        }

        if (keys[KEY_RIGHT] && !ducking)
        {
            if (facing == -1) sliding = false;
            xAcceleration += sideWaysSpeed;
            if (jumpTime >= 0) facing = 1;
        }

        if ((!keys[KEY_LEFT] && !keys[KEY_RIGHT]) || ducking || yAcceleration < 0 || onGround)
        {
            sliding = false;
        }


        //if (keys[KEY_SPEED] && ableToShoot && this.fire && worldSim.numFireBalls() < 2)
        //{
        //worldSim.addFireball(new FireBallSim(x + facing * 6, y - 20, Sprite.KIND_FIREBALL , facing));
        //}

        //ableToShoot = (worldSim.numFireBalls() < 2);

        mayJump = (onGround || sliding) && !keys[KEY_JUMP];


        if (Math.abs(xAcceleration) < 0.5f)
        {
            xAcceleration = 0;
        }

        if (sliding)
        {
            yAcceleration *= 0.5f;
        }

        onGround = false;
        move(xAcceleration, 0);
        move(0, yAcceleration);

        if (xLocation < 0)
        {
            xLocation = 0;
            xAcceleration = 0;
        }

        yAcceleration *= AIR_INERTIA;
        xAcceleration *= GROUND_INERTIA;

        if (!onGround)
        {
            yAcceleration += 3;
        }
    }


    /**
     * Move.
     *
     * @param xa the xa
     * @param ya the ya
     * @return true, if successful
     */
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
            if (isBlocking(xLocation + xa - width, yLocation + ya, xa, 0)) collide = true;
            else if (isBlocking(xLocation + xa + width, yLocation + ya, xa, 0)) collide = true;
            else if (isBlocking(xLocation + xa - width, yLocation + ya + 1, xa, ya)) collide = true;
            else if (isBlocking(xLocation + xa + width, yLocation + ya + 1, xa, ya)) collide = true;
        }
        if (ya < 0)
        {
            if (isBlocking(xLocation + xa, yLocation + ya - this.getHeight(), xa, ya)) collide = true;
            else if (collide || isBlocking(xLocation + xa - width, yLocation + ya - getHeight(), xa, ya)) collide = true;
            else if (collide || isBlocking(xLocation + xa + width, yLocation + ya - getHeight(), xa, ya)) collide = true;
        }
        if (xa > 0)
        {
            sliding = true;
            if (isBlocking(xLocation + xa + width, yLocation + ya - getHeight(), xa, ya)) collide = true;
            else sliding = false;
            if (isBlocking(xLocation + xa + width, yLocation + ya - getHeight() / 2, xa, ya)) collide = true;
            else sliding = false;
            if (isBlocking(xLocation + xa + width, yLocation + ya, xa, ya)) collide = true;
            else sliding = false;
        }
        if (xa < 0)
        {
            sliding = true;
            if (isBlocking(xLocation + xa - width, yLocation + ya - getHeight(), xa, ya)) collide = true;
            else sliding = false;
            if (isBlocking(xLocation + xa - width, yLocation + ya - getHeight() / 2, xa, ya)) collide = true;
            else sliding = false;
            if (isBlocking(xLocation + xa - width, yLocation + ya, xa, ya)) collide = true;
            else sliding = false;
        }

        if (collide)
        {
            if (xa < 0) {
                xLocation = (int) ((xLocation - width) / 16) * 16 + width;
                this.xAcceleration = 0;
            } else if (xa > 0) {
                xLocation = (int) ((xLocation + width) / 16 + 1) * 16 - width - 1;
                this.xAcceleration = 0;
            }

            if (ya < 0) {
                yLocation = (int) ((yLocation - getHeight()) / 16) * 16 + getHeight();
                jumpTime = 0;
                this.yAcceleration = 0;
            } else if (ya > 0) {
                yLocation = (int) ((yLocation - 1) / 16 + 1) * 16 - 1;
                onGround = true;
            }
            return false;
        }
        else
        {
            xLocation += xa;
            yLocation += ya;
            return true;
        }
    }

    /**
     * Checks if is blocking.
     *
     * @param _x the _x
     * @param _y the _y
     * @param xa the xa
     * @param ya the ya
     * @return true, if is blocking
     */
    private boolean isBlocking(float _x, float _y, float xa, float ya)
    {
        int x = (int) (_x / 16);
        int y = (int) (_y / 16);
        if (x == (int) (this.xLocation / 16) && y == (int) (this.yLocation / 16)) return false;

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

    /**
     * Stomp.
     *
     * @param enemy the enemy
     */
    public void stomp(SpriteSim enemy)
    {
        //System.out.println(" PREDICTED STOMP!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        //System.out.println("T: " + enemy.type + " X: " + enemy.x + " Y: " + enemy.y + "Height: " + enemy.height());

        float targetY = enemy.yLocation - enemy.getHeight() / 2;
        move(0, targetY - yLocation);

        xJumpSpeed = 0;
        yJumpSpeed = -1.9f;
        jumpTime = 8;
        yAcceleration = jumpTime * yJumpSpeed;
        onGround = false;
        sliding = false;
        invulnerableTime = 1;
        //System.out.println("XA: " + xa + " YA: " + ya );
    }

    /**
     * Stomp.
     *
     * @param keys the keys
     * @param shell the shell
     */
    public void stomp(boolean [] keys, final ShellSim shell)
    {
        if (keys[KEY_SPEED] && shell.facing == 0)
        {
            carried = shell;
            shell.carried = true;
        } else
        {
            float targetY = shell.yLocation - shell.height / 2;
            move(0, targetY - yLocation);

            xJumpSpeed = 0;
            yJumpSpeed = -1.9f;
            jumpTime = 8;
            yAcceleration = jumpTime * yJumpSpeed;
            onGround = false;
            sliding = false;
            invulnerableTime = 1;
        }
    }

    /**
     * Gets the hurt.
     *
     * @return the hurt
     */
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

    /**
     * Checks if is dead.
     *
     * @return true, if is dead
     */
    public boolean isDead()
    {
        return this.dead;
    }

    /**
     * Syncs the location of MArio to that set from the game engine.
     *
     * @param _x the X location
     * @param _y the X location
     * @param _mayJump mayJump
     * @param _onGround onGround
     * @param _wasOnGround the wasOnGround
     * @param _fire the fire
     * @param _big the big
     */
    public void syncLocation(float _x, float _y, boolean _mayJump, boolean _onGround, boolean _wasOnGround, boolean _fire, int _big)
    {
        if ((_x != xLocation)|| (_y !=yLocation))
        {
            xLocation = _x;
            yLocation = _y;
            xAcceleration = (xLocation - lastX) * GROUND_INERTIA;
            yAcceleration = (yLocation - lastY) * AIR_INERTIA + 3.0F;
        }

        this.mayJump = _mayJump;
        wasOnGround = _wasOnGround;
        this.onGround = _onGround;
        this.big = (_big > 0);
        this.fire = (_big == 2);
        ableToShoot = _fire;

    }

    /**
     * Gets the ya.
     *
     * @return the ya
     */
    public float getYa()
    {
        return this.yAcceleration;
    }

    /**
     * Checks if is on ground.
     *
     * @return true, if is on ground
     */
    public boolean isOnGround()
    {
        return onGround;
    }

    /**
     * Was on ground.
     *
     * @return true, if successful
     */
    public boolean wasOnGround()
    {
        return wasOnGround;
    }

    /**
     * Sets the mode.
     *
     * @param _big the _big
     * @param _fire the _fire
     */
    public void setMode(boolean _big, boolean _fire)
    {
        this.big = _big;
        this.fire = _fire;
    }

    /**
     * Sets the keys.
     *
     * @param _keys the _keys
     * @param accurateX the accurate x
     * @param accurateY the accurate y
     */
    public void setKeys(boolean[] _keys, float accurateX, float accurateY)
    {
        keys = _keys;
        lastX = accurateX;
        lastY = accurateY;
    }

    /**
     * Gets the old x.
     *
     * @return the old x
     */
    public float getOldX() {
        return oldX;
    }

    /**
     * Gets the old y.
     *
     * @return the old y
     */
    public float getOldY() {
        return oldY;
    }

    /**
     * Gets the old ya.
     *
     * @return the old ya
     */
    public float getOldYa() {
        return oldYa;
    }

    /**
     * Old was on ground.
     *
     * @return true, if successful
     */
    public boolean oldWasOnGround()
    {
        return this.oldWasOnGround;
    }

    /**
     * Old is on ground old.
     *
     * @return true, if successful
     */
    public boolean oldIsOnGroundOld()
    {
        return this.oldIsOnGround;
    }

    /**
     * Gets the xa.
     *
     * @return the xa
     */
    public float getXA() {
        return xAcceleration;
    }

    /**
     * Gets the mario mode.
     *
     * @return the mario mode
     */
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

    /**
     * Checks if is mario ableto jump.
     *
     * @return true, if is mario ableto jump
     */
    public boolean isMarioAbletoJump() {
        return mayJump;
    }

    /**
     * Gets the jump time.
     *
     * @return the jump time
     */
    public int getJumpTime() {
        return jumpTime;
    }

    /**
     * May jump.
     *
     * @return true, if successful
     */
    public boolean mayJump()
    {
        return mayJump;
    }

    /** 
     * Provides a string representation of the sprite, used for debugging purposes
     * @see competition.uu2013.common.Sprites.SpriteSim#toString()
     */
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("X: " + this.getXLocation() + ", Y: " + this.getYLocation());
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

    /**
     * Gets the ya.
     *
     * @return the ya
     */
    public float getYA() {
        return this.yAcceleration;
    }

    /**
     * Checks if is hurt.
     *
     * @return true, if is hurt
     */
    public boolean isHurt() {
        return hurt;
    }
}
