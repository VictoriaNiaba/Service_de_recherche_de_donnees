package fr.univamu.webdesdonnees.parking.services;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.text.TextStringBuilder;
import org.apache.jena.datatypes.xsd.XSDDateTime;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.springframework.stereotype.Repository;

import fr.univamu.webdesdonnees.core.model.Measure;
import fr.univamu.webdesdonnees.core.services.BaseSparqlRepository;

@Repository
public class SparqlParkingRepository extends BaseSparqlRepository implements ParkingRepository {

	private String[] prefixes = {
			"PREFIX ct: <http://www.insight-centre.org/citytraffic#>",
			"PREFIX ns1: <http://purl.oclc.org/NET/ssnx/ssn#>",
			"PREFIX prov: <http://purl.org/NET/provenance.owl#>",
			"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>",
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>",
			"PREFIX sao: <http://iot.ee.surrey.ac.uk/citypulse/resources/ontologies/sao.ttl>",
			"PREFIX tl: <http://purl.org/NET/c4dm/timeline.owl#>",
			"PREFIX xml: <http://www.w3.org/XML/1998/namespace>",
			"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>",
			"PREFIX unit0: <http://purl.oclc.org/NET/muo/citypulse/unit/traffic>"
	};

	public SparqlParkingRepository() {
		super("aarhus_parking.ttl");
	}

	@Override
	public Collection<Measure<Integer>> getMeasures(String idParam, String locationParam) {

		TextStringBuilder sb = new TextStringBuilder();
		sb.appendWithSeparators(prefixes, "\n")
				.appendln("SELECT ?unit ?date ?value ?id ?lat ?lon ?location")
				.appendln("WHERE {")
				.appendln("?id sao:hasUnitOfMeasurement ?unit ;")
				.appendln("sao:value ?value ;")
				.appendln("sao:time ?instant .")
				.appendln("?instant tl:at ?date .")
				.appendln("?id ns1:featureOfInterest ?location .")
				.appendln("?location ct:hasFirstNode ?firstNode .")
				.appendln("?firstNode ct:hasLatitude ?lat ;")
				.appendln("ct:hasLongitude ?lon .");

		// Filter clauses
		String idPrefix = "http://iot.ee.surrey.ac.uk/citypulse/datasets/parking/parkingDataStream#";

		if (idParam != null) {
			String filter = "FILTER strstarts(str(?id), '" + idPrefix + idParam + "')";
			sb.appendln(filter);
		}
		String locationPrefix = "file://" + System.getProperty("user.dir") + "/";

		if (locationParam != null) {
			String filter = "FILTER strstarts(str(?location), '" + locationPrefix + locationParam + "')";
			sb.appendln(filter);
		}

		String queryString = sb.appendln("}").build();

		ResultSet results = this.runQuery(queryString);

		ArrayList<Measure<Integer>> measures = new ArrayList<Measure<Integer>>();
		while (results.hasNext()) {
			QuerySolution row = results.next();
			String unit = row.getResource("unit").getLocalName();
			int value = Integer.parseInt(row.getLiteral("value").getString());
			String id = row.getResource("id").getLocalName();
			String location = row.getResource("location").getNameSpace();
			location = location.substring(location.lastIndexOf('/') + 1);
			location = location.substring(0, location.indexOf('#'));
			double latitude = Double.parseDouble(row.getLiteral("lat").getString());
			double longitude = Double.parseDouble(row.getLiteral("lon").getString());

			XSDDateTime dateTime = (XSDDateTime) row.getLiteral("date").getValue();
			String fixedDateTime = dateTime.toString() + "+02:00";
			ZonedDateTime zonedDateTime = ZonedDateTime.parse(fixedDateTime, DateTimeFormatter.ISO_DATE_TIME);
			ZonedDateTime zonedDateTimeUTC = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
			Measure<Integer> measure = new Measure<Integer>();
			measure.setId(id);
			measure.setTime(zonedDateTimeUTC);
			measure.setUnit(unit);
			measure.setValue(value);
			measure.setDomain("parking-data-stream");
			measure.setLocation(location);
			measure.setLatitude(latitude);
			measure.setLongitude(longitude);

			measures.add(measure);
		}
		return measures;
	}

	@Override
	public Measure<Integer> getMeasureById(String id) {

		String measureId = "<http://iot.ee.surrey.ac.uk/citypulse/datasets/parking/parkingDataStream#"
				+ id
				+ ">";

		TextStringBuilder sb = new TextStringBuilder();
		String queryString = sb.appendWithSeparators(prefixes, "\n")
				.appendln("SELECT ?unit ?date ?value")
				.appendln("WHERE {")
				.appendln("%s sao:hasUnitOfMeasurement ?unit ;", measureId)
				.appendln("sao:value ?value ;")
				.appendln("sao:time ?instant .")
				.appendln("?instant tl:at ?date")
				.appendln("}")
				.build();

		ResultSet results = this.runQuery(queryString);

		Measure<Integer> measure = null;
		if (results.hasNext()) {
			QuerySolution row = results.next();
			String unit = row.getResource("unit").getLocalName();
			int value = Integer.parseInt(row.getLiteral("value").getString());

			XSDDateTime dateTime = (XSDDateTime) row.getLiteral("date").getValue();
			String fixedDateTime = dateTime.toString() + "+02:00";
			ZonedDateTime zonedDateTime = ZonedDateTime.parse(fixedDateTime, DateTimeFormatter.ISO_DATE_TIME);
			ZonedDateTime zonedDateTimeUTC = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));

			measure = new Measure<Integer>();
			measure.setId(id);
			measure.setTime(zonedDateTimeUTC);
			measure.setUnit(unit);
			measure.setValue(value);
			measure.setDomain("parking-data-stream");
		}
		return measure;
	}
}
