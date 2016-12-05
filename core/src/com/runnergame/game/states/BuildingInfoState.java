package com.runnergame.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.runnergame.game.GameRunner;
import com.runnergame.game.sprites.Button;
import com.runnergame.game.sprites.MetaGame.Building;

public class BuildingInfoState extends State {

    private Button closeBtn, buyBtn;
    private String name, param;
    private final GlyphLayout layout;
    //private Sprite sprite;

    //private DataManager dm;
    private Building building;
    int price;
    int coins;
    int cardNum = 0;
    int type;
    int build_level, max_build_level, player_level, lvl;
    public BuildingInfoState(GameStateManager gameStateMenager, Building build) {
        super(gameStateMenager);
        //cardNum = cn;
        camera.setToOrtho(false, GameRunner.WIDTH, GameRunner.HEIGHT);
        closeBtn = new Button("close.png", camera.position.x - 280, camera.position.y - 150, 1, 1);
        buyBtn = new Button("ok.png", camera.position.x + 280, camera.position.y - 150, 1, 1);

        coins = GameRunner.dm.load2("coins");

        build_level = build.getLevel_now();
        max_build_level = build.getMax_level();
        lvl = build.getLevel();
        player_level = GameRunner.dm.load2("level");

        price = build.getPrice();
        name = build.getName();
        layout = new GlyphLayout(GameRunner.font, name);
        param = build.getParam();
        building = build;
    }

    @Override
    protected void hendleInput() {
        if(Gdx.input.justTouched()) {
            Vector3 vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            if(closeBtn.collide(vec.x, vec.y)) {
                gameStateMenager.pop();
            } else if(buyBtn.collide(vec.x, vec.y) && price < coins && build_level < max_build_level && lvl <= player_level) {
                int p = GameRunner.dm.load2(param);
                if(building.update(p)) {
                    GameRunner.dm.addData2(param, p+1);
                    GameRunner.dm.addData2("coins", coins-price);
                    gameStateMenager.set(new MoonCityState(gameStateMenager));
                }

            }
        }
    }

    @Override
    public void update(float delta) {
        hendleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        GameRunner.font.draw(sb, name, (GameRunner.WIDTH - layout.width) / 2, GameRunner.HEIGHT - 100);
        GameRunner.font.draw(sb, "Price: " + price, (GameRunner.WIDTH - layout.width) / 2, GameRunner.HEIGHT - 50);
        closeBtn.getSprite().draw(sb);
        if(coins > price && build_level < max_build_level && lvl <= player_level) {
            buyBtn.getSprite().draw(sb);
        }
        sb.end();
    }

    @Override
    public void dispose() {
        closeBtn.dispose();
        buyBtn.dispose();
    }
}
