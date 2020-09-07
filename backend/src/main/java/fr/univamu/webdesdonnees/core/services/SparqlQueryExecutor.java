package fr.univamu.webdesdonnees.core.services;

import java.io.InputStream;
import java.util.stream.Stream;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;

public class SparqlQueryExecutor {
	public Model model;
	public static final String BASE_URI = "/web-of-things/";

	public SparqlQueryExecutor(String inputFileName) {
		// Construction du modéle RDF et de la requête SPARQL
		model = createDefaultModel();
		readFile(inputFileName);
	}
	
	public SparqlQueryExecutor(String[] inputFileNames) {
		model = createDefaultModel();
		readFiles(inputFileNames);
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

	private Model createDefaultModel() {
		Model model = ModelFactory.createDefaultModel();
		return model;
	}
	
	public void readFile(String inputFileName) {
		// use the RDFDataMgr to find the input file
		InputStream in = RDFDataMgr.open(inputFileName);
		if (in == null) {
			throw new IllegalArgumentException("File: " + inputFileName + " not found");
		}

		// read the RDF/XML file
		model.read(in, BASE_URI, "TTL");
	}
	
	public void readFiles(String[] inputFileNames) {
		Stream.of(inputFileNames).forEach(inputFileName -> readFile(inputFileName));
	}
}