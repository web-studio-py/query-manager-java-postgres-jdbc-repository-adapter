package com.webstudio.query.manager._base.adapters.repositories.where;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SqlLikePatternTest {

	@Test
	void containsWrapsWithLeadingAndTrailingWildcard() {
		assertEquals("%ann%", SqlLikePattern.contains("ann"));
	}

	@Test
	void startsWithWrapsWithTrailingWildcard() {
		assertEquals("ann%", SqlLikePattern.startsWith("ann"));
	}

	@Test
	void endsWithWrapsWithLeadingWildcard() {
		assertEquals("%ann", SqlLikePattern.endsWith("ann"));
	}

	@Test
	void escapesWildcardsAndEscapeCharBeforeWrapping() {
		assertEquals("%50\\%\\_x%", SqlLikePattern.contains("50%_x"));
		assertEquals("a\\\\b%", SqlLikePattern.startsWith("a\\b"));
	}
}
