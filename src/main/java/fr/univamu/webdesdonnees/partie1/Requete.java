package fr.univamu.webdesdonnees.partie1;

import java.io.InputStream;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;

public class Requete {

	public static void runQuery(String inputFileName, String queryString) {

		// Constru du modéle RDF et de la requête SPARQL
		Model model = Requete.createDefaultModel(inputFileName);
		Query query = QueryFactory.create(queryString);

		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		try {
			// Exécution de la requête
			ResultSet results = qexec.execSelect();

			// Affichage des résultats formattés sur la sortie standard
			ResultSetFormatter.out(System.out, results, query);
		} finally {
			qexec.close();
		}
	}

	public static Model createDefaultModel(String inputFileName) {
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
