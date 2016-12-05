package com.runnergame.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.runnergame.game.GameRunner;
import com.runnergame.game.Levels;
import com.runnergame.game.sprites.Button;
import com.runnergame.game.sprites.MetaGame.Building;
import com.runnergame.game.sprites.MetaGame.CityBuild;
import com.runnergame.game.sprites.MetaGame.HouseBuild;
import com.runnergame.game.sprites.MetaGame.Map;
import com.runnergame.game.sprites.MetaGame.MedBuild;
import com.runnergame.game.sprites.MetaGame.PoliceBuild;
import com.runnergame.game.sprites.MetaGame.RocketBuild;
import com.runnergame.game.sprites.MetaGame.StadiumBuild;

public class MoonCityState extends State implements GestureDetector.GestureListener
{
    boolean visibleCard = false;
    boolean visibleCardBtn = false;
    int priceTex;
    Sprite cardSprite;
    Button cardBtn;
    int card = 0;

    boolean I_AM_HERE = true;
    Map map;

    Button ncBtn, pauseBtn, runnerPlayBtn;
    private DataManager dm;
    private SpriteBatch tb;
    private long timeNC, timeNow;
    boolean isUpdate = false;
    private Array<Building> builds;

    OrthographicCamera cam_btn;

    GestureDetector gestureDetector;
    private void loadBuilds() {
        builds.add(new CityBuild(0));
        builds.add(new HouseBuild(1));
        builds.add(new PoliceBuild(2));
        builds.add(new MedBuild(3));
        builds.add(new StadiumBuild(4));
        builds.add(new RocketBuild(5));
    }

    private Sprite headder = new Sprite(new Texture("headder.png"));
    public MoonCityState(GameStateManager gameStateMenager) {
        super(gameStateMenager);
        tb = new SpriteBatch();
        map = new Map(camera.position.x, camera.position.y);
        builds = new Array<Building>(10);
        loadBuilds();

        camera.setToOrtho(false, GameRunner.WIDTH / 2, GameRunner.HEIGHT / 2);
        gestureDetector = new GestureDetector(this);
        Gdx.input.setInputProcessor(gestureDetector);

        cam_btn = new OrthographicCamera(GameRunner.WIDTH, GameRunner.HEIGHT);
        cam_btn.update();
        GameRunner.now_coins = GameRunner.dm.load2("coins");
        GameRunner.now_metal = GameRunner.dm.load2("metal");
        pauseBtn = new Button("Pause.png", cam_btn.position.x - 550, cam_btn.position.y + 320, 1, 1);
        pauseBtn.setScale(0.7f);
        runnerPlayBtn = new Button("Play.png", cam_btn.position.x + 550, cam_btn.position.y + 320, 1, 1);
        runnerPlayBtn.setScale(0.7f);
        cardBtn = new Button("meta/infoBtn.png", cam_btn.position.x, cam_btn.position.y, 1, 1);
        cardBtn.setScale(0.7f);

        ncBtn = new Button("nc.png", cam_btn.position.x + 550, cam_btn.position.y - 320, 1, 1);
        //sleep();

        timeNC = GameRunner.dm.loadDataTime("NCMODE");
        headder.setCenter(cam_btn.position.x, cam_btn.position.y-450);
    }

