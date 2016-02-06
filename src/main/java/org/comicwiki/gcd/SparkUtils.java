package org.comicwiki.gcd;

import java.text.MessageFormat;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SQLContext;

public final class SparkUtils {

	private static final String jdbcPattern = "jdbc:mysql://localhost:3306/{2}?useSSL=false&user={0}&password={1}";

	public static String createJDBCUrl(String username, String password,
			String database) {
		return MessageFormat.format(jdbcPattern, username, password, database);
	}

	public static SQLContext createLocalContext() {
		SparkConf conf = new SparkConf().setAppName("GCDB").setMaster("local");
		return new SQLContext(new JavaSparkContext(conf));
	}
}
