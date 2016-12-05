package com.runnergame.game.states;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.runnergame.game.GameRunner;
import com.runnergame.game.sprites.Background;
import com.runnergame.game.sprites.Button;

public class GameOver extends State {
    private Button playBtn, rebornBtn, onSoundBtn, offSoundBtn, exitBtn;
    boolean playBtnPress=false,rebornBtnPress=false, onSoundBtnPress=false, exitBtnPress;
    private String TITLE = "<< GAME OVER >>";
    private final GlyphLayout layout = new GlyphLayout(GameRunner.font, TITLE);

    private float rbtnY0 = 0, rbtnY = 400;
    private float pbtnY0 = 0, pbtnY = 400;
    private float ebtnY0 = 250, ebtnY = 400;
    private float sbtnY0 = -250, sbtnY = -400;
    Background bg;
    public GameOver(GameStateManager gameStateMenager) {
        super(gameStateMenager);
        camera.setToOrtho(false, GameRunner.WIDTH, GameRunner.HEIGHT);
        playBtn = new Button("playAgain.png", camera.position.x-200, camera.position.y+pbtnY, 1, 1);
        rebornBtn = new Button("continue.png", camera.position.x+200, camera.position.y+rbtnY, 1, 1);
        onSoundBtn = new Button("SoundOn.png", camera.position.x-530, camera.position.y+sbtnY, 1, 1);
        offSoundBtn = new Button("SoundOff.png", camera.position.x-530, camera.position.y+sbtnY, 1, 1);
        exitBtn = new Button("EndGame.png", camera.position.x-530, camera.position.y+ebtnY, 1, 1);
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
            } else if(exitBtn.collide(vec.x, vec.y) && exitBtnPress) {
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        time--;
                        if (time == 0) {
                            exitBtnPress = false;
                            exitBtn.setIsPress(false);
                            time = 2;
                        }
                    }
                }, 1);
            } else if(rebornBtn.collide(vec.x, vec.y) && rebornBtnPress) {
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        time--;
                        if (time == 0) {
                            rebornBtnPress = false;
                            rebornBtn.setIsPress(false);
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
            }
        } else {
            if(playBtnPress) {
                playBtnPress = false;
                playBtn.setIsPress(false);
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
            if(exitBtnPress) {
                exitBtnPress = false;
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
            if(rebornBtnPress) {
                rebornBtnPress = false;
                rebornBtn.setIsPress(false);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        time--;
                        if (time <= 1) {
                            time = 2;
                            if (GameRunner.dm.load2("coins") >= 100 && !PlayState.reborn) {
                                GameRunner.dm.addData2("coins", GameRunner.dm.load2("coins")-100);
                                PlayState.reborn = true;
                                gameStateMenager.pop();
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
        }
        if(Gdx.input.justTouched()) {
            Vector3 vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if(playBtn.collide(vec.x, vec.y)) {
                if(!playBtnPress) {
                    playBtn.setIsPress(true);
                    playBtnPress = true;
                }
            }
            if(rebornBtn.collide(vec.x, vec.y)) {
                if(!rebornBtnPress) {
                    rebornBtn.setIsPress(true);
                    rebornBtnPress = true;
                }
            }
            if(onSoundBtn.collide(vec.x, vec.y)) {
                if(!onSoundBtnPress) {
                    onSoundBtn.setIsPress(true);
                    offSoundBtn.setIsPress(true);
                    onSoundBtnPress = true;
                }
            }
            if(exitBtn.collide(vec.x, vec.y)) {
                if(!exitBtnPress) {
                    exitBtn.setIsPress(true);
                    exitBtnPress = true;
                }
            }
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
        if(rbtnY0 < rbtnY) {
            rbtnY -= speed*delta;
        } else {
            rbtnY = rbtnY0;
        }
        if(ebtnY0 > ebtnY) {
            ebtnY -= speed*delta;
        } else {
            ebtnY = ebtnY0;
        }
        if(sbtnY0 > sbtnY) {
            sbtnY += speed*delta;
        } else {
            sbtnY = sbtnY0;
        }
        playBtn.setPos(playBtn.getPos().x, camera.position.y + pbtnY);
        rebornBtn.setPos(rebornBtn.getPos().x, camera.position.y + rbtnY);
        exitBtn.setPos(exitBtn.getPos().x, camera.position.y + ebtnY);
        offSoundBtn.setPos(offSoundBtn.getPos().x, camera.position.y + sbtnY);
        onSoundBtn.setPos(onSoundBtn.getPos().x, camera.position.y + sbtnY);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        bg.getBgSprite().draw(sb);
        GameRunner.font.draw(sb, TITLE, (GameRunner.WIDTH - layout.width) / 2, GameRunner.HEIGHT - 100);
        playBtn.getSprite().draw(sb);
        exitBtn.getSprite().draw(sb);
        rebornBtn.getSprite().draw(sb);
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