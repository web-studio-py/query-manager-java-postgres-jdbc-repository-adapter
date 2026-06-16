package com.webstudio.query.manager._base.adapters.repositories.where;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.webstudio.query.manager._base.application.filter.FilterOperator;

/**
 * A leaf predicate of the {@code WHERE} tree: an expression, an operator and
 * (for value-bearing operators) the named parameters and their converted
 * values.
 *
 * <p>
 * A single criteria may bind several parameters: one for the common
 * single-value operators, two for {@code BETWEEN}/{@code NOT_BETWEEN} and N for
 * {@code IN}/ {@code NOT_IN}. Each value is converted individually through the
 * field's {@link SqlValueConverter}; the {@code LIKE}-family operators
 * additionally wrap the converted value into a {@link SqlLikePattern}.
 *
 * @param <TValue>
 *            the domain value type
 * @param <TSqlValue>
 *            the type bound to the SQL parameter
 */
public final class SqlWhereCriteria<TValue, TSqlValue> implements SqlWhereNode {

	private final String expression;
	private final FilterOperator operator;
	private final boolean ignoreCase;
	private final List<String> parameterNames;
	private final List<TValue> values;
	private final SqlValueConverter<TValue, TSqlValue> valueConverter;

	public SqlWhereCriteria(String expression, FilterOperator operator, boolean ignoreCase, List<String> parameterNames,
			List<TValue> values, SqlValueConverter<TValue, TSqlValue> valueConverter) {
		if (expression == null || expression.isBlank()) {
			throw new IllegalArgumentException("Expression cannot be null or blank");
		}
		if (operator == null) {
			throw new IllegalArgumentException("Operator cannot be null");
		}
		if (parameterNames == null || values == null) {
			throw new IllegalArgumentException("Parameter names and values cannot be null");
		}
		if (parameterNames.size() != values.size()) {
			throw new IllegalArgumentException("Parameter names and values must have the same size");
		}
		if (valueConverter == null) {
			throw new IllegalArgumentException("Value converter cannot be null");
		}
		this.expression = expression;
		this.operator = operator;
		this.ignoreCase = ignoreCase;
		this.parameterNames = List.copyOf(parameterNames);
		this.values = values;
		this.valueConverter = valueConverter;
	}

	/**
	 * @return the bound values keyed by parameter name (including the leading
	 *         colon), in binding order; empty for the null-aware operators
	 */
	public Map<String, Object> getParameters() {
		Map<String, Object> parameters = new LinkedHashMap<>();
		for (int i = 0; i < parameterNames.size(); i++) {
			parameters.put(parameterNames.get(i), sqlValue(values.get(i)));
		}
		return parameters;
	}

	private Object sqlValue(TValue value) {
		TSqlValue converted = valueConverter.convert(value);
		return switch (operator) {
			case CONTAINS, NOT_CONTAINS -> SqlLikePattern.contains((String) converted);
			case STARTS_WITH -> SqlLikePattern.startsWith((String) converted);
			case ENDS_WITH -> SqlLikePattern.endsWith((String) converted);
			default -> converted;
		};
	}

	/**
	 * @return the predicate {@code "<expression> <operator-fragment>"} (no
	 *         surrounding whitespace)
	 */
	@Override
	public String toSql() {
		return expression + " " + SqlOperatorExpressionResolver.resolve(operator, parameterNames, ignoreCase);
	}

}
