package com.runnergame.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

public class Colors {
    public static int BLUE = 1;
    public static int RED = 2;
    public static int GRAY = 0;

    public static Color blue = new Color(0.32f, 0.47f, 0.57f, 1f);
    public static Color red = new Color(0.69f, 0.29f, 0.29f, 1f);
    public static Color green = new Color(0.25f, 0.58f, 0.4f, 1f);
    public static Color gray = new Color(0.43f, 0.43f, 0.43f, 1f);


    public static Color purpleBg = new Color(38/255f, 31/255f, 39/255f, 1f);
    public static Color purple2Bg = new Color(75/255f, 58/255f, 66/255f, 1f);
    public static Color yellowBg = new Color(0.8f, 0.6f, 0.2f, 1);
    //public static Color yellow2Bg = new Color(243/255f, 230/255f, 122/255f, 1);
    public static Color grayBg = new Color(41/255f, 41/255f, 41/255f, 1);
    public static Color orangeBg = new Color(212/255f, 165/255f, 38/255f, 1);

    public Color getBgColor() {
        int c = MathUtils.random(0, 4);
        switch (c) {
            case 0:
                return purpleBg;
            case 1:
                return purple2Bg;
            case 2:
                return yellowBg;
            case 3:
                return grayBg;
            case 4:
                return orangeBg;
        }
        return grayBg;
    }

}
