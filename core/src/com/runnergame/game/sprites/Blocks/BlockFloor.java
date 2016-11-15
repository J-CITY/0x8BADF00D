package com.runnergame.game.sprites.Blocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.runnergame.game.Colors;
import com.runnergame.game.sprites.*;

public class BlockFloor extends com.runnergame.game.sprites.Blocks.Block {

    public BlockFloor(float x, float y, int type) {
        super(x, type);
        color = 0;
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
            pos.y -= 10;
            bounds.setCenter(pos.x, pos.y);
        }
    }

    @Override
    public boolean collide(Player player) {
        if(bounds.overlaps(player.getBounds()) &&
                (color == Colors.GRAY || color == player.color) ) {
            player.flag = false;
            if(pos.y + 20 < player.getPosition().y - 20) {
                player.onFloor(pos.y+63);
            } else if((pos.y + 20 > player.getPosition().y - 20) && (pos.x-20 > player.getPosition().x + 20)) {
                player.setLife(false);
                player.jump(300);
            }
        }
        return false;
    }

    @Override
    public void dispose() {
    }
}
