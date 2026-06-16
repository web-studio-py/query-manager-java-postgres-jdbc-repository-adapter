package com.webstudio.query.manager._base.adapters.repositories.select;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

class SqlSelectTest {

	@Test
	void rendersSingleItem() {
		SqlSelect select = new SqlSelect(List.of(new SqlSelectItem("u.id", "id")));
		assertEquals("SELECT u.id AS id", select.toSql());
	}

	@Test
	void rendersMultipleItemsSeparatedByComma() {
		SqlSelect select = new SqlSelect(List.of(new SqlSelectItem("u.id", "id"), new SqlSelectItem("u.name", null)));
		assertEquals("SELECT u.id AS id, u.name", select.toSql());
	}

	@Test
	void rejectsNullItems() {
		assertThrows(IllegalArgumentException.class, () -> new SqlSelect(null));
	}

	@Test
	void rejectsEmptyItems() {
		assertThrows(IllegalArgumentException.class, () -> new SqlSelect(List.of()));
	}
}
