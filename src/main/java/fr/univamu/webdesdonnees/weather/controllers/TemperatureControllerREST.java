package fr.univamu.webdesdonnees.weather.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.univamu.webdesdonnees.weather.model.Measure;
import fr.univamu.webdesdonnees.weather.services.TemperatureRepository;

@RestController
@RequestMapping(value = "web-of-things/weather/temperature", produces = "application/json")
public class TemperatureControllerREST {

	@Autowired
	private TemperatureRepository temperatureRepository;

	@GetMapping("/measures")
	public ResponseEntity<Collection<Measure>> getMeasures() {
		Collection<Measure> measures = temperatureRepository.getMeasures();
		return ResponseEntity.ok().body(measures);
	}

	@GetMapping("/measures/{measure-id}")
	public ResponseEntity<Measure> getMeasureById(@PathVariable("measure-id") String id) {
		Measure measure = temperatureRepository.getMeasureById(id);

		return ResponseEntity.ok().body(measure);
	}

}
