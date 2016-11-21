package com.runnergame.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.runnergame.game.GameRunner;
import com.runnergame.game.sprites.Button;
import com.runnergame.game.sprites.Unit;

public class SelectLevel extends State {

    private void limitFPS() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    Button backBtn;
    Button arr_rBtn, arr_lBtn;

    int LEVEL_COUNT = 9;
    int PAGE = 0;
    int MAX_PAGE = 1;
    int LEVEL_OPENED = 0;
    private DataManager dm;

    private SpriteBatch tb;

    private Array<Button> lvls;


    SelectLevel(GameStateManager gameStateMenager) {
        super(gameStateMenager);
        camera.setToOrtho(false, GameRunner.WIDTH, GameRunner.HEIGHT);

        tb = new SpriteBatch();

        dm = new DataManager("GameRunner");
        dm.setParam("level");
        LEVEL_OPENED = dm.load();

        lvls = new Array<Button>();
        for(int i = 0; i < LEVEL_COUNT/3; ++i) {
            for(int j = 0; j < LEVEL_COUNT/3; ++j) {
                if((i*3+j + PAGE*10) <= LEVEL_OPENED) {
                    lvls.add(new Button("level.png", camera.position.x - 70 + j*70,
                            camera.position.y + 70 - i*70, 1, 1));
                } else {
                    lvls.add(new Button("level_c.png", camera.position.x - 70 + j*70,
                            camera.position.y + 70 - i*70, 1, 1));
                }
                lvls.get(lvls.size-1).setScale(2);
            }
        }
        if(LEVEL_COUNT + PAGE*10 <= LEVEL_OPENED) {
            lvls.add(new Button("level.png", camera.position.x,
                    camera.position.y - 210, 1, 1));
        } else {
            lvls.add(new Button("level_c.png", camera.position.x,
                    camera.position.y - 210, 1, 1));
        }
        lvls.get(lvls.size-1).setScale(2);

        backBtn = new Button("close.png", camera.position.x - 380, camera.position.y - 250, 1, 1);
        arr_lBtn = new Button("arror_l.png", camera.position.x - 180, camera.position.y - 250, 1, 1);
        arr_rBtn = new Button("arror_r.png", camera.position.x + 180, camera.position.y - 250, 1, 1);
        limitFPS();

    }

    @Override
    protected void hendleInput() {
        if(Gdx.input.isTouched()) {
            Vector3 vec = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if(backBtn.collide(vec.x, vec.y)) {
                gameStateMenager.pop();
            }
            if(arr_rBtn.collide(vec.x, vec.y) && PAGE < MAX_PAGE) {
                PAGE++;
                lvls.clear();
                System.out.print(lvls.size + "\n");
                for(int i = 0; i < LEVEL_COUNT/3; ++i) {
                    for(int j = 0; j < LEVEL_COUNT/3; ++j) {
                        if((i*3+j + PAGE*10) <= LEVEL_OPENED) {
                            lvls.add(new Button("level.png", camera.position.x - 70 + j*70,
                                    camera.position.y + 70 - i*70, 1, 1));
                        } else {
                            lvls.add(new Button("level_c.png", camera.position.x - 70 + j*70,
                                    camera.position.y + 70 - i*70, 1, 1));
                        }
                        lvls.get(lvls.size-1).setScale(2);
                    }
                }
                if(LEVEL_COUNT + PAGE*10 <= LEVEL_OPENED) {
                    lvls.add(new Button("level.png", camera.position.x,
                            camera.position.y - 210, 1, 1));
                } else {
                    lvls.add(new Button("level_c.png", camera.position.x,
                            camera.position.y - 210, 1, 1));
                }
                lvls.get(lvls.size-1).setScale(2);
            } else if(arr_lBtn.collide(vec.x, vec.y) && PAGE > 0) {
                PAGE--;
                lvls.clear();
                System.out.print(lvls.size + "\n");
                for(int i = 0; i < LEVEL_COUNT/3; ++i) {
                    for(int j = 0; j < LEVEL_COUNT/3; ++j) {
                        if((i*3+j + PAGE*10) <= LEVEL_OPENED) {
                            lvls.add(new Button("level.png", camera.position.x - 70 + j*70,
                                    camera.position.y + 70 - i*70, 1, 1));
                        } else {
                            lvls.add(new Button("level_c.png", camera.position.x - 70 + j*70,
                                    camera.position.y + 70 - i*70, 1, 1));
                        }
                        lvls.get(lvls.size-1).setScale(2);
                    }
                }
                if(LEVEL_COUNT + PAGE*10 <= LEVEL_OPENED) {
                    lvls.add(new Button("level.png", camera.position.x,
                            camera.position.y - 210, 1, 1));
                } else {
                    lvls.add(new Button("level_c.png", camera.position.x,
                            camera.position.y - 210, 1, 1));
                }
                lvls.get(lvls.size-1).setScale(2);
            }

            for (int i = 0; i < lvls.size; ++i) {
                Button b = lvls.get(i);
                if(b.collide(vec.x, vec.y)) {
                    if(i + PAGE*10 <= LEVEL_OPENED) {
                        if((i+1) % 10 == 0) {
                            //BossGameState.lvl = i+PAGE*10;
                            PlayState.lvl = i+PAGE*10;
                            //gameStateMenager.set(new BossGameState(gameStateMenager));
                            gameStateMenager.set(new PlayState(gameStateMenager));
                        } else {
                            PlayState.lvl = i+PAGE*10;
                            if(0 + PAGE*10 <= i && i <= 2 + PAGE*10) {
                                HelperState.lvl = i+PAGE*10;
                                gameStateMenager.set(new HelperState(gameStateMenager));
                            } else {
                                gameStateMenager.set(new PlayState(gameStateMenager));
                            }
                        }
                    }
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
        backBtn.getSprite().draw(sb);
        int i = 1;
        for (Button b : lvls) {
            b.getSprite().draw(sb);
            //System.out.print((i+PAGE*10) + "\n");
            GameRunner.font.draw(sb, ""+(i+PAGE*10), b.getPos().x-30, b.getPos().y);
            ++i;
        }
        //System.out.print("!!!!!!!!!!!!!!!\n");

        if(PAGE == 0) {
            arr_rBtn.getSprite().draw(sb);
        } else if(PAGE == MAX_PAGE) {
            arr_lBtn.getSprite().draw(sb);
        } else {
            arr_rBtn.getSprite().draw(sb);
            arr_lBtn.getSprite().draw(sb);
        }
        sb.end();
    }

    @Override
    public void dispose() {
        for(Button b : lvls) {
            b.dispose();
        }
        backBtn.dispose();
    }
}
