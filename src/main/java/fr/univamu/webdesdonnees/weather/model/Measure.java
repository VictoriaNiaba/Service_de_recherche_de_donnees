package fr.univamu.webdesdonnees.weather.model;

import java.io.Serializable;
import java.util.Date;

public class Measure implements Serializable {

	private static final long serialVersionUID = 1L;

	private String unit;
	private Date time;
	private double value;

	public Measure(String unit, Date time, double value) {
		this.unit = unit;
		this.time = time;
		this.value = value;
	}

	public Measure() {
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

}
