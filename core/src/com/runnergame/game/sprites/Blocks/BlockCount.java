package com.runnergame.game.sprites.Blocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.runnergame.game.Colors;
import com.runnergame.game.Constants;
import com.runnergame.game.sprites.Animation;
import com.runnergame.game.sprites.Player;

/**
 * Created by 333da on 28.02.2017.
 */

public class BlockCount extends Block {
    public BlockCount(float x, float y, int type) {
        super(x, type);
        color = 0;
        TYPE = Constants.B_COUNT;
        animOn = new Animation(new TextureRegion(new Texture("blocks/floorOn.png")), 1, 1);
        animOff = new Animation(new TextureRegion(new Texture("blocks/floorOff.png")), 1, 1);
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
            pos.y -= GRAVITY;
            bounds.setCenter(pos.x, pos.y);
        }
    }

    @Override
    public boolean collide(Player player) {
        if(bounds.overlaps(player.getBounds()) &&
                (color == Colors.GRAY || color == player.color) ) {
            player.flag = false;
            if(pos.y + 2 < player.getPosition().y - 2) {
                player.onFloor(pos.y + player.getBounds().getHeight()/2 + Constants.BLOCK_H/2 - 1);
            } else if((pos.y + 2 > player.getPosition().y - 2) && (pos.x-2 > player.getPosition().x + 2)) {
                player.setLife(false);
                player.jump(300);
            }
        }
        return false;
    }

    @Override
    public void dispose() {
        animOff.dispouse();
        animOn.dispouse();
        sprite.getTexture().dispose();
    }
}
