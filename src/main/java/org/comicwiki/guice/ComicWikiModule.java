package org.comicwiki.guice;

import java.io.File;
import java.io.IOException;

import org.apache.spark.sql.SQLContext;
import org.comicwiki.OrgLookupService;
import org.comicwiki.PersonNameMatcher;
import org.comicwiki.SparkUtils;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class ComicWikiModule extends AbstractModule {

	private File baseDir;

	public ComicWikiModule(File baseDir) {
		this.baseDir = baseDir;
	}

	@Override
	protected void configure() {

	}

	@Provides
	@Singleton
	PersonNameMatcher providePersonNameMatcher() {
		PersonNameMatcher namesImporter = new PersonNameMatcher();
		try {
			namesImporter.load(new File(baseDir, "names/names.txt"));
			namesImporter
					.loadLastNames(new File(baseDir, "names/lastname.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return namesImporter;
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
			orgs.load(new File(baseDir, "organizations"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return orgs;
	}
}
