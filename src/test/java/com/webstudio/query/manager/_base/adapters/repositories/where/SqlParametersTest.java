package com.webstudio.query.manager._base.adapters.repositories.where;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

class SqlParametersTest {

	@Test
	void bindReturnsSequentialPlaceholdersWithLeadingColon() {
		SqlParameters params = new SqlParameters();
		assertEquals(":p0", params.bind("a"));
		assertEquals(":p1", params.bind("b"));
		assertEquals(":p2", params.bind("c"));
	}

	@Test
	void valuesAreKeyedByNameWithoutColonInBindingOrder() {
		SqlParameters params = new SqlParameters();
		params.bind("a");
		params.bind("b");

		assertEquals(List.of("p0", "p1"), List.copyOf(params.values().keySet()));
		assertEquals("a", params.values().get("p0"));
		assertEquals("b", params.values().get("p1"));
	}
}
