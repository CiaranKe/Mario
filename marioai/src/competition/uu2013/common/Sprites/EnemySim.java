/*
 * 
 */
package competition.uu2013.common.Sprites;

import ch.idsia.benchmark.mario.engine.sprites.Sprite;
import competition.uu2013.common.level.Enemy;
import competition.uu2013.common.level.Map;
import competition.uu2013.common.level.WorldSim;

// TODO: Auto-generated Javadoc
/**
 * Created with IntelliJ IDEA.
 * User: fluffy
 * Date: 24/01/14
 * Time: 10:56
 * To change this template use File | Settings | File Templates.
 */
public class EnemySim extends SpriteSim implements Comparable
{

    /** The Constant SIDE_WAY_SPEED. */
    public static final float SIDE_WAY_SPEED = 1.75f;
    
    /** The last y. */
    protected float lastX, lastY;
    
    /** The height. */
    protected int height;
    
    /** The width. */
    protected int width;
    
    /** The avoid cliffs. */
    protected boolean avoidCliffs; //red koopa
    
    /** The winged. */
    protected boolean winged;
    
    /** The on ground. */
    protected boolean onGround;
    
    /** The seen. */
    protected boolean seen;
    
    /** The fly death. */
    protected boolean flyDeath; //bumpCheck, fireballCollide, shellCollide
    
    /** The dead time. */
    protected int deadTime;
    
    /** The no fireball death. */
    protected boolean noFireballDeath;
    
    /** The dead. */
    protected boolean dead;
    
    /** The mario sim. */
    protected MarioSim marioSim;
    
    /** The world sim. */
    protected WorldSim worldSim;
    
    /** The accurate y. */
    protected float accurateY;
    
    /** The accurate x. */
    protected float accurateX;
    
    /** The ya unknown. */
    protected boolean yaUnknown;
    
    /** The first move. */
    protected boolean firstMove;
    
    /** The old x. */
    protected float oldX;
    
    /** The old y. */
    protected float oldY;
    
    /** The map. */
    protected Map map;
    
    /** The already in scope. */
    protected boolean alreadyInScope;

    /**
     * Instantiates a new enemy sim.
     *
     * @param _x the _x
     * @param _y the _y
     * @param _type the _type
     */
    public EnemySim(float _x, float _y, int _type )
    {
        this.xLocation = _x;
        this.yLocation = _y;
        this.firstMove = true;
        this.accurateY = 0;
        this.accurateX = 0;
        this.yaUnknown = true;
        this.simType = _type;
        this.deadTime = 0;
        this.dead = false;
        this.flyDeath = false;
        this.avoidCliffs = false;
        this.width = 4;
        this.height = 12;
        this.facing = -1;
        this.seen = false;
        this.noFireballDeath = false;
        this.accurateY = 0;
        this.alreadyInScope = false;

        switch (this.simType)
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

    /* (non-Javadoc)
     * @see competition.uu2013.common.Sprites.SpriteSim#clone()
     */
    @Override
    public EnemySim clone()  throws CloneNotSupportedException
    {
        EnemySim n = new EnemySim(this.xLocation, this.yLocation, this.simType);
        n.xLocation = this.xLocation;
        n.yLocation = this.yLocation;
        n.xAcceleration = this.xAcceleration;
        n.yAcceleration = this.yAcceleration;
        n.facing = this.facing;
        n.simType = this.simType;
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
        n.alreadyInScope = this.alreadyInScope;
        return n;
    }

    /**
     * Checks if is winged.
     *
     * @return true, if is winged
     */
    public boolean isWinged()
    {
        return this.winged;
    }

    /**
     * Sets the world sim.
     *
     * @param _worldSim the new world sim
     */
    public void setWorldSim(WorldSim _worldSim)
    {
        this.worldSim = _worldSim;
    }

    /**
     * Sets the mario sim.
     *
     * @param _marioSim the new mario sim
     */
    public void setMarioSim(MarioSim _marioSim)
    {
        this.marioSim = _marioSim;
    }

    /**
     * Sets the map sim.
     *
     * @param _map the new map sim
     */
    public void setMapSim (Map _map)
    {
        this.map = _map;
    }

    /* (non-Javadoc)
     * @see competition.uu2013.common.Sprites.SpriteSim#getHeight()
     */
    public int getHeight ()
    {
        return this.height;
    }

    /* (non-Javadoc)
     * @see competition.uu2013.common.Sprites.SpriteSim#setXYLocation(float, float)
     */
    @Override
    public void setXYLocation(float _x, float _y)
    {
        this.lastX = this.xLocation;
        this.lastY = this.yLocation;
        this.xLocation = _x;
        this.yLocation = _y;
        this.xAcceleration = this.xLocation - this.lastX;
        this.yAcceleration = (this.yLocation - this.lastY);

        if (this.xAcceleration == 0)
        {
            deadTime = 9;
        }
    }

    /**
     * Gets the xa.
     *
     * @return the xa
     */
    public float getXA()
    {
        return this.xAcceleration;
    }

    /**
     * Gets the ya.
     *
     * @return the ya
     */
    public float getYA()
    {
        return this.yAcceleration;
    }

    /**
     * Sets the xy.
     *
     * @param _x the _x
     * @param _y the _y
     * @param _xa the _xa
     * @param _ya the _ya
     */
    public void setXY(float _x, float _y, float _xa, float _ya)
    {
        this.lastX = this.xLocation;
        this.lastY = this.yLocation;
        this.xLocation = _x;
        this.yLocation = _y;
        this.xAcceleration = _xa;
        this.yAcceleration = _ya;

        if (this.xAcceleration == 0)
        {
            deadTime = 9;
        }
    }

    /**
     * 
     * Enemies move twice before being shown to the agent, this method simulates the XA and YA
     * VAlues the sprites have as they are shown.
     *   
     */
    public void drop()
    {
        if (this.winged && !this.seen)
        {
            this.seen = true;
            this.yAcceleration = -10.F;
        }
        else if (!this.winged && !this.seen)
        {
            this.seen = true;
            this.xAcceleration = 2;
            this.yAcceleration = 1.7F;
            this.onGround = true;

        }
    }

    /* (non-Javadoc)
     * @see competition.uu2013.common.Sprites.SpriteSim#move()
     */
    public void move()
    {
        this.oldX = xLocation;
        this.oldY = yLocation;

        if (xAcceleration == -1.5575F && yAcceleration == 1.7F)
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
                xLocation += xAcceleration;
                yLocation += yAcceleration;
                yAcceleration *= 0.95;
                yAcceleration += 1;
            }
            return;
        }

