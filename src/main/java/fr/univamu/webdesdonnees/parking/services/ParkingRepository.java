package fr.univamu.webdesdonnees.parking.services;

import java.util.Collection;

import fr.univamu.webdesdonnees.parking.model.Measure;

public interface ParkingRepository {

	Collection<Measure> getMeasures();

	Measure getMeasureById(String id);
}
