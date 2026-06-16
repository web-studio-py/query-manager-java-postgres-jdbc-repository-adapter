package com.webstudio.query.manager._base.adapters.repositories.where;

import java.util.List;

import com.webstudio.query.manager._base.application.filter.LogicalOperator;

/**
 * A parenthesised group of {@code WHERE} nodes combined with a single logical
 * operator ({@code AND}/{@code OR}).
 *
 * @param logicalOperator
 *            the operator joining the children; must not be {@code null}
 * @param groupExpression
 *            the child nodes; must not be {@code null} or empty
 */
public record SqlWhereGroup(LogicalOperator logicalOperator,
		List<SqlWhereNode> groupExpression) implements SqlWhereNode {

	public SqlWhereGroup(LogicalOperator logicalOperator, List<SqlWhereNode> groupExpression) {
		if (logicalOperator == null) {
			throw new IllegalArgumentException("Logical operator cannot be null");
		}
		if (groupExpression == null || groupExpression.isEmpty()) {
			throw new IllegalArgumentException("Group expression cannot be null or empty");
		}
		this.logicalOperator = logicalOperator;
		this.groupExpression = groupExpression;
	}

	public List<SqlWhereNode> getChildren() {
		return groupExpression;
	}

	/**
	 * @return the children joined by the logical operator and wrapped in
	 *         parentheses, e.g. {@code "(a = :p0 AND b = :p1)"}
	 */
	public String toSql() {
		StringBuilder sql = new StringBuilder("(");
		for (int i = 0; i < groupExpression.size(); i++) {
			sql.append(groupExpression.get(i).toSql());
			if (i < groupExpression.size() - 1) {
				sql.append(" ").append(logicalOperator.name()).append(" ");
			}
		}
		sql.append(")");
		return sql.toString();
	}

}
