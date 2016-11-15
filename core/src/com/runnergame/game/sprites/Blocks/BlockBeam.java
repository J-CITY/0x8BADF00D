package com.runnergame.game.sprites.Blocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.runnergame.game.sprites.*;

public class BlockBeam extends com.runnergame.game.sprites.Blocks.Block {
    public BlockBeam(float x, float y, int type) {
        super(x, type);
        TYPE = 6;
        color = 0;
        animOn = new Animation(new TextureRegion(new Texture("blocks/beamOn.png")), 3, 1);
        animOff = new Animation(new TextureRegion(new Texture("blocks/beamOff.png")), 1, 1);

        sprite = animOn.getSprite();
        sprite.setCenter(x, y);
        bounds = new Rectangle(sprite.getBoundingRectangle());
        pos = new Vector2(x, y);
        //texOn = new Texture("beamOn.png");
        //texOff = new Texture("beamOff.png");
        //spriteOn = new Sprite(texOn);
        //spriteOff = new Sprite(texOff);
        //bounds = new Rectangle(spriteOn.getBoundingRectangle());
        //spriteOn.setCenter(pos.x, pos.y);
        //spriteOff.setCenter(pos.x, pos.y);
        //bounds.setCenter(pos.x, pos.y);
    }

    @Override
    public Vector2 getPos() {
        return pos;
    }


    @Override
    public void update(float delta, float _x) {
        animOn.update(delta);
        animOff.update(delta);
        pos.add(-(speed+250) * delta, 0.0f);
        //spriteOn.setCenter(pos.x, pos.y);
        //spriteOff.setCenter(pos.x, pos.y);
        bounds.setPosition(pos.x, pos.y);
        bounds.setCenter(pos.x, pos.y);
        if(pos.x < _x - 160) {
            pos.y -= 10;
            //spriteOn.setCenter(pos.x, pos.y);
            //spriteOff.setCenter(pos.x, pos.y);
            bounds.setCenter(pos.x, pos.y);
        }
    }

    @Override
    public boolean collide(Player player) {
        if(bounds.overlaps(player.getBounds()) && color == player.color &&
                player.getPosition().y - player.getSprite().getHeight()/2 + 5 < pos.y + sprite.getHeight()/2 - 5) {
            player.setLife(false);
            player.jump(300);
        }
        return false;
    }

    @Override
    public void dispose() {
        //texOn.dispose();
        //texOff.dispose();
    }
}
