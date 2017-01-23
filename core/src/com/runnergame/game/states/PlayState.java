package com.runnergame.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.Timer;
import com.flurry.android.FlurryAgent;
import com.runnergame.game.Colors;
import com.runnergame.game.Constants;
import com.runnergame.game.GameRunner;
import com.runnergame.game.sprites.Background;
import com.runnergame.game.sprites.Blocks.Block;
import com.runnergame.game.sprites.Blocks.BlockBeam;
import com.runnergame.game.sprites.Blocks.BlockBoost;
import com.runnergame.game.sprites.Blocks.BlockBoss;
import com.runnergame.game.sprites.Blocks.BlockBullet;
import com.runnergame.game.sprites.Blocks.BlockFinish;
import com.runnergame.game.sprites.Blocks.BlockFloor;
import com.runnergame.game.sprites.Blocks.BlockGun;
import com.runnergame.game.sprites.Blocks.BlockIce;
import com.runnergame.game.sprites.Blocks.BlockJump;
import com.runnergame.game.sprites.Blocks.BlockNeedle;
import com.runnergame.game.sprites.Button;
import com.runnergame.game.sprites.Coin;
import com.runnergame.game.sprites.Player;

import java.util.HashMap;

public class PlayState extends State {
    //CHEATS
    public static boolean doNotDie=false;
    public static boolean doNotCollige=false;

    public static int lvl=0;
    private int colorSize;
    public static final int BLOCK_SPACING = 64;
    public static final int BLOCKS_MAX_COUNT = 20;

    private boolean START_LEVEL = false;
    private Queue<Block> blocks;
    private Array<Coin> coins;

    private Player player;
    private Array<Sprite> playerTail;
    private Sprite helpColorChange = new Sprite(new Texture("halper.png"));

    private Array<Sprite> bgEffect;
    Background bg;

    boolean pauseBtnPress = false;
    Button pauseBtn;
    //REBORN
    public static boolean reborn = false;
    private boolean doReborn = true;

    private SpriteBatch tb;
    OrthographicCamera cam_btn;
    //BOSS
    private boolean IS_BOSS = false;
    BlockBoss boss;
    Array<BlockBullet> bul;
    boolean heat = false;
    float timeHeat = 2;

    //TIME LEVEL
    boolean TIME_LEVEL=false;
    float timer;
    boolean CAN_ADD_TIMER=true;
    float addTime = 10;

    private void addBeam(float x) {
        int size = MathUtils.random(5, 10);
        int color = MathUtils.random(1, colorSize);
        int len;
        for(int i = 0; i < size; ++i) {
            len = MathUtils.random(3, 7);
            for(int j = 0; j < len; ++j) {
                if(j == len-1) {
                    blocks.addLast(new BlockBeam(x, Constants.Y0+Constants.BLOCK_H/2+16, Constants.B_FLOOR));
                    blocks.last().setColor(color);
                    color = MathUtils.random(1, colorSize);
                }
                if(CAN_ADD_TIMER && TIME_LEVEL && timer < addTime) {
                    coins.add(new Coin(x, Constants.Y0+32, 2));
                    CAN_ADD_TIMER = false;
                }
                blocks.addLast(new BlockFloor(x, Constants.Y0, Constants.B_FLOOR));
                blocks.last().setColor(0);
                x += 64;
            }
        }
    }

    private void addIceBridge(float x) {
        int c = MathUtils.random(1, colorSize);
        blocks.addLast(new BlockBoost(x + BLOCK_SPACING*0, Constants.Y0, Constants.B_FLOOR, 200));
        blocks.last().setColor(c);
        x += BLOCK_SPACING;
        //for (int i = 0; i < 2; ++i) {
        blocks.addLast(new BlockIce(x + BLOCK_SPACING, Constants.Y0, Constants.B_FLOOR));
        blocks.last().setColor(Colors.GRAY);
        blocks.addLast(new BlockIce(x + BLOCK_SPACING*4, Constants.Y0, Constants.B_FLOOR));
        blocks.last().setColor(Colors.GRAY);
        if(CAN_ADD_TIMER && TIME_LEVEL && timer < addTime) {
            coins.add(new Coin(x + BLOCK_SPACING, Constants.Y0+32, 2));
            CAN_ADD_TIMER = false;
        }
        //}
    }

    private void addHole(float x) {
        int color = MathUtils.random(1, colorSize);
        blocks.addLast(new BlockJump(x, Constants.Y0, Constants.B_FLOOR, 350));
        blocks.last().setColor(color);
        x += BLOCK_SPACING*3;
        for (int i = 0; i < 4; ++i) {
            blocks.addLast(new BlockFloor(x + BLOCK_SPACING*i, Constants.Y0, Constants.B_FLOOR));
            blocks.last().setColor(Colors.GRAY);
        }
    }

