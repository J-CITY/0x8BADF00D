package com.runnergame.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.XmlReader;
import com.flurry.android.FlurryAgent;
import com.runnergame.game.sprites.Background;
import com.runnergame.game.sprites.Coin;
import com.runnergame.game.states.BonusState;
import com.runnergame.game.states.DataManager;
import com.runnergame.game.states.GameStateManager;
import com.runnergame.game.states.MenuState;
import java.util.HashMap;


public class GameRunner implements ApplicationListener {
	final String FONT_CHARS = "абвгдежзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyzАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";
	public static final int WIDTH = 1366;
	public static final int HEIGHT = 768;
	public static final String TITLE = "0x8BADF00D";

	private GameStateManager gsm;
	private SpriteBatch batch;

	private Music music;
	public static Sound soundPressBtn;
	public static float soundVol = 0.5f;
	public static boolean isPlay = true;
	public static boolean updateMusic = false;
	public static int playMusic=0;
	public  static BitmapFont font;

	public static DataManager dm;
	public static int score = 0;
	public static int now_coins = 0;
	public static int now_metal = 0;
	public static boolean reborn = false;

	public static Colors colors = new Colors();
	public static Levels levels = new Levels();
	int firstRun=0;
	public long timeStart, timeStop;

	public static boolean adMobFlag = false;
	final AdMob adMob;

	float width;
	float height;

	private AnLauncher context;

	public GameRunner(AdMob adMob, AnLauncher context) {
		this.adMob = adMob;
		this.context = context;
	}

	//SHOP
	public static boolean buyBtn100coins = false;
	public static boolean buyBtn500coins = false;
	public static boolean buyBtn1000coins = false;
	//SHOP_END

	@Override
	public void create () {
		dm = new DataManager("GameRunner");

		//GameRunner.dm.addData2("helperMetaLevel", 0);
		timeStart = System.currentTimeMillis();
		//dm.addData2("firstRun", 0);
		firstRun = dm.load2("firstRun");
		if(firstRun == 0) {
			FlurryAgent.logEvent("FIRST_RUN", true);
		} else {
			FlurryAgent.logEvent("SESSION_LENGTH", true);
		}
		/////////////////////////////////////////////
		batch = new SpriteBatch();
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setCatchMenuKey(true);

		/*DataManager dm = new DataManager("GameRunner");
		for(int i=0; i<16; ++i) {
			dm.setParam("Unit" + i);
			dm.addData(0);
		}*/
		//dm.addData2("level", 10);
		//					dm.addData2("coins",500);
		now_coins = dm.load2("coins");
		now_metal = dm.load2("metal");


		gsm = new GameStateManager();
		music = Gdx.audio.newMusic(Gdx.files.internal(Constants.Music+0+".mp3"));
		music.setLooping(true);
		music.setVolume(0.1f);
		music.play();

		soundPressBtn = Gdx.audio.newSound(Gdx.files.internal("soundPressBtn.wav"));
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();

		Gdx.gl.glClearColor(1, 0, 0, 1);

		font = new BitmapFont();
		FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.characters = FONT_CHARS;
		parameter.size = 32;
		font = fontGenerator.generateFont(parameter);
		fontGenerator.dispose();
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		gsm.push(new MenuState(gsm));
	}

	@Override
	public void resize(int width, int height) {

	}
	int TIME = 5;
	int time = TIME;
	@Override
	public void render () {
		com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
			@Override
			public void run() {
				time--;
				if (time == 0) {
					time = TIME;
					GameRunner.now_coins = GameRunner.dm.load2("coins");
					GameRunner.now_metal = GameRunner.dm.load2("metal");
				}
			}
		}, 1);

		//Gdx.gl.glClearColor( 245/255f, 232/255f, 101/255f, 1 );
		//Gdx.gl.glClearColor( 204/255f, 204/255f, 204/255f, 1 );
		//Gdx.gl.glClearColor( 205/255f, 153/255f, 52/255f, 1 );
		Gdx.gl.glClearColor(41/255f, 41/255f, 41/255f, 1);
		if(updateMusic) {
			updateMusic = false;
			music.stop();
			music.dispose();
			music = Gdx.audio.newMusic(Gdx.files.internal(Constants.Music+playMusic+".mp3"));
			music.setLooping(true);
			music.setVolume(0.1f);
			music.play();
		}
		if(music.isPlaying() && !isPlay) {
			music.stop();
		} else if(!music.isPlaying() && isPlay) {
			music.play();
		}
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(buyBtn100coins) {
			buyBtn100coins = false;
			context.onBuyButtonClicked(0);
		}
		if(buyBtn500coins) {
			buyBtn500coins = false;
			context.onBuyButtonClicked(1);
		}
		if(buyBtn1000coins) {
			buyBtn1000coins = false;
			context.onBuyButtonClicked(2);
		}
		if(context.getInfoBuyCoins(0)) {
			context.setInfoBuyCoins(0,false);
			dm.addData2("coins", dm.load2("coins") + 100);
		}
		if(context.getInfoBuyCoins(1)) {
			context.setInfoBuyCoins(1,false);
			dm.addData2("coins", dm.load2("coins") + 500);
		}
		if(context.getInfoBuyCoins(2)) {
			context.setInfoBuyCoins(2,false);
			dm.addData2("coins", dm.load2("coins") + 1000);
		}
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);

		boolean escapePressed = Gdx.input.isKeyPressed( Input.Keys.ESCAPE );
		boolean backPressed   = Gdx.input.isKeyPressed( Input.Keys.BACK );
		if ( escapePressed || backPressed ) {
			Gdx.app.exit();
		}
		if(adMobFlag)
			adMob.show();
		else
			adMob.hide();
		//if ( Gdx.input.justTouched() ) {
		//	float x = Gdx.input.getX();
		//	float y = Gdx.input.getY();
		//adMob.show();
		//}
	}

	private String getSessionTIme(long t) {
		String seconds = new String();
		if(t <= 30)
			seconds = "0-30 sec";
		if(t > 30 && t <= 60)
			seconds = "30-60 sec";
		if(t > 60 && t <= 300)
			seconds = "1-5 min";
		if(t > 300 && t <= 600)
			seconds = "5-10 min";
		if(t > 600 && t <= 900)
			seconds = "10-15 min";
		if(t > 900 && t <= 1800)
			seconds = "15-30 min";
		if(t > 1800 && t <= 3600)
			seconds = "30-60 min";
		if(t > 3600)
			seconds = " > 60 min";
		return seconds;
	}

	@Override
	public void pause() {
		timeStop = System.currentTimeMillis();
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("TIME", getSessionTIme((timeStop-timeStart)/1000));
		if(firstRun == 0) {
			FlurryAgent.endTimedEvent("FIRST_RUN", parameters);
			Gdx.app.debug("TAGGG", "FIRST___RUN "+parameters.get("TIME"));
			dm.addData2("firstRun", 1);
		} else {
			FlurryAgent.endTimedEvent("SESSION_LENGTH", parameters);
			Gdx.app.debug("TAGGG", "SESSION___RUN"+parameters.get("TIME"));
		}
	}

	@Override
	public void resume() {
		timeStart = System.currentTimeMillis();
	}


	@Override
	public void dispose () {
		batch.dispose();
		music.dispose();
		soundPressBtn.dispose();
	}
}
