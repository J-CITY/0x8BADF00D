package com.runnergame.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.runnergame.game.GameRunner;
import com.runnergame.game.sprites.Button;

public class PauseState extends State {
    private Button playBtn, onSoundBtn, offSoundBtn, restartBtn, exitBtn;
    private String TITLE = "<< PAUSE >>";
    private final GlyphLayout layout = new GlyphLayout(GameRunner.font, TITLE);

    public static int whatGame = 0;

    private float pbtnY0 = 0, pbtnY = 400;
    private float ebtnY0 = 250, ebtnY = 400;
    private float sbtnY0 = -250, sbtnY = -400;
    private float rbtnY0 = 0, rbtnY = -400;

    public PauseState(GameStateManager gameStateMenager) {
        super(gameStateMenager);
        camera.setToOrtho(false, GameRunner.WIDTH, GameRunner.HEIGHT);
        playBtn = new Button("Play.png", camera.position.x-200, camera.position.y+pbtnY, 1, 1);
        restartBtn = new Button("Restart.png", camera.position.x+200, camera.position.y+rbtnY, 1, 1);
        onSoundBtn = new Button("SoundOn.png", camera.position.x-530, camera.position.y+sbtnY, 1, 1);
        offSoundBtn = new Button("SoundOff.png", camera.position.x-530, camera.position.y+sbtnY, 1, 1);
        exitBtn = new Button("close.png", camera.position.x-530, camera.position.y+ebtnY, 1, 1);
    }

    @Override
    protected void hendleInput() {
        if(Gdx.input.justTouched()) {
            Vector3 vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if(playBtn.collide(vec.x, vec.y)) {
                gameStateMenager.pop();
            } else if(onSoundBtn.collide(vec.x, vec.y)) {
                GameRunner.isPlay = !GameRunner.isPlay;
            } else if(exitBtn.collide(vec.x, vec.y)) {
                gameStateMenager.set(new MoonCityState(gameStateMenager));
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

        if(pbtnY0 < pbtnY) {
            pbtnY -= speed*delta;
            playBtn.setPos(playBtn.getPos().x, camera.position.y + pbtnY);
        }
        if(rbtnY0 > rbtnY) {
            rbtnY += speed*delta;
            restartBtn.setPos(restartBtn.getPos().x, camera.position.y + rbtnY);
        }
        if(ebtnY0 < ebtnY) {
            ebtnY -= speed * delta;
            exitBtn.setPos(exitBtn.getPos().x, camera.position.y + ebtnY);
        }
        if(sbtnY0 > sbtnY) {
            sbtnY += speed*delta;
            offSoundBtn.setPos(offSoundBtn.getPos().x, camera.position.y + sbtnY);
            onSoundBtn.setPos(onSoundBtn.getPos().x, camera.position.y + sbtnY);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();

        GameRunner.font.draw(sb, TITLE, (GameRunner.WIDTH - layout.width) / 2, GameRunner.HEIGHT - 100);
        playBtn.getSprite().draw(sb);
        restartBtn.getSprite().draw(sb);
        exitBtn.getSprite().draw(sb);
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
