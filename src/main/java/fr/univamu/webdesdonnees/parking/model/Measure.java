package fr.univamu.webdesdonnees.parking.model;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class Measure implements Serializable {
	private static final long serialVersionUID = 1L;

	private String unit;
	private ZonedDateTime time;
	private int value;
	private String id;
	// private double localisation;

	public Measure(String unit, ZonedDateTime time, int value, String id /* double localisation */ ) {
		this.unit = unit;
		this.time = time;
		this.value = value;
		this.id = id;
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

	public ZonedDateTime getTime() {
		return time;
	}

	public void setTime(ZonedDateTime time) {
		this.time = time;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

//	public double getLocalisation() {
//		return localisation;
//	}

//	public void setLocalisation(double localisation) {
//		this.localisation = localisation;
//	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Measure) {
			Measure otherMeasure = (Measure) other;
			return this.id.equals(otherMeasure.id);
		}
		return false;
	}

	@Override
	public String toString() {
		return "{ id: " + id + " unit: " + unit + " }";
	}
}
