package org.comicwiki.gcd.fields;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.comicwiki.ThingFactory;
import org.comicwiki.model.ComicIssueNumber;
import org.junit.Test;

public class IssueNumberFieldParserTest extends BaseFieldTest {

	@Test
	public void single() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		IssueNumberFieldParser parser = new IssueNumberFieldParser(thingFactory);
		String input = "1";
		ComicIssueNumber number = parser.parser(input);
		assertEquals("1",number.issueNumbers.iterator().next());
	}
	
	@Test
	public void year() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		IssueNumberFieldParser parser = new IssueNumberFieldParser(thingFactory);
		String input = "41/1984";
		ComicIssueNumber number = parser.parser(input);
		assertEquals("1984", number.year);
		assertEquals("41", number.issueNumbers.iterator().next());
	}
	
	@Test
	public void yearWithCover() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		IssueNumberFieldParser parser = new IssueNumberFieldParser(thingFactory);
		String input = "5/2015 (#191)";
		ComicIssueNumber number = parser.parser(input);
		assertEquals("2015", number.year);
		assertEquals("5", number.issueNumbers.iterator().next());
		assertEquals("#191", number.cover);
	}
	
	@Test
	public void issueCover() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		IssueNumberFieldParser parser = new IssueNumberFieldParser(thingFactory);
		String input = "34 (132)";
		ComicIssueNumber number = parser.parser(input);
		assertEquals("132", number.cover);
		assertEquals("34", number.issueNumbers.iterator().next());
	}
	
	@Test
	public void assigned() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		IssueNumberFieldParser parser = new IssueNumberFieldParser(thingFactory);
		String input = "34 [191]";
		ComicIssueNumber number = parser.parser(input);
		assertEquals("34", number.issueNumbers.iterator().next());
		assertEquals("191", number.assigned);
	}
	
	@Test
	public void dualNumber() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		IssueNumberFieldParser parser = new IssueNumberFieldParser(thingFactory);
		String input = "69 / 9";
		ComicIssueNumber number = parser.parser(input);
		
		assertTrue(number.issueNumbers.contains("69"));
		assertTrue(number.issueNumbers.contains("9"));
	}
	
	@Test
	public void noNumber() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		IssueNumberFieldParser parser = new IssueNumberFieldParser(thingFactory);
		String input = "[nn]";
		ComicIssueNumber number = parser.parser(input);
		assertEquals("[nn]", number.label);

	}
	
	@Test
	public void second() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		IssueNumberFieldParser parser = new IssueNumberFieldParser(thingFactory);
		String input = "[2]";
		ComicIssueNumber number = parser.parser(input);
		assertEquals("2", number.assigned);
	}
}
