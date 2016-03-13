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
import org.comicwiki.TableRow;
import org.comicwiki.model.ThingInItself;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Maps relation between a brand and a brand group
 */
@Singleton
public class BrandEmblemGroupTable extends
		BaseTable<BrandEmblemGroupTable.BrandEmblemGroupRow> {

	public static class BrandEmblemGroupRow extends TableRow<ThingInItself> {

		public Integer brandId;

		public Integer brandGroupId;
	}

	public static class Columns {

		public static final Column[] ALL_COLUMNS = new Column[] {
				new Column("id"), new Column("brand_id"),
				new Column("brandgroup_id") };
		
		public static final Integer BRAND_ID = 1;

		public static final int ID = 0;

		public static final Integer BRAND_GROUP_ID = 2;

	}

	private static final String sInputTable = "gcd_brand_emblem_group";

	private static final String sParquetName = sInputTable + ".parquet";

	@Inject
	public BrandEmblemGroupTable(SQLContext sqlContext) {
		super(sqlContext, sParquetName);
	}

	@Override
	public BrandEmblemGroupRow process(Row row) throws IOException {
		BrandEmblemGroupRow groupRow = new BrandEmblemGroupRow();

		if (!row.isNullAt(Columns.BRAND_GROUP_ID)) {
			groupRow.brandGroupId = row.getInt(Columns.BRAND_GROUP_ID);
		}

		if (!row.isNullAt(Columns.BRAND_ID)) {
			groupRow.brandId = row.getInt(Columns.BRAND_ID);
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
}
