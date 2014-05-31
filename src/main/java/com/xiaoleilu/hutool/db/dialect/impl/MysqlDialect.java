package com.xiaoleilu.hutool.db.dialect.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.xiaoleilu.hutool.PageUtil;
import com.xiaoleilu.hutool.db.DbUtil;
import com.xiaoleilu.hutool.db.Entity;

/**
 * MySQL方言
 * @author loolly
 *
 */
public class MysqlDialect extends AnsiSqlDialect{

	@Override
	public PreparedStatement psForPage(Connection conn, Collection<String> fields, Entity where, int page, int numPerPage) throws SQLException {
		final List<Object> paramValues = new ArrayList<Object>(where.size());
		final StringBuilder sql = buildSelectQuery(fields, where, paramValues);
		
		int[] startEnd = PageUtil.transToStartEnd(page, numPerPage);
		sql.append(" limit ").append(startEnd[0]).append(", ").append(startEnd[1]);
		
		final PreparedStatement ps = conn.prepareStatement(sql.toString());
		DbUtil.fillParams(ps, paramValues.toArray(new Object[paramValues.size()]));
		return ps;
	}
}
