package com.webstudio.query.manager._base.adapters.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SqlQueryTest {

	@Test
	void joinsPartsWithSingleSpace() {
		assertEquals("SELECT a FROM t", SqlQuery.join("SELECT a", "FROM t"));
	}

	@Test
	void skipsNullParts() {
		assertEquals("SELECT a FROM t", SqlQuery.join("SELECT a", null, "FROM t"));
	}

	@Test
	void skipsBlankParts() {
		assertEquals("SELECT a FROM t", SqlQuery.join("SELECT a", "", "   ", "FROM t"));
	}

	@Test
	void returnsEmptyStringWhenNoUsableParts() {
		assertEquals("", SqlQuery.join(null, "", "   "));
	}

	@Test
	void assemblesFullStatementWithoutDoubleOrEdgeSpaces() {
		String sql = SqlQuery.join("SELECT u.id AS id", "FROM users u", "INNER JOIN accounts a ON a.user_id = u.id",
				"WHERE u.status = :p0", "ORDER BY u.id ASC", "LIMIT 10", "OFFSET 20");
		assertEquals("SELECT u.id AS id FROM users u INNER JOIN accounts a ON a.user_id = u.id "
				+ "WHERE u.status = :p0 ORDER BY u.id ASC LIMIT 10 OFFSET 20", sql);
	}
}
