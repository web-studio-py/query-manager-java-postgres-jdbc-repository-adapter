package com.webstudio.query.manager._base.adapters.repositories.from;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

class SqlFromTest {

	@Test
	void rendersSingleItemWithoutLeadingSpace() {
		assertEquals("FROM users u", new SqlFrom(List.of(new SqlFromItem("users", "u"))).toSql());
	}

	@Test
	void rendersMultipleItemsSeparatedByComma() {
		SqlFrom from = new SqlFrom(List.of(new SqlFromItem("users", "u"), new SqlFromItem("accounts", "a")));
		assertEquals("FROM users u, accounts a", from.toSql());
	}

	@Test
	void rejectsNullItems() {
		assertThrows(IllegalArgumentException.class, () -> new SqlFrom(null));
	}

	@Test
	void rejectsEmptyItems() {
		assertThrows(IllegalArgumentException.class, () -> new SqlFrom(List.of()));
	}
}
