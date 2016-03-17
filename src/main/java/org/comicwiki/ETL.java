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

import com.google.inject.Inject;

final class ETL {

	private static final Logger LOG = Logger.getLogger("ETL");
	
	private WikiTableInjector injector;
	
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
		for (BaseTable<?> table : injector.getTables()) {
			if (table != null) {
				table.saveToParquetFormat(jdbcUrl);
			}
		}
	}
	
	protected void processTables() throws Exception {
		BaseTable<TableRow<?>>[] tables = injector.getTables();
		injector.destroy();
		injector = null;
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
					table.clear();
				} catch (Exception e) {
					LOG.severe("Transform failure: " + table.datasourceName
							+ ", " + e.getMessage());
					e.printStackTrace();
				}
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
		
		processTables();
		
		if (resourceIds.exists()) {
			resourceIDCache.loadResourceIDs(resourceIds);
		}

		LOG.info("Assign resource IDs");
		thingCache.assignResourceIDs();
		//thingCache.clear();
		
		LOG.info("Export resource IDs: size = " + resourceIds.length());
		resourceIDCache.exportResourceIDs(resourceIds);
		resourceIDCache.clear();
		
		LOG.info("Export to repositories");
		thingCache.exportToRepositories();
		thingCache.clear();
		
		for (Repository<?> repo : repositories.getRepositories()) {
			LOG.info("Export Repository: " + repo.getName());
			repo.transform();
			try {
			//	repo.save(new File(outputDir, repo.getName() + ".json"),
			//			DataFormat.JSON);
				repo.save(new File(outputDir, repo.getName() + ".ttl"),
						DataFormat.TURTLE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void setInjector(WikiTableInjector injector) {
		this.injector = injector;
	}
}
