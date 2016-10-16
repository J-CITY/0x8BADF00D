package com.runnergame.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Button {

    private Rectangle bounds;
    private Texture texture;
    private Sprite sprite;

    public Button(String tex_path, float x, float y) {
        texture = new Texture(tex_path);
        sprite = new Sprite(texture);
        sprite.setCenter(x, y);
        bounds = new Rectangle(sprite.getBoundingRectangle());

    }

    public boolean collide(float x, float y) {
        return bounds.contains(x, y);
    }

    public Sprite getSprite() {
        return sprite;
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
