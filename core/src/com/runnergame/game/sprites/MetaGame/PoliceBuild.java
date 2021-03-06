package com.runnergame.game.sprites.MetaGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.runnergame.game.Constants;
import com.runnergame.game.GameRunner;

public class PoliceBuild extends Building {
    public PoliceBuild(int _t) {
        super(_t);
        name = "Police";
        level = 10;
        max_level = 1;
        //GameRunner.dm.addData2("Police_lvl", 0);
        level_now = GameRunner.dm.load2("Police_lvl");
        price = 100;
        if(level_now == 0) {
            sprite = new Sprite(new Texture(Gdx.files.internal("meta/tree1.png")));
        } else {
            sprite = new Sprite(new Texture(Gdx.files.internal("meta/police.png")));
        }
        sprite.setCenter(1406-__x, __y-1065);
        bounds = new Rectangle(sprite.getBoundingRectangle());
        pos.x = sprite.getX();
        pos.y = sprite.getY();
    }


}
