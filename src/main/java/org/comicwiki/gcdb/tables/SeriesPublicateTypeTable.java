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

import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

public class SeriesPublicateTypeTable extends
		BaseTable<SeriesPublicateTypeTable.SeriesPublicateTypeRow> {
	public static class SeriesPublicateTypeRow extends TableRow {
		public String name;
	}

	public SeriesPublicateTypeTable(SQLContext sqlContext, String datasourceName) {
		super(sqlContext, datasourceName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void join(SeriesPublicateTypeRow row) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public SeriesPublicateTypeRow process(Row row) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveToParquetFormat(String jdbcUrl) {
		// TODO Auto-generated method stub

	}
}
