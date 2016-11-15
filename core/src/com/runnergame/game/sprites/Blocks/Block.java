package com.runnergame.game.sprites.Blocks;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.runnergame.game.GameRunner;
import com.runnergame.game.sprites.Animation;
import com.runnergame.game.sprites.Player;

import java.util.Random;

public abstract class Block {
    public int TYPE = 0;
    public boolean life = true;

    protected Animation animOn, animOff;

    protected Sprite sprite;
    protected Vector2 pos;
    protected Random rand;
    protected Rectangle bounds;
    protected static float speed = 400;
    protected static float speed0 = 400;

    public int color = 0;//0- def; 1- blue; 2- red

    public Block(float x, int type) {
        TYPE = type;
    }

    public abstract void update(float delta, float _x);

    public abstract Vector2 getPos();

    public abstract boolean collide(Player player);

    public Sprite getSprite(int p_color) {
        if(color == p_color || color == 0) {
            sprite = animOn.getSprite();
        } else {
            sprite = animOff.getSprite();
        }
        if(color == 0) {
            sprite.setColor(GameRunner.colors.gray);
        } else if(color == 1) {
            sprite.setColor(GameRunner.colors.blue);
        } else if(color == 2) {
            sprite.setColor(GameRunner.colors.red);
        }
        sprite.setCenter(pos.x, pos.y);
        return sprite;

    }

    public void setSpeed(float s) {
        speed = s;
    }
    public void setSpeed0(float s) {
        speed0 = s;
    }

    public void setColor(int c) {
        color = c;
    }
    public void setPos(float _x, float _y) {
        pos.x = _x;
        pos.y = _y;
    }

    public abstract void dispose();

}