package com.runnergame.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.runnergame.game.GameRunner;
import com.runnergame.game.sprites.Background;
import com.runnergame.game.sprites.Button;

public class BonusState extends State {
    private Array<Button> bonus;
    private Button okBtn;
    private String TITLE = "EVERYDAY BONUS";
    private final GlyphLayout layout = new GlyphLayout(GameRunner.font, TITLE);
    Background bg;
    public BonusState(GameStateManager gameStateMenager) {
        super(gameStateMenager);
        GameRunner.adMobFlag = false;
        camera.setToOrtho(false, GameRunner.WIDTH, GameRunner.HEIGHT);
        bonus = new Array<Button>(9);
        float y = -150;
        for(int i = 0; i < 3; ++i) {
            float x = -150;
            for(int j = 0; j < 3; ++j) {
                bonus.add(new Button("button/box", camera.position.x+x, camera.position.y+y));
                bonus.get(bonus.size-1).setScale(0.6f);
                x+=150;
            }
            y+=100;
        }
        okBtn = new Button("button/takePrize", camera.position.x, camera.position.y-220);

        bg = new Background(camera.position.x, camera.position.y, 0);
    }
    float time = 2;
    private boolean PRIZE_OK = false;
    private int Coins, Metal;
    @Override
    protected void hendleInput() {
        //System.out.print(time +"\n");
        if(Gdx.input.isTouched()) {
            Vector3 vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if(!PRIZE_OK) {
                for(Button b : bonus) {
                    b.updatePress(vec.x, vec.y);
                }
            }
            if(PRIZE_OK)
                okBtn.updatePress(vec.x, vec.y);
        } else {
            if(!PRIZE_OK) {
                for(Button b : bonus) {
                    if(b.getIsPress()) {
                        b.setIsPress(false);
                        Coins = MathUtils.random(10, 100);
                        Metal = MathUtils.random(10, 100);
                        GameRunner.dm.addData2("coins",  GameRunner.dm.load2("coins") + Coins);
                        GameRunner.dm.addData2("metal",  GameRunner.dm.load2("metal") + Metal);
                        PRIZE_OK = true;
                    }
                }
            }
            if(okBtn.getIsPress() && PRIZE_OK) {
                okBtn.setIsPress(false);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        time--;
                        if (time <= 1) {
                            time = 2;
                            //gameStateMenager.pop();
                            gameStateMenager.set(new MoonCityState(gameStateMenager));
                        }
                    }
                }, 0.1f);
            }
        }
        if(Gdx.input.justTouched()) {
            Vector3 vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if(!PRIZE_OK) {
                for(Button b : bonus) {
                    b.setPress(vec.x, vec.y);
                }
            }
            if(PRIZE_OK)
                okBtn.setPress(vec.x, vec.y);
        }
    }


    @Override
    public void update(float delta) {
        GameRunner.adMobFlag = false;
        hendleInput();

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        bg.getBgSprite().draw(sb);
        GameRunner.font.draw(sb, TITLE, (GameRunner.WIDTH - layout.width) / 2, GameRunner.HEIGHT - 100);
        if(!PRIZE_OK) {
            for(Button b : bonus) {
                b.getSprite().draw(sb);
            }
        }
        if(PRIZE_OK) {
            GameRunner.font.draw(sb, "METAL + " + Metal + " COINS + " + Coins, camera.position.x-100, camera.position.y - 150);
            okBtn.getSprite().draw(sb);
        }
        sb.end();
    }

    @Override
    public void dispose() {
        for(Button b : bonus) {
            b.dispose();
        }
    }
}
