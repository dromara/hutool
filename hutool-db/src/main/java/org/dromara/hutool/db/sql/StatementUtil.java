/*
 * Copyright (c) 2023-2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.db.sql;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.collection.iter.ArrayIter;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.db.DbException;
import org.dromara.hutool.db.config.DbConfig;
import org.dromara.hutool.db.handler.ResultSetUtil;
import org.dromara.hutool.db.handler.RsHandler;

import java.sql.*;
import java.util.List;
import java.util.Map;

/**
 * Statement和PreparedStatement工具类
 *
 * @author looly
 * @since 4.0.10
 */
public class StatementUtil {

	/**
	 * 创建{@link PreparedStatement}
	 *
	 * @param returnGeneratedKey 当为insert语句时，是否返回主键
	 * @param config               数据库配置
	 * @param conn                 数据库连接
	 * @param sql                  SQL语句，使用"?"做为占位符
	 * @param params               "?"对应参数列表或者Map表示命名参数
	 * @return {@link PreparedStatement}
	 * @since 5.8.19
	 */
	public static PreparedStatement prepareStatement(final boolean returnGeneratedKey, final DbConfig config, final Connection conn, final String sql, final Object... params) {
		return StatementBuilder.of()
			.setConnection(conn)
			.setReturnGeneratedKey(returnGeneratedKey)
			.setSqlFilter(config.getSqlFilters())
			.setSql(sql)
			.setParams(params)
			.build();
	}

	/**
	 * 创建批量操作的{@link PreparedStatement}
	 *
	 * @param config      数据库配置
	 * @param conn        数据库连接
	 * @param sql         SQL语句，使用"?"做为占位符
	 * @param paramsBatch "?"对应参数批次列表
	 * @return {@link PreparedStatement}
	 * @since 4.1.13
	 */
	public static PreparedStatement prepareStatementForBatch(final DbConfig config, final Connection conn, final String sql, final Object[]... paramsBatch) {
		return prepareStatementForBatch(config, conn, sql, new ArrayIter<>(paramsBatch));
	}

	/**
	 * 创建批量操作的{@link PreparedStatement}
	 *
	 * @param config      数据库配置
	 * @param conn        数据库连接
	 * @param sql         SQL语句，使用"?"做为占位符
	 * @param paramsBatch "?"对应参数批次列表
	 * @return {@link PreparedStatement}
	 * @since 4.1.13
	 */
	public static PreparedStatement prepareStatementForBatch(final DbConfig config, final Connection conn, final String sql,
															 final Iterable<Object[]> paramsBatch) {
		return StatementBuilder.of()
			.setConnection(conn)
			.setReturnGeneratedKey(false)
			.setSqlFilter(config.getSqlFilters())
			.setSql(sql)
			.setParams(ArrayUtil.ofArray(paramsBatch, Object.class))
			.buildForBatch();
	}

	/**
	 * 创建{@link CallableStatement}
	 *
	 * @param config 数据库配置
	 * @param conn   数据库连接
	 * @param sql    SQL语句，使用"?"做为占位符
	 * @param params "?"对应参数列表
	 * @return {@link CallableStatement}
	 * @throws SQLException SQL异常
	 * @since 4.1.13
	 */
	public static CallableStatement prepareCall(final DbConfig config, final Connection conn, final String sql, final Object... params) throws SQLException {
		return StatementBuilder.of()
			.setConnection(conn)
			.setSqlFilter(config.getSqlFilters())
			.setSql(sql)
			.setParams(params)
			.buildForCall();
	}

	// region ----- getGeneratedKey

	/**
	 * 获得自增键的值<br>
	 * 此方法对于Oracle无效（返回null）
	 *
	 * @param ps PreparedStatement
	 * @return 自增键的值，不存在返回null
	 * @throws SQLException SQL执行异常
	 */
	public static Long getGeneratedKeyOfLong(final Statement ps) throws SQLException {
		return getGeneratedKeys(ps, (rs) -> {
			Long generatedKey = null;
			if (rs != null && rs.next()) {
				try {
					generatedKey = rs.getLong(1);
				} catch (final SQLException e) {
					// 自增主键不为数字或者为Oracle的rowid，跳过
				}
			}
			return generatedKey;
		});
	}

	/**
	 * 获得所有主键
	 *
	 * @param ps PreparedStatement
	 * @return 所有主键
	 * @throws SQLException SQL执行异常
	 */
	public static List<Object> getGeneratedKeys(final Statement ps) throws SQLException {
		return getGeneratedKeys(ps, ResultSetUtil::handleRowToList);
	}

