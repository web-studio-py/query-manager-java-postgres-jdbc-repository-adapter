package com.webstudio.query.manager._base.adapters.repositories.where;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Allocates unique named-parameter placeholders ({@code p0}, {@code p1}, ...)
 * while a {@code WHERE} tree is being built, accumulating their bound values.
 */
public class SqlParameters {

	private final Map<String, Object> values = new LinkedHashMap<>();
	private int counter = 0;

	/**
	 * Binds a value to a freshly generated parameter name.
	 *
	 * @param value
	 *            the value to bind
	 * @return the placeholder to embed in the SQL, including the leading colon
	 *         (e.g. {@code ":p0"})
	 */
	public String bind(Object value) {
		String name = "p" + counter++;
		values.put(name, value);
		return ":" + name;
	}

	/**
	 * @return the bound values keyed by parameter name (without the leading colon),
	 *         in binding order
	 */
	public Map<String, Object> values() {
		return values;
	}

}
