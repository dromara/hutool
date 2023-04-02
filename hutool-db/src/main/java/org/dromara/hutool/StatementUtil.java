/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool;

import org.dromara.hutool.collection.iter.ArrayIter;
import org.dromara.hutool.convert.Convert;
import org.dromara.hutool.handler.ResultSetUtil;
import org.dromara.hutool.handler.RsHandler;
import org.dromara.hutool.lang.Assert;
import org.dromara.hutool.map.MapUtil;
import org.dromara.hutool.sql.NamedSql;
import org.dromara.hutool.sql.SqlBuilder;
import org.dromara.hutool.sql.SqlLog;
import org.dromara.hutool.sql.SqlUtil;
import org.dromara.hutool.text.StrUtil;
import org.dromara.hutool.array.ArrayUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;
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
	 * 填充SQL的参数。
	 *
	 * @param ps     PreparedStatement
	 * @param params SQL参数
	 * @return {@link PreparedStatement}
	 * @throws SQLException SQL执行异常
	 */
	public static PreparedStatement fillParams(final PreparedStatement ps, final Object... params) throws SQLException {
		if (ArrayUtil.isEmpty(params)) {
			return ps;
		}
		return fillParams(ps, new ArrayIter<>(params));
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
		if (null == params) {
			return ps;// 无参数
		}

		int paramIndex = 1;//第一个参数从1计数
		for (final Object param : params) {
			setParam(ps, paramIndex++, param, nullTypeCache);
		}
		return ps;
	}

	/**
	 * 创建{@link PreparedStatement}
	 *
	 * @param conn       数据库连接
	 * @param sqlBuilder {@link SqlBuilder}包括SQL语句和参数
	 * @return {@link PreparedStatement}
	 * @throws SQLException SQL异常
	 * @since 4.1.3
	 */
	public static PreparedStatement prepareStatement(final Connection conn, final SqlBuilder sqlBuilder) throws SQLException {
		return prepareStatement(conn, sqlBuilder.build(), sqlBuilder.getParamValueArray());
	}

	/**
	 * 创建{@link PreparedStatement}
	 *
	 * @param conn   数据库连接
	 * @param sql    SQL语句，使用"?"做为占位符
	 * @param params "?"对应参数列表
	 * @return {@link PreparedStatement}
	 * @throws SQLException SQL异常
	 * @since 3.2.3
	 */
	public static PreparedStatement prepareStatement(final Connection conn, final String sql, final Collection<Object> params) throws SQLException {
		return prepareStatement(conn, sql, params.toArray(new Object[0]));
	}

	/**
	 * 创建{@link PreparedStatement}
	 *
	 * @param conn   数据库连接
	 * @param sql    SQL语句，使用"?"做为占位符
	 * @param params "?"对应参数列表或者Map表示命名参数
	 * @return {@link PreparedStatement}
	 * @throws SQLException SQL异常
	 * @since 3.2.3
	 */
	public static PreparedStatement prepareStatement(final Connection conn, String sql, Object... params) throws SQLException {
		Assert.notBlank(sql, "Sql String must be not blank!");
		sql = sql.trim();

		if(ArrayUtil.isNotEmpty(params) && 1 == params.length && params[0] instanceof Map){
			// 检查参数是否为命名方式的参数
			final NamedSql namedSql = new NamedSql(sql, Convert.toMap(String.class, Object.class, params[0]));
			sql = namedSql.getSql();
			params = namedSql.getParams();
		}

		SqlLog.INSTANCE.log(sql, ArrayUtil.isEmpty(params) ? null : params);
		final PreparedStatement ps;
		if (GlobalDbConfig.returnGeneratedKey && StrUtil.startWithIgnoreCase(sql, "insert")) {
			// 插入默认返回主键
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		} else {
			ps = conn.prepareStatement(sql);
		}
		return fillParams(ps, params);
	}

	/**
	 * 创建批量操作的{@link PreparedStatement}
	 *
	 * @param conn        数据库连接
	 * @param sql         SQL语句，使用"?"做为占位符
	 * @param paramsBatch "?"对应参数批次列表
	 * @return {@link PreparedStatement}
	 * @throws SQLException SQL异常
	 * @since 4.1.13
	 */
	public static PreparedStatement prepareStatementForBatch(final Connection conn, final String sql, final Object[]... paramsBatch) throws SQLException {
		return prepareStatementForBatch(conn, sql, new ArrayIter<>(paramsBatch));
	}

	/**
	 * 创建批量操作的{@link PreparedStatement}
	 *
	 * @param conn        数据库连接
	 * @param sql         SQL语句，使用"?"做为占位符
	 * @param paramsBatch "?"对应参数批次列表
	 * @return {@link PreparedStatement}
	 * @throws SQLException SQL异常
	 * @since 4.1.13
	 */
	public static PreparedStatement prepareStatementForBatch(final Connection conn, String sql, final Iterable<Object[]> paramsBatch) throws SQLException {
		Assert.notBlank(sql, "Sql String must be not blank!");

		sql = sql.trim();
		SqlLog.INSTANCE.log(sql, paramsBatch);
		final PreparedStatement ps = conn.prepareStatement(sql);
		final Map<Integer, Integer> nullTypeMap = new HashMap<>();
		for (final Object[] params : paramsBatch) {
			fillParams(ps, new ArrayIter<>(params), nullTypeMap);
			ps.addBatch();
		}
		return ps;
	}

	/**
	 * 创建批量操作的{@link PreparedStatement}
	 *
	 * @param conn     数据库连接
	 * @param sql      SQL语句，使用"?"做为占位符
	 * @param fields   字段列表，用于获取对应值
	 * @param entities "?"对应参数批次列表
	 * @return {@link PreparedStatement}
	 * @throws SQLException SQL异常
	 * @since 4.6.7
	 */
	public static PreparedStatement prepareStatementForBatch(final Connection conn, String sql, final Iterable<String> fields, final Entity... entities) throws SQLException {
		Assert.notBlank(sql, "Sql String must be not blank!");

		sql = sql.trim();
		SqlLog.INSTANCE.logForBatch(sql);
		final PreparedStatement ps = conn.prepareStatement(sql);
		//null参数的类型缓存，避免循环中重复获取类型
		final Map<Integer, Integer> nullTypeMap = new HashMap<>();
		for (final Entity entity : entities) {
			fillParams(ps, MapUtil.valuesOfKeys(entity, fields), nullTypeMap);
			ps.addBatch();
		}
		return ps;
	}

	/**
	 * 创建{@link CallableStatement}
	 *
	 * @param conn   数据库连接
	 * @param sql    SQL语句，使用"?"做为占位符
	 * @param params "?"对应参数列表
	 * @return {@link CallableStatement}
	 * @throws SQLException SQL异常
	 * @since 4.1.13
	 */
	public static CallableStatement prepareCall(final Connection conn, String sql, final Object... params) throws SQLException {
		Assert.notBlank(sql, "Sql String must be not blank!");

		sql = sql.trim();
		SqlLog.INSTANCE.log(sql, params);
		final CallableStatement call = conn.prepareCall(sql);
		fillParams(call, params);
		return call;
	}

	/**
	 * 获得自增键的值<br>
	 * 此方法对于Oracle无效（返回null）
	 *
	 * @param ps PreparedStatement
	 * @return 自增键的值，不存在返回null
	 * @throws SQLException SQL执行异常
	 */
	public static Long getGeneratedKeyOfLong(final Statement ps) throws SQLException {
		return getGeneratedKeys(ps, (rs)->{
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
	 * @param statement {@link Statement}
	 * @param rsHandler 主键结果集处理器
	 * @param <T> 自定义主键类型
	 * @return 主键
	 * @throws SQLException SQL执行异常
	 * @since 5.5.3
	 */
	public static <T> T getGeneratedKeys(final Statement statement, final RsHandler<T> rsHandler) throws SQLException {
		try (final ResultSet rs = statement.getGeneratedKeys()) {
			return rsHandler.handle(rs);
		}
	}

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

		int sqlType = Types.VARCHAR;

		final ParameterMetaData pmd;
		try {
			pmd = ps.getParameterMetaData();
			sqlType = pmd.getParameterType(paramIndex);
		} catch (final SQLException ignore) {
			// ignore
			// log.warn("Null param of index [{}] type get failed, by: {}", paramIndex, e.getMessage());
		}

		return sqlType;
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
		setParam(ps, paramIndex, param, null);
	}

	//--------------------------------------------------------------------------------------------- Private method start

	/**
	 * 为{@link PreparedStatement} 设置单个参数
	 *
	 * @param ps            {@link PreparedStatement}
	 * @param paramIndex    参数位置，从1开始
	 * @param param         参数，不能为{@code null}
	 * @param nullTypeCache 用于缓存参数为null位置的类型，避免重复获取
	 * @throws SQLException SQL异常
	 * @since 4.6.7
	 */
	private static void setParam(final PreparedStatement ps, final int paramIndex, final Object param, final Map<Integer, Integer> nullTypeCache) throws SQLException {
		if (null == param) {
			Integer type = (null == nullTypeCache) ? null : nullTypeCache.get(paramIndex);
			if (null == type) {
				type = getTypeOfNull(ps, paramIndex);
				if (null != nullTypeCache) {
					nullTypeCache.put(paramIndex, type);
				}
			}
			ps.setNull(paramIndex, type);
		}

		// 日期特殊处理，默认按照时间戳传入，避免毫秒丢失
		if (param instanceof java.util.Date) {
			if (param instanceof java.sql.Date) {
				ps.setDate(paramIndex, (java.sql.Date) param);
			} else if (param instanceof java.sql.Time) {
				ps.setTime(paramIndex, (java.sql.Time) param);
			} else {
				ps.setTimestamp(paramIndex, SqlUtil.toSqlTimestamp((java.util.Date) param));
			}
			return;
		}

		// 针对大数字类型的特殊处理
		if (param instanceof Number) {
			if (param instanceof BigDecimal) {
				// BigDecimal的转换交给JDBC驱动处理
				ps.setBigDecimal(paramIndex, (BigDecimal) param);
				return;
			}
			if (param instanceof BigInteger) {
				// BigInteger转为BigDecimal
				ps.setBigDecimal(paramIndex, new BigDecimal((BigInteger) param));
				return;
			}
			// 忽略其它数字类型，按照默认类型传入
		}

		// 其它参数类型
		ps.setObject(paramIndex, param);
	}
	//--------------------------------------------------------------------------------------------- Private method end
}
