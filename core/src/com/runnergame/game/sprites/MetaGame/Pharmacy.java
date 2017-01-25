package com.runnergame.game.sprites.MetaGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.runnergame.game.GameRunner;

public class Pharmacy extends Building {

    public Pharmacy(int _t) {
        super(_t);
        name = "Pharmacy";
        level = 10;
        max_level = 1;
        level_now = GameRunner.dm.load2("Pharmacy_lvl");
        price = 100;
        if(level_now == 0) {
            sprite = new Sprite(new Texture(Gdx.files.internal("meta/tree2.png")));
        } else {
            sprite = new Sprite(new Texture(Gdx.files.internal("meta/pharmacy.png")));
        }
        sprite.setCenter(2476-__x, __y-680);
        bounds = new Rectangle(sprite.getBoundingRectangle());
        pos.x = sprite.getX();
        pos.y = sprite.getY();
    }

    @Override
    public boolean update(int p) {
        return false;
    }
}