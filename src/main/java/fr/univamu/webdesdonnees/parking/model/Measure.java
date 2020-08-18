package fr.univamu.webdesdonnees.parking.model;

import java.io.Serializable;
import java.util.Date;

public class Measure implements Serializable {
	private static final long serialVersionUID = 1L;

	private String unit;
	private Date time;
	private int value;
	private String name;
	// private double localisation;

	public Measure(String unit, Date time, int value, String name /* , double localisation */) {
		this.unit = unit;
		this.time = time;
		this.value = value;
		this.name = name;
		// this.localisation = localisation;

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

	public void setValue(int value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

//	public double getLocalisation() {
//		return localisation;
//	}

//	public void setLocalisation(double localisation) {
//		this.localisation = localisation;
//	}

}
