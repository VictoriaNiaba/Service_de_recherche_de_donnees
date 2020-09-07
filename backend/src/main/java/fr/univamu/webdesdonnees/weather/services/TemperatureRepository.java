package fr.univamu.webdesdonnees.weather.services;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;

import fr.univamu.webdesdonnees.core.model.Measure;

public interface TemperatureRepository {

	Collection<Measure<Serializable>> getMeasures(Optional<String> id);

	Measure<Serializable> getMeasureById(String id);

}
