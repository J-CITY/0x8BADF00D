package com.runnergame.game.sprites.MetaGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.runnergame.game.GameRunner;


public class CityBuild extends  Building {
    public CityBuild(int _t) {
        super(_t);
        name = "City";
        level = 0;
        max_level = 1;
        price = 0;
        //GameRunner.dm.addData2("City_lvl", 0);
        level_now = GameRunner.dm.load2("City_lvl");
        if(level_now == 0) {
            sprite = new Sprite(new Texture(Gdx.files.internal("meta/city_ic.png")));
        } else {
            sprite = new Sprite(new Texture(Gdx.files.internal("meta/city.png")));
        }

        sprite.setPosition(300, 450);
        sprite.setCenter(300, 450);
        bounds = new Rectangle(sprite.getBoundingRectangle());

        cardSprite = new Sprite(new Texture("cityCard.png"));
    }

    @Override
    public void update(float delta, float _x) {

    }

}
