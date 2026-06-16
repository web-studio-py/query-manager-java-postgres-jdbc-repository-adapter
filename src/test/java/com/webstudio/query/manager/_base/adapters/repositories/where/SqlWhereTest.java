package com.webstudio.query.manager._base.adapters.repositories.where;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.webstudio.query.manager._base.application.filter.FilterOperator;
import com.webstudio.query.manager._base.application.filter.LogicalOperator;

class SqlWhereTest {

	private static SqlWhereCriteria<String, String> criteria(String expression, String parameterName, String value) {
		return new SqlWhereCriteria<>(expression, FilterOperator.EQUAL, false, List.of(parameterName), List.of(value),
				v -> v);
	}

	@Test
	void rendersClauseWithoutLeadingSpace() {
		SqlWhere where = new SqlWhere(criteria("u.status", ":p0", "ACTIVE"));
		assertEquals("WHERE u.status = :p0", where.toSql());
	}

	@Test
	void extractsParametersFromSingleCriteria() {
		SqlWhere where = new SqlWhere(criteria("u.status", ":p0", "ACTIVE"));
		assertEquals(Map.of(":p0", "ACTIVE"), where.getParameters());
	}

	@Test
	void extractsParametersFromNestedGroups() {
		SqlWhereGroup group = new SqlWhereGroup(LogicalOperator.AND,
				List.of(criteria("a", ":p0", "1"), criteria("b", ":p1", "2")));
		SqlWhere where = new SqlWhere(group);

		assertEquals("WHERE (a = :p0 AND b = :p1)", where.toSql());
		assertEquals(Map.of(":p0", "1", ":p1", "2"), where.getParameters());
	}

	@Test
	void extractsAllParametersFromMultiValueCriteria() {
		SqlWhereCriteria<String, String> in = new SqlWhereCriteria<>("u.status", FilterOperator.IN, false,
				List.of(":p0", ":p1"), List.of("A", "B"), v -> v);
		SqlWhere where = new SqlWhere(in);

		assertEquals("WHERE u.status IN (:p0, :p1)", where.toSql());
		assertEquals(Map.of(":p0", "A", ":p1", "B"), where.getParameters());
	}

	@Test
	void rejectsNullRootNode() {
		assertThrows(IllegalArgumentException.class, () -> new SqlWhere(null));
	}
}
