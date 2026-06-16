package com.webstudio.query.manager._base.adapters.repositories.where;

/**
 * Converts a domain filter value into the value actually bound to the SQL
 * parameter.
 *
 * <p>
 * Use it to adapt a domain type to what the JDBC driver expects, for example
 * turning a {@code UUID} into its {@code String} form or wrapping a value for a
 * {@code LIKE} pattern.
 *
 * @param <TValue>
 *            the domain value type
 * @param <TSqlValue>
 *            the type bound to the SQL parameter
 */
@FunctionalInterface
public interface SqlValueConverter<TValue, TSqlValue> {

	/**
	 * @param value
	 *            the domain value (may be {@code null} for null-aware operators)
	 * @return the value to bind to the SQL parameter
	 */
	TSqlValue convert(TValue value);
}
