package com.webstudio.query.manager._base.adapters.repositories.where;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.webstudio.query.manager._base.application.filter.FilterOperator;

class SqlWhereCriteriaTest {

	@Test
	void rendersValueBearingCriteria() {
		SqlWhereCriteria<String, String> criteria = new SqlWhereCriteria<>("u.status", FilterOperator.EQUAL, false,
				List.of(":p0"), List.of("ACTIVE"), value -> value);
		assertEquals("u.status = :p0", criteria.toSql());
		assertEquals(Map.of(":p0", "ACTIVE"), criteria.getParameters());
	}

	@Test
	void rendersNullAwareCriteria() {
		SqlWhereCriteria<String, String> criteria = new SqlWhereCriteria<>("u.deleted_at", FilterOperator.IS_NULL,
				false, List.of(), List.of(), value -> value);
		assertEquals("u.deleted_at IS NULL", criteria.toSql());
		assertTrue(criteria.getParameters().isEmpty());
	}

	@Test
	void appliesValueConverterToEachParameter() {
		SqlWhereCriteria<Integer, String> criteria = new SqlWhereCriteria<>("u.age", FilterOperator.IN, false,
				List.of(":p0", ":p1"), List.of(18, 21), String::valueOf);
		assertEquals("u.age IN (:p0, :p1)", criteria.toSql());
		assertEquals(Map.of(":p0", "18", ":p1", "21"), criteria.getParameters());
	}

	@Test
	void rendersBetweenWithTwoParameters() {
		SqlWhereCriteria<Integer, Integer> criteria = new SqlWhereCriteria<>("u.age", FilterOperator.BETWEEN, false,
				List.of(":p0", ":p1"), List.of(18, 65), value -> value);
		assertEquals("u.age BETWEEN :p0 AND :p1", criteria.toSql());
		assertEquals(Map.of(":p0", 18, ":p1", 65), criteria.getParameters());
	}

	@Test
	void wrapsAndEscapesLikePatternForContains() {
		SqlWhereCriteria<String, String> criteria = new SqlWhereCriteria<>("u.code", FilterOperator.CONTAINS, false,
				List.of(":p0"), List.of("50%_x"), value -> value);
		assertEquals("u.code LIKE :p0 ESCAPE '\\'", criteria.toSql());
		assertEquals(Map.of(":p0", "%50\\%\\_x%"), criteria.getParameters());
	}

	@Test
	void rendersCaseInsensitiveContains() {
		SqlWhereCriteria<String, String> criteria = new SqlWhereCriteria<>("u.name", FilterOperator.CONTAINS, true,
				List.of(":p0"), List.of("ann"), value -> value);
		assertEquals("u.name ILIKE :p0 ESCAPE '\\'", criteria.toSql());
		assertEquals(Map.of(":p0", "%ann%"), criteria.getParameters());
	}

	@Test
	void rejectsBlankExpression() {
		assertThrows(IllegalArgumentException.class, () -> new SqlWhereCriteria<>(" ", FilterOperator.EQUAL, false,
				List.of(":p0"), List.of("x"), value -> value));
	}

	@Test
	void rejectsNullOperator() {
		assertThrows(IllegalArgumentException.class,
				() -> new SqlWhereCriteria<>("u.status", null, false, List.of(":p0"), List.of("x"), value -> value));
	}

	@Test
	void rejectsMismatchedParameterAndValueCounts() {
		assertThrows(IllegalArgumentException.class, () -> new SqlWhereCriteria<>("u.status", FilterOperator.IN, false,
				List.of(":p0"), List.of("x", "y"), value -> value));
	}

	@Test
	void rejectsNullValueConverter() {
		assertThrows(IllegalArgumentException.class, () -> new SqlWhereCriteria<>("u.status", FilterOperator.EQUAL,
				false, List.of(":p0"), List.of("x"), null));
	}
}
