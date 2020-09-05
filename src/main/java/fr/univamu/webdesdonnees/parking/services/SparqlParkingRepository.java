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

import fr.univamu.webdesdonnees.core.services.BaseSparqlRepository;
import fr.univamu.webdesdonnees.parking.model.Measure;

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
	public Collection<Measure> getMeasures() {

		TextStringBuilder sb = new TextStringBuilder();
		String queryString = sb.appendWithSeparators(prefixes, "\n")
				.appendln("SELECT ?unit ?date ?value ?id")
				.appendln("WHERE {")
				.appendln("?id sao:hasUnitOfMeasurement ?unit ;")
				.appendln("sao:value ?value ;")
				.appendln("sao:time ?instant .")
				.appendln("?instant tl:at ?date")
				.appendln("}")
				.build();

		ResultSet results = this.runQuery(queryString);

		ArrayList<Measure> measures = new ArrayList<Measure>();
		while (results.hasNext()) {
			QuerySolution row = results.next();
			String unit = row.getResource("unit").getLocalName();
			int value = Integer.parseInt(row.getLiteral("value").getString());
			String id = row.getResource("id").getLocalName();

			XSDDateTime dateTime = (XSDDateTime) row.getLiteral("date").getValue();
			String fixedDateTime = dateTime.toString() + "+02:00";
			ZonedDateTime zonedDateTime = ZonedDateTime.parse(fixedDateTime, DateTimeFormatter.ISO_DATE_TIME);
			ZonedDateTime zonedDateTimeUTC = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
			Measure measure = new Measure(unit, zonedDateTimeUTC, value, id);
			measures.add(measure);
		}
		return measures;
	}

	@Override
	public Measure getMeasureById(String id) {

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

		Measure measure = null;
		if (results.hasNext()) {
			QuerySolution row = results.next();
			String unit = row.getResource("unit").getLocalName();
			int value = Integer.parseInt(row.getLiteral("value").getString());

			XSDDateTime dateTime = (XSDDateTime) row.getLiteral("date").getValue();
			String fixedDateTime = dateTime.toString() + "+02:00";
			ZonedDateTime zonedDateTime = ZonedDateTime.parse(fixedDateTime, DateTimeFormatter.ISO_DATE_TIME);
			ZonedDateTime zonedDateTimeUTC = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));

			measure = new Measure(unit, zonedDateTimeUTC, value, id);
		}
		return measure;
	}
}
