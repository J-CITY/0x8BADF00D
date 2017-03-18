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
import com.runnergame.game.MetaLevels;
import com.runnergame.game.sprites.Background;
import com.runnergame.game.sprites.Button;

public class SettingsState extends State {
    private Button lenguageLBtn, lenguageRBtn, closeBtn, onSoundBtn, offSoundBtn, onMusicBtn, offMusicBtn;
    private String TITLE = " SETTINGS ";
    private String Language = "";
    private final GlyphLayout layout = new GlyphLayout(GameRunner.font, TITLE);


    private float lbtnY0 = 0, lbtnY = 400, cbtnY0 = 340, cbtnY = 400;
    private float sbtnY0 = -150, sbtnY = -400;

    private Sprite headder = new Sprite(new Texture("headder.png"));

    Background bg;
    public SettingsState(GameStateManager gameStateMenager) {
        super(gameStateMenager);
        camera.setToOrtho(false, GameRunner.WIDTH, GameRunner.HEIGHT);

        if(GameRunner.dm.loadDataString("language").equals("") || GameRunner.dm.loadDataString("language").equals("en")) {
            Language = "English";
        } else if(GameRunner.dm.loadDataString("language").equals("ru")) {
            Language = "Russian";
        }

        lenguageLBtn = new Button("button/arrL", camera.position.x-200, camera.position.y+lbtnY);
        lenguageLBtn.setScale(1.5f);
        lenguageRBtn = new Button("button/arrR", camera.position.x+200, camera.position.y+lbtnY);
        lenguageRBtn.setScale(1.5f);
        closeBtn = new Button("button/close", camera.position.x-550, camera.position.y+cbtnY);
        closeBtn.setScale(1.5f);
        onSoundBtn = new Button("button/audioOn", camera.position.x-200, camera.position.y+sbtnY);
        onSoundBtn.setScale(1.5f);
        offSoundBtn = new Button("button/audioOff", camera.position.x-200, camera.position.y+sbtnY);
        offSoundBtn.setScale(1.5f);
        onMusicBtn = new Button("button/musicOn", camera.position.x+200, camera.position.y+sbtnY);
        onMusicBtn.setScale(1.5f);
        offMusicBtn = new Button("button/musicOff", camera.position.x+200, camera.position.y+sbtnY);
        offMusicBtn.setScale(1.5f);
        bg = new Background(camera.position.x, camera.position.y, 0);

        headder.setCenter(camera.position.x, camera.position.y+330);
        headder.setScale(1, 0.5f);
    }
    float time = 2;
    @Override
    protected void hendleInput() {
        if(Gdx.input.isTouched()) {
            Vector3 vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            lenguageLBtn.updatePress(vec.x, vec.y);
            lenguageRBtn.updatePress(vec.x, vec.y);
            if(GameRunner.isPlay) {
                onMusicBtn.updatePress(vec.x, vec.y);
            } else {
                offMusicBtn.updatePress(vec.x, vec.y);
            }
            if(GameRunner.soundVol != 0) {
                onSoundBtn.updatePress(vec.x, vec.y);
            } else {
                offSoundBtn.updatePress(vec.x, vec.y);
            }
            closeBtn.updatePress(vec.x, vec.y);
        } else {
            if(lenguageLBtn.getIsPress()) {
                lenguageLBtn.setIsPress(false);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        time--;
                        if (time <= 2) {
                            time = 2;
                            if(Language == "English") {
                                Language = "Russian";
                                GameRunner.dm.addDataString("language", "ru");
                            } else {
                                Language = "English";
                                GameRunner.dm.addDataString("language", "en");
                            }
                        }
                    }
                }, 0.1f);
            }
            if(lenguageRBtn.getIsPress()) {
                lenguageRBtn.setIsPress(false);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        time--;
                        if (time <= 2) {
                            time = 2;
                            if(Language == "English") {
                                Language = "Russian";
                                GameRunner.dm.addDataString("language", "ru");
                            } else {
                                Language = "English";
                                GameRunner.dm.addDataString("language", "en");
                            }
                        }
                    }
                }, 0.1f);
            }
            if(onSoundBtn.getIsPress()) {
                onSoundBtn.setIsPress(false);
                if(GameRunner.soundVol == 0) GameRunner.soundVol = 0.5f;
                else GameRunner.soundVol = 0;
            }
            if(offSoundBtn.getIsPress()) {
                offSoundBtn.setIsPress(false);
                if(GameRunner.soundVol == 0) GameRunner.soundVol = 0.5f;
                else GameRunner.soundVol = 0;
            }
            if(onMusicBtn.getIsPress()) {
                onSoundBtn.setIsPress(false);
                GameRunner.isPlay = false;
            }
            if(offMusicBtn.getIsPress()) {
                offSoundBtn.setIsPress(false);
                GameRunner.isPlay = true;
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
            lenguageRBtn.setPress(vec.x, vec.y);
            lenguageLBtn.setPress(vec.x, vec.y);
            closeBtn.setPress(vec.x, vec.y);
            if(GameRunner.soundVol != 0) {
                onSoundBtn.setPress(vec.x, vec.y);
            } else {
                offSoundBtn.setPress(vec.x, vec.y);
            }
            if(GameRunner.isPlay) {
                onMusicBtn.setPress(vec.x, vec.y);
            } else {
                offMusicBtn.setPress(vec.x, vec.y);
            }
        }
    }

    @Override
    public void update(float delta) {
        hendleInput();

        if(lbtnY0 < lbtnY) {
            lbtnY -= speed*delta;
            lenguageLBtn.setPos(lenguageLBtn.getPos().x, camera.position.y + lbtnY);
            lenguageRBtn.setPos(lenguageRBtn.getPos().x, camera.position.y + lbtnY);
        } else {
            lbtnY = lbtnY0;
            lenguageLBtn.setPos(lenguageLBtn.getPos().x, camera.position.y + lbtnY);
            lenguageRBtn.setPos(lenguageRBtn.getPos().x, camera.position.y + lbtnY);
        }
        if(cbtnY0 < cbtnY) {
            cbtnY -= speed*delta;
            closeBtn.setPos(closeBtn.getPos().x, camera.position.y + cbtnY);
        } else {
            cbtnY = cbtnY0;
            closeBtn.setPos(closeBtn.getPos().x, camera.position.y + cbtnY);
        }
        if(sbtnY0 > sbtnY) {
            sbtnY += speed*delta;
            offSoundBtn.setPos(offSoundBtn.getPos().x, camera.position.y + sbtnY);
            onSoundBtn.setPos(onSoundBtn.getPos().x, camera.position.y + sbtnY);
            offMusicBtn.setPos(offMusicBtn.getPos().x, camera.position.y + sbtnY);
            onMusicBtn.setPos(onMusicBtn.getPos().x, camera.position.y + sbtnY);
        } else {
            sbtnY = sbtnY0;
            offSoundBtn.setPos(offSoundBtn.getPos().x, camera.position.y + sbtnY);
            onSoundBtn.setPos(onSoundBtn.getPos().x, camera.position.y + sbtnY);
            offMusicBtn.setPos(offMusicBtn.getPos().x, camera.position.y + sbtnY);
            onMusicBtn.setPos(onMusicBtn.getPos().x, camera.position.y + sbtnY);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        bg.getBgSprite().draw(sb);
        headder.draw(sb);
        GameRunner.font.draw(sb, TITLE, camera.position.x - 400, camera.position.y + 350);
        GameRunner.font.draw(sb, Language, camera.position.x - 100, camera.position.y+5);
        GameRunner.font.draw(sb, "Music: http://www.bensound.com", camera.position.x - 250, camera.position.y-200);
        //System.out.print(Language+"\n");
        lenguageLBtn.getSprite().draw(sb);
        lenguageRBtn.getSprite().draw(sb);
        closeBtn.getSprite().draw(sb);
        if(GameRunner.isPlay) {
            onMusicBtn.getSprite().draw(sb);
        } else {
            offMusicBtn.getSprite().draw(sb);
        }

        if(GameRunner.soundVol == 0) {
            offSoundBtn.getSprite().draw(sb);
        } else {
            onSoundBtn.getSprite().draw(sb);
        }
        sb.end();
    }

    @Override
    public void dispose() {
        lenguageLBtn.dispose();
        lenguageRBtn.dispose();
        onMusicBtn.dispose();
        offMusicBtn.dispose();
        onSoundBtn.dispose();
        offSoundBtn.dispose();
        closeBtn.dispose();
        headder.getTexture().dispose();
        bg.dispouse();
    }
}