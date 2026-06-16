package com.webstudio.query.manager._base.adapters.repositories.select;

import java.util.List;

/**
 * The {@code SELECT} clause: the ordered list of projected columns.
 *
 * @param items
 *            the projected columns; must not be {@code null} or empty
 */
public record SqlSelect(List<SqlSelectItem> items) {

	/**
	 * @throws IllegalArgumentException
	 *             if {@code items} is {@code null} or empty
	 */
	public SqlSelect(List<SqlSelectItem> items) {
		if (items == null || items.isEmpty()) {
			throw new IllegalArgumentException("Select items cannot be null or empty");
		}
		this.items = List.copyOf(items);
	}

	/**
	 * @return the clause {@code "SELECT <item>, <item>, ..."} (no leading or
	 *         trailing whitespace)
	 */
	public String toSql() {
		StringBuilder sb = new StringBuilder("SELECT ");
		for (int i = 0; i < items.size(); i++) {
			sb.append(items.get(i).toSql());
			if (i < items.size() - 1) {
				sb.append(", ");
			}
		}
		return sb.toString();
	}
}