	/**
	 * 获取主键，并使用{@link RsHandler} 处理后返回
	 *
	 * @param statement {@link Statement}
	 * @param rsHandler 主键结果集处理器
	 * @param <T>       自定义主键类型
	 * @return 主键
	 * @throws SQLException SQL执行异常
	 * @since 5.5.3
	 */
	public static <T> T getGeneratedKeys(final Statement statement, final RsHandler<T> rsHandler) throws SQLException {
		try (final ResultSet rs = statement.getGeneratedKeys()) {
			return rsHandler.handle(rs);
		}
	}
	// endregion

	/**
	 * 获取null字段对应位置的数据类型<br>
	 * 有些数据库对于null字段必须指定类型，否则会插入报错，此方法用于获取其类型，如果获取失败，使用默认的{@link Types#VARCHAR}
	 *
	 * @param ps         {@link Statement}
	 * @param paramIndex 参数位置，第一位从1开始
	 * @return 数据类型，默认{@link Types#VARCHAR}
	 * @since 4.6.7
	 */
	public static int getTypeOfNull(final PreparedStatement ps, final int paramIndex) {
		Assert.notNull(ps, "ps PreparedStatement must be not null in (getTypeOfNull)!");
		return StatementWrapper.of(ps).getTypeOfNull(paramIndex);
	}

	/**
	 * 填充SQL的参数。
	 *
	 * @param ps     PreparedStatement
	 * @param params SQL参数
	 * @return {@link PreparedStatement}
	 * @throws SQLException SQL执行异常
	 */
	public static PreparedStatement fillArrayParam(final PreparedStatement ps, final Object... params) throws SQLException {
		return StatementWrapper.of(ps).fillArrayParam(params);
	}

	/**
	 * 填充SQL的参数。<br>
	 * 对于日期对象特殊处理：传入java.util.Date默认按照Timestamp处理
	 *
	 * @param ps     PreparedStatement
	 * @param params SQL参数
	 * @return {@link PreparedStatement}
	 * @throws SQLException SQL执行异常
	 */
	public static PreparedStatement fillParams(final PreparedStatement ps, final Iterable<?> params) throws SQLException {
		return fillParams(ps, params, null);
	}

	/**
	 * 填充SQL的参数。<br>
	 * 对于日期对象特殊处理：传入java.util.Date默认按照Timestamp处理
	 *
	 * @param ps            PreparedStatement
	 * @param params        SQL参数
	 * @param nullTypeCache null参数的类型缓存，避免循环中重复获取类型
	 * @return {@link PreparedStatement}
	 * @throws SQLException SQL执行异常
	 * @since 4.6.7
	 */
	public static PreparedStatement fillParams(final PreparedStatement ps, final Iterable<?> params, final Map<Integer, Integer> nullTypeCache) throws SQLException {
		return StatementWrapper.of(ps).fillParams(params, nullTypeCache);
	}

	/**
	 * 为{@link PreparedStatement} 设置单个参数
	 *
	 * @param ps         {@link PreparedStatement}
	 * @param paramIndex 参数位置，从1开始
	 * @param param      参数
	 * @throws SQLException SQL异常
	 * @since 4.6.7
	 */
	public static void setParam(final PreparedStatement ps, final int paramIndex, final Object param) throws SQLException {
		StatementWrapper.of(ps).setParam(paramIndex, param);
	}

	/**
	 * 执行查询
	 *
	 * @param ps  {@link PreparedStatement}
	 * @param rsh 结果集处理对象
	 * @param <T> 结果类型
	 * @return 结果对象
	 * @throws DbException SQL执行异常
	 * @since 4.1.13
	 */
	public static <T> T executeQuery(final PreparedStatement ps, final RsHandler<T> rsh) throws DbException {
		ResultSet rs = null;
		try {
			rs = ps.executeQuery();
			return rsh.handle(rs);
		} catch (final SQLException e) {
			throw new DbException(e);
		} finally {
			IoUtil.closeQuietly(rs);
		}
	}

	/**
	 * 用于执行 INSERT、UPDATE 或 DELETE 语句以及 SQL DDL（数据定义语言）语句，例如 CREATE TABLE 和 DROP TABLE。<br>
	 * INSERT、UPDATE 或 DELETE 语句的效果是修改表中零行或多行中的一列或多列。<br>
	 * executeUpdate 的返回值是一个整数（int），指示受影响的行数（即更新计数）。<br>
	 * 对于 CREATE TABLE 或 DROP TABLE 等不操作行的语句，executeUpdate 的返回值总为零。<br>
	 * 此方法不会关闭PreparedStatement
	 *
	 * @param ps     PreparedStatement对象
	 * @param params 参数
	 * @return 影响的行数
	 * @throws DbException SQL执行异常
	 */
	public static int executeUpdate(final PreparedStatement ps, final Object... params) throws DbException {
		try {
			StatementUtil.fillArrayParam(ps, params);
			return ps.executeUpdate();
		} catch (final SQLException e) {
			throw new DbException(e);
		}
	}
}
