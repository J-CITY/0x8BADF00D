package com.runnergame.game.sprites.MetaGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.runnergame.game.GameRunner;

public class NPRoad extends Building {

    public NPRoad(int _t) {
        super(_t);
        name = "NP_road";
        level = 10;
        max_level = 1;
        level_now = GameRunner.dm.load2("NP_road_lvl");
        price = 100;
        if(level_now == 0) {
            sprite = new Sprite(new Texture(Gdx.files.internal("meta/tree2.png")));
        } else {
            sprite = new Sprite(new Texture(Gdx.files.internal("meta/NP_road.png")));
        }
        sprite.setCenter(249-__x, __y-909);
        bounds = new Rectangle(sprite.getBoundingRectangle());
    }

    @Override
    public boolean update(int p) {
        return false;
    }
}