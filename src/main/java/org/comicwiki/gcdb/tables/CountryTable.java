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
package org.comicwiki.gcdb.tables;

import java.io.IOException;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.comicwiki.gcdb.repositories.CountryRepository;
import org.comicwiki.model.schema.Country;

public class CountryTable extends BaseTable<CountryTable.CountryRow> {

	public static class Columns {

		public static final Column[] ALL_COLUMNS = new Column[] {
				new Column("id"), new Column("code"), new Column("name") };
		public static final int CODE = 1;
		public static final int ID = 0;

		public static final int NAME = 2;
	}

	public class CountryRow extends TableRow {

		public Country country;
	}

	private static final String sTableName = "gcd_country";

	private static final String sParquetName = sTableName + ".parquet";

	private CountryRepository countryRepository;

	public CountryTable(SQLContext sqlContext,
			CountryRepository countryRepository) {
		super(sqlContext, sParquetName);
		this.countryRepository = countryRepository;
	}

	@Override
	public void join(CountryRow row) throws IOException {
		// noop
	}

	@Override
	public CountryRow process(Row row) throws IOException {
		Country country = new Country();
		country.name = row.getString(Columns.CODE);

		// CountryNameField f = new CountryNameField();
		// country.alternateNames.add(f.process(row));

		countryRepository.add(country);

		CountryRow countryRow = new CountryRow();
		countryRow.id = row.getInt(Columns.CODE);
		countryRow.country = country;

		add(countryRow.id, countryRow);
		return countryRow;
	}

	@Override
	public void saveToParquetFormat(String jdbcUrl) {
		super.saveToParquetFormat(sTableName, Columns.ALL_COLUMNS, jdbcUrl);
	}
}
