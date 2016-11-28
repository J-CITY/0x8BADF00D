package com.runnergame.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class Colors {
    public static int BLUE = 1;
    public static int RED = 2;
    public static int GRAY = 0;

    public static int colorScheme=0;

    public Color blue = new Color(57/255f, 225/255f, 205/255f, 1f);
    public Color red = new Color(237/255f, 53/255f, 106/255f, 1f);
    public Color green = new Color(72/255f, 210/255f, 57/255f, 1f);
    public Color gray = new Color(216/255f, 216/255f, 216/255f, 1f);
    public Color yellow = new Color(246/255f, 223/255f, 14/255f, 1f);


    public Array<String> playerSkins = new Array<String>();
    public Array<Integer> playerSkinsPrice = new Array<Integer>();
    Colors() {
        playerSkins.add("player_p.png");
        playerSkinsPrice.add(0);
        playerSkins.add("circle_p.png");
        playerSkinsPrice.add(10);
        playerSkins.add("circle2_p.png");
        playerSkinsPrice.add(15);
        playerSkins.add("3_p.png");
        playerSkinsPrice.add(15);
        playerSkins.add("5_p.png");
        playerSkinsPrice.add(15);
        playerSkins.add("6_p.png");
        playerSkinsPrice.add(15);
        playerSkins.add("7_p.png");
        playerSkinsPrice.add(15);
        playerSkins.add("8_p.png");
        playerSkinsPrice.add(15);
        playerSkins.add("9_p.png");
        playerSkinsPrice.add(15);
        playerSkins.add("10_p.png");
        playerSkinsPrice.add(15);
    }
    public void setScheme(int s) {
        switch (s) {
            case 0:
                blue = new Color(57/255f, 225/255f, 205/255f, 1f);
                red = new Color(237/255f, 53/255f, 106/255f, 1f);
                green = new Color(72/255f, 210/255f, 57/255f, 1f);
                gray = new Color(216/255f, 216/255f, 216/255f, 1f);
                yellow = new Color(246/255f, 223/255f, 14/255f, 1f);
                break;
            case 1:
                blue = new Color(89/255f, 109/255f, 161/255f, 1f);
                red = new Color(161/255f, 89/255f, 139/255f, 1f);
                green = new Color(89/255f, 175/255f, 86/255f, 1f);
                gray = new Color(58/255f, 58/255f, 58/255f, 1f);
                yellow = new Color(169/255f, 166/255f, 85/255f, 1f);
                break;
        }
    }




}
