package com.webstudio.query.manager.queryPage.adapters.repositories;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.webstudio.query.manager._base.adapters.repositories.PostgresJdbcQueryRepositoryAdapter;
import com.webstudio.query.manager._base.adapters.repositories.SqlQuery;
import com.webstudio.query.manager._base.adapters.repositories.where.SqlWhere;
import com.webstudio.query.manager._base.application.filter.FiltrableField;
import com.webstudio.query.manager._base.application.result.QueryResultRow;
import com.webstudio.query.manager._base.application.result.SelectableField;
import com.webstudio.query.manager.queryPage.application.Page;
import com.webstudio.query.manager.queryPage.application.port.QueryPage;
import com.webstudio.query.manager.queryPage.application.port.QueryPageRepository;

/**
 * JDBC implementation of {@link QueryPageRepository}: runs a filtered, sorted
 * query for a single page and a matching {@code COUNT(*)} for the total.
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
public abstract class QueryPagePostgresJdbcRepositoryAdapter<TFilterableField extends Enum<TFilterableField> & FiltrableField<?>, TSortableField extends Enum<TSortableField>, TSelectableField extends Enum<TSelectableField> & SelectableField<?>>
		extends
			PostgresJdbcQueryRepositoryAdapter<TFilterableField, TSortableField, TSelectableField>
		implements
			QueryPageRepository<TFilterableField, TSortableField, TSelectableField> {

	protected QueryPagePostgresJdbcRepositoryAdapter(NamedParameterJdbcTemplate jdbcTemplate) {
		super(jdbcTemplate);
	}

	/**
	 * Builds and executes the page query
	 * ({@code ... LIMIT size OFFSET page * size}) plus a {@code COUNT(*)} sharing
	 * the same {@code FROM}/{@code JOIN}/{@code WHERE}, and returns them as a
	 * {@link Page}.
	 *
	 * @param query
	 *            the selection, filters, sort and page coordinates to apply
	 * @return the requested page along with total element and page counts
	 */
	@Override
	public Page<QueryResultRow<TSelectableField>> findPage(
			QueryPage<TFilterableField, TSortableField, TSelectableField> query) {
		List<TSelectableField> selected = query.selectedFields();
		int page = query.page();
		int size = query.size();

		SqlWhere where = sqlWhere(query.filters());
		String whereSql = where != null ? where.toSql() : null;
		Map<String, Object> parameters = where != null ? where.getParameters() : Map.of();

		String dataSql = SqlQuery.join(sqlSelect(selected).toSql(), sqlFrom().toSql(), sqlJoins().toSql(), whereSql,
				sqlOrder(query.sortBy()).toSql(), sqlLimit(size).toSql(), sqlOffset(page * size).toSql());
		List<QueryResultRow<TSelectableField>> items = executeQuery(dataSql, parameters, selected);

		String countSql = SqlQuery.join("SELECT COUNT(*)", sqlFrom().toSql(), sqlJoins().toSql(), whereSql);
		Long total = jdbcTemplate.queryForObject(countSql, parameters, Long.class);
		int totalPages = size > 0 ? (int) Math.ceil((double) total / size) : 0;

		return new Page<>(items, page, size, total, totalPages);
	}

}
