package com.runnergame.game.sprites.MetaGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.runnergame.game.GameRunner;

/**
 * Created by 333da on 22.01.2017.
 */

public class Cafe extends Building {
    public Cafe(int _t) {
        super(_t);
        name = "Cafe";
        level = 20;
        max_level = 1;
        level_now = GameRunner.dm.load2("Cafe_lvl");
        price = 100;
        if(level_now == 0) {
            sprite = new Sprite(new Texture(Gdx.files.internal("meta/tree1.png")));
        } else {
            sprite = new Sprite(new Texture(Gdx.files.internal("meta/cafe.png")));
        }
        sprite.setCenter(1109-__x, __y-821);
        bounds = new Rectangle(sprite.getBoundingRectangle());
        pos.x = sprite.getX();
        pos.y = sprite.getY();
    }

    @Override
    public boolean update(int p) {
        return false;
    }
}
