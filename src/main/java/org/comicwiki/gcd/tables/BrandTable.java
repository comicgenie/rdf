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
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.comicwiki.BaseTable;

import com.google.inject.Inject;

public class BrandTable extends BaseTable<BrandTable.BrandRow> {

	public static class BrandRow extends TableRow {
		public String name;

		public Collection<String> notes = new HashSet<>(3);

		public URI url;

		public Date yearBegan;

		public Date yearEnded;
	}
	
	private static final class Columns {
		public static final Column[] ALL_COLUMNS = new Column[] {
				new Column("id"), new Column("name"), new Column("parent_id"),
				new Column("year_began"), new Column("year_ended"),
				new Column("notes"), new Column("url"), new Column("modified")};

		public static final int ID = 0;

		public static final int MODIFIED = 7;

		public static final int NAME = 1;

		public static final int NOTES = 5;

		public static final int PARENT_ID = 2;

		public static final int URL = 6;
		
		public static final int YEAR_BEGAN = 3;

		public static final int YEAR_ENDED = 4;
	}

	private static final String sInputTable = "gcd_brand";

	private static final String sParquetName = sInputTable + ".parquet";

	@Inject
	public BrandTable(SQLContext sqlContext) {
		super(sqlContext, sParquetName);
	}

	@Override
	public BrandRow process(Row row) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveToParquetFormat(String jdbcUrl) {
		super.saveToParquetFormat(sInputTable, Columns.ALL_COLUMNS, jdbcUrl);
	}
}
