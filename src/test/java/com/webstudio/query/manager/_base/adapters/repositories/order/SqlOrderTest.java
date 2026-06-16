package com.webstudio.query.manager._base.adapters.repositories.order;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.webstudio.query.manager._base.application.sort.SortDirection;

class SqlOrderTest {

	@Test
	void returnsEmptyStringWhenNull() {
		assertEquals("", new SqlOrder(null).toSql());
	}

	@Test
	void returnsEmptyStringWhenEmpty() {
		assertEquals("", new SqlOrder(List.of()).toSql());
	}

	@Test
	void rendersSingleItemWithoutLeadingSpace() {
		SqlOrder order = new SqlOrder(List.of(new SqlOrderItem("u.name", SortDirection.ASC)));
		assertEquals("ORDER BY u.name ASC", order.toSql());
	}

	@Test
	void rendersMultipleItemsSeparatedByComma() {
		SqlOrder order = new SqlOrder(
				List.of(new SqlOrderItem("u.name", SortDirection.ASC), new SqlOrderItem("u.id", SortDirection.DESC)));
		assertEquals("ORDER BY u.name ASC, u.id DESC", order.toSql());
	}
}
