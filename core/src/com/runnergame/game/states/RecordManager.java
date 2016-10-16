package com.runnergame.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class RecordManager {

    Preferences prefs;

    public RecordManager(String name) {
        prefs = Gdx.app.getPreferences(name);
    }

    public void save(int record) {
        if(prefs.getInteger("record") < record) {
            prefs.putInteger("record", record);
            prefs.flush();
        }
    }

    public int load() {
        return prefs.getInteger("record");
    }
}
