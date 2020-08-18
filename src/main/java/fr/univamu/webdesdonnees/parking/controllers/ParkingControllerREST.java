package fr.univamu.webdesdonnees.parking.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.univamu.webdesdonnees.parking.model.Measure;
import fr.univamu.webdesdonnees.parking.services.ParkingRepository;

@RestController
@RequestMapping(value = "web-of-things/parking", produces = "application/json")
public class ParkingControllerREST {
	@Autowired
	private ParkingRepository parkingRepository;

	@GetMapping("/measures")
	public ResponseEntity<Collection<Measure>> getMeasures() {
		Collection<Measure> measures = parkingRepository.getMeasures();
		return ResponseEntity.ok().body(measures);
	}

	@GetMapping("/measures/{measure-id}")
	public ResponseEntity<Measure> getMeasureById(@PathVariable("measure-id") String id) {
		Measure measure = parkingRepository.getMeasureById(id);

		return ResponseEntity.ok().body(measure);
	}

}
