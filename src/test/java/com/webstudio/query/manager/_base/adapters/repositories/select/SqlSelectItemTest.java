package com.webstudio.query.manager._base.adapters.repositories.select;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class SqlSelectItemTest {

	@Test
	void rendersExpressionWithAlias() {
		assertEquals("u.name AS name", new SqlSelectItem("u.name", "name").toSql());
	}

	@Test
	void rendersExpressionWithoutAliasWhenAliasNull() {
		assertEquals("u.name", new SqlSelectItem("u.name", null).toSql());
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = "   ")
	void rendersExpressionWithoutAliasWhenAliasBlank(String alias) {
		assertEquals("u.name", new SqlSelectItem("u.name", alias).toSql());
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = "   ")
	void rejectsBlankExpression(String expression) {
		assertThrows(IllegalArgumentException.class, () -> new SqlSelectItem(expression, "a"));
	}
}
