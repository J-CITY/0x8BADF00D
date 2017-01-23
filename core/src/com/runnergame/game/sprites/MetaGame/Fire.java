package com.runnergame.game.sprites.MetaGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.runnergame.game.GameRunner;

/**
 * Created by 333da on 22.01.2017.
 */

public class Fire extends Building {
    public Fire(int _t) {
        super(_t);
        name = "Fire";
        level = 20;
        max_level = 1;
        level_now = GameRunner.dm.load2("Fire_lvl");
        price = 100;
        if(level_now == 0) {
            sprite = new Sprite(new Texture(Gdx.files.internal("meta/tree2.png")));
        } else {
            sprite = new Sprite(new Texture(Gdx.files.internal("meta/fire.png")));
        }
        sprite.setCenter(1385-__x, __y-1410);
        bounds = new Rectangle(sprite.getBoundingRectangle());
    }

    @Override
    public boolean update(int p) {
        return false;
    }
}