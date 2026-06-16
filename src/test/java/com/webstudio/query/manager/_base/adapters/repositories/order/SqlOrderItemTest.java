package com.webstudio.query.manager._base.adapters.repositories.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.webstudio.query.manager._base.application.sort.SortDirection;

class SqlOrderItemTest {

	@Test
	void rendersAscending() {
		assertEquals("u.name ASC", new SqlOrderItem("u.name", SortDirection.ASC).toSql());
	}

	@Test
	void rendersDescending() {
		assertEquals("u.created_at DESC", new SqlOrderItem("u.created_at", SortDirection.DESC).toSql());
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = "   ")
	void rejectsBlankExpression(String expression) {
		assertThrows(IllegalArgumentException.class, () -> new SqlOrderItem(expression, SortDirection.ASC));
	}

	@Test
	void rejectsNullDirection() {
		assertThrows(IllegalArgumentException.class, () -> new SqlOrderItem("u.name", null));
	}
}
