package com.webstudio.query.manager._base.adapters.repositories.order;

import java.util.List;

/**
 * The {@code ORDER BY} clause. May be empty, in which case it contributes
 * nothing to the statement.
 *
 * @param items
 *            the ordering terms, in priority order; may be {@code null} or
 *            empty
 */
public record SqlOrder(List<SqlOrderItem> items) {

	/**
	 * @return the clause {@code "ORDER BY <item>, <item>, ..."} (no leading or
	 *         trailing whitespace), or {@code ""} when there are no items
	 */
	public String toSql() {
		if (items == null || items.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder("ORDER BY ");
		for (int i = 0; i < items.size(); i++) {
			sb.append(items.get(i).toSql());
			if (i < items.size() - 1) {
				sb.append(", ");
			}
		}
		return sb.toString();
	}

}
