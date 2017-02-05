package com.xiaoleilu.hutool.db.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * @author loolly
 *
 */
public class NumberHandler implements RsHandler<Number>{
	
	/**
	 * 创建一个 NumberHandler对象
	 * @return NumberHandler对象
	 */
	public static NumberHandler create() {
		return new NumberHandler();
	}

	@Override
	public Number handle(ResultSet rs) throws SQLException {
		return rs.next() ? rs.getBigDecimal(1) : null;
	}
}
