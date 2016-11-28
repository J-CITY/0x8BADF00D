package com.runnergame.game.sprites.MetaGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.runnergame.game.GameRunner;

public class MedBuild extends Building {
    public MedBuild(int _t) {
        super(_t);
        name = "Med";
        level = 20;
        max_level = 1;
        //GameRunner.dm.addData2("Police_lvl", 0);
        level_now = GameRunner.dm.load2("Med_lvl");
        price = 100;
        if(level_now == 0) {
            sprite = new Sprite(new Texture(Gdx.files.internal("meta/med_ic.png")));
        } else {
            sprite = new Sprite(new Texture(Gdx.files.internal("meta/med.png")));
        }

        sprite.setPosition(200, -50);
        sprite.setCenter(200, -50);
        bounds = new Rectangle(sprite.getBoundingRectangle());
        cardSprite = new Sprite(new Texture("policeCard.png"));
    }

    @Override
    public void update(float delta, float _x) {

    }
}
