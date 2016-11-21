package com.runnergame.game.sprites.Blocks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.runnergame.game.Colors;
import com.runnergame.game.sprites.Animation;
import com.runnergame.game.sprites.Player;

public class BlockBoss extends Block {

    public int HP, HP0;
    Sprite hpBarSprite, hpSprite;
    public BlockBoss(float x, float y, int type, int hp) {
        super(x, type);
        HP = HP0 = hp;
        color = 0;
        animOn = new Animation(new TextureRegion(new Texture("blocks/boss.png")), 1, 1);
        animOff = new Animation(new TextureRegion(new Texture("blocks/boss.png")), 1, 1);
        sprite = animOn.getSprite();
        sprite.setCenter(x, y);
        bounds = new Rectangle(sprite.getBoundingRectangle());
        pos = new Vector2(x, y);

        hpBarSprite = new Sprite(new Texture("blocks/hpBar.png"));
        hpSprite = new Sprite(new Texture("blocks/hp.png"));
    }

    @Override
    public Vector2 getPos() {
        return pos;
    }


    @Override
    public void update(float delta, float _x) {
        animOn.update(delta);
        animOff.update(delta);
        if(pos.x > 450) {
            pos.add(-speed * delta, 0.0f);
        }
        bounds.setPosition(pos.x, pos.y);
        bounds.setCenter(pos.x, pos.y);
        if(HP <= 0) {
            pos.y -= 10;
            pos.add(-speed * delta, 0.0f);
            bounds.setCenter(pos.x, pos.y);

        }
        hpSprite.setScale((float)HP/(float)HP0, 1);
        hpBarSprite.setPosition(pos.x + 20, pos.y + 50);
        hpSprite.setPosition(pos.x + 22, pos.y + 52);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Sprite getHpSprite() {
        return hpSprite;
    }
    public Sprite getHpBarSprite() {
        return hpBarSprite;
    }

    @Override
    public boolean collide(Player player) {
        return false;
    }

    @Override
    public void dispose() {
    }
}
