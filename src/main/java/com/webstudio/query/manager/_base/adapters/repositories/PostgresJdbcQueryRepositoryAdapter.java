package com.webstudio.query.manager._base.adapters.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.webstudio.query.manager._base.adapters.repositories.from.SqlFrom;
import com.webstudio.query.manager._base.adapters.repositories.join.SqlJoins;
import com.webstudio.query.manager._base.adapters.repositories.limit.SqlLimit;
import com.webstudio.query.manager._base.adapters.repositories.offset.SqlOffset;
import com.webstudio.query.manager._base.adapters.repositories.order.SqlOrder;
import com.webstudio.query.manager._base.adapters.repositories.order.SqlOrderItem;
import com.webstudio.query.manager._base.adapters.repositories.select.SqlSelect;
import com.webstudio.query.manager._base.adapters.repositories.select.SqlSelectItem;
import com.webstudio.query.manager._base.adapters.repositories.where.SqlParameters;
import com.webstudio.query.manager._base.adapters.repositories.where.SqlWhere;
import com.webstudio.query.manager._base.adapters.repositories.where.SqlWhereCriteriaFactory;
import com.webstudio.query.manager._base.adapters.repositories.where.SqlWhereGroup;
import com.webstudio.query.manager._base.adapters.repositories.where.SqlWhereNode;
import com.webstudio.query.manager._base.application.filter.FilterCriteria;
import com.webstudio.query.manager._base.application.filter.FilterGroup;
import com.webstudio.query.manager._base.application.filter.FilterNode;
import com.webstudio.query.manager._base.application.filter.FilterOperator;
import com.webstudio.query.manager._base.application.filter.Filters;
import com.webstudio.query.manager._base.application.filter.FiltrableField;
import com.webstudio.query.manager._base.application.result.QueryResultRow;
import com.webstudio.query.manager._base.application.result.SelectableField;
import com.webstudio.query.manager._base.application.sort.SortOptions;

/**
 * Base class for JDBC repositories that translate the query-manager
 * abstractions (selection, filtering, sorting, pagination) into SQL and execute
 * it through a {@link NamedParameterJdbcTemplate}.
 *
 * <p>
 * Subclasses bind each enum-based field to its SQL counterpart by implementing
 * the abstract mapping methods, and describe the table layout via
 * {@link #sqlFrom()} and {@link #sqlJoins()}. The protected {@code sql*}
 * helpers assemble the individual clauses; the concrete query repositories
 * ({@code QueryAllPostgresJdbcRepositoryAdapter},
 * {@code QueryPagePostgresJdbcRepositoryAdapter}) stitch them into a statement
 * and run it.
 *
 * @param <TFilterableField>
 *            the enum of filterable fields
 * @param <TSortableField>
 *            the enum of sortable fields
 * @param <TSelectableField>
 *            the enum of selectable fields
 */
public abstract class PostgresJdbcQueryRepositoryAdapter<TFilterableField extends Enum<TFilterableField> & FiltrableField<?>, TSortableField extends Enum<TSortableField>, TSelectableField extends Enum<TSelectableField> & SelectableField<?>> {

	protected final NamedParameterJdbcTemplate jdbcTemplate;

	protected PostgresJdbcQueryRepositoryAdapter(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * @return the {@link SqlSelectItem} (column expression and alias) for each
	 *         selectable field; the alias is the key used to read the value back
	 *         from the result set
	 */
	protected abstract Map<TSelectableField, SqlSelectItem> sqlSelectItemsBySelectableField();

	/**
	 * @return the {@link SqlWhereCriteriaFactory} for each filterable field,
	 *         defining its SQL expression and value conversion
	 */
	protected abstract Map<TFilterableField, SqlWhereCriteriaFactory<?, ?>> sqlWhereCriteriaFactoriesByFilterableField();

	/**
	 * @return a deterministic tie-breaker appended after the caller's ordering
	 *         (typically a unique key) to keep pagination stable, or {@code null}
	 *         for none
	 */
	protected abstract SqlOrderItem sqlTieBreakerOrderItem();

	/**
	 * @return the {@link SqlOrderItem} for each sortable field
	 */
	protected abstract Map<TSortableField, SqlOrderItem> sqlOrderItemsBySortableField();

	protected SqlLimit sqlLimit(int limit) {
		return new SqlLimit(limit);
	}

	protected SqlOffset sqlOffset(int offset) {
		return new SqlOffset(offset);
	}

	protected SqlSelect sqlSelect(List<TSelectableField> selectedFields) {
		List<SqlSelectItem> sqlSelectItems = selectedFields.stream().map(sqlSelectItemsBySelectableField()::get)
				.toList();
		return new SqlSelect(sqlSelectItems);
	}

	/**
	 * @return the {@code FROM} clause identifying the base table(s) of the query
	 */
	protected abstract SqlFrom sqlFrom();

	/**
	 * @return the {@code JOIN} clauses of the query; return an empty
	 *         {@link SqlJoins} when no joins are needed
	 */
	protected abstract SqlJoins sqlJoins();

	protected SqlWhere sqlWhere(Filters<TFilterableField> filters) {
		if (filters == null || filters.isEmpty()) {
			return null;
		}
		SqlParameters params = new SqlParameters();
		return new SqlWhere(buildNode(filters.getRoot(), params));
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private SqlWhereNode buildNode(FilterNode node, SqlParameters params) {
		if (node instanceof FilterGroup group) {
			List<SqlWhereNode> children = new ArrayList<>();
			for (Object child : group.getChildren()) {
				children.add(buildNode((FilterNode) child, params));
			}
			return new SqlWhereGroup(group.getOperator(), children);
		}
		FilterCriteria criteria = (FilterCriteria) node;
		FilterOperator operator = criteria.getOperator();
		boolean ignoreCase = criteria.isIgnoreCase();
		SqlWhereCriteriaFactory factory = sqlWhereCriteriaFactoriesByFilterableField().get(criteria.getField());

		if (operator == FilterOperator.IS_NULL || operator == FilterOperator.IS_NOT_NULL) {
			return factory.createCriteria(operator, List.of(), List.of(), ignoreCase);
		}

		List<Object> values = new ArrayList<>(criteria.getValues());
		List<String> names = new ArrayList<>(values.size());
		for (Object value : values) {
			names.add(params.bind(value));
		}
		return factory.createCriteria(operator, names, values, ignoreCase);
	}

	protected SqlOrder sqlOrder(SortOptions<TSortableField> sortBy) {
		List<SqlOrderItem> sqlOrderItems = new ArrayList<>(sortBy.getOptions().stream()
				.map(option -> sqlOrderItemsBySortableField().get(option.getField())).toList());
		SqlOrderItem tieBreakerOrderItem = sqlTieBreakerOrderItem();
		if (tieBreakerOrderItem != null) {
			sqlOrderItems.add(tieBreakerOrderItem);
		}
		return new SqlOrder(sqlOrderItems);
	}

	protected List<QueryResultRow<TSelectableField>> executeQuery(String sql, Map<String, Object> parameters,
			List<TSelectableField> selectedFields) {
		return jdbcTemplate.query(sql, parameters, (rs, rowNum) -> {
			Map<SelectableField<?>, Object> values = new HashMap<>();
			for (TSelectableField field : selectedFields) {
				SqlSelectItem sqlSelectItem = sqlSelectItemsBySelectableField().get(field);
				Object value = rs.getObject(sqlSelectItem.alias());
				values.put(field, value);
			}
			return new QueryResultRow<>(values);
		});
	}

}
