package com.runnergame.game.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.runnergame.game.Colors;
import com.runnergame.game.Constants;
import com.runnergame.game.GameRunner;
import com.runnergame.game.sprites.Block;
import com.runnergame.game.sprites.BlockFinish;
import com.runnergame.game.sprites.BlockFloor;
import com.runnergame.game.sprites.BlockJump;
import com.runnergame.game.sprites.BlockNeedle;
import com.runnergame.game.sprites.Button;
import com.runnergame.game.sprites.Coin;
import com.runnergame.game.sprites.Player;

public class NCState extends State {
    public static final int BLOCK_SPACING = 64;
    private Player player;

    //Button pauseBtn;

    private Array<Coin> coins;

    //int NYAN_CAT_MODE = 0;

    //int BLOCK_COUNT;
    private SpriteBatch tb;

    public NCState(GameStateManager gameStateMenager) {
        super(gameStateMenager);

        camera.setToOrtho(false, GameRunner.WIDTH / 2, GameRunner.HEIGHT / 2);
        tb = new SpriteBatch();
        player = new Player(200, 232);

        coins = new Array<Coin>();//!!!!!!!!
        int coins_size = MathUtils.random(20, 40);
        coins.add(new Coin(player.getPosition().x + 100, MathUtils.random(100, 550), 0));
        for(int j = 1; j < coins_size; ++j) {
            coins.add(new Coin(coins.get(coins.size-1).getPos().x+64, MathUtils.random(100, 550), 0));
        }

        //pauseBtn = new Button("Pause.png", camera.position.x - 280, camera.position.y + 150);
        //pauseBtn.getBounds().setCenter(camera.position.x - 280, camera.position.y + 150);
    }

    @Override
    protected void hendleInput() {
        if(Gdx.input.justTouched()) {
            Vector3 vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            //if(pauseBtn.collide(vec.x, vec.y)) {
            //    gameStateMenager.push(new PauseState(gameStateMenager));
            //}
        }
        if(Gdx.input.isTouched()) {
            Vector3 vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            //if(pauseBtn.collide(vec.x, vec.y)) {
            //    gameStateMenager.push(new PauseState(gameStateMenager));
            //}
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

        for(int i = 0; i < coins.size; ++i) {//!!!!!!!!!!!!!!!!
            Coin c = coins.get(i);
            c.update(delta, player.getPosition().x);

            if (c.collide(player.getBounds()) && c.life) {
                if(c.TYPE == 0) {
                    GameRunner.new_coins++;
                } else {
                    GameRunner.new_stars++;
                }
                c.life = false;
            }
        }
        for(int i = 0; i < coins.size; ++i) {//!!!!!!!!!!!!!!!!
            Coin c = coins.get(i);
            if (camera.position.x - (camera.viewportWidth / 2) > c.getPos().x + c.getPos().x + 64) {
                coins.get(i).dispose();
                coins.removeIndex(i);
            }
        }
        if(coins.size == 0) {
            gameStateMenager.set(new MetaGameState(gameStateMenager));
        }
        camera.update();
    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        player.getSprite().draw(sb);

        for (Coin c : coins) {
            if (c.life) {
                c.getSprite().draw(sb);
            }
        }
        //pauseBtn.getSprite().draw(sb);
        sb.end();

        tb.setProjectionMatrix(camera.combined.scl(0.5f));
        tb.begin();
        GameRunner.font.draw(tb, "COINS: " + GameRunner.new_coins + " STARS: " + GameRunner.new_stars, camera.position.x - 280, camera.position.y - 160);
        tb.end();
    }

    @Override
    public void dispose() {
        player.dispose();
    }
}
