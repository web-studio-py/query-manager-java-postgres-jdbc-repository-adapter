package com.webstudio.query.manager._base.adapters.repositories.join;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The ordered collection of {@code JOIN} clauses. May be empty, in which case
 * it contributes nothing to the statement.
 */
public class SqlJoins {

	private final List<SqlJoin> join;

	/**
	 * @param join
	 *            the joins, in order; may be {@code null} or empty for no joins
	 */
	public SqlJoins(List<SqlJoin> join) {
		this.join = join;
	}

	/**
	 * @return the joins separated by a single space, or {@code ""} when there are
	 *         none
	 */
	public String toSql() {
		if (join == null || join.isEmpty()) {
			return "";
		}
		return join.stream().map(SqlJoin::toSql).collect(Collectors.joining(" "));
	}
}
