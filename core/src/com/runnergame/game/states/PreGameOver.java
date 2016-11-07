package com.runnergame.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.runnergame.game.GameRunner;
import com.runnergame.game.sprites.Button;

public class PreGameOver extends State {
    private Button playBtn, onSoundBtn, offSoundBtn, endGameBtn;
    private String TITLE = "<< GAME OVER >>";
    private final GlyphLayout layout = new GlyphLayout(GameRunner.font, TITLE);

    public PreGameOver(GameStateManager gameStateMenager) {
        super(gameStateMenager);
        camera.setToOrtho(false, GameRunner.WIDTH, GameRunner.HEIGHT);
        playBtn = new Button("Play.png", camera.position.x-200, camera.position.y);
        onSoundBtn = new Button("SoundOn.png", camera.position.x-530, camera.position.y-250);
        offSoundBtn = new Button("SoundOff.png", camera.position.x-530, camera.position.y-250);
        endGameBtn = new Button("EndGame.png", camera.position.x+200, camera.position.y);
    }

    @Override
    protected void hendleInput() {
        if(Gdx.input.justTouched()) {
            Vector3 vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if(playBtn.collide(vec.x, vec.y)) {
                GameRunner.dm.setParam("coins");
                if (GameRunner.dm.load() >= 100) {
                    GameRunner.dm.addData(GameRunner.dm.load()-100);
                    GameRunner.reborn = true;
                    gameStateMenager.set(new PlayState(gameStateMenager));
                }
            } else if(onSoundBtn.collide(vec.x, vec.y)) {
                GameRunner.isPlay = !GameRunner.isPlay;
            } if(endGameBtn.collide(vec.x, vec.y)) {
                gameStateMenager.set(new GameOver(gameStateMenager));
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
        GameRunner.dm.setParam("coins");
        GameRunner.font.draw(sb, "COINS: " + GameRunner.dm.load(), GameRunner.WIDTH / 2 - 100, GameRunner.HEIGHT - 250);
        GameRunner.font.draw(sb, TITLE, (GameRunner.WIDTH - layout.width) / 2, GameRunner.HEIGHT - 100);
        playBtn.getSprite().draw(sb);
        GameRunner.font.draw(sb, "CONTINUE. 100 COINS.", playBtn.getPos().x-30, playBtn.getPos().y + 70);
        endGameBtn.getSprite().draw(sb);
        GameRunner.font.draw(sb, "END GAME.", endGameBtn.getPos().x-30, endGameBtn.getPos().y + 70);
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
