package com.nkpodolyan.weatherforecast;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class DtoSearchResult {

	@SerializedName("result")
	private List<DtoSearchResultEntry> result;

	public List<DtoSearchResultEntry> getResult() {
		return result;
	}

	public void setResult(List<DtoSearchResultEntry> result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "DtoSearchResult [result=" + result + "]";
	}

}
