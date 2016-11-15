package com.runnergame.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.runnergame.game.GameRunner;
import com.runnergame.game.sprites.Button;

public class MenuState extends State {

    private Button playBtn, onSoundBtn, offSoundBtn;
    private String TITLE = "0x8BADF00D";
    private final GlyphLayout layout = new GlyphLayout(GameRunner.font, TITLE);

    public MenuState(GameStateManager gameStateMenager) {
        super(gameStateMenager);
        camera.setToOrtho(false, GameRunner.WIDTH, GameRunner.HEIGHT);
        playBtn = new Button("play_t.png", camera.position.x, camera.position.y, 1, 1);
        onSoundBtn = new Button("SoundOn.png", camera.position.x-530, camera.position.y-250, 1, 1);
        offSoundBtn = new Button("SoundOff.png", camera.position.x-530, camera.position.y-250, 1, 1);
    }

    @Override
    protected void hendleInput() {
        if(Gdx.input.justTouched()) {
            Vector3 vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            if(playBtn.collide(vec.x, vec.y)) {
                //gameStateMenager.set(new PlayState(gameStateMenager));
                gameStateMenager.set(new MoonCityState(gameStateMenager));
                //gameStateMenager.set(new NCState(gameStateMenager));
            } else if(onSoundBtn.collide(vec.x, vec.y)) {
                GameRunner.isPlay = !GameRunner.isPlay;
            }
        }
    }


    @Override
    public void update(float delta) {
        hendleInput();
        playBtn.update(delta);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();

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