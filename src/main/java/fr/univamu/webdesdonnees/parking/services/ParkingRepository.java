package fr.univamu.webdesdonnees.parking.services;

import java.util.Collection;

import fr.univamu.webdesdonnees.core.model.Measure;

public interface ParkingRepository {

	Collection<Measure<Integer>> getMeasures(String idParam, String locationParam);

	Measure<Integer> getMeasureById(String id);
}
