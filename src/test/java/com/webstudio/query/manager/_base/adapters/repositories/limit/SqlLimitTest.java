package com.webstudio.query.manager._base.adapters.repositories.limit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class SqlLimitTest {

	@Test
	void rendersLimitWithoutLeadingSpace() {
		assertEquals("LIMIT 10", new SqlLimit(10).toSql());
	}

	@Test
	void allowsZero() {
		assertEquals("LIMIT 0", new SqlLimit(0).toSql());
	}

	@Test
	void rejectsNegativeLimit() {
		assertThrows(IllegalArgumentException.class, () -> new SqlLimit(-1));
	}
}
