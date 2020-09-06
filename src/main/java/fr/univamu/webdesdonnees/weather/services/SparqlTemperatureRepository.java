package fr.univamu.webdesdonnees.weather.services;

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
public class SparqlTemperatureRepository extends BaseSparqlRepository implements TemperatureRepository {

	private String[] prefixes = { "PREFIX ct:<http://www.insight-centre.org/citytraffic#>",
			"PREFIX ns1:<http://purl.oclc.org/NET/ssnx/ssn#>",
			"PREFIX prov:<http://purl.org/NET/provenance.owl#>",
			"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>",
			"PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>",
			"PREFIX sao:<http://iot.ee.surrey.ac.uk/citypulse/resources/ontologies/sao.ttl>",
			"PREFIX tl:<http://purl.org/NET/c4dm/timeline.owl#>",
			"PREFIX xml:<http://www.w3.org/XML/1998/namespace>",
			"PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>",
			"PREFIX unit0: <http://purl.oclc.org/NET/muo/ucum/unit/temperature#> " };

	public SparqlTemperatureRepository() {
		super("temperature.ttl");
	}

	@Override
	public Collection<Measure<Double>> getMeasures(String id) {

		/*-------------------- Construction of the request ----------------------*/
		TextStringBuilder sb = new TextStringBuilder();
		sb.appendWithSeparators(prefixes, "\n")
				.appendln("SELECT ?unit ?date ?value ?id")
				.appendln("WHERE { ")
				.appendln("?id sao:hasUnitOfMeasurement ?unit ;")
				.appendln("sao:value ?value ;")
				.appendln("sao:time ?instant .")
				.appendln("?instant tl:at ?date .");
		// Filter clauses
		String tempPrefix = "http://iot.ee.surrey.ac.uk/citypulse/datasets/weather/aarhus_weather_temperature#";

//		id.map(value -> "FILTER strstarts(str(?id), '" + tempPrefix + value + "')")
//		  .ifPresent(value -> sb.appendln(value));

		if (id != null) {
			String filter = "FILTER strstarts(str(?id), '" + tempPrefix + id + "')";
			sb.appendln(filter);
		}

		// End of the query
		String queryString = sb.appendln("}").build();

		/*-------------------- Request execution -----------------------*/
		ResultSet results = this.runQuery(queryString);

		/*-------------------- Results formatting ----------------------*/
		ArrayList<Measure<Double>> measures = new ArrayList<Measure<Double>>();
		while (results.hasNext()) {
			QuerySolution row = results.next();

			Measure<Double> measure = new Measure<Double>();
			measure.setId(row.getResource("id").getLocalName());
			measure.setUnit(row.getResource("unit").getLocalName());
			measure.setValue(Double.parseDouble(row.getLiteral("value").getString()));
			measure.setTime(toUTC((XSDDateTime) row.getLiteral("date").getValue()));

			measures.add(measure);
		}

		return measures;
	}

	@Override
	public Measure<Double> getMeasureById(String id) {

		String measureId = "<http://iot.ee.surrey.ac.uk/citypulse/datasets/weather/aarhus_weather_temperature#"
				+ id
				+ ">";

		TextStringBuilder sb = new TextStringBuilder();
		String queryString = sb.appendWithSeparators(prefixes, "\n")
				.appendln("SELECT ?unit ?date ?value")
				.appendln("WHERE { ")
				.appendln("%s sao:hasUnitOfMeasurement ?unit ;", measureId)
				.appendln("sao:value ?value ;")
				.appendln("sao:time ?instant .")
				.appendln("?instant tl:at ?date")
				.appendln("} ")
				.build();

		ResultSet results = this.runQuery(queryString);

		Measure<Double> measure = null;
		if (results.hasNext()) {
			QuerySolution row = results.next();

			measure = new Measure<Double>();
			measure.setId(row.getResource("id").getLocalName());
			measure.setUnit(row.getResource("unit").getLocalName());
			measure.setValue(Double.parseDouble(row.getLiteral("value").getString()));
			measure.setTime(toUTC((XSDDateTime) row.getLiteral("date").getValue()));
		}
		return measure;
	}
}
