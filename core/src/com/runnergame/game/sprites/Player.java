package com.runnergame.game.sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.runnergame.game.GameRunner;

public class Player {
    public int GRAVITY = -15;
    public int SCL = 1;
    private Vector3 position;
    private Vector3 velosity;
    private Rectangle bound;
    private Texture texture;
    private Sprite sprite;

    public boolean isLife = true;
    public int color = 1;//1- blue; 2- red
    public boolean on_floor = false;
    float floor = 163;


    public Player(int x, int y) {
        position = new Vector3(x, y, 0);
        velosity = new Vector3(0, 0, 0);
        texture = new Texture("Player.png");
        sprite = new Sprite(texture);
        sprite.setColor(GameRunner.colors.blue);

        bound = new Rectangle(sprite.getBoundingRectangle());
    }

    public Vector3 getPosition() {
        return position;
    }
    public void update(float delta) {
        SCL = 1;
        if(position.y > 0 && !on_floor) {
            velosity.add(0, GRAVITY*SCL, 0);
        }
        velosity.scl(delta);
        position.add(0, velosity.y, 0);
        if(on_floor) {
            position.y = floor;
        }

        velosity.scl(1/delta);
        if(color == 1) sprite.setColor(GameRunner.colors.blue);
        if(color == 2) sprite.setColor(GameRunner.colors.red);
        sprite.setCenter(position.x, position.y);
        bound.setPosition(position.x, position.y);
        bound.setCenter(position.x, position.y);
    }


    public Sprite getSprite() {
        return sprite;
    }


    public  void jump() {
        on_floor = false;
        velosity.y = 350;
    }

    public void dispose() {
        texture.dispose();
    }

    public Rectangle getBounds() {
        return bound;
    }

    public void setPositionX(float x) {
        position.x = x;
    }

    public void setPositionY(float y) {
        position.y = y;
    }

    public void setVelosity(float v) {
        velosity.y = v;
    }

    public void onFloor(float _y) {
        on_floor = true;
        floor = _y;
    }

    public void NCM(int __y) {
        position.y+=__y;
        sprite.setCenter(position.x, position.y);
        bound.setPosition(position.x, position.y);
        bound.setCenter(position.x, position.y);
    }

    public void changeColor() {
        color = color % 2 + 1;
    }
}
