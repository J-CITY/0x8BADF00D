package com.runnergame.game;

public class MetaLevel {
    public String discription;
    public String tex;
    public String param;
    public int lvl;
    public int status;
    public int prize;
    public int price;

    MetaLevel(String d, int _lvl, int _prize, String t, int p, String _param) {
        discription = d;
        lvl = _lvl;
        prize = _prize;
        tex = t;
        price = p;
        param = _param;
    }
}
