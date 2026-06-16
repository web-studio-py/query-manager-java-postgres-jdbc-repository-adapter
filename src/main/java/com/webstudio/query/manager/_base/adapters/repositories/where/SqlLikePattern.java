package com.webstudio.query.manager._base.adapters.repositories.where;

/**
 * Builds the bound value for the {@code LIKE}-family operators
 * ({@code CONTAINS}, {@code NOT_CONTAINS}, {@code STARTS_WITH},
 * {@code ENDS_WITH}).
 *
 * <p>
 * The raw value is first escaped so that the {@code LIKE} wildcards {@code %}
 * and {@code _} (and the escape character {@code \} itself) are matched
 * literally, then wrapped with the wildcards that express the operator's
 * semantics. The rendered SQL pairs the resulting parameter with an explicit
 * {@code ESCAPE '\'} clause (see {@link SqlOperatorExpressionResolver}).
 */
public final class SqlLikePattern {

	private SqlLikePattern() {
	}

	/**
	 * @return the value wrapped as {@code "%<escaped>%"} for a {@code CONTAINS}/
	 *         {@code NOT_CONTAINS} match
	 */
	public static String contains(String value) {
		return "%" + escape(value) + "%";
	}

	/**
	 * @return the value wrapped as {@code "<escaped>%"} for a {@code STARTS_WITH}
	 *         match
	 */
	public static String startsWith(String value) {
		return escape(value) + "%";
	}

	/**
	 * @return the value wrapped as {@code "%<escaped>"} for an {@code ENDS_WITH}
	 *         match
	 */
	public static String endsWith(String value) {
		return "%" + escape(value);
	}

	private static String escape(String value) {
		StringBuilder escaped = new StringBuilder(value.length());
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			if (c == '\\' || c == '%' || c == '_') {
				escaped.append('\\');
			}
			escaped.append(c);
		}
		return escaped.toString();
	}

}
