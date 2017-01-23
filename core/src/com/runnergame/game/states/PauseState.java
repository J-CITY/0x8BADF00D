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

public class PauseState extends State {
    private Button playBtn, onSoundBtn, offSoundBtn, restartBtn, exitBtn;
    private String TITLE = " PAUSE ";
    private final GlyphLayout layout = new GlyphLayout(GameRunner.font, TITLE);
    private Sprite headder = new Sprite(new Texture("headder.png"));
    public static int whatGame = 0;

    private float pbtnY0 = 0, pbtnY = 400;
    private float ebtnY0 = 340, ebtnY = 400;
    private float sbtnY0 = -250, sbtnY = -400;
    private float rbtnY0 = 0, rbtnY = -400;
    Background bg;

    public PauseState(GameStateManager gameStateMenager) {
        super(gameStateMenager);
        GameRunner.adMobFlag = true;
        camera.setToOrtho(false, GameRunner.WIDTH, GameRunner.HEIGHT);
        playBtn = new Button("button/play", camera.position.x-200, camera.position.y+pbtnY);
        playBtn.setScale(2);
        restartBtn = new Button("button/restart", camera.position.x+200, camera.position.y+rbtnY);
        restartBtn.setScale(2);
        onSoundBtn = new Button("button/musicOn", camera.position.x-530, camera.position.y+sbtnY);
        offSoundBtn = new Button("button/musicOff", camera.position.x-530, camera.position.y+sbtnY);
        exitBtn = new Button("button/close", camera.position.x-530, camera.position.y+ebtnY);
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
            exitBtn.updatePress(vec.x, vec.y);
            restartBtn.updatePress(vec.x, vec.y);
            onSoundBtn.updatePress(vec.x, vec.y);
            offSoundBtn.updatePress(vec.x, vec.y);
        } else {
            if(playBtn.getIsPress()) {
                playBtn.setIsPress(false);
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
            if(exitBtn.getIsPress()) {
                exitBtn.setIsPress(false);
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
            if(restartBtn.getIsPress()) {
                restartBtn.setIsPress(false);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        time--;
                        if (time <= 1) {
                            time = 2;
                            gameStateMenager.set(new PlayState(gameStateMenager));
                        }
                    }
                }, 0.1f);
            }
            if(onSoundBtn.getIsPress()) {
                onSoundBtn.setIsPress(false);
                offSoundBtn.setIsPress(false);
                GameRunner.isPlay = !GameRunner.isPlay;

            }
        }
        if(Gdx.input.justTouched()) {
            Vector3 vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            playBtn.setPress(vec.x, vec.y);
            restartBtn.setPress(vec.x, vec.y);
            exitBtn.setPress(vec.x, vec.y);
            onSoundBtn.setPress(vec.x, vec.y);
            offSoundBtn.setPress(vec.x, vec.y);
        }
    }

    @Override
    public void update(float delta) {
        hendleInput();

        if(pbtnY0 < pbtnY) {
            pbtnY -= speed*delta;
        } else {
            pbtnY = pbtnY0;
        }
        if(rbtnY0 > rbtnY) {
            rbtnY += speed*delta;
        } else {
            rbtnY = rbtnY0;
        }
        if(ebtnY0 < ebtnY) {
            ebtnY -= speed * delta;
        } else {
            ebtnY = ebtnY0;
        }
        if(sbtnY0 > sbtnY) {
            sbtnY += speed*delta;
        } else {
            sbtnY = sbtnY0;
        }
        restartBtn.setPos(restartBtn.getPos().x, camera.position.y + rbtnY);
        exitBtn.setPos(exitBtn.getPos().x, camera.position.y + ebtnY);
        offSoundBtn.setPos(offSoundBtn.getPos().x, camera.position.y + sbtnY);
        onSoundBtn.setPos(onSoundBtn.getPos().x, camera.position.y + sbtnY);
        playBtn.setPos(playBtn.getPos().x, camera.position.y + pbtnY);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        bg.getBgSprite().draw(sb);
        headder.draw(sb);
        GameRunner.font.draw(sb, TITLE, camera.position.x - 400, camera.position.y + 350);
        playBtn.getSprite().draw(sb);
        restartBtn.getSprite().draw(sb);
        exitBtn.getSprite().draw(sb);
        if(GameRunner.isPlay) {
            onSoundBtn.getSprite().draw(sb);
        } else {
            offSoundBtn.getSprite().draw(sb);
        }
        sb.end();
    }

    @Override
    public void dispose() {
        onSoundBtn.dispose();
        offSoundBtn.dispose();
        playBtn.dispose();
        restartBtn.dispose();
    }
}
