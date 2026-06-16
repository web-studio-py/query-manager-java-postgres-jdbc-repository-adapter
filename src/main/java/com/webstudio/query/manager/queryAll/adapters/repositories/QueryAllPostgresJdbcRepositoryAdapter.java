package com.webstudio.query.manager.queryAll.adapters.repositories;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.webstudio.query.manager._base.adapters.repositories.PostgresJdbcQueryRepositoryAdapter;
import com.webstudio.query.manager._base.adapters.repositories.SqlQuery;
import com.webstudio.query.manager._base.adapters.repositories.where.SqlWhere;
import com.webstudio.query.manager._base.application.filter.FiltrableField;
import com.webstudio.query.manager._base.application.result.QueryResultRow;
import com.webstudio.query.manager._base.application.result.SelectableField;
import com.webstudio.query.manager.queryAll.application.port.QueryAll;
import com.webstudio.query.manager.queryAll.application.port.QueryAllRepository;

/**
 * JDBC implementation of {@link QueryAllRepository}: runs a filtered, sorted
 * query and returns every matching row.
 *
 * <p>
 * Subclasses only provide the field-to-SQL mappings and table layout defined by
 * {@link PostgresJdbcQueryRepositoryAdapter}.
 *
 * @param <TFilterableField>
 *            the enum of filterable fields
 * @param <TSortableField>
 *            the enum of sortable fields
 * @param <TSelectableField>
 *            the enum of selectable fields
 */
public abstract class QueryAllPostgresJdbcRepositoryAdapter<TFilterableField extends Enum<TFilterableField> & FiltrableField<?>, TSortableField extends Enum<TSortableField>, TSelectableField extends Enum<TSelectableField> & SelectableField<?>>
		extends
			PostgresJdbcQueryRepositoryAdapter<TFilterableField, TSortableField, TSelectableField>
		implements
			QueryAllRepository<TFilterableField, TSortableField, TSelectableField> {

	protected QueryAllPostgresJdbcRepositoryAdapter(NamedParameterJdbcTemplate jdbcTemplate) {
		super(jdbcTemplate);
	}

	/**
	 * Builds and executes
	 * {@code SELECT ... FROM ... [JOIN ...] [WHERE ...] [ORDER BY ...]} for the
	 * given query.
	 *
	 * @param query
	 *            the selection, filters and sort to apply
	 * @return one {@link QueryResultRow} per matching row
	 */
	@Override
	public List<QueryResultRow<TSelectableField>> findAll(
			QueryAll<TFilterableField, TSortableField, TSelectableField> query) {
		List<TSelectableField> selected = query.selectedFields();
		SqlWhere where = sqlWhere(query.filters());

		String sql = SqlQuery.join(sqlSelect(selected).toSql(), sqlFrom().toSql(), sqlJoins().toSql(),
				where != null ? where.toSql() : null, sqlOrder(query.sortBy()).toSql());

		Map<String, Object> parameters = where != null ? where.getParameters() : Map.of();

		return executeQuery(sql, parameters, selected);
	}

}
