package com.runnergame.game.sprites.MetaGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.runnergame.game.Constants;
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

        sprite.setPosition(-730, -200);
        sprite.setCenter(-730, -200);
        bounds = new Rectangle(sprite.getBoundingRectangle());
    }

    @Override
    public boolean update(int p) {
        if(p == 0) {
            if (GameRunner.dm.load2(Constants.CITI_PARAM) == 3 &&
                    GameRunner.dm.load2(Constants.HOUSE_PARAM) >= 2) {
                return true;
            }
        }
        return false;
    }
}
