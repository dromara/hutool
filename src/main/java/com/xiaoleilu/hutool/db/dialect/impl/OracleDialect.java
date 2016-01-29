package com.xiaoleilu.hutool.db.dialect.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import com.xiaoleilu.hutool.db.DbUtil;
import com.xiaoleilu.hutool.db.Entity;
import com.xiaoleilu.hutool.db.Page;
import com.xiaoleilu.hutool.db.sql.Order;
import com.xiaoleilu.hutool.db.sql.SqlBuilder;
import com.xiaoleilu.hutool.db.sql.Wrapper;
import com.xiaoleilu.hutool.db.sql.SqlBuilder.LogicalOperator;
import com.xiaoleilu.hutool.exceptions.DbRuntimeException;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * Oracle 方言
 * @author loolly
 *
 */
public class OracleDialect extends AnsiSqlDialect{
	
	public OracleDialect() {
		wrapper = new Wrapper('"');	//Oracle所有字段名用双引号包围，防止字段名或表名与系统关键字冲突
	}
	
	@Override
	public PreparedStatement psForInsert(Connection conn, Entity entity) throws SQLException {
		if(null != wrapper) {
			//包装字段名
			entity = wrapper.wrap(entity);
		}
		
		final StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ").append(entity.getTableName()).append(" (");

		final StringBuilder placeHolder = new StringBuilder();
		placeHolder.append(") values(");

		final List<Object> paramValues = new ArrayList<Object>(entity.size());
		for (Entry<String, Object> entry : entity.entrySet()) {
			if (paramValues.size() > 0) {
				sql.append(", ");
				placeHolder.append(", ");
			}
			sql.append(entry.getKey());
			final Object value = entry.getValue();
			if(value instanceof String && ((String)value).toLowerCase().endsWith(".nextval")) {
				//Oracle的特殊自增键，通过字段名.nextval获得下一个值
				placeHolder.append(value);
			}else {
				placeHolder.append("?");
				paramValues.add(value);
			}
		}
		sql.append(placeHolder.toString()).append(")");

		final PreparedStatement ps = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
		DbUtil.fillParams(ps, paramValues);
		return ps;
	}
	
	@Override
	public PreparedStatement psForPage(Connection conn, Collection<String> fields, Entity where, Page page) throws SQLException {
		//验证
		if(where == null || StrUtil.isBlank(where.getTableName())) {
			throw new DbRuntimeException("Table name is null !");
		}
		
		final SqlBuilder find = SqlBuilder.create(wrapper)
				.select(fields)
				.from(where.getTableName())
				.where(LogicalOperator.AND, DbUtil.buildConditions(where));
		
		final Order[] orders = page.getOrders();
		if(null != orders){
			find.orderBy(orders);
		}
		
		int[] startEnd = page.getStartEnd();
		final StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM ( SELECT row_.*, rownum rownum_ from ( ")
			.append(find)
			.append(" ) row_ where rownum <= ").append(startEnd[1])
			.append(") table_alias")
			.append(" where table_alias.rownum_ >= ").append(startEnd[0]);
		
		final PreparedStatement ps = conn.prepareStatement(sql.toString());
		DbUtil.fillParams(ps, find.getParamValues());
		return ps;
	}
}
