package fr.univamu.webdesdonnees.core.services;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.apache.jena.datatypes.xsd.XSDDateTime;
import org.apache.jena.query.QuerySolution;
import org.springframework.stereotype.Service;

import fr.univamu.webdesdonnees.core.model.JenaMeasure;
import fr.univamu.webdesdonnees.core.model.Measure;

@Service
public class MeasureMapper<T extends Serializable> {
	
	Measure<T> jenaMeasureToMeasure(JenaMeasure jenaMeasure, Class<T> clazz) {
		// Constructions simples
		String unit = jenaMeasure.getUnit().getLocalName();
		String id = jenaMeasure.getId().getLocalName();
		// Construction de la valeur en fonction du type générique T
		Object valueAsObject = null;
		if(clazz.equals(Integer.class)) {
			valueAsObject = Integer.parseInt(jenaMeasure.getValue().getString());
		} else {
			valueAsObject = Double.parseDouble(jenaMeasure.getValue().getString());
		}
		T value = clazz.cast(valueAsObject);
	
		// Construction de la date
		String fixedDateTime = jenaMeasure.getTime().toString() + "+02:00";
		ZonedDateTime zonedDateTime = ZonedDateTime.parse(fixedDateTime, DateTimeFormatter.ISO_DATE_TIME);
		ZonedDateTime zonedDateTimeUTC = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
		
		String domain = jenaMeasure.getStreamEvent().getNameSpace();
		domain = domain.substring(0, domain.length() -1);
		domain = domain.substring(domain.lastIndexOf('/') + 1);
		
		String streamEvent = jenaMeasure.getStreamEvent().getLocalName();
		
		// Création d'une mesure à partir des variables précédentes
		Measure<T> measure = new Measure<T>();
		measure.setId(id);
		measure.setTime(zonedDateTimeUTC);
		measure.setUnit(unit);
		measure.setValue(value);
		measure.setDomain(domain);
		measure.setStreamEvent(streamEvent);
		
		jenaMeasure.getLatitude()
			.ifPresent(val -> measure.setLatitude(Double.parseDouble(val.getString())));
		
		jenaMeasure.getLongitude()
		.ifPresent(val -> measure.setLongitude(Double.parseDouble(val.getString())));
		
		// Construction de la localisation
		jenaMeasure.getLocation()
			.map(val -> val.getNameSpace())
			.map(val -> val.substring(val.lastIndexOf('/') + 1))
			.map(val -> val.substring(0, val.indexOf('#')))
			.ifPresent(val -> measure.setLocation(val));
		
		return measure;
	}
	
	public JenaMeasure querySolutionToJenaMeasure(QuerySolution row) {
		
		JenaMeasure jenaMeasure = JenaMeasure.builder()
			.id(row.getResource("id"))
			.unit(row.getResource("unit"))
			.value(row.getLiteral("value"))
			.location(Optional.ofNullable(row.getResource("location")))
			.latitude(Optional.ofNullable(row.getLiteral("lat")))
			.longitude(Optional.ofNullable(row.getLiteral("lon")))
			.time((XSDDateTime) row.getLiteral("date").getValue())
			.streamEvent(row.getResource("streamEvent"))
			.build();
		
		return jenaMeasure;
	}
}
