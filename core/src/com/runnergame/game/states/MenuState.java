package com.runnergame.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.runnergame.game.GameRunner;
import com.runnergame.game.MetaLevels;
import com.runnergame.game.sprites.Background;
import com.runnergame.game.sprites.Button;
import com.runnergame.game.sprites.MetaGame.Building;

public class MenuState extends State {

    private Button playBtn, onSoundBtn, offSoundBtn, shopBtn, settingsBtn;
    private float pbtnY0 = 0, pbtnY = 400;
    private float sbtnY0 = -250, sbtnY = -400;
    private float shbtnY0 = -120, shbtnY = -400;
    private float stbtnY0 = 250, stbtnY = 400;
    private String TITLE = "0x8BADF00D";
    private final GlyphLayout layout = new GlyphLayout(GameRunner.font, TITLE);
    private Sprite CCS = new Sprite( new Texture("CC.png"));
    private Sprite Loading = new Sprite( new Texture("loading.png"));
    private boolean LOADING_bool = false;
    Background bg;
    //Button GB;
    public MenuState(GameStateManager gameStateMenager) {
        super(gameStateMenager);
        GameRunner.adMobFlag = true;
        camera.setToOrtho(false, GameRunner.WIDTH, GameRunner.HEIGHT);
        String leng;
        if(GameRunner.dm.loadDataString("language").equals("") || GameRunner.dm.loadDataString("language").equals("en")) {
            leng = new String("en");
        } else if(GameRunner.dm.loadDataString("language").equals("ru")) {
            leng = new String("ru");
        }

        playBtn = new Button("button/playT", camera.position.x, camera.position.y+pbtnY);
        onSoundBtn = new Button("button/musicOn", camera.position.x-530, camera.position.y+sbtnY);
        offSoundBtn = new Button("button/musicOff", camera.position.x-530, camera.position.y+sbtnY);
        shopBtn = new Button("button/shoppingCart", camera.position.x-530, camera.position.y+shbtnY);
        settingsBtn = new Button("button/settings", camera.position.x-530, camera.position.y+stbtnY);
        //GB = new Button("button/shoppingCart", camera.position.x+530, camera.position.y);

        bg = new Background(camera.position.x, camera.position.y, 0);
        CCS.setCenter(camera.position.x, camera.position.y+250);
        Loading.setCenter(camera.position.x, camera.position.y);
    }
    private boolean onSoundBtnPress=false, playBtnPress=false, shopBtnPress=false, settingsBtnPress = false;
    float time = 2;

    @Override
    protected void hendleInput() {
        //System.out.print(time +"\n");
        if(Gdx.input.isTouched()) {
            Vector3 vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            playBtn.updatePress(vec.x, vec.y);
            if(GameRunner.isPlay) {
                onSoundBtn.updatePress(vec.x, vec.y);
            } else {
                offSoundBtn.updatePress(vec.x, vec.y);
            }
            shopBtn.updatePress(vec.x, vec.y);
            settingsBtn.updatePress(vec.x, vec.y);
        } else {
            if(playBtn.getIsPress()) {
                playBtn.setIsPress(false);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        time--;
                        if (time <= 2) {
                            time = 2;
                            LOADING_bool = false;
                            gameStateMenager.set(new MoonCityState(gameStateMenager));
                        }
                    }
                }, 0.1f);
            }
            if(onSoundBtn.getIsPress()) {
                onSoundBtn.setIsPress(false);
                GameRunner.isPlay = !GameRunner.isPlay;
            }
            if(offSoundBtn.getIsPress()) {
                offSoundBtn.setIsPress(false);
                GameRunner.isPlay = !GameRunner.isPlay;
            }
            if(shopBtn.getIsPress()) {
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
            if(settingsBtn.getIsPress()) {
                settingsBtn.setIsPress(false);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        time--;
                        if (time <= 1) {
                            time = 2;
                            gameStateMenager.push(new SettingsState(gameStateMenager));
                        }
                    }
                }, 0.1f);
            }
        }
        if(Gdx.input.justTouched()) {
            Vector3 vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if (playBtn.setPress(vec.x, vec.y)) {
                LOADING_bool = true;
            }
            settingsBtn.setPress(vec.x, vec.y);
            shopBtn.setPress(vec.x, vec.y);
            if(GameRunner.isPlay) {
                onSoundBtn.setPress(vec.x, vec.y);
            } else {
                offSoundBtn.setPress(vec.x, vec.y);
            }

            //if(GB.collide(vec.x, vec.y)) {
            //   gameStateMenager.push(new InAppBillingState(gameStateMenager));
            //}
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
        if(stbtnY0 < stbtnY) {
            stbtnY -= speed*delta;
        } else {
            stbtnY = stbtnY0;
        }
        shopBtn.setPos(shopBtn.getPos().x, camera.position.y + shbtnY);
        playBtn.setPos(playBtn.getPos().x, camera.position.y + pbtnY);
        offSoundBtn.setPos(offSoundBtn.getPos().x, camera.position.y + sbtnY);
        onSoundBtn.setPos(onSoundBtn.getPos().x, camera.position.y + sbtnY);
        settingsBtn.setPos(settingsBtn.getPos().x, camera.position.y + stbtnY);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        bg.getBgSprite().draw(sb);
        CCS.draw(sb);
        //GameRunner.font.draw(sb, TITLE, (GameRunner.WIDTH - layout.width) / 2, GameRunner.HEIGHT - 100);
        playBtn.getSprite().draw(sb);
        shopBtn.getSprite().draw(sb);
        settingsBtn.getSprite().draw(sb);
        //GB.getSprite().draw(sb);
        if(GameRunner.isPlay) {
            onSoundBtn.getSprite().draw(sb);
        } else {
            offSoundBtn.getSprite().draw(sb);
        }
        if (LOADING_bool) {
            Loading.draw(sb);
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