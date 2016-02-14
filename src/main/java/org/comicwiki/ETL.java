package org.comicwiki;

import org.comicwiki.gcd.tables.BaseTable;
import org.comicwiki.gcd.tables.BrandEmblemGroupTable;
import org.comicwiki.gcd.tables.BrandGroupTable;
import org.comicwiki.gcd.tables.BrandTable;
import org.comicwiki.gcd.tables.BrandUseTable;
import org.comicwiki.gcd.tables.CountryTable;
import org.comicwiki.gcd.tables.GenresTable;
import org.comicwiki.gcd.tables.IndiciaPublisherTable;
import org.comicwiki.gcd.tables.IssueReprintTable;
import org.comicwiki.gcd.tables.IssueTable;
import org.comicwiki.gcd.tables.LanguageTable;
import org.comicwiki.gcd.tables.PublisherTable;
import org.comicwiki.gcd.tables.ReprintFromIssueTable;
import org.comicwiki.gcd.tables.ReprintTable;
import org.comicwiki.gcd.tables.ReprintToIssueTable;
import org.comicwiki.gcd.tables.SeriesBondTable;
import org.comicwiki.gcd.tables.SeriesBondTypeTable;
import org.comicwiki.gcd.tables.SeriesPublicationTypeTable;
import org.comicwiki.gcd.tables.SeriesTable;
import org.comicwiki.gcd.tables.StoryTable;
import org.comicwiki.gcd.tables.StoryTypeTable;

public class ETL {
	
	@SuppressWarnings("unchecked")
	private static final Class<? extends BaseTable<?>>[] tables 
		= new Class[]{BrandEmblemGroupTable.class, BrandGroupTable.class,
		BrandTable.class, BrandUseTable.class, CountryTable.class, GenresTable.class,
		IndiciaPublisherTable.class, IssueReprintTable.class, IssueTable.class,
		LanguageTable.class, PublisherTable.class, ReprintFromIssueTable.class,
		ReprintTable.class, ReprintToIssueTable.class, SeriesBondTable.class,
		SeriesBondTypeTable.class, SeriesPublicationTypeTable.class,
		SeriesTable.class, StoryTable.class, StoryTypeTable.class};
	
	public void extract() {
		
		for(Class<? extends BaseTable<?>> table : tables) {
			
		}
	}
}
