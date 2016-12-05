package com.runnergame.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.runnergame.game.GameRunner;
import com.runnergame.game.sprites.Button;

/**
 * Created by 333da on 29.11.2016.
 */
public class CheatState extends State {
    private Button closeBtn, doNotDieBtn, doNotColBtn, add100CoinsBtn, add100MetalBtn;

    private String TITLE = "CHEATS";
    private final GlyphLayout layout = new GlyphLayout(GameRunner.font, TITLE);

    public CheatState(GameStateManager gameStateMenager) {
        super(gameStateMenager);
        camera.setToOrtho(false, GameRunner.WIDTH, GameRunner.HEIGHT);
        closeBtn = new Button("close.png", camera.position.x-530, camera.position.y + 250, 1, 1);
        doNotDieBtn = new Button("dndbtn.png", camera.position.x-330, camera.position.y, 1, 1);
        doNotColBtn = new Button("dncbtn.png", camera.position.x-330, camera.position.y+100, 1, 1);
        add100CoinsBtn = new Button("add100coinbtn.png", camera.position.x-330, camera.position.y-100, 1, 1);
        add100MetalBtn = new Button("add100metalbtn.png", camera.position.x-330, camera.position.y-200, 1, 1);
    }

    @Override
    protected void hendleInput() {
        if(Gdx.input.justTouched()) {
            Vector3 vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            if(closeBtn.collide(vec.x, vec.y)) {
                gameStateMenager.pop();
            } else if(doNotDieBtn.collide(vec.x, vec.y)) {
                PlayState.doNotDie = !PlayState.doNotDie;
            } else if(doNotColBtn.collide(vec.x, vec.y)) {
                PlayState.doNotCollige = !PlayState.doNotCollige;
            } else if(add100CoinsBtn.collide(vec.x, vec.y)) {
                GameRunner.dm.addData2("coins", GameRunner.dm.load2("coins") + 100);
            } else if(add100MetalBtn.collide(vec.x, vec.y)) {
                GameRunner.dm.addData2("metal", GameRunner.dm.load2("metal") + 100);
            }
        }
    }


    @Override
    public void update(float delta) {
        hendleInput();

        //System.out.print(pbtnY + "\n");
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        GameRunner.font.draw(sb, TITLE, (GameRunner.WIDTH - layout.width) / 2, GameRunner.HEIGHT - 100);
        closeBtn.getSprite().draw(sb);
        doNotColBtn.getSprite().draw(sb);
        doNotDieBtn.getSprite().draw(sb);
        add100MetalBtn.getSprite().draw(sb);
        add100CoinsBtn.getSprite().draw(sb);
        sb.end();
    }

    @Override
    public void dispose() {
        closeBtn.dispose();
        doNotDieBtn.dispose();
        doNotColBtn.dispose();
        add100CoinsBtn.dispose();
        add100MetalBtn.dispose();
    }
}
