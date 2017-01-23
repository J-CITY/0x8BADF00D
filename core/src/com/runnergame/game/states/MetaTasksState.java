package com.runnergame.game.states;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.runnergame.game.GameRunner;
import com.runnergame.game.Levels;
import com.runnergame.game.MetaLevels;
import com.runnergame.game.sprites.Background;
import com.runnergame.game.sprites.Button;

public class MetaTasksState extends State {
    private Button doBtn, closeBtn;
    private float dbtnY0 = 0, dbtnY = 400, cbtnY0 = 340, cbtnY = 400;
    private GlyphLayout layout;

    public MetaLevels metaLevels;

    Background bg;

    private String TITLE;
    private String TASK;
    private float prize;
    private float price;
    float metal;
    private Sprite headder = new Sprite(new Texture("headder.png"));
    //private Sprite bgs = new Sprite(new Texture("meta/helper/lvl.png"));

    public MetaTasksState(GameStateManager gameStateMenager) {
        super(gameStateMenager);
        GameRunner.adMobFlag = true;
        if(GameRunner.dm.loadDataString("language").equals("") || GameRunner.dm.loadDataString("language").equals("en")) {
            metaLevels = new MetaLevels("xml/metaLevels_en.xml");
        } else if(GameRunner.dm.loadDataString("language").equals("ru")) {
            metaLevels = new MetaLevels("xml/metaLevels_ru.xml");
        }
        metaLevels.load();
        if(metaLevels.levels.size == metaLevels.level) {
            TITLE = "No tasks";
        } else {
            TITLE = "Level " + metaLevels.level;
            TASK = metaLevels.levels.get(metaLevels.level).discription + " Price: " + metaLevels.levels.get(metaLevels.level).price;
            prize = metaLevels.levels.get(metaLevels.level).prize;
            price = metaLevels.levels.get(metaLevels.level).price;
        }
        //GameRunner.dm.addData2("metaGameLevel", 0);

        metal = GameRunner.dm.load2("metal");

        layout = new GlyphLayout(GameRunner.font, TITLE);

        camera.setToOrtho(false, GameRunner.WIDTH, GameRunner.HEIGHT);

        doBtn = new Button("button/perfom", camera.position.x + 400, camera.position.y+dbtnY);
        closeBtn = new Button("button/close", camera.position.x-530, camera.position.y+cbtnY);

        bg = new Background(camera.position.x, camera.position.y, 0);
        headder.setCenter(camera.position.x, camera.position.y+330);
        headder.setScale(1, 0.5f);
        //bgs.setCenter(camera.position.x, camera.position.y);
    }
    float time = 2;

    @Override
    protected void hendleInput() {
        if(Gdx.input.isTouched()) {
            Vector3 vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            doBtn.updatePress(vec.x, vec.y);
            closeBtn.updatePress(vec.x, vec.y);
        } else {
            if(doBtn.getIsPress()) {
                doBtn.setIsPress(false);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        time--;
                        if (time <= 2) {
                            time = 2;
                            if(metal >= price) {
                                GameRunner.dm.addData2("metal", (int) (metal - price));
                                GameRunner.now_metal = GameRunner.dm.load2("metal");
                                GameRunner.dm.addData2("coins", (int) (GameRunner.dm.load2("coins") + prize));
                                GameRunner.now_coins = GameRunner.dm.load2("coins");
                                int p = GameRunner.dm.load2(metaLevels.levels.get(metaLevels.level).param+"_lvl");
                                GameRunner.dm.addData2(metaLevels.levels.get(metaLevels.level).param+"_lvl", p + 1);
                                System.out.print(metaLevels.levels.get(metaLevels.level).param+"_lvl\n");
                                GameRunner.dm.addData2("metaGameLevel", metaLevels.level+1);
                                gameStateMenager.pop();
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
            doBtn.setPress(vec.x, vec.y);
            closeBtn.setPress(vec.x, vec.y);
        }
    }

    @Override
    public void update(float delta) {
        hendleInput();

        if(dbtnY0 < dbtnY) {
            dbtnY -= speed*delta;
            doBtn.setPos(doBtn.getPos().x, camera.position.y + dbtnY);
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
        //bgs.draw(sb);
        headder.draw(sb);
        //bg.getBgSprite().draw(sb);
        GameRunner.font.draw(sb, TITLE, camera.position.x - 400, camera.position.y + 350);
        if(metaLevels.levels.size > metaLevels.level) {
            GameRunner.font.draw(sb, TASK,  camera.position.x-500, camera.position.y);
            doBtn.getSprite().draw(sb);
        }
        closeBtn.getSprite().draw(sb);
        sb.end();
    }

    @Override
    public void dispose() {
        doBtn.dispose();
        closeBtn.dispose();
    }
}
