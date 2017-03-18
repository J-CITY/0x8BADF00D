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

    public Background(float x, float y, int scheme) {
        switch (scheme) {
            case 0:
                sprite_bg = new Sprite(new Texture("bgWall0.png"));
                break;
            case 1:
                sprite_bg = new Sprite(new Texture("bgWall1.png"));
                break;
        }
        pos = new Vector2(x, y);
        sprite_bg.setCenter(x, y);
    }

    public Sprite getBgSprite() {
        return sprite_bg;
    }

    public void dispouse() {
        sprite_bg.getTexture().dispose();
    }

}
