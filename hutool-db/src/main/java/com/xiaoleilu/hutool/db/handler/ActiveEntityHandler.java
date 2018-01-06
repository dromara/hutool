package com.xiaoleilu.hutool.db.handler;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.xiaoleilu.hutool.db.ActiveEntity;

/**
 * ActiveEntity对象处理器，只处理第一条数据
 * 
 * @author loolly
 *
 */
public class ActiveEntityHandler implements RsHandler<ActiveEntity>{
	
	/**
	 * 创建一个 ActiveEntityHandler对象
	 * @return ActiveEntityHandler对象
	 */
	public static ActiveEntityHandler create() {
		return new ActiveEntityHandler();
	}

	@Override
	public ActiveEntity handle(ResultSet rs) throws SQLException {
		final ResultSetMetaData  meta = rs.getMetaData();
		final int columnCount = meta.getColumnCount();
		
		return rs.next() ? HandleHelper.handleRow(ActiveEntity.create(),columnCount, meta, rs, true) : null;
	}
}
