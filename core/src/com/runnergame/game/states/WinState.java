package com.runnergame.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.runnergame.game.GameRunner;
import com.runnergame.game.Levels;
import com.runnergame.game.sprites.Background;
import com.runnergame.game.sprites.Button;

public class WinState extends State {
    private Button playBtn, closeBtn, onSoundBtn, offSoundBtn;
    private String TITLE = " WIN ";
    private final GlyphLayout layout = new GlyphLayout(GameRunner.font, TITLE);
    private Sprite headder = new Sprite(new Texture("headder.png"));
    int lvl;

    private float pbtnY0 = 0, pbtnY = 400, cbtnY0 = 340, cbtnY = 400;
    private float sbtnY0 = -250, sbtnY = -400;
    Background bg;
    public WinState(GameStateManager gameStateMenager, int _lvl) {
        super(gameStateMenager);
        GameRunner.adMobFlag = false;
        lvl =_lvl;
        GameRunner.dm.addData2("metal", GameRunner.now_metal+100);
        GameRunner.dm.addData2("coins", GameRunner.now_coins);
        camera.setToOrtho(false, GameRunner.WIDTH, GameRunner.HEIGHT);
        playBtn = new Button("button/next", camera.position.x, camera.position.y+cbtnY);
        playBtn.setScale(2);
        closeBtn = new Button("button/close", camera.position.x-530, camera.position.y+cbtnY);
        onSoundBtn = new Button("button/musicOn", camera.position.x-530, camera.position.y+sbtnY);
        offSoundBtn = new Button("button/musicOff", camera.position.x-530, camera.position.y+sbtnY);
        bg = new Background(camera.position.x, camera.position.y, 0);
        headder.setCenter(camera.position.x, camera.position.y+330);
        headder.setScale(1, 0.5f);
    }
    float time = 2;
    @Override
    protected void hendleInput() {
        if(Gdx.input.isTouched()) {
            Vector3 vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            playBtn.updatePress(vec.x, vec.y);
            offSoundBtn.updatePress(vec.x, vec.y);
            onSoundBtn.updatePress(vec.x, vec.y);
            closeBtn.updatePress(vec.x, vec.y);
        } else {
            if(playBtn.getIsPress()) {
                playBtn.setIsPress(false);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        time--;
                        if (time <= 2) {
                            time = 2;
                            if(lvl+1 < Levels.levels.size) {
                                lvl++;
                                PlayState.lvl = lvl;
                                if(lvl >= 0 && lvl <= 2) {
                                    HelperState.lvl = lvl;
                                    gameStateMenager.set(new HelperState(gameStateMenager));
                                } else {
                                    gameStateMenager.set(new PlayState(gameStateMenager));
                                }
                            }
                        }
                    }
                }, 0.1f);
            }
            if(onSoundBtn.getIsPress()) {
                onSoundBtn.setIsPress(false);
                offSoundBtn.setIsPress(false);
                GameRunner.isPlay = !GameRunner.isPlay;

            }
            if(closeBtn.getIsPress()) {
                closeBtn.setIsPress(false);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        time--;
                        if (time <= 1) {
                            time = 2;
                            gameStateMenager.set(new MoonCityState(gameStateMenager));
                        }
                    }
                }, 0.1f);
            }
        }
        if(Gdx.input.justTouched()) {
            Vector3 vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            playBtn.setPress(vec.x, vec.y);
            closeBtn.setPress(vec.x, vec.y);
            onSoundBtn.setPress(vec.x, vec.y);
            offSoundBtn.setPress(vec.x, vec.y);
        }
    }

    @Override
    public void update(float delta) {
        hendleInput();

        if(pbtnY0 < pbtnY) {
            pbtnY -= speed*delta;
            playBtn.setPos(playBtn.getPos().x, camera.position.y + pbtnY);
        } else {
            pbtnY = pbtnY0;
        }
        if(cbtnY0 < cbtnY) {
            cbtnY -= speed*delta;
            closeBtn.setPos(closeBtn.getPos().x, camera.position.y + cbtnY);
        } else {
            cbtnY = cbtnY0;
        }
        if(sbtnY0 > sbtnY) {
            sbtnY += speed*delta;
            offSoundBtn.setPos(offSoundBtn.getPos().x, camera.position.y + sbtnY);
            onSoundBtn.setPos(onSoundBtn.getPos().x, camera.position.y + sbtnY);
        } else {
            sbtnY = sbtnY0;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        bg.getBgSprite().draw(sb);
        headder.draw(sb);
        //GameRunner.font.draw(sb, "SCORE: " + GameRunner.score, GameRunner.WIDTH / 2, GameRunner.HEIGHT - 150);
        //GameRunner.font.draw(sb, "RECORD: " + GameRunner.rm.load(), GameRunner.WIDTH / 2, GameRunner.HEIGHT - 200);
        GameRunner.font.draw(sb, TITLE, camera.position.x - 400, camera.position.y + 350);
        playBtn.getSprite().draw(sb);
        closeBtn.getSprite().draw(sb);
        if(GameRunner.isPlay) {
            onSoundBtn.getSprite().draw(sb);
        } else {
            offSoundBtn.getSprite().draw(sb);
        }
        sb.end();
    }

    @Override
    public void dispose() {
        playBtn.dispose();
        onSoundBtn.dispose();
        offSoundBtn.dispose();
    }
}
