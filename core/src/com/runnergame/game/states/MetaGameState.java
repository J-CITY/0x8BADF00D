package com.runnergame.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.runnergame.game.Colors;
import com.runnergame.game.Constants;
import com.runnergame.game.GameRunner;
import com.runnergame.game.sprites.Block;
import com.runnergame.game.sprites.BlockFloor;
import com.runnergame.game.sprites.Button;
import com.runnergame.game.sprites.Coin;
import com.runnergame.game.sprites.Player;
import com.runnergame.game.sprites.Unit;

public class MetaGameState extends State {

    private final float targetFPS = 1f; // Any value
    private final long targetDelay = 1000 / (long) targetFPS;
    private long diff, start;

    private void limitFPS() {
        //diff = System.currentTimeMillis() - start;

        /*if (diff < targetDelay) {
            try {
                Thread.sleep(targetDelay - diff);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        //}

        //start = System.currentTimeMillis();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    int star=0;

    Button pauseBtn, runnerPlayBtn;
    boolean isUpdate = false;
    private Array<Unit> units;
    int UNIT_COUNT = 16;
    private DataManager dm;
    private int energy=5;

    private SpriteBatch tb;

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
                if(units.get(units.size-1).takeParam == 3) {
                    energy -= units.get(units.size-1).takeCount;
                }
                if(units.get(units.size-1).giveParam == 3) {
                    energy += units.get(units.size-1).giveCount;
                }
            }
        }

        pauseBtn = new Button("Pause.png", camera.position.x - 280, camera.position.y + 150);
        //pauseBtn.getBounds().setCenter(camera.position.x - 280, camera.position.y + 150);
        runnerPlayBtn = new Button("Play.png", camera.position.x + 280, camera.position.y + 150);
        limitFPS();
    }

    @Override
    protected void hendleInput() {

        if(Gdx.input.isTouched()) {//!!!!!!!!!!!
            Vector3 vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if(pauseBtn.collide(vec.x, vec.y)) {
                gameStateMenager.push(new PauseState(gameStateMenager));
            }
            if(runnerPlayBtn.collide(vec.x, vec.y)) {
                gameStateMenager.set(new PlayState(gameStateMenager));
            }
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
        //Gdx.app.log("GameScreen FPS", (1/delta) + "");
        hendleInput();
        if(isUpdate) {
            isUpdate = false;
            units.clear();
            energy=5;
            for(int i = 0; i < UNIT_COUNT/4; ++i) {
                for(int j = 0; j < UNIT_COUNT/4; ++j) {
                    units.add(new Unit(camera.position.x - 35*2 + i*35,
                            camera.position.y - 35*2 + j*35, i*4+j));
                    if(units.get(units.size-1).takeParam == 3) {
                        energy -= units.get(units.size-1).takeCount;
                    }
                    if(units.get(units.size-1).giveParam == 3) {
                        energy += units.get(units.size-1).giveCount;
                    }
                }
            }
        }
        //System.out.print("Coins: " + coin + "  Stars: " + star + "\n");

        for (Unit b : units) {
            b.update(delta);
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
        //GameRunner.font.draw(sb, "COINS: " + GameRunner.new_coins, camera.position.x - 280, camera.position.y - 160);
        //GameRunner.font.draw(sb, "STARS: " + GameRunner.new_stars, camera.position.x - 280, camera.position.y - 135);
        //GameRunner.font.draw(sb, "ENERGY: " + energy, camera.position.x - 280, camera.position.y - 110);
        sb.end();

        //mx4Font.setToRotation(new Vector3(0, 0, 0), 0);
        tb.setProjectionMatrix(camera.combined.scl(0.5f));
        tb.begin();
        GameRunner.font.draw(tb, "COINS: " + GameRunner.new_coins + " STARS: " + GameRunner.new_stars +  " ENERGY: " + energy, camera.position.x - 280, camera.position.y - 160);
        tb.end();
    }

    @Override
    public void dispose() {
        for(Unit b : units) {
            b.dispose();
        }
    }
}
