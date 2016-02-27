package org.comicwiki.guice;

import java.io.File;
import java.io.IOException;

import org.apache.spark.sql.SQLContext;
import org.comicwiki.gcd.OrgLookupService;
import org.comicwiki.gcd.SparkUtils;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class ComicWikiModule extends AbstractModule {

	@Override
	protected void configure() {

	}

	@Provides
	@Singleton
	SQLContext provideSqlContext() {
		return SparkUtils.createLocalContext();
	}
	
	@Provides
	@Singleton
	OrgLookupService provideComicOrganizations() {
		OrgLookupService orgs = new OrgLookupService();
		try {
			orgs.load(new File("."));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return orgs;
	}
}
