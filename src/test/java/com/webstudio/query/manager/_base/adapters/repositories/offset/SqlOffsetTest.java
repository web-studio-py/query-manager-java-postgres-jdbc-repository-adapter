package com.webstudio.query.manager._base.adapters.repositories.offset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class SqlOffsetTest {

	@Test
	void rendersOffsetWithoutLeadingSpace() {
		assertEquals("OFFSET 20", new SqlOffset(20).toSql());
	}

	@Test
	void allowsZero() {
		assertEquals("OFFSET 0", new SqlOffset(0).toSql());
	}

	@Test
	void rejectsNegativeOffset() {
		assertThrows(IllegalArgumentException.class, () -> new SqlOffset(-1));
	}
}
