package com.runnergame.game.sprites.MetaGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Map {
    Sprite sprite;
    Vector2 pos;

    public Map(float x, float y) {
        sprite = new Sprite(new Texture(Gdx.files.internal("meta/map.png")));
        sprite.setPosition(x, y);
        sprite.setCenter(x, y);
    }

    public Sprite getSprite() {
        return sprite;
    }
}
