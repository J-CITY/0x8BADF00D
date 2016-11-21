package com.runnergame.game.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;


public abstract class State {
    protected float speed = 600;
    protected OrthographicCamera camera;
    protected Vector3 projection;
    protected GameStateManager gameStateMenager;//будет управлять окнами игры

    public State(GameStateManager gameStateMenager) {
        this.gameStateMenager = gameStateMenager;
        camera = new OrthographicCamera();
        projection = new Vector3();
    }

    protected  abstract void hendleInput();
    public abstract void update(float delta);
    public abstract void render(SpriteBatch sb);
    public abstract void dispose();
}
