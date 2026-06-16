package com.webstudio.query.manager._base.adapters.repositories.from;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The {@code FROM} clause: one or more table items.
 */
public class SqlFrom {

	private final List<SqlFromItem> items;

	/**
	 * @param items
	 *            the table items; must not be {@code null} or empty
	 * @throws IllegalArgumentException
	 *             if {@code items} is {@code null} or empty
	 */
	public SqlFrom(List<SqlFromItem> items) {
		if (items == null || items.isEmpty()) {
			throw new IllegalArgumentException("At least one FROM item is required");
		}
		this.items = items;
	}

	/**
	 * @return the clause {@code "FROM <item>, <item>, ..."} (no leading or trailing
	 *         whitespace)
	 */
	public String toSql() {
		return "FROM " + items.stream().map(SqlFromItem::toSql).collect(Collectors.joining(", "));
	}

}
