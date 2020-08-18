package fr.univamu.webdesdonnees.weather.services;

import java.util.Collection;

import fr.univamu.webdesdonnees.weather.model.Measure;

public interface TemperatureRepository {

	Collection<Measure> getMeasures();

	Measure getMeasureById(String id);

}
