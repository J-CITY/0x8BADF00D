package com.runnergame.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class CoinsManager {

        Preferences prefs;

        public CoinsManager(String name) {
            prefs = Gdx.app.getPreferences(name);
        }

        public void addCoins(int _c) {
            //int c = prefs.getInteger("coins");
            //c += _c;
            prefs.putInteger("coins", _c);
            prefs.flush();
        }

        public void setCoins(int _c) {
            prefs.putInteger("coins", _c);
            prefs.flush();
        }

        public int load() {
            return prefs.getInteger("coins");
        }


}
