package fr.univamu.webdesdonnees.core.model;

import java.io.Serializable;
import java.time.ZonedDateTime;

import lombok.Data;

@Data
public class Measure<T extends Serializable> implements Serializable {
	private static final long serialVersionUID = 1L;

	private String unit;
	private ZonedDateTime time;
	private T value;
	private String id;
	private String location;
	private Double longitude;
	private Double latitude;
	private String domain;
	private String streamEvent;
}
