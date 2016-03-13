package org.comicwiki.gcd.fields;

import org.comicwiki.OrgLookupService;
import org.comicwiki.ThingFactory;
import org.comicwiki.model.schema.bib.ComicIssue;
import org.comicwiki.model.schema.bib.ComicStory;

import com.google.inject.Inject;

public class FieldParserFactory {

	private final ThingFactory thingFactory;

	@Inject
	public FieldParserFactory(ThingFactory thingFactory) {
		this.thingFactory = thingFactory;
	}

	private CharacterFieldParser parser;
	
	public CharacterFieldParser character(OrgLookupService comicOrganizations,
			ComicStory comicStory) {
		if(parser == null) {
			parser = new CharacterFieldParser(thingFactory, comicOrganizations);
		} else {
			parser.clear();
		}
		parser.setStory(comicStory);
		return parser;
	}
	
	public CreatorFieldParser creator(ComicIssue comicIssue) {
		return new CreatorFieldParser(comicIssue, thingFactory);
	}
	
	public CreatorFieldParser creator(ComicStory comicStory) {
		return new CreatorFieldParser(comicStory, thingFactory);
	}
	
	public IssueNumberFieldParser issueNumber() {
		return new IssueNumberFieldParser(thingFactory);
	}
	
	public PriceFieldParser price() {
		return new PriceFieldParser(thingFactory);
	}
	
	public StringFieldParser string(boolean toUpperCase) {
		return new StringFieldParser(toUpperCase);
	}
	
	public PublishDateFieldParser publishDate() {
		return new PublishDateFieldParser(thingFactory);
	}

	public SaleDateFieldParser saleDate() {
		return new SaleDateFieldParser(thingFactory);
	}
}
