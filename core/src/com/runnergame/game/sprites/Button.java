package com.runnergame.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Button {

    private Rectangle bounds;
    private Texture texture;
    private Sprite sprite;
    private Vector2 pos;

    public Button(String tex_path, float x, float y) {
        texture = new Texture(tex_path);
        sprite = new Sprite(texture);
        sprite.setCenter(x, y);
        bounds = new Rectangle(sprite.getBoundingRectangle());
        pos = new Vector2(x, y);
    }

    public Vector2 getPos() {
        return pos;
    }

    public boolean collide(float x, float y) {
        return bounds.contains(x, y);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setScale(float scl) {
        sprite.setScale(scl);
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
