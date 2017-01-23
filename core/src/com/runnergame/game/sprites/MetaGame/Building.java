package com.runnergame.game.sprites.MetaGame;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.runnergame.game.sprites.Animation;
import com.runnergame.game.states.DataManager;

public abstract class Building {
    String name = new String();
    protected float __x = 1160;
    protected float __y = 1327;
    int level;
    int level_now;
    int max_level;
    int type;
    int price;
    protected Rectangle bounds;
    protected Animation anim;
    protected Sprite sprite;
    protected Vector2 pos;

    public Building(int _t) {
        type = _t;
    }

    public abstract boolean update(int p);

    public Sprite getSprite()
    {
        return sprite;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }
    public int getLevel_now() {
        return level_now;
    }
    public int getMax_level() {
        return max_level;
    }
    public String getParam() {
        return name+"_lvl";
    }
    public Vector2 getPos() {
        return pos;
    }

    public boolean collide(float x, float y) {
        return bounds.contains(x, y);
    }
}
