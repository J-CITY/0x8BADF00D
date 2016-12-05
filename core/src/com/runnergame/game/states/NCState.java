package com.runnergame.game.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.runnergame.game.Constants;
import com.runnergame.game.GameRunner;
import com.runnergame.game.sprites.Background;
import com.runnergame.game.sprites.Blocks.Block;
import com.runnergame.game.sprites.Coin;
import com.runnergame.game.sprites.Player;

public class NCState extends State {
    public static final int BLOCK_SPACING = 48;
    private Player player;
    private Array<Sprite> playerTail;
    private Array<Coin> coins;
    private SpriteBatch tb;
    Background bg;
    private void addCoins(float _x) {
        int w = MathUtils.random(4, 5);
        int h = MathUtils.random(2, 3);
        int type;
        float _y = (camera.position.y + MathUtils.random(-150, 150));
        for(int i = 0; i < w; ++i) {
            for(int j = 0; j < h; ++j) {
                type = MathUtils.random(0,1);
                coins.add(new Coin(_x + i*BLOCK_SPACING, _y + j*BLOCK_SPACING, type));
                coins.get(coins.size-1).speed = 800;
            }
        }
    }

    public NCState(GameStateManager gameStateMenager) {
        super(gameStateMenager);

        camera.setToOrtho(false, GameRunner.WIDTH / 2, GameRunner.HEIGHT / 2);
        bg = new Background(camera.position.x, camera.position.y, 0);
        tb = new SpriteBatch();
        player = new Player(200, 232, "ncplayer.png", 1);
        playerTail = new Array<Sprite>();
        for(int i = 0; i < 10; ++i) {
            playerTail.add(new Sprite(player.getSprite()));
            playerTail.get(playerTail.size-1).setCenter(player.getPosition().x - Constants.BLOCK_W/2 + i*20, player.getPosition().y);
            playerTail.get(playerTail.size-1).setScale(MathUtils.random(0.1f, 0.25f));
        }

        coins = new Array<Coin>();
        int coins_size = MathUtils.random(10, 20);
        addCoins(player.getPosition().x + 200);
        for(int j = 1; j < coins_size; ++j) {
            addCoins(coins.get(coins.size-1).getPos().x + 300);
        }
    }

    @Override
    protected void hendleInput() {
        if(Gdx.input.isTouched()) {
            Vector3 vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if(vec.y > player.getPosition().y) {
                player.NCM(5);
            } else {
                player.NCM(-5);
            }
        }
    }

    @Override
    public void update(float delta) {
        hendleInput();
        player.animUpdate(delta);
        for(Sprite s : playerTail) {
            if(s.getX() <= player.getPosition().x - 220) {
                s.setCenter(player.getPosition().x-20, player.getPosition().y);
                s.setScale(MathUtils.random(0.1f, 0.25f));
            } else {
                s.setX(s.getX() - Block.speed*delta);
            }
        }
        for(int i = 0; i < coins.size; ++i) {
            Coin c = coins.get(i);
            c.update(delta, player.getPosition().x);

            if (c.collide(player.getBounds()) && c.life) {
                if(c.TYPE == 0) {
                    GameRunner.now_coins++;
                } else {
                    GameRunner.now_metal++;
                }
                c.life = false;
            }
        }
        for(int i = 0; i < coins.size; ++i) {
            Coin c = coins.get(i);
            if (camera.position.x - (camera.viewportWidth / 2) > c.getPos().x + c.getPos().x + 64) {
                coins.get(i).dispose();
                coins.removeIndex(i);
            }
        }
        if(coins.size == 0) {
            GameRunner.dm.addData2("coins", GameRunner.now_coins);
            GameRunner.dm.addData2("metal", GameRunner.now_metal);
            gameStateMenager.set(new MoonCityState(gameStateMenager));
            //gameStateMenager.set(new MetaGameState(gameStateMenager));
        }
        camera.update();
    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        bg.getBgSprite().draw(sb);
        for(Sprite sp : playerTail) {
            if(player.color == 1)
                sp.setColor(GameRunner.colors.blue);
            else if (player.color == 2)
                sp.setColor(GameRunner.colors.red);
            else
                sp.setColor(GameRunner.colors.green);
            sp.draw(sb);
        }
        player.getSprite().draw(sb);

        for (Coin c : coins) {
            if (c.life) {
                c.getSprite().draw(sb);
                System.out.print(c.getPos().x+"\n");
            }
        }
        sb.end();

        tb.setProjectionMatrix(camera.combined.scl(0.5f));
        tb.begin();
        GameRunner.font.draw(tb, "COINS: " + GameRunner.now_coins + "   METAL: " + GameRunner.now_metal, camera.position.x - 280, camera.position.y - 160);
        tb.end();
    }

    @Override
    public void dispose() {
        player.dispose();
    }
}
