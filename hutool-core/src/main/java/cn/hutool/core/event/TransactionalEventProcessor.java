package cn.hutool.core.event;

import cn.hutool.core.exceptions.ExceptionUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

/**
 * 加入事务的前置操作和后置操作<br>
 * 在前置操作中尝试开始事务，如果开始成功则进入事务中执行，开启的事务为非只读
 *
 * @author Create by liuwenhao on 2022/7/28 11:53
 */
public class TransactionalEventProcessor<E, R> implements EventProcessor<E, R> {

	/**
	 * 数据源
	 */
	DataSource dataSource;

	/**
	 * 是否开启了事务
	 */
	boolean isOpen = false;

	/**
	 * 连接
	 */
	Connection connection;

	public TransactionalEventProcessor(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void before(E event, ListenerDecorate<E, R> listenerDecorate) {
		try {
			connection.setAutoCommit(false);
			connection.setReadOnly(false);
			connection = dataSource.getConnection();
			isOpen = true;
		} catch (SQLException e) {
			// 获取连接失败则以非事务方式执行
		}

	}

	@Override
	public void after(E event, ListenerDecorate<E, R> listenerDecorate) {
		if (isOpen && Objects.nonNull(connection)) {
			try {
				if (Objects.isNull(listenerDecorate.getThrowResult())) {
					connection.commit();
				} else {
					connection.rollback();
				}
			} catch (SQLException e) {
				throw ExceptionUtil.wrapRuntime(e);
			}
		}
	}
}
