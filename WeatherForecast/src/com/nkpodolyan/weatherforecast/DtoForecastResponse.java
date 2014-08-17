package com.nkpodolyan.weatherforecast;

import com.google.gson.annotations.SerializedName;

public class DtoForecastResponse {

	@SerializedName("data")
	private DtoForecastResponseData data;

	public DtoForecastResponseData getData() {
		return data;
	}

	public void setData(DtoForecastResponseData data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "DtoForecastResponse [data=" + data + "]";
	}

}
