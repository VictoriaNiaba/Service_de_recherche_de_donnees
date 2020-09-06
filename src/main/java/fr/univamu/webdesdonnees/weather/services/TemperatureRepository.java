package fr.univamu.webdesdonnees.weather.services;

import java.util.Collection;

import fr.univamu.webdesdonnees.core.model.Measure;

public interface TemperatureRepository {

	Collection<Measure<Double>> getMeasures(String id);

	Measure<Double> getMeasureById(String id);

}
