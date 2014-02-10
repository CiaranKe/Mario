package competition.uu2013.common.oldCode;

import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.benchmark.mario.engine.Replayer;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.tools.MarioAIOptions;
import competition.uu2013.common.oldCode.level.Level;
import competition.uu2013.common.oldCode.level.SpriteTemplate;
import competition.uu2013.common.oldCode.sprites.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class LevelScene implements SpriteContext
{
    public static final boolean[] defaultKeys = new boolean[Environment.numberOfKeys];
    public static final String[] keysStr = {"<<L ", "R>> ", "\\\\//", "JUMP", " RUN", "^UP^"};

    public static final int cellSize = 16;

    private List<Sprite> sprites = new ArrayList<Sprite>();
    final private List<Sprite> spritesToAdd = new ArrayList<Sprite>();
    final private List<Sprite> spritesToRemove = new ArrayList<Sprite>();

    public Level level;
    public Mario mario;
    public float xCam, yCam, xCamO, yCamO;

    public int tickCount;

    public int startTime = 0;
    private int timeLeft;
    private int width;
    private int height;

    private static boolean onLadder = false;

    private Random randomGen = new Random(0);

    final private List<Float> enemiesFloatsList = new ArrayList<Float>();
    final private float[] marioFloatPos = new float[2];
    final private int[] marioState = new int[11];
    private int numberOfHiddenCoinsGained = 0;

    private int greenMushroomMode = 0;

    public String memo = "";
    private Point marioInitialPos;
    private int bonusPoints = -1;
    private int totalTime = 200;

//    public int getTimeLimit() {  return timeLimit; }

    public void setTimeLimit(int timeLimit)
    { this.timeLimit = timeLimit; }

    private int timeLimit = 200;

    private long levelSeed;
    private int levelType;
    private int levelDifficulty;
    private int levelLength;
    private int levelHeight;
    public static int killedCreaturesTotal;
    public static int killedCreaturesByFireBall;
    public static int killedCreaturesByStomp;
    public static int killedCreaturesByShell;

    private Replayer replayer;

//    private int[] args; //passed to reset method. ATTENTION: not cloned.

    public LevelScene()
    {
         Level.loadBehaviors();
    }


    public Sprite getSprite (Sprite sprite)
    {
        for (Sprite current:sprites)
        {
            if (current.equals(sprite))
            {
                return current;
            }
        }
        return null;
    }


// TODO: !H!: Move to MarioEnvironment !!

    public float[] getEnemiesFloatPos()
    {
        enemiesFloatsList.clear();
        for (Sprite sprite : sprites)
        {
            // TODO:[M]: add unit tests for getEnemiesFloatPos involving all kinds of creatures
            if (sprite.isDead()) continue;
            switch (sprite.kind)
            {
                case Sprite.KIND_GOOMBA:
                case Sprite.KIND_BULLET_BILL:
                case Sprite.KIND_ENEMY_FLOWER:
                case Sprite.KIND_GOOMBA_WINGED:
                case Sprite.KIND_GREEN_KOOPA:
                case Sprite.KIND_GREEN_KOOPA_WINGED:
                case Sprite.KIND_RED_KOOPA:
                case Sprite.KIND_RED_KOOPA_WINGED:
                case Sprite.KIND_SPIKY:
                case Sprite.KIND_SPIKY_WINGED:
                case Sprite.KIND_SHELL:
                {
                    enemiesFloatsList.add((float) sprite.kind);
                    enemiesFloatsList.add(sprite.x - mario.x);
                    enemiesFloatsList.add(sprite.y - mario.y);
                }
            }
        }

        float[] enemiesFloatsPosArray = new float[enemiesFloatsList.size()];

        int i = 0;
        for (Float F : enemiesFloatsList)
            enemiesFloatsPosArray[i++] = F;

        return enemiesFloatsPosArray;
    }

    public int fireballsOnScreen = 0;

    List<Shell> shellsToCheck = new ArrayList<Shell>();

    public void checkShellCollide(Shell shell)
    {
        shellsToCheck.add(shell);
    }

    List<Fireball> fireballsToCheck = new ArrayList<Fireball>();

    public void checkFireballCollide(Fireball fireball)
    {
        fireballsToCheck.add(fireball);
    }

    public void tick()
    {
        if (GlobalOptions.isGameplayStopped)
            return;

        timeLeft--;
        if (timeLeft == 0)
            mario.die("Time out!");
        xCamO = xCam;
        yCamO = yCam;

        if (startTime > 0)
        {
            startTime++;
        }

        float targetXCam = mario.x - 160;

        xCam = targetXCam;

        if (xCam < 0) xCam = 0;
        if (xCam > level.length * cellSize - GlobalOptions.VISUAL_COMPONENT_WIDTH)
            xCam = level.length * cellSize - GlobalOptions.VISUAL_COMPONENT_WIDTH;

        fireballsOnScreen = 0;

        for (Sprite sprite : sprites)
        {
            if (sprite != mario)
            {
                float xd = sprite.x - xCam;
                float yd = sprite.y - yCam;
                if (xd < -64 || xd > GlobalOptions.VISUAL_COMPONENT_WIDTH + 64 || yd < -64 || yd > GlobalOptions.VISUAL_COMPONENT_HEIGHT + 64)
                {
                    removeSprite(sprite);
                } else
                {
                    if (sprite instanceof Fireball)
                        fireballsOnScreen++;
                }
            }
        }

        tickCount++;
        level.tick();

//            boolean hasShotCannon = false;
//            int xCannon = 0;

        for (int x = (int) xCam / cellSize - 1; x <= (int) (xCam + this.width) / cellSize + 1; x++)
            for (int y = (int) yCam / cellSize - 1; y <= (int) (yCam + this.height) / cellSize + 1; y++)
            {
                int dir = 0;

                if (x * cellSize + 8 > mario.x + cellSize) dir = -1;
                if (x * cellSize + 8 < mario.x - cellSize) dir = 1;

                /*
                SpriteTemplate st = level.getSpriteTemplate(x, y);

                if (st != null)
                {
//                        if (st.getType() == Sprite.KIND_SPIKY)
//                        {
//                            System.out.println("here");
//                        }

                    if (st.lastVisibleTick != tickCount - 1)
                    {
                        if (st.sprite == null || !sprites.contains(st.sprite))
                            st.spawn(this, x, y, dir);
                    }

                    st.lastVisibleTick = tickCount;
                }
                */

                if (dir != 0)
                {
                    byte b = level.getBlock(x, y);
                    if (((Level.TILE_BEHAVIORS[b & 0xff]) & Level.BIT_ANIMATED) > 0)
                    {
                        if ((b % cellSize) / 4 == 3 && b / cellSize == 0)
                        {
                            if ((tickCount - x * 2) % 100 == 0)
                            {
//                                    xCannon = x;
                                for (int i = 0; i < 8; i++)
                                {
                                    //addSprite(new Sparkle(x * cellSize + 8, y * cellSize + (int) (Math.random() * cellSize), (float) Math.random() * dir, 0, 0, 1, 5));
                                }
                                addSprite(new BulletBill(this, x * cellSize + 8 + dir * 8, y * cellSize + 15, dir));

//                                    hasShotCannon = true;
                            }
                        }
                    }
                }
            }

        for (Sprite sprite : sprites)
        {
            sprite.tick();
        }

        byte levelElement = level.getBlock(mario.mapX, mario.mapY);
        if (levelElement == (byte) (13 + 3 * 16) || levelElement == (byte) (13 + 5 * 16))
        {
            if (levelElement == (byte) (13 + 5 * 16))
                mario.setOnTopOfLadder(true);
            else
                mario.setInLadderZone(true);
        } else if (mario.isInLadderZone())
        {
            mario.setInLadderZone(false);
        }


        for (Sprite sprite : sprites)
            sprite.collideCheck();

        for (Shell shell : shellsToCheck)
        {
            for (Sprite sprite : sprites)
            {
                if (sprite != shell && !shell.dead)
                {
                    if (sprite.shellCollideCheck(shell))
                    {
                        if (mario.carried == shell && !shell.dead)
                        {
                            mario.carried = null;
                            mario.setRacoon(false);
                            //System.out.println("sprite = " + sprite);
                            shell.die();
                            ++this.killedCreaturesTotal;
                        }
                    }
                }
            }
        }
        shellsToCheck.clear();

        for (Fireball fireball : fireballsToCheck)
            for (Sprite sprite : sprites)
                if (sprite != fireball && !fireball.dead)
                    if (sprite.fireballCollideCheck(fireball))
                        fireball.die();
        fireballsToCheck.clear();


        sprites.addAll(0, spritesToAdd);
        sprites.removeAll(spritesToRemove);
        spritesToAdd.clear();
        spritesToRemove.clear();
    }

    public void addSprite(Sprite sprite)
    {
        spritesToAdd.add(sprite);
        sprite.tick();
    }

    public void removeSprite(Sprite sprite)
    {
        spritesToRemove.add(sprite);
    }

    public void bump(int x, int y, boolean canBreakBricks)
    {
        byte block = level.getBlock(x, y);

        if ((Level.TILE_BEHAVIORS[block & 0xff] & Level.BIT_BUMPABLE) > 0)
        {
            if (block == 1)
                Mario.gainHiddenBlock();
            bumpInto(x, y - 1);
            byte blockData = level.getBlockData(x, y);
            if (blockData < 0)
                level.setBlockData(x, y, (byte) (blockData + 1));

            if (blockData == 0)
            {
                level.setBlock(x, y, (byte) 4);
                level.setBlockData(x, y, (byte) 4);
            }

            if (((Level.TILE_BEHAVIORS[block & 0xff]) & Level.BIT_SPECIAL) > 0)
            {
                if (randomGen.nextInt(5) == 0 && level.difficulty > 4)
                {
                    addSprite(new GreenMushroom(this, x * cellSize + 8, y * cellSize + 8));
                } else
                {
                    if (!Mario.large)
                    {
                        addSprite(new Mushroom(this, x * cellSize + 8, y * cellSize + 8));
                    } else
                    {
                        addSprite(new FireFlower(this, x * cellSize + 8, y * cellSize + 8));
                    }
                }
            } else
            {
                Mario.gainCoin();
                //addSprite(new CoinAnim(x, y));
            }
        }

        if ((Level.TILE_BEHAVIORS[block & 0xff] & Level.BIT_BREAKABLE) > 0)
        {
            bumpInto(x, y - 1);
            if (canBreakBricks)
            {
                level.setBlock(x, y, (byte) 0);
                //for (int xx = 0; xx < 2; xx++)
                // for (int yy = 0; yy < 2; yy++)
                //        addSprite(new Particle(x * cellSize + xx * 8 + 4, y * cellSize + yy * 8 + 4, (xx * 2 - 1) * 4, (yy * 2 - 1) * 4 - 8));
            } else
            {
                level.setBlockData(x, y, (byte) 4);
            }
        }
    }

    public void bumpInto(int x, int y)
    {
        byte block = level.getBlock(x, y);
        if (((Level.TILE_BEHAVIORS[block & 0xff]) & Level.BIT_PICKUPABLE) > 0)
        {
            Mario.gainCoin();
            level.setBlock(x, y, (byte) 0);
            //addSprite(new CoinAnim(x, y + 1));
        }

        for (Sprite sprite : sprites)
        {
            sprite.bumpCheck(x, y);
        }
    }

    public int getTimeSpent() { return startTime / GlobalOptions.mariosecondMultiplier; }

    public int getTimeLeft() { return timeLeft / GlobalOptions.mariosecondMultiplier; }

    public int getKillsTotal()
    {
        return killedCreaturesTotal;
    }

    public int getKillsByFire()
    {
        return killedCreaturesByFireBall;
    }

    public int getKillsByStomp()
    {
        return killedCreaturesByStomp;
    }

    public int getKillsByShell()
    {
        return killedCreaturesByShell;
    }

    public int[] getMarioState()
    {
        marioState[0] = this.getMarioStatus();
        marioState[1] = this.getMarioMode();
        marioState[2] = this.isMarioOnGround() ? 1 : 0;
        marioState[3] = this.isMarioAbleToJump() ? 1 : 0;
        marioState[4] = this.isMarioAbleToShoot() ? 1 : 0;
        marioState[5] = this.isMarioCarrying() ? 1 : 0;
        marioState[6] = this.getKillsTotal();
        marioState[7] = this.getKillsByFire();
        marioState[8] = this.getKillsByStomp();
        marioState[9] = this.getKillsByShell();
        marioState[10] = this.getTimeLeft();
        return marioState;
    }

    public void performAction(boolean[] action)
    {
        // might look ugly , but arrayCopy is not necessary here:
        this.mario.keys = action;
    }

    public boolean isLevelFinished()
    {
        return (mario.getStatus() != Mario.STATUS_RUNNING);
    }

    public boolean isMarioAbleToShoot()
    {
        return mario.isAbleToShoot();
    }

    public int getMarioStatus()
    {
        return mario.getStatus();
    }

    /**
     * first and second elements of the array are x and y Mario coordinates correspondingly
     *
     * @return an array of size 2*(number of creatures on screen) including mario
     */
    public float[] getCreaturesFloatPos()
    {
        float[] enemies = this.getEnemiesFloatPos();
        float ret[] = new float[enemies.length + 2];
        System.arraycopy(this.getMarioFloatPos(), 0, ret, 0, 2);
        System.arraycopy(enemies, 0, ret, 2, enemies.length);
        return ret;
    }

    public boolean isMarioOnGround()
    { return mario.isOnGround(); }

    public boolean isMarioAbleToJump()
    { return mario.mayJump(); }

    public void resetDefault()
    {
        // TODO: set values to defaults
        reset(MarioAIOptions.getDefaultOptions());
    }

    public void reset(MarioAIOptions marioAIOptions)
    {
//
    }

    public float[] getMarioFloatPos()
    {
        marioFloatPos[0] = this.mario.x;
        marioFloatPos[1] = this.mario.y;
        return marioFloatPos;
    }

    public int getMarioMode()
    { return mario.getMode(); }

    public boolean isMarioCarrying()
    { return mario.carried != null; }

    public int getLevelDifficulty()
    { return levelDifficulty; }

    public long getLevelSeed()
    { return levelSeed; }

    public int getLevelLength()
    { return levelLength; }

    public int getLevelHeight()
    { return levelHeight; }

    public int getLevelType()
    { return levelType; }


    public void addMemoMessage(final String memoMessage)
    {
        memo += memoMessage;
    }

    public Point getMarioInitialPos() {return marioInitialPos;}

    public void setReplayer(Replayer replayer)
    {
        this.replayer = replayer;
    }

    public int getGreenMushroomMode()
    {
        return greenMushroomMode;
    }

    public int getBonusPoints()
    {
        return bonusPoints;
    }

    public void setBonusPoints(final int bonusPoints)
    {
        this.bonusPoints = bonusPoints;
    }

    public void appendBonusPoints(final int superPunti)
    {
        bonusPoints += superPunti;
    }

    /*********** My Code **********************************/

    public byte getEnemies(int xPos, int yPos)
    {
        for (Sprite sprite: sprites)
        {
            if (sprite.mapX == xPos && sprite.mapY == yPos)
            {
                return sprite.kind;
            }
        }
        return 0;
    }

    public boolean setEnemies(float[] enemies, float marioX, float marioY)
    {
        boolean requireReplanning = false;
        List<Sprite> newSprites = new ArrayList<Sprite>();
        for (int i = 0; i < enemies.length; i += 3)
        {
            int kind = (int) enemies[i];
            float x = enemies[i+1];
            float y = enemies[i+2];
            int mapX = (int) (marioX + x)/16;
            int mapY = (int) (marioY + y)/16;

            if (kind == -1 || kind == 15)
                continue;
            int type = -1;
            boolean winged = false;
            switch(kind)
            {
                case(Sprite.KIND_BULLET_BILL): type = -2; break;
                case(Sprite.KIND_GOOMBA): type = Enemy.KIND_GOOMBA; break;
                case(Sprite.KIND_SHELL): type = Enemy.KIND_SHELL; break;
                case(Sprite.KIND_GOOMBA_WINGED): type = Enemy.KIND_GOOMBA_WINGED; winged = true; break;
                case(Sprite.KIND_GREEN_KOOPA): type = Enemy.KIND_GREEN_KOOPA; break;
                case(Sprite.KIND_GREEN_KOOPA_WINGED): type = Enemy.KIND_GREEN_KOOPA_WINGED; winged = true; break;
                case(Sprite.KIND_RED_KOOPA): type = Enemy.KIND_RED_KOOPA; break;
                case(Sprite.KIND_RED_KOOPA_WINGED): type = Enemy.KIND_RED_KOOPA_WINGED; winged = true; break;
                case(Sprite.KIND_SPIKY): type = Enemy.KIND_SPIKY; break;
                case(Sprite.KIND_SPIKY_WINGED): type = Enemy.KIND_SPIKY_WINGED; winged = true; break;
                case(Sprite.KIND_ENEMY_FLOWER): type = Enemy.KIND_ENEMY_FLOWER; break;
                case Sprite.KIND_WAVE_GOOMBA: type = Sprite.KIND_WAVE_GOOMBA; winged = true; break;
            }
            if (type == -1)
                continue;


            // is there already an enemy here?
            float maxDelta = 2.01f * 1.75f;
            boolean enemyFound = false;
            for (Sprite sprite:sprites)
            {
                // check if object is of same kind and close enough
                if (sprite.kind == kind
                        && Math.abs(sprite.x - x) < maxDelta
                        && ((Math.abs(sprite.y - y) < maxDelta) || sprite.kind == Sprite.KIND_ENEMY_FLOWER))
                {
                    if (Math.abs(sprite.x - x) > 0)
                    {

                        if (sprite.kind == Sprite.KIND_SHELL)
                            ((Shell) sprite).facing *= -1;
                        else
                            ((Enemy) sprite).facing *= -1;
                        requireReplanning = true;
                        sprite.x = x;
                    }
                    if ((sprite.y - y) != 0 && sprite.kind == Sprite.KIND_ENEMY_FLOWER)
                    {

                        ((Enemy) sprite).ya = (y - sprite.lastAccurateY) * 0.89f;//+= sprite.y - y;
                        sprite.y = y;
                    }
                    enemyFound = true;
                }

                if (sprite.kind == kind &&
                        (sprite.x - x) == 0 &&
                        (sprite.y - y) != 0 &&
                        Math.abs(sprite.y - y) < 8 &&
                        sprite.kind != Sprite.KIND_SHELL &&
                        sprite.kind != Sprite.KIND_BULLET_BILL &&
                        ((Enemy) sprite).winged)
                {

                    sprite.ya = (y - sprite.lastAccurateY) * 0.95f + 0.6f; // / 0.89f;

                    sprite.y = y;
                    sprite.unknownYA = false;
                    enemyFound = true;
                    requireReplanning = true;
                }
                if (sprite.kind == kind &&
                        (sprite.x - x) == 0 &&
                        (sprite.y - y) != 0 &&
                        Math.abs(sprite.y - y) <= 2 &&
                        sprite.unknownYA &&
                        sprite.lastAccurateY != 0)
                {
                    // should be not winged, falling down a cliff
                    sprite.ya = (y - sprite.lastAccurateY) * 0.85f + 2; // / 0.89f;
                    sprite.y = y;

                    sprite.unknownYA = false;
                    enemyFound = true;
                }

                if (enemyFound)
                {
                    newSprites.add(sprite);
                    sprite.lastAccurateX = x;
                    sprite.lastAccurateY = y;
                    break;
                }
            }
            // didn't find a close enemy in our representation of the world,
            // create a new one.
            if (!enemyFound)
            {
                requireReplanning = true;
                Sprite sprite;
                if (type == Enemy.KIND_ENEMY_FLOWER)
                {
                    sprite = new FlowerEnemy(this, mapX * 16 + 15, mapY * 16 + 24, mapX, mapY);
                }
                else if (type == Sprite.KIND_WAVE_GOOMBA)
                {
                    sprite = new WaveGoomba(this, mapX * 16 + 8, mapY * 16 + 15 - 3 * 16, -1, mapX, mapY);
                }
                else
                {
                    // Add new enemy to the system.
                    sprite = new Enemy(this, mapX, mapY * 16 + 15, -1, type, winged, mapX, mapY);
                    sprite.xa = 2;
                }

                sprite.lastAccurateX = x;
                sprite.lastAccurateY = y;
                sprite.x = enemies[i+1];
                sprite.y = enemies[i+2];

                sprite.spriteTemplate =  new SpriteTemplate(type);
                newSprites.add(sprite);
            }

        }
        newSprites.add(mario);

        // add fireballs
        for (Sprite sprite:sprites)
        {
            if (sprite.kind == Sprite.KIND_FIREBALL)
                newSprites.add(sprite);
        }
        sprites = newSprites;
        return requireReplanning;
    }



    public void init(MarioAIOptions options)
    {
        Level.loadBehaviors();
        Sprite.spriteContext = this;
        sprites.clear();
        mario = new Mario(this);
        Mario.resetStatic(options);
        mario.ya = 3;
        sprites.add(mario);
        startTime = 1;
        timeLeft = totalTime * 15;
        tickCount  = 1;
    }
}

//    public void update(boolean[] action)
//    {
//        System.arraycopy(action, 0, mario.keys, 0, 6);
//    }
