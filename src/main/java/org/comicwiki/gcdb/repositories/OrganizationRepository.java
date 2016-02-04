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
package org.comicwiki.gcdb.repositories;

import java.io.IOException;
import java.net.URI;
import java.util.Calendar;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.comicwiki.gcdb.tables.PublisherTable;
import org.comicwiki.model.schema.Organization;

public class OrganizationRepository extends BaseRepository<Organization> {

	@Override
	public void load() throws IOException {
		SparkConf conf = new SparkConf().setAppName("GCDB").setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		SQLContext sqlContext = new SQLContext(sc);
		DataFrame publishersFrame = sqlContext.read()
				.load(PublisherTable.PARQUET_NAME).limit(200);

		for (Row row : publishersFrame.collect()) {
			String publisherField = row.getString(PublisherTable.NAME);
			publisherField = publisherField.replaceAll("\\(.*?\\)", "")
					.replaceAll("\\[.*?\\]", "").trim();

			String[] names = null;
			if (publisherField.contains(";")) {
				names = publisherField.split(";");
			} else if (publisherField.contains("/")) {
				names = publisherField.split("/");
			} else {
				names = new String[] { publisherField };
			}
			for (String name : names) {
				Organization org = new Organization();
				org.name = name.trim().replaceFirst("Co$", "Company")
						.replaceFirst("Co\\.", "Company");

				if(!row.isNullAt(PublisherTable.YEAR_BEGAN)) {
					int yearBegin = row.getInt(PublisherTable.YEAR_BEGAN);
					Calendar calendar = Calendar.getInstance();
					calendar.set(Calendar.YEAR, yearBegin);	
					org.foundingDate = calendar.getTime();
				}
				
				if(!row.isNullAt(PublisherTable.YEAR_ENDED)) {
					int year = row.getInt(PublisherTable.YEAR_ENDED);
					Calendar calendar = Calendar.getInstance();
					calendar.set(Calendar.YEAR, year);	
					org.dissolutionDate = calendar.getTime();
				}

				String url = row.getString(PublisherTable.URL);
				if (url != null && !url.isEmpty()) {
					try {
						org.urls.add(URI.create(url));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				String description = row.getString(PublisherTable.NOTES);
				if (description != null && !description.isEmpty()) {
					org.description.add(description);
				}
				add(org);
			}

			// country_id
		}
	}

	@Override
	public Organization merge(Organization source, Organization target) {
		return target;
	}
}
