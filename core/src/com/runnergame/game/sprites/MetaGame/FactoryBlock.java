package com.runnergame.game.sprites.MetaGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.runnergame.game.GameRunner;

/**
 * Created by 333da on 22.01.2017.
 */

public class FactoryBlock extends Building {
    public FactoryBlock(int _t) {
        super(_t);
        name = "Factory_block";
        level = 20;
        max_level = 1;
        level_now = GameRunner.dm.load2("Factory_block_lvl");
        price = 100;
        if(level_now == 0) {
            sprite = new Sprite(new Texture(Gdx.files.internal("meta/tree2.png")));
        } else {
            sprite = new Sprite(new Texture(Gdx.files.internal("meta/factory_block.png")));
        }
        sprite.setCenter(701-__x, __y-1002);
        bounds = new Rectangle(sprite.getBoundingRectangle());
    }

    @Override
    public boolean update(int p) {
        return false;
    }
}