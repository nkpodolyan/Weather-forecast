package com.nkpodolyan.weatherforecast;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ChooseCityDialog extends DialogFragment implements OnItemClickListener {

	private CitiesModel citiesModel = CitiesModel.getInstance();

	public static final String DIALOG_TAG = ChooseCityDialog.class.getSimpleName();

	public static void show(Activity context) {
		ChooseCityDialog chooseCityDialog = new ChooseCityDialog();
		chooseCityDialog.show(context.getFragmentManager(), DIALOG_TAG);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder dialogBuilder = new Builder(getActivity());
		dialogBuilder.setTitle(R.string.choose_city_dialog_title);
		dialogBuilder.setPositiveButton(R.string.choose_city_dialog_add, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				AddCityActivity.start(getActivity());
			}
		});
		dialogBuilder.setNegativeButton(R.string.choose_city_dialog_cancel, null);
		ListView citiesList = new ListView(getActivity());
		final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
				citiesModel.getCitiesList());
		citiesList.setAdapter(adapter);
		citiesList.setOnItemClickListener(this);
		SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(citiesList,
				new SwipeDismissListViewTouchListener.DismissCallbacks() {
					@Override
					public boolean canDismiss(int position) {
						return true;
					}

					@Override
					public void onDismiss(ListView listView, int[] reverseSortedPositions) {
						if (citiesModel.getCitiesList().size() == 1) {
							Toast.makeText(getActivity(), getString(R.string.error_can_not_delete_last_entry),
									Toast.LENGTH_SHORT).show();
							return;
						}
						for (int position : reverseSortedPositions) {
							String city = adapter.getItem(position);
							citiesModel.removeCity(city);
							adapter.remove(city);
							if (citiesModel.getCurrentCity().equals(city)) {
								// current displaying city was deleted, update current city to first available
								String availableCity = citiesModel.getCitiesList().get(0);
								citiesModel.setCurrentCity(availableCity);
							}
						}
						adapter.notifyDataSetChanged();
					}
				});
		citiesList.setOnTouchListener(touchListener);
		dialogBuilder.setView(citiesList);
		return dialogBuilder.create();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String city = (String) parent.getItemAtPosition(position);
		citiesModel.setCurrentCity(city);
		dismiss();
	}

}
