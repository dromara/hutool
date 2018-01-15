package cn.hutool.db;

import java.io.Closeable;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.dialect.Dialect;
import cn.hutool.db.dialect.DialectFactory;
import cn.hutool.db.ds.DSFactory;
import cn.hutool.db.meta.Column;
import cn.hutool.db.meta.Table;
import cn.hutool.db.meta.TableType;
import cn.hutool.db.sql.Condition;
import cn.hutool.db.sql.Condition.LikeType;
import cn.hutool.db.sql.SqlBuilder;
import cn.hutool.db.sql.SqlFormatter;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

/**
 * 数据库操作工具类
 * 
 * @author Luxiaolei
 * 
 */
public final class DbUtil {
	private final static Log log = LogFactory.get();

	/**
	 * 实例化一个新的SQL运行对象
	 * 
	 * @param dialect 数据源
	 * @return SQL执行类
	 */
	public static SqlConnRunner newSqlConnRunner(Dialect dialect) {
		return SqlConnRunner.create(dialect);
	}

	/**
	 * 实例化一个新的SQL运行对象
	 * 
	 * @param ds 数据源
	 * @return SQL执行类
	 */
	public static SqlConnRunner newSqlConnRunner(DataSource ds) {
		return SqlConnRunner.create(ds);
	}

	/**
	 * 实例化一个新的SQL运行对象
	 * 
	 * @param conn 数据库连接对象
	 * @return SQL执行类
	 */
	public static SqlConnRunner newSqlConnRunner(Connection conn) {
		return SqlConnRunner.create(DialectFactory.newDialect(conn));
	}

	/**
	 * 实例化一个新的SQL运行对象，使用默认数据源
	 * 
	 * @return SQL执行类
	 */
	public static SqlRunner newSqlRunner() {
		return SqlRunner.create(getDs());
	}

	/**
	 * 实例化一个新的SQL运行对象
	 * 
	 * @param ds 数据源
	 * @return SQL执行类
	 */
	public static SqlRunner newSqlRunner(DataSource ds) {
		return SqlRunner.create(ds);
	}

	/**
	 * 实例化一个新的SQL运行对象
	 * 
	 * @param ds 数据源
	 * @param dialect SQL方言
	 * @return SQL执行类
	 */
	public static SqlRunner newSqlRunner(DataSource ds, Dialect dialect) {
		return SqlRunner.create(ds, dialect);
	}

	/**
	 * 新建数据库会话，使用默认数据源
	 * 
	 * @return 数据库会话
	 */
	public static Session newSession() {
		return Session.create(getDs());
	}

	/**
	 * 新建数据库会话
	 * 
	 * @param ds 数据源
	 * @return 数据库会话
	 */
	public static Session newSession(DataSource ds) {
		return Session.create(ds);
	}

	/**
	 * 新建数据库会话
	 * 
	 * @param conn 数据库连接对象
	 * @return 数据库会话
	 */
	public static Session newSession(Connection conn) {
		return Session.create(conn);
	}

	/**
	 * 连续关闭一系列的SQL相关对象<br>
	 * 这些对象必须按照顺序关闭，否则会出错。
	 * 
	 * @param objsToClose 需要关闭的对象
	 */
	public static void close(Object... objsToClose) {
		for (Object obj : objsToClose) {
			if (obj instanceof AutoCloseable) {
				IoUtil.close((AutoCloseable) obj);
			} else if (obj instanceof Closeable) {
				IoUtil.close((Closeable) obj);
			} else {
				try {
					if (obj != null) {
						if (obj instanceof ResultSet) {
							((ResultSet) obj).close();
						} else if (obj instanceof Statement) {
							((Statement) obj).close();
						} else if (obj instanceof PreparedStatement) {
							((PreparedStatement) obj).close();
						} else if (obj instanceof Connection) {
							((Connection) obj).close();
						} else {
							log.warn("Object {} not a ResultSet or Statement or PreparedStatement or Connection!", obj.getClass().getName());
						}
					}
				} catch (SQLException e) {
					// ignore
				}
			}
		}
	}

	/**
	 * 获得默认数据源
	 * 
	 * @return 默认数据源
	 */
	public static DataSource getDs() {
		return DSFactory.get();
	}

	/**
	 * 获取指定分组的数据源
	 * 
	 * @param group 分组
	 * @return 数据源
	 */
	public static DataSource getDs(String group) {
		return DSFactory.get(group);
	}

