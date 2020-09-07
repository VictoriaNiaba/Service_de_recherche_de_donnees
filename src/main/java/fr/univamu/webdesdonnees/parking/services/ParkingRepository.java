package fr.univamu.webdesdonnees.parking.services;

import java.util.Collection;
import java.util.Optional;

import fr.univamu.webdesdonnees.core.model.Measure;

public interface ParkingRepository {

	Collection<Measure<Integer>> getMeasures(Optional<String> idParam, Optional<String> locationParam);

	Measure<Integer> getMeasureById(String id);
}
