package com.webstudio.query.manager._base.adapters.repositories;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Assembles the SQL clause fragments produced by the {@code toSql()} methods of
 * the query building blocks into a single statement.
 *
 * <p>
 * Each building block ({@code SqlSelect}, {@code SqlFrom}, {@code SqlJoins},
 * {@code SqlWhere}, {@code SqlOrder}, {@code SqlLimit}, {@code SqlOffset})
 * returns its clause <em>without</em> leading or trailing whitespace, and
 * optional clauses return an empty string when they are not present. This class
 * is the single place that owns the spacing between clauses, joining the
 * non-blank fragments with exactly one space.
 */
public final class SqlQuery {

	private SqlQuery() {
	}

	/**
	 * Joins the given clause fragments into a single SQL statement.
	 *
	 * <p>
	 * {@code null} and blank fragments are skipped, and the remaining fragments are
	 * separated by a single space. This keeps optional clauses (joins, where,
	 * order, limit, offset) from introducing double or trailing spaces.
	 *
	 * @param parts
	 *            the clause fragments, in the order they should appear in the
	 *            statement
	 * @return the assembled SQL statement
	 */
	public static String join(String... parts) {
		return Stream.of(parts).filter(part -> part != null && !part.isBlank()).collect(Collectors.joining(" "));
	}

}
