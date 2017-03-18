package com.runnergame.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.runnergame.game.GameRunner;

public class Button {

    private Rectangle bounds;
    private Sprite spriteOn;
    private Sprite spriteOff;
    private Vector2 pos;
    private float scl = 1;
    private boolean isPress = false;
    float time = 2;

    public Button(String path, float x, float y) {
        spriteOn = new Sprite(new Texture(path+"_on.png"));
        spriteOff = new Sprite(new Texture(path+"_off.png"));
        spriteOn.setCenter(x, y);
        spriteOff.setCenter(x, y);
        bounds = new Rectangle();
        bounds.setSize(spriteOff.getWidth()+10, spriteOff.getHeight()+10);
        bounds.setCenter(x, y);
        pos = new Vector2(x, y);

    }
    public void updatePress(float x, float y) {
        if(collide(x, y) && isPress) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    time--;
                    if (time == 0) {
                        isPress = false;
                        time = 2;
                    }
                }
            }, 1);
        }
    }
    public boolean setPress(float x, float y) {
        if(collide(x, y)) {
            GameRunner.soundPressBtn.play(GameRunner.soundVol);
            isPress = true;
            return  true;
        }
        return false;
    }
    public void setIsPress(boolean b) {
        isPress = b;
    }
    public boolean getIsPress() {
        return isPress;
    }
    public void update(float delta) {

    }

    public Vector2 getPos() {
        return pos;
    }
    public void setPos(float x, float y) {
        pos.x = x;
        pos.y = y;
        bounds.setCenter(x, y);
        spriteOff.setCenter(x,y);
        spriteOn.setCenter(x,y);
    }
    boolean playSound = false;
    public boolean collide(float x, float y) {
        return bounds.contains(x, y);

    }

    public void setTexture(String s) {
        spriteOn = new Sprite(new Texture(s));
        spriteOff = new Sprite(new Texture(s));
        spriteOn.setCenter(pos.x, pos.y);
        spriteOff.setCenter(pos.x, pos.y);
        bounds = new Rectangle(spriteOn.getBoundingRectangle());
    }

    public Sprite getSprite() {
        if(isPress) {
            spriteOn.setScale(scl);
            return spriteOn;
        }
        spriteOff.setScale(scl);
        return spriteOff;
    }

    public void setScale(float _scl) {
        scl = _scl;
        bounds.setSize(spriteOff.getWidth()*scl+10, spriteOff.getHeight()*scl+10);
        bounds.setCenter(pos.x, pos.y);
        /// /sprite.setScale(scl);
    }

    public Rectangle getBounds() {
        return bounds;
    }


    public void dispose() {
        spriteOff.getTexture().dispose();
        spriteOn.getTexture().dispose();
    }
}
