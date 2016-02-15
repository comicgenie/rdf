package org.comicwiki.gcd;

import static org.junit.Assert.*;

import org.junit.Test;

public class SparkUtilsTest {

	@Test
	public void jdbcUrl() throws Exception {
		String url = SparkUtils.createJDBCUrl("name", "pw", "db");
		assertEquals(
				"jdbc:mysql://localhost:3306/db?useSSL=false&user=name&password=pw",
				url);
	}

	@Test(expected = IllegalArgumentException.class)
	public void jdbcUrlNoUser() throws Exception {
		SparkUtils.createJDBCUrl(null, "pw", "db");
	}

	@Test(expected = IllegalArgumentException.class)
	public void jdbcUrlNoPassword() throws Exception {
		SparkUtils.createJDBCUrl("name", null, "db");
	}

	@Test(expected = IllegalArgumentException.class)
	public void jdbcUrlNoDB() throws Exception {
		SparkUtils.createJDBCUrl("name", "pw", null);
	}
}
