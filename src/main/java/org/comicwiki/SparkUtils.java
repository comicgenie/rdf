package org.comicwiki;

import java.text.MessageFormat;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SQLContext;

import com.google.common.base.Strings;

public final class SparkUtils {

	private static final String jdbcPattern = "jdbc:mysql://localhost:3306/{2}?useSSL=false&user={0}&password={1}";

	public static boolean isValidScheme(String jdbcUrl) {
		return !Strings.isNullOrEmpty(jdbcUrl)
				&& jdbcUrl.toLowerCase().startsWith("jdbc:mysql://");
	}

	public static String createJDBCUrl(String username, String password,
			String database) {
		if (Strings.isNullOrEmpty(username) || Strings.isNullOrEmpty(password)
				|| Strings.isNullOrEmpty(database)) {
			throw new IllegalArgumentException("missing parameter");
		}
		return MessageFormat.format(jdbcPattern, username, password, database);
	}

	public static SQLContext createLocalContext() {
		SparkConf conf = new SparkConf().setAppName("GCDB").setMaster("local")
				.set("spark.executor.memory", "2g");
		return new SQLContext(new JavaSparkContext(conf));
	}
}
