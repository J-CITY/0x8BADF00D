package com.runnergame.game;

public interface AnLauncher {
    void onBuyButtonClicked(int key);
    boolean getInfoBuyCoins(int key);
    void setInfoBuyCoins(int key, boolean b);
}
