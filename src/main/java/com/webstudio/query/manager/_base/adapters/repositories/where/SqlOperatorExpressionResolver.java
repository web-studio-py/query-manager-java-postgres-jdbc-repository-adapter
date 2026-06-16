package com.webstudio.query.manager._base.adapters.repositories.where;

import java.util.List;

import com.webstudio.query.manager._base.application.filter.FilterOperator;

/**
 * Renders the right-hand side of a {@code WHERE} criteria for a given
 * {@link FilterOperator}, embedding the named-parameter placeholders where they
 * are needed.
 */
public class SqlOperatorExpressionResolver {

	private SqlOperatorExpressionResolver() {
	}

	/**
	 * @param operator
	 *            the filter operator
	 * @param parameterNames
	 *            the placeholders to embed (e.g. {@code [":p0", ":p1"]}); one for
	 *            single-value operators, two for {@code BETWEEN}/
	 *            {@code NOT_BETWEEN}, N for {@code IN}/{@code NOT_IN}, and empty
	 *            for the null-aware operators {@code IS_NULL}/{@code IS_NOT_NULL}
	 * @param ignoreCase
	 *            when {@code true}, the {@code LIKE}-family operators render
	 *            {@code ILIKE} instead of {@code LIKE} (PostgreSQL); ignored by
	 *            every other operator
	 * @return the operator fragment, e.g. {@code "= :p0"}, {@code "IN (:p0, :p1)"},
	 *         {@code "ILIKE :p0 ESCAPE '\'"} or {@code "IS NULL"}
	 */
	public static String resolve(FilterOperator operator, List<String> parameterNames, boolean ignoreCase) {
		return switch (operator) {
			case EQUAL -> "= " + parameterNames.get(0);
			case NOT_EQUAL -> "<> " + parameterNames.get(0);
			case GREATER_THAN -> "> " + parameterNames.get(0);
			case GREATER_THAN_OR_EQUAL -> ">= " + parameterNames.get(0);
			case LESS_THAN -> "< " + parameterNames.get(0);
			case LESS_THAN_OR_EQUAL -> "<= " + parameterNames.get(0);
			case IN -> "IN (" + String.join(", ", parameterNames) + ")";
			case NOT_IN -> "NOT IN (" + String.join(", ", parameterNames) + ")";
			case CONTAINS, STARTS_WITH, ENDS_WITH -> like(parameterNames.get(0), ignoreCase);
			case NOT_CONTAINS -> "NOT " + like(parameterNames.get(0), ignoreCase);
			case BETWEEN -> "BETWEEN " + parameterNames.get(0) + " AND " + parameterNames.get(1);
			case NOT_BETWEEN -> "NOT BETWEEN " + parameterNames.get(0) + " AND " + parameterNames.get(1);
			case IS_NULL -> "IS NULL";
			case IS_NOT_NULL -> "IS NOT NULL";
		};
	}

	private static String like(String parameterName, boolean ignoreCase) {
		return (ignoreCase ? "ILIKE " : "LIKE ") + parameterName + " ESCAPE '\\'";
	}

}
