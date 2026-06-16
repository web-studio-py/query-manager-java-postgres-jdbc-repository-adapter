package com.webstudio.query.manager._base.adapters.repositories.join;

/**
 * A single {@code JOIN} clause.
 *
 * @param type
 *            the join kind; see {@link SqlJoinType}
 * @param expression
 *            the joined table or expression (e.g. {@code "orders"}); must not
 *            be {@code null} or blank
 * @param alias
 *            the table alias; when {@code null} or blank no alias is emitted
 * @param on
 *            the {@code ON} condition; when {@code null} or blank no {@code ON}
 *            is emitted. Must be absent for a {@link SqlJoinType#CROSS} join.
 */
public record SqlJoin(SqlJoinType type, String expression, String alias, String on) {

	/**
	 * @throws IllegalArgumentException
	 *             if {@code expression} is {@code null} or blank, or if a
	 *             {@link SqlJoinType#CROSS} join declares an {@code ON} clause
	 */
	public SqlJoin {
		if (expression == null || expression.isBlank()) {
			throw new IllegalArgumentException("Expression cannot be null or blank");
		}
		boolean hasOn = on != null && !on.isBlank();
		if (type == SqlJoinType.CROSS && hasOn) {
			throw new IllegalArgumentException("CROSS JOIN must not declare an ON clause: " + on);
		}
	}

	/**
	 * @return the clause {@code "<TYPE> JOIN <expression> [<alias>] [ON <on>]"} (no
	 *         leading or trailing whitespace)
	 */
	public String toSql() {
		StringBuilder sql = new StringBuilder();
		sql.append(type.name()).append(" JOIN ").append(expression);
		if (alias != null && !alias.isBlank()) {
			sql.append(' ').append(alias);
		}
		if (on != null && !on.isBlank()) {
			sql.append(" ON ").append(on);
		}
		return sql.toString();
	}
}
