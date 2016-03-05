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
package org.comicwiki.gcd.tables;

import java.io.IOException;
import java.net.URI;
import java.util.Date;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.comicwiki.BaseTable;
import org.comicwiki.Join;
import org.comicwiki.TableRow;
import org.comicwiki.ThingFactory;
import org.comicwiki.model.Instant;
import org.comicwiki.model.schema.Country;
import org.comicwiki.model.schema.Organization;

import com.google.common.base.Strings;
import com.google.inject.Inject;

@Join(value = CountryTable.class, leftKey = "fkCountryId", leftField = "country")
@Join(value = PublisherTable.class, leftKey = "fkParentId", leftField = "parentOrganization")
public class IndiciaPublisherTable extends
		BaseTable<IndiciaPublisherTable.IndiciaPublisherRow> {

	private static final class Columns {
		public static final Column[] ALL_COLUMNS = new Column[] {
				new Column("id"), new Column("name"), new Column("country_id"),
				new Column("year_began"), new Column("year_ended"),
				new Column("notes"), new Column("url"), new Column("modified"),
				new Column("parent_id") };

		/**
		 * gcd_country.id
		 */
		public static final int COUNTRY_ID = 2;

		public static final int ID = 0;

		public static final int MODIFIED = 7;

		public static final int NAME = 1;

		public static final int NOTES = 5;

		/**
		 * gcd_publisher.id
		 */
		public static final int PARENT_ID = 8;

		public static final int URL = 6;

		public static final int YEAR_BEGAN = 3;

		public static final int YEAR_ENDED = 4;
	}

	public static class IndiciaPublisherRow extends TableRow<Organization> {

		public Country country;
		
		/**
		 * gcd_country.id
		 */
		public int fkCountryId;
		
		/**
		 * gcd_publisher.id
		 * map to Organization.parentOrganization
		 */
		public int fkParentId;
		
		public Organization instance = create(thingFactory);

		public Date modified;

		public String name;

		public String notes;

		public Organization parentOrganization;

		public String url;

		public Integer yearBegan;

		public Integer yearEnded;
	}

	private static final String sInputTable = "gcd_publisher";

	private static final String sParquetName = sInputTable + ".parquet";

	private static ThingFactory thingFactory;

	@Inject
	public IndiciaPublisherTable(SQLContext sqlContext, ThingFactory thingFactory) {
		super(sqlContext, sParquetName);
		IndiciaPublisherTable.thingFactory = thingFactory;
	}

	@Override
	public IndiciaPublisherRow process(Row row) throws IOException {
		IndiciaPublisherRow publisherRow = new IndiciaPublisherRow();
		if (!row.isNullAt(Columns.COUNTRY_ID)) {
			publisherRow.fkCountryId = row.getInt(Columns.COUNTRY_ID);
		}

		publisherRow.modified = row.getTimestamp(Columns.MODIFIED);
		publisherRow.name = row.getString(Columns.NAME);
		publisherRow.instance.name = publisherRow.name;
		publisherRow.notes = row.getString(Columns.NOTES);
		publisherRow.url = row.getString(Columns.URL);
		if (!row.isNullAt(Columns.YEAR_BEGAN)) {
			publisherRow.yearBegan = row.getInt(Columns.YEAR_BEGAN);
		}
		if (!row.isNullAt(Columns.YEAR_ENDED)) {
			publisherRow.yearEnded = row.getInt(Columns.YEAR_ENDED);
		}
		if (!row.isNullAt(Columns.PARENT_ID)) {
			publisherRow.fkParentId = row.getInt(Columns.PARENT_ID);
		}

		if (!row.isNullAt(Columns.ID)) {
			publisherRow.id = row.getInt(Columns.ID);
			add(publisherRow);
		}
		return publisherRow;
	}

	@Override
	public void saveToParquetFormat(String jdbcUrl) {
		super.saveToParquetFormat(sInputTable, Columns.ALL_COLUMNS, jdbcUrl);
	}

	@Override
	protected void transform(IndiciaPublisherRow row) {
		super.transform(row);
		
		Organization publisher = row.instance;
		publisher.name = row.name;
		if(row.country != null) {
			publisher.location = row.country.instanceId;
		}
			
		if(row.yearBegan != null) {
			Instant began = thingFactory.create(Instant.class);
			began.year = row.yearBegan;
			publisher.foundingDate = began.instanceId;			
		}

		if(row.yearEnded != null) {
			Instant ended = thingFactory.create(Instant.class);
			ended.year = row.yearEnded;
			publisher.dissolutionDate = ended.instanceId;		
		}
		
		if(!Strings.isNullOrEmpty(row.notes)) {
			publisher.description.add(row.notes);
		}
		
		if(row.parentOrganization != null) {
			publisher.parentOrganization = row.parentOrganization.instanceId;
			row.parentOrganization.subOrganization.add(row.instance.instanceId);
		}
		
		if(!Strings.isNullOrEmpty(row.url)) {
			publisher.urls.add(URI.create(row.url));
		}
		
	}
}
