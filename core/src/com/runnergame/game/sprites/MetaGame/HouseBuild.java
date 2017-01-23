package com.runnergame.game.sprites.MetaGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.runnergame.game.Constants;
import com.runnergame.game.GameRunner;


public class HouseBuild extends Building {

    public HouseBuild(int _t, float x, float y) {
        super(_t);
        name = "House";
        level = 10;
        max_level = 3;
        level_now = GameRunner.dm.load2("House_lvl");
        price = 100 * level_now + 100;
        if(level_now == 0) {
            sprite = new Sprite(new Texture(Gdx.files.internal("meta/tree1.png")));
        } else if(level_now == 1) {
            sprite = new Sprite(new Texture(Gdx.files.internal("meta/house1.png")));
        } else if(level_now == 2) {
            sprite = new Sprite(new Texture(Gdx.files.internal("meta/house2.png")));
        } else if(level_now == 3) {
            sprite = new Sprite(new Texture(Gdx.files.internal("meta/house3.png")));
        }
        sprite.setCenter(2616-__x, __y-491);
        bounds = new Rectangle(sprite.getBoundingRectangle());
    }

    @Override
    public boolean update(int p) {
        if(p == 0) {
            return true;
        } else if(p == 1) {
            return  true;
        } else if(p == 2) {
            return true;
        }
        return false;
    }
}
