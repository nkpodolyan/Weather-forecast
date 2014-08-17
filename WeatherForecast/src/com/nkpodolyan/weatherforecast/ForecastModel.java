package com.nkpodolyan.weatherforecast;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.nkpodolyan.weatherforecast.CitiesModel.CitiesModelListener;

public class ForecastModel implements CitiesModelListener {

	private static final String LOG_TAG = ForecastModel.class.getSimpleName();

	private static String API_KEY;

	private static String API_URL;

	private final static ForecastModel instance = new ForecastModel();

	private RequestQueue requestQueue;

	private Request<String> currentForecastRequest;

	private ModelState modelState;

	private Set<ForecastModelListener> listeners = new HashSet<>();

	public static void setApiKeyAndUrl(Context context) {
		if (context != null) {
			PackageManager pm = context.getPackageManager();
			ApplicationInfo appInfo = null;
			try {
				appInfo = (ApplicationInfo) pm.getApplicationInfo(context.getPackageName(),
						PackageManager.GET_META_DATA);
				if (appInfo.metaData.containsKey("weather_api_key")) {
					ForecastModel.API_KEY = (String) appInfo.metaData.get("weather_api_key");
				} else {
					throw new IllegalArgumentException("Manifes application should contain weather_api_key metadata");
				}
				if (appInfo.metaData.containsKey("weather_api_url")) {
					ForecastModel.API_URL = (String) appInfo.metaData.get("weather_api_url");
				} else {
					throw new IllegalArgumentException("Manifes application should contain weather_api_url metadata");
				}
			} catch (NameNotFoundException e) {
				Log.e(LOG_TAG, "Could not get app info", e);
			}
		}
	}

	public static ForecastModel getInstance() {
		return instance;
	}

	private ForecastModel() {
		requestQueue = WeatherForecastApp.getInstance().getRequestQueue();
		modelState = new ModelState();
		modelState.modelState = ModelState.MODEL_STATE_IDLE;
	}

	public ModelState getModelState() {
		return modelState;
	}

	public boolean addListener(ForecastModelListener l) {
		return listeners.add(l);
	}

	public boolean removeListener(ForecastModelListener l) {
		return listeners.remove(l);
	}

	public void reloadForecast() {
		// cancel current request if executing
		if (currentForecastRequest != null) {
			currentForecastRequest.cancel();
			// change model state to idle
			modelState.modelState = ModelState.MODEL_STATE_IDLE;
		}
		// load forecast
		loadForecast();
	}

