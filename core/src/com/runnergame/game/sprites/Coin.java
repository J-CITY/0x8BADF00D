package com.runnergame.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.runnergame.game.GameRunner;

import java.util.Random;

public class Coin {
    public int TYPE = 0;
    public boolean life = true;

    protected Texture tex;
    protected Sprite sprite;
    protected Vector2 pos;
    protected Rectangle bounds;
    public float speed = 400;

    public Coin(float x, float y, int type) {
        if(type == 0) {
            tex = new Texture("coin.png");
        } else {
            tex = new Texture("star.png");
        }
        TYPE = type;
        sprite = new Sprite(tex);
        bounds = new Rectangle(sprite.getBoundingRectangle());
        pos = new Vector2(x, y);
        sprite.setCenter(pos.x, pos.y);
        sprite.setScale(0.5f);
        sprite.setColor(GameRunner.colors.yellow.r,
                GameRunner.colors.yellow.g,
                GameRunner.colors.yellow.b, 1);
        bounds.setCenter(pos.x, pos.y);
    }

    public Vector2 getPos() {
        return pos;
    }

    public void update(float delta, float _x) {
        pos.add(-speed * delta, 0.0f);
        sprite.setCenter(pos.x, pos.y);
        bounds.setPosition(pos.x, pos.y);
        bounds.setCenter(pos.x, pos.y);
        if(pos.x < _x - 160) {
            pos.y -= 10;
            sprite.setCenter(pos.x, pos.y);
            bounds.setCenter(pos.x, pos.y);
        }
    }

    public boolean collide(Rectangle player) {
        return bounds.overlaps(player);
    }

    public void dispose() {
        tex.dispose();
    }

    public Sprite getSprite() {
        return sprite;
    }

}
