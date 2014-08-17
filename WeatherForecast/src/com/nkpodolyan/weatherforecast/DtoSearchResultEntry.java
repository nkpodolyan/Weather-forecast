package com.nkpodolyan.weatherforecast;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class DtoSearchResultEntry {

	@SerializedName("areaName")
	private List<DtoValue> areaName;

	@SerializedName("country")
	private List<DtoValue> country;

	@SerializedName("region")
	private List<DtoValue> region;

	@SerializedName("latitude")
	private double latitude;

	@SerializedName("longitude")
	private double longitude;

	@SerializedName("population")
	private int population;

	public List<DtoValue> getAreaName() {
		return areaName;
	}

	public void setAreaName(List<DtoValue> areaName) {
		this.areaName = areaName;
	}

	public List<DtoValue> getCountry() {
		return country;
	}

	public void setCountry(List<DtoValue> country) {
		this.country = country;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public int getPopulation() {
		return population;
	}

	public void setPopulation(int population) {
		this.population = population;
	}

	@Override
	public String toString() {
		return "DtoSearchResultEntry [areaName=" + areaName + ", country=" + country + ", region=" + region
				+ ", latitude=" + latitude + ", longitude=" + longitude + ", population=" + population + "]";
	}

}
