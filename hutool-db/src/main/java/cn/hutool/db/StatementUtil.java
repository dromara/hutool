package cn.hutool.db;

import cn.hutool.core.collection.ArrayIter;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.sql.NamedSql;
import cn.hutool.db.sql.SqlBuilder;
import cn.hutool.db.sql.SqlLog;
import cn.hutool.db.sql.SqlUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.util.*;

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
	public static PreparedStatement fillParams(PreparedStatement ps, Object... params) throws SQLException {
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
	public static PreparedStatement fillParams(PreparedStatement ps, Iterable<?> params) throws SQLException {
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
	public static PreparedStatement fillParams(PreparedStatement ps, Iterable<?> params, Map<Integer, Integer> nullTypeCache) throws SQLException {
		if (null == params) {
			return ps;// 无参数
		}

		int paramIndex = 1;//第一个参数从1计数
		for (Object param : params) {
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
	public static PreparedStatement prepareStatement(Connection conn, SqlBuilder sqlBuilder) throws SQLException {
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
	public static PreparedStatement prepareStatement(Connection conn, String sql, Collection<Object> params) throws SQLException {
		return prepareStatement(conn, sql, params.toArray(new Object[0]));
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
	public static PreparedStatement prepareStatement(Connection conn, String sql, Object... params) throws SQLException {
		Assert.notBlank(sql, "Sql String must be not blank!");
		sql = sql.trim();

		if(ArrayUtil.isNotEmpty(params) && 1 == params.length && params[0] instanceof Map){
			// 检查参数是否为命名方式的参数
			final NamedSql namedSql = new NamedSql(sql, Convert.toMap(String.class, Object.class, params[0]));
			sql = namedSql.getSql();
			params = namedSql.getParams();
		}

		SqlLog.INSTANCE.log(sql, ArrayUtil.isEmpty(params) ? null : params);
		PreparedStatement ps;
		if (StrUtil.startWithIgnoreCase(sql, "insert")) {
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
	public static PreparedStatement prepareStatementForBatch(Connection conn, String sql, Object[]... paramsBatch) throws SQLException {
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
	public static PreparedStatement prepareStatementForBatch(Connection conn, String sql, Iterable<Object[]> paramsBatch) throws SQLException {
		Assert.notBlank(sql, "Sql String must be not blank!");

		sql = sql.trim();
		SqlLog.INSTANCE.log(sql, paramsBatch);
		PreparedStatement ps = conn.prepareStatement(sql);
		for (Object[] params : paramsBatch) {
			StatementUtil.fillParams(ps, params);
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
	public static PreparedStatement prepareStatementForBatch(Connection conn, String sql, List<String> fields, Entity... entities) throws SQLException {
		Assert.notBlank(sql, "Sql String must be not blank!");

		sql = sql.trim();
		SqlLog.INSTANCE.logForBatch(sql);
		PreparedStatement ps = conn.prepareStatement(sql);
		//null参数的类型缓存，避免循环中重复获取类型
		final Map<Integer, Integer> nullTypeMap = new HashMap<>();
		for (Entity entity : entities) {
			StatementUtil.fillParams(ps, CollectionUtil.valuesOfKeys(entity, fields), nullTypeMap);
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
	public static CallableStatement prepareCall(Connection conn, String sql, Object... params) throws SQLException {
		Assert.notBlank(sql, "Sql String must be not blank!");

		sql = sql.trim();
		SqlLog.INSTANCE.log(sql, params);
		final CallableStatement call = conn.prepareCall(sql);
		fillParams(call, params);
		return call;
	}

	/**
	 * 获得自增键的值<br>
	 * 此方法对于Oracle无效
	 *
	 * @param ps PreparedStatement
	 * @return 自增键的值
	 * @throws SQLException SQL执行异常
	 */
	public static Long getGeneratedKeyOfLong(Statement ps) throws SQLException {
		try (final ResultSet rs = ps.getGeneratedKeys()) {
			Long generatedKey = null;
			if (rs != null && rs.next()) {
				try {
					generatedKey = rs.getLong(1);
				} catch (SQLException e) {
					// 自增主键不为数字或者为Oracle的rowid，跳过
				}
			}
			return generatedKey;
		}
	}

	/**
	 * 获得所有主键
	 *
	 * @param ps PreparedStatement
	 * @return 所有主键
	 * @throws SQLException SQL执行异常
	 */
	public static List<Object> getGeneratedKeys(Statement ps) throws SQLException {
		final List<Object> keys = new ArrayList<>();
		try (final ResultSet rs = ps.getGeneratedKeys()) {
			if (null != rs) {
				int i = 1;
				while (rs.next()) {
					keys.add(rs.getObject(i++));
				}
			}
			return keys;
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
	public static int getTypeOfNull(PreparedStatement ps, int paramIndex) {
		int sqlType = Types.VARCHAR;

		final ParameterMetaData pmd;
		try {
			pmd = ps.getParameterMetaData();
			sqlType = pmd.getParameterType(paramIndex);
		} catch (SQLException ignore) {
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
	public static void setParam(PreparedStatement ps, int paramIndex, Object param) throws SQLException {
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
	private static void setParam(PreparedStatement ps, int paramIndex, Object param, Map<Integer, Integer> nullTypeCache) throws SQLException {
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
				// BigInteger转为Long
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
