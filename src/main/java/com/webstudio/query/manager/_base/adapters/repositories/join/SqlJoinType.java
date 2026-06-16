package com.webstudio.query.manager._base.adapters.repositories.join;

/**
 * The kind of SQL {@code JOIN} to emit. The enum name is rendered verbatim
 * before the {@code JOIN} keyword (e.g. {@link #LEFT} produces
 * {@code "LEFT JOIN ..."}).
 *
 * <p>
 * {@link #CROSS} must not declare an {@code ON} clause.
 */
public enum SqlJoinType {
	INNER, LEFT, RIGHT, FULL, CROSS
}
