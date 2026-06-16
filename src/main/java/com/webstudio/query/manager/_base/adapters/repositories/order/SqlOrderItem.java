package com.webstudio.query.manager._base.adapters.repositories.order;

import com.webstudio.query.manager._base.application.sort.SortDirection;

/**
 * A single {@code ORDER BY} term: an expression and its sort direction.
 *
 * @param expression
 *            the column or expression to sort by; must not be {@code null} or
 *            blank
 * @param direction
 *            the sort direction; must not be {@code null}
 */
public record SqlOrderItem(String expression, SortDirection direction) {

	/**
	 * @throws IllegalArgumentException
	 *             if {@code expression} is {@code null} or blank, or if
	 *             {@code direction} is {@code null}
	 */
	public SqlOrderItem {
		if (expression == null || expression.isBlank()) {
			throw new IllegalArgumentException("Expression cannot be null or blank");
		}
		if (direction == null) {
			throw new IllegalArgumentException("Direction cannot be null");
		}
	}

	/**
	 * @return {@code "<expression> ASC"} or {@code "<expression> DESC"}
	 */
	public String toSql() {
		return expression + " " + direction.name();
	}

}
