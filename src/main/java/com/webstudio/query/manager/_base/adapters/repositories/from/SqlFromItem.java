package com.webstudio.query.manager._base.adapters.repositories.from;

/**
 * A single table (or table-like expression) of the {@code FROM} clause.
 *
 * @param expression
 *            the table name or expression (e.g. {@code "users"}); must not be
 *            {@code null} or blank
 * @param alias
 *            the table alias; when {@code null} or blank no alias is emitted
 */
public record SqlFromItem(String expression, String alias) {

	/**
	 * @throws IllegalArgumentException
	 *             if {@code expression} is {@code null} or blank
	 */
	public SqlFromItem {
		if (expression == null || expression.isBlank()) {
			throw new IllegalArgumentException("Expression cannot be null or blank");
		}
	}

	/**
	 * @return {@code "<expression> <alias>"}, or just {@code "<expression>"} when
	 *         no alias is set
	 */
	public String toSql() {
		if (alias == null || alias.isBlank()) {
			return expression;
		}
		return expression + " " + alias;
	}
}
