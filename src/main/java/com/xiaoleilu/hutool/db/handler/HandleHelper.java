package com.xiaoleilu.hutool.db.handler;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.xiaoleilu.hutool.db.Entity;

/**
 * 数据结果集处理辅助类
 * @author loolly
 *
 */
public class HandleHelper {
	/**
	 * 处理单条数据
	 * @param columnCount 列数
	 * @param meta ResultSetMetaData
	 * @param rs 数据集
	 * @return 每一行的Entity
	 * @throws SQLException
	 */
	public static Entity handleRow(int columnCount, ResultSetMetaData meta, ResultSet rs) throws SQLException {
		final Entity row = Entity.create(meta.getTableName(1));
		String columnLabel;
		for (int i = 1; i <= columnCount; i++) {
			columnLabel = meta.getColumnLabel(i);
			row.put(columnLabel, rs.getObject(columnLabel));
		}
		return row;
	}
	
	/**
	 * 处理单条数据
	 * @param rs 数据集
	 * @return 每一行的Entity
	 * @throws SQLException
	 */
	public static Entity handleRow(ResultSet rs) throws SQLException {
		final ResultSetMetaData meta = rs.getMetaData();
		final int columnCount = meta.getColumnCount();
		final Entity row = Entity.create(meta.getTableName(1));
		String columnLabel;
		for (int i = 1; i <= columnCount; i++) {
			columnLabel = meta.getColumnLabel(i);
			row.put(columnLabel, rs.getObject(columnLabel));
		}
		return row;
	}
}
