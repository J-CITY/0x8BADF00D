package com.runnergame.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.runnergame.game.states.CoinsManager;
import com.runnergame.game.states.DataManager;
import com.runnergame.game.states.GameStateManager;
import com.runnergame.game.states.MenuState;
import com.runnergame.game.states.MoonCityState;
import com.runnergame.game.states.RecordManager;

public class GameRunner extends ApplicationAdapter {
	public static final int WIDTH = 1366;
	public static final int HEIGHT = 768;
	public static final String TITLE = "0x8BADF00D";

	private GameStateManager gsm;
	private SpriteBatch batch;

	private Music music;
	public static boolean isPlay = true;
	public  static BitmapFont font;

	//public static RecordManager rm;
	//public static CoinsManager cm;
	public static DataManager dm;
	public static int score = 0;
	public static int new_coins = 0;
	public static int new_stars = 0;
	public static boolean reborn = false;

	public static Colors colors = new Colors();
	public static Levels levels = new Levels();;

	@Override
	public void create () {
		batch = new SpriteBatch();
		//rm = new RecordManager("GameRunner");
		//cm = new CoinsManager("GameRunner");
		dm = new DataManager("GameRunner");

		/*DataManager dm = new DataManager("GameRunner");
		for(int i=0; i<16; ++i) {
			dm.setParam("Unit" + i);
			dm.addData(0);
		}*/
		//dm.addData2("level", 10);
		dm.setParam("coins");
		//					dm.addData(250);
		new_coins = dm.load();
		dm.setParam("star");
		//dm.addData(250);
		new_stars = dm.load();


		gsm = new GameStateManager();
		music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
		music.setLooping(true);
		music.setVolume(0.1f);
		music.play();
		Gdx.gl.glClearColor(1, 0, 0, 1);

		font = new BitmapFont();
		FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font2.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 32;
		font = fontGenerator.generateFont(parameter);
		fontGenerator.dispose();

		gsm.push(new MenuState(gsm));
		//gsm.push(new MoonCityState(gsm));
	}

	float H = 0, S=80, V=20;
	float dir = 1;
	int inc=0, next = 1;
	float Vmin, Vinc, Vdec;

	public static float R, G, B;

	@Override
	public void render () {
		next = 0;
		if(inc == 3) {
			inc = 0;
			if(H == 0)
				dir = 1;
			if(H == 360)
				dir = -1;
			H += dir;
			Vmin = (100 - S) * V / 100;
			float a = (V - Vmin) * (H % 60) / 60;
			Vinc = Vmin + a;
			Vdec = V - a;
		}
		if(H < 60) {
			R = V * 1 / 255; G = Vinc * 1 / 255; B = Vmin * 1 / 255;
			//Gdx.gl.glClearColor( R, G, B, 1 );
			//System.out.print("R: " + V * 1 / 255 + "G: " + Vinc * 1 / 255 + "B: " + Vmin * 1 / 255 + "\n");
		}
		if(H >= 60 && H < 120) {
			R = Vdec * 1 / 255; G = V * 1 / 255; B = Vmin * 1 / 255;
			//Gdx.gl.glClearColor( R, G, B, 1 );
			//System.out.print("R: " +  Vdec * 1 / 255 + "G: " + V * 1 / 255 + "B: " + Vmin * 1 / 255 + "\n");
		}
		if(H >= 120 && H < 180) {
			R = Vmin * 1 / 255; G = V * 1 / 255; B = Vinc * 1 / 255;
			//Gdx.gl.glClearColor( R, G, B, 1 );
			//System.out.print("R: " + Vmin * 1 / 255 + "G: " + V * 1 / 255 + "B: " + Vinc * 1 / 255 + "\n");
		}
		if(H >= 180 && H < 240) {
			R = Vmin * 1 / 255; G = Vdec * 1 / 255; B = V * 1 / 255;
			//Gdx.gl.glClearColor( R, G, B, 1 );
			//System.out.print("R: " + Vmin * 1 / 255 + "G: " + Vdec * 1 / 255 + "B: " + V * 1 / 255 + "\n");
		}
		if(H >= 240 && H < 300) {
			R = Vinc * 1 / 255; G = Vmin * 1 / 255; B = V * 1 / 255;
			//Gdx.gl.glClearColor( R, G, B, 1 );
			//System.out.print("R: " + Vinc * 1 / 255 + "G: " + Vmin * 1 / 255 + "B: " + V * 1 / 255 + "\n");
		}
		if(H >= 300 && H < 360) {
			R = V * 1 / 255; G = Vmin * 1 / 255; B = Vdec * 1 / 255;
			//Gdx.gl.glClearColor( R, G, B, 1 );
			//System.out.print("R: " + V * 1 / 255 + "G: " + Vmin * 1 / 255 + "B: " + Vdec * 1 / 255 + "\n");
		}
		inc++;
		//Gdx.gl.glClearColor( R, G, B, 1 );
		//Gdx.gl.glClearColor( 245/255f, 232/255f, 101/255f, 1 );
		//Gdx.gl.glClearColor( 204/255f, 204/255f, 204/255f, 1 );
		//Gdx.gl.glClearColor( 205/255f, 153/255f, 52/255f, 1 );
		Gdx.gl.glClearColor(41/255f, 41/255f, 41/255f, 1);

		if(music.isPlaying() && !isPlay) {
			music.stop();
		} else if(!music.isPlaying() && isPlay) {
			music.play();
		}
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		music.dispose();
	}
}
