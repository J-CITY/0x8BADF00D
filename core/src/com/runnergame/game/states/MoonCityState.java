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
import com.runnergame.game.sprites.MetaGame.Bank;
import com.runnergame.game.sprites.MetaGame.BankRoad;
import com.runnergame.game.sprites.MetaGame.Building;
import com.runnergame.game.sprites.MetaGame.Cafe;
import com.runnergame.game.sprites.MetaGame.Factory;
import com.runnergame.game.sprites.MetaGame.FactoryBlock;
import com.runnergame.game.sprites.MetaGame.FactoryRoad;
import com.runnergame.game.sprites.MetaGame.Fire;
import com.runnergame.game.sprites.MetaGame.FireRoad;
import com.runnergame.game.sprites.MetaGame.Gas;
import com.runnergame.game.sprites.MetaGame.HouseBuild;
import com.runnergame.game.sprites.MetaGame.HouseRoad;
import com.runnergame.game.sprites.MetaGame.Map;
import com.runnergame.game.sprites.MetaGame.MedBuild;
import com.runnergame.game.sprites.MetaGame.MedRoad;
import com.runnergame.game.sprites.MetaGame.NP;
import com.runnergame.game.sprites.MetaGame.NPRoad;
import com.runnergame.game.sprites.MetaGame.Pharmacy;
import com.runnergame.game.sprites.MetaGame.PharmacyRoad;
import com.runnergame.game.sprites.MetaGame.PoliceBuild;
import com.runnergame.game.sprites.MetaGame.PoliceRoad;
import com.runnergame.game.sprites.MetaGame.School;
import com.runnergame.game.sprites.MetaGame.SchoolRoad;
import com.runnergame.game.sprites.MetaGame.Shop;

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

    Button ncBtn, pauseBtn, runnerPlayBtn, metaGameBtn, buyCoinsBtn, metalBtn;
    private DataManager dm;
    private SpriteBatch tb;
    private long timeNC, timeNow;
    boolean isUpdate = false;
    private Array<Building> builds;
    OrthographicCamera cam_btn;

    GestureDetector gestureDetector;
    int RUNNER_LEVEL;

    private void loadBuilds() {
        builds.add(new Shop(21));
        builds.add(new Cafe(20));
        builds.add(new NP(19));
        builds.add(new NPRoad(18));
        builds.add(new FactoryBlock(17));
        builds.add(new Factory(16));
        builds.add(new FactoryRoad(15));
        builds.add(new Fire(14));
        builds.add(new School(13));
        builds.add(new SchoolRoad(12));
        builds.add(new FireRoad(11));
        builds.add(new PoliceRoad(10));
        builds.add(new PoliceBuild(9));
        builds.add(new Gas(8));
        builds.add(new Bank(7));
        builds.add(new BankRoad(6));
        builds.add(new PharmacyRoad(5));
        builds.add(new MedRoad(4));
        builds.add(new MedBuild(3));
        builds.add(new Pharmacy(2));
        builds.add(new HouseBuild(1, camera.position.x, camera.position.y));
        builds.add(new HouseRoad(0, camera.position.x, camera.position.y));
    }

    //private Sprite headder = new Sprite(new Texture("headder.png"));
    public MoonCityState(GameStateManager gameStateMenager) {
        super(gameStateMenager);
        GameRunner.adMobFlag = false;
        tb = new SpriteBatch();

        camera.setToOrtho(false, GameRunner.WIDTH / 2, GameRunner.HEIGHT / 2);
        gestureDetector = new GestureDetector(this);
        Gdx.input.setInputProcessor(gestureDetector);

        map = new Map(camera.position.x, camera.position.y);
        builds = new Array<Building>(21);
        loadBuilds();

        cam_btn = new OrthographicCamera(GameRunner.WIDTH, GameRunner.HEIGHT);
        cam_btn.update();
        GameRunner.now_coins = GameRunner.dm.load2("coins");
        GameRunner.now_metal = GameRunner.dm.load2("metal");
        pauseBtn = new Button("button/bar", cam_btn.position.x - 580, cam_btn.position.y + 320);
        pauseBtn.setScale(2);
        runnerPlayBtn = new Button("button/runnerPlay", cam_btn.position.x + 580, cam_btn.position.y - 320);
        runnerPlayBtn.setScale(2);
        //cardBtn = new Button("meta/infoBtn.png", cam_btn.position.x, cam_btn.position.y);
        //cardBtn.setScale(0.7f);

        ncBtn = new Button("button/box", cam_btn.position.x - 560, cam_btn.position.y - 100);
        ncBtn.setScale(0.7f);
        metaGameBtn = new Button("button/info", cam_btn.position.x - 580, cam_btn.position.y - 320);
        metaGameBtn.setScale(2);

        buyCoinsBtn = new Button("button/by", cam_btn.position.x - 350, cam_btn.position.y + 330);
        buyCoinsBtn.setScale(1.4f);
        metalBtn = new Button("button/by", cam_btn.position.x, cam_btn.position.y + 330);
        metalBtn.setScale(1.4f);

        timeNC = GameRunner.dm.loadDataTime("NCMODE");
        //headder.setCenter(cam_btn.position.x, cam_btn.position.y-430);
        if(GameRunner.playMusic != 0) {
            GameRunner.playMusic = 0;
            GameRunner.updateMusic = true;
        }
        RUNNER_LEVEL = GameRunner.dm.load2("level") + 1;
    }

    @Override
    protected void hendleInput() {

    }
    int helperMetaLvl = GameRunner.dm.load2("helperMetaLevel");
    int metaLvl = GameRunner.dm.load2("metaGameLevel");
    @Override
    public void update(float delta) {
        GameRunner.adMobFlag = false;
        if(helperMetaLvl == metaLvl) {
            if(metaLvl == 0 || metaLvl == 2 || metaLvl == 6 ||
                    metaLvl == 9 || metaLvl == 11 ||metaLvl == 16 ||
                    metaLvl == 19 || metaLvl == 24  ) {
                switch (metaLvl) {
                    case 0:
                        helperMetaLvl = 2;
                        break;
                    case 2:
                        helperMetaLvl = 6;
                        break;
                    case 6:
                        helperMetaLvl = 9;
                        break;
                    case 9:
                        helperMetaLvl = 11;
                        break;
                    case 11:
                        helperMetaLvl = 16;
                        break;
                    case 16:
                        helperMetaLvl = 19;
                        break;
                    case 19:
                        helperMetaLvl = 15;
                        break;
                    case 24:
                        helperMetaLvl = 0;
                        break;
                }
                isUpdate = true;
                gameStateMenager.push(new HelperMetaState(gameStateMenager, metaLvl, helperMetaLvl));
            }
        }
        I_AM_HERE = true;
        camera.update();

        if(isUpdate) {
            helperMetaLvl = GameRunner.dm.load2("helperMetaLevel");
            metaLvl = GameRunner.dm.load2("metaGameLevel");
            isUpdate = false;
            for(Building b : builds) {
                if(b.updateBuild()) {
                    Vector2 vec = b.getPos();
                    camera.position.x = vec.x;
                    camera.position.y = vec.y;
                    if(camera.position.x < -1270) {
                        camera.position.x = -1269;
                    }
                    if(camera.position.x > 1270) {
                        camera.position.x = 1269;
                    }
                    if(camera.position.y < -1370) {
                        camera.position.y = -1369;
                    }
                    if(camera.position.y > 1370) {
                        camera.position.y = 1369;
                    }
                }
            }
            builds.clear();
            loadBuilds();
            RUNNER_LEVEL = GameRunner.dm.load2("level") + 1;
        }
    }
    //Sprite spriteForDraw;
    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        map.getSprite().draw(sb);
        System.out.print("Pharmacy:   "+GameRunner.dm.load2("Pharmacy_lvl"));
        //System.out.print(map.getSprite().getWidth() + " " + map.getSprite().getHeight()+"!!!!!!!!!!!!!!\n" );
        for (Building b : builds) {
            b.getSprite().draw(sb);
        }
        sb.end();

        tb.setProjectionMatrix(cam_btn.combined);
        tb.begin();
        //headder.draw(tb);
        pauseBtn.getSprite().draw(tb);
        runnerPlayBtn.getSprite().draw(tb);
        metaGameBtn.getSprite().draw(tb);
        buyCoinsBtn.getSprite().draw(tb);
        metalBtn.getSprite().draw(tb);
        timeNow = System.currentTimeMillis();
        if(timeNC < timeNow) {
            ncBtn.getSprite().draw(tb);
        }
        if(timeNC >= timeNow) {
            long seconds = (timeNC - timeNow)/1000;
            long h = seconds / 3600;
            long m = (seconds - 3600*h) / 60;
            long s = (seconds - 3600*h - 60*m);
            GameRunner.font.draw(tb, "NCMode: " +  h + ":" + m + ":" + s, cam_btn.position.x-480,
                    cam_btn.position.y - 350);
        }
        GameRunner.font.draw(tb, "     COINS: " + GameRunner.now_coins, cam_btn.position.x - 520, cam_btn.position.y + 350);
        GameRunner.font.draw(tb, "     METAL: " + GameRunner.now_metal, cam_btn.position.x - 160, cam_btn.position.y + 350);
        GameRunner.font.draw(tb, RUNNER_LEVEL+"", cam_btn.position.x + 530, cam_btn.position.y - 300);
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
        //System.out.print(vec.x+" " + vec.y+"\n");
        Vector3 vec_btn = cam_btn.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        if(pauseBtn.collide(vec_btn.x, vec_btn.y)) {
            I_AM_HERE = false;
            GameRunner.soundPressBtn.play(GameRunner.soundVol);
            gameStateMenager.set(new MenuState(gameStateMenager));
        }
        /*if(cardBtn.collide(vec_btn.x, vec_btn.y) && visibleCard) {
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
        }*/
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
            GameRunner.soundPressBtn.play(GameRunner.soundVol);
            gameStateMenager.push(new SelectLevel(gameStateMenager));
        }
        if(metaGameBtn.collide(vec_btn.x, vec_btn.y)) {
            I_AM_HERE = false;
            isUpdate = true;
            GameRunner.soundPressBtn.play(GameRunner.soundVol);
            gameStateMenager.push(new MetaTasksState(gameStateMenager));
        }
        if(buyCoinsBtn.collide(vec_btn.x, vec_btn.y)) {
            I_AM_HERE = false;
            isUpdate = true;
            GameRunner.soundPressBtn.play(GameRunner.soundVol);
            gameStateMenager.push(new InAppBillingState(gameStateMenager));
        }
        timeNow = System.currentTimeMillis();
        if(timeNC < timeNow) {
            if(ncBtn.collide(vec_btn.x, vec_btn.y)) {
                I_AM_HERE = false;
                GameRunner.dm.addDataTime("NCMODE", timeNow + 86400000);
                GameRunner.soundPressBtn.play(GameRunner.soundVol);
                //gameStateMenager.set(new NCState(gameStateMenager));
                gameStateMenager.push(new BonusState(gameStateMenager));
                return true;
            }
        }

        boolean flag = false;
        /*card = 0;
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
        }*/
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
        camera.translate(-deltaX*camera.zoom, deltaY*camera.zoom);
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
    float SPRITE_SCALE = 1;
    @Override
    public boolean zoom(float initialDistance, float distance) {
        if(!I_AM_HERE)
            return false;
        // System.out.print(camera.zoom+"\n");
        if (initialDistance >= distance) {
            if(camera.zoom < 2)
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