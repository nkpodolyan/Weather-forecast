package com.nkpodolyan.weatherforecast;

import com.google.gson.annotations.SerializedName;

public class DtoValue {

	@SerializedName("value")
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "DtoValue [value=" + value + "]";
	}

}