	@SuppressWarnings("unchecked")
	public boolean loadForecast() {
		if (modelState.modelState == ModelState.MODEL_STATE_LOADING) {
			return false;
		}
		if (!isNetworkAvailable()) {
			modelState.modelState = ModelState.MODEL_STATE_ERROR;
			modelState.e = new NoInternetException();
			notifyListeners();
			return true;
		}
		String currenCity = null;
		try {
			currenCity = URLEncoder.encode(CitiesModel.getInstance().getCurrentCity(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Log.e(LOG_TAG, "could not encode: " + CitiesModel.getInstance().getCurrentCity(), e);
		}
		// construct request
		StringBuilder requestBuilder = new StringBuilder();
		requestBuilder.append(API_URL).append("?");
		requestBuilder.append("q=").append(currenCity);
		requestBuilder.append("&format=json");
		requestBuilder.append("&num_of_days=5");
		// we do not need current location forecast
		requestBuilder.append("&cc=no");
		requestBuilder.append("&lang=").append(Locale.getDefault().getLanguage());
		requestBuilder.append("&key=").append(API_KEY);
		String requestUrl = requestBuilder.toString();
		RequestListener listener = new RequestListener();
		StringRequest request = new StringRequest(Request.Method.GET, requestUrl, listener, listener);
		request.setShouldCache(false);
		currentForecastRequest = requestQueue.add(request);
		modelState.modelState = ModelState.MODEL_STATE_LOADING;
		notifyListeners();
		return true;
	}

	private void notifyListeners() {
		for (ForecastModelListener l : listeners) {
			l.onModelStateChanged(modelState);
		}
	}

	private boolean isNetworkAvailable() {
		Context context = WeatherForecastApp.getInstance();
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}
		return haveConnectedWifi || haveConnectedMobile;
	}

	private class RequestListener implements Listener<String>, ErrorListener {

		@Override
		public void onErrorResponse(VolleyError error) {
			currentForecastRequest = null;
			Log.e(LOG_TAG, "caught error!", error);
			modelState.modelState = ModelState.MODEL_STATE_ERROR;
			modelState.e = error;
			notifyListeners();
		}

		@Override
		public void onResponse(String response) {
			DtoForecastResponse dtoResponse = null;
			currentForecastRequest = null;
			try {
				dtoResponse = new Gson().fromJson(response, DtoForecastResponse.class);
				modelState.modelState = ModelState.MODEL_STATE_IDLE;
				modelState.response = dtoResponse;
			} catch (Exception e) {
				Log.e(LOG_TAG, "Could not parse response", e);
				modelState.modelState = ModelState.MODEL_STATE_ERROR;
				modelState.e = new IOException("could not parse rsponse");
			}
			notifyListeners();
		}

	}

	public class ModelState {

		public static final int MODEL_STATE_LOADING = 0;

		public static final int MODEL_STATE_IDLE = 1;

		public static final int MODEL_STATE_ERROR = 2;

		public static final int ERROR_NO_ERROR = 0;

		public static final int ERROR_NO_INTERNET = 1;

		public static final int ERROR_NETWORK = 2;

		private int modelState;

		private DtoForecastResponse response;

		private Exception e;

		public int getModelState() {
			return modelState;
		}

		public DtoForecastResponse getResponse() {
			return response;
		}

		public int getErrorDescription() {
			if (modelState != MODEL_STATE_ERROR) {
				return ERROR_NO_ERROR;
			} else if (e instanceof NoInternetException) {
				return ERROR_NO_INTERNET;
			} else {
				return ERROR_NETWORK;
			}
		}

	}

	private class NoInternetException extends Exception {

		private static final long serialVersionUID = -931545350448843009L;

	}

	public interface ForecastModelListener {
		public void onModelStateChanged(ModelState modelState);
	}

	@Override
	public void onCurrentCityChanged(String currentCity) {
		reloadForecast();
	}

	@Override
	public void onCitiesListModofoed() {
		// ignore this call
	}

	public ArrayList<String> autoComplete(String input) {
		ArrayList<String> resultList = new ArrayList<>();

		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		try {
			StringBuilder requestBuilder = new StringBuilder();
			requestBuilder.append("https://api.worldweatheronline.com/free/v1/search.ashx").append("?");
			requestBuilder.append("q=").append(URLEncoder.encode(input, "UTF-8"));
			requestBuilder.append("&format=json");
			requestBuilder.append("&key=").append(API_KEY);
			URL url = new URL(requestBuilder.toString());
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());

			// Load the results into a StringBuilder
			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);
			}
		} catch (MalformedURLException e) {
			Log.e(LOG_TAG, "Error processing Places API URL", e);
			return resultList;
		} catch (IOException e) {
			Log.e(LOG_TAG, "Error connecting to Places API", e);
			return resultList;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		if (jsonResults.length() != 0) {
			try {
				DtoSearchResponse response = new Gson().fromJson(jsonResults.toString(), DtoSearchResponse.class);

				// Extract the Place descriptions from the results
				List<DtoSearchResultEntry> searchResult = response.getResult().getResult();
				resultList = new ArrayList<>(searchResult.size());
				for (DtoSearchResultEntry resultEntry : searchResult) {
					String result = resultEntry.getAreaName().get(0).getValue();
					result += ", " + resultEntry.getCountry().get(0).getValue();
					resultList.add(result);
				}
			} catch (Exception e) {
				Log.e(LOG_TAG, "Cannot process JSON results", e);
			}

		}
		return resultList;
	}
}
