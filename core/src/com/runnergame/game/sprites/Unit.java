package com.runnergame.game.sprites;

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

    public int giveParam = 0;//0-nothing, 1-coin, 2 star, 3 energy
    public int giveCount = 0;

    public int takeParam = 0;
    public int takeCount = 0;

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
                giveParam = 0;
                giveCount = 0;
                takeParam = 0;
                takeCount = 0;
                price = 0;
                discription = "";
                break;
            case 1:
                texture = new Texture("unlock.png");
                giveParam = 0;
                giveCount = 0;
                takeParam = 0;
                takeCount = 0;
                price = 100;
                discription = "";
                break;
            case 2:
                texture = new Texture("energy.png");
                giveParam = 3;
                giveCount = 1;
                takeParam = 0;
                takeCount = 0;
                price = 100;
                discription = "+1 energy";
                break;
            case 3:
                texture = new Texture("coinGen.png");
                giveParam = 1;
                giveCount = 1;
                takeParam = 3;
                takeCount = 1;
                price = 100;
                discription = "-1 energy; +1 coin.";
                break;
            case 4:
                texture = new Texture("starGen.png");
                giveParam = 2;
                giveCount = 1;
                takeParam = 3;
                takeCount = 1;
                price = 100;
                discription = "-1 energy; +1 star.";
                break;
        }
        //System.out.print(type);
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
        if(!onTime) {
            onTime = !onTime;
            com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {

                @Override
                public void run() {
                    time--;
                    if (time == 0) {
                        time = TIME;
                        if(giveParam == 1)
                            dm.setParam("coins");
                        if(giveParam == 2)
                            dm.setParam("star");
                        dm.plusData(1);
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
