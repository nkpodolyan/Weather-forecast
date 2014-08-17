package com.nkpodolyan.weatherforecast;

import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

public class DtoForecastResponseData {

	@SerializedName("request")
	private List<Map<String, String>> request;

	@SerializedName("weather")
	private List<DtoForecast> weatherForecast;

	public List<DtoForecast> getWeatherForecast() {
		return weatherForecast;
	}

	public void setWeatherForecast(List<DtoForecast> weatherForecast) {
		this.weatherForecast = weatherForecast;
	}

	@Override
	public String toString() {
		return "DtoForecastResponseData [request=" + request + ", weatherForecast=" + weatherForecast + "]";
	}

}
