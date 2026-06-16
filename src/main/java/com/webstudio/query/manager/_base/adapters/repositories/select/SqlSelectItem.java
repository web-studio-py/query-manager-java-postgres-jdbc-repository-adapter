package com.webstudio.query.manager._base.adapters.repositories.select;

/**
 * A single projected column of the {@code SELECT} clause.
 *
 * <p>
 * The {@code alias} is also the key used to read the value back from the result
 * set, so it must match the name the consumer maps each selectable field to.
 *
 * @param expression
 *            the column or SQL expression to project (e.g. {@code "u.name"});
 *            must not be {@code null} or blank
 * @param alias
 *            the column alias; when {@code null} or blank no {@code AS} is
 *            emitted
 */
public record SqlSelectItem(String expression, String alias) {

	/**
	 * @throws IllegalArgumentException
	 *             if {@code expression} is {@code null} or blank
	 */
	public SqlSelectItem(String expression, String alias) {
		if (expression == null || expression.isBlank()) {
			throw new IllegalArgumentException("Expression cannot be null or blank");
		}
		this.expression = expression;
		this.alias = alias;
	}

	/**
	 * @return {@code "<expression> AS <alias>"}, or just {@code "<expression>"}
	 *         when no alias is set
	 */
	public String toSql() {
		return alias != null && !alias.isBlank() ? expression + " AS " + alias : expression;
	}

}
