package fr.univamu.webdesdonnees.parking.controllers;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.univamu.webdesdonnees.core.model.Measure;
import fr.univamu.webdesdonnees.parking.services.ParkingRepository;

@CrossOrigin
@RestController
@RequestMapping(value = "web-of-things/parking", produces = "application/json")
public class ParkingControllerREST {
	
	@Autowired
	private ParkingRepository parkingRepository;

	@GetMapping("/measures")
	public ResponseEntity<Collection<Measure<Integer>>> getMeasures(
			@RequestParam(value = "id") Optional<String> id,
			@RequestParam(value = "location") Optional<String> location) {
		
		Collection<Measure<Integer>> measures = parkingRepository.getMeasures(id, location);
		return ResponseEntity.ok().body(measures);
	}

	@GetMapping("/measures/{measure-id}")
	public ResponseEntity<Measure<Integer>> getMeasureById(@PathVariable("measure-id") String id) {
		Measure<Integer> measure = parkingRepository.getMeasureById(id);
		return ResponseEntity.ok().body(measure);
	}

}
