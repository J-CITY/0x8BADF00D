package com.runnergame.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.runnergame.game.GameRunner;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width =  GameRunner.WIDTH;
		config.height = GameRunner.HEIGHT;
		config.title =  GameRunner.TITLE;
		new LwjglApplication(new GameRunner(), config);
	}
}
