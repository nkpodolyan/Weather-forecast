package com.nkpodolyan.weatherforecast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;

public class CitiesModel {

	private static final String PREFS_NAME = CitiesModel.class.getSimpleName();

	private static final String CURRENT_CITY_KEY = CitiesModel.class.getSimpleName() + ".currentCity";

	private static final String CITIES_KEY = CitiesModel.class.getSimpleName() + ".citiesKey";

	private final static String DEFAULT_CITY = "Dublin";

	private SharedPreferences prefs;

	private Set<CitiesModelListener> listeners = new HashSet<>();

	private List<String> citiesList = new ArrayList<>();

	private String currentCity;

	private static final CitiesModel instance = new CitiesModel();

	private CitiesModel() {
		Context context = WeatherForecastApp.getInstance();
		prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		restoreState();
	}

	public static CitiesModel getInstance() {
		return instance;
	}

	public String getCurrentCity() {
		return currentCity;
	}

	public void setCurrentCity(String currentCity) {
		if (!currentCity.equals(this.currentCity)) {
			this.currentCity = currentCity;
			saveCurrentCity();
			notifyListenersCurrentCityChanged();
		}
	}

	public List<String> getCitiesList() {
		return citiesList;
	}

	public void addCity(String city) {
		if (!citiesList.contains(city)) {
			citiesList.add(city);
			Collections.sort(citiesList);
			saveCitiesList();
			notifyListenersCitiesListModified();
		}
	}

	public void removeCity(String city) {
		if (citiesList.remove(city)) {
			saveCitiesList();
			notifyListenersCitiesListModified();
		}
	}

	public boolean addListener(CitiesModelListener l) {
		return listeners.add(l);
	}

	public boolean removeListener(CitiesModelListener l) {
		return listeners.remove(l);
	}

	private void notifyListenersCurrentCityChanged() {
		for (CitiesModelListener l : listeners) {
			l.onCurrentCityChanged(currentCity);
		}
	}

	private void notifyListenersCitiesListModified() {
		for (CitiesModelListener l : listeners) {
			l.onCitiesListModofoed();
		}
	}

	private void restoreState() {
		currentCity = prefs.getString(CURRENT_CITY_KEY, DEFAULT_CITY);
		restoreCitiesList();
		if (citiesList.isEmpty()) {
			// first start, need to fill list with defaults
			citiesList.add("Dublin, Ireland");
			citiesList.add("London, United Kingdom");
			citiesList.add("New York, United States Of America");
			citiesList.add("Barcelona, Spain");
			Collections.sort(citiesList);
			saveCitiesList();
		}
	}

	private void saveCurrentCity() {
		prefs.edit().putString(CURRENT_CITY_KEY, currentCity).commit();
	}

	private boolean saveCitiesList() {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(CITIES_KEY + "_size", citiesList.size());

		for (int i = 0; i < citiesList.size(); i++) {
			editor.remove(CITIES_KEY + i);
			editor.putString(CITIES_KEY + i, citiesList.get(i));
		}

		return editor.commit();
	}

	private void restoreCitiesList() {
		int size = prefs.getInt(CITIES_KEY + "_size", 0);
		for (int i = 0; i < size; i++) {
			citiesList.add(prefs.getString(CITIES_KEY + i, null));
		}
	}

	public interface CitiesModelListener {

		void onCurrentCityChanged(String currentCity);

		void onCitiesListModofoed();
	}

}
