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

public class WinState extends State {
    private Button playBtn, closeBtn, onSoundBtn, offSoundBtn;
    private boolean playBtnPress = false, closeBtnPress = false, onSoundBtnPress = false;
    private String TITLE = "<< WIN >>";
    private final GlyphLayout layout = new GlyphLayout(GameRunner.font, TITLE);

    private DataManager dm;
    int lvl;

    private float pbtnY0 = 0, pbtnY = 400, cbtnY0 = 250, cbtnY = 400;
    private float sbtnY0 = -250, sbtnY = -400;
    Background bg;
    public WinState(GameStateManager gameStateMenager, int _lvl) {
        super(gameStateMenager);
        lvl =_lvl;
        GameRunner.dm.addData2("metal", GameRunner.now_metal+100);
        GameRunner.dm.addData2("coins", GameRunner.now_coins);
        camera.setToOrtho(false, GameRunner.WIDTH, GameRunner.HEIGHT);
        playBtn = new Button("Play.png", camera.position.x, camera.position.y+cbtnY, 1, 1);
        closeBtn = new Button("close.png", camera.position.x-530, camera.position.y+cbtnY, 1, 1);
        onSoundBtn = new Button("SoundOn.png", camera.position.x-530, camera.position.y+sbtnY, 1, 1);
        offSoundBtn = new Button("SoundOff.png", camera.position.x-530, camera.position.y+sbtnY, 1, 1);
        bg = new Background(camera.position.x, camera.position.y, 0);
    }
    float time = 2;
    @Override
    protected void hendleInput() {
        if(Gdx.input.isTouched()) {
            Vector3 vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if(playBtn.collide(vec.x, vec.y) && playBtnPress) {
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        time--;
                        if (time == 0) {
                            playBtnPress = false;
                            playBtn.setIsPress(false);
                            time = 2;
                        }
                    }
                }, 1);
            } else if(onSoundBtn.collide(vec.x, vec.y) && onSoundBtnPress) {
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        time--;
                        if (time == 0) {
                            onSoundBtnPress = false;
                            onSoundBtn.setIsPress(false);
                            offSoundBtn.setIsPress(false);
                            time = 2;
                        }
                    }
                }, 1);
            } else if(closeBtn.collide(vec.x, vec.y) && closeBtnPress) {
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        time--;
                        if (time == 0) {
                            closeBtnPress = false;
                            closeBtn.setIsPress(false);
                            time = 2;
                        }
                    }
                }, 1);
            }
        } else {
            if(playBtnPress) {
                playBtnPress = false;
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
            if(onSoundBtnPress) {
                onSoundBtnPress = false;
                onSoundBtn.setIsPress(false);
                offSoundBtn.setIsPress(false);
                GameRunner.isPlay = !GameRunner.isPlay;

            }
            if(closeBtnPress) {
                closeBtnPress = false;
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
            if(playBtn.collide(vec.x, vec.y)) {
                GameRunner.soundPressBtn.play(0.2f);
                if(!playBtnPress) {
                    playBtn.setIsPress(true);
                    playBtnPress = true;
                }
            }
            if(closeBtn.collide(vec.x, vec.y)) {
                GameRunner.soundPressBtn.play(0.2f);
                if(!closeBtnPress) {
                    closeBtn.setIsPress(true);
                    closeBtnPress = true;
                }
            }
            if(onSoundBtn.collide(vec.x, vec.y)) {
                GameRunner.soundPressBtn.play(0.2f);
                if(!onSoundBtnPress) {
                    onSoundBtn.setIsPress(true);
                    offSoundBtn.setIsPress(true);
                    onSoundBtnPress = true;
                }
            }
        }
    }

    @Override
    public void update(float delta) {
        hendleInput();

        if(pbtnY0 < pbtnY) {
            pbtnY -= speed*delta;
            playBtn.setPos(playBtn.getPos().x, camera.position.y + pbtnY);
        }
        if(cbtnY0 < cbtnY) {
            cbtnY -= speed*delta;
            closeBtn.setPos(closeBtn.getPos().x, camera.position.y + cbtnY);
        }
        if(sbtnY0 > sbtnY) {
            sbtnY += speed*delta;
            offSoundBtn.setPos(offSoundBtn.getPos().x, camera.position.y + sbtnY);
            onSoundBtn.setPos(onSoundBtn.getPos().x, camera.position.y + sbtnY);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        bg.getBgSprite().draw(sb);
        //GameRunner.font.draw(sb, "SCORE: " + GameRunner.score, GameRunner.WIDTH / 2, GameRunner.HEIGHT - 150);
        //GameRunner.font.draw(sb, "RECORD: " + GameRunner.rm.load(), GameRunner.WIDTH / 2, GameRunner.HEIGHT - 200);
        GameRunner.font.draw(sb, TITLE, (GameRunner.WIDTH - layout.width) / 2, GameRunner.HEIGHT - 100);
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
