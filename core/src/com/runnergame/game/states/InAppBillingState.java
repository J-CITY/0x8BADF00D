package com.runnergame.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.runnergame.game.GameRunner;
import com.runnergame.game.Levels;
import com.runnergame.game.sprites.Background;
import com.runnergame.game.sprites.Button;

/**
 * Created by 333da on 18.12.2016.
 */

public class InAppBillingState extends State {
    private Button buy1Btn, buy2Btn, buy3Btn, closeBtn;
    private String TITLE = "SHOP";
    private final GlyphLayout layout = new GlyphLayout(GameRunner.font, TITLE);



    private float b1btnY0 = 0, b1btnY = 400, cbtnY0 = 250, cbtnY = 400;
    Background bg;
    public InAppBillingState(GameStateManager gameStateMenager) {
        super(gameStateMenager);
        //GameRunner.dm.addData2("coins", GameRunner.now_coins);
        camera.setToOrtho(false, GameRunner.WIDTH, GameRunner.HEIGHT);
        buy1Btn = new Button("button/buy", camera.position.x-400, camera.position.y+cbtnY);
        buy2Btn = new Button("button/buy", camera.position.x, camera.position.y+cbtnY);
        buy3Btn = new Button("button/buy", camera.position.x+400, camera.position.y+cbtnY);
        closeBtn = new Button("button/close", camera.position.x-530, camera.position.y+cbtnY);
        bg = new Background(camera.position.x, camera.position.y, 0);
    }
    float time = 2;
    @Override
    protected void hendleInput() {
        if(Gdx.input.isTouched()) {
            Vector3 vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            buy1Btn.updatePress(vec.x, vec.y);
            buy2Btn.updatePress(vec.x, vec.y);
            buy3Btn.updatePress(vec.x, vec.y);
            closeBtn.updatePress(vec.x, vec.y);
        } else {
            if(buy1Btn.getIsPress()) {
                buy1Btn.setIsPress(false);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        time--;
                        if (time <= 2) {
                            time = 2;
                            GameRunner.buyBtn100coins = true;
                        }
                    }
                }, 0.1f);
            }
            if(buy2Btn.getIsPress()) {
                buy2Btn.setIsPress(false);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        time--;
                        if (time <= 2) {
                            time = 2;
                            GameRunner.buyBtn500coins = true;
                        }
                    }
                }, 0.1f);
            }
            if(buy3Btn.getIsPress()) {
                buy3Btn.setIsPress(false);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        time--;
                        if (time <= 2) {
                            time = 2;
                            GameRunner.buyBtn1000coins = true;
                        }
                    }
                }, 0.1f);
            }
            if(closeBtn.getIsPress()) {
                closeBtn.setIsPress(false);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        time--;
                        if (time <= 1) {
                            time = 2;
                            gameStateMenager.pop();
                        }
                    }
                }, 0.1f);
            }
        }
        if(Gdx.input.justTouched()) {
            Vector3 vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            buy1Btn.setPress(vec.x, vec.y);
            buy2Btn.setPress(vec.x, vec.y);
            buy3Btn.setPress(vec.x, vec.y);
            closeBtn.setPress(vec.x, vec.y);
        }
    }

    @Override
    public void update(float delta) {
        hendleInput();

        if(b1btnY0 < b1btnY) {
            b1btnY -= speed*delta;
            buy1Btn.setPos(buy1Btn.getPos().x, camera.position.y + b1btnY);
            buy2Btn.setPos(buy2Btn.getPos().x, camera.position.y + b1btnY);
            buy3Btn.setPos(buy3Btn.getPos().x, camera.position.y + b1btnY);
        } else {
            b1btnY = b1btnY0;
        }
        if(cbtnY0 < cbtnY) {
            cbtnY -= speed*delta;
            closeBtn.setPos(closeBtn.getPos().x, camera.position.y + cbtnY);
        } else {
            cbtnY = cbtnY0;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        bg.getBgSprite().draw(sb);
        GameRunner.font.draw(sb, TITLE, (GameRunner.WIDTH - layout.width) / 2, GameRunner.HEIGHT - 100);
        buy1Btn.getSprite().draw(sb);
        buy2Btn.getSprite().draw(sb);
        buy3Btn.getSprite().draw(sb);
        closeBtn.getSprite().draw(sb);
        sb.end();
    }

    @Override
    public void dispose() {
        closeBtn.dispose();
        buy1Btn.dispose();
    }
}
