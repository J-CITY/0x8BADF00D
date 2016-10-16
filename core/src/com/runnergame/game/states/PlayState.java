package com.runnergame.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.runnergame.game.Colors;
import com.runnergame.game.Constants;
import com.runnergame.game.GameRunner;
import com.runnergame.game.sprites.Block;
import com.runnergame.game.sprites.BlockFloor;
import com.runnergame.game.sprites.BlockJump;
import com.runnergame.game.sprites.BlockNeedle;
import com.runnergame.game.sprites.Button;
import com.runnergame.game.sprites.Player;

public class PlayState extends State {

    public static final int BLOCK_SPACING = 64;


    SpriteBatch spriteFont = new SpriteBatch();;
    Matrix4 mx4Font = new Matrix4();

    private Player player;

    private Array<Block> blocks;
    public static final int BLOCKS_MAX_COUNT = 80;
    Button pauseBtn;

    private void addPlatform(float x) {
        int platform_count = MathUtils.random(3, 8);
        int platform_color;
        int _floor = 0;
        float __x;

        blocks.add(new BlockFloor(x + BLOCK_SPACING*0, 100, 0));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockFloor(x + BLOCK_SPACING*1, 100, 0));
        blocks.get(blocks.size-1).setColor(0);

        __x = blocks.get(blocks.size-1).getPos().x+64;

        for (int i = 0; i < platform_count; ++i) {
            platform_color = MathUtils.random(1, 2);
            if (i == 0) {
                blocks.add(new BlockJump(__x, 100, 4));
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

            blocks.add(new BlockJump(__x, 100 + 32*_floor, 4));
            blocks.get(blocks.size-1).setColor(platform_color);
            __x += 64;
            blocks.add(new BlockJump(__x, 100 + 32*_floor, 4));
            blocks.get(blocks.size-1).setColor(platform_color);
            __x += 230;
        }
    }

