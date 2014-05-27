package com.xiaoleilu.hutool.db;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 结果集处理接口
 * @author Luxiaolei
 *
 */
public interface RsHandler<T> {
	public T handle(ResultSet rs) throws SQLException;
}
