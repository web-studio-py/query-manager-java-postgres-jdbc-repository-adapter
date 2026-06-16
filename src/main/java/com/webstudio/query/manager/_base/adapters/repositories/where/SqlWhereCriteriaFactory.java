package com.webstudio.query.manager._base.adapters.repositories.where;

import java.util.List;

import com.webstudio.query.manager._base.application.filter.FilterOperator;

/**
 * Binds a filterable field to the SQL expression and value conversion used to
 * build its {@code WHERE} criteria.
 *
 * <p>
 * Consumers register one factory per filterable field; the repository then
 * calls {@link #createCriteria} for each incoming filter on that field.
 *
 * @param <TValue>
 *            the domain value type of the field
 * @param <TSqlValue>
 *            the type bound to the SQL parameter
 */
public final class SqlWhereCriteriaFactory<TValue, TSqlValue> {

	private final String expression;
	private final SqlValueConverter<TValue, TSqlValue> valueConverter;

	/**
	 * @param expression
	 *            the left-hand SQL expression of the criteria (e.g.
	 *            {@code "u.status"}); must not be {@code null} or blank
	 * @param valueConverter
	 *            converts the domain value into the bound SQL value; must not be
	 *            {@code null}
	 * @throws IllegalArgumentException
	 *             if {@code expression} is {@code null} or blank, or if
	 *             {@code valueConverter} is {@code null}
	 */
	public SqlWhereCriteriaFactory(String expression, SqlValueConverter<TValue, TSqlValue> valueConverter) {
		if (expression == null || expression.isBlank()) {
			throw new IllegalArgumentException("Expression cannot be null or blank");
		}
		if (valueConverter == null) {
			throw new IllegalArgumentException("Value converter cannot be null");
		}
		this.expression = expression;
		this.valueConverter = valueConverter;
	}

	/**
	 * Creates a criteria node for one filter on the bound field.
	 *
	 * @param operator
	 *            the filter operator
	 * @param parameterNames
	 *            the named-parameter placeholders (e.g. {@code [":p0"]}); one per
	 *            value, or empty for null-aware operators that bind no value
	 * @param values
	 *            the domain values; one for single-value operators, two for
	 *            {@code BETWEEN}/{@code NOT_BETWEEN}, N for {@code IN}/
	 *            {@code NOT_IN}, or empty for null-aware operators
	 * @param ignoreCase
	 *            whether {@code LIKE}-family operators should match
	 *            case-insensitively
	 * @return the criteria node
	 */
	public SqlWhereCriteria<TValue, TSqlValue> createCriteria(FilterOperator operator, List<String> parameterNames,
			List<TValue> values, boolean ignoreCase) {
		return new SqlWhereCriteria<>(expression, operator, ignoreCase, parameterNames, values, valueConverter);
	}

}
