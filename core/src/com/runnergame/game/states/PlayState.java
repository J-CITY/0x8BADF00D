package com.runnergame.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
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

public class PlayState extends State {
    public static int lvl=0;
    public static final int BLOCK_SPACING = 64;
    public static final int BLOCKS_MAX_COUNT = 20;
    //private Color bgColor;
    private boolean GO = false;
    private Array<Block> blocks;
    private Array<Coin> coins;

    private Player player;
    private Array<Sprite> playerTail;

    private Array<Sprite> bgEffect;
    Button pauseBtn;
    Background bg;
    private boolean IS_BOSS = false;

    private SpriteBatch tb;
    OrthographicCamera cam_btn;
    //BOSS
    BlockBoss boss;
    Array<BlockBullet> bul;

    private int colorSize;

    private void addBeam(float x) {
        int size = MathUtils.random(5, 10);
        int color = MathUtils.random(1, colorSize);
        int len;
        for(int i = 0; i < size; ++i) {
            len = MathUtils.random(3, 7);
            for(int j = 0; j < len; ++j) {
                if(j == len-1) {
                    blocks.add(new BlockBeam(x, Constants.Y0+Constants.BLOCK_H/2+16, Constants.B_FLOOR));
                    blocks.get(blocks.size-1).setColor(color);
                    color = MathUtils.random(1, colorSize);
                }
                blocks.add(new BlockFloor(x, Constants.Y0, Constants.B_FLOOR));
                blocks.get(blocks.size-1).setColor(0);
                x += 64;
            }
        }
    }

