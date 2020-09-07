package fr.univamu.webdesdonnees.core.model;

import java.util.Optional;

import org.apache.jena.datatypes.xsd.XSDDateTime;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Resource;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JenaMeasure {
	private Resource streamEvent;
	private Resource unit;
	private XSDDateTime time;
	private Literal value;
	private Resource id;
	private Optional<Resource> location;
	private Optional<Literal> longitude;
	private Optional<Literal> latitude;
}
