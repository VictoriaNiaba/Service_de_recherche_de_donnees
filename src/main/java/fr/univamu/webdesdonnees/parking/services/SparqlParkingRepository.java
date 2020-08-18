package fr.univamu.webdesdonnees.parking.services;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.jena.datatypes.xsd.XSDDateTime;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.springframework.stereotype.Repository;

import fr.univamu.webdesdonnees.parking.model.Measure;

@Repository
public class SparqlParkingRepository implements ParkingRepository {

	private Model model;
	private String inputFileName = "aarhus_parking.ttl";

	private String prefixes = " PREFIX ct: <http://www.insight-centre.org/citytraffic#> "
			+ " PREFIX ns1: <http://purl.oclc.org/NET/ssnx/ssn#> "
			+ " PREFIX prov: <http://purl.org/NET/provenance.owl#> "
			+ " PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
			+ " PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
			+ " PREFIX sao: <http://iot.ee.surrey.ac.uk/citypulse/resources/ontologies/sao.ttl> "
			+ " PREFIX tl: <http://purl.org/NET/c4dm/timeline.owl#> "
			+ " PREFIX xml: <http://www.w3.org/XML/1998/namespace> "
			+ " PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
			+ " PREFIX unit0: <http://purl.oclc.org/NET/muo/citypulse/unit/traffic> ";

	public SparqlParkingRepository() {
		// Construction du modéle RDF et de la requête SPARQL
		model = this.createDefaultModel(inputFileName);
	}

	@Override
	public Collection<Measure> getMeasures() {
		String queryString = prefixes
				+ "SELECT ?unit ?date ?value "
				+ "WHERE { "
				+ "?measure sao:hasUnitOfMeasurement ?unit ; "
				+ "sao:value ?value ; "
				+ "sao:time ?instant . "
				+ "?instant tl:at ?date "
				+ "} ";

		ResultSet results = this.runQuery(queryString);

		ArrayList<Measure> measures = new ArrayList<Measure>();
		while (results.hasNext()) {
			QuerySolution row = results.next();
			String unit = row.getResource("unit").getLocalName();
			int value = Integer.parseInt(row.getLiteral("value").getString());

//			String name = row.getResource("name").getLocalName();

			XSDDateTime dateTime = (XSDDateTime) row.getLiteral("date").getValue();
			Date time = dateTime.asCalendar().getTime();

			Measure measure = new Measure(unit, time, value, null);
			measures.add(measure);
		}
		return measures;
	}

	@Override
	public Measure getMeasureById(String id) {

		String measureId = "<http://iot.ee.surrey.ac.uk/citypulse/datasets/parking/parkingDataStream#"
				+ id
				+ ">";

		String queryString = prefixes
				+ "SELECT ?unit ?date ?value "
				+ "WHERE { "
				+ measureId + " sao:hasUnitOfMeasurement ?unit ; "
				+ "sao:value ?value ; "
				+ "sao:time ?instant . "
				+ "?instant tl:at ?date "
				+ "} ";

		ResultSet results = this.runQuery(queryString);

		Measure measure = null;
		if (results.hasNext()) {
			QuerySolution row = results.next();
			String unit = row.getResource("unit").getLocalName();
			int value = Integer.parseInt(row.getLiteral("value").getString());
//			String name = row.getResource("name").getLocalName();

			XSDDateTime dateTime = (XSDDateTime) row.getLiteral("date").getValue();
			Date time = dateTime.asCalendar().getTime();

			measure = new Measure(unit, time, value, null);
		}
		return measure;
	}

	/**
	 * Exécution d'une requête
	 * 
	 * @param queryString
	 * @return
	 */
	private ResultSet runQuery(String queryString) {
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		ResultSet results = null;
		try {
			results = ResultSetFactory.copyResults(qexec.execSelect());
		} finally {
			qexec.close();
		}

		return results;
	}

	private Model createDefaultModel(String inputFileName) {
		Model model = ModelFactory.createDefaultModel();
		// use the RDFDataMgr to find the input file
		InputStream in = RDFDataMgr.open(inputFileName);
		if (in == null) {
			throw new IllegalArgumentException("File: " + inputFileName + " not found");
		}

		// read the RDF/XML file
		model.read(in, null, "TTL");
		return model;
	}
}
