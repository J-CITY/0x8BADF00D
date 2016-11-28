package com.runnergame.game.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.runnergame.game.GameRunner;
import com.runnergame.game.sprites.Button;
import com.runnergame.game.sprites.MetaGame.Building;

public class ShopState extends State {
    private SpriteBatch tb;
    OrthographicCamera cam_btn;

    private Array<Button> buttons;


    private Button backBtn, buyBtn, setBtn;
    private int pref=-1;
    private int price=-1;
    private int numSkin = 0;
    private String TITLE = "<< SHOP >>";
    private final GlyphLayout layout = new GlyphLayout(GameRunner.font, TITLE);

    private Sprite headder = new Sprite(new Texture("headder.png"));
    private Sprite frame = new Sprite(new Texture("frame.png"));



    public ShopState(GameStateManager gameStateMenager) {
        super(gameStateMenager);
        camera.setToOrtho(false, GameRunner.WIDTH, GameRunner.HEIGHT);
        cam_btn = new OrthographicCamera(GameRunner.WIDTH, GameRunner.HEIGHT);
        cam_btn.update();
        backBtn = new Button("close.png", cam_btn.position.x-530, cam_btn.position.y+350, 1, 1);
        backBtn.setScale(0.5f);
        buyBtn = new Button("btnBuy.png", cam_btn.position.x+530, cam_btn.position.y-100, 1, 1);
        //buyBtn.setScale(0.5f);
        setBtn = new Button("btnSet.png", cam_btn.position.x+530, cam_btn.position.y-100, 1, 1);

        buttons = new Array<Button>(5);

        tb = new SpriteBatch();

        float dx = 600, dy = 100, d = 120;
        float _x = camera.position.x-dx;
        float _y = camera.position.y+dy;
        for(int i = 0; i < GameRunner.colors.playerSkins.size; ++i) {
            buttons.add(new Button(GameRunner.colors.playerSkins.get(i), _x, _y, 1, 1));
            _x+=d;
            if(_x >= camera.position.x + dx-300) {
                _y -= d;
                _x = camera.position.x-dx;
            }
        }
        headder.setCenter(cam_btn.position.x, cam_btn.position.y+450);
    }

    @Override
    protected void hendleInput() {
        if(Gdx.input.justTouched()) {
            Vector3 vec = cam_btn.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if(backBtn.collide(vec.x, vec.y)) {
                gameStateMenager.pop();
            }
            if(buyBtn.collide(vec.x, vec.y) && pref == 0 && price >= 0) {
                int c = GameRunner.dm.load2("coins");
                if(c >= price) {
                    GameRunner.dm.addData2("coins", c - price);
                    GameRunner.dm.addData2("playerSkin", numSkin);
                    GameRunner.dm.addData2(GameRunner.colors.playerSkins.get(numSkin), 1);
                }
            }
            if(setBtn.collide(vec.x, vec.y) && pref != 0 && price >= 0) {
                GameRunner.dm.addData2("playerSkin", numSkin);
            }
            vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            int i = 0;
            for(Button b : buttons) {
                if(b.collide(vec.x, vec.y) && i != 0) {
                    price = GameRunner.colors.playerSkinsPrice.get(i);
                    pref = GameRunner.dm.load2(GameRunner.colors.playerSkins.get(i));
                    numSkin = i;
                } else if(b.collide(vec.x, vec.y) && i == 0) {
                    pref = 1;
                    price = 0;
                    numSkin = 0;
                    System.out.print(numSkin+"\n");
                }
                i++;
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
        for (Button b : buttons) {
            frame.setCenter(b.getPos().x, b.getPos().y);
            frame.draw(sb);
            b.getSprite().draw(sb);
        }
        sb.end();
        tb.setProjectionMatrix(cam_btn.combined);
        tb.begin();
        headder.draw(tb);
        GameRunner.font.draw(tb, TITLE + " COINS: " + GameRunner.new_coins, cam_btn.position.x - 400, cam_btn.position.y + 350);
        if(pref != 0 && price >= 0) {
            setBtn.getSprite().draw(tb);
        } else if(pref == 0 && price >= 0) {
            GameRunner.font.draw(tb, "PRICE: " + price, cam_btn.position.x + 400, cam_btn.position.y);
            buyBtn.getSprite().draw(tb);
        }

        backBtn.getSprite().draw(tb);
        tb.end();
    }

    @Override
    public void dispose() {

    }
}
