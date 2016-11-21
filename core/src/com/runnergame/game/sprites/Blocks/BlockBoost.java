package com.runnergame.game.sprites.Blocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.runnergame.game.Constants;
import com.runnergame.game.GameRunner;
import com.runnergame.game.sprites.Animation;
import com.runnergame.game.sprites.Player;
import com.runnergame.game.states.DataManager;
import com.runnergame.game.states.WinState;

public class BlockBoost extends Block {
    float vel;

    public BlockBoost(float x, float y, int type, float _vel) {
        super(x, type);
        vel = _vel;
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

    private int time = 2, time0 = 2;
    private boolean onTime = false;
    long lastDropTime;
    @Override
    public void update(float delta, float _x) {
        if(!onTime) {
            onTime = !onTime;
            Timer.schedule(new Timer.Task() {

                @Override
                public void run() {
                    time--;
                    if (time == 0) {
                        Block.speed = Block.speed0;
                    }
                    onTime = !onTime;
                }
            }, 1);
        }
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
        if (bounds.overlaps(player.getBounds())) {
            if((pos.y + 2 > player.getPosition().y - 2) && (pos.x-2 > player.getPosition().x + 2)) {
                player.setLife(false);
                return false;
            }
            if(color == player.color) {
                if(Block.speed == Block.speed0) {
                    Block.speed += vel;
                    time = time0;
                }
                player.onFloor(pos.y + player.getBounds().getHeight()/2 + Constants.BLOCK_H/2 - 1);
            } else  if(pos.y < player.getPosition().y) {
                player.onFloor(pos.y + player.getBounds().getHeight()/2 + Constants.BLOCK_H/2 - 1);
            }
            player.flag = false;
        }
        return false;
    }

    @Override
    public void dispose() {
    }
}
