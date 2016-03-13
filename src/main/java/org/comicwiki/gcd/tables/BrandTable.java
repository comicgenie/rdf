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
import java.net.URL;
import java.util.Date;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.comicwiki.BaseTable;
import org.comicwiki.TableRow;
import org.comicwiki.ThingFactory;
import org.comicwiki.model.Instant;
import org.comicwiki.model.schema.Brand;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class BrandTable extends BaseTable<BrandTable.BrandRow> {

	public static class BrandRow extends TableRow<Brand> {
		
		public Brand instance = create(thingFactory);
		
		public String name;

		public String notes;

		public String url;

		public Integer yearBegan;

		public Integer yearEnded;
		
		public Date modified;
	}

	private static final class Columns {
		public static final Column[] ALL_COLUMNS = new Column[] {
				new Column("id"), new Column("name"),
				new Column("year_began"), new Column("year_ended"),
				new Column("notes"), new Column("url"), new Column("modified") };

		public static final int ID = 0;

		public static final int MODIFIED = 6;

		public static final int NAME = 1;

		public static final int NOTES = 4;

		public static final int URL = 5;

		public static final int YEAR_BEGAN = 2;

		public static final int YEAR_ENDED = 3;
	}

	private static final String sInputTable = "gcd_brand";

	private static final String sParquetName = sInputTable + ".parquet";

	private static ThingFactory thingFactory;

	@Inject
	public BrandTable(SQLContext sqlContext, ThingFactory thingFactory) {
		super(sqlContext, sParquetName);
		BrandTable.thingFactory = thingFactory;
	}

	@Override
	public BrandRow process(Row row) throws IOException {
		BrandRow brandRow = new BrandRow();

		brandRow.modified = row.getTimestamp(Columns.MODIFIED);
		brandRow.name = row.getString(Columns.NAME);
		brandRow.instance.name = row.getString(Columns.NAME);
		
		brandRow.notes = row.getString(Columns.NOTES);
		brandRow.url = row.getString(Columns.URL);
		if (!row.isNullAt(Columns.YEAR_BEGAN)) {
			brandRow.yearBegan = row.getInt(Columns.YEAR_BEGAN);
		}
		if (!row.isNullAt(Columns.YEAR_ENDED)) {
			brandRow.yearEnded = row.getInt(Columns.YEAR_ENDED);
		}
			
		if (!row.isNullAt(Columns.ID)) {
			brandRow.id = row.getInt(Columns.ID);
			add(brandRow);
		}
		return brandRow;
	}

	@Override
	public void saveToParquetFormat(String jdbcUrl) {
		super.saveToParquetFormat(sInputTable, Columns.ALL_COLUMNS, jdbcUrl);
	}

	@Override
	protected void transform(BrandRow row) {
		super.transform(row);

		if (row.yearBegan != null) {
			Instant begin = thingFactory.create(Instant.class);
			begin.year = row.yearBegan;
			row.instance.startUseDate = begin.instanceId;
		}

		if (row.yearEnded != null) {
			Instant end = thingFactory.create(Instant.class);
			end.year = row.yearEnded;
			row.instance.endUseDate = end.instanceId;
		}
		
		if(!Strings.isNullOrEmpty(row.notes)) {			
			row.instance.addDescription(row.notes);
		}
		
		if(!Strings.isNullOrEmpty(row.url)) {			
			try {
				row.instance.addUrl(new URL(row.url));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
}