    private void addPlatform2(float x) {
        int len = MathUtils.random(5, 10);
        int platform_color1 = MathUtils.random(1, colorSize);
        int platform_color2 = MathUtils.random(1, colorSize);
        int _floor = 0;
        float __x;

        blocks.addLast(new BlockFloor(x + BLOCK_SPACING*0, Constants.Y0, Constants.B_FLOOR));
        blocks.last().setColor(0);
        blocks.addLast(new BlockFloor(x + BLOCK_SPACING*1, Constants.Y0, Constants.B_FLOOR));
        blocks.last().setColor(0);
        blocks.addLast(new BlockJump(x + BLOCK_SPACING*2, Constants.Y0, Constants.B_JUMP, 500));
        blocks.last().setColor(platform_color1);
        __x = blocks.get(blocks.size-1).getPos().x+Constants.BLOCK_W+192;
        //blocks.add(new BlockJump(x + BLOCK_SPACING*4, Constants.Y0+64, Constants.B_JUMP));
        //blocks.get(blocks.size-1).setColor(platform_color);
        blocks.addLast(new BlockFloor(x + BLOCK_SPACING*3, Constants.Y0, Constants.B_FLOOR));
        blocks.last().setColor(0);
        blocks.addLast(new BlockFloor(x + BLOCK_SPACING*4, Constants.Y0, Constants.B_FLOOR));
        blocks.last().setColor(0);
        blocks.addLast(new BlockFloor(x + BLOCK_SPACING*5, Constants.Y0, Constants.B_FLOOR));
        blocks.last().setColor(0);
        _floor = 2;
        for (int i = 0; i < len; ++i) {
            if (MathUtils.random(0, 20) >= 16) {
                coins.add(new Coin(__x, Constants.GROUND + 32, 0));
            }
            blocks.addLast(new BlockFloor(__x, Constants.Y0 + 64*_floor, Constants.B_FLOOR));
            blocks.last().setColor(platform_color1);
            blocks.addLast(new BlockFloor(__x, Constants.Y0, Constants.B_FLOOR));
            blocks.last().setColor(platform_color2);
            if(CAN_ADD_TIMER && TIME_LEVEL && timer < addTime) {
                coins.add(new Coin(__x, Constants.Y0+32, 2));
                CAN_ADD_TIMER = false;
            }
            __x += 64;
        }
    }

    private void addPlatform(float x) {
        int platform_count = MathUtils.random(3, 5);
        int platform_color;
        int _floor = 0;
        float __x;

        blocks.addLast(new BlockFloor(x + BLOCK_SPACING*0, Constants.Y0, Constants.B_FLOOR));
        blocks.last().setColor(0);
        blocks.addLast(new BlockFloor(x + BLOCK_SPACING*1, Constants.Y0, Constants.B_FLOOR));
        blocks.last().setColor(0);

        __x = blocks.last().getPos().x+Constants.BLOCK_W;

        for (int i = 0; i < platform_count; ++i) {
            platform_color = MathUtils.random(1, colorSize);
            if (i == 0) {
                blocks.addLast(new BlockJump(__x, Constants.Y0, Constants.B_JUMP, 350));
                blocks.last().setColor(platform_color);
                __x += Block.getPlatformSpace();
                continue;
            }
            int k = MathUtils.random(0, 2);
            if (k == 0 && _floor > 0) {
                _floor -= 1;
            } else if(k == 1 && _floor < 3) {
                _floor += 1;
            }
            if (MathUtils.random(0, 20) == 20) {
                coins.add(new Coin(__x, Constants.GROUND + 32*_floor, 0));
            }

            blocks.addLast(new BlockJump(__x, Constants.Y0 + 32*_floor, 4, 350));
            blocks.last().setColor(platform_color);
            if(CAN_ADD_TIMER && TIME_LEVEL && timer < addTime) {
                coins.add(new Coin(__x, Constants.Y0 + 32*_floor+32, 2));
                CAN_ADD_TIMER = false;
            }
            __x += 64;
            blocks.addLast(new BlockJump(__x, Constants.Y0 + 32*_floor, 4, 310));
            blocks.last().setColor(platform_color);
            __x += Block.getPlatformSpace();;
        }
    }

    private void addBridge(float x) {
        int bridge_color = MathUtils.random(1, colorSize);
        int size = MathUtils.random(8, 16);

        for (int i = 0; i < size; ++i) {
            if (MathUtils.random(0, 20) == 20) {
                coins.add(new Coin(x + BLOCK_SPACING*i, Constants.GROUND, 0));
            }
            blocks.addLast(new BlockFloor(x + BLOCK_SPACING*i, Constants.Y0, Constants.B_FLOOR));
            if(i == 0 || i == 1 || i == size-1 || i == size-2) {
                blocks.last().setColor(0);
            } else {
                blocks.last().setColor(bridge_color);
            }
        }
    }

