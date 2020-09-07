package fr.univamu.webdesdonnees.core.services;

import java.util.Optional;

import org.apache.commons.text.TextStringBuilder;
import org.springframework.stereotype.Service;

@Service
public class SparqlQueryBuilder {

	public String buildGetMeasuresQuery(String[] prefixes, Optional<String> idParam, Optional<String> locationParam) {
		TextStringBuilder sb = new TextStringBuilder();
		sb.appendWithSeparators(prefixes, "\n")
			.appendln("SELECT ?unit ?date ?value ?id ?lat ?lon ?location ?streamEvent")
			.appendln("WHERE {")
			.appendln("?streamEvent prov:used ?id .")
			.appendln("?id sao:hasUnitOfMeasurement ?unit ;")
			.appendln("sao:value ?value ;")
			.appendln("sao:time ?instant .")
			.appendln("?instant tl:at ?date .")
			// Clauses optionnelles
			.appendln("OPTIONAL {")
			.appendln("?id ns1:featureOfInterest ?location .")
			.appendln("?location ct:hasFirstNode ?firstNode . ")
			.appendln("?firstNode ct:hasLatitude ?lat ; ")
			.appendln("ct:hasLongitude ?lon .")
			.appendln("}");
		// Clauses de filtrage
		idParam.ifPresent(value -> sb.appendln("FILTER strstarts(str(?id), concat(str(?streamEvent), " + "'#" + value + "'))"));
		locationParam.ifPresent(value -> sb.appendln("FILTER strstarts(str(?location), '" + value + "')"));

		return sb.appendln("}").build();
	}

	public String buildGetMeasureByIdQuery(String[] prefixes, String id) {
		TextStringBuilder sb = new TextStringBuilder();
		return sb.appendWithSeparators(prefixes, "\n")
			.appendln("SELECT ?unit ?date ?value ?lat ?lon ?location")
			.appendln("WHERE { ")
			.appendln("?streamEvent prov:used %s .", id)
			.appendln("%s sao:hasUnitOfMeasurement ?unit ;", id)
			.appendln("sao:value ?value ;")
			.appendln("sao:time ?instant .")
			.appendln("?instant tl:at ?date .")
			.appendln("?id ns1:featureOfInterest ?location .")
			.appendln("?location ct:hasFirstNode ?firstNode .")
			.appendln("?firstNode ct:hasLatitude ?lat ;")
			.appendln("ct:hasLongitude ?lon .")
			.appendln("}")
			.build();
	}
}