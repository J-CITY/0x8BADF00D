package com.runnergame.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.runnergame.game.GameRunner;
import com.runnergame.game.sprites.Button;

public class PauseState extends State {
    private Button playBtn, onSoundBtn, offSoundBtn, restartBtn;
    private String TITLE = "<< PAUSE >>";
    private final GlyphLayout layout = new GlyphLayout(GameRunner.font, TITLE);

    public static int whatGame = 0;

    public PauseState(GameStateManager gameStateMenager) {
        super(gameStateMenager);
        camera.setToOrtho(false, GameRunner.WIDTH, GameRunner.HEIGHT);
        playBtn = new Button("Play.png", camera.position.x-200, camera.position.y);
        restartBtn = new Button("Restart.png", camera.position.x+200, camera.position.y);
        onSoundBtn = new Button("SoundOn.png", camera.position.x-530, camera.position.y-250);
        offSoundBtn = new Button("SoundOff.png", camera.position.x-530, camera.position.y-250);
    }

    @Override
    protected void hendleInput() {
        if(Gdx.input.justTouched()) {
            Vector3 vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if(playBtn.collide(vec.x, vec.y)) {
                gameStateMenager.pop();
            } else if(onSoundBtn.collide(vec.x, vec.y)) {
                GameRunner.isPlay = !GameRunner.isPlay;
            } else if(restartBtn.collide(vec.x, vec.y)) {
                gameStateMenager.pop();
                if(whatGame == 0) {
                    gameStateMenager.set(new PlayState(gameStateMenager));
                } else if(whatGame == 1) {
                    gameStateMenager.set(new BossGameState(gameStateMenager));
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

        GameRunner.font.draw(sb, TITLE, (GameRunner.WIDTH - layout.width) / 2, GameRunner.HEIGHT - 100);
        playBtn.getSprite().draw(sb);
        restartBtn.getSprite().draw(sb);
        if(GameRunner.isPlay) {
            onSoundBtn.getSprite().draw(sb);
        } else {
            offSoundBtn.getSprite().draw(sb);
        }
        sb.end();
    }

    @Override
    public void dispose() {
        onSoundBtn.dispose();
        offSoundBtn.dispose();
        playBtn.dispose();
        restartBtn.dispose();
    }
}
