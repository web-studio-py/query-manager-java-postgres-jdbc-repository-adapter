package com.webstudio.query.manager._base.adapters.repositories.where;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.webstudio.query.manager._base.application.filter.FilterOperator;

class SqlWhereCriteriaFactoryTest {

	@Test
	void createsCriteriaBoundToExpressionAndConverter() {
		SqlWhereCriteriaFactory<Integer, String> factory = new SqlWhereCriteriaFactory<>("u.age", String::valueOf);

		SqlWhereCriteria<Integer, String> criteria = factory.createCriteria(FilterOperator.GREATER_THAN, List.of(":p0"),
				List.of(18), false);

		assertEquals("u.age > :p0", criteria.toSql());
		assertEquals(Map.of(":p0", "18"), criteria.getParameters());
	}

	@Test
	void rejectsBlankExpression() {
		assertThrows(IllegalArgumentException.class, () -> new SqlWhereCriteriaFactory<>(" ", value -> value));
	}

	@Test
	void rejectsNullValueConverter() {
		assertThrows(IllegalArgumentException.class, () -> new SqlWhereCriteriaFactory<String, String>("u.age", null));
	}
}
