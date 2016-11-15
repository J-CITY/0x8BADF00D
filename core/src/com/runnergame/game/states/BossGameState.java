package com.runnergame.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.runnergame.game.GameRunner;
import com.runnergame.game.sprites.Bullet;
import com.runnergame.game.sprites.Button;
import com.runnergame.game.sprites.Coin;
import com.runnergame.game.sprites.Player;

import java.util.Iterator;

public class BossGameState extends State {
    public static int lvl=0;
    private Player player;

    Button pauseBtn;

    private Array<Bullet> bullets;

    private SpriteBatch tb;

    private Texture bullet_tex;
    private int TIME;
    private int SPEED;

    private int time;

    public BossGameState(GameStateManager gameStateMenager) {
        super(gameStateMenager);
        camera.setToOrtho(false, GameRunner.WIDTH / 2, GameRunner.HEIGHT / 2);
        tb = new SpriteBatch();
        bullet_tex = new Texture("bullet.png");
        bullets = new Array<Bullet>();
        player = new Player((int)camera.position.x, (int)camera.position.y);
        player.setScale(0.5f);

        TIME = (int)(GameRunner.levels.levels.get(lvl).charAt(0)-48) * 10;
        SPEED = (int)GameRunner.levels.levels.get(lvl).charAt(1)-48;

        time = TIME;
        pauseBtn = new Button("Pause.png", camera.position.x - 280, camera.position.y + 150, 1, 1);
        pauseBtn.getBounds().setCenter(camera.position.x - 280, camera.position.y + 150);
        pauseBtn.setScale(0.5f);
    }

    @Override
    protected void hendleInput() {
        if(Gdx.input.justTouched()) {
            Vector3 vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if(pauseBtn.collide(vec.x, vec.y)) {
                PauseState.whatGame = 1;
                gameStateMenager.push(new PauseState(gameStateMenager));
            }
            player.changeColor();
        }
    }


    private boolean onTime = false;
    long lastDropTime;
    @Override
    public void update(float delta) {
        if(!onTime) {
            onTime = !onTime;
            Timer.schedule(new Timer.Task() {

                @Override
                public void run() {
                    time--;
                    if (time == 0) {
                        //time = TIME;
                        GameRunner.new_coins += 100;
                        DataManager dm = new DataManager("GameRunner");
                        dm.setParam("level");
                        int l = dm.load();
                        if(l == lvl)
                            dm.addData(lvl+1);
                        gameStateMenager.set(new WinState(gameStateMenager, lvl));
                    }
                    onTime = !onTime;
                }
            }, 1);
        }

        hendleInput();
        Iterator<Bullet> iter = bullets.iterator();
        while (iter.hasNext()) {
            Bullet bul = iter.next();
            if(!bul.isLife) {
                iter.remove();
            }
            bul.update(delta);
            if(bul.collide(player.getBounds())) {
                bul.isLife = false;
                if(player.color == bul.COLOR) {
                    gameStateMenager.set(new GameOverBoss(gameStateMenager));
                }
            }
        }

        if(TimeUtils.nanoTime() - lastDropTime > 1000000000) {
            bullets.add(new Bullet(SPEED, camera.position.x, camera.position.y,  bullet_tex));
            lastDropTime = TimeUtils.nanoTime();
        }

        camera.update();
    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        player.getSprite().draw(sb);

        for (Bullet b : bullets) {
            b.getSprite().draw(sb);
        }

        pauseBtn.getSprite().draw(sb);
        GameRunner.font.draw(sb, "" + time, camera.position.x-20, camera.position.y+20);
        sb.end();

        tb.setProjectionMatrix(camera.combined.scl(0.5f));
        tb.begin();

        GameRunner.font.draw(tb, "COINS: " + GameRunner.new_coins + " STARS: " + GameRunner.new_stars, camera.position.x - 280, camera.position.y - 160);
        tb.end();
    }

    @Override
    public void dispose() {
        player.dispose();
    }
}
