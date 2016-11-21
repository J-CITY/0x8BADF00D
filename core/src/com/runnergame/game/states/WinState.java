package com.runnergame.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.runnergame.game.GameRunner;
import com.runnergame.game.Levels;
import com.runnergame.game.sprites.Button;

public class WinState extends State {
    private Button playBtn, closeBtn, onSoundBtn, offSoundBtn;
    private String TITLE = "<< WIN >>";
    private final GlyphLayout layout = new GlyphLayout(GameRunner.font, TITLE);

    private DataManager dm;
    int lvl;

    private float pbtnY0 = 0, pbtnY = 400, cbtnY0 = 250, cbtnY = 400;
    private float sbtnY0 = -250, sbtnY = -400;

    public WinState(GameStateManager gameStateMenager, int _lvl) {
        super(gameStateMenager);
        lvl =_lvl;
        GameRunner.dm.setParam("star");
        GameRunner.dm.addData(GameRunner.new_stars);
        GameRunner.dm.setParam("coins");
        GameRunner.dm.addData(GameRunner.new_coins);
        camera.setToOrtho(false, GameRunner.WIDTH, GameRunner.HEIGHT);
        playBtn = new Button("Play.png", camera.position.x, camera.position.y+cbtnY, 1, 1);
        closeBtn = new Button("close.png", camera.position.x-530, camera.position.y+cbtnY, 1, 1);
        onSoundBtn = new Button("SoundOn.png", camera.position.x-530, camera.position.y+sbtnY, 1, 1);
        offSoundBtn = new Button("SoundOff.png", camera.position.x-530, camera.position.y+sbtnY, 1, 1);
    }

    @Override
    protected void hendleInput() {
        if(Gdx.input.justTouched()) {
            Vector3 vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if(playBtn.collide(vec.x, vec.y) && lvl+1 < Levels.levels.size) {
                lvl++;
                if(lvl % 9 == 0) {
                    BossGameState.lvl = lvl;
                    gameStateMenager.set(new BossGameState(gameStateMenager));
                } else {
                    PlayState.lvl = lvl;
                    gameStateMenager.set(new PlayState(gameStateMenager));
                }
            } else if(onSoundBtn.collide(vec.x, vec.y)) {
                GameRunner.isPlay = !GameRunner.isPlay;
            } else if(closeBtn.collide(vec.x, vec.y)) {
                gameStateMenager.set(new MoonCityState(gameStateMenager));
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
        if(cbtnY0 < cbtnY) {
            cbtnY -= speed*delta;
            closeBtn.setPos(closeBtn.getPos().x, camera.position.y + cbtnY);
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
        //GameRunner.font.draw(sb, "SCORE: " + GameRunner.score, GameRunner.WIDTH / 2, GameRunner.HEIGHT - 150);
        //GameRunner.font.draw(sb, "RECORD: " + GameRunner.rm.load(), GameRunner.WIDTH / 2, GameRunner.HEIGHT - 200);
        GameRunner.font.draw(sb, TITLE, (GameRunner.WIDTH - layout.width) / 2, GameRunner.HEIGHT - 100);
        playBtn.getSprite().draw(sb);
        closeBtn.getSprite().draw(sb);
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
