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
import com.runnergame.game.sprites.Unit;

public class CardSetState extends State {
    private Button closeBtn;
    private Array<Button> buyBtns;
    private String mesg = "Open unit. Price 100.";
    private final GlyphLayout layout = new GlyphLayout(GameRunner.font, mesg);

    private DataManager dm;

    private Array<Texture> tex;
    private Array<Sprite> sprite;
    private Array<String> str;

    int price = 100;
    int coins;
    int cardNum = 0;
    int

            type;
    public CardSetState(GameStateManager gameStateMenager, int cn) {
        super(gameStateMenager);
        cardNum = cn;
        camera.setToOrtho(false, GameRunner.WIDTH, GameRunner.HEIGHT);
        closeBtn = new Button("close.png", camera.position.x - 280, camera.position.y - 150);

        tex = new Array<Texture>();
        sprite = new Array<Sprite>();
        str = new Array<String>();

        tex.add(new Texture("energy.png"));
        sprite.add(new Sprite(tex.get(tex.size-1)));
        str.add("+1 energy. Price 100.");
        tex.add(new Texture("coinGen.png"));
        sprite.add(new Sprite(tex.get(tex.size-1)));
        str.add("-1 energy; +1 coin. Price 100.");
        tex.add(new Texture("starGen.png"));
        sprite.add(new Sprite(tex.get(tex.size-1)));
        str.add("-1 energy; +1 star. Price 100.");

        buyBtns = new Array<Button>();
        for(int i = 0; i < 3; ++i) {
            buyBtns.add(new Button("ok.png", camera.position.x + 280, camera.position.y + 150 - 70 * i));
            sprite.get(i).setCenter(camera.position.x - 280, camera.position.y + 150 - 70 * i);
        }
        GameRunner.dm.setParam("coins");
        coins = GameRunner.dm.load();

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
            for (int i = 0; i < buyBtns.size; ++i) {
                Button b = buyBtns.get(i);
                if(b.collide(vec.x, vec.y) && coins > price) {
                    dm.addData(2+i);
                    dm.addData2("coins", dm.load2("coins")-price);
                    gameStateMenager.pop();
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
        GameRunner.font.draw(sb, mesg, (GameRunner.WIDTH - layout.width) / 2, GameRunner.HEIGHT - 100);
        closeBtn.getSprite().draw(sb);

        for (Sprite b : sprite) {
            b.draw(sb);
        }
        for(int i = 0; i < 3; ++i) {
            GameRunner.font.draw(sb, str.get(i), camera.position.x - 240, camera.position.y + 150 - 70 * i);
        }

        for (Button b : buyBtns) {
            if(coins > price) {
                b.getSprite().draw(sb);
            }
        }

        sb.end();
    }

    @Override
    public void dispose() {
        closeBtn.dispose();
        for (Button b : buyBtns) {
            b.dispose();
        }
    }
}
