package org.comicwiki;

import org.comicwiki.gcd.tables.BrandEmblemGroupTable;
import org.comicwiki.gcd.tables.BrandGroupTable;
import org.comicwiki.gcd.tables.BrandTable;
import org.comicwiki.gcd.tables.BrandUseTable;
import org.comicwiki.gcd.tables.CountryTable;
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
import org.comicwiki.model.schema.Thing;

import com.google.inject.Injector;

public class WikiTableInjector {

	@SuppressWarnings("unchecked")
	protected static final Class<? extends BaseTable<TableRow<? extends Thing>>>[] tableClasses = new Class[] {
			BrandEmblemGroupTable.class, BrandGroupTable.class,
			BrandTable.class, BrandUseTable.class, CountryTable.class,
			IndiciaPublisherTable.class, IssueReprintTable.class,
			IssueTable.class, LanguageTable.class, PublisherTable.class,
			ReprintFromIssueTable.class, ReprintTable.class,
			ReprintToIssueTable.class, SeriesBondTable.class,
			SeriesBondTypeTable.class, SeriesPublicationTypeTable.class,
			SeriesTable.class, StoryTable.class, StoryTypeTable.class };

	protected BaseTable<TableRow<?>>[] getTables() throws Exception {
		BaseTable<TableRow<? extends Thing>>[] tables = new BaseTable[tableClasses.length];
		for (int i = 0; i < tableClasses.length; i++) {
			tables[i] = (BaseTable<TableRow<? extends Thing>>) injector
					.getInstance(tableClasses[i]);
		}
		return tables;
	}

	private Injector injector;

	protected WikiTableInjector(Injector injector) {
		this.injector = injector;
	}

	protected void destroy() {
		injector = null;
	}

}
