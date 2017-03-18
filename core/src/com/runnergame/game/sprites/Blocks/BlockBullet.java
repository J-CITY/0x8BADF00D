package com.runnergame.game.sprites.Blocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.runnergame.game.Colors;
import com.runnergame.game.sprites.Animation;
import com.runnergame.game.sprites.Player;

public class BlockBullet extends Block {
    int DIR;
    private float X0, Y0;
    public boolean isLife = true;

    public BlockBullet(float x, float y, int type, int dir, float _x0, float _y0) {
        super(x, type);
        DIR = dir;
        X0 = _x0;
        Y0 = _y0;
        color = 0;
        animOn = new Animation(new TextureRegion(new Texture("blocks/bullet_on.png")), 1, 1);
        animOff = new Animation(new TextureRegion(new Texture("blocks/bullet_off.png")), 1, 1);
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

        double distance = Math.sqrt((X0 - pos.x)*(X0 - pos.x) +
                (Y0 - pos.y)*(Y0 - pos.y));
        if (distance > 10) {
            pos.x += 400 * delta * (X0 - pos.x) / distance;
            pos.y += 400 * delta * (Y0 - pos.y) / distance;
        } else {
            isLife = false;
        }


        //pos.add(DIR*speed * delta, 0.0f);
        bounds.setPosition(pos.x, pos.y);
        bounds.setCenter(pos.x, pos.y);

    }

    @Override
    public boolean collide(Player player) {
        if(bounds.overlaps(player.getBounds()) &&
                (color == player.color) && DIR < 0) {
            //player.setLife(false);
            //player.jump(300);
            isLife = false;
            return true;
        }
        return false;
    }

    public boolean collideBoss(Rectangle r) {
        if(bounds.overlaps(r) && DIR > 0) {
            isLife = false;
            return true;
        }
        return false;
    }
    public boolean getLife() {
        return isLife;
    }

    @Override
    public void dispose() {
        animOff.dispouse();
        animOn.dispouse();
        sprite.getTexture().dispose();
    }
}
