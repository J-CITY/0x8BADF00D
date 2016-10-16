package com.runnergame.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.runnergame.game.GameRunner;

import java.util.Random;

public abstract class Block {
    public int TYPE = 0;

    protected Texture texOn, texOff;
    protected Sprite spriteOn;
    protected Sprite spriteOff;
    protected Vector2 pos;
    protected Random rand;
    protected Rectangle bounds;

    public int color = 0;//0- def; 1- blue; 2- red

    public Block(float x, int type) {
 //       tex = new Texture("block.png");
        TYPE = type;
        //pos = new Vector2(x, rand.nextInt(FLUCTUATION) + 20);

    }

    public abstract void update(float delta, float _x);

    public abstract void reposition(float x, float y);

    public abstract Vector2 getPos();

    public abstract boolean collide(Rectangle player);

    public Sprite getSprite(int p_color) {
        if(color == p_color || color == 0) {
            return spriteOn;
        } else {
            return spriteOff;
        }

    }

    public void setColor(int c) {
        color = c;
        if(color == 0) {
            spriteOff.setColor(GameRunner.colors.gray);
            spriteOn.setColor(GameRunner.colors.gray);
        }
        if(color == 1) {
            spriteOff.setColor(GameRunner.colors.blue);
            spriteOn.setColor(GameRunner.colors.blue);
        }
        if(color == 2) {
            spriteOff.setColor(GameRunner.colors.red);
            spriteOn.setColor(GameRunner.colors.red);
        }
    }

    public abstract void dispose();
}