    private void addIceBridge(float x) {
        int c = MathUtils.random(1, colorSize);
        blocks.add(new BlockBoost(x + BLOCK_SPACING*0, Constants.Y0, Constants.B_FLOOR, 200));
        blocks.get(blocks.size-1).setColor(c);
        x += BLOCK_SPACING;
        //for (int i = 0; i < 2; ++i) {
        blocks.add(new BlockIce(x + BLOCK_SPACING, Constants.Y0, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(Colors.GRAY);
        blocks.add(new BlockIce(x + BLOCK_SPACING*4, Constants.Y0, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(Colors.GRAY);
        //}
    }

    private void addHole(float x) {
        int color = MathUtils.random(1, colorSize);
        blocks.add(new BlockJump(x, Constants.Y0, Constants.B_FLOOR, 350));
        blocks.get(blocks.size-1).setColor(color);
        x += BLOCK_SPACING*3;
        for (int i = 0; i < 4; ++i) {
            blocks.add(new BlockFloor(x + BLOCK_SPACING*i, Constants.Y0, Constants.B_FLOOR));
            blocks.get(blocks.size-1).setColor(Colors.GRAY);
        }
    }

    private void addPlatform2(float x) {
        int len = MathUtils.random(5, 10);
        int platform_color1 = MathUtils.random(1, colorSize);
        int platform_color2 = MathUtils.random(1, colorSize);
        int _floor = 0;
        float __x;

        blocks.add(new BlockFloor(x + BLOCK_SPACING*0, Constants.Y0, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockFloor(x + BLOCK_SPACING*1, Constants.Y0, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockJump(x + BLOCK_SPACING*2, Constants.Y0, Constants.B_JUMP, 500));
        blocks.get(blocks.size-1).setColor(platform_color1);
        __x = blocks.get(blocks.size-1).getPos().x+Constants.BLOCK_W+192;
        //blocks.add(new BlockJump(x + BLOCK_SPACING*4, Constants.Y0+64, Constants.B_JUMP));
        //blocks.get(blocks.size-1).setColor(platform_color);
        blocks.add(new BlockFloor(x + BLOCK_SPACING*3, Constants.Y0, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockFloor(x + BLOCK_SPACING*4, Constants.Y0, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockFloor(x + BLOCK_SPACING*5, Constants.Y0, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(0);
        _floor = 2;
        for (int i = 0; i < len; ++i) {
            if (MathUtils.random(0, 20) >= 16) {
                coins.add(new Coin(__x, Constants.GROUND + 32*_floor, 0));
            }
            blocks.add(new BlockFloor(__x, Constants.Y0 + 64*_floor, Constants.B_FLOOR));
            blocks.get(blocks.size-1).setColor(platform_color1);
            blocks.add(new BlockFloor(__x, Constants.Y0, Constants.B_FLOOR));
            blocks.get(blocks.size-1).setColor(platform_color2);
            __x += 64;
        }
    }

    private void addPlatform(float x) {
        int platform_count = MathUtils.random(3, 8);
        int platform_color;
        int _floor = 0;
        float __x;

        blocks.add(new BlockFloor(x + BLOCK_SPACING*0, Constants.Y0, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockFloor(x + BLOCK_SPACING*1, Constants.Y0, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(0);

        __x = blocks.get(blocks.size-1).getPos().x+Constants.BLOCK_W;

        for (int i = 0; i < platform_count; ++i) {
            platform_color = MathUtils.random(1, colorSize);
            if (i == 0) {
                blocks.add(new BlockJump(__x, Constants.Y0, Constants.B_JUMP, 335));
                blocks.get(blocks.size-1).setColor(platform_color);
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

            blocks.add(new BlockJump(__x, Constants.Y0 + 32*_floor, 4, 335));
            blocks.get(blocks.size-1).setColor(platform_color);
            __x += 64;
            blocks.add(new BlockJump(__x, Constants.Y0 + 32*_floor, 4, 335));
            blocks.get(blocks.size-1).setColor(platform_color);
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
            blocks.add(new BlockFloor(x + BLOCK_SPACING*i, Constants.Y0, Constants.B_FLOOR));
            if(i == 0 || i == 1 || i == size-1 || i == size-2) {
                blocks.get(blocks.size-1).setColor(0);
            } else {
                blocks.get(blocks.size-1).setColor(bridge_color);
            }
        }
    }

    private void addNeedle(float x) {
        int needle_color = MathUtils.random(0, colorSize);
        int jump_color = MathUtils.random(1, colorSize);
        int size = MathUtils.random(5, 10);

        for (int i = 0; i < size; ++i) {
            if (i == 2 && needle_color == 0) {
                blocks.add(new BlockJump(x + BLOCK_SPACING*i, Constants.Y0, Constants.B_JUMP, 350));
                blocks.get(blocks.size-1).setColor(jump_color);
                continue;
            }
            if (i == 4) {
                blocks.add(new BlockNeedle(x + BLOCK_SPACING*i, Constants.Y0 + Constants.BLOCK_H/2 + 16, Constants.B_NEEDLE));
                blocks.get(blocks.size-1).setColor(needle_color);
            }
            blocks.add(new BlockFloor(x + BLOCK_SPACING*i, Constants.Y0, 0));
            blocks.get(blocks.size-1).setColor(0);
        }
    }

    private void addNeedleUp(float x) {
        int needle_color = MathUtils.random(1, colorSize);
        int size = MathUtils.random(5, 10);

        blocks.add(new BlockFloor(x + BLOCK_SPACING*0, Constants.Y0, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockFloor(x + BLOCK_SPACING*1, Constants.Y0, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(0);
        if (MathUtils.random(0, 20) == 20) {
            coins.add(new Coin(x + BLOCK_SPACING, Constants.GROUND, 0));
        }
        for (int i = 2; i < size-2; ++i) {
            blocks.add(new BlockNeedle(x + BLOCK_SPACING*i, Constants.Y0 + Constants.BLOCK_H/2 + 16, Constants.B_NEEDLE));
            blocks.get(blocks.size-1).setColor(needle_color);
            blocks.add(new BlockFloor(x + BLOCK_SPACING*i, Constants.Y0, Constants.B_FLOOR));
            blocks.get(blocks.size-1).setColor(0);
        }
        blocks.add(new BlockFloor(x + BLOCK_SPACING*(size-2), Constants.Y0, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockFloor(x + BLOCK_SPACING*(size-1), Constants.Y0, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(0);
    }

    private void addNeedleDown(float x) {
        int needle_color = MathUtils.random(0, colorSize);
        int jump_color = MathUtils.random(1, colorSize);
        if (MathUtils.random(0, 20) == 20) {
            coins.add(new Coin(x + BLOCK_SPACING, Constants.GROUND, 0));
        }
        blocks.add(new BlockFloor(x + BLOCK_SPACING*0, Constants.Y0, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockFloor(x + BLOCK_SPACING*1, Constants.Y0, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockJump(x + BLOCK_SPACING*2, Constants.Y0, Constants.B_JUMP, 350));
        blocks.get(blocks.size-1).setColor(jump_color);
        for (int i = 3; i < 5; ++i) {
            blocks.add(new BlockNeedle(x + BLOCK_SPACING*i, Constants.Y0, Constants.B_NEEDLE));
            blocks.get(blocks.size-1).setColor(needle_color);
        }
        blocks.add(new BlockFloor(x + BLOCK_SPACING*5, Constants.Y0, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockFloor(x + BLOCK_SPACING*6, Constants.Y0, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(0);
    }

    private void addBridgeDown(float x) {
        int needle_color = MathUtils.random(1, colorSize);
        int jump_color = MathUtils.random(1, colorSize);
        int size = MathUtils.random(5, 10);

        blocks.add(new BlockFloor(x + BLOCK_SPACING*0, Constants.Y0, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockFloor(x + BLOCK_SPACING*1, Constants.Y0, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(0);
        if (MathUtils.random(0, 20) == 20) {
            coins.add(new Coin(x + BLOCK_SPACING, Constants.GROUND, 0));
        }
        for (int i = 2; i < size+2; ++i) {
            blocks.add(new BlockNeedle(x + BLOCK_SPACING*i, Constants.Y0-Constants.BLOCK_W+Constants.BLOCK_H/2+16, Constants.B_NEEDLE));
            blocks.get(blocks.size-1).setColor(needle_color);
            blocks.add(new BlockFloor(x + BLOCK_SPACING*i, Constants.Y0-Constants.BLOCK_W, Constants.B_FLOOR));
            blocks.get(blocks.size-1).setColor(0);
        }
        blocks.add(new BlockFloor(x + BLOCK_SPACING*(size+2), Constants.Y0-Constants.BLOCK_W, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockFloor(x + BLOCK_SPACING*(size+3), Constants.Y0-Constants.BLOCK_W, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockFloor(x + BLOCK_SPACING*(size+4), Constants.Y0-Constants.BLOCK_W, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockJump(x + BLOCK_SPACING*(size+5), Constants.Y0-Constants.BLOCK_W, Constants.B_JUMP, 350));
        blocks.get(blocks.size-1).setColor(jump_color);
        blocks.add(new BlockFloor(x + BLOCK_SPACING*(size+6), Constants.Y0-Constants.BLOCK_W, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockFloor(x + BLOCK_SPACING*(size+7), Constants.Y0-Constants.BLOCK_W, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockNeedle(x + BLOCK_SPACING*(size+7), Constants.Y0-Constants.BLOCK_W+Constants.BLOCK_H/2+16, Constants.B_NEEDLE));
        blocks.get(blocks.size-1).setColor(0);

        blocks.add(new BlockFloor(x + BLOCK_SPACING*(size+8), Constants.Y0, 0));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockFloor(x + BLOCK_SPACING*(size+9), Constants.Y0, 0));
        blocks.get(blocks.size-1).setColor(0);

    }

    private void addFloor(float x) {
        int size = MathUtils.random(3, 6);

        for (int i = 0; i < size; ++i) {
            /*int addCoin = MathUtils.random(0, 20);
            if(addCoin > 15 && addCoin < 20) {
                coins.add(new Coin(x + BLOCK_SPACING*i, 163, 0));
            } else if (addCoin == 20) {
                coins.add(new Coin(x + BLOCK_SPACING*i, 163, 1));
            }*/
            blocks.add(new BlockFloor(x + BLOCK_SPACING*i, Constants.Y0, Constants.B_FLOOR));
            blocks.get(blocks.size-1).setColor(Colors.GRAY);
        }
    }

    private void addBoss(float x) {
        blocks.add(new BlockFloor(x + BLOCK_SPACING*0, Constants.Y0, Constants.B_FLOOR));
        boss = new BlockBoss(x + BLOCK_SPACING*0, Constants.Y0+Constants.BLOCK_H/2+18, Constants.B_FLOOR, (lvl/10)*10 + 15);//25
    }

    private void addGun(float x) {
        int color = MathUtils.random(1, colorSize);
        blocks.add(new BlockFloor(x + BLOCK_SPACING*0, Constants.Y0, Constants.B_FLOOR));
        blocks.add(new BlockJump(x + BLOCK_SPACING*1, Constants.Y0, Constants.B_FLOOR, 350));
        blocks.get(blocks.size-1).setColor(color);
        blocks.add(new BlockGun(x + BLOCK_SPACING*2, Constants.Y0 + 100, Constants.B_FLOOR));//163
        blocks.add(new BlockFloor(x + BLOCK_SPACING*2, Constants.Y0, Constants.B_FLOOR));
        blocks.add(new BlockFloor(x + BLOCK_SPACING*3, Constants.Y0, Constants.B_FLOOR));
    }

    private void addCoinJump(float x) {
        int jump_color = MathUtils.random(1, colorSize);

        blocks.add(new BlockJump(x + BLOCK_SPACING*0, Constants.Y0, Constants.B_JUMP, 350));
        blocks.get(blocks.size-1).setColor(jump_color);
        blocks.add(new BlockFloor(x + BLOCK_SPACING*1, Constants.Y0, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(0);
        if (MathUtils.random(0, 20) == 20) {
            coins.add(new Coin(x + BLOCK_SPACING*1, 227, 0));
        }
    }

    private void addFinish(float x) {
        blocks.add(new BlockFloor(x + BLOCK_SPACING*0, Constants.Y0, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockFloor(x + BLOCK_SPACING*1, Constants.Y0, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockFinish(x + BLOCK_SPACING*2, Constants.Y0 + Constants.BLOCK_H/2 + Constants.BLOCK_W/2, Constants.B_FINISH));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockFloor(x + BLOCK_SPACING*2, Constants.Y0, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(0);
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
        colorScheme = GameRunner.levels.levels.get(lvl).color_scheme;
        GameRunner.colors.setScheme(colorScheme);
        //bgColor = GameRunner.colors.getBgColor();
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
        if(GameRunner.reborn) {
            GameRunner.reborn = false;
            BLOCK_COUNT_NOW = BLOCK_LEN / 2;
        } else {
            GameRunner.score = 0;
        }


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
        blocks = new Array<Block>(100);
        for (int i = 0; i < 10; ++i) {
            blocks.add(new BlockFloor(BLOCK_SPACING*i, Constants.Y0, Constants.B_FLOOR));
            blocks.get(blocks.size-1).setColor(Colors.GRAY);
        }

        pauseBtn = new Button("Pause.png", cam_btn.position.x - 560, cam_btn.position.y + 300, 1, 1);
        pauseBtn.setScale(0.5f);

        //BOSS
        bul = new Array<BlockBullet>();
    }

    @Override
    protected void hendleInput() {
        if(Gdx.input.justTouched()) {
            if(!GO) {
                GO = true;
            }
            Vector3 vec = cam_btn.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if(pauseBtn.collide(vec.x, vec.y)) {
                PauseState.whatGame = 0;
                gameStateMenager.push(new PauseState(gameStateMenager));
            }
            player.changeColor();
        }
    }

    @Override
    public void update(float delta) {
        hendleInput();
        player.update(delta);
        //player.isLife = true;
        if (player.getPosition().y <= -100) {
            gameStateMenager.set(new PreGameOver(gameStateMenager));
        }
        for(Sprite s : playerTail) {
            if(s.getX() <= player.getPosition().x - 220) {
                s.setCenter(player.getPosition().x-20, player.getPosition().y);
                s.setScale(MathUtils.random(0.1f, 0.25f));
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
        //if(!player.getLife()) {
        //    gameStateMenager.set(new PreGameOver(gameStateMenager));
        //}
        boolean flag = false;
        player.flag = true;
        for (int i = 0; i < blocks.size; ++i) {
            Block f = blocks.get(i);
            f.update(delta, player.getPosition().x);
            if(player.gun == true) {
                player.gun = false;
                bul.add(new BlockBullet(player.getPosition().x, player.getPosition().y, 99, 1, boss.getPos().x, boss.getPos().y));
            }
            if (player.getLife())
                flag = f.collide(player);
            if (flag) {
                gameStateMenager.set(new WinState(gameStateMenager, lvl));
            }
        }
        if(player.isLife)
            camera.position.y = player.getPosition().y;
        //camera.position.y = blocks.get(blocks.size-1).getPos().y;
        if(GO) {
            while (blocks.size < BLOCKS_MAX_COUNT && BLOCK_COUNT_NOW < BLOCK_LEN) {
                char new_blocks = GameRunner.levels.levels.get(lvl).level.charAt(BLOCK_COUNT_NOW);
                BLOCK_COUNT_NOW++;
                Constants.Y0 = blocks.get(blocks.size - 1).getPos().y;
                //System.out.print(Constants.Y0+"\n");
                switch (new_blocks) {
                    case '0':
                        if(BLOCK_COUNT_NOW == 1) {
                            addFloor(blocks.size*BLOCK_SPACING);
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
                }
            }
            if(IS_BOSS) {
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
                    if(MathUtils.random(0, 100) < 15) {
                        bul.add(new BlockBullet(boss.getPos().x, boss.getPos().y, 99, -1, player.getPosition().x-600, player.getPosition().y));
                        bul.get(bul.size-1).setColor(MathUtils.random(1,2));
                    }
                }
                for (BlockBullet b : bul) {
                    b.update(delta, player.getPosition().x);
                    b.collide(player);
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
                        GameRunner.new_coins++;
                    } else {
                        GameRunner.new_stars++;
                    }
                    c.life = false;
                }
            }
            for (int i = 0; i < coins.size; ++i) {
                Coin c = coins.get(i);
                if (camera.position.x - (camera.viewportWidth / 2) > c.getPos().x + c.getPos().x + 64) {
                    coins.get(i).dispose();
                    coins.removeIndex(i);
                }
            }
            for (int i = 0; i < blocks.size; ++i) {
                Block r = blocks.get(i);
                if (camera.position.x - (camera.viewportWidth / 2) > r.getPos().x + r.getPos().x + 64) {
                    if (r.TYPE == Constants.B_FLOOR || r.TYPE == Constants.B_JUMP) {
                        GameRunner.score++;
                    }
                    blocks.get(i).dispose();
                    blocks.removeIndex(i);
                }
            }
        } else {
            for (int i = 0; i < blocks.size; ++i) {
                Block r = blocks.get(i);
                if (camera.position.x - (camera.viewportWidth / 2) > r.getPos().x + r.getPos().x + 64) {
                    r.setPos(blocks.size * BLOCK_SPACING + r.getPos().x, Constants.Y0);
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
        //Gdx.gl.glClearColor(bgColor.r,
         //                   bgColor.g,
         //                   bgColor.b, 1);
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
        //Sprite s = bg.getBgSprite();
        //s.setColor(GameRunner.R, GameRunner.G, GameRunner.B, 1);
        //s.setColor(0.08f, 0.29f, 0.29f, 1);
        //s.draw(sb);

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
        sp.setRotation(player.getRot());
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
        pauseBtn.getSprite().draw(tb);
        if(!GO) {
            GameRunner.font.draw(tb, "TAP TO START", cam_btn.position.x - 100, cam_btn.position.y);
        }
        GameRunner.font.draw(tb, "COINS: " + GameRunner.new_coins + " STARS: " + GameRunner.new_stars, cam_btn.position.x - 560, cam_btn.position.y - 320);
        tb.end();
    }

    @Override
    public void dispose() {
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
