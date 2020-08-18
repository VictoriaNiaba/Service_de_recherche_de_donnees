package fr.univamu.webdesdonnees.partie1;

import java.io.InputStream;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;

public class JenaRDF {

	public static void main(String[] args) {

		String inputFileName = "aarhus_parking.ttl";

		// create an empty model
		Model model = ModelFactory.createDefaultModel();

		// use the RDFDataMgr to find the input file
		InputStream in = RDFDataMgr.open(inputFileName);
		if (in == null) {
			throw new IllegalArgumentException("File: " + inputFileName + " not found");
		}

		// read the RDF/XML file
		model.read(in, null, "TTL");

		// write it to standard out
		model.write(System.out);
	}
}
