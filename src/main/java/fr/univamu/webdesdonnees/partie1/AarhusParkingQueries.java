package fr.univamu.webdesdonnees.partie1;

public class AarhusParkingQueries {

	private static String inputFileName = "aarhus_parking.ttl";

	private static String prefixes = "PREFIX dc:<http://purl.org/dc/elements/1.1/> "
			+ "PREFIX sao: <http://iot.ee.surrey.ac.uk/citypulse/resources/ontologies/sao.ttl> "
			+ "PREFIX tl: <http://purl.org/NET/c4dm/timeline.owl#> "
			+ "PREFIX ct: <http://www.insight-centre.org/citytraffic#> "
			+ "PREFIX ns1: <http://purl.oclc.org/NET/ssnx/ssn#> "
			+ "PREFIX prov: <http://purl.org/NET/provenance.owl#> "
			+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
			+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
			+ "PREFIX xml: <http://www.w3.org/XML/1998/namespace> " + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
			+ "PREFIX unit0: <http://purl.oclc.org/NET/muo/citypulse/unit/traffic> ";

	public static void main(String args[]) {
		// AarhusParkingQueries.printObject();
		AarhusParkingQueries.printValue(
				"<http://iot.ee.surrey.ac.uk/citypulse/datasets/parking/parkingDataStream#observations_point_kd05socppljl85phrlv5mu7vfu> ");
	}

	public static void printObject() {
		String queryString = prefixes + "SELECT ?object WHERE {?object a sao:streamEvent}";
		Requete.runQuery(inputFileName, queryString);
	}

	public static void printStartAndEnd(String object) {

		String queryString = prefixes
				+ "SELECT ?tempsdebut ?tempsfin ?test WHERE { ?test tl:start ?tempsdebut . ?test tl:end ?tempsfin }";

		Requete.runQuery(inputFileName, queryString);

	}

	public static void printUnitOfMesure(String object) {
		String queryString = prefixes + "SELECT ?unitofmesure WHERE { " + object
				+ " sao:hasUnitOfMeasurement ?unitofmesure} ";
		Requete.runQuery(inputFileName, queryString);
	}

	public static void printObservationTime(String object) {
		String queryString = prefixes + "SELECT ?observationtime WHERE { " + object
				+ " sao:time ?instant . ?instant tl:at ?observationtime}";
		Requete.runQuery(inputFileName, queryString);
	}

	public static void printLongitudeAndLatitude(String object) {
		String queryString = prefixes + "SELECT ?latitude ?longitude WHERE {" + object
				+ " ct:hasFirstNode ?test .?test ct:hasLatitude ?latitude ;" + "ct:hasLongitude ?longitude}";
		Requete.runQuery(inputFileName, queryString);
	}

	public static void printhasNodeName(String object) {
		String queryString = prefixes + "SELECT ?nodename WHERE { " + object
				+ " ct:hasFirstNode ?node.?node ct:hasNodeName ?nodename}";
		Requete.runQuery(inputFileName, queryString);
	}

	public static void printValue(String object) {
		String queryString = prefixes + "SELECT ?value WHERE{ ?val sao:value ?value}";
		Requete.runQuery(inputFileName, queryString);
	}

}
