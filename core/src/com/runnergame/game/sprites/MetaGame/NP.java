package com.runnergame.game.sprites.MetaGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.runnergame.game.GameRunner;

public class NP extends Building {

    public NP(int _t) {
        super(_t);
        name = "NP";
        level = 10;
        max_level = 3;
        level_now = GameRunner.dm.load2("NP_lvl");
        price = 100 * level_now + 100;
        if(level_now == 0) {
            sprite = new Sprite(new Texture(Gdx.files.internal("meta/tree1.png")));
        } else if(level_now == 1) {
            sprite = new Sprite(new Texture(Gdx.files.internal("meta/NP.png")));
        } else if(level_now == 2) {
            sprite = new Sprite(new Texture(Gdx.files.internal("meta/NP1.png")));
        }  else if(level_now == 3) {
            sprite = new Sprite(new Texture(Gdx.files.internal("meta/NP2.png")));
        }
        sprite.setCenter(499-__x, __y-827);
        bounds = new Rectangle(sprite.getBoundingRectangle());
        pos.x = sprite.getX();
        pos.y = sprite.getY();
    }

    @Override
    public boolean update(int p) {
        return false;
    }
}