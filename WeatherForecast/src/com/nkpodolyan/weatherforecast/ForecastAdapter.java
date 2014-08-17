package com.nkpodolyan.weatherforecast;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class ForecastAdapter extends BaseAdapter {

	private LayoutInflater inflater;

	private List<DtoForecast> data;

	private ImageLoader imageLoader;

	private Context context;

	public ForecastAdapter(Context context, List<DtoForecast> data) {
		this.context = context;
		imageLoader = WeatherForecastApp.getInstance().getImageLoader();
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.data = data;
	}

	public void setData(List<DtoForecast> data) {
		this.data = data;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return data == null ? 0 : data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.raw_day_forecast, parent, false);
			holder = new ViewHolder();
			holder.icon = (NetworkImageView) convertView.findViewById(R.id.forecast_icon);
			holder.temperature = (TextView) convertView.findViewById(R.id.temperature);
			holder.wind = (TextView) convertView.findViewById(R.id.wind);
			holder.date = (TextView) convertView.findViewById(R.id.date);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		DtoForecast forecast = data.get(position);

		Map<String, String> icons = forecast.getIcons().get(0);
		String iconKey = icons.keySet().iterator().next();
		String iconUrl = icons.get(iconKey);

		Map<String, String> descriptions = forecast.getWeatherDescription().get(0);
		String descKey = descriptions.keySet().iterator().next();
		String desc = descriptions.get(descKey);

		holder.date.setText(forecast.getDate() + " " + desc);
		holder.icon.setImageUrl(iconUrl, imageLoader);

		String temperatureString = forecast.getTempMinC() + "-" + forecast.getTempMaxC() + " C" + (char) 0x00B0;
		temperatureString += " (" + forecast.getTempMinF() + "-" + forecast.getTempMaxF() + " F" + ")";

		String windString = forecast.getWinddirection() + " " + forecast.getWindspeedKmph();
		windString += " " + context.getString(R.string.kmph);
		windString += " (" + forecast.getWindspeedMiles() + context.getString(R.string.mph) + ")";

		holder.temperature.setText(temperatureString);
		holder.wind.setText(windString);
		return convertView;
	}

	private class ViewHolder {
		private NetworkImageView icon;
		private TextView temperature;
		private TextView date;
		private TextView wind;
	}

}
