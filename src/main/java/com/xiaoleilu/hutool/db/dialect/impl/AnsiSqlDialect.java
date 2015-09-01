package com.xiaoleilu.hutool.db.dialect.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.xiaoleilu.hutool.StrUtil;
import com.xiaoleilu.hutool.db.DbUtil;
import com.xiaoleilu.hutool.db.Entity;
import com.xiaoleilu.hutool.db.SqlBuilder;
import com.xiaoleilu.hutool.db.SqlBuilder.LogicalOperator;
import com.xiaoleilu.hutool.db.SqlBuilder.Order;
import com.xiaoleilu.hutool.db.Wrapper;
import com.xiaoleilu.hutool.db.dialect.Dialect;
import com.xiaoleilu.hutool.exceptions.DbRuntimeException;

/**
 * ANSI SQL 方言
 * @author loolly
 *
 */
public class AnsiSqlDialect implements Dialect {
	
	protected Wrapper wrapper = new Wrapper('`');

	@Override
	public PreparedStatement psForInsert(Connection conn, Entity entity) throws SQLException {
		final SqlBuilder insert = SqlBuilder.create(wrapper).insert(entity);

		final PreparedStatement ps = conn.prepareStatement(insert.build(), Statement.RETURN_GENERATED_KEYS);
		DbUtil.fillParams(ps, insert.getParamValueArray());
		return ps;
	}

	@Override
	public PreparedStatement psForDelete(Connection conn, Entity entity) throws SQLException {
		if (null == entity || entity.isEmpty()) {
			// 对于无条件的删除语句直接抛出异常禁止，防止误删除
			throw new SQLException("No condition define, we can't build delete query for del everything.");
		}
		
		final SqlBuilder delete = SqlBuilder.create(wrapper)
			.delete(entity.getTableName())
			.where(LogicalOperator.AND, DbUtil.buildConditions(entity));

		final PreparedStatement ps = conn.prepareStatement(delete.build());
		DbUtil.fillParams(ps, delete.getParamValueArray());
		return ps;
	}

	@Override
	public PreparedStatement psForUpdate(Connection conn, Entity entity, Entity where) throws SQLException {
		if (null == entity || entity.isEmpty()) {
			// 对于无条件的更新语句直接抛出异常禁止，防止误删除
			throw new SQLException("No condition define, we can't build update query for update everything.");
		}
		
		final SqlBuilder update = SqlBuilder.create(wrapper)
				.update(entity)
				.where(LogicalOperator.AND, DbUtil.buildConditions(where));

		final PreparedStatement ps = conn.prepareStatement(update.build());
		DbUtil.fillParams(ps, update.getParamValueArray());
		return ps;
	}

	@Override
	public PreparedStatement psForFind(Connection conn, Collection<String> fields, Entity where) throws SQLException {
		//验证
		if(where == null || StrUtil.isBlank(where.getTableName())) {
			throw new DbRuntimeException("Table name is null !");
		}
		
		final SqlBuilder find = SqlBuilder.create(wrapper)
			.select(fields)
			.from(where.getTableName())
			.where(LogicalOperator.AND, DbUtil.buildConditions(where));

		final PreparedStatement ps = conn.prepareStatement(find.build());
		DbUtil.fillParams(ps, find.getParamValueArray());
		return ps;
	}

	@Override
	public PreparedStatement psForPage(Connection conn, Collection<String> fields, Entity where, int page, int numPerPage) throws SQLException {
		throw new SQLException("ANSI SQL is not support for page query!");
	}
	
	@Override
	public PreparedStatement psForPage(Connection conn, Collection<String> fields, Entity where, int page, int numPerPage, Collection<String> orderFields, Order order) throws SQLException {
		throw new SQLException("ANSI SQL is not support for page query!");
	}

	@Override
	public PreparedStatement psForCount(Connection conn, Entity where) throws SQLException {
		List<String> fields = new ArrayList<String>();
		fields.add("count(1)");
		return psForFind(conn, fields, where);
	}

	// ---------------------------------------------------------------------------- Protected method start
	// ---------------------------------------------------------------------------- Protected method end

}
