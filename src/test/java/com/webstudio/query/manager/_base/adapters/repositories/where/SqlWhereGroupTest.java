package com.webstudio.query.manager._base.adapters.repositories.where;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.webstudio.query.manager._base.application.filter.FilterOperator;
import com.webstudio.query.manager._base.application.filter.LogicalOperator;

class SqlWhereGroupTest {

	private static SqlWhereCriteria<String, String> criteria(String expression, String parameterName) {
		return new SqlWhereCriteria<>(expression, FilterOperator.EQUAL, false, List.of(parameterName), List.of("x"),
				value -> value);
	}

	@Test
	void rendersSingleChildWrappedInParentheses() {
		SqlWhereGroup group = new SqlWhereGroup(LogicalOperator.AND, List.of(criteria("a", ":p0")));
		assertEquals("(a = :p0)", group.toSql());
	}

	@Test
	void joinsChildrenWithAnd() {
		SqlWhereGroup group = new SqlWhereGroup(LogicalOperator.AND,
				List.of(criteria("a", ":p0"), criteria("b", ":p1")));
		assertEquals("(a = :p0 AND b = :p1)", group.toSql());
	}

	@Test
	void joinsChildrenWithOr() {
		SqlWhereGroup group = new SqlWhereGroup(LogicalOperator.OR,
				List.of(criteria("a", ":p0"), criteria("b", ":p1")));
		assertEquals("(a = :p0 OR b = :p1)", group.toSql());
	}

	@Test
	void rendersNestedGroups() {
		SqlWhereGroup inner = new SqlWhereGroup(LogicalOperator.OR,
				List.of(criteria("b", ":p1"), criteria("c", ":p2")));
		SqlWhereGroup group = new SqlWhereGroup(LogicalOperator.AND, List.of(criteria("a", ":p0"), inner));
		assertEquals("(a = :p0 AND (b = :p1 OR c = :p2))", group.toSql());
	}

	@Test
	void rejectsNullLogicalOperator() {
		assertThrows(IllegalArgumentException.class, () -> new SqlWhereGroup(null, List.of(criteria("a", ":p0"))));
	}

	@Test
	void rejectsEmptyChildren() {
		assertThrows(IllegalArgumentException.class, () -> new SqlWhereGroup(LogicalOperator.AND, List.of()));
	}
}
