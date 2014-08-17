package com.nkpodolyan.weatherforecast;

import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

public class DtoForecast {

	@SerializedName("date")
	private String date;

	@SerializedName("precipMM")
	private float precipitation;

	@SerializedName("tempMaxC")
	private int tempMaxC;

	@SerializedName("tempMaxF")
	private int tempMaxF;

	@SerializedName("tempMinC")
	private int tempMinC;

	@SerializedName("tempMinF")
	private int tempMinF;

	@SerializedName("weatherCode")
	private int weatherCode;

	@SerializedName("weatherDesc")
	private List<Map<String, String>> weatherDescription;

	@SerializedName("weatherIconUrl")
	private List<Map<String, String>> icons;

	@SerializedName("winddir16Point")
	private String winddir16point;

	@SerializedName("winddirDegree")
	private int winddirDegree;

	@SerializedName("winddirection")
	private String winddirection;

	@SerializedName("windspeedKmph")
	private int windspeedKmph;

	@SerializedName("windspeedMiles")
	private int windspeedMiles;

	public List<Map<String, String>> getWeatherDescription() {
		return weatherDescription;
	}

	public void setWeatherDescription(List<Map<String, String>> weatherDescription) {
		this.weatherDescription = weatherDescription;
	}

	public List<Map<String, String>> getIcons() {
		return icons;
	}

	public void setIcons(List<Map<String, String>> icons) {
		this.icons = icons;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public float getPrecipitation() {
		return precipitation;
	}

	public void setPrecipitation(float precipitation) {
		this.precipitation = precipitation;
	}

	public int getTempMaxC() {
		return tempMaxC;
	}

	public void setTempMaxC(int tempMaxC) {
		this.tempMaxC = tempMaxC;
	}

	public int getTempMaxF() {
		return tempMaxF;
	}

	public void setTempMaxF(int tempMaxF) {
		this.tempMaxF = tempMaxF;
	}

	public int getTempMinC() {
		return tempMinC;
	}

	public void setTempMinC(int tempMinC) {
		this.tempMinC = tempMinC;
	}

	public int getTempMinF() {
		return tempMinF;
	}

	public void setTempMinF(int tempMinF) {
		this.tempMinF = tempMinF;
	}

	public int getWeatherCode() {
		return weatherCode;
	}

	public void setWeatherCode(int weatherCode) {
		this.weatherCode = weatherCode;
	}

	public String getWinddir16point() {
		return winddir16point;
	}

	public void setWinddir16point(String winddir16point) {
		this.winddir16point = winddir16point;
	}

	public int getWinddirDegree() {
		return winddirDegree;
	}

	public void setWinddirDegree(int winddirDegree) {
		this.winddirDegree = winddirDegree;
	}

	public String getWinddirection() {
		return winddirection;
	}

	public void setWinddirection(String winddirection) {
		this.winddirection = winddirection;
	}

	public int getWindspeedKmph() {
		return windspeedKmph;
	}

	public void setWindspeedKmph(int windspeedKmph) {
		this.windspeedKmph = windspeedKmph;
	}

	public int getWindspeedMiles() {
		return windspeedMiles;
	}

	public void setWindspeedMiles(int windspeedMiles) {
		this.windspeedMiles = windspeedMiles;
	}

	@Override
	public String toString() {
		return "DtoDayForecast [date=" + date + ", precipitation=" + precipitation + ", tempMaxC=" + tempMaxC
				+ ", tempMaxF=" + tempMaxF + ", tempMinC=" + tempMinC + ", tempMinF=" + tempMinF + ", weatherCode="
				+ weatherCode + ", weatherDescription=" + weatherDescription + ", icons=" + icons + ", winddir16point="
				+ winddir16point + ", winddirDegree=" + winddirDegree + ", winddirection=" + winddirection
				+ ", windspeedKmph=" + windspeedKmph + ", windspeedMiles=" + windspeedMiles + "]";
	}

}
