package com.webstudio.query.manager._base.adapters.repositories.join;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class SqlJoinTest {

	@Test
	void rendersInnerJoinWithAliasAndOnWithoutLeadingSpace() {
		SqlJoin join = new SqlJoin(SqlJoinType.INNER, "accounts", "a", "a.user_id = u.id");
		assertEquals("INNER JOIN accounts a ON a.user_id = u.id", join.toSql());
	}

	@Test
	void rendersLeftJoinWithoutAlias() {
		SqlJoin join = new SqlJoin(SqlJoinType.LEFT, "accounts", null, "accounts.user_id = u.id");
		assertEquals("LEFT JOIN accounts ON accounts.user_id = u.id", join.toSql());
	}

	@Test
	void rendersCrossJoinWithoutOn() {
		SqlJoin join = new SqlJoin(SqlJoinType.CROSS, "accounts", "a", null);
		assertEquals("CROSS JOIN accounts a", join.toSql());
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = "   ")
	void rejectsBlankExpression(String expression) {
		assertThrows(IllegalArgumentException.class, () -> new SqlJoin(SqlJoinType.INNER, expression, "a", "x = y"));
	}

	@Test
	void rejectsCrossJoinWithOn() {
		assertThrows(IllegalArgumentException.class,
				() -> new SqlJoin(SqlJoinType.CROSS, "accounts", "a", "a.user_id = u.id"));
	}
}
