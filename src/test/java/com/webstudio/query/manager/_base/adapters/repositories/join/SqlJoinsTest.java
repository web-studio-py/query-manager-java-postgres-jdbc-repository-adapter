package com.webstudio.query.manager._base.adapters.repositories.join;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

class SqlJoinsTest {

	@Test
	void returnsEmptyStringWhenNull() {
		assertEquals("", new SqlJoins(null).toSql());
	}

	@Test
	void returnsEmptyStringWhenEmpty() {
		assertEquals("", new SqlJoins(List.of()).toSql());
	}

	@Test
	void joinsMultipleJoinsWithSingleSpace() {
		SqlJoins joins = new SqlJoins(List.of(new SqlJoin(SqlJoinType.INNER, "accounts", "a", "a.user_id = u.id"),
				new SqlJoin(SqlJoinType.LEFT, "roles", "r", "r.id = u.role_id")));
		assertEquals("INNER JOIN accounts a ON a.user_id = u.id LEFT JOIN roles r ON r.id = u.role_id", joins.toSql());
	}
}
