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

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.comicwiki.BaseTable;
import org.comicwiki.model.BrandUse;

import com.google.inject.Inject;

public class BrandUseTable extends BaseTable<BrandUseTable.BrandUseRow> {

	public static class BrandUseRow extends TableRow<BrandUse> {

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

	@Inject
	public BrandUseTable(SQLContext sqlContext) {
		super(sqlContext, sParquetName);
	}

	@Override
	public BrandUseRow process(Row row) throws IOException {
		BrandUseRow groupRow = new BrandUseRow();
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
}
