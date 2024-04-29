package cn.hutool.db.meta;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.DbRuntimeException;
import cn.hutool.db.DbUtil;
import cn.hutool.db.Entity;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * 数据库元数据信息工具类
 *
 * <p>
 * 需要注意的是，此工具类在某些数据库（比如Oracle）下无效，此时需要手动在数据库配置中增加：
 * <pre>
 *  remarks = true
 *  useInformationSchema = true
 * </pre>
 *
 * @author looly
 */
public class MetaUtil {
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
	 * @param ds    数据源
	 * @param types 表类型
	 * @return 表名列表
	 */
	public static List<String> getTables(DataSource ds, TableType... types) {
		return getTables(ds, null, null, types);
	}

	/**
	 * 获得所有表名
	 *
	 * @param ds     数据源
	 * @param schema 表数据库名，对于Oracle为用户名
	 * @param types  表类型
	 * @return 表名列表
	 * @since 3.3.1
	 */
	public static List<String> getTables(DataSource ds, String schema, TableType... types) {
		return getTables(ds, schema, null, types);
	}

	/**
	 * 获得所有表名
	 *
	 * @param ds        数据源
	 * @param schema    表数据库名，对于Oracle为用户名
	 * @param tableName 表名
	 * @param types     表类型
	 * @return 表名列表
	 * @since 3.3.1
	 */
	public static List<String> getTables(DataSource ds, String schema, String tableName, TableType... types) {
		final List<String> tables = new ArrayList<>();
		Connection conn = null;
		try {
			conn = ds.getConnection();

			// catalog和schema获取失败默认使用null代替
			final String catalog = getCatalog(conn);
			if (null == schema) {
				schema = getSchema(conn);
			}

			final DatabaseMetaData metaData = conn.getMetaData();
			try (final ResultSet rs = metaData.getTables(catalog, schema, tableName, Convert.toStrArray(types))) {
				if (null != rs) {
					String table;
					while (rs.next()) {
						table = rs.getString("TABLE_NAME");
						if (StrUtil.isNotBlank(table)) {
							tables.add(table);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new DbRuntimeException("Get tables error!", e);
		} finally {
			DbUtil.close(conn);
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
			final ResultSetMetaData rsmd = rs.getMetaData();
			final int columnCount = rsmd.getColumnCount();
			final String[] labelNames = new String[columnCount];
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
	 * @param ds        数据源
	 * @param tableName 表名
	 * @return 列数组
	 * @throws DbRuntimeException SQL执行异常
	 */
	public static String[] getColumnNames(DataSource ds, String tableName) {
		final List<String> columnNames = new ArrayList<>();
		Connection conn = null;
		try {
			conn = ds.getConnection();

			// catalog和schema获取失败默认使用null代替
			final String catalog = getCatalog(conn);
			final String schema = getSchema(conn);

			final DatabaseMetaData metaData = conn.getMetaData();
			try (final ResultSet rs = metaData.getColumns(catalog, schema, tableName, null)) {
				if (null != rs) {
					while (rs.next()) {
						columnNames.add(rs.getString("COLUMN_NAME"));
					}
				}
			}
			return columnNames.toArray(new String[0]);
		} catch (Exception e) {
			throw new DbRuntimeException("Get columns error!", e);
		} finally {
			DbUtil.close(conn);
		}
	}

	/**
	 * 创建带有字段限制的Entity对象<br>
	 * 此方法读取数据库中对应表的字段列表，加入到Entity中，当Entity被设置内容时，会忽略对应表字段外的所有KEY
	 *
	 * @param ds        数据源
	 * @param tableName 表名
	 * @return Entity对象
	 */
	public static Entity createLimitedEntity(DataSource ds, String tableName) {
		final String[] columnNames = getColumnNames(ds, tableName);
		return Entity.create(tableName).setFieldNames(columnNames);
	}

	/**
	 * 获得表的元信息<br>
	 * 注意如果需要获取注释，某些数据库如MySQL，需要在配置中添加:
	 * <pre>
	 *     remarks = true
	 *     useInformationSchema = true
	 * </pre>
	 *
	 * @param ds        数据源
	 * @param tableName 表名
	 * @return Table对象
	 */
	public static Table getTableMeta(DataSource ds, String tableName) {
		return getTableMeta(ds, null, null, tableName);
	}

	/**
	 * 获得表的元信息<br>
	 * 注意如果需要获取注释，某些数据库如MySQL，需要在配置中添加:
	 * <pre>
	 *     remarks = true
	 *     useInformationSchema = true
	 * </pre>
	 *
	 * @param ds        数据源
	 * @param tableName 表名
	 * @param catalog   catalog name，{@code null}表示自动获取，见：{@link #getCatalog(Connection)}
	 * @param schema    a schema name pattern，{@code null}表示自动获取，见：{@link #getSchema(Connection)}
	 * @return Table对象
	 * @since 5.7.22
	 */
	public static Table getTableMeta(DataSource ds, String catalog, String schema, String tableName) {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			return getTableMeta(conn, catalog, schema, tableName);
		} catch (final SQLException e){
			throw new DbRuntimeException(e);
		} finally {
			IoUtil.close(conn);
		}
	}

	/**
	 * 获得表的元信息<br>
	 * 注意如果需要获取注释，某些数据库如MySQL，需要在配置中添加:
	 * <pre>
	 *     remarks = true
	 *     useInformationSchema = true
	 * </pre>
	 *
	 * @param conn       数据库连接对象，使用结束后不会关闭。
	 * @param tableName 表名
	 * @param catalog   catalog name，{@code null}表示自动获取，见：{@link #getCatalog(Connection)}
	 * @param schema    a schema name pattern，{@code null}表示自动获取，见：{@link #getSchema(Connection)}
	 * @return Table对象
	 * @since 5.8.28
	 */
	public static Table getTableMeta(final Connection conn, String catalog, String schema, final String tableName) {
		final Table table = Table.create(tableName);

		// catalog和schema获取失败默认使用null代替
		if (null == catalog) {
			catalog = getCatalog(conn);
		}
		table.setCatalog(catalog);
		if (null == schema) {
			schema = getSchema(conn);
		}
		table.setSchema(schema);

		final DatabaseMetaData metaData = getMetaData(conn);
		// 获取原始表名
		final String pureTableName = unWrapIfOracle(metaData, tableName);
		// 获得表元数据（表注释）
		table.setComment(getRemarks(metaData, catalog, schema, pureTableName));
		// 获得主键
		table.setPkNames(getPrimaryKeys(metaData, catalog, schema, pureTableName));
		// 获得列
		fetchColumns(metaData, catalog, schema, table);
		// 获得索引信息(since 5.7.23)
		final Map<String, IndexInfo> indexInfoMap = getIndexInfo(metaData, catalog, schema, tableName);
		table.setIndexInfoList(ListUtil.toList(indexInfoMap.values()));

		return table;
	}

	/**
	 * 获取catalog，获取失败返回{@code null}
	 *
	 * @param conn {@link Connection} 数据库连接，{@code null}时返回null
	 * @return catalog，获取失败返回{@code null}
	 * @since 4.6.0
	 * @deprecated 拼写错误，请使用{@link #getCatalog(Connection)}
	 */
	@Deprecated
	public static String getCataLog(Connection conn) {
		return getCatalog(conn);
	}

	/**
	 * 获取catalog，获取失败返回{@code null}
	 *
	 * @param conn {@link Connection} 数据库连接，{@code null}时返回null
	 * @return catalog，获取失败返回{@code null}
	 * @since 5.7.23
	 */
	public static String getCatalog(Connection conn) {
		if (null == conn) {
			return null;
		}
		try {
			return conn.getCatalog();
		} catch (SQLException e) {
			// ignore
		}

		return null;
	}

	/**
	 * 获取schema，获取失败返回{@code null}
	 *
	 * @param conn {@link Connection} 数据库连接，{@code null}时返回null
	 * @return schema，获取失败返回{@code null}
	 * @since 4.6.0
	 */
	public static String getSchema(Connection conn) {
		if (null == conn) {
			return null;
		}
		try {
			return conn.getSchema();
		} catch (SQLException e) {
			// ignore
		}

		return null;
	}

	/**
	 * 获取数据库连接的元数据信息。
	 *
	 * @param conn 数据库连接对象。如果连接为null，则返回null。
	 * @return DatabaseMetaData 数据库元数据对象，如果获取失败或连接为null，则返回null。
	 * @since 5.8.28
	 */
	public static DatabaseMetaData getMetaData(final Connection conn) {
		if (null == conn) {
			return null;
		}
		try {
			return conn.getMetaData();
		} catch (final SQLException e) {
			// ignore
		}

		return null;
	}

	/**
	 * 获取指定表的备注信息。
	 *
	 * @param metaData  数据库元数据，用于查询表信息。
	 * @param catalog   目录名称，用于指定查询的数据库（可为{@code null}，表示任意目录）。
	 * @param schema    方案名称，用于指定表所属的schema（可为{@code null}，表示任意schema）。
	 * @param tableName 表名称，指定要查询备注信息的表。
	 * @return 表的备注信息。未找到指定的表或查询成功但无结果，则返回null。
	 * @since 5.8.28
	 */
	public static String getRemarks(final DatabaseMetaData metaData, final String catalog, final String schema, String tableName) {
		// issue#I9BANE Oracle中特殊表名需要解包
		tableName = unWrapIfOracle(metaData, tableName);

		try (final ResultSet rs = metaData.getTables(catalog, schema, tableName, new String[]{TableType.TABLE.value()})) {
			if (null != rs) {
				if (rs.next()) {
					return rs.getString("REMARKS");
				}
			}
		} catch (final SQLException e) {
			throw new DbRuntimeException(e);
		}
		// 未找到指定的表或查询成功但无结果
		return null;
	}

	/**
	 * 获取指定表的主键列名列表。
	 *
	 * @param metaData  数据库元数据，用于查询主键信息。
	 * @param catalog   数据库目录，用于限定查询范围。
	 * @param schema    数据库模式，用于限定查询范围。
	 * @param tableName 表名，指定要查询主键的表。
	 * @return 主键列名的列表。如果表没有主键，则返回空列表。
	 * @throws DbRuntimeException 如果查询过程中发生SQLException，将抛出DbRuntimeException。
	 * @since 5.8.28
	 */
	public static Set<String> getPrimaryKeys(final DatabaseMetaData metaData, final String catalog, final String schema, String tableName) {
		// issue#I9BANE Oracle中特殊表名需要解包
		tableName = unWrapIfOracle(metaData, tableName);

		// 初始化主键列表
		Set<String> primaryKeys = null;
		try (final ResultSet rs = metaData.getPrimaryKeys(catalog, schema, tableName)) {
			// 如果结果集不为空，遍历结果集获取主键列名
			if (null != rs) {
				primaryKeys = new LinkedHashSet<>(rs.getFetchSize(), 1);
				while (rs.next()) {
					primaryKeys.add(rs.getString("COLUMN_NAME"));
				}
			}
		} catch (final SQLException e) {
			// 将SQLException转换为自定义的DbException抛出
			throw new DbRuntimeException(e);
		}
		return primaryKeys;
	}

	/**
	 * 获取指定表的索引信息。
	 *
	 * @param metaData  数据库元数据，用于查询索引信息。
	 * @param catalog   数据库目录，用于限定查询范围。
	 * @param schema    数据库模式，用于限定查询范围。
	 * @param tableName 需要查询索引信息的表名。
	 * @return 返回一个映射，其中包含表的索引信息。键是表名和索引名的组合，值是索引信息对象。
	 * @since 5.8.28
	 */
	public static Map<String, IndexInfo> getIndexInfo(final DatabaseMetaData metaData, final String catalog, final String schema, final String tableName) {
		final Map<String, IndexInfo> indexInfoMap = new LinkedHashMap<>();

		try (final ResultSet rs = metaData.getIndexInfo(catalog, schema, tableName, false, false)) {
			if (null != rs) {
				while (rs.next()) {
					//排除统计（tableIndexStatistic）类型索引
					if (0 == rs.getShort("TYPE")) {
						continue;
					}

					final String indexName = rs.getString("INDEX_NAME");
					final String key = StrUtil.join("&", tableName, indexName);
					// 联合索引情况下一个索引会有多个列，此处须组合索引列到一个索引信息对象下
					IndexInfo indexInfo = indexInfoMap.get(key);
					if (null == indexInfo) {
						indexInfo = new IndexInfo(rs.getBoolean("NON_UNIQUE"), indexName, tableName, schema, catalog);
						indexInfoMap.put(key, indexInfo);
					}
					indexInfo.getColumnIndexInfoList().add(ColumnIndexInfo.create(rs));
				}
			}
		} catch (final SQLException e) {
			throw new DbRuntimeException(e);
		}
		return indexInfoMap;
	}

	/**
	 * 判断当前数据库是否为Oracle。
	 *
	 * @param metaData 数据库元数据，用于获取数据库产品名称。
	 * @return 返回true表示当前数据库是Oracle，否则返回false。
	 * @throws DbRuntimeException 如果获取数据库产品名称时发生SQLException，将抛出DbRuntimeException。
	 * @since 5.8.28
	 */
	public static boolean isOracle(final DatabaseMetaData metaData) throws DbRuntimeException {
		try {
			return StrUtil.equalsIgnoreCase("Oracle", metaData.getDatabaseProductName());
		} catch (final SQLException e) {
			throw new DbRuntimeException(e);
		}
	}

	/**
	 * 如果是在Oracle数据库中并且表名被双引号包裹，则移除这些引号。
	 *
	 * @param metaData  数据库元数据，用于判断是否为Oracle数据库。
	 * @param tableName 待处理的表名，可能被双引号包裹。
	 * @return 处理后的表名，如果原表名被双引号包裹且是Oracle数据库，则返回去除了双引号的表名；否则返回原表名。
	 */
	private static String unWrapIfOracle(final DatabaseMetaData metaData, String tableName) {
		final char wrapChar = '"';
		// 判断表名是否被双引号包裹且当前数据库为Oracle，如果是，则移除双引号
		if (StrUtil.isWrap(tableName, wrapChar) && isOracle(metaData)) {
			tableName = StrUtil.unWrap(tableName, wrapChar);
		}
		return tableName;
	}

	/**
	 * 从数据库元数据中获取指定表的列信息。
	 *
	 * @param metaData 数据库元数据，用于查询列信息。
	 * @param catalog  数据库目录，用于过滤列信息。
	 * @param schema   数据库模式，用于过滤列信息。
	 * @param table    表对象，用于存储获取到的列信息。
	 */
	private static void fetchColumns(final DatabaseMetaData metaData, final String catalog, final String schema, final Table table) {
		// issue#I9BANE Oracle中特殊表名需要解包
		final String tableName = unWrapIfOracle(metaData, table.getTableName());

		// 获得列
		try (final ResultSet rs = metaData.getColumns(catalog, schema, tableName, null)) {
			if (null != rs) {
				while (rs.next()) {
					table.setColumn(Column.create(table, rs));
				}
			}
		} catch (final SQLException e) {
			throw new DbRuntimeException(e);
		}
	}
}
