package com.runnergame.game.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.runnergame.game.GameRunner;
import com.runnergame.game.sprites.Background;
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
    Background bg;


    public ShopState(GameStateManager gameStateMenager) {
        super(gameStateMenager);
        camera.setToOrtho(false, GameRunner.WIDTH, GameRunner.HEIGHT);
        cam_btn = new OrthographicCamera(GameRunner.WIDTH, GameRunner.HEIGHT);
        cam_btn.update();
        backBtn = new Button("button/close", cam_btn.position.x-550, cam_btn.position.y+340);
        backBtn.setScale(0.8f);
        buyBtn = new Button("button/buy", cam_btn.position.x+530, cam_btn.position.y-100);
        //buyBtn.setScale(0.5f);
        setBtn = new Button("button/set", cam_btn.position.x+530, cam_btn.position.y-100);
        bg = new Background(cam_btn.position.x, cam_btn.position.y, 0);

        buttons = new Array<Button>(5);

        tb = new SpriteBatch();

        float dx = 600, dy = 100, d = 120;
        float _x = camera.position.x-dx;
        float _y = camera.position.y+dy;
        for(int i = 0; i < GameRunner.colors.playerSkins.size; ++i) {
            buttons.add(new Button("button/b", _x, _y));
            buttons.get(buttons.size-1).setTexture(GameRunner.colors.playerSkins.get(i));
            _x+=d;
            if(_x >= camera.position.x + dx-300) {
                _y -= d;
                _x = camera.position.x-dx;
            }
        }
        headder.setCenter(cam_btn.position.x, cam_btn.position.y+450);
    }
    float time = 2;
    @Override
    protected void hendleInput() {
        if (Gdx.input.isTouched()) {
            Vector3 vec = cam_btn.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            backBtn.updatePress(vec.x, vec.y);
            buyBtn.updatePress(vec.x, vec.y);
            setBtn.updatePress(vec.x, vec.y);
        } else {
            if (backBtn.getIsPress()) {
                backBtn.setIsPress(false);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        time--;
                        if (time <= 1) {
                            time = 2;
                            gameStateMenager.pop();
                        }
                    }
                }, 0.1f);
            }
            if (buyBtn.getIsPress()) {
                buyBtn.setIsPress(false);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        time--;
                        if (time <= 1) {
                            time = 2;
                            int c = GameRunner.dm.load2("coins");
                            if (c >= price) {
                                GameRunner.dm.addData2("coins", c - price);
                                GameRunner.dm.addData2("playerSkin", numSkin);
                                GameRunner.dm.addData2(GameRunner.colors.playerSkins.get(numSkin), 1);
                            }
                        }
                    }
                }, 0.1f);

            }
            if (setBtn.getIsPress()) {
                setBtn.setIsPress(false);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        time--;
                        if (time <= 1) {
                            time = 2;
                            GameRunner.dm.addData2("playerSkin", numSkin);
                        }
                    }
                }, 0.1f);
            }
        }
        if (Gdx.input.justTouched()) {
            Vector3 vec = vec = cam_btn.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            backBtn.setPress(vec.x, vec.y);
            if(pref != 0) {
                setBtn.setPress(vec.x, vec.y);
            } else {
                buyBtn.setPress(vec.x, vec.y);
            }

            vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            int i = 0;
            for (Button b : buttons) {
                if (b.collide(vec.x, vec.y) && i != 0) {
                    GameRunner.soundPressBtn.play(0.2f);
                    price = GameRunner.colors.playerSkinsPrice.get(i);
                    pref = GameRunner.dm.load2(GameRunner.colors.playerSkins.get(i));
                    numSkin = i;
                } else if (b.collide(vec.x, vec.y) && i == 0) {
                    GameRunner.soundPressBtn.play(0.2f);
                    pref = 1;
                    price = 0;
                    numSkin = 0;
                    System.out.print(numSkin + "\n");
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
        tb.setProjectionMatrix(cam_btn.combined);
        tb.begin();
        bg.getBgSprite().draw(tb);
        tb.end();
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        int i = 0;
        for (Button b : buttons) {
            frame.setCenter(b.getPos().x, b.getPos().y);
            if(numSkin == i) {
                frame.setColor(GameRunner.colors.yellow.r,GameRunner.colors.yellow.g,GameRunner.colors.yellow.b,0.8f);
            } else {
                frame.setColor(GameRunner.colors.blue.r,GameRunner.colors.blue.g,GameRunner.colors.blue.b,0.8f);
            }
            frame.draw(sb);
            b.getSprite().draw(sb);
            i++;
        }
        sb.end();
        tb.setProjectionMatrix(cam_btn.combined);
        tb.begin();
        headder.draw(tb);
        GameRunner.font.draw(tb, TITLE + "  COINS: " + GameRunner.now_coins, cam_btn.position.x - 400, cam_btn.position.y + 350);
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
