package cn.hutool.db.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 处理为字符串结果，当查询结果为单个字符串时使用此处理器
 * 
 * @author  weibaohui
 */
public class StringHandler implements RsHandler<String>{
	private static final long serialVersionUID = -5296733366845720383L;

	/**
	 * 创建一个 NumberHandler对象
	 * @return NumberHandler对象
	 */
	public static StringHandler create() {
		return new StringHandler();
	}

	@Override
	public String handle(ResultSet rs) throws SQLException {
		return rs.next() ? rs.getString(1) : null;
	}
}
