package com.runnergame.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.runnergame.game.GameRunner;

public class BlockFloor extends Block {

    public BlockFloor(float x, float y, int type) {
        super(x, type);
        color = 0;
        texOn = new Texture("FloorOn.png");
        texOff = new Texture("FloorOff.png");
        spriteOn = new Sprite(texOn);
        spriteOff = new Sprite(texOff);
        bounds = new Rectangle(spriteOn.getBoundingRectangle());
        pos = new Vector2(x, y);
        spriteOn.setCenter(pos.x, pos.y);
        spriteOff.setCenter(pos.x, pos.y);
        //spriteOff.setScale(0.5f);
        //spriteOn.setScale(0.5f);
        bounds.setCenter(pos.x, pos.y);
    }

    @Override
    public void reposition(float x, float y) {
        pos.set(x, y);
        spriteOn.setCenter(pos.x, pos.y);
        spriteOff.setCenter(pos.x, pos.y);
        bounds.setCenter(pos.x, pos.y);
    }

    @Override
    public Vector2 getPos() {
        return pos;
    }


    @Override
    public void update(float delta, float _x) {
        pos.add(-400 * delta, 0.0f);
        spriteOn.setCenter(pos.x, pos.y);
        spriteOff.setCenter(pos.x, pos.y);
        bounds.setPosition(pos.x, pos.y);
        bounds.setCenter(pos.x, pos.y);
        if(pos.x < _x - 160) {
            pos.y -= 10;
            spriteOn.setCenter(pos.x, pos.y);
            spriteOff.setCenter(pos.x, pos.y);
            bounds.setCenter(pos.x, pos.y);
        }
    }

    @Override
    public boolean collide(Rectangle player) {
        return bounds.overlaps(player);
    }

    @Override
    public void dispose() {
        texOn.dispose();
        texOff.dispose();
    }
}
