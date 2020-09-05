package fr.univamu.webdesdonnees.weather.model;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class Measure implements Serializable {

	private static final long serialVersionUID = 1L;

	private String unit;
	private ZonedDateTime time;
	private double value;
	private String id;

	public Measure(String unit, ZonedDateTime time, double value, String id) {
		this.unit = unit;
		this.time = time;
		this.value = value;
		this.id = id;
	}

	public Measure() {
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public ZonedDateTime getTime() {
		return time;
	}

	public void setTime(ZonedDateTime time) {
		this.time = time;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
