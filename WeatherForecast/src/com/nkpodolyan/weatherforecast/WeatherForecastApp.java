package com.nkpodolyan.weatherforecast;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class WeatherForecastApp extends Application {

	private static WeatherForecastApp instance;

	private String weatherForecastApiKey;

	private RequestQueue requestQueue;

	private ImageLoader imageLoader;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;

		requestQueue = Volley.newRequestQueue(getApplicationContext());
		imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
			private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);

			public void putBitmap(String url, Bitmap bitmap) {
				mCache.put(url, bitmap);
			}

			public Bitmap getBitmap(String url) {
				return mCache.get(url);
			}
		});
		ForecastModel.setApiKeyAndUrl(getApplicationContext());
		ForecastModel forecastModel = ForecastModel.getInstance();
		forecastModel.loadForecast();
		CitiesModel citiesModel = CitiesModel.getInstance();
		citiesModel.addListener(forecastModel);
	}

	public static WeatherForecastApp getInstance() {
		return instance;
	}

	public RequestQueue getRequestQueue() {
		return requestQueue;
	}

	public ImageLoader getImageLoader() {
		return imageLoader;
	}

}
