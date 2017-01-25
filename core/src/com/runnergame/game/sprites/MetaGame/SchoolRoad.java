package com.runnergame.game.sprites.MetaGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.runnergame.game.GameRunner;

public class SchoolRoad extends Building {

    public SchoolRoad(int _t) {
        super(_t);
        name = "School_road";
        level = 10;
        max_level = 1;
        level_now = GameRunner.dm.load2("School_road_lvl");
        price = 100;
        if(level_now == 0) {
            sprite = new Sprite(new Texture(Gdx.files.internal("meta/tree2.png")));
        } else  {
            sprite = new Sprite(new Texture(Gdx.files.internal("meta/school_road.png")));
        }
        sprite.setCenter(1681-__x, __y-1451);
        bounds = new Rectangle(sprite.getBoundingRectangle());
        pos.x = sprite.getX();
        pos.y = sprite.getY();
    }

    @Override
    public boolean update(int p) {
        return false;
    }
}