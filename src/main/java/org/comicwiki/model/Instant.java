package org.comicwiki.model;

import static org.comicwiki.rdf.DataType.XSD_GDAY;
import static org.comicwiki.rdf.DataType.XSD_GMONTH;
import static org.comicwiki.rdf.DataType.XSD_GYEAR;

import java.util.Date;

import org.comicwiki.rdf.annotations.ObjectDate;
import org.comicwiki.rdf.annotations.ObjectNonNegativeInteger;
import org.comicwiki.rdf.annotations.ObjectNumber;
import org.comicwiki.rdf.annotations.ObjectXSD;
import org.comicwiki.rdf.annotations.Predicate;
import org.comicwiki.rdf.annotations.SchemaComicWiki;
import org.comicwiki.rdf.annotations.Subject;

@SchemaComicWiki
@Subject(value = "Instant", key = "name", isBlankNode = true)
public class Instant extends TemporalEntity {

		@Predicate("year")
		@ObjectXSD(XSD_GYEAR)
		public int year;
		
		@Predicate("month")
		@ObjectXSD(XSD_GMONTH)
		public int month;
		
		@Predicate("day")
		@ObjectXSD(XSD_GDAY)
		public int day;
		
		@Predicate("hour")
		@ObjectNonNegativeInteger
		public int hour;
		
		@Predicate("minute")
		@ObjectNonNegativeInteger
		public int minute;
		
		@Predicate("second")
		@ObjectNonNegativeInteger
		public int second;
		
		//tzont;TimeZone
		private String timezone;
	
		@Predicate("instantDate")
		@ObjectDate
		public Date date;
		
		//DateType
		
		//SET_A
		//YYYY-MM-DD
		//YYYY-MM
		//YYYY
		
		//SET_B (Interval)
		//SET_A/SET_A

		//SET_C (DateTime)
		//Date
		
}
