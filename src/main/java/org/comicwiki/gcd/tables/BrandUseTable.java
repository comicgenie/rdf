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
import java.util.Date;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.comicwiki.BaseTable;
import org.comicwiki.TableRow;
import org.comicwiki.ThingFactory;
import org.comicwiki.model.BrandUse;
import org.comicwiki.model.Instant;

import com.google.common.base.Strings;
import com.google.inject.Inject;

public class BrandUseTable extends BaseTable<BrandUseTable.BrandUseRow> {

	public static class BrandUseRow extends TableRow<BrandUse> {

		/**
		 * gcd_brand_emblem_group.id
		 */
		public int fkEmblemId;

		/**
		 * gcd_publisher.id
		 */
		public int fkPublisherId;

		public BrandUse instance = create(thingFactory);

		public Date modified;

		public String notes;

		public int yearBegan;

		public int yearEnded;
		
	}

	private static final class Columns {
		public static final Column[] ALL_COLUMNS = new Column[] {
				new Column("id"), new Column("publisher_id"),
				new Column("emblem_id"), new Column("year_began"),
				new Column("year_ended"), new Column("notes"),
				new Column("modified") };

		public static final int EMBLEM_ID = 2;

		public static final int ID = 0;

		public static final int MODIFIED = 6;

		public static final int NOTES = 5;

		public static final int PUBLISHER_ID = 1;

		public static final int YEAR_BEGAN = 3;

		public static final int YEAR_ENDED = 4;
	}

	private static final String sInputTable = "gcd_brand_use";

	private static final String sParquetName = sInputTable + ".parquet";

	private static ThingFactory thingFactory;

	@Inject
	public BrandUseTable(SQLContext sqlContext, ThingFactory thingFactory) {
		super(sqlContext, sParquetName);
		this.thingFactory = thingFactory;
	}

	@Override
	public BrandUseRow process(Row row) throws IOException {
		BrandUseRow groupRow = new BrandUseRow();
		groupRow.modified = row.getTimestamp(Columns.MODIFIED);
		groupRow.notes = row.getString(Columns.NOTES);

		if (!row.isNullAt(Columns.EMBLEM_ID)) {
			groupRow.fkEmblemId = row.getInt(Columns.EMBLEM_ID);
		}

		if (!row.isNullAt(Columns.PUBLISHER_ID)) {
			groupRow.fkPublisherId = row.getInt(Columns.PUBLISHER_ID);
		}

		if (!row.isNullAt(Columns.YEAR_BEGAN)) {
			groupRow.yearBegan = row.getInt(Columns.YEAR_BEGAN);
		}

		if (!row.isNullAt(Columns.YEAR_ENDED)) {
			groupRow.yearEnded = row.getInt(Columns.YEAR_ENDED);
		}

		if (!row.isNullAt(Columns.ID)) {
			groupRow.id = row.getInt(Columns.ID);
			add(groupRow);
		}
		return groupRow;
	}

	@Override
	public void saveToParquetFormat(String jdbcUrl) {
		super.saveToParquetFormat(sInputTable, Columns.ALL_COLUMNS, jdbcUrl);
	}

	@Override
	protected void transform(BrandUseRow row) {
		super.transform(row);
		if (row.yearBegan != 0) {
			Instant begin = thingFactory.create(Instant.class);
			begin.year = row.yearBegan;
			row.instance.begin = begin.instanceId;
		}

		if (row.yearEnded != 0) {
			Instant end = thingFactory.create(Instant.class);
			end.year = row.yearEnded;
			row.instance.end = end.instanceId;
		}

		if (!Strings.isNullOrEmpty(row.notes)) {
			row.instance.description.add(row.notes);
		}
	}
}
