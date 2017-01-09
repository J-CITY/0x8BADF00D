package com.runnergame.game.sprites.MetaGame;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.runnergame.game.Constants;
import com.runnergame.game.GameRunner;

public class RocketBuild extends Building {
    public RocketBuild(int _t) {
        super(_t);
        name = "Rocket";
        level = 10;
        max_level = 1;
        //GameRunner.dm.addData2("Police_lvl", 0);
        level_now = GameRunner.dm.load2("Rocket_lvl");
        price = 500;
        if(level_now == 0) {
            sprite = new Sprite(new Texture(Gdx.files.internal("meta/rocket_ic.png")));
        } else {
            sprite = new Sprite(new Texture(Gdx.files.internal("meta/rocket.png")));
        }

        sprite.setPosition(-1030, -1200);
        sprite.setCenter(-1030, -1200);
        bounds = new Rectangle(sprite.getBoundingRectangle());
    }

    @Override
    public boolean update(int p) {
        if(p == 0) {
            if (GameRunner.dm.load2(Constants.CITI_PARAM) == 3 &&
                    GameRunner.dm.load2(Constants.HOUSE_PARAM) == 3 &&
                    GameRunner.dm.load2(Constants.MED_PARAM)==1 &&
                    GameRunner.dm.load2(Constants.POLICE_PARAM)==1) {
                return true;
            }
        }
        return false;
    }
}
