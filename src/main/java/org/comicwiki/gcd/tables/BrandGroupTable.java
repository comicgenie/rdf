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
import org.comicwiki.IRI;
import org.comicwiki.Join;
import org.comicwiki.TableRow;
import org.comicwiki.ThingFactory;
import org.comicwiki.gcd.tables.joinrules.BrandGroupAndBrandEmblemRule;
import org.comicwiki.gcd.tables.joinrules.BrandGroupAndBrandRule;
import org.comicwiki.model.schema.Brand;

import com.google.inject.Inject;

@Join(value = BrandEmblemGroupTable.class, withRule = BrandGroupAndBrandEmblemRule.class)
@Join(value = BrandTable.class, withRule = BrandGroupAndBrandRule.class)
public class BrandGroupTable extends BaseTable<BrandGroupTable.BrandGroupRow> {

	private static final class Columns {
		public static final Column[] ALL_COLUMNS = new Column[] {
				new Column("id"), new Column("name"), new Column("year_began"),
				new Column("year_ended"), new Column("notes"),
				new Column("url"), new Column("modified"),
				new Column("parent_id") };

		public static final int ID = 0;

		public static final int MODIFIED = 6;

		public static final int NAME = 1;

		public static final int NOTES = 4;

		/**
		 * gcd_publisher.id
		 */
		public static final int PARENT_ID = 7;

		public static final int URL = 5;

		public static final int YEAR_BEGAN = 2;

		public static final int YEAR_ENDED = 3;
	}

	public class BrandGroupRow extends TableRow<Brand> {

		public Brand instance = create(thingFactory);

		public Date modified;

		public String name;

		public String notes;

		public int parentId;// publisher

		public String url;

		public int yearBegan;

		public int yearEnded;

		public int fkBrandId;

		public IRI brandInstanceID;
	}

	private static final String sInputTable = "gcd_brand_group";

	private static final String sParquetName = sInputTable + ".parquet";

	private ThingFactory thingFactory;

	@Inject
	public BrandGroupTable(SQLContext sqlContext, ThingFactory thingFactory) {
		super(sqlContext, sParquetName);
		this.thingFactory = thingFactory;
	}

	@Override
	public BrandGroupRow process(Row row) throws IOException {
		BrandGroupRow brandRow = new BrandGroupRow();

		brandRow.modified = row.getTimestamp(Columns.MODIFIED);
		brandRow.name = row.getString(Columns.NAME);
		brandRow.notes = row.getString(Columns.NOTES);
		brandRow.url = row.getString(Columns.URL);

		if (!row.isNullAt(Columns.PARENT_ID)) {
			brandRow.parentId = row.getInt(Columns.PARENT_ID);
		}

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
}
