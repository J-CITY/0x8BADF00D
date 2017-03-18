package com.runnergame.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.runnergame.game.GameRunner;

public class Bullet {
    public int RADIUS = GameRunner.WIDTH / 2 + 20;
    public int SPEED;
    public int COLOR;

    public boolean isLife = true;
    public int alpha;
    private Vector3 position;
    private Rectangle bounds;
    private Sprite sprite;

    private float X0, Y0;

    public Bullet(int speed, float _x, float _y, Texture tex) {
        alpha = MathUtils.random(0, 359);
        SPEED = speed;
        X0 = _x;
        Y0 = _y;

        COLOR = MathUtils.random(1, 2);

        position = new Vector3(0, 0, 0);
        position.x = (float) (_x + RADIUS * Math.sin(Math.toRadians(alpha)));
        position.y = (float) (_y + RADIUS * Math.cos(Math.toRadians(alpha)));
        sprite = new Sprite(tex);
        sprite.setCenter(position.x, position.y);
        sprite.setScale((float) MathUtils.random(2, 10) / 10);
        sprite.setRotation(180-alpha);
        if(COLOR == 1) sprite.setColor(GameRunner.colors.blue);
        if(COLOR == 2) sprite.setColor(GameRunner.colors.red);

        bounds = new Rectangle(sprite.getBoundingRectangle());
        bounds.setCenter(position.x, position.y);
    }

    public void update(float delta) {
        double distance = Math.sqrt((X0 - position.x)*(X0 - position.x) +
                (Y0 - position.y)*(Y0 - position.y));
        if (distance > 10) {
            position.x += 100 * delta * (X0 - position.x) / distance;
            position.y += 100 * delta * (Y0 - position.y) / distance;
        } else {
            isLife = false;
        }

        sprite.setCenter(position.x, position.y);
        bounds.setCenter(position.x, position.y);
    }

    public boolean collide(Rectangle player) {
        return player.overlaps(bounds);
    }

    public Vector3 getPosition() {
        return position;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void dispose() {
        sprite.getTexture().dispose();
    }
}
