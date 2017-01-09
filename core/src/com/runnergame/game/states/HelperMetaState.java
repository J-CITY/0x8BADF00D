package com.runnergame.game.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.runnergame.game.GameRunner;
import com.runnergame.game.sprites.Button;

public class HelperMetaState extends State {
    public int level = 0;

    private Button okBtn;
    private Texture tex;
    private Sprite sprite;

    public HelperMetaState(GameStateManager gameStateMenager) {
        super(gameStateMenager);
        GameRunner.adMobFlag = true;
        camera.setToOrtho(false, GameRunner.WIDTH / 2, GameRunner.HEIGHT / 2);
        level = GameRunner.dm.load2("helperMetaLevel");
        GameRunner.dm.addData2("helperMetaLevel", level+1);
        okBtn = new Button("button/ok", camera.position.x + 280, camera.position.y + 150);

        if(level == 0) {
            tex = new Texture("meta/helper/lvl0.png");
        } else if(level == 1) {
            tex = new Texture("meta/helper/lvl1.png");
        } else if(level == 2) {
            tex = new Texture("meta/helper/lvl2.png");
        } else if(level == 3) {
            tex = new Texture("meta/helper/lvl3.png");
        } else if(level == 4) {
            tex = new Texture("meta/helper/lvl4.png");
        }
        sprite = new Sprite(tex);
        sprite.setCenter(camera.position.x, camera.position.y);
        sprite.setScale(0.7f);
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
        sprite.draw(sb);
        okBtn.getSprite().draw(sb);
        sb.end();
    }

    @Override
    public void dispose() {
        tex.dispose();
    }
}