    private void addNeedle(float x) {
        int needle_color = MathUtils.random(0, colorSize);
        int jump_color = MathUtils.random(1, colorSize);
        int size = MathUtils.random(5, 10);

        for (int i = 0; i < size; ++i) {
            if (i == 2 && needle_color == 0) {
                blocks.addLast(new BlockJump(x + BLOCK_SPACING*i, Constants.Y0, Constants.B_JUMP, 350));
                blocks.last().setColor(jump_color);
                continue;
            }
            if (i == 4) {
                blocks.addLast(new BlockNeedle(x + BLOCK_SPACING*i, Constants.Y0 + Constants.BLOCK_H/2 + 16, Constants.B_NEEDLE));
                blocks.last().setColor(needle_color);
            }
            blocks.addLast(new BlockFloor(x + BLOCK_SPACING*i, Constants.Y0, 0));
            blocks.last().setColor(0);
        }
    }

    private void addNeedleUp(float x) {
        int needle_color = MathUtils.random(1, colorSize);
        int size = MathUtils.random(5, 10);

        blocks.addLast(new BlockFloor(x + BLOCK_SPACING*0, Constants.Y0, Constants.B_FLOOR));
        blocks.last().setColor(0);
        blocks.addLast(new BlockFloor(x + BLOCK_SPACING*1, Constants.Y0, Constants.B_FLOOR));
        blocks.last().setColor(0);
        if (MathUtils.random(0, 20) == 20) {
            coins.add(new Coin(x + BLOCK_SPACING, Constants.GROUND, 0));
        }
        for (int i = 2; i < size-2; ++i) {
            blocks.addLast(new BlockNeedle(x + BLOCK_SPACING*i, Constants.Y0 + Constants.BLOCK_H/2 + 16, Constants.B_NEEDLE));
            blocks.last().setColor(needle_color);
            blocks.addLast(new BlockFloor(x + BLOCK_SPACING*i, Constants.Y0, Constants.B_FLOOR));
            blocks.last().setColor(0);
            if(CAN_ADD_TIMER && TIME_LEVEL && timer < addTime) {
                coins.add(new Coin(x + BLOCK_SPACING*i, Constants.Y0, 2));
                CAN_ADD_TIMER = false;
            }
        }
        blocks.addLast(new BlockFloor(x + BLOCK_SPACING*(size-2), Constants.Y0, Constants.B_FLOOR));
        blocks.last().setColor(0);
        blocks.addLast(new BlockFloor(x + BLOCK_SPACING*(size-1), Constants.Y0, Constants.B_FLOOR));
        blocks.last().setColor(0);
    }

    private void addNeedleDown(float x) {
        int needle_color = MathUtils.random(0, colorSize);
        int jump_color = MathUtils.random(1, colorSize);
        if (MathUtils.random(0, 20) == 20) {
            coins.add(new Coin(x + BLOCK_SPACING, Constants.GROUND, 0));
        }
        blocks.addLast(new BlockFloor(x + BLOCK_SPACING*0, Constants.Y0, Constants.B_FLOOR));
        blocks.last().setColor(0);
        blocks.addLast(new BlockFloor(x + BLOCK_SPACING*1, Constants.Y0, Constants.B_FLOOR));
        blocks.last().setColor(0);
        blocks.addLast(new BlockJump(x + BLOCK_SPACING*2, Constants.Y0, Constants.B_JUMP, 350));
        blocks.last().setColor(jump_color);
        for (int i = 3; i < 5; ++i) {
            blocks.addLast(new BlockNeedle(x + BLOCK_SPACING*i, Constants.Y0, Constants.B_NEEDLE));
            blocks.last().setColor(needle_color);
            if(CAN_ADD_TIMER && TIME_LEVEL && timer < addTime) {
                coins.add(new Coin(x + BLOCK_SPACING*i, Constants.Y0, 2));
                CAN_ADD_TIMER = false;
            }
        }
        blocks.addLast(new BlockFloor(x + BLOCK_SPACING*5, Constants.Y0, Constants.B_FLOOR));
        blocks.last().setColor(0);
        blocks.addLast(new BlockFloor(x + BLOCK_SPACING*6, Constants.Y0, Constants.B_FLOOR));
        blocks.last().setColor(0);
    }

