package com.runnergame.game.sprites;

import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.runnergame.game.Colors;
import com.runnergame.game.states.DataManager;

import java.util.Timer;
import java.util.TimerTask;

public class Unit {
    private Rectangle bounds;
    private Texture texture;
    private Sprite sprite;

    public int level = 0;
    public int energy = 0;//0-nothing, 1-coin, 2 star, 3 energy
    public int coin = 0;
    public int star = 0;

    public int type = 0;// 0-lock 1-unlock
    public int num;
    DataManager dm;

    public int price;
    public String discription;


    public Unit(float x, float y, int i) {
        num = i;
        dm = new DataManager("GameRunner");
        dm.setParam("Unit" + i);
        type = dm.load();
        switch (type) {
            case 0:
                texture = new Texture("lock.png");
                level = 0;
                energy = 0;
                coin = 0;
                star = 0;
                price = 0;
                discription = "";
                break;
            case 1:
                texture = new Texture("unlock.png");
                level = 0;
                energy = 0;
                coin = 0;
                star = 0;
                price = 100;
                discription = "";
                break;
            case 2:
                texture = new Texture("energy.png");
                level = 0;
                energy = 1;
                coin = 0;
                star = 0;
                price = 100;
                discription = "+1 energy";
                break;
            case 3:
                texture = new Texture("coinGen.png");
                level = 0;
                energy = -1;
                coin = 1;
                star = 0;
                price = 100;
                discription = "-1 energy; +1 coin.";
                break;
            case 4:
                texture = new Texture("starGen.png");
                level = 0;
                energy = -1;
                coin = 0;
                star = 1;
                price = 100;
                discription = "-1 energy; +1 star.";
                break;
            case 5:
                texture = new Texture("starGen.png");
                level = 10;
                energy = 21;
                coin = 0;
                star = 3;
                price = 100;
                discription = "-2 energy; +3 star.";
                break;
            case 6:
                texture = new Texture("coinGen.png");
                level = 10;
                energy = -2;
                coin = 4;
                star = 0;
                price = 100;
                discription = "-2 energy; +4 coin.";
                break;
        }
        sprite = new Sprite(texture);
        if(type == 0 || type == 1) {
            sprite.setColor(Colors.red);
        }
        sprite.setScale(0.5f);
        sprite.setCenter(x, y);
        bounds = new Rectangle(sprite.getBoundingRectangle());

    }
    boolean onTime = false;
    int TIME = 30;
    int time = TIME;
    public void update(float delta) {
        //long curTime = System.currentTimeMillis();
        //System.out.print(curTime + "\n");
        if(!onTime) {
            onTime = !onTime;
            com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {

                @Override
                public void run() {
                    time--;
                    if (time == 0) {
                        time = TIME;
                        long curTime = System.currentTimeMillis();

                        if(curTime > dm.loadDataTime("datatime" + num)) {
                            System.out.print(curTime + " " +  dm.loadDataTime("datatime" + num) +  "\n");
                            System.out.print(coin + " " +  star +  "\n");
                            if(coin > 0) {
                                dm.setParam("coins");
                                dm.plusData(coin);
                                dm.addDataTime("datatime" + num, curTime + 300000);
                            }
                            if(star > 0) {
                                dm.setParam("star");
                                dm.plusData(star);
                                dm.addDataTime("datatime" + num, curTime + 300000);
                            }
                        }
                    }
                    onTime = !onTime;
                }
            }, 1);
        }
    }

    public boolean collide(float x, float y) {
        return bounds.contains(x, y);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public int getType() {
        return type;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Texture getTexture() {
        return texture;
    }

    public String getDiscription() {
        return discription;
    }

    public void dispose() {
        texture.dispose();
    }
}