	/**
	 * 获得JNDI数据源
	 * 
	 * @param jndiName JNDI名称
	 * @return 数据源
	 */
	public static DataSource getJndiDsWithLog(String jndiName) {
		try {
			return getJndiDs(jndiName);
		} catch (DbRuntimeException e) {
			log.error(e.getCause(), "Find JNDI datasource error!");
		}
		return null;
	}

	/**
	 * 获得JNDI数据源
	 * 
	 * @param jndiName JNDI名称
	 * @return 数据源
	 */
	public static DataSource getJndiDs(String jndiName) {
		try {
			return (DataSource) new InitialContext().lookup(jndiName);
		} catch (NamingException e) {
			throw new DbRuntimeException(e);
		}
	}

	/**
	 * 获得所有表名
	 * 
	 * @param ds 数据源
	 * @return 表名列表
	 */
	public static List<String> getTables(DataSource ds) {
		return getTables(ds, TableType.TABLE);
	}
	
	/**
	 * 获得所有表名
	 * 
	 * @param ds 数据源
	 * @param types 表类型
	 * @return 表名列表
	 */
	public static List<String> getTables(DataSource ds, TableType... types) {
		return getTables(ds, null, null, types);
	}
	
	/**
	 * 获得所有表名
	 * 
	 * @param ds 数据源
	 * @param schema 表数据库名，对于Oracle为用户名
	 * @param types 表类型
	 * @return 表名列表
	 * @since 3.3.1
	 */
	public static List<String> getTables(DataSource ds, String schema, TableType... types) {
		return getTables(ds, schema, null, types);
	}

