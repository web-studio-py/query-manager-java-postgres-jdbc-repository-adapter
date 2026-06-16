package com.webstudio.query.manager._base.adapters.repositories.where;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import com.webstudio.query.manager._base.application.filter.FilterOperator;

class SqlOperatorExpressionResolverTest {

	@ParameterizedTest
	@CsvSource({"EQUAL,                 '= :p0'", "NOT_EQUAL,             '<> :p0'", "GREATER_THAN,          '> :p0'",
			"GREATER_THAN_OR_EQUAL, '>= :p0'", "LESS_THAN,             '< :p0'", "LESS_THAN_OR_EQUAL,    '<= :p0'",
			"IN,                    'IN (:p0)'", "NOT_IN,                'NOT IN (:p0)'",})
	void resolvesSingleParameterOperators(FilterOperator operator, String expected) {
		assertEquals(expected, SqlOperatorExpressionResolver.resolve(operator, List.of(":p0"), false));
	}

	@ParameterizedTest
	@CsvSource({"IN,     'IN (:p0, :p1)'", "NOT_IN, 'NOT IN (:p0, :p1)'",})
	void resolvesMultiValueInOperators(FilterOperator operator, String expected) {
		assertEquals(expected, SqlOperatorExpressionResolver.resolve(operator, List.of(":p0", ":p1"), false));
	}

	@ParameterizedTest
	@CsvSource({"BETWEEN,     'BETWEEN :p0 AND :p1'", "NOT_BETWEEN, 'NOT BETWEEN :p0 AND :p1'",})
	void resolvesBetweenOperators(FilterOperator operator, String expected) {
		assertEquals(expected, SqlOperatorExpressionResolver.resolve(operator, List.of(":p0", ":p1"), false));
	}

	@ParameterizedTest
	@EnumSource(names = {"CONTAINS", "STARTS_WITH", "ENDS_WITH"})
	void resolvesLikeOperatorsCaseSensitive(FilterOperator operator) {
		assertEquals("LIKE :p0 ESCAPE '\\'", SqlOperatorExpressionResolver.resolve(operator, List.of(":p0"), false));
	}

	@ParameterizedTest
	@EnumSource(names = {"CONTAINS", "STARTS_WITH", "ENDS_WITH"})
	void resolvesLikeOperatorsCaseInsensitive(FilterOperator operator) {
		assertEquals("ILIKE :p0 ESCAPE '\\'", SqlOperatorExpressionResolver.resolve(operator, List.of(":p0"), true));
	}

	@Test
	void resolvesNotContainsCaseSensitive() {
		assertEquals("NOT LIKE :p0 ESCAPE '\\'",
				SqlOperatorExpressionResolver.resolve(FilterOperator.NOT_CONTAINS, List.of(":p0"), false));
	}

	@Test
	void resolvesNotContainsCaseInsensitive() {
		assertEquals("NOT ILIKE :p0 ESCAPE '\\'",
				SqlOperatorExpressionResolver.resolve(FilterOperator.NOT_CONTAINS, List.of(":p0"), true));
	}

	@ParameterizedTest
	@CsvSource({"IS_NULL,     'IS NULL'", "IS_NOT_NULL, 'IS NOT NULL'",})
	void resolvesNullAwareOperatorsIgnoringParameterNames(FilterOperator operator, String expected) {
		assertEquals(expected, SqlOperatorExpressionResolver.resolve(operator, List.of(), false));
	}
}
