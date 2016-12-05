package com.runnergame.game.sprites.MetaGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.runnergame.game.Constants;
import com.runnergame.game.GameRunner;

public class StadiumBuild extends Building {
    public StadiumBuild(int _t) {
        super(_t);
        name = "Stadium";
        level = 10;
        max_level = 1;
        //GameRunner.dm.addData2("Police_lvl", 0);
        level_now = GameRunner.dm.load2("Stadium_lvl");
        price = 200;
        if(level_now == 0) {
            sprite = new Sprite(new Texture(Gdx.files.internal("meta/stadium_ic.png")));
        } else {
            sprite = new Sprite(new Texture(Gdx.files.internal("meta/stadium.png")));
        }

        sprite.setPosition(1000, -50);
        sprite.setCenter(1000, -50);
        bounds = new Rectangle(sprite.getBoundingRectangle());
        cardSprite = new Sprite(new Texture("policeCard.png"));
    }

    @Override
    public boolean update(int p) {
        if(p == 0) {
            if (GameRunner.dm.load2(Constants.CITI_PARAM) == 3 &&
                    GameRunner.dm.load2(Constants.HOUSE_PARAM) == 3) {
                return true;
            }
        }
        return false;
    }
}