	/**
	 * 获得所有表名
	 * 
	 * @param ds 数据源
	 * @param schema 表数据库名，对于Oracle为用户名
	 * @param tableName 表名
	 * @param types 表类型
	 * @return 表名列表
	 * @since 3.3.1
	 */
	public static List<String> getTables(DataSource ds, String schema, String tableName, TableType... types) {
		final List<String> tables = new ArrayList<String>();
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = ds.getConnection();
			final DatabaseMetaData metaData = conn.getMetaData();
			rs = metaData.getTables(conn.getCatalog(), schema, tableName, Convert.toStrArray(types));
			if (rs == null) {
				return null;
			}
			String table;
			while (rs.next()) {
				table = rs.getString("TABLE_NAME");
				if (StrUtil.isNotBlank(table)) {
					tables.add(table);
				}
			}
		} catch (Exception e) {
			throw new DbRuntimeException("Get tables error!", e);
		} finally {
			close(rs, conn);
		}
		return tables;
	}

	/**
	 * 获得结果集的所有列名
	 * 
	 * @param rs 结果集
	 * @return 列名数组
	 * @throws DbRuntimeException SQL执行异常
	 */
	public static String[] getColumnNames(ResultSet rs) throws DbRuntimeException {
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			String[] labelNames = new String[columnCount];
			for (int i = 0; i < labelNames.length; i++) {
				labelNames[i] = rsmd.getColumnLabel(i + 1);
			}
			return labelNames;
		} catch (Exception e) {
			throw new DbRuntimeException("Get colunms error!", e);
		}
	}

	/**
	 * 获得表的所有列名
	 * 
	 * @param ds 数据源
	 * @param tableName 表名
	 * @return 列数组
	 * @throws DbRuntimeException SQL执行异常
	 */
	public static String[] getColumnNames(DataSource ds, String tableName) {
		List<String> columnNames = new ArrayList<String>();
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = ds.getConnection();
			final DatabaseMetaData metaData = conn.getMetaData();
			rs = metaData.getColumns(conn.getCatalog(), null, tableName, null);
			while (rs.next()) {
				columnNames.add(rs.getString("COLUMN_NAME"));
			}
			return columnNames.toArray(new String[columnNames.size()]);
		} catch (Exception e) {
			throw new DbRuntimeException("Get columns error!", e);
		} finally {
			close(rs, conn);
		}
	}

	/**
	 * 创建带有字段限制的Entity对象<br>
	 * 此方法读取数据库中对应表的字段列表，加入到Entity中，当Entity被设置内容时，会忽略对应表字段外的所有KEY
	 * 
	 * @param ds 数据源
	 * @param tableName 表名
	 * @return Entity对象
	 */
	public static Entity createLimitedEntity(DataSource ds, String tableName) {
		String[] columnNames = getColumnNames(ds, tableName);
		return Entity.create(tableName).setFieldNames(columnNames);
	}

	/**
	 * 获得表的元信息
	 * 
	 * @param ds 数据源
	 * @param tableName 表名
	 * @return Table对象
	 */
	@SuppressWarnings("resource")
	public static Table getTableMeta(DataSource ds, String tableName) {
		final Table table = Table.create(tableName);
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = ds.getConnection();
			final DatabaseMetaData metaData = conn.getMetaData();
			// 获得主键
			rs = metaData.getPrimaryKeys(conn.getCatalog(), null, tableName);
			while (rs.next()) {
				table.addPk(rs.getString("COLUMN_NAME"));
			}

			// 获得列
			rs = metaData.getColumns(conn.getCatalog(), null, tableName, null);
			while (rs.next()) {
				table.setColumn(Column.create(tableName, rs));
			}
		} catch (SQLException e) {
			throw new DbRuntimeException("Get columns error!", e);
		} finally {
			close(rs, conn);
		}

		return table;
	}

	/**
	 * 填充SQL的参数。
	 * 
	 * @param ps PreparedStatement
	 * @param params SQL参数
	 * @return {@link PreparedStatement}
	 * @throws SQLException SQL执行异常
	 */
	public static PreparedStatement fillParams(PreparedStatement ps, Collection<Object> params) throws SQLException {
		return fillParams(ps, params.toArray(new Object[params.size()]));
	}

	/**
	 * 填充SQL的参数。<br>
	 * 对于日期对象特殊处理：传入java.util.Date默认按照Timestamp处理
	 * 
	 * @param ps PreparedStatement
	 * @param params SQL参数
	 * @return {@link PreparedStatement}
	 * @throws SQLException SQL执行异常
	 */
	public static PreparedStatement fillParams(PreparedStatement ps, Object... params) throws SQLException {
		if (ArrayUtil.isEmpty(params)) {
			return ps;// 无参数
		}
		Object param;
		for (int i = 0; i < params.length; i++) {
			int paramIndex = i + 1;
			param = params[i];
			if (null != param) {
				if (param instanceof java.util.Date) {
					// 日期特殊处理
					if (param instanceof java.sql.Date) {
						ps.setDate(paramIndex, (java.sql.Date) param);
					} else if (param instanceof java.sql.Time) {
						ps.setTime(paramIndex, (java.sql.Time)param);
					} else {
						ps.setTimestamp(paramIndex, toSqlTimestamp((java.util.Date) param));
					}
				}else if(param instanceof Number) {
					//针对大数字类型的特殊处理
					if(param instanceof BigInteger) {
						//BigInteger转为Long
						ps.setLong(paramIndex, ((BigInteger)param).longValue());
					}else if(param instanceof BigDecimal) {
						//BigDecimal的转换交给JDBC驱动处理
						ps.setBigDecimal(paramIndex, (BigDecimal)param);
					}
				} else {
					ps.setObject(paramIndex, param);
				}
			} else {
				final ParameterMetaData pmd = ps.getParameterMetaData();
				int sqlType = Types.VARCHAR;
				try {
					sqlType = pmd.getParameterType(paramIndex);
				} catch (SQLException e) {
					// ignore
					// log.warn("Null param of index [{}] type get failed, by: {}", paramIndex, e.getMessage());
				}
				ps.setNull(paramIndex, sqlType);
			}
		}
		return ps;
	}

	/**
	 * 创建{@link PreparedStatement}
	 * 
	 * @param conn 数据库连接
	 * @param sql SQL语句，使用"?"做为占位符
	 * @param params "?"对应参数列表
	 * @return {@link PreparedStatement}
	 * @throws SQLException SQL异常
	 * @since 3.2.3
	 */
	public static PreparedStatement prepareStatement(Connection conn, String sql, Collection<Object> params) throws SQLException {
		return prepareStatement(conn, sql, params.toArray(new Object[params.size()]));
	}

	/**
	 * 创建{@link PreparedStatement}
	 * 
	 * @param conn 数据库连接
	 * @param sql SQL语句，使用"?"做为占位符
	 * @param params "?"对应参数列表
	 * @return {@link PreparedStatement}
	 * @throws SQLException SQL异常
	 * @since 3.2.3
	 */
	public static PreparedStatement prepareStatement(Connection conn, String sql, Object... params) throws SQLException {
		Assert.notBlank(sql, "Sql String must be not blank!");

		sql = sql.trim();
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
	 * 获得自增键的值<br>
	 * 此方法对于Oracle无效
	 * 
	 * @param ps PreparedStatement
	 * @return 自增键的值
	 * @throws SQLException SQL执行异常
	 */
	public static Long getGeneratedKeyOfLong(PreparedStatement ps) throws SQLException {
		ResultSet rs = null;
		try {
			rs = ps.getGeneratedKeys();
			Long generatedKey = null;
			if (rs != null && rs.next()) {
				try {
					generatedKey = rs.getLong(1);
				} catch (SQLException e) {
					// 自增主键不为数字或者为Oracle的rowid，跳过
				}
			}
			return generatedKey;
		} catch (SQLException e) {
			throw e;
		} finally {
			close(rs);
		}
	}

	/**
	 * 获得所有主键<br>
	 * 
	 * @param ps PreparedStatement
	 * @return 所有主键
	 * @throws SQLException SQL执行异常
	 */
	public static List<Object> getGeneratedKeys(PreparedStatement ps) throws SQLException {
		List<Object> keys = new ArrayList<Object>();
		ResultSet rs = null;
		int i = 1;
		try {
			rs = ps.getGeneratedKeys();
			if (rs != null && rs.next()) {
				keys.add(rs.getObject(i++));
			}
			return keys;
		} catch (SQLException e) {
			throw e;
		} finally {
			close(rs);
		}
	}

	/**
	 * 构件相等条件的where语句<br>
	 * 如果没有条件语句，泽返回空串，表示没有条件
	 * 
	 * @param entity 条件实体
	 * @param paramValues 条件值得存放List
	 * @return 带where关键字的SQL部分
	 */
	public static String buildEqualsWhere(Entity entity, List<Object> paramValues) {
		if (null == entity || entity.isEmpty()) {
			return StrUtil.EMPTY;
		}

		final StringBuilder sb = new StringBuilder(" WHERE ");
		boolean isNotFirst = false;
		for (Entry<String, Object> entry : entity.entrySet()) {
			if (isNotFirst) {
				sb.append(" and ");
			} else {
				isNotFirst = true;
			}
			sb.append("`").append(entry.getKey()).append("`").append(" = ?");
			paramValues.add(entry.getValue());
		}

		return sb.toString();
	}

	/**
	 * 通过实体对象构建条件对象
	 * 
	 * @param entity 实体对象
	 * @return 条件对象
	 */
	public static Condition[] buildConditions(Entity entity) {
		if (null == entity || entity.isEmpty()) {
			return null;
		}

		final Condition[] conditions = new Condition[entity.size()];
		int i = 0;
		Object value;
		for (Entry<String, Object> entry : entity.entrySet()) {
			value = entry.getValue();
			if (value instanceof Condition) {
				conditions[i++] = (Condition) value;
			} else {
				conditions[i++] = new Condition(entry.getKey(), value);
			}
		}

		return conditions;
	}

	/**
	 * 创建LIKE语句中的值
	 * 
	 * @param value 被查找值
	 * @param likeType LIKE值类型 {@link LikeType}
	 * @return 拼接后的like值
	 */
	public static String buildLikeValue(String value, LikeType likeType, boolean withLikeKeyword) {
		StringBuilder likeValue = StrUtil.builder(withLikeKeyword ? "LIKE " : "");
		switch (likeType) {
		case StartWith:
			likeValue.append('%').append(value);
			break;
		case EndWith:
			likeValue.append(value).append('%');
			break;
		case Contains:
			likeValue.append('%').append(value).append('%');
			break;

		default:
			break;
		}
		return likeValue.toString();
	}

	/**
	 * 通过JDBC URL等信息识别JDBC驱动名
	 * 
	 * @param nameContainsProductInfo 包含数据库标识的字符串
	 * @return 驱动
	 * @see DialectFactory#identifyDriver(String)
	 */
	public static String identifyDriver(String nameContainsProductInfo) {
		return DialectFactory.identifyDriver(nameContainsProductInfo);
	}

	/**
	 * 识别JDBC驱动名
	 * 
	 * @param ds 数据源
	 * @return 驱动
	 */
	public static String identifyDriver(DataSource ds) {
		Connection conn = null;
		String driver = null;
		try {
			try {
				conn = ds.getConnection();
			} catch (SQLException e) {
				throw new DbRuntimeException("Get Connection error !", e);
			} catch (NullPointerException e) {
				throw new DbRuntimeException("Unexpected NullPointException, maybe [jdbcUrl] or [url] is empty!", e);
			}
			driver = identifyDriver(conn);
		} finally {
			close(conn);
		}

		return driver;
	}

	/**
	 * 识别JDBC驱动名
	 * 
	 * @param conn 数据库连接对象
	 * @return 驱动
	 * @throws DbRuntimeException SQL异常包装，获取元数据信息失败
	 */
	public static String identifyDriver(Connection conn) throws DbRuntimeException {
		String driver = null;
		try {
			DatabaseMetaData meta = conn.getMetaData();
			driver = identifyDriver(meta.getDatabaseProductName());
			if (StrUtil.isBlank(driver)) {
				driver = identifyDriver(meta.getDriverName());
			}
		} catch (SQLException e) {
			throw new DbRuntimeException("Identify driver error!", e);
		}

		return driver;
	}

	/**
	 * 验证实体类对象的有效性
	 * 
	 * @param entity 实体类对象
	 * @throws DbRuntimeException SQL异常包装，获取元数据信息失败
	 */
	public static void validateEntity(Entity entity) throws DbRuntimeException {
		if (null == entity) {
			throw new DbRuntimeException("Entity is null !");
		}
		if (StrUtil.isBlank(entity.getTableName())) {
			throw new DbRuntimeException("Entity`s table name is null !");
		}
		if (entity.isEmpty()) {
			throw new DbRuntimeException("No filed and value in this entity !");
		}
	}

	/**
	 * 将RowId转为字符串
	 * 
	 * @param rowId RowId
	 * @return RowId字符串
	 */
	public static String rowIdToString(RowId rowId) {
		return StrUtil.str(rowId.getBytes(), CharsetUtil.CHARSET_ISO_8859_1);
	}

	/**
	 * 格式化SQL
	 * 
	 * @param sql SQL
	 * @return 格式化后的SQL
	 */
	public static String formatSql(String sql) {
		return SqlFormatter.format(sql);
	}

	/**
	 * Clob字段值转字符串
	 * 
	 * @param clob {@link Clob}
	 * @return 字符串
	 * @since 3.0.6
	 */
	public static String clobToStr(Clob clob) {
		Reader reader = null;
		try {
			reader = clob.getCharacterStream();
			return IoUtil.read(reader);
		} catch (SQLException e) {
			throw new DbRuntimeException(e);
		} finally {
			IoUtil.close(reader);
		}
	}

	/**
	 * Blob字段值转字符串
	 * 
	 * @param blob {@link Blob}
	 * @param charset 编码
	 * @return 字符串
	 * @since 3.0.6
	 */
	public static String blobToStr(Blob blob, Charset charset) {
		InputStream in = null;
		try {
			in = blob.getBinaryStream();
			return IoUtil.read(in, charset);
		} catch (SQLException e) {
			throw new DbRuntimeException(e);
		} finally {
			IoUtil.close(in);
		}
	}

	/**
	 * 转换为{@link java.sql.Date}
	 * 
	 * @param date {@link java.util.Date}
	 * @return {@link java.sql.Date}
	 * @since 3.1.2
	 */
	public static java.sql.Date toSqlDate(java.util.Date date) {
		return new java.sql.Date(date.getTime());
	}

	/**
	 * 转换为{@link java.sql.Timestamp}
	 * 
	 * @param date {@link java.util.Date}
	 * @return {@link java.sql.Timestamp}
	 * @since 3.1.2
	 */
	public static java.sql.Timestamp toSqlTimestamp(java.util.Date date) {
		return new java.sql.Timestamp(date.getTime());
	}

	/**
	 * 设置全局配置：是否通过debug日志显示SQL
	 * 
	 * @param isShowSql 是否显示SQL
	 * @param isFormatSql 是否格式化显示的SQL
	 * @since 3.1.2
	 */
	public static void setShowSqlGlobal(boolean isShowSql, boolean isFormatSql) {
		SqlBuilder.setShowSql(true, isFormatSql);
	}
	// ---------------------------------------------------------------------------- Private method start
	// ---------------------------------------------------------------------------- Private method end
}
