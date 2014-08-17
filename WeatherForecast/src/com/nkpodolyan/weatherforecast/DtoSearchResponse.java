package com.nkpodolyan.weatherforecast;

import com.google.gson.annotations.SerializedName;

public class DtoSearchResponse {

	@SerializedName("search_api")
	private DtoSearchResult result;

	public DtoSearchResult getResult() {
		return result;
	}

	public void setResult(DtoSearchResult result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "DtoSearchResponse [result=" + result + "]";
	}

}
