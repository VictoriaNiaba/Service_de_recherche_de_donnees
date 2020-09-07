package fr.univamu.webdesdonnees.weather.services;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import fr.univamu.webdesdonnees.core.model.Measure;
import fr.univamu.webdesdonnees.core.services.BaseSparqlRepository;

@Repository
public class SparqlTemperatureRepository extends BaseSparqlRepository<Serializable> implements TemperatureRepository {

	public SparqlTemperatureRepository() {
		super(new String[] {
			"temperature.ttl", 
			"humidity.ttl",
			"pressure.ttl",
			"dewpoint.ttl",
			"wind_speed.ttl"
		}, Serializable.class);
		
		String[] prefixes = { "PREFIX ct:<http://www.insight-centre.org/citytraffic#>",
				"PREFIX ns1:<http://purl.oclc.org/NET/ssnx/ssn#>",
				"PREFIX prov:<http://purl.org/NET/provenance.owl#>",
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>",
				"PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>",
				"PREFIX sao:<http://iot.ee.surrey.ac.uk/citypulse/resources/ontologies/sao.ttl>",
				"PREFIX tl:<http://purl.org/NET/c4dm/timeline.owl#>",
				"PREFIX xml:<http://www.w3.org/XML/1998/namespace>",
				"PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>",
				
				"PREFIX unit0: <http://purl.oclc.org/NET/muo/ucum/unit/temperature#>",
				"PREFIX unit1: <http://purl.oclc.org/NET/muo/ucum/unit/fraction#>",
				"PREFIX unit2: <http://purl.oclc.org/NET/muo/citypulse/unit/velocity#>",
				"PREFIX unit3: <http://purl.oclc.org/NET/muo/ucum/unit/pressure#> " };
		
		setPrefixes(prefixes);
	}

	@Override
	public Collection<Measure<Serializable>> getMeasures(Optional<String> id) {
		return super.searchMeasures(id, Optional.empty());
	}
}
