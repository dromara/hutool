package com.xiaoleilu.hutool.db.handler;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.xiaoleilu.hutool.db.Entity;

/**
 * 结果集处理类 ，处理出的结果为Entity列表
 * @author loolly
 *
 */
public class EntityHandler implements RsHandler<List<Entity>>{
	
	/**
	 * 创建一个 EntityHandler对象
	 * @return EntityHandler对象
	 */
	public static EntityHandler create() {
		return new EntityHandler();
	}

	@Override
	public List<Entity> handle(ResultSet rs) throws SQLException {
		final ResultSetMetaData  meta = rs.getMetaData();
		final int columnCount = meta.getColumnCount();
		
		final List<Entity> result = new ArrayList<Entity>();
		while(rs.next()) {
			result.add(HandleHelper.handleRow(columnCount, meta, rs));
		}
		
		return result;
	}
}
