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
    public float sprite_scl = 0.5f;
    public float sprite_rot = 0;
    private Vector3 position;
    private Vector3 velosity;
    private Rectangle bound;

    private Animation anim;
    private Sprite sprite;

    public boolean isLife = true;
    public int colorSize = 2;
    public int color = 1;//1- blue; 2- red
    public boolean on_floor = false;
    public boolean flag;
    float floor = 163;

    public boolean gun = false;

    public float getRot() {
        return sprite_rot;
    }

    public Player(int x, int y, String path, int fc) {
        position = new Vector3(x, y, 0);
        velosity = new Vector3(0, 0, 0);
        int _path = GameRunner.dm.load2("playerSkin");
        path = GameRunner.colors.playerSkins.get(_path);

        anim = new Animation(new TextureRegion(new Texture(path)), fc, 1);
        sprite = anim.getSprite();
        sprite.setCenter(x, y);
        bound = new Rectangle(sprite.getBoundingRectangle());
        sprite.setColor(GameRunner.colors.blue);
    }

    public Vector3 getPosition() {
        return position;
    }
    public void animUpdate(float delta) {
        anim.update(delta);
    }
    public void update(float delta) {
        SCL = 1;
        if(position.y > 0 && !on_floor) {
            velosity.add(0, GRAVITY*SCL, 0);
        }
        velosity.scl(delta);
        //System.out.print("!!!" + delta + " " + velosity.y + "\n");
        //System.out.print(velosity.y+"\n");
        if(velosity.y < -8) velosity.y = -8;

        position.add(0, velosity.y, 0);
        if(on_floor) {
            position.y = floor;
        }

        velosity.scl(1/delta);

        //sprite_rot -= (180*delta)%360;
    }

    public void setScale(float scl) {
        sprite_scl = scl;
    }

    public Sprite getSprite() {
        sprite = anim.getSprite();
        sprite.setCenter(position.x, position.y);
        sprite.setScale(sprite_scl);
        //sprite.setRotation(sprite_rot);
        if(color == 1)
            sprite.setColor(GameRunner.colors.blue);
        else if(color == 2)
            sprite.setColor(GameRunner.colors.red);
        else if(color == 3)
            sprite.setColor(GameRunner.colors.green);
        bound = sprite.getBoundingRectangle();
        return sprite;
    }


    public  void jump(float vel) {
        on_floor = false;
        velosity.y = vel;
    }

    public void dispose() {
        sprite.getTexture().dispose();
        anim.dispouse();
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

    public void setLife(boolean b) {
        isLife = b;
    }
    public boolean getLife() {
        return isLife;
    }

    public void onFloor(float _y) {
        if(velosity.y <=0) {
            on_floor = true;
            floor = _y;
        }
    }

    public void NCM(int __y) {
        position.y+=__y;
        //bound.setPosition(position.x, position.y);
        //bound.setCenter(position.x, position.y);
    }

    public void changeColor() {
        color = color % colorSize + 1;
    }

    public void setVelosity(float x, float y) {
        velosity.x = x;
        velosity.y = y;
    }
}
