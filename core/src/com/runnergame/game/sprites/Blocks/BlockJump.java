package com.runnergame.game.sprites.Blocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.runnergame.game.Constants;
import com.runnergame.game.sprites.*;

public class BlockJump extends com.runnergame.game.sprites.Blocks.Block {
    float vel = 350;

    public BlockJump(float x, float y, int type, float _vel) {
        super(x, type);
        vel = _vel;
        TYPE = 4;
        color = 0;
        animOn = new Animation(new TextureRegion(new Texture("blocks/jump.png")), 3, 1);
        animOff = new Animation(new TextureRegion(new Texture("blocks/jump.png")), 3, 1);
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
        if (bounds.overlaps(player.getBounds())) {
            if((pos.y + 5 > player.getPosition().y - 5) && (pos.x - 5 > player.getPosition().x + 5)) {
                player.setLife(false);
                player.jump(300);
                return false;
            }
            if(color == player.color) {
                player.jump(vel);
            } else  if(pos.y < player.getPosition().y) {
                player.onFloor(pos.y + player.getBounds().getHeight()/2 + Constants.BLOCK_H/2 - 1);
            }
            player.flag = false;
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
