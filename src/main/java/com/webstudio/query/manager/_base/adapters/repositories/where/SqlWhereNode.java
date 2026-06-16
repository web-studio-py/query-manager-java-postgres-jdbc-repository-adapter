package com.webstudio.query.manager._base.adapters.repositories.where;

/**
 * A node of the {@code WHERE} predicate tree: either a leaf
 * {@link SqlWhereCriteria} or a nested {@link SqlWhereGroup}.
 */
public sealed interface SqlWhereNode permits SqlWhereCriteria, SqlWhereGroup {

	/**
	 * @return this node rendered as a SQL fragment (no surrounding whitespace)
	 */
	String toSql();

}
