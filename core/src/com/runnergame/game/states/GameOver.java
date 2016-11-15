package com.runnergame.game.states;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.runnergame.game.GameRunner;
import com.runnergame.game.sprites.Button;

public class GameOver extends State {
    private Button playBtn, onSoundBtn, offSoundBtn, exitBtn;
    private String TITLE = "<< GAME OVER >>";
    private final GlyphLayout layout = new GlyphLayout(GameRunner.font, TITLE);

    public GameOver(GameStateManager gameStateMenager) {
        super(gameStateMenager);
        camera.setToOrtho(false, GameRunner.WIDTH, GameRunner.HEIGHT);
        playBtn = new Button("Play.png", camera.position.x-200, camera.position.y, 1, 1);
        onSoundBtn = new Button("SoundOn.png", camera.position.x-530, camera.position.y-250, 1, 1);
        offSoundBtn = new Button("SoundOff.png", camera.position.x-530, camera.position.y-250, 1, 1);
        exitBtn = new Button("close.png", camera.position.x+200, camera.position.y, 1, 1);
    }

    @Override
    protected void hendleInput() {
        if(Gdx.input.justTouched()) {
            Vector3 vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if(playBtn.collide(vec.x, vec.y)) {
                gameStateMenager.set(new PlayState(gameStateMenager));
            } else if(onSoundBtn.collide(vec.x, vec.y)) {
                GameRunner.isPlay = !GameRunner.isPlay;
            } else if(exitBtn.collide(vec.x, vec.y)) {
                //gameStateMenager.set(new MetaGameState(gameStateMenager));
                gameStateMenager.set(new MoonCityState(gameStateMenager));
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
        GameRunner.font.draw(sb, TITLE, (GameRunner.WIDTH - layout.width) / 2, GameRunner.HEIGHT - 100);
        playBtn.getSprite().draw(sb);
        GameRunner.font.draw(sb, "PLAY AGAIN.", playBtn.getPos().x-30, playBtn.getPos().y + 70);
        exitBtn.getSprite().draw(sb);
        GameRunner.font.draw(sb, "END GAME.", exitBtn.getPos().x-30, exitBtn.getPos().y + 70);
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