package com.runnergame.game;

import com.badlogic.gdx.utils.Array;

/**
 * Created by 333da on 28.11.2016.
 */
public class Level {
    public String level;
    public Float level_speed;
    public Integer level_colors;
    public Float level_rot;
    public Float level_rot_speed;
    public Integer color_scheme;

    public Level(String l, float ls, int lc, float lr, float lrs, int cs) {
        level = l;
        level_speed = ls;
        level_colors = lc;
        level_rot = lr;
        level_rot_speed = lrs;
        color_scheme = cs;
    }
}
