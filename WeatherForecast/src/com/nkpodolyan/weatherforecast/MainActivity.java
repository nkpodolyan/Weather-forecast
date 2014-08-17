package com.nkpodolyan.weatherforecast;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nkpodolyan.weatherforecast.CitiesModel.CitiesModelListener;
import com.nkpodolyan.weatherforecast.ForecastModel.ForecastModelListener;
import com.nkpodolyan.weatherforecast.ForecastModel.ModelState;

public class MainActivity extends Activity implements ForecastModelListener, CitiesModelListener {

	private ListView forecastList;

	private TextView errorDesc;

	private ForecastAdapter adapter;

	private ProgressBar forecastProgress;

	private ForecastModel forecastModel = ForecastModel.getInstance();

	private CitiesModel citiesModel = CitiesModel.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		forecastList = (ListView) findViewById(R.id.forecast_list);
		forecastProgress = (ProgressBar) findViewById(R.id.forecast_progress);
		errorDesc = (TextView) findViewById(R.id.error_desc);
		forecastModel.addListener(this);
		citiesModel.addListener(this);
		adapter = new ForecastAdapter(this, null);
		adjustUiByForecastModelState(forecastModel.getModelState());
		getActionBar().setTitle(citiesModel.getCurrentCity());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		forecastModel.removeListener(this);
		citiesModel.removeListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.action_change_city:
			ChooseCityDialog.show(this);
			return true;
		case R.id.action_refresh:
			forecastModel.loadForecast();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void adjustUiByForecastModelState(ModelState modelState) {
		boolean isLoading = modelState.getModelState() == ModelState.MODEL_STATE_LOADING;
		boolean isError = modelState.getModelState() == ModelState.MODEL_STATE_ERROR;
		forecastProgress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
		forecastList.setVisibility(isLoading && !isError ? View.GONE : View.VISIBLE);
		errorDesc.setVisibility(isError ? View.VISIBLE : View.GONE);
		DtoForecastResponse response;
		if (!isLoading && !isError) {
			if ((response = modelState.getResponse()) != null) {
				adapter.setData(response.getData().getWeatherForecast());
				forecastList.setAdapter(adapter);
			}
		} else if (isError) {
			switch (modelState.getErrorDescription()) {
			case ModelState.ERROR_NETWORK:
				errorDesc.setText(R.string.error_network);
				break;
			case ModelState.ERROR_NO_INTERNET:
				errorDesc.setText(R.string.error_no_internet);
				break;
			default:
				errorDesc.setVisibility(View.GONE);
				break;
			}
		}
	}

	@Override
	public void onModelStateChanged(ModelState modelState) {
		adjustUiByForecastModelState(modelState);
	}

	@Override
	public void onCurrentCityChanged(String currentCity) {
		getActionBar().setTitle(citiesModel.getCurrentCity());
	}

	@Override
	public void onCitiesListModofoed() {
		// ignore this callback
	}

}
