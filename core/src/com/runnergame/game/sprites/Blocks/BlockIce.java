package com.runnergame.game.sprites.Blocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.runnergame.game.Colors;
import com.runnergame.game.Constants;
import com.runnergame.game.sprites.Animation;
import com.runnergame.game.sprites.Player;

public class BlockIce extends Block {
    boolean flag = true;
    public BlockIce(float x, float y, int type) {
        super(x, type);
        color = 0;
        animOn = new Animation(new TextureRegion(new Texture("blocks/iceOn.png")), 3, 0.5f);
        animOff = new Animation(new TextureRegion(new Texture("blocks/iceOff.png")), 3, 0.5f);
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

        if(!flag && animOff.getFrameNow() < animOff.getFrameCount()-1) {
            animOn.update(delta);
            animOff.update(delta);
        }
        if(animOff.getFrameNow() >= animOff.getFrameCount()-1) {
            life = false;
        }

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
        if(bounds.overlaps(player.getBounds()) &&
                (color == Colors.GRAY || color == player.color) && Block.speed > Block.speed0 ) {
            flag = false;
            //life = false;
            player.flag = false;
            if(pos.y + 2 < player.getPosition().y - 2) {
                player.onFloor(pos.y + player.getBounds().getHeight()/2 + Constants.BLOCK_H/2 - 1);
            } else if((pos.y + 5 > player.getPosition().y - 5) && (pos.x-5 > player.getPosition().x + 5)) {
                player.setLife(false);
                player.jump(300);
            }
        }
        return false;
    }

    @Override
    public void dispose() {
        Block.speed = Block.speed0;
    }
}