    private void addBridgeDown(float x) {
        int needle_color = MathUtils.random(1, colorSize);
        int jump_color = MathUtils.random(1, colorSize);
        int size = MathUtils.random(5, 10);

        blocks.addLast(new BlockFloor(x + BLOCK_SPACING*0, Constants.Y0, Constants.B_FLOOR));
        blocks.last().setColor(0);
        blocks.addLast(new BlockFloor(x + BLOCK_SPACING*1, Constants.Y0, Constants.B_FLOOR));
        blocks.last().setColor(0);
        if (MathUtils.random(0, 20) == 20) {
            coins.add(new Coin(x + BLOCK_SPACING, Constants.GROUND, 0));
        }
        for (int i = 2; i < size+2; ++i) {
            blocks.addLast(new BlockNeedle(x + BLOCK_SPACING*i, Constants.Y0-Constants.BLOCK_W+Constants.BLOCK_H/2+16, Constants.B_NEEDLE));
            blocks.last().setColor(needle_color);
            blocks.addLast(new BlockFloor(x + BLOCK_SPACING*i, Constants.Y0-Constants.BLOCK_W, Constants.B_FLOOR));
            blocks.last().setColor(0);
        }
        blocks.addLast(new BlockFloor(x + BLOCK_SPACING*(size+2), Constants.Y0-Constants.BLOCK_W, Constants.B_FLOOR));
        blocks.last().setColor(0);
        blocks.addLast(new BlockFloor(x + BLOCK_SPACING*(size+3), Constants.Y0-Constants.BLOCK_W, Constants.B_FLOOR));
        blocks.last().setColor(0);
        blocks.addLast(new BlockFloor(x + BLOCK_SPACING*(size+4), Constants.Y0-Constants.BLOCK_W, Constants.B_FLOOR));
        blocks.last().setColor(0);
        if(CAN_ADD_TIMER && TIME_LEVEL && timer < addTime) {
            coins.add(new Coin(x + BLOCK_SPACING*(size+4), Constants.Y0-Constants.BLOCK_W, 2));
            CAN_ADD_TIMER = false;
        }
        blocks.addLast(new BlockJump(x + BLOCK_SPACING*(size+5), Constants.Y0-Constants.BLOCK_W, Constants.B_JUMP, 350));
        blocks.last().setColor(jump_color);
        blocks.addLast(new BlockFloor(x + BLOCK_SPACING*(size+6), Constants.Y0-Constants.BLOCK_W, Constants.B_FLOOR));
        blocks.last().setColor(0);
        blocks.addLast(new BlockFloor(x + BLOCK_SPACING*(size+7), Constants.Y0-Constants.BLOCK_W, Constants.B_FLOOR));
        blocks.last().setColor(0);
        blocks.addLast(new BlockNeedle(x + BLOCK_SPACING*(size+7), Constants.Y0-Constants.BLOCK_W+Constants.BLOCK_H/2+16, Constants.B_NEEDLE));
        blocks.last().setColor(0);

        blocks.addLast(new BlockFloor(x + BLOCK_SPACING*(size+8), Constants.Y0, 0));
        blocks.last().setColor(0);
        blocks.addLast(new BlockFloor(x + BLOCK_SPACING*(size+9), Constants.Y0, 0));
        blocks.last().setColor(0);

    }

    private void addFloor(float x) {
        int size = MathUtils.random(3, 6);

        for (int i = 0; i < size; ++i) {
            blocks.addLast(new BlockFloor(x + BLOCK_SPACING*i, Constants.Y0, Constants.B_FLOOR));
            blocks.last().setColor(Colors.GRAY);
        }
    }

    private void addBoss(float x) {
        blocks.addLast(new BlockFloor(x + BLOCK_SPACING*0, Constants.Y0, Constants.B_FLOOR));
        boss = new BlockBoss(x + BLOCK_SPACING*0, Constants.Y0+Constants.BLOCK_H/2+18, Constants.B_FLOOR, (lvl/25)*5 + 15);//25
    }

    private void addGun(float x) {
        int color = MathUtils.random(1, colorSize);
        blocks.addLast(new BlockFloor(x + BLOCK_SPACING*0, Constants.Y0, Constants.B_FLOOR));
        blocks.addLast(new BlockJump(x + BLOCK_SPACING*1, Constants.Y0, Constants.B_FLOOR, 350));
        blocks.last().setColor(color);
        blocks.addLast(new BlockGun(x + BLOCK_SPACING*2, Constants.Y0 + 100, Constants.B_FLOOR));//163
        blocks.addLast(new BlockFloor(x + BLOCK_SPACING*2, Constants.Y0, Constants.B_FLOOR));
        blocks.addLast(new BlockFloor(x + BLOCK_SPACING*3, Constants.Y0, Constants.B_FLOOR));
    }

    private void addCoinJump(float x) {
        int jump_color = MathUtils.random(1, colorSize);

        blocks.addLast(new BlockJump(x + BLOCK_SPACING*0, Constants.Y0, Constants.B_JUMP, 350));
        blocks.last().setColor(jump_color);
        blocks.addLast(new BlockFloor(x + BLOCK_SPACING*1, Constants.Y0, Constants.B_FLOOR));
        blocks.last().setColor(0);
        if (MathUtils.random(0, 20) == 20) {
            coins.add(new Coin(x + BLOCK_SPACING*1, 227, 0));
        }
    }

