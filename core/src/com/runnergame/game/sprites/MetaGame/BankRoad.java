package com.runnergame.game.sprites.MetaGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.runnergame.game.GameRunner;

public class BankRoad extends Building {

    public BankRoad(int _t) {
        super(_t);
        name = "Bank_road";
        level = 10;
        max_level = 1;
        level_now = GameRunner.dm.load2("Bank_road_lvl");
        price = 100 * level_now + 100;
        if(level_now == 0) {
            sprite = new Sprite(new Texture(Gdx.files.internal("meta/tree1.png")));
        } else {
            sprite = new Sprite(new Texture(Gdx.files.internal("meta/bank_road.png")));
        }
        sprite.setCenter(1734-__x, __y-901);
        bounds = new Rectangle(sprite.getBoundingRectangle());
        pos.x = sprite.getX();
        pos.y = sprite.getY();
    }

}