    private void addBridge(float x) {
        int bridge_color = MathUtils.random(1, 2);
        int size = MathUtils.random(8, 16);

        for (int i = 0; i < size; ++i) {
            blocks.add(new BlockFloor(x + BLOCK_SPACING*i, 100, 0));
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
                blocks.add(new BlockJump(x + BLOCK_SPACING*i, 100, 4));
                blocks.get(blocks.size-1).setColor(jump_color);
                continue;
            }
            if (i == 4) {
                blocks.add(new BlockNeedle(x + BLOCK_SPACING*i, 148, 1));
                blocks.get(blocks.size-1).setColor(needle_color);
            }
            blocks.add(new BlockFloor(x + BLOCK_SPACING*i, 100, 0));
            blocks.get(blocks.size-1).setColor(0);
        }
    }

    private void addNeedleUp(float x) {
        int needle_color = MathUtils.random(1, 2);
        int size = MathUtils.random(5, 10);

        blocks.add(new BlockFloor(x + BLOCK_SPACING*0, 100, 0));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockFloor(x + BLOCK_SPACING*1, 100, 0));
        blocks.get(blocks.size-1).setColor(0);
        for (int i = 2; i < size-2; ++i) {
            blocks.add(new BlockNeedle(x + BLOCK_SPACING*i, 148, 1));
            blocks.get(blocks.size-1).setColor(needle_color);
            blocks.add(new BlockFloor(x + BLOCK_SPACING*i, 100, 0));
            blocks.get(blocks.size-1).setColor(0);
        }
        blocks.add(new BlockFloor(x + BLOCK_SPACING*(size-2), 100, 0));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockFloor(x + BLOCK_SPACING*(size-1), 100, 0));
        blocks.get(blocks.size-1).setColor(0);
    }

    private void addNeedleDown(float x) {
        int needle_color = MathUtils.random(0, 2);
        int jump_color = MathUtils.random(1, 2);

        blocks.add(new BlockFloor(x + BLOCK_SPACING*0, 100, 0));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockFloor(x + BLOCK_SPACING*1, 100, 0));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockJump(x + BLOCK_SPACING*2, 100, 4));
        blocks.get(blocks.size-1).setColor(jump_color);
        for (int i = 3; i < 5; ++i) {
            blocks.add(new BlockNeedle(x + BLOCK_SPACING*i, 100, 1));
            blocks.get(blocks.size-1).setColor(needle_color);
        }
        blocks.add(new BlockFloor(x + BLOCK_SPACING*5, 100, 0));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockFloor(x + BLOCK_SPACING*6, 100, 0));
        blocks.get(blocks.size-1).setColor(0);
    }

    private void addBridgeDown(float x) {
        int needle_color = MathUtils.random(1, 2);
        int jump_color = MathUtils.random(1, 2);
        int size = MathUtils.random(5, 10);

        blocks.add(new BlockFloor(x + BLOCK_SPACING*0, 100, 0));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockFloor(x + BLOCK_SPACING*1, 100, 0));
        blocks.get(blocks.size-1).setColor(0);
        for (int i = 2; i < size+2; ++i) {
            blocks.add(new BlockNeedle(x + BLOCK_SPACING*i, 100-16, 1));
            blocks.get(blocks.size-1).setColor(needle_color);
            blocks.add(new BlockFloor(x + BLOCK_SPACING*i, 100-64, 0));
            blocks.get(blocks.size-1).setColor(0);
        }
        blocks.add(new BlockFloor(x + BLOCK_SPACING*(size+2), 100-64, 0));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockFloor(x + BLOCK_SPACING*(size+3), 100-64, 0));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockFloor(x + BLOCK_SPACING*(size+4), 100-64, 0));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockJump(x + BLOCK_SPACING*(size+5), 100-64, 4));
        blocks.get(blocks.size-1).setColor(jump_color);
        blocks.add(new BlockFloor(x + BLOCK_SPACING*(size+6), 100-64, 0));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockFloor(x + BLOCK_SPACING*(size+7), 100-64, 0));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockNeedle(x + BLOCK_SPACING*(size+7), 100-16, 1));
        blocks.get(blocks.size-1).setColor(0);

        blocks.add(new BlockFloor(x + BLOCK_SPACING*(size+8), 100, 0));
        blocks.get(blocks.size-1).setColor(0);
        blocks.add(new BlockFloor(x + BLOCK_SPACING*(size+9), 100, 0));
        blocks.get(blocks.size-1).setColor(0);

    }

    private void addFloor(float x) {
        int size = MathUtils.random(3, 6);

        for (int i = 0; i < size; ++i) {
            blocks.add(new BlockFloor(x + BLOCK_SPACING*i, 100, 0));
            blocks.get(blocks.size-1).setColor(0);
        }
    }

    public PlayState(GameStateManager gameStateMenager) {
        super(gameStateMenager);

        GameRunner.score = 0;
        camera.setToOrtho(false, GameRunner.WIDTH / 2, GameRunner.HEIGHT / 2);

        player = new Player(200, 232);

        blocks = new Array<Block>();
        addFloor(0);
        for (int i = 0; i < 3; ++i) {
            addFloor(blocks.get(blocks.size-1).getPos().x + Constants.BLOCK_W);
        }

        pauseBtn = new Button("Pause.png", camera.position.x - 280, camera.position.y + 150);
        pauseBtn.getBounds().setCenter(camera.position.x - 280, camera.position.y + 150);
    }

    @Override
    protected void hendleInput() {
        if(Gdx.input.justTouched()) {
            Vector3 vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if(pauseBtn.collide(vec.x, vec.y)) {
                gameStateMenager.push(new PauseState(gameStateMenager));
            }
            player.changeColor();
        }
    }

    @Override
    public void update(float delta) {
        //Gdx.app.log("GameScreen FPS", (1/delta) + "");
        hendleInput();
        player.update(delta);
        if (player.getPosition().y <= -100) {
            System.out.print("GAME OVER" + "\n");
            gameStateMenager.set(new GameOver(gameStateMenager));
        }

        while (blocks.size < BLOCKS_MAX_COUNT) {
            int new_blocks = MathUtils.random(0, 6);
            switch (new_blocks) {
                case 0:
                    addFloor(blocks.get(blocks.size-1).getPos().x + Constants.BLOCK_W);
                    break;
                case 1:
                    addNeedle(blocks.get(blocks.size-1).getPos().x + Constants.BLOCK_W);
                    break;
                case 2:
                    addBridge(blocks.get(blocks.size-1).getPos().x + Constants.BLOCK_W);
                    break;
                case 3:
                    addPlatform(blocks.get(blocks.size-1).getPos().x + Constants.BLOCK_W);
                    addFloor(blocks.get(blocks.size-1).getPos().x + Constants.BLOCK_W);
                    break;
                case 4:
                    addBridgeDown(blocks.get(blocks.size-1).getPos().x + Constants.BLOCK_W);
                    break;
                case 5:
                    addNeedleDown(blocks.get(blocks.size-1).getPos().x + Constants.BLOCK_W);
                    break;
                case 6:
                    addNeedleUp(blocks.get(blocks.size-1).getPos().x + Constants.BLOCK_W);
                    break;
            }
        }
        boolean flag = true;
        for (int i = 0; i < blocks.size; ++i){
            Block f = blocks.get(i);
            f.update(delta, player.getPosition().x);

            if (f.collide(player.getBounds())) {
                if (f.TYPE == Constants.B_JUMP) {
                    flag = false;
                    if(f.getPos().y < player.getPosition().y)
                        player.onFloor(f.getPos().y+63);
                }
                if (f.TYPE == Constants.B_FLOOR && (f.color == Colors.GRAY || f.color == player.color) ) {
                    flag = false;
                    if(f.getPos().y < player.getPosition().y)
                        player.onFloor(f.getPos().y+63);
                }
                if ((f.color == Colors.GRAY || f.color == player.color) && f.TYPE != Constants.B_FLOOR && f.TYPE != Constants.B_JUMP) {
                    System.out.print("GAME OVER_0" + "\n");
                    gameStateMenager.set(new GameOver(gameStateMenager));
                }
                if (f.TYPE == Constants.B_JUMP && f.color == player.color) {
                    flag = false;
                    player.jump();
                }
            }
        }
        if (flag) {
            player.onFloor(Constants.GROUND);
            player.on_floor = false;
        }

        for (int i = 0; i < blocks.size; ++i) {
            Block r = blocks.get(i);
            if (camera.position.x - (camera.viewportWidth / 2) > r.getPos().x + r.getPos().x + 64) {
                if(r.TYPE == Constants.B_FLOOR || r.TYPE == Constants.B_JUMP) {
                    GameRunner.score++;
                }
                blocks.get(i).dispose();
                blocks.removeIndex(i);
            }
        }
        camera.update();
    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        player.getSprite().draw(sb);

        for (Block b : blocks) {
            b.getSprite(player.color).draw(sb);
        }

        pauseBtn.getSprite().draw(sb);
        GameRunner.font.draw(sb, "SCORE: " + GameRunner.score, 800, 200);
        sb.end();
    }

    @Override
    public void dispose() {
        player.dispose();
        for(Block b : blocks) {
            b.dispose();
        }
    }
}
