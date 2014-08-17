package com.nkpodolyan.weatherforecast;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;

public class AddCityActivity extends Activity implements OnItemClickListener {

	private ForecastModel forecastModel = ForecastModel.getInstance();

	private CitiesModel citiesModel = CitiesModel.getInstance();

	public static void start(Activity context) {
		Intent intent = new Intent(context, AddCityActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_city_activity);
		AutoCompleteTextView citiesAutocomplete = (AutoCompleteTextView) findViewById(R.id.autocomplete_tv);
		citiesAutocomplete.setAdapter(new CitiesAutoCompleteAdapter(this, android.R.layout.simple_list_item_1));
		citiesAutocomplete.setOnItemClickListener(this);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		String str = (String) adapterView.getItemAtPosition(position);
		citiesModel.addCity(str);
		citiesModel.setCurrentCity(str);
		finish();
	}

	private class CitiesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

		private ArrayList<String> resultList;

		public CitiesAutoCompleteAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
		}

		@Override
		public int getCount() {
			return resultList.size();
		}

		@Override
		public String getItem(int index) {
			return resultList.get(index);
		}

		@Override
		public Filter getFilter() {
			Filter filter = new Filter() {
				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					FilterResults filterResults = new FilterResults();
					if (constraint != null) {
						// Retrieve the autocomplete results.

						resultList = forecastModel.autoComplete(constraint.toString());

						// Assign the data to the FilterResults
						filterResults.values = resultList;
						filterResults.count = resultList.size();
					}
					return filterResults;
				}

				@Override
				protected void publishResults(CharSequence constraint, FilterResults results) {
					if (results != null && results.count > 0) {
						notifyDataSetChanged();
					} else {
						notifyDataSetInvalidated();
					}
				}
			};
			return filter;
		}
	}

}
