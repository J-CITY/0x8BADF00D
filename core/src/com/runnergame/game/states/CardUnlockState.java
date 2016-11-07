package com.runnergame.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.runnergame.game.GameRunner;
import com.runnergame.game.sprites.Button;

public class CardUnlockState extends State {
    private Button closeBtn, buyBtn;
    private String mesg = "Open unit. Price 100";
    private final GlyphLayout layout = new GlyphLayout(GameRunner.font, mesg);

    private DataManager dm;

    int price = 100;
    int coins;
    int cardNum = 0;
    int type;
    public CardUnlockState(GameStateManager gameStateMenager, int cn) {
        super(gameStateMenager);
        cardNum = cn;
        camera.setToOrtho(false, GameRunner.WIDTH, GameRunner.HEIGHT);
        closeBtn = new Button("close.png", camera.position.x - 280, camera.position.y - 150);
        buyBtn = new Button("ok.png", camera.position.x + 280, camera.position.y - 150);

        GameRunner.dm.setParam("coins");
        coins = GameRunner.dm.load();

        dm = new DataManager("GameRunner");
        dm.setParam("Unit" + cn);
        //type = dm.load();
    }

    @Override
    protected void hendleInput() {
        if(Gdx.input.justTouched()) {
            Vector3 vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            if(closeBtn.collide(vec.x, vec.y)) {
                gameStateMenager.pop();
            } else if(buyBtn.collide(vec.x, vec.y) && price < coins) {
                dm.addData(1);
                dm.addData2("coins", dm.load2("coins")-price);
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
        if(coins > price) {
            buyBtn.getSprite().draw(sb);
        }
        sb.end();
    }

    @Override
    public void dispose() {
        closeBtn.dispose();
        buyBtn.dispose();
    }
}
