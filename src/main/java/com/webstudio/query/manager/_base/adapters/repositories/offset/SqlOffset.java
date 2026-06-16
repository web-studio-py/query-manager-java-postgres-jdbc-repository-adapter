package com.webstudio.query.manager._base.adapters.repositories.offset;

/**
 * The {@code OFFSET} clause, skipping a number of leading rows.
 *
 * @param offset
 *            the number of rows to skip; must be non-negative
 */
public record SqlOffset(int offset) {

	/**
	 * @throws IllegalArgumentException
	 *             if {@code offset} is negative
	 */
	public SqlOffset {
		if (offset < 0) {
			throw new IllegalArgumentException("Offset must be non-negative");
		}
	}

	/**
	 * @return the clause {@code "OFFSET <offset>"} (no leading or trailing
	 *         whitespace)
	 */
	public String toSql() {
		return "OFFSET " + offset;
	}

}
