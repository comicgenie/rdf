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
import java.util.logging.Logger;

import org.apache.spark.sql.SQLContext;
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

import com.google.inject.Inject;
import com.google.inject.Injector;

public class ETL {

	private static final Logger LOG = Logger.getLogger("ETL");

	@SuppressWarnings("unchecked")
	protected static final Class<? extends BaseTable<?>>[] tableClasses = new Class[] {
			BrandEmblemGroupTable.class, BrandGroupTable.class,
			BrandTable.class, BrandUseTable.class, CountryTable.class,
			IndiciaPublisherTable.class, IssueReprintTable.class,
			IssueTable.class, LanguageTable.class, PublisherTable.class,
			ReprintFromIssueTable.class, ReprintTable.class,
			ReprintToIssueTable.class, SeriesBondTable.class,
			SeriesBondTypeTable.class, SeriesPublicationTypeTable.class,
			SeriesTable.class, StoryTable.class, StoryTypeTable.class };

	protected static BaseTable<TableRow<?>>[] getTables(Injector injector)
			throws Exception {
		BaseTable<TableRow<?>>[] tables = new BaseTable[tableClasses.length];
		for (int i = 0; i < tableClasses.length; i++) {
			tables[i] = (BaseTable<TableRow<?>>) injector
					.getInstance(tableClasses[i]);
		}
		return tables;
	}
	private Injector injector;
	private Repositories repositories;
	private ResourceIDCache resourceIDCache;

	private SQLContext sqlContext;

	private ThingCache thingCache;

	@Inject
	public ETL(ThingCache thingCache, Repositories repositories,
			ResourceIDCache resourceIDCache, SQLContext sqlContext) {
		this.thingCache = thingCache;
		this.repositories = repositories;
		this.resourceIDCache = resourceIDCache;
		this.sqlContext = sqlContext;
	}

	public void fromRDB(String jdbcUrl) throws Exception {
		for (BaseTable<?> table : getTables(injector)) {
			if (table != null) {
				table.saveToParquetFormat(jdbcUrl);
			}
		}
	}

	public void process(File resourceIds, File outputDir) throws Exception {
		if (resourceIds == null) {
			throw new IllegalArgumentException("resourceIds file not specified");
		}

		if (outputDir == null) {
			throw new IllegalArgumentException("outputDir not specified");
		} else {
			outputDir.mkdirs();
		}

		if (resourceIds.exists()) {
			resourceIDCache.loadResourceIDs(resourceIds);
		}

		BaseTable<TableRow<?>>[] tables = getTables(injector);

		for (BaseTable<TableRow<?>> table : tables) {
			if (table != null) {
				try {
					LOG.info("Extracting table: " + table.datasourceName);
					table.extract();
				} catch (Exception e) {
					LOG.severe("Error extracting table: "
							+ table.datasourceName + ", " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
		if (sqlContext != null) {
			LOG.info("Stopping Spark Context");
			sqlContext.sparkContext().stop();
		}

		for (BaseTable<TableRow<?>> table : tables) {
			if (table != null) {
				try {
					LOG.info("Parsing table fields: " + table.datasourceName);
					table.parse();
				} catch (Exception e) {
					LOG.severe("Error parsing table: " + table.datasourceName
							+ ", " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
		for (BaseTable<TableRow<?>> table : tables) {
			if (table != null) {
				try {
					LOG.info("Joining table: " + table.datasourceName);
					table.joinTables(tables);
				} catch (Exception e) {
					LOG.severe("Error joining table: " + table.datasourceName
							+ ", " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
		for (BaseTable<TableRow<?>> table : tables) {
			if (table != null) {
				try {
					LOG.info("Transform table: " + table.datasourceName);
					table.tranform();
					table.getCache().clear();
				} catch (Exception e) {
					LOG.severe("Transform failure: " + table.datasourceName
							+ ", " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
		tables = null;
		
		LOG.info("Assign resource IDs");
		thingCache.assignResourceIDs();

		LOG.info("Export to repositories");
		thingCache.exportToRepositories();

		for (Repository<?> repo : repositories.getRepositories()) {
			LOG.info("Export Repository: " + repo.getName());
			repo.transform();
			try {
				repo.save(new File(outputDir, repo.getName() + ".json"),
						DataFormat.JSON);
				repo.save(new File(outputDir, repo.getName() + ".ttl"),
						DataFormat.TURTLE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		LOG.info("Export resource IDs: size = " + resourceIds.length());
		resourceIDCache.exportResourceIDs(resourceIds);
	}

	public void setInjector(Injector injector) {
		this.injector = injector;
	}
}