    @Override
    protected void hendleInput() {

    }
    @Override
    public void update(float delta) {
        //System.out.print(camera.position.x+" "+ camera.position.y+"\n");
        I_AM_HERE = true;
        camera.update();

        if(isUpdate) {
            isUpdate = false;
            builds.clear();
            loadBuilds();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        map.getSprite().draw(sb);
        for (Building b : builds) {
            b.getSprite().draw(sb);
        }
        sb.end();

        tb.setProjectionMatrix(cam_btn.combined);
        tb.begin();
        headder.draw(tb);
        pauseBtn.getSprite().draw(tb);
        runnerPlayBtn.getSprite().draw(tb);
        timeNow = System.currentTimeMillis();
        if(timeNC < timeNow) {
            ncBtn.getSprite().draw(tb);
        }
        if(timeNC >= timeNow) {
            long seconds = (timeNC - timeNow)/1000;
            long h = seconds / 3600;
            long m = (seconds - 3600*h) / 60;
            long s = (seconds - 3600*h - 60*m);
            GameRunner.font.draw(tb, "NCMode: " +  h + ":" + m + ":" + s, cam_btn.position.x+80,
                    cam_btn.position.y - 350);
        }
        GameRunner.font.draw(tb, "COINS: " + GameRunner.now_coins + "  METAL: " + GameRunner.now_metal, cam_btn.position.x - 680, cam_btn.position.y - 350);
        if(visibleCard) {
            cardSprite.draw(tb);
            if(visibleCardBtn) {
                cardBtn.getSprite().draw(tb);
                GameRunner.font.draw(tb, "PRICE: " + priceTex, cardBtn.getPos().x, cardBtn.getPos().y - 50);
            }
        }
        tb.end();

    }

    @Override
    public void dispose() {

    }
    int activeB;
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        if(!I_AM_HERE)
            return false;
        Vector3 vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        Vector3 vec_btn = cam_btn.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        if(pauseBtn.collide(vec_btn.x, vec_btn.y)) {
            I_AM_HERE = false;
            gameStateMenager.set(new MenuState(gameStateMenager));
        }
        if(cardBtn.collide(vec_btn.x, vec_btn.y) && visibleCard) {
            I_AM_HERE = false;
            int price = builds.get(activeB).getPrice();
            int metal = GameRunner.dm.load2("metal");
            if(price <= metal && builds.get(activeB).getLevel_now() < builds.get(activeB).getMax_level()) {
                int p = GameRunner.dm.load2(builds.get(activeB).getParam());
                if (builds.get(activeB).update(p)) {
                    GameRunner.dm.addData2(builds.get(activeB).getParam(), p + 1);
                    GameRunner.dm.addData2("metal", metal - price);
                    //gameStateMenager.set(new MoonCityState(gameStateMenager));
                }
            }
            //gameStateMenager.push(new BuildingInfoState(gameStateMenager, builds.get(card)));
        }
        if(runnerPlayBtn.collide(vec_btn.x, vec_btn.y)) {
            I_AM_HERE = false;
            /*int _lvl = dm.load2("level");
            if(_lvl < Levels.levels.size) {
                PlayState.lvl = _lvl;
                if (PlayState.lvl >= 0 && PlayState.lvl <= 2) {
                    HelperState.lvl = PlayState.lvl;
                    gameStateMenager.set(new HelperState(gameStateMenager));
                } else {
                    gameStateMenager.set(new PlayState(gameStateMenager));
                }
            }*/
            gameStateMenager.push(new SelectLevel(gameStateMenager));
        }
        timeNow = System.currentTimeMillis();
        if(timeNC < timeNow) {
            if(ncBtn.collide(vec_btn.x, vec_btn.y)) {
                I_AM_HERE = false;
                GameRunner.dm.addDataTime("NCMODE", timeNow + 86400000);
                gameStateMenager.set(new NCState(gameStateMenager));
                return true;
            }
        }
        boolean flag = false;
        card = 0;
        int i = 0;
        for (Building b : builds) {
            isUpdate = true;
            if(b.collide(vec.x, vec.y)) {
                flag = true;
                card = i;
                activeB = i;
                cardSprite = b.getCardSprite();
                cardSprite.setCenter(cam_btn.position.x, cam_btn.position.y);
                cardBtn.setPos(cam_btn.position.x, cam_btn.position.y - 50);
                if(b.getLevel_now() < b.getMax_level()) {
                    visibleCardBtn = true;
                    priceTex = b.getPrice();
                } else {
                    visibleCardBtn = false;
                }
                //I_AM_HERE = false;
                //gameStateMenager.set(new BuildingInfoState(gameStateMenager, b));
                //return true;
            }
            i++;
        }
        if(flag) {
            visibleCard = true;
        } else {
            visibleCard = false;
        }
        return true;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        if(!I_AM_HERE)
            return false;
        float _x = camera.position.x, _y = camera.position.y;
        Vector3 touchPos = new Vector3(x,y,0);
        camera.unproject(touchPos);
        camera.translate(-deltaX, deltaY);
        if(camera.position.x < -1270 || camera.position.x > 1270) {
            camera.position.x = _x;
        }
        if(camera.position.y < -1370 || camera.position.y > 1370) {
            camera.position.y = _y;
        }
        return true;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        if(!I_AM_HERE)
            return false;
        System.out.print(camera.zoom+"\n");
        if (initialDistance >= distance) {
            if(camera.zoom < 3)
                camera.zoom += 0.02;
        } else {
            if(camera.zoom > 0.5)
                camera.zoom -= 0.02;
        }
        return true;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
