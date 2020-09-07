package fr.univamu.webdesdonnees.core.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.springframework.beans.factory.annotation.Autowired;

import fr.univamu.webdesdonnees.core.model.JenaMeasure;
import fr.univamu.webdesdonnees.core.model.Measure;
import lombok.Setter;

public abstract class BaseSparqlRepository<T extends Serializable> {
	protected @Setter String[] prefixes;
	protected @Autowired MeasureMapper<T> mapper;
	protected SparqlQueryExecutor queryExecutor;
	protected @Autowired SparqlQueryBuilder queryBuilder;
	protected Class<T> clazz;
	
	public BaseSparqlRepository(String inputFileName, Class<T> clazz) {
		// Construction du modéle RDF et de la requête SPARQL
		this.queryExecutor = new SparqlQueryExecutor(inputFileName);
		this.clazz = clazz;
	}
	public BaseSparqlRepository(String inputFileNames[], Class<T> clazz) {
		this.queryExecutor = new SparqlQueryExecutor(inputFileNames);
		this.clazz = clazz;
	}

	public Collection<Measure<T>> searchMeasures(Optional<String> id, Optional<String> location) {

		String query = queryBuilder.buildGetMeasuresQuery(prefixes, id, location);
		ResultSet results = queryExecutor.runQuery(query);

		ArrayList<JenaMeasure> jenaMeasures = new ArrayList<JenaMeasure>();
		while (results.hasNext()) {
			QuerySolution row = results.next();
			JenaMeasure jenaMeasure = mapper.querySolutionToJenaMeasure(row);
			jenaMeasures.add(jenaMeasure);
		}
		
		return jenaMeasures.stream()
				.map(jenaMeasure -> mapper.jenaMeasureToMeasure(jenaMeasure, clazz))
				.collect(Collectors.toList());
	}

	public Measure<T> getMeasureById(String id) {

		String fullId = "<http://iot.ee.surrey.ac.uk/citypulse/datasets/weather/aarhus_weather_temperature#" 
				+ id
				+ ">";
		
		String query = queryBuilder.buildGetMeasureByIdQuery(prefixes, fullId);
		ResultSet results = queryExecutor.runQuery(query);

		JenaMeasure jenaMeasure = null;
		if (results.hasNext()) {
			QuerySolution row = results.next();
			jenaMeasure = mapper.querySolutionToJenaMeasure(row);
		}
		return mapper.jenaMeasureToMeasure(jenaMeasure, clazz);
	}
}
