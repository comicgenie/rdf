package org.comicwiki.gcd.fields;

import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.OrgLookupService;
import org.comicwiki.model.schema.bib.ComicStory;

public class FieldParserFactory {

	private final ThingFactory thingFactory;

	public FieldParserFactory(ThingFactory thingFactory) {
		this.thingFactory = thingFactory;
	}

	public PublishDateFieldParser publishDate() {
		return new PublishDateFieldParser(thingFactory);
	}
	
	public IssueNumberFieldParser issueNumber() {
		return new IssueNumberFieldParser(thingFactory);
	}
	
	public CreatorFieldParser creator() {
		return new CreatorFieldParser(thingFactory);
	}
	
	public PriceFieldParser price() {
		return new PriceFieldParser(thingFactory);
	}
	
	public SaleDateFieldParser saleDate() {
		return new SaleDateFieldParser(thingFactory);
	}

	public CharacterFieldParser character(OrgLookupService comicOrganizations,
			ComicStory comicStory) {
		return new CharacterFieldParser(thingFactory, comicOrganizations,
				comicStory);
	}
}
