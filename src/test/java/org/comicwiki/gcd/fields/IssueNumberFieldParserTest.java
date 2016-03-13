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
		ComicIssueNumber number = parser.parse(input);
		assertEquals(Integer.valueOf(1), number.issueNumbers.iterator().next());
	}
	
	@Test
	public void year() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		IssueNumberFieldParser parser = new IssueNumberFieldParser(thingFactory);
		String input = "41/1984";
		ComicIssueNumber number = parser.parse(input);
		assertEquals(Integer.valueOf(1984), number.year);
		assertEquals(Integer.valueOf(41), number.issueNumbers.iterator().next());
	}
	
	@Test
	public void yearWithCover() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		IssueNumberFieldParser parser = new IssueNumberFieldParser(thingFactory);
		String input = "5/2015 (#191)";
		ComicIssueNumber number = parser.parse(input);
		assertEquals(Integer.valueOf(2015), number.year);
		assertEquals(Integer.valueOf(5), number.issueNumbers.iterator().next());
		assertEquals(Integer.valueOf(191), number.cover);
	}
	
	@Test
	public void issueCover() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		IssueNumberFieldParser parser = new IssueNumberFieldParser(thingFactory);
		String input = "34 (132)";
		ComicIssueNumber number = parser.parse(input);
		assertEquals(Integer.valueOf(132), number.cover);
		assertEquals(Integer.valueOf(34), number.issueNumbers.iterator().next());
	}
	
	@Test
	public void assigned() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		IssueNumberFieldParser parser = new IssueNumberFieldParser(thingFactory);
		String input = "34 [191]";
		ComicIssueNumber number = parser.parse(input);
		assertEquals(Integer.valueOf(34), number.issueNumbers.iterator().next());
		assertEquals(Integer.valueOf(191), number.assigned);
	}
	
	@Test
	public void dualNumber() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		IssueNumberFieldParser parser = new IssueNumberFieldParser(thingFactory);
		String input = "69 / 9";
		ComicIssueNumber number = parser.parse(input);
		
		assertTrue(number.issueNumbers.contains(69));
		assertTrue(number.issueNumbers.contains(9));
	}
	
	@Test
	public void noNumber() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		IssueNumberFieldParser parser = new IssueNumberFieldParser(thingFactory);
		String input = "[nn]";
		ComicIssueNumber number = parser.parse(input);
		assertEquals("[nn]", number.label);

	}
	
	@Test
	public void second() throws Exception {
		ThingFactory thingFactory = createThingFactory();
		IssueNumberFieldParser parser = new IssueNumberFieldParser(thingFactory);
		String input = "[2]";
		ComicIssueNumber number = parser.parse(input);
		assertEquals(Integer.valueOf(2), number.assigned);
	}
}
