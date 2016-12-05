package com.runnergame.game;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.flurry.android.FlurryAgent;
import com.runnergame.game.GameRunner;
import com.runnergame.game.states.DataManager;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FlurryAgent.setLogEnabled(true);
		FlurryAgent.setLogLevel(Log.VERBOSE);
		FlurryAgent.init(this, "958ZTSCXWZTQ775CBHF3");
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useCompass = false;
		config.useAccelerometer = false;
		initialize(new GameRunner(), config);
	}
	@Override
	protected void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(this, "958ZTSCXWZTQ775CBHF3");
		//FlurryAgent.logEvent("SESSION_LENGTH", true);
	}

	@Override
	protected void onStop() {
		super.onStop();
		//FlurryAgent.endTimedEvent("SESSION_LENGTH");
		//Gdx.app.debug("TAGGG", "SESSION___RUN");
		FlurryAgent.onEndSession(this);
	}


	@Override
	public void onDestroy()
	{
		//FlurryAgent.onEndSession(this);
		super.onDestroy();

	}
}
