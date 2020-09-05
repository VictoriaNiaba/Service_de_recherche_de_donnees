package fr.univamu.webdesdonnees.core.services;

import java.io.InputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.jena.datatypes.xsd.XSDDateTime;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;

public abstract class BaseSparqlRepository {
	private Model model;

	public BaseSparqlRepository(String inputFileName) {
		// Construction du modéle RDF et de la requête SPARQL
		model = this.createDefaultModel(inputFileName);
	}

	protected ResultSet runQuery(String queryString) {
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
	
	protected ZonedDateTime toUTC(XSDDateTime dateTime) {
		String fixedDateTime = dateTime.toString() + "+02:00";
		ZonedDateTime zonedDateTime = ZonedDateTime.parse(fixedDateTime, DateTimeFormatter.ISO_DATE_TIME);
		ZonedDateTime zonedDateTimeUTC = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
		
		return zonedDateTimeUTC;
	}
}
