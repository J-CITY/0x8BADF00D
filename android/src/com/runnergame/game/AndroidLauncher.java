package com.runnergame.game;

import android.app.AlertDialog;
import android.content.Intent;
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
import com.runnergame.game.util.IabHelper;
import com.runnergame.game.util.IabResult;
import com.runnergame.game.util.Inventory;
import com.runnergame.game.util.Purchase;

import java.util.ArrayList;
import java.util.List;

public class AndroidLauncher extends AndroidApplication implements AnLauncher {
	//SHOP
	// Метка для ведения логов
	static final String TAG = "Google BILLING";
	// Индефикаторы двух продуктов:
	//static final String SKU = "coins";
	static final String SKU = "android.test.purchased";
	static final String SKU1 = "android.test.purchased";
	static final String SKU2 = "android.test.purchased";
	static public boolean buy100coins = false;
	static public boolean buy500coins = false;
	static public boolean buy1000coins = false;
	// (произвольный) код для возвращения в приложение данных при покупке из приложения "Play Маркет"
	static final int RC_REQUEST = 10001;
	// Объект Хелпера для взаимодействия с биллингом.
	IabHelper mHelper;
	//END_SHOP

	final AndroidLauncher context = this;
	final AdMobImpl adMob;

	public AndroidLauncher() {
		adMob = new AdMobImpl("ca-app-pub-5313477092955900/1317887470");
	}

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// В переменной base64EncodedPublicKey должен быть указан открытый ключ RSA приложения вместо
		String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAg97P7QLl5OGDhY07AEcX2+ZUSC1J0csIfC0cMNNqdjwZiDCdQO8QBf5ZWF2HpL6KTZMmxFX60fmUIbS9hDYHsaouEkw7NBCgCxOaNdjF7oQIoONOK4cFL5jNOqqYKtpUJEEEiROLiQclYb5fVM0ZwjrS6tzfCmb2v/C4WoVlN6W2kMRzKtgQ1QUTdYijnYYvmv/1kiBNA/+NL00SBQEsUfGrDvXI/m5c1alT8WdJCKWEU8F3gWWZ+dV/W2zjiDiN/Pbo2GocVxYO6yXJwmRnE+7falpeyQh62eFNvMOLkBuAwNd58zi/jckYtkZYkhVEy+dGD84XOfY/nPKwoZi5gQIDAQAB";
		if (base64EncodedPublicKey.contains("CONSTRUCT_YOUR")) {
			throw new RuntimeException("Please put your app's public key in MainActivity.java. See README.");
		}
		// Создается новая копия объекта хелпера для взаимодействия с биллингом
		mHelper = new IabHelper(this, base64EncodedPublicKey);
		// Включается режим логирования в хелпере
		mHelper.enableDebugLogging(true);
		// Начинается настройка хелпера. Это ассинхронная процедура со своими слушателями,
		// которые будут вызванны по завешении получения данных.
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				Log.d(TAG, "Setup finished.");
				if (!result.isSuccess()) {
					// Произошла ошибка, сообщаем о ней пользователю и выходим из метода
					complain("Problem setting up in-app billing: " + result);
					return;
				}
				// Хелпер полностью инициализирован, получаем
				Log.d(TAG, "Setup successful. Querying inventory.");
				mHelper.queryInventoryAsync(mGotInventoryListener);
			}
		});
		//
		FlurryAgent.setLogEnabled(true);
		FlurryAgent.setLogLevel(Log.VERBOSE);
		FlurryAgent.init(this, "958ZTSCXWZTQ775CBHF3");


		adMob.init( context );

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useWakelock = true;
		config.useCompass = false;
		config.useAccelerometer = false;
		View gameView = initializeForView( new GameRunner(adMob, this), config );

		RelativeLayout layout = new RelativeLayout( this );
		layout.addView( gameView );
		layout.addView( adMob.adView, adMob.adParams );

		setContentView( layout );

		//initialize(new GameRunner(adMob), config);
	}
	// Вызывается по завершении выполнения запроса купленных продуктов в магазине.
