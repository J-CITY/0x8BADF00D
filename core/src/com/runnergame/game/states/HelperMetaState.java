package com.runnergame.game.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.runnergame.game.GameRunner;
import com.runnergame.game.MetaLevels;
import com.runnergame.game.sprites.Button;

public class HelperMetaState extends State {
    public int level = 0;

    private Button okBtn;
    private Texture tex;
    private Sprite sprite;
    private Sprite bgs = new Sprite(new Texture("meta/helper/lvl.png"));
    private int MAX_LEVEL = 25;
    String language;

    public HelperMetaState(GameStateManager gameStateMenager, int lvl, int hlvl) {
        super(gameStateMenager);
        GameRunner.adMobFlag = true;
        level = lvl;
        camera.setToOrtho(false, GameRunner.WIDTH / 2, GameRunner.HEIGHT / 2);
        //level = GameRunner.dm.load2("helperMetaLevel");
        GameRunner.dm.addData2("helperMetaLevel", hlvl);
        okBtn = new Button("button/ok", camera.position.x + 180, camera.position.y + 150);

        if(GameRunner.dm.loadDataString("language").equals("") || GameRunner.dm.loadDataString("language").equals("en")) {
            language = "en";
        } else if(GameRunner.dm.loadDataString("language").equals("ru")) {
            language = "ru";
        }

        if(level <= MAX_LEVEL) {
            tex = new Texture("meta/helper/lvl"+level+"_" + language +".png");
        }
        sprite = new Sprite(tex);
        sprite.setCenter(camera.position.x-100, camera.position.y-70);
        sprite.setScale(0.7f);
        bgs.setCenter(camera.position.x, camera.position.y);
    }

    @Override
    protected void hendleInput() {
        if(Gdx.input.isTouched()) {
            Vector3 vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if(okBtn.collide(vec.x, vec.y)) {
                gameStateMenager.pop();
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
        bgs.draw(sb);
        sprite.draw(sb);
        okBtn.getSprite().draw(sb);
        sb.end();
    }

    @Override
    public void dispose() {
        tex.dispose();
        okBtn.dispose();
        sprite.getTexture().dispose();
        bgs.getTexture().dispose();
    }
}