        float sideWaysSpeed = 1.75f;
        //        float sideWaysSpeed = onGround ? 2.5f : 1.2f;

        if (xAcceleration > 2)
            facing = 1;
        else if (xAcceleration < -2)
            facing = -1;

        xAcceleration = facing * sideWaysSpeed;
//    xa += facing == 1 ? -wind : wind;
//        mayJump = (onGround);



        if (!move(xAcceleration, 0)) facing = -facing;
        onGround = false;
        move(0, yAcceleration);



        yAcceleration *= winged ? 0.95f : 0.85f;
        if (onGround)
        {
            xAcceleration *= (GROUND_INERTIA + windScale(windCoeff, facing) + iceScale(iceCoeff));
        } else
        {
            xAcceleration *= (AIR_INERTIA + windScale(windCoeff, facing) + iceScale(iceCoeff));
        }

        if (!onGround)
        {
            if (winged)
            {
                yAcceleration += 0.6f * 1.0F;
            } else
            {
                yAcceleration += 2.0F;
            }
        } else if (winged)
        {
            yAcceleration = -10;
        }
        //System.out.println("Sim\t: X:\t"+x + " XA:\t" +xa + " Y:\t" +y + " YA:\t" + ya + " OnGround: " + onGround);
    }

    /**
     * Move.
     *
     * @param xa the xa
     * @param ya the ya
     * @return true, if successful
     */
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
            if (isBlocking(xLocation + xa - width, yLocation + ya, xa, 0)) collide = true;
            else if (isBlocking(xLocation + xa + width, yLocation + ya, xa, 0)) collide = true;
            else if (isBlocking(xLocation + xa - width, yLocation + ya + 1, xa, ya)) collide = true;
            else if (isBlocking(xLocation + xa + width, yLocation + ya + 1, xa, ya)) collide = true;
        }
        if (ya < 0)
        {
            if (isBlocking(xLocation + xa, yLocation + ya - height, xa, ya)) collide = true;
            else if (collide || isBlocking(xLocation + xa - width, yLocation + ya - height, xa, ya)) collide = true;
            else if (collide || isBlocking(xLocation + xa + width, yLocation + ya - height, xa, ya)) collide = true;
        }
        if (xa > 0)
        {
            if (isBlocking(xLocation + xa + width, yLocation + ya - height, xa, ya)) collide = true;
            if (isBlocking(xLocation + xa + width, yLocation + ya - height / 2, xa, ya)) collide = true;
            if (isBlocking(xLocation + xa + width, yLocation + ya, xa, ya)) collide = true;

            if (avoidCliffs && onGround && !map.isBlocking((int) ((xLocation + xa + width) / 16), (int) ((yLocation) / 16 + 1),ya))
                collide = true;
        }
        if (xa < 0)
        {
            if (isBlocking(xLocation + xa - width, yLocation + ya - height, xa, ya)) collide = true;
            if (isBlocking(xLocation + xa - width, yLocation + ya - height / 2, xa, ya)) collide = true;
            if (isBlocking(xLocation + xa - width, yLocation + ya, xa, ya)) collide = true;

            if (avoidCliffs && onGround && !map.isBlocking((int) ((xLocation + xa - width) / 16), (int) ((yLocation) / 16 + 1),ya))
                collide = true;
        }

        if (collide)
        {
            if (xa < 0)
            {
                xLocation = (int) ((xLocation - width) / 16) * 16 + width;
                this.xAcceleration = 0;
            }
            if (xa > 0)
            {
                xLocation = (int) ((xLocation + width) / 16 + 1) * 16 - width - 1;
                this.xAcceleration = 0;
            }
            if (ya < 0)
            {
                yLocation = (int) ((yLocation - height) / 16) * 16 + height;
//                jumpTime = 0;
                this.yAcceleration = 0;
            }
            if (ya > 0)
            {
                yLocation = (int) (yLocation / 16 + 1) * 16 - 1;
                onGround = true;
            }
            return false;
        } else
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

