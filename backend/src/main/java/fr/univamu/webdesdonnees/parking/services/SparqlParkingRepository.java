package fr.univamu.webdesdonnees.parking.services;

import java.util.Collection;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import fr.univamu.webdesdonnees.core.model.Measure;
import fr.univamu.webdesdonnees.core.services.BaseSparqlRepository;
import fr.univamu.webdesdonnees.core.services.SparqlQueryExecutor;

@Repository
public class SparqlParkingRepository extends BaseSparqlRepository<Integer> implements ParkingRepository {

	public SparqlParkingRepository() {
		super("aarhus_parking.ttl", Integer.class);

		String[] prefixes = { "PREFIX ct: <http://www.insight-centre.org/citytraffic#>",
				"PREFIX ns1: <http://purl.oclc.org/NET/ssnx/ssn#>",
				"PREFIX prov: <http://purl.org/NET/provenance.owl#>",
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>",
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>",
				"PREFIX sao: <http://iot.ee.surrey.ac.uk/citypulse/resources/ontologies/sao.ttl>",
				"PREFIX tl: <http://purl.org/NET/c4dm/timeline.owl#>",
				"PREFIX xml: <http://www.w3.org/XML/1998/namespace>", "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>",
				"PREFIX unit0: <http://purl.oclc.org/NET/muo/citypulse/unit/traffic>" };

		setPrefixes(prefixes);
	}

	@Override
	public Collection<Measure<Integer>> getMeasures(Optional<String> id, Optional<String> location) {
//		String idPrefix = "http://iot.ee.surrey.ac.uk/citypulse/datasets/parking/parkingDataStream#";
//		ArrayList<String> ids = new ArrayList<String>();
//		id.ifPresent(value -> ids.add(idPrefix + value));

		String locationPrefix = "file://" + SparqlQueryExecutor.BASE_URI;
		location = location.map(value -> locationPrefix + value);

		return super.searchMeasures(id, location);
	}
}
