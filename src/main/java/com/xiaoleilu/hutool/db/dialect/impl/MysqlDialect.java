package com.xiaoleilu.hutool.db.dialect.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

import com.xiaoleilu.hutool.CollectionUtil;
import com.xiaoleilu.hutool.PageUtil;
import com.xiaoleilu.hutool.StrUtil;
import com.xiaoleilu.hutool.db.DbUtil;
import com.xiaoleilu.hutool.db.Entity;
import com.xiaoleilu.hutool.db.SqlBuilder;
import com.xiaoleilu.hutool.db.SqlBuilder.LogicalOperator;
import com.xiaoleilu.hutool.db.SqlBuilder.Order;
import com.xiaoleilu.hutool.exceptions.DbRuntimeException;

/**
 * MySQL方言
 * @author loolly
 *
 */
public class MysqlDialect extends AnsiSqlDialect{

	@Override
	public PreparedStatement psForPage(Connection conn, Collection<String> fields, Entity where, int page, int numPerPage) throws SQLException {
		return psForPage(conn, fields, where, page, numPerPage, null, null);
	}
	
	@Override
	public PreparedStatement psForPage(Connection conn, Collection<String> fields, Entity where, int page, int numPerPage, Collection<String> orderFields, Order order) throws SQLException {
		//验证
		if(where == null || StrUtil.isBlank(where.getTableName())) {
			throw new DbRuntimeException("Table name is null !");
		}
		
		final SqlBuilder find = SqlBuilder.create(wrapper)
				.select(fields)
				.from(where.getTableName())
				.where(LogicalOperator.AND, DbUtil.buildConditions(where));
		
		if(null != order && CollectionUtil.isNotEmpty(orderFields)) {
			find.orderBy(order, orderFields.toArray(new String[orderFields.size()]));
		}
		
		int[] startEnd = PageUtil.transToStartEnd(page, numPerPage);
		find.append(" LIMIT ").append(startEnd[0]).append(", ").append(numPerPage);
		
		final PreparedStatement ps = conn.prepareStatement(find.build());
		DbUtil.fillParams(ps, find.getParamValueArray());
		return ps;
	}
}
