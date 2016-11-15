package com.runnergame.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.runnergame.game.GameRunner;
import com.runnergame.game.sprites.Button;

public class CardInfoState extends State {
    private Button closeBtn;
    private Button delBtn;
    private String mesg = "Unit info.";
    private final GlyphLayout layout = new GlyphLayout(GameRunner.font, mesg);

    private DataManager dm;

    private Sprite sprite;
    private String str;

    int cardNum = 0;

    public CardInfoState(GameStateManager gameStateMenager, int cn, Sprite _s, String _d) {
        super(gameStateMenager);
        camera.setToOrtho(false, GameRunner.WIDTH, GameRunner.HEIGHT);

        cardNum = cn;
        sprite = _s;
        sprite.setCenter(camera.position.x - 280, camera.position.y);
        str = _d;

        closeBtn = new Button("close.png", camera.position.x - 280, camera.position.y - 150, 1, 1);
        delBtn = new Button("delete.png", camera.position.x + 280, camera.position.y - 150, 1, 1);

        dm = new DataManager("GameRunner");
        dm.setParam("Unit" + cn);
    }

    @Override
    protected void hendleInput() {
        if(Gdx.input.justTouched()) {
            Vector3 vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            if(closeBtn.collide(vec.x, vec.y)) {
                gameStateMenager.pop();
            }
            if(delBtn.collide(vec.x, vec.y)) {
                dm.addData(1);
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

        GameRunner.font.draw(sb, mesg, (GameRunner.WIDTH - layout.width) / 2, GameRunner.HEIGHT - 100);
        closeBtn.getSprite().draw(sb);
        delBtn.getSprite().draw(sb);

        sprite.draw(sb);
        GameRunner.font.draw(sb, str, camera.position.x - 240, camera.position.y);

        sb.end();
    }

    @Override
    public void dispose() {
        closeBtn.dispose();
        delBtn.dispose();
    }
}
