package com.runnergame.game.sprites.MetaGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.runnergame.game.GameRunner;

/**
 * Created by 333da on 22.01.2017.
 */

public class School extends Building {
    public School(int _t) {
        super(_t);
        name = "School";
        level = 20;
        max_level = 1;
        level_now = GameRunner.dm.load2("School_lvl");
        price = 100;
        if(level_now == 0) {
            sprite = new Sprite(new Texture(Gdx.files.internal("meta/tree1.png")));
        } else {
            sprite = new Sprite(new Texture(Gdx.files.internal("meta/school.png")));
        }
        sprite.setCenter(1908-__x, __y-1296);
        bounds = new Rectangle(sprite.getBoundingRectangle());
        pos.x = sprite.getX();
        pos.y = sprite.getY();
    }

}