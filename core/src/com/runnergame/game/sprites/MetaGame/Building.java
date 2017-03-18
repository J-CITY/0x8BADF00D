package com.runnergame.game.sprites.MetaGame;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.runnergame.game.GameRunner;
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
    public boolean isVisable = true;
    protected Rectangle bounds;
    protected Sprite sprite;
    protected Vector2 pos;
    public float alphaChannel = 1f;

    public Building(int _t) {
        type = _t;
        pos = new Vector2();
    }

    public void dispouse() {
        sprite.getTexture().dispose();
    }

    public boolean update(float delta) {
        if(alphaChannel < 1) {
            alphaChannel += 2*delta;
            return false;
        } else {
            alphaChannel = 1;
            return true;
        }
    }

    public Sprite getSprite()
    {
        sprite.setAlpha(alphaChannel);
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

    public boolean updateBuild() {
        int lvl = GameRunner.dm.load2(name+"_lvl");
        if(lvl != level_now) {
            isVisable = false;
            level_now = lvl;
            return true;
        }
        return false;
    }

    public boolean collide(float x, float y) {
        return bounds.contains(x, y);
    }
}
