package com.runnergame.game.sprites.Blocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.runnergame.game.GameRunner;
import com.runnergame.game.sprites.*;
import com.runnergame.game.states.PlayState;


public class BlockFinish extends com.runnergame.game.sprites.Blocks.Block {
    public BlockFinish(float x, float y, int type) {
        super(x, type);
        color = 0;
        animOn = new Animation(new TextureRegion(new Texture("finish.png")), 1, 1);
        animOff = new Animation(new TextureRegion(new Texture("finish.png")), 1, 1);
        sprite = animOn.getSprite();
        sprite.setCenter(x, y);
        bounds = new Rectangle(sprite.getBoundingRectangle());
        pos = new Vector2(x, y);
    }

    @Override
    public Vector2 getPos() {
        return pos;
    }


    @Override
    public void update(float delta, float _x) {
        animOn.update(delta);
        animOff.update(delta);
        pos.add(-speed * delta, 0.0f);

        bounds.setPosition(pos.x, pos.y);
        bounds.setCenter(pos.x, pos.y);
        if(pos.x < _x - 160) {
            pos.y -= 10;
            bounds.setCenter(pos.x, pos.y);
        }
    }

    @Override
    public boolean collide(Player player) {
        if(bounds.overlaps(player.getBounds())) {
            int l = GameRunner.dm.load2("level");
            if(l == PlayState.lvl)
                GameRunner.dm.addData2("level", PlayState.lvl+1);
            return true;
        }
        return false;
    }

    @Override
    public void dispose() {
    }
}
