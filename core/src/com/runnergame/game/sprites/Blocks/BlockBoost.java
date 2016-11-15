package com.runnergame.game.sprites.Blocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.runnergame.game.sprites.Animation;
import com.runnergame.game.sprites.Player;

public class BlockBoost extends Block {
    public static float vel = 0;
    float vel0 = 0;
    public static float vel_new = 0;

    public BlockBoost(float x, float y, int type, float _vel) {
        super(x, type);
        vel0 = _vel;
        TYPE = 4;
        color = 0;
        animOn = new Animation(new TextureRegion(new Texture("blocks/boost.png")), 3, 1);
        animOff = new Animation(new TextureRegion(new Texture("blocks/boost.png")), 3, 1);
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

        speed = speed0 + vel;
        System.out.print(speed+"\n");

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
        if (bounds.overlaps(player.getBounds())) {
            if((pos.y + 10 > player.getPosition().y - 10) && (pos.x-10 > player.getPosition().x + 10)) {
                player.setLife(false);
                return false;
            }
            if(color == player.color) {
                vel = vel0;
                //player.boost(vel);
                player.onFloor(pos.y+63);
            } else  if(pos.y < player.getPosition().y) {
                player.onFloor(pos.y+63);
            }
            player.flag = false;
        }
        return false;
    }

    public static void updateVel() {
        if(vel <= 2)
            vel = 0;
        else
            vel -= 2;
    }

    @Override
    public void dispose() {
    }
}
