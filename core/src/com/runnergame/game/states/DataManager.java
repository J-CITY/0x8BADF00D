package com.runnergame.game.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class DataManager {
    Preferences prefs;
    String param = "";

    public DataManager(String name) {
        prefs = Gdx.app.getPreferences(name);
    }

    public void setParam(String p) {
        param = p;
    }

    public void addData(int _c) {
        prefs.putInteger(param, _c);
        prefs.flush();
    }

    public void plusData(int _c) {
        int c = prefs.getInteger(param);
        c += _c;
        prefs.putInteger(param, c);
        prefs.flush();
    }

    public void addData2(String par, int c) {
        prefs.putInteger(par, c);
        prefs.flush();
    }

    public void addDataTime(String par, long c) {
        prefs.putLong(par, c);
        prefs.flush();
    }
    public long loadDataTime(String str) {
        return prefs.getLong(str, 0);
    }

    public int load() {
        return prefs.getInteger(param, 0);
    }
    public int load2(String str) {
        return prefs.getInteger(str, 0);
    }
}
