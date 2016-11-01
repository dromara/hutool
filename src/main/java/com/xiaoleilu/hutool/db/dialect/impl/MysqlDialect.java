package com.xiaoleilu.hutool.db.dialect.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.xiaoleilu.hutool.db.DbUtil;
import com.xiaoleilu.hutool.db.Page;
import com.xiaoleilu.hutool.db.dialect.DialectName;
import com.xiaoleilu.hutool.db.sql.LogicalOperator;
import com.xiaoleilu.hutool.db.sql.Order;
import com.xiaoleilu.hutool.db.sql.Query;
import com.xiaoleilu.hutool.db.sql.SqlBuilder;
import com.xiaoleilu.hutool.db.sql.Wrapper;
import com.xiaoleilu.hutool.exceptions.DbRuntimeException;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * MySQL方言
 * @author loolly
 *
 */
public class MysqlDialect extends AnsiSqlDialect{
	
	public MysqlDialect() {
		wrapper = new Wrapper('`');
	}

	@Override
	public PreparedStatement psForPage(Connection conn, Query query) throws SQLException {
		//验证
		if(query == null || StrUtil.hasBlank(query.getTableNames())) {
			throw new DbRuntimeException("Table name is null !");
		}
		
		final SqlBuilder find = SqlBuilder.create(wrapper)
				.select(query.getFields())
				.from(query.getTableNames())
				.where(LogicalOperator.AND, query.getWhere());
		
		
		final Page page = query.getPage();
		if(null != page){
			final Order[] orders = page.getOrders();
			if(null != orders){
				find.orderBy(orders);
			}
		}
		
		find.append(" LIMIT ").append(page.getStartPosition()).append(", ").append(page.getNumPerPage());
		
		final PreparedStatement ps = conn.prepareStatement(find.build());
		DbUtil.fillParams(ps, find.getParamValueArray());
		return ps;
	}
	
	@Override
	public DialectName dialectName() {
		return DialectName.MYSQL;
	}
}