//        byte block = levelScene.level.getBlock(x, y);

        return blocking;
    }


    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o)
    {
        EnemySim oldSim = (EnemySim) o;

        if (this.simType != oldSim.getType() )
        {
            return -1;
        }

        boolean xMatch = false;
        boolean yMatch = false;

        //allow a 2 pixel diff for movement.
        float maxDelta = 2.01f * SIDE_WAY_SPEED;

        if (Math.abs(oldSim.getXLocation() - this.xLocation) < maxDelta)
        {
            xMatch = true;
        }

        if (Math.abs(oldSim.getYLocation() - this.yLocation) < (maxDelta + 10.0F))
        {
            yMatch = true;
        }

        if (xMatch && yMatch)
        {
            return 0;
        }
        return -1;
    }

    /**
     * Bump check.
     *
     * @param xTile the x tile
     * @param yTile the y tile
     * @param marioSim the mario sim
     */
    public void bumpCheck(int xTile, int yTile, MarioSim marioSim)
    {
        if (deadTime != 0)
        {
            return;
        }

        if (xLocation + width > xTile * 16 && xLocation - width < xTile * 16 + 16 && yTile == (int) ((yLocation - 1) / 16))
        {
            xAcceleration = -marioSim.getFacing() * 2;
            yAcceleration = -5;
            flyDeath = true;
            deadTime = 100;
            winged = false;
        }
    }

    /**
     * Can kill.
     *
     * @return true, if successful
     */
    public final boolean canKill()
    {
        switch (simType)
        {
            case Sprite.KIND_ENEMY_FLOWER:
            case Sprite.KIND_SPIKY:
            case Sprite.KIND_SPIKY_WINGED:
                return true;
        }
        return false;
    }

    /* (non-Javadoc)
     * @see competition.uu2013.common.Sprites.SpriteSim#collideCheck()
     */
    @Override
    public void collideCheck()
    {
        if (deadTime != 0)
        {
            return;
        }

        float xMarioD = marioSim.getXLocation() - xLocation;
        float yMarioD = marioSim.getYLocation() - yLocation;
        float height = this.getHeight();


        if (xMarioD > -width*2-4 && xMarioD < width*2+4) {
            if (yMarioD > -height && yMarioD < marioSim.getHeight()) {
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

    /* (non-Javadoc)
     * @see competition.uu2013.common.Sprites.SpriteSim#checkFireballCollide(competition.uu2013.common.Sprites.FireBallSim)
     */
    @Override
    public boolean checkFireballCollide(FireBallSim fireball)
    {
        if (deadTime != 0)
        {
            return false;
        }

        float xD = fireball.getXLocation() - xLocation;
        float yD = fireball.getYLocation() - yLocation;

        if (xD > -16 && xD < 16)
        {
            if (yD > -height && yD < fireball.height)
            {
                if (noFireballDeath)
                {
                    return true;
                }

                xAcceleration = fireball.facing * 2;
                yAcceleration = -5;
                flyDeath = true;
                this.dead = true;
                deadTime = 100;
                winged = false;
                return true;
            }
        }
        return false;
    }

    /* (non-Javadoc)
     * @see competition.uu2013.common.Sprites.SpriteSim#checkShellCollide(competition.uu2013.common.Sprites.ShellSim)
     */
    public void checkShellCollide(ShellSim shell)
    {
        if (deadTime != 0)
        {
            return;
        }

        float xD = shell.xLocation - xLocation;
        float yD = shell.yLocation - yLocation;

        if (xD > -16 && xD < 16)
        {
            if (yD > -height && yD < shell.height)
            {
                xAcceleration = shell.facing * 2;
                yAcceleration = -5;
                flyDeath = true;
                deadTime = 100;
                winged = false;
            }
        }
    }


    /**
     * Sets the facing.
     *
     * @param _facing the new facing
     */
    public void setFacing(int _facing)
    {
        this.facing = _facing;
    }

    /**
     * Sets the x.
     *
     * @param _x the new x
     */
    public void setX(float _x)
    {
        this.xLocation = _x;
    }

    /**
     * Sets the ya.
     *
     * @param _ya the new ya
     */
    public void setYA(float _ya)
    {
        this.yAcceleration = _ya;
    }

    /**
     * Sets the y.
     *
     * @param _y the new y
     */
    public void setY(float _y)
    {
        this.yLocation = _y;
    }

    /**
     * Gets the accurate y.
     *
     * @return the accurate y
     */
    public float getAccurateY()
    {
        return accurateY;
    }

    /**
     * Checks if is YA unknown.
     *
     * @return true, if is YA unknown
     */
    public boolean isYAUnknown()
    {
        return yaUnknown;
    }

    /**
     * Sets the known ya.
     *
     * @param known the new known ya
     */
    public void setKnownYA(boolean known)
    {
        this.yaUnknown = known;
    }

    /**
     * Sets the accurate y location.
     *
     * @param _accurateY the new accurate y location
     */
    public void setAccurateYLocation(float _accurateY)
    {
        this.accurateY = _accurateY;
    }

    /**
     * Sets the xa.
     *
     * @param _xa the new xa
     */
    public void setXA(float _xa)
    {
        this.xAcceleration = _xa;
    }

    /**
     * Sets the accurate x location.
     *
     * @param _accurateX the new accurate x location
     */
    public void setAccurateXLocation(float _accurateX)
    {
        this.accurateX = _accurateX;
    }

    /**
     * Gets the accurate x.
     *
     * @return the accurate x
     */
    public float getAccurateX()
    {
        return accurateX;
    }

    /**
     * Determines if an enemy has entered the scope of the Receptive field and should be tracked 
     *
     * @param marioX Mario's X Position
     * @param marioY Mario's Y position
     * @param halfSceneWidth Half the receptive field width
     * @param halfSceneHeight Half the receptive field height
     * @return true, if new to scope
     */
    public boolean newWithinScope(float marioX, float marioY, int halfSceneWidth, int halfSceneHeight)
    {
        if (!alreadyInScope  && this.withinScope(marioX, marioY, halfSceneWidth, halfSceneHeight))
        {
            this.alreadyInScope = true;
        }
        return !alreadyInScope;
    }

    /**
     * Determines if an enemy is in the scope of the Receptive field and should be tracked 
     *
     * @param marioX Mario's X Position
     * @param marioY Mario's Y position
     * @param halfSceneWidth Half the receptive field width
     * @param halfSceneHeight Half the receptive field height
     * @return true, if new to scope
     */
    public boolean withinScope(float marioX, float marioY, int halfSceneWidth, int halfSceneHeight)
    {
        float lookAHead = marioX + (halfSceneWidth * 16);
        float lookBelow = marioY + (halfSceneHeight * 16);
        float lookBehind = marioX - (halfSceneWidth * 16);
        float lookAbove = marioY - (halfSceneHeight * 16);

        if ((this.xLocation < lookAHead) && (this.yLocation < lookBelow) && (this.xLocation > lookBehind) && (this.yLocation > lookAbove))
        {
            return true;
        }
        return false;
    }

    /** 
     * Provides a string representation of the sprite, used for debugging purposes. 
     * @see competition.uu2013.common.Sprites.SpriteSim#toString()
     */
    public String toString()
    {
        return Enemy.nameEnemy(this.simType) + ", X: " + this.getXLocation() + ", Y: " + this.getYLocation() + ", height: " + this.height + ", width: " + this.width +  ", Dead: " + this.dead;
    }
}
