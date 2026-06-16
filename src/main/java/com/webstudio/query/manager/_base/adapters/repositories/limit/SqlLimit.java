package com.webstudio.query.manager._base.adapters.repositories.limit;

/**
 * The {@code LIMIT} clause, capping the number of returned rows.
 *
 * @param limit
 *            the maximum number of rows; must be non-negative
 */
public record SqlLimit(int limit) {

	/**
	 * @throws IllegalArgumentException
	 *             if {@code limit} is negative
	 */
	public SqlLimit {
		if (limit < 0) {
			throw new IllegalArgumentException("Limit must be non-negative");
		}
	}

	/**
	 * @return the clause {@code "LIMIT <limit>"} (no leading or trailing
	 *         whitespace)
	 */
	public String toSql() {
		return "LIMIT " + limit;
	}

}
