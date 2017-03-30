package com.xiaoleilu.hutool.db.handler;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.xiaoleilu.hutool.db.Entity;

/**
 * 
 * @author loolly
 *
 */
public class EntityHandler implements RsHandler<Entity>{
	
	/**
	 * 创建一个 SingleEntityHandler对象
	 * @return SingleEntityHandler对象
	 */
	public static EntityHandler create() {
		return new EntityHandler();
	}

	@Override
	public Entity handle(ResultSet rs) throws SQLException {
		final ResultSetMetaData  meta = rs.getMetaData();
		final int columnCount = meta.getColumnCount();
		
		return rs.next() ? HandleHelper.handleRow(columnCount, meta, rs) : null;
	}
}
