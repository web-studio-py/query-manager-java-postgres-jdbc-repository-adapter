package com.webstudio.query.manager._base.adapters.repositories.from;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class SqlFromItemTest {

	@Test
	void rendersExpressionWithAlias() {
		assertEquals("users u", new SqlFromItem("users", "u").toSql());
	}

	@Test
	void rendersExpressionWithoutAliasWhenAliasNull() {
		assertEquals("users", new SqlFromItem("users", null).toSql());
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = "   ")
	void rejectsBlankExpression(String expression) {
		assertThrows(IllegalArgumentException.class, () -> new SqlFromItem(expression, "u"));
	}
}
