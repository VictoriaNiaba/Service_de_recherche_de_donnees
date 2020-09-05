package fr.univamu.webdesdonnees.weather.services;

import java.util.Collection;
import java.util.Optional;

import fr.univamu.webdesdonnees.weather.model.Measure;

public interface TemperatureRepository {

	Collection<Measure> getMeasures(Optional<String> id);

	Measure getMeasureById(String id);

}
