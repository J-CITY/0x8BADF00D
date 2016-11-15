package com.runnergame.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.runnergame.game.sprites.Blocks.BlockFinish;
import com.runnergame.game.sprites.Blocks.BlockFloor;
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
    private boolean GO = false;
    private Array<Block> blocks;
    private Array<Coin> coins;

    private Player player;
    Button pauseBtn;
    Background bg;

    private SpriteBatch tb;
    OrthographicCamera cam_btn;

    private void addBeam(float x) {
        int size = MathUtils.random(5, 10);
        int color = MathUtils.random(1, 2);
        int len;
        for(int i = 0; i < size; ++i) {
            len = MathUtils.random(3, 7);
            for(int j = 0; j < len; ++j) {
                if(j == len-1) {
                    blocks.add(new BlockBeam(x, Constants.Y0+64, Constants.B_FLOOR));
                    blocks.get(blocks.size-1).setColor(color);
                    color = MathUtils.random(1, 2);
                }
                blocks.add(new BlockFloor(x, Constants.Y0, Constants.B_FLOOR));
                blocks.get(blocks.size-1).setColor(0);
                x += 64;
            }
        }
    }

    private void addIceBridge(float x) {
        int c = MathUtils.random(1, 2);
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
        int color = MathUtils.random(1, 2);
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
        int platform_color = MathUtils.random(1, 2);
        int _floor = 0;
        float __x;

        blocks.add(new BlockFloor(x + BLOCK_SPACING*0, Constants.Y0, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockFloor(x + BLOCK_SPACING*1, Constants.Y0, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockJump(x + BLOCK_SPACING*2, Constants.Y0, Constants.B_JUMP, 500));
        blocks.get(blocks.size-1).setColor(platform_color);
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
            coins.add(new Coin(__x, Constants.GROUND + 64*(_floor), 0));
            blocks.add(new BlockFloor(__x, Constants.Y0 + 64*_floor, Constants.B_FLOOR));
            blocks.get(blocks.size-1).setColor(platform_color);
            blocks.add(new BlockFloor(__x, Constants.Y0, Constants.B_FLOOR));
            blocks.get(blocks.size-1).setColor(platform_color);
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
            platform_color = MathUtils.random(1, 2);
            if (i == 0) {
                blocks.add(new BlockJump(__x, Constants.Y0, Constants.B_JUMP, 330));
                blocks.get(blocks.size-1).setColor(platform_color);
                __x += 230;
                continue;
            }
            int k = MathUtils.random(0, 2);
            if (k == 0 && _floor > 0) {
                _floor -= 1;
            } else if(k == 1 && _floor < 3) {
                _floor += 1;
            }
            int addCoin = MathUtils.random(0, 20);
            if(addCoin > 15 && addCoin < 20) {
                coins.add(new Coin(__x, Constants.GROUND + 32*_floor, 0));
            } else if (addCoin == 20) {
                coins.add(new Coin(__x, Constants.GROUND + 32*_floor, 1));
            }

            blocks.add(new BlockJump(__x, Constants.Y0 + 32*_floor, 4, 330));
            blocks.get(blocks.size-1).setColor(platform_color);
            __x += 64;
            blocks.add(new BlockJump(__x, Constants.Y0 + 32*_floor, 4, 330));
            blocks.get(blocks.size-1).setColor(platform_color);
            __x += 230;
        }
    }

    private void addBridge(float x) {
        int bridge_color = MathUtils.random(1, 2);
        int size = MathUtils.random(8, 16);

        for (int i = 0; i < size; ++i) {
            int addCoin = MathUtils.random(0, 20);
            if(addCoin > 15 && addCoin < 20) {
                coins.add(new Coin(x + BLOCK_SPACING*i, Constants.GROUND, 0));
            } else if (addCoin == 20) {
                coins.add(new Coin(x + BLOCK_SPACING*i, Constants.GROUND, 1));
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
        int needle_color = MathUtils.random(0, 2);
        int jump_color = MathUtils.random(1, 2);
        int size = MathUtils.random(5, 10);

        for (int i = 0; i < size; ++i) {
            if (i == 2 && needle_color == 0) {
                blocks.add(new BlockJump(x + BLOCK_SPACING*i, Constants.Y0, Constants.B_JUMP, 350));
                blocks.get(blocks.size-1).setColor(jump_color);
                continue;
            }
            if (i == 4) {
                blocks.add(new BlockNeedle(x + BLOCK_SPACING*i, 148, Constants.B_NEEDLE));
                blocks.get(blocks.size-1).setColor(needle_color);
            }
            blocks.add(new BlockFloor(x + BLOCK_SPACING*i, Constants.Y0, 0));
            blocks.get(blocks.size-1).setColor(0);
        }
    }

    private void addNeedleUp(float x) {
        int needle_color = MathUtils.random(1, 2);
        int size = MathUtils.random(5, 10);

        blocks.add(new BlockFloor(x + BLOCK_SPACING*0, Constants.Y0, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockFloor(x + BLOCK_SPACING*1, Constants.Y0, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(0);
        int addCoin = MathUtils.random(0, 20);
        if(addCoin > 15 && addCoin < 20) {
            coins.add(new Coin(x + BLOCK_SPACING*0, Constants.GROUND, 0));
        } else if (addCoin == 20) {
            coins.add(new Coin(x + BLOCK_SPACING*1, Constants.GROUND, 1));
        }
        for (int i = 2; i < size-2; ++i) {
            blocks.add(new BlockNeedle(x + BLOCK_SPACING*i, 148, Constants.B_NEEDLE));
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
        int needle_color = MathUtils.random(0, 2);
        int jump_color = MathUtils.random(1, 2);
        int addCoin = MathUtils.random(0, 20);
        if(addCoin > 15 && addCoin < 20) {
            coins.add(new Coin(x + BLOCK_SPACING*0, Constants.GROUND, 0));
        } else if (addCoin == 20) {
            coins.add(new Coin(x + BLOCK_SPACING*1, Constants.GROUND, 1));
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
        int needle_color = MathUtils.random(1, 2);
        int jump_color = MathUtils.random(1, 2);
        int size = MathUtils.random(5, 10);

        blocks.add(new BlockFloor(x + BLOCK_SPACING*0, Constants.Y0, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockFloor(x + BLOCK_SPACING*1, Constants.Y0, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(0);
        int addCoin = MathUtils.random(0, 20);
        if(addCoin > 15 && addCoin < 20) {
            coins.add(new Coin(x + BLOCK_SPACING*0, Constants.GROUND, 0));
        } else if (addCoin == 20) {
            coins.add(new Coin(x + BLOCK_SPACING*1, Constants.GROUND, 1));
        }
        for (int i = 2; i < size+2; ++i) {
            blocks.add(new BlockNeedle(x + BLOCK_SPACING*i, Constants.Y0-16, Constants.B_NEEDLE));
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
        blocks.add(new BlockNeedle(x + BLOCK_SPACING*(size+7), Constants.Y0-16, Constants.B_NEEDLE));
        blocks.get(blocks.size-1).setColor(0);

        blocks.add(new BlockFloor(x + BLOCK_SPACING*(size+8), 100, 0));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockFloor(x + BLOCK_SPACING*(size+9), 100, 0));
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

    private void addCoinJump(float x) {
        int jump_color = MathUtils.random(1, 2);

        blocks.add(new BlockJump(x + BLOCK_SPACING*0, Constants.Y0, Constants.B_JUMP, 350));
        blocks.get(blocks.size-1).setColor(jump_color);
        blocks.add(new BlockFloor(x + BLOCK_SPACING*1, Constants.Y0, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(0);
        int addCoin = MathUtils.random(0, 20);
        if(addCoin > 15 && addCoin < 20) {
            coins.add(new Coin(x + BLOCK_SPACING*1, 227, 0));
        } else if (addCoin == 20) {
            coins.add(new Coin(x + BLOCK_SPACING*1, 227, 1));
        }
    }

    private void addFinish(float x) {
        blocks.add(new BlockFloor(x + BLOCK_SPACING*0, Constants.Y0, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockFloor(x + BLOCK_SPACING*1, Constants.Y0, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockFloor(x + BLOCK_SPACING*2, Constants.Y0, Constants.B_FLOOR));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockFinish(x + BLOCK_SPACING*2, 148, Constants.B_FINISH));
        blocks.get(blocks.size-1).setColor(0);

    }

    int BLOCK_COUNT_NOW = 0;
    int BLOCK_LEN = 0;
    public PlayState(GameStateManager gameStateMenager) {
        super(gameStateMenager);
        camera.setToOrtho(false, GameRunner.WIDTH / 2, GameRunner.HEIGHT / 2);

        cam_btn = new OrthographicCamera(GameRunner.WIDTH, GameRunner.HEIGHT);
        cam_btn.update();
        bg = new Background(camera.position.x, camera.position.y);
        BLOCK_LEN = GameRunner.levels.levels.get(lvl).length();
        if(GameRunner.reborn) {
            GameRunner.reborn = false;
            BLOCK_COUNT_NOW = BLOCK_LEN / 2;
        } else {
            GameRunner.score = 0;
        }


        tb = new SpriteBatch();

        player = new Player(200, 232);
        coins = new Array<Coin>();
        blocks = new Array<Block>();
        addFloor(0);
        for (int i = 0; i < 3; ++i) {
            addFloor(blocks.get(blocks.size-1).getPos().x + Constants.BLOCK_W);
        }

        pauseBtn = new Button("Pause.png", cam_btn.position.x - 560, cam_btn.position.y + 300, 1, 1);
        pauseBtn.setScale(0.5f);
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
        if (player.getPosition().y <= -100) {
            gameStateMenager.set(new PreGameOver(gameStateMenager));
        }
        //if(!player.getLife()) {
        //    gameStateMenager.set(new PreGameOver(gameStateMenager));
        //}
        boolean flag = false;
        player.flag = true;
        for (int i = 0; i < blocks.size; ++i) {
            Block f = blocks.get(i);
            f.update(delta, player.getPosition().x);
            if (player.getLife())
                flag = f.collide(player);
            if (flag) {
                gameStateMenager.set(new WinState(gameStateMenager, lvl));
            }
        }

        camera.position.y = player.getPosition().y;
        if(GO) {
            while (blocks.size < BLOCKS_MAX_COUNT && BLOCK_COUNT_NOW < BLOCK_LEN) {
                char new_blocks = GameRunner.levels.levels.get(lvl).charAt(BLOCK_COUNT_NOW);
                BLOCK_COUNT_NOW++;
                Constants.Y0 = blocks.get(blocks.size - 1).getPos().y;
                //System.out.print(Constants.Y0+"\n");
                switch (new_blocks) {
                    case '0':
                        addFloor(blocks.get(blocks.size - 1).getPos().x + Constants.BLOCK_W);
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
                }
            }
            if (BLOCK_COUNT_NOW == BLOCK_LEN) {
                BLOCK_COUNT_NOW++;
                addFinish(blocks.get(blocks.size - 1).getPos().x + Constants.BLOCK_W);
            }


            BlockBoost.updateVel();
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
        camera.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        Sprite s = bg.getBgSprite();
        s.setColor(GameRunner.R, GameRunner.G, GameRunner.B, 1);
        s.draw(sb);

        player.getSprite().draw(sb);
        for (Block b : blocks) {
            b.getSprite(player.color).draw(sb);
        }
        for (Coin c : coins) {
            if (c.life) {
                c.getSprite().draw(sb);
            }
        }
        sb.end();

        tb.setProjectionMatrix(cam_btn.combined);
        tb.begin();
        pauseBtn.getSprite().draw(tb);
        if(!GO) {
            GameRunner.font.draw(tb, "TAP TO START" + GameRunner.new_stars, cam_btn.position.x - 100, cam_btn.position.y);
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
