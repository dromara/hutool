package cn.hutool.db;

import java.sql.Connection;

/**
 * 控制{@link Connection}获取和关闭
 *
 * @author looly
 * @since 6.0.0
 */
public interface ConnectionHolder {
	/**
	 * 获得链接。根据实现不同，可以自定义获取连接的方式
	 *
	 * @return {@link Connection}
	 * @throws DbRuntimeException 连接获取异常
	 */
	Connection getConnection() throws DbRuntimeException;

	/**
	 * 关闭连接<br>
	 * 自定义关闭连接有利于自定义回收连接机制，或者不关闭
	 *
	 * @param conn 连接 {@link Connection}
	 */
	void closeConnection(Connection conn);
}