// Создаем новый экземпляр класса  IabHelper.QueryInventoryFinishedListener и определяем в нем
// метод onQueryInventoryFinished
	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
			Log.d(TAG, "Query inventory finished.");
			if (result.isFailure()) {
				// Сообщаем пользователю о том, что не удалось получить список купленных продуктов в магазине
				complain("Failed to query inventory: " + result);
				// Выходим из метода
				return;
			}
			// Удачно получили список купленных товаров в магазине, начинаем разбор
			Log.d(TAG, "Query inventory was successful.");

			// проверяем, есть ли купленное топливо, если есть, то сразу заправляем его в бак
			// (расходуем, чтоб пользователь мог еще раз этот товар)
			if (inventory.hasPurchase(SKU)) {
				Log.d(TAG, "We have gas. Consuming it.");
				// Сообщаем магазину в асинхронном режиме, что товар израсходован.
				mHelper.consumeAsync(inventory.getPurchase(SKU), mConsumeFinishedListener);
				return;
			}
			if (inventory.hasPurchase(SKU1)) {
				Log.d(TAG, "We have gas. Consuming it.");
				// Сообщаем магазину в асинхронном режиме, что товар израсходован.
				mHelper.consumeAsync(inventory.getPurchase(SKU1), mConsumeFinishedListener);
				return;
			}
			if (inventory.hasPurchase(SKU2)) {
				Log.d(TAG, "We have gas. Consuming it.");
				// Сообщаем магазину в асинхронном режиме, что товар израсходован.
				mHelper.consumeAsync(inventory.getPurchase(SKU2), mConsumeFinishedListener);
				return;
			}
			// Если нет товаров, которые необходимо израсходовать, то обновляем интефейс программы.
			//updateUi();             // Отключаем заставку ожидания данных в программе.
			//setWaitScreen(false);
			Log.d(TAG, "Initial inventory query finished; enabling main UI.");
		}
	};
	// Вызывается при завершении  процедуры подтверждения расходования продукта магазином
	// Создаем новый экземпляр класса IabHelper.OnConsumeFinishedListener и определяем в нем
	// метод onConsumeFinished
	IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
		public void onConsumeFinished(Purchase purchase, IabResult result) {
			Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);
			// Мы знаем, что "gas" это единственный продукт потребляемый в нашем приложении,
			// но если в вашем приложении более одного потребляемого продукта,
			// то имеет смысл тут установить проверку на идентификатор продукта.
			//purchase.getSku();
			if (result.isSuccess()) {
				// Потребление продукта прошло успешно.
				// Активируем логику приложения и заправим наш танк.
				Log.d(TAG, "Consumption successful. Provisioning.");
				// Сохраняем данные о танке.
				saveData();
			} else {
				// Иначе сообщаем пользователю об ошибке
				complain("Error while consuming: " + result);
			}
			// Обновляем графический интерфейс приложения
			//updateUi();
			// Отключаем заставку ожидания данных в программе.
			//setWaitScreen(false);
			Log.d(TAG, "End consumption flow.");
		}
	};

	// Пользователь нажимает на кнопку "Buy 100 coins"
	public void onBuyButtonClicked(int key) {
		Log.d(TAG, "Buy button clicked.");
		// И через хелпер связываемся с магазином для покупки продукта
		switch (key) {
			case 0:
				mHelper.launchPurchaseFlow(this, SKU, RC_REQUEST, mPurchaseFinishedListener);
				break;
			case 1:
				mHelper.launchPurchaseFlow(this, SKU1, RC_REQUEST, mPurchaseFinishedListener);
				break;
			case 2:
				mHelper.launchPurchaseFlow(this, SKU2, RC_REQUEST, mPurchaseFinishedListener);
				break;
			default:
		}

	}
	// Вызывается при завершении  процедуры покупки продукта в магазине
	// Создаем новый экземпляр класса IabHelper.OnIabPurchaseFinishedListener и определяем в нем
	// метод onIabPurchaseFinished
	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);
			if (result.isFailure()) {
				// Произошла ошибка, сообщаем об этом пользователю
				complain("Error purchasing: " + result);
				//setWaitScreen(false);
				// Выходим из метода
				return;
			}
			Log.d(TAG, "Purchase successful.");
			// Проверяем, что же именно купил пользователь
			if (purchase.getSku().equals(SKU)) {
				Log.d(TAG, "Purchase is gas. Starting gas consumption.");
				// Ели это оказалось топливо, то запускаем метод потребления продукта
				mHelper.consumeAsync(purchase, mConsumeFinishedListener);
				buy100coins = true;
			} else if (purchase.getSku().equals(SKU1)) {
				Log.d(TAG, "Purchase is gas. Starting gas consumption.");
				// Ели это оказалось топливо, то запускаем метод потребления продукта
				mHelper.consumeAsync(purchase, mConsumeFinishedListener);
				buy500coins = true;
			} else if (purchase.getSku().equals(SKU2)) {
				Log.d(TAG, "Purchase is gas. Starting gas consumption.");
				// Ели это оказалось топливо, то запускаем метод потребления продукта
				mHelper.consumeAsync(purchase, mConsumeFinishedListener);
				buy1000coins = true;
			}
		}
	};
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
		// Отдаем переменные в хелпер
		if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
			// если переменные не для хелпера, то передаем их дальше
			super.onActivityResult(requestCode, resultCode, data);
		} else {
			Log.d(TAG, "onActivityResult handled by IABUtil.");
		}
		if(GameRunner.buyBtn100coins)
			mHelper.launchPurchaseFlow(this, SKU, RC_REQUEST, mPurchaseFinishedListener);
		else if(GameRunner.buyBtn500coins)
			mHelper.launchPurchaseFlow(this, SKU1, RC_REQUEST, mPurchaseFinishedListener);
		else if(GameRunner.buyBtn1000coins)
			mHelper.launchPurchaseFlow(this, SKU2, RC_REQUEST, mPurchaseFinishedListener);
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
		//SHOP
		Log.d(TAG, "Destroying helper.");
		if (mHelper != null)
			mHelper.dispose();
		mHelper = null;
		//SHOP_END

		//FlurryAgent.onEndSession(this);
		super.onDestroy();

	}

	void complain(String message) {
		Log.e(TAG, "**** TrivialDrive Error: " + message);
		alert("Error: " + message);
	}
	void alert(String message) {
		AlertDialog.Builder bld = new AlertDialog.Builder(this);
		bld.setMessage(message);
		bld.setNeutralButton("OK", null);
		Log.d(TAG, "Showing alert dialog: " + message);
		bld.create().show();
	}
	void saveData() {
		//SharedPreferences.Editor spe = getPreferences(MODE_PRIVATE).edit();
		//spe.putInt("tank", mTank);
		//spe.commit();
		Log.d(TAG, "Saved data:");
	}

	void loadData() {
		//SharedPreferences sp = getPreferences(MODE_PRIVATE);
		//mTank = sp.getInt("tank", 2);
		Log.d(TAG, "Loaded data:");
	}

	public boolean getInfoBuyCoins(int key) {
		if(key == 0)
			return buy100coins;
		else if(key == 1)
			return buy500coins;
		else if(key == 2)
			return buy1000coins;
		return false;
	}

	public void setInfoBuyCoins(int key, boolean b) {
		switch (key){
			case 0:
				buy100coins = b;
			break;
			case 1:
				buy500coins = b;
			break;
			case 2:
				buy1000coins = b;
			break;
		}
	}
}
