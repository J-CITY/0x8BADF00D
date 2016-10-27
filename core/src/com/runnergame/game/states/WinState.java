package com.runnergame.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.runnergame.game.GameRunner;
import com.runnergame.game.sprites.Button;

public class WinState extends State {
    private Button playBtn, onSoundBtn, offSoundBtn;
    private String TITLE = "<< WIN >>";
    private final GlyphLayout layout = new GlyphLayout(GameRunner.font, TITLE);

    private DataManager dm;

    public WinState(GameStateManager gameStateMenager) {
        super(gameStateMenager);

        dm = new DataManager("GameRunner");
        dm.setParam("star");
        dm.plusData(GameRunner.new_stars);

        //GameRunner.dm.save(GameRunner.score);
        GameRunner.dm.setParam("coins");
        GameRunner.dm.addData(GameRunner.new_coins);
        camera.setToOrtho(false, GameRunner.WIDTH, GameRunner.HEIGHT);
        playBtn = new Button("Play.png", camera.position.x, camera.position.y);
        onSoundBtn = new Button("SoundOn.png", camera.position.x-530, camera.position.y-250);
        offSoundBtn = new Button("SoundOff.png", camera.position.x-530, camera.position.y-250);
    }

    @Override
    protected void hendleInput() {
        if(Gdx.input.justTouched()) {
            Vector3 vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if(playBtn.collide(vec.x, vec.y)) {
                gameStateMenager.set(new MetaGameState(gameStateMenager));
            } else if(onSoundBtn.collide(vec.x, vec.y)) {
                GameRunner.isPlay = !GameRunner.isPlay;
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
        //GameRunner.font.draw(sb, "SCORE: " + GameRunner.score, GameRunner.WIDTH / 2, GameRunner.HEIGHT - 150);
        //GameRunner.font.draw(sb, "RECORD: " + GameRunner.rm.load(), GameRunner.WIDTH / 2, GameRunner.HEIGHT - 200);
        GameRunner.font.draw(sb, TITLE, (GameRunner.WIDTH - layout.width) / 2, GameRunner.HEIGHT - 100);
        playBtn.getSprite().draw(sb);
        if(GameRunner.isPlay) {
            onSoundBtn.getSprite().draw(sb);
        } else {
            offSoundBtn.getSprite().draw(sb);
        }
        sb.end();
    }

    @Override
    public void dispose() {
        playBtn.dispose();
        onSoundBtn.dispose();
        offSoundBtn.dispose();
    }
}
