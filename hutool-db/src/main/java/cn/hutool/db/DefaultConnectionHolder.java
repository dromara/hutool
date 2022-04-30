package cn.hutool.db;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 默认的连接持有器
 *
 * @author looly
 */
public class DefaultConnectionHolder implements ConnectionHolder {

	protected final DataSource ds;

	public DefaultConnectionHolder(DataSource ds) {
		this.ds = ds;
	}

	@Override
	public Connection getConnection() throws DbRuntimeException {
		try {
			return ThreadLocalConnection.INSTANCE.get(this.ds);
		} catch (final SQLException e) {
			throw new DbRuntimeException(e);
		}
	}

	@Override
	public void closeConnection(final Connection conn) {
		try {
			if (conn != null && false == conn.getAutoCommit()) {
				// 事务中的Session忽略关闭事件
				return;
			}
		} catch (final SQLException e) {
			// ignore
		}

		ThreadLocalConnection.INSTANCE.close(this.ds);
	}
}
