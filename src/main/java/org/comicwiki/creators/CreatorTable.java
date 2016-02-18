package org.comicwiki.creators;

import java.io.IOException;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.comicwiki.BaseTable;
import org.comicwiki.TableFormat;
import org.comicwiki.gcd.tables.TableRow;
import org.comicwiki.model.schema.Person;

public class CreatorTable extends BaseTable<CreatorTable.CreatorRow> {

	private static final class Columns {
		public static final Column[] ALL_COLUMNS = new Column[] {
				new Column("fullName"), new Column("givenName"),
				new Column("familyName"), new Column("middleName") };

		public static final int FULL_NAME = 0;

		public static final int GIVEN_NAME = 1;

		public static final int FAMILY_NAME = 2;

		public static final int MIDDLE_NAME = 3;
	}

	public CreatorTable(SQLContext sqlContext, String datasourceName) {
		super(sqlContext, datasourceName, TableFormat.JSON);
	}

	public static final class CreatorRow extends TableRow<Person> {
		
		
	}

	@Override
	protected CreatorRow process(Row row) throws IOException {
		return null;
	}

	@Override
	public void saveToParquetFormat(String jdbcUrl) {
		// noop
	}
}
