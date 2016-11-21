package com.runnergame.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Button {

    private Rectangle bounds;
    private Texture texture;
    private Animation anim;
    private Sprite sprite;
    private Vector2 pos;
    private float scl = 1;

    public Button(String tex_path, float x, float y, int framesCount, float frameTime) {
        texture = new Texture(tex_path);
        anim = new Animation(new TextureRegion(texture), framesCount, frameTime);
        sprite = anim.getSprite();
        sprite.setCenter(x, y);
        bounds = new Rectangle(sprite.getBoundingRectangle());
        pos = new Vector2(x, y);
    }

    public void update(float delta) {
        anim.update(delta);
    }

    public Vector2 getPos() {
        return pos;
    }
    public void setPos(float x, float y) {
        pos.x = x;
        pos.y = y;
        bounds.setCenter(x, y);
    }

    public boolean collide(float x, float y) {
        return bounds.contains(x, y);
    }

    public Sprite getSprite() {
        sprite = anim.getSprite();
        sprite.setCenter(pos.x, pos.y);
        sprite.setScale(scl);
        return sprite;
    }

    public void setScale(float _scl) {
        scl = _scl;
        /// /sprite.setScale(scl);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Texture getTexture() {
        return texture;
    }

    public void dispose() {
        texture.dispose();
    }
}
