package com.runnergame.game.sprites;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.runnergame.game.Constants;
import com.runnergame.game.GameRunner;

public class Background {
    Sprite sprite_bg;
    private Vector2 pos;

    public Background(float x, float y) {
        sprite_bg = new Sprite(new Texture("bg.png"));
        pos = new Vector2(x, y);
        sprite_bg.setCenter(x, y);
    }

    public Sprite getBgSprite() {
        return sprite_bg;
    }

}
