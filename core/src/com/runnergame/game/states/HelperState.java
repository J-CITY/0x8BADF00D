package com.runnergame.game.states;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.runnergame.game.GameRunner;
import com.runnergame.game.sprites.Background;
import com.runnergame.game.sprites.Button;


public class HelperState extends State {
    public static int lvl = -1;
    private Button okBtn;
    private Texture tex;
    private Sprite sprite;
    Background bg;

    public HelperState(GameStateManager gameStateMenager) {
        super(gameStateMenager);
        GameRunner.adMobFlag = true;
        camera.setToOrtho(false, GameRunner.WIDTH / 2, GameRunner.HEIGHT / 2);

        okBtn = new Button("button/ok", camera.position.x + 180, camera.position.y + 150);

        if(lvl == 0) {
            tex = new Texture("lvl0.png");
        } else if(lvl == 1) {
            tex = new Texture("lvl1.png");
        } else if(lvl == 2) {
            tex = new Texture("lvl2.png");
        }
        sprite = new Sprite(tex);
        sprite.setCenter(camera.position.x, camera.position.y);
        sprite.setScale(0.7f);

        bg = new Background(camera.position.x, camera.position.y, 0);

    }

    @Override
    protected void hendleInput() {
        if(Gdx.input.isTouched()) {
            Vector3 vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if(okBtn.collide(vec.x, vec.y)) {
                gameStateMenager.set(new PlayState(gameStateMenager));
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
        bg.getBgSprite().draw(sb);
        sprite.draw(sb);
        okBtn.getSprite().draw(sb);
        sb.end();
    }

    @Override
    public void dispose() {
        tex.dispose();
        okBtn.dispose();
        sprite.getTexture().dispose();
        bg.dispouse();
    }
}
