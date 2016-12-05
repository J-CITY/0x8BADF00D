package com.runnergame.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.runnergame.game.GameRunner;
import com.runnergame.game.sprites.Background;
import com.runnergame.game.sprites.Button;
import com.runnergame.game.sprites.MetaGame.Building;

public class MenuState extends State {

    private Button playBtn, onSoundBtn, offSoundBtn, shopBtn;
    private float pbtnY0 = 0, pbtnY = 400;
    private float sbtnY0 = -250, sbtnY = -400;
    private float shbtnY0 = -150, shbtnY = -400;
    private String TITLE = "0x8BADF00D";
    private final GlyphLayout layout = new GlyphLayout(GameRunner.font, TITLE);
    Background bg;
    public MenuState(GameStateManager gameStateMenager) {
        super(gameStateMenager);
        camera.setToOrtho(false, GameRunner.WIDTH, GameRunner.HEIGHT);
        playBtn = new Button("play_t.png", camera.position.x, camera.position.y+pbtnY, 1, 1);
        onSoundBtn = new Button("SoundOn.png", camera.position.x-530, camera.position.y+sbtnY, 1, 1);
        offSoundBtn = new Button("SoundOff.png", camera.position.x-530, camera.position.y+sbtnY, 1, 1);
        shopBtn = new Button("btnShop.png", camera.position.x-530, camera.position.y+shbtnY, 1, 1);

        bg = new Background(camera.position.x, camera.position.y, 0);
    }
    private boolean onSoundBtnPress=false, playBtnPress=false, shopBtnPress=false;
    float time = 2;

    @Override
    protected void hendleInput() {
        //System.out.print(time +"\n");
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
            } else if(shopBtn.collide(vec.x, vec.y) && shopBtnPress) {
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        time--;
                        if (time == 0) {
                            shopBtnPress = false;
                            shopBtn.setIsPress(false);
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
                            gameStateMenager.set(new MoonCityState(gameStateMenager));
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
            if(shopBtnPress) {
                shopBtnPress = false;
                shopBtn.setIsPress(false);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        time--;
                        if (time <= 1) {
                            time = 2;
                            gameStateMenager.push(new ShopState(gameStateMenager));
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
            if(shopBtn.collide(vec.x, vec.y)) {
                GameRunner.soundPressBtn.play(0.2f);
                if(!shopBtnPress) {
                    shopBtn.setIsPress(true);
                    shopBtnPress = true;
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
        //playBtn.update(delta);
        if(pbtnY0 < pbtnY) {
            pbtnY -= speed*delta;
        } else {
            pbtnY = pbtnY0;
        }
        if(sbtnY0 > sbtnY) {
            sbtnY += speed*delta;
        } else {
            sbtnY = sbtnY0;
        }
        if(shbtnY0 > shbtnY) {
            shbtnY += speed*delta;
        } else {
            shbtnY = shbtnY0;
        }
        shopBtn.setPos(shopBtn.getPos().x, camera.position.y + shbtnY);
        playBtn.setPos(playBtn.getPos().x, camera.position.y + pbtnY);
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
        shopBtn.getSprite().draw(sb);
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