    private void addFinish(float x) {
        blocks.addLast(new BlockFloor(x + BLOCK_SPACING*0, Constants.Y0, Constants.B_FLOOR));
        blocks.last().setColor(0);
        blocks.addLast(new BlockFloor(x + BLOCK_SPACING*1, Constants.Y0, Constants.B_FLOOR));
        blocks.last().setColor(0);
        blocks.addLast(new BlockFinish(x + BLOCK_SPACING*2, Constants.Y0 + 64, Constants.B_FINISH));
        blocks.last().setColor(0);
        blocks.addLast(new BlockFloor(x + BLOCK_SPACING*2, Constants.Y0, Constants.B_FLOOR));
        blocks.last().setColor(0);
    }

    private void addTiemr() {
        TIME_LEVEL = true;
        timer = 10;
    }

    private void rotLeft() {
        rot1 = rot0 + MathUtils.random(10, 50);
        rotSpeedForBlock = MathUtils.random(5, 20);
    }
    private void rotRight() {
        rot1 = rot0 - MathUtils.random(10, 50);
        rotSpeedForBlock = MathUtils.random(5, 20);
    }
    private void rotDef() {
        rot1 = 0;
        rotSpeedForBlock = MathUtils.random(5, 20);
    }

    int BLOCK_COUNT_NOW = 0;
    int BLOCK_LEN = 0;
    float rot0 = 0;
    float rotSpeed;
    float rot1;
    float rotSpeedForBlock;
    int colorScheme = 0;
    public PlayState(GameStateManager gameStateMenager) {
        super(gameStateMenager);
        GameRunner.adMobFlag = false;
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("LEVEL", String.valueOf(lvl));
        FlurryAgent.logEvent("START_LEVEL", parameters);
        colorScheme = GameRunner.levels.levels.get(lvl).color_scheme;
        GameRunner.colors.setScheme(colorScheme);
        colorSize = GameRunner.levels.levels.get(lvl).level_colors;
        Block.speed0 = Block.speed = GameRunner.levels.levels.get(lvl).level_speed;
        rot0 = rot1 = GameRunner.levels.levels.get(lvl).level_rot;
        rotSpeed = GameRunner.levels.levels.get(lvl).level_rot_speed;

        camera.setToOrtho(false, GameRunner.WIDTH / 2, GameRunner.HEIGHT / 2);
        camera.rotate(rot0);
        cam_btn = new OrthographicCamera(GameRunner.WIDTH, GameRunner.HEIGHT);
        cam_btn.update();
        bg = new Background(cam_btn.position.x, cam_btn.position.y, colorScheme);
        BLOCK_LEN = GameRunner.levels.levels.get(lvl).level.length();

        tb = new SpriteBatch();

        player = new Player(200, 232, "player_p.png", 1);
        player.colorSize = colorSize;
        playerTail = new Array<Sprite>();
        for(int i = 0; i < 10; ++i) {
            playerTail.add(new Sprite(player.getSprite()));
            playerTail.get(playerTail.size-1).setCenter(player.getPosition().x - Constants.BLOCK_W/2 + i*20, player.getPosition().y);
            playerTail.get(playerTail.size-1).setScale(MathUtils.random(0.1f, 0.25f));
        }
        bgEffect = new Array<Sprite>();
        for(int i = 0; i < 70; ++i) {
            bgEffect.add(new Sprite(new Texture("s.png")));
            bgEffect.get(bgEffect.size-1).setCenter(cam_btn.position.x + 550  - Constants.BLOCK_W/2 - i*20, cam_btn.position.y + MathUtils.random(-290, 290));
            bgEffect.get(bgEffect.size-1).setScale(MathUtils.random(0.5f, 1.5f));
            bgEffect.get(bgEffect.size-1).setColor(1,1,1,MathUtils.random(0.5f, 1f));
        }
        coins = new Array<Coin>(30);
        blocks = new Queue<Block>(100);
        for (int i = 0; i < 15; ++i) {
            blocks.addLast(new BlockFloor(BLOCK_SPACING*i, Constants.Y0, Constants.B_FLOOR));
            blocks.last().setColor(Colors.GRAY);
        }

        pauseBtn = new Button("button/bar", cam_btn.position.x - 560, cam_btn.position.y + 300);

        reborn = false;
        //BOSS
        bul = new Array<BlockBullet>();
        GameRunner.playMusic = MathUtils.random(1, 4);
        GameRunner.updateMusic = true;
    }
    float time = 2;
    @Override
    protected void hendleInput() {
        boolean first = Gdx.input.isTouched(0);
        boolean second = Gdx.input.isTouched(1);
        boolean third = Gdx.input.isTouched(2);
        if(first && second && third) {
            gameStateMenager.push(new CheatState(gameStateMenager));
        }
        if(Gdx.input.isTouched()) {
            Vector3 vec = cam_btn.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            pauseBtn.updatePress(vec.x, vec.y);
        } else {
            if(pauseBtn.getIsPress()) {
                pauseBtn.setIsPress(false);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        time--;
                        if (time <= 1) {
                            time = 2;
                            gameStateMenager.push(new PauseState(gameStateMenager));
                        }
                    }
                }, 0.2f);
            }
        }
        if(Gdx.input.justTouched()) {
            Vector3 vec = cam_btn.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if (pauseBtn.setPress(vec.x, vec.y)) {
                return;
            }
            if(!START_LEVEL) {
                START_LEVEL = true;
                return;
            }
            player.changeColor();
        }
    }
    boolean onTime = false;
    @Override
    public void update(float delta) {
        GameRunner.adMobFlag = false;
        hendleInput();
        if(TIME_LEVEL) {
            if(!onTime) {
                onTime = !onTime;
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        timer--;
                        if (timer == 0) {
                            gameStateMenager.push(new GameOver(gameStateMenager));
                        }
                        onTime = !onTime;
                    }
                }, 1);
            }
        }
        if(reborn && doReborn) {//REBORN
            if(TIME_LEVEL)
                timer += 10;
            doReborn = false;
            for(int i = 0; i < 5; ++i) {
                blocks.removeFirst();
            }
            for (int i = 0; i < 15; ++i) {
                if(i==12) {
                    blocks.addFirst(new BlockJump(BLOCK_SPACING*(14-i), Constants.Y0, Constants.B_JUMP, 350));
                }
                blocks.addFirst(new BlockFloor(BLOCK_SPACING*(14-i), Constants.Y0, Constants.B_FLOOR));
                blocks.first().setColor(Colors.GRAY);
            }
            int i = 0;
            float _x = 0;
            for(Block b : blocks) {
                if(Math.abs(_x-b.getPos().x) >= 10 ) {
                    _x = b.getPos().x;
                    if(Math.abs(_x-b.getPos().x) >= 40)
                        i++;
                    if(Math.abs(_x-b.getPos().x) >= 80)
                        i++;
                    ++i;
                }
                b.setPos(i*BLOCK_SPACING, b.getPos().y);
            }
            player.setPositionX(200);
            player.setPositionY(232);
            player.setLife(true);
            camera.position.y = player.getPosition().y;
        }
        if(!doNotCollige)
            player.update(delta);
        if(doNotDie)
            player.isLife = true;
        if (player.getPosition().y <= -100) {
            HashMap<String, String> parameters = new HashMap<String, String>();
            parameters.put("LEVEL", String.valueOf(lvl));
            FlurryAgent.logEvent("LOSE_LEVEL", parameters);
            gameStateMenager.push(new GameOver(gameStateMenager));
            //gameStateMenager.set(new GameOver(gameStateMenager));
        }
        for(Sprite s : playerTail) {
            if(s.getX() <= player.getPosition().x - 220) {
                s.setCenter(player.getPosition().x-20, player.getPosition().y);
                s.setScale(MathUtils.random(0.1f, 0.25f));
                s.setRotation(MathUtils.random(0f, 359f));
            } else {
                s.setX(s.getX() - Block.speed*delta);
            }
        }
        for(Sprite s : bgEffect) {
            if(s.getX() <= cam_btn.position.x-700) {
                s.setCenter(cam_btn.position.x + 690, cam_btn.position.y + MathUtils.random(-290, 290));
                s.setScale(MathUtils.random(0.5f, 1.5f));
            } else {
                s.setX(s.getX() - (Block.speed/4)*delta);
            }
        }
        boolean flag = false;
        player.flag = true;
        for (Block b : blocks) {
            b.update(delta, player.getPosition().x);
            if(player.gun == true) {
                player.gun = false;
                bul.add(new BlockBullet(player.getPosition().x, player.getPosition().y, 99, 1, boss.getPos().x, boss.getPos().y));
            }
            if (player.getLife())
                flag = b.collide(player);
            if(flag) {
                break;
            }
        }
        if (flag) {
            HashMap<String, String> parameters = new HashMap<String, String>();
            parameters.put("LEVEL", String.valueOf(lvl));
            FlurryAgent.logEvent("WON_LEVEL", parameters);
            gameStateMenager.set(new WinState(gameStateMenager, lvl));
        }
        if(player.isLife)
            camera.position.y = player.getPosition().y;
        if(START_LEVEL) {
            while (blocks.size < BLOCKS_MAX_COUNT && BLOCK_COUNT_NOW < BLOCK_LEN) {
                char new_blocks = GameRunner.levels.levels.get(lvl).level.charAt(BLOCK_COUNT_NOW);
                BLOCK_COUNT_NOW++;
                switch (new_blocks) {
                    case '0':
                        if(BLOCK_COUNT_NOW == 1) {
                            addFloor((blocks.size-1)*BLOCK_SPACING);
                        } else {
                            addFloor(blocks.get(blocks.size - 1).getPos().x + Constants.BLOCK_W);
                        }
                        break;
                    case '1':
                        addNeedle(blocks.get(blocks.size - 1).getPos().x + Constants.BLOCK_W);
                        break;
                    case '2':
                        addBridge(blocks.get(blocks.size - 1).getPos().x + Constants.BLOCK_W);
                        break;
                    case '3':
                        addPlatform(blocks.get(blocks.size - 1).getPos().x + Constants.BLOCK_W);
                        addFloor(blocks.get(blocks.size - 1).getPos().x + Constants.BLOCK_W);
                        break;
                    case '4':
                        addBridgeDown(blocks.get(blocks.size - 1).getPos().x + Constants.BLOCK_W);
                        break;
                    case '5':
                        addNeedleDown(blocks.get(blocks.size - 1).getPos().x + Constants.BLOCK_W);
                        break;
                    case '6':
                        addNeedleUp(blocks.get(blocks.size - 1).getPos().x + Constants.BLOCK_W);
                        break;
                    case '7':
                        addCoinJump(blocks.get(blocks.size - 1).getPos().x + Constants.BLOCK_W);
                        break;
                    case '8':
                        addPlatform2(blocks.get(blocks.size - 1).getPos().x + Constants.BLOCK_W);
                        break;
                    case '9':
                        addBeam(blocks.get(blocks.size - 1).getPos().x + Constants.BLOCK_W);
                        break;
                    case 'q':
                        addHole(blocks.get(blocks.size - 1).getPos().x + Constants.BLOCK_W);
                        break;
                    case 'i':
                        addIceBridge(blocks.get(blocks.size - 1).getPos().x + Constants.BLOCK_W);
                        break;
                    case 'B':
                        addBoss(blocks.get(blocks.size - 1).getPos().x + Constants.BLOCK_W);
                        IS_BOSS = true;
                        break;
                    case 'r':
                        rotRight();
                        break;
                    case 'l':
                        rotLeft();
                        break;
                    case 'd':
                        rotDef();
                        break;
                    case 't':
                        addTiemr();
                        break;
                }
            }
            if(IS_BOSS) {
                if(heat) {
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            timeHeat--;
                            if (timeHeat == 0) {
                                heat = false;
                                timeHeat = 2;
                            }
                        }
                    }, 1);
                }
                if(boss.HP <= 0) {
                    IS_BOSS = false;
                }
                while (blocks.size < BLOCKS_MAX_COUNT) {
                    int g = MathUtils.random(0, 20);
                    if(g > 17) {
                        addGun(blocks.get(blocks.size - 1).getPos().x + Constants.BLOCK_W);
                    }
                    addFloor(blocks.get(blocks.size - 1).getPos().x + Constants.BLOCK_W);
                }
                boss.update(delta, player.getPosition().x);
                if(bul.size == 0) {
                    if(MathUtils.random(0, 100) < 15 && !heat) {
                        bul.add(new BlockBullet(boss.getPos().x, boss.getPos().y, 99, -1, player.getPosition().x-600, player.getPosition().y));
                        bul.get(bul.size-1).setColor(MathUtils.random(1,2));
                    }
                }
                for (BlockBullet b : bul) {
                    b.update(delta, player.getPosition().x);
                    if(b.getLife() && b.collide(player)) {
                        heat = true;
                        if(boss.HP < boss.HP0) {
                            boss.HP += 5;
                        } else {
                            player.setLife(false);
                            player.jump(300);
                        }
                    }
                    if(b.getLife() && b.collideBoss(boss.getBounds())) {
                        boss.HP -= 5;
                    }
                }
                for (int i = 0; i < bul.size; ++i) {
                    BlockBullet r = bul.get(i);
                    if (!r.isLife) {
                        bul.get(i).dispose();
                        bul.removeIndex(i);
                    }
                }
            }
            if (BLOCK_COUNT_NOW == BLOCK_LEN && IS_BOSS == false) {
                BLOCK_COUNT_NOW++;
                addFinish(blocks.get(blocks.size - 1).getPos().x + Constants.BLOCK_W);
            }

            if (player.flag) {
                player.onFloor(Constants.GROUND);
                player.on_floor = false;
            }
            for (int i = 0; i < coins.size; ++i) {
                Coin c = coins.get(i);
                c.update(delta, player.getPosition().x);
                if (c.collide(player.getBounds()) && c.life) {
                    if (c.TYPE == 0) {
                        GameRunner.now_coins++;
                    } else if(c.TYPE == 1) {
                        GameRunner.now_metal++;
                    } else if(c.TYPE == 2) {
                        timer += 5;
                        CAN_ADD_TIMER = true;
                    }
                    c.life = false;
                }
            }
            for (int i = 0; i < coins.size; ++i) {
                Coin c = coins.get(i);
                if(c.TYPE == 2) {
                    System.out.print(""+c.getPos().x);
                }
                if (camera.position.x - (camera.viewportWidth / 2) > c.getPos().x + c.getPos().x + 64) {
                    coins.get(i).dispose();
                    coins.removeIndex(i);
                }
            }
            for (int i = 0; i < blocks.size; ++i) {
                Block r = blocks.get(i);
                if (camera.position.x - (camera.viewportWidth / 2) > r.getPos().x + r.getPos().x + 64) {
                    //if (r.TYPE == Constants.B_FLOOR || r.TYPE == Constants.B_JUMP) {
                    //    GameRunner.score++;
                    //}
                    blocks.get(i).dispose();
                    blocks.removeIndex(i);
                }
            }
        } else {
            for (Block b : blocks) {
                if (camera.position.x - (camera.viewportWidth / 2) > b.getPos().x + b.getPos().x + 64) {
                    b.setPos(blocks.size * BLOCK_SPACING + b.getPos().x, Constants.Y0);
                }
            }
        }
        float rot = rotSpeed*delta;
        if(rot0 != rot1) {
            if(rot0 < rot1) {
                rot = rotSpeedForBlock * delta;
                rot0 += rot;
                if(rot0 >= rot1) rot0 = rot1;
            } else if(rot0 > rot1) {
                rot = -rotSpeedForBlock * delta;
                rot0 += rot;
                if(rot0 <= rot1) rot0 = rot1;
            }
        }

        camera.rotate(rot);
        camera.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tb.setProjectionMatrix(cam_btn.combined);
        tb.begin();
        bg.getBgSprite().draw(tb);
        for(Sprite sp : bgEffect) {
            sp.draw(tb);
        }
        tb.end();
        sb.setProjectionMatrix(camera.combined);
        sb.begin();

        for(Sprite sp : playerTail) {
            if(player.color == 1)
                sp.setColor(GameRunner.colors.blue);
            else if (player.color == 2)
                sp.setColor(GameRunner.colors.red);
            else
                sp.setColor(GameRunner.colors.green);
            sp.draw(sb);
        }
        Sprite sp = player.getSprite();
        //sp.setRotation(player.getRot());
        //sp.setCenter(player.getPosition().x, player.getPosition().y + 2);
        sp.draw(sb);

        for (Block b : blocks) {
            b.getSprite(player.color).draw(sb);
        }
        for (Coin c : coins) {
            if (c.life) {
                c.getSprite().draw(sb);
            }
        }

        if(IS_BOSS) {
            for (BlockBullet b : bul) {
                b.getSprite(player.color).draw(sb);
            }
            boss.getSprite(player.color).draw(sb);
            boss.getHpSprite().draw(sb);
            boss.getHpBarSprite().draw(sb);
        }
        sb.end();

        tb.setProjectionMatrix(cam_btn.combined);
        tb.begin();
        helpColorChange.setColor(GameRunner.colors.blue.r, GameRunner.colors.blue.g, GameRunner.colors.blue.b, 1);
        helpColorChange.setCenter(cam_btn.position.x + 530, cam_btn.position.y + 320);
        if(player.color == 1) {
            helpColorChange.setScale(1f);
        } else {
            helpColorChange.setScale(0.7f);
        }
        helpColorChange.draw(tb);
        helpColorChange.setColor(GameRunner.colors.red.r, GameRunner.colors.red.g, GameRunner.colors.red.b, 1);
        helpColorChange.setCenter(cam_btn.position.x + 560, cam_btn.position.y + 320);
        if(player.color == 2) {
            helpColorChange.setScale(1f);
        } else {
            helpColorChange.setScale(0.7f);
        }
        helpColorChange.draw(tb);
        if(colorSize == 3) {
            helpColorChange.setColor(GameRunner.colors.green.r, GameRunner.colors.green.g, GameRunner.colors.green.b, 1);
            helpColorChange.setCenter(cam_btn.position.x + 590, cam_btn.position.y + 320);
            if(player.color == 3) {
                helpColorChange.setScale(1f);
            } else {
                helpColorChange.setScale(0.7f);
            }
            helpColorChange.draw(tb);
        }

        pauseBtn.getSprite().draw(tb);
        if(!START_LEVEL) {
            GameRunner.font.draw(tb, "TAP TO START", cam_btn.position.x - 100, cam_btn.position.y);
        }
        if(TIME_LEVEL) {
            GameRunner.font.draw(tb, ""+timer, cam_btn.position.x, cam_btn.position.y+50);
        }
        //GameRunner.font.draw(tb, "COINS: " + GameRunner.now_coins + "  METAL: " + GameRunner.now_metal, cam_btn.position.x - 520, cam_btn.position.y - 320);
        tb.end();
    }

    @Override
    public void dispose() {
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("LEVEL", String.valueOf(lvl));
        FlurryAgent.logEvent("STOP_LEVEL", parameters);
        player.dispose();
        pauseBtn.dispose();
        for(Block b : blocks) {
            b.dispose();
        }
        for (Coin c : coins) {
            c.dispose();
        }
    }
}
