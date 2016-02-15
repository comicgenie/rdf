/*******************************************************************************
 * See the NOTICE file distributed with this work for additional 
 * information regarding copyright ownership. ComicGenie licenses this 
 * file to you under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License.  
 * You may obtain a copy of the License at
 *  
 *     http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.comicwiki;

import java.io.File;
import java.lang.reflect.Constructor;

import org.apache.spark.sql.SQLContext;
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
	private static final Class<? extends BaseTable<?>>[] tableClasses = new Class[] {
			BrandEmblemGroupTable.class, BrandGroupTable.class,
			BrandTable.class, BrandUseTable.class, CountryTable.class,
			GenresTable.class, IndiciaPublisherTable.class,
			IssueReprintTable.class, IssueTable.class, LanguageTable.class,
			PublisherTable.class, ReprintFromIssueTable.class,
			ReprintTable.class, ReprintToIssueTable.class,
			SeriesBondTable.class, SeriesBondTypeTable.class,
			SeriesPublicationTypeTable.class, SeriesTable.class,
			StoryTable.class, StoryTypeTable.class };

	private static BaseTable<?>[] createTables(SQLContext context) throws Exception {
		BaseTable<?>[] tables = new BaseTable<?>[tableClasses.length];
		for (int i = 0; i < tableClasses.length; i++) {
			Constructor<?> constructor = tableClasses[i]
					.getConstructor(SQLContext.class);
			tables[i]  = (BaseTable<?>) constructor.newInstance(context);
		}
		return tables;	
	}

	private ThingCache thingCache;
	
	public ETL(ThingCache thingCache) {
		this.thingCache = thingCache;
	}
	
	public void fromRDB(SQLContext context, String jdbcUrl)
			throws Exception {
		for(BaseTable<?> table : createTables(context)) {
			table.saveToParquetFormat(jdbcUrl);
		}
	}

	public void process(SQLContext context, File resourceIds, File outputDir)
			throws Exception {
		if (resourceIds.exists()) {
			ThingCache.loadResourceIDs(resourceIds);
		}

		BaseTable<?>[] tables = createTables(context);

		for (BaseTable<?> table : tables) {
			table.extract();
			table.join(tables);
			table.tranform();
		}

		thingCache.assignResourceIDs();
		thingCache.load();
		for(Repository<?> repo : Repositories.getRepositories()) {
			repo.transform();
			String repoName = repo.getClass().getSimpleName();
			repo.load(new File(outputDir,  repoName +".json"), DataFormat.JSON);
			repo.load(new File(outputDir,  repoName +".ttl"), DataFormat.TURTLE);
		}
		ThingCache.exportResourceIDs(resourceIds);
	}
}
