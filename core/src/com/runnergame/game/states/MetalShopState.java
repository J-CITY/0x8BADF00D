package com.runnergame.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.runnergame.game.GameRunner;
import com.runnergame.game.sprites.Background;
import com.runnergame.game.sprites.Button;

/**
 * Created by 333da on 16.02.2017.
 */

public class MetalShopState extends State {
    private Button buy1Btn, buy2Btn, buy3Btn, closeBtn;
    private String TITLE = "METAL SHOP";
    private final GlyphLayout layout = new GlyphLayout(GameRunner.font, TITLE);

    private Sprite headder = new Sprite(new Texture("headder.png"));

    private float b1btnY0 = 0, b1btnY = 400, cbtnY0 = 340, cbtnY = 400;
    Background bg;
    public MetalShopState(GameStateManager gameStateMenager) {
        super(gameStateMenager);
        //GameRunner.dm.addData2("coins", GameRunner.now_coins);
        camera.setToOrtho(false, GameRunner.WIDTH, GameRunner.HEIGHT);
        buy1Btn = new Button("button/buy", camera.position.x-400, camera.position.y+cbtnY);
        buy2Btn = new Button("button/buy", camera.position.x, camera.position.y+cbtnY);
        buy3Btn = new Button("button/buy", camera.position.x+400, camera.position.y+cbtnY);
        closeBtn = new Button("button/close", camera.position.x-550, camera.position.y+cbtnY);
        closeBtn.setScale(1.5f);
        bg = new Background(camera.position.x, camera.position.y, 0);

        headder.setCenter(camera.position.x, camera.position.y+330);
        headder.setScale(1, 0.5f);
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
                            if (GameRunner.dm.load2("coins") >= 100) {
                                GameRunner.dm.addData2("coins", GameRunner.dm.load2("coins")-100);
                                GameRunner.dm.addData2("metal", GameRunner.dm.load2("metal") + 100);
                                GameRunner.now_coins = GameRunner.dm.load2("coins");
                                GameRunner.now_metal = GameRunner.dm.load2("metal");
                            }
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
                            if (GameRunner.dm.load2("coins") >= 450) {
                                GameRunner.dm.addData2("coins", GameRunner.dm.load2("coins")-450);
                                GameRunner.dm.addData2("metal", GameRunner.dm.load2("metal") + 500);
                                GameRunner.now_coins = GameRunner.dm.load2("coins");
                                GameRunner.now_metal = GameRunner.dm.load2("metal");
                            }
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
                            if (GameRunner.dm.load2("coins") >= 900) {
                                GameRunner.dm.addData2("coins", GameRunner.dm.load2("coins")-900);
                                GameRunner.dm.addData2("metal", GameRunner.dm.load2("metal") + 1000);
                                GameRunner.now_coins = GameRunner.dm.load2("coins");
                                GameRunner.now_metal = GameRunner.dm.load2("metal");
                            }
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
            buy1Btn.setPos(buy1Btn.getPos().x, camera.position.y + b1btnY);
            buy2Btn.setPos(buy2Btn.getPos().x, camera.position.y + b1btnY);
            buy3Btn.setPos(buy3Btn.getPos().x, camera.position.y + b1btnY);
        }
        if(cbtnY0 < cbtnY) {
            cbtnY -= speed*delta;
            closeBtn.setPos(closeBtn.getPos().x, camera.position.y + cbtnY);
        } else {
            cbtnY = cbtnY0;
            closeBtn.setPos(closeBtn.getPos().x, camera.position.y + cbtnY);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        bg.getBgSprite().draw(sb);
        headder.draw(sb);
        GameRunner.font.draw(sb, TITLE, camera.position.x - 400, camera.position.y + 350);
        GameRunner.font.draw(sb, "     COINS: " + GameRunner.now_coins, camera.position.x - 200, camera.position.y + 350);
        GameRunner.font.draw(sb, "     METAL: " + GameRunner.now_metal, camera.position.x - 0, camera.position.y + 350);
        GameRunner.font.draw(sb, "100 Metal for 100 Coins", camera.position.x-550, camera.position.y+100);
        GameRunner.font.draw(sb, "500 Metal for 450 Coins", camera.position.x-170, camera.position.y+100);
        GameRunner.font.draw(sb, "1000 Metal for 900 Coins", camera.position.x+230, camera.position.y+100);
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
        buy2Btn.dispose();
        buy3Btn.dispose();
        headder.getTexture().dispose();
        bg.dispouse();
    }
}
