package com.webstudio.query.manager._base.adapters.repositories.where;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@code WHERE} clause: the root of the predicate tree together with the
 * named parameters extracted from it.
 */
public class SqlWhere {

	private SqlWhereNode rootNode;
	private Map<String, Object> parameters = new HashMap<>();

	/**
	 * @param rootNode
	 *            the root predicate node; must not be {@code null}
	 * @throws IllegalArgumentException
	 *             if {@code rootNode} is {@code null}
	 */
	public SqlWhere(SqlWhereNode rootNode) {
		if (rootNode == null) {
			throw new IllegalArgumentException("Root node cannot be null");
		}
		this.rootNode = rootNode;
		this.parameters = extractParameters(rootNode);
	}

	private Map<String, Object> extractParameters(SqlWhereNode node) {
		Map<String, Object> params = new HashMap<>();
		if (node instanceof SqlWhereCriteria<?, ?> criteria) {
			params.putAll(criteria.getParameters());
		} else if (node instanceof SqlWhereGroup group) {
			for (SqlWhereNode child : group.getChildren()) {
				params.putAll(extractParameters(child));
			}
		}
		return params;
	}

	/**
	 * @return the named parameters and their values, keyed by parameter name
	 */
	public Map<String, Object> getParameters() {
		return parameters;
	}

	/**
	 * @return the clause {@code "WHERE <predicate>"} (no leading or trailing
	 *         whitespace)
	 */
	public String toSql() {
		return "WHERE " + rootNode.toSql();
	}

}
