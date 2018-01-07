package cn.hutool.db.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 处理为数字结果，当查询结果为单个数字时使用此处理器（例如select count(1)）
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
