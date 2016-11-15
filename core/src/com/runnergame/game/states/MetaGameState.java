package com.runnergame.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.runnergame.game.GameRunner;
import com.runnergame.game.sprites.Button;
import com.runnergame.game.sprites.Unit;

public class MetaGameState extends State {

    private void limitFPS() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    Button ncBtn;
    Button pauseBtn, runnerPlayBtn;
    boolean isUpdate = false;
    private Array<Unit> units;
    int UNIT_COUNT = 16;
    private DataManager dm;
    private int energy=5;

    private SpriteBatch tb;

    private long timeNC, timeNow;


    public MetaGameState(GameStateManager gameStateMenager) {
        super(gameStateMenager);
        camera.setToOrtho(false, GameRunner.WIDTH / 2, GameRunner.HEIGHT / 2);

        tb = new SpriteBatch();

        dm = new DataManager("GameRunner");
        dm.setParam("coins");
        GameRunner.new_coins = dm.load();
        dm.setParam("star");
        GameRunner.new_stars = dm.load();

        units = new Array<Unit>();
        for(int i = 0; i < UNIT_COUNT/4; ++i) {
            for(int j = 0; j < UNIT_COUNT/4; ++j) {
                units.add(new Unit(camera.position.x - 35*2 + i*35,
                        camera.position.y - 35*2 + j*35, i*4+j));
                energy += units.get(units.size-1).energy;
            }
        }
        pauseBtn = new Button("Pause.png", camera.position.x - 280, camera.position.y + 150, 1, 1);
        pauseBtn.setScale(0.5f);
        runnerPlayBtn = new Button("Play.png", camera.position.x + 280, camera.position.y + 150, 1, 1);
        runnerPlayBtn.setScale(0.5f);

        ncBtn = new Button("nc.png", camera.position.x + 280, camera.position.y - 150, 1, 1);
        limitFPS();

        timeNC = dm.loadDataTime("NCMODE");
    }

    @Override
    protected void hendleInput() {

        if(Gdx.input.isTouched()) {
            Vector3 vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if(pauseBtn.collide(vec.x, vec.y)) {
                gameStateMenager.set(new MenuState(gameStateMenager));
            }
            if(runnerPlayBtn.collide(vec.x, vec.y)) {
                gameStateMenager.push(new SelectLevel(gameStateMenager));
            }
            timeNow = System.currentTimeMillis();
            if(timeNC < timeNow) {
                if(ncBtn.collide(vec.x, vec.y)) {
                    //if(GameRunner.new_stars >= 5) {
                        GameRunner.new_stars -= 5;
                        dm.addData2("star", GameRunner.new_stars);
                        dm.addDataTime("NCMODE", timeNow + 86400000);
                        gameStateMenager.set(new NCState(gameStateMenager));
                    //}
                    return;
                }
            }
            /*if(ncBtn.collide(vec.x, vec.y)) {
                if(GameRunner.new_stars >= 5) {
                    GameRunner.new_stars -= 5;
                    gameStateMenager.push(new NCState(gameStateMenager));
                }
                return;
            }*/
            for (Unit u : units) {
                isUpdate = true;
                if(u.collide(vec.x, vec.y)) {
                    switch (u.type) {
                        case 0:
                            gameStateMenager.push(new CardUnlockState(gameStateMenager, u.num));
                            break;
                        case 1:
                            gameStateMenager.push(new CardSetState(gameStateMenager, u.num));
                            break;
                        default:
                            gameStateMenager.push(new CardInfoState(gameStateMenager, u.num, u.getSprite(), u.getDiscription()));
                            break;
                    }

                }
            }
        }
    }
    boolean onTime = false;
    int TIME = 10;
    int time = TIME;
    @Override
    public void update(float delta) {
        if(!onTime) {
            onTime = !onTime;
            com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {

                @Override
                public void run() {
                    time--;
                    if (time == 0) {
                        time = TIME;
                        dm.setParam("coins");
                        GameRunner.new_coins = dm.load();
                        dm.setParam("star");
                        GameRunner.new_stars = dm.load();
                    }
                    onTime = !onTime;
                }
            }, 1);
        }
        hendleInput();
        if(isUpdate) {
            isUpdate = false;
            units.clear();
            energy=5;
            for(int i = 0; i < UNIT_COUNT/4; ++i) {
                for(int j = 0; j < UNIT_COUNT/4; ++j) {
                    units.add(new Unit(camera.position.x - 35*2 + i*35,
                            camera.position.y - 35*2 + j*35, i*4+j));
                    energy += units.get(units.size-1).energy;
                }
            }
        }
        if(energy >= 0) {
            for (Unit b : units) {
                b.update(delta);
            }
        }

        camera.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        pauseBtn.getSprite().draw(sb);
        runnerPlayBtn.getSprite().draw(sb);
        for (Unit b : units) {
            b.getSprite().draw(sb);
        }

        timeNow = System.currentTimeMillis();
        if(timeNC < timeNow) {
            ncBtn.getSprite().draw(sb);
        }

        sb.end();

        tb.setProjectionMatrix(camera.combined.scl(0.5f));
        tb.begin();
        if(timeNC >= timeNow) {
            long seconds = (timeNC - timeNow)/1000;
            long h = seconds / 3600;
            long m = (seconds - 3600*h) / 60;
            long s = (seconds - 3600*h - 60*m);
            GameRunner.font.draw(tb, "NCMode: " +  h + ":" + m + ":" + s, camera.position.x+680,
                    camera.position.y - 150);
        }
        GameRunner.font.draw(tb, "COINS: " + GameRunner.new_coins + " STARS: " + GameRunner.new_stars +  " ENERGY: " + energy, camera.position.x - 280, camera.position.y - 160);
        tb.end();
    }

    @Override
    public void dispose() {
        for(Unit b : units) {
            b.dispose();
        }
        pauseBtn.dispose();
        runnerPlayBtn.dispose();
    }
}
