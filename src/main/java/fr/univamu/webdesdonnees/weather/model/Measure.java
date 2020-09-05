package fr.univamu.webdesdonnees.weather.model;

import java.io.Serializable;
import java.time.ZonedDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Measure implements Serializable {

	private static final long serialVersionUID = 1L;

	private String unit;
	private ZonedDateTime time;
	private double value;
	private String id;
}
