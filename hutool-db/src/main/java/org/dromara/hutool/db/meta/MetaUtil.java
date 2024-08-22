/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.db.meta;

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.db.DbException;
import org.dromara.hutool.db.Entity;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

	// region ----- getTableNames

	/**
	 * 获得所有表名
	 *
	 * @param ds 数据源
	 * @return 表名列表
	 */
	public static List<String> getTableNames(final DataSource ds) {
		return getTableNames(ds, TableType.TABLE);
	}

	/**
	 * 获得所有表名
	 *
	 * @param ds    数据源
	 * @param types 表类型
	 * @return 表名列表
	 */
	public static List<String> getTableNames(final DataSource ds, final TableType... types) {
		return getTableNames(ds, null, null, types);
	}

	/**
	 * 获得所有表名
	 *
	 * @param ds     数据源
	 * @param schema 要使用的数据库模式（在某些数据库系统中相当于命名空间，对于Oracle为用户名）
	 * @param types  表类型
	 * @return 表名列表
	 * @since 3.3.1
	 */
	public static List<String> getTableNames(final DataSource ds, final String schema, final TableType... types) {
		return getTableNames(ds, schema, null, types);
	}

	/**
	 * 获得所有表名
	 *
	 * @param ds               数据源
	 * @param schema           要使用的数据库模式（在某些数据库系统中相当于命名空间，对于Oracle为用户名）
	 * @param tableNamePattern 表名匹配模式，null表示匹配所有表
	 * @param types            表类型
	 * @return 表名列表
	 * @since 3.3.1
	 */
	public static List<String> getTableNames(final DataSource ds, final String schema, final String tableNamePattern, final TableType... types) {
		return getTableNames(ds, null, schema, tableNamePattern, types);
	}

	/**
	 * 获得所有表名
	 *
	 * @param ds               数据源
	 * @param catalog          要使用的数据库目录（在某些数据库系统中相当于数据库名称）
	 * @param schema           要使用的数据库模式（在某些数据库系统中相当于命名空间，对于Oracle为用户名）
	 * @param tableNamePattern 表名匹配模式，null表示匹配所有表
	 * @param types            表类型
	 * @return 表名列表
	 * @since 3.3.1
	 */
	public static List<String> getTableNames(final DataSource ds, String catalog, String schema, final String tableNamePattern, final TableType... types) {
		Connection conn = null;
		try {
			conn = ds.getConnection();

			// catalog和schema获取失败默认使用null代替
			if (null == schema) {
				catalog = getCatalog(conn);
			}
			if (null == schema) {
				schema = getSchema(conn);
			}

			return DatabaseMetaDataWrapper.of(conn.getMetaData(), catalog, schema).getTableNames(tableNamePattern, types);
		} catch (final Exception e) {
			throw new DbException("Get tables error!", e);
		} finally {
			IoUtil.closeQuietly(conn);
		}
	}
	// endregion

	// region ----- getColumnNames

	/**
	 * 获得结果集的所有列名
	 *
	 * @param rs 结果集
	 * @return 列名数组
	 * @throws DbException SQL执行异常
	 */
	public static String[] getColumnNames(final ResultSet rs) throws DbException {
		final ResultSetMetaData metaData;
		try {
			metaData = rs.getMetaData();
		} catch (final SQLException e) {
			throw new DbException(e);
		}
		return getColumnNames(metaData);
	}

	/**
	 * 获得结果集的所有列名
	 *
	 * @param metaData {@link ResultSetMetaData}
	 * @return 列名数组
	 * @throws DbException SQL执行异常
	 */
	public static String[] getColumnNames(final ResultSetMetaData metaData) throws DbException {
		try {
			final int columnCount = metaData.getColumnCount();
			final String[] labelNames = new String[columnCount];
			for (int i = 0; i < labelNames.length; i++) {
				labelNames[i] = metaData.getColumnLabel(i + 1);
			}
			return labelNames;
		} catch (final SQLException e) {
			throw new DbException("Get colunms error!", e);
		}
	}

	/**
	 * 获得表的所有列名
	 *
	 * @param ds        数据源
	 * @param tableName 表名
	 * @return 列数组
	 * @throws DbException SQL执行异常
	 */
	public static String[] getColumnNames(final DataSource ds, final String tableName) {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			return DatabaseMetaDataWrapper.of(conn).getColumnNames(tableName);
		} catch (final Exception e) {
			throw new DbException("Get columns error!", e);
		} finally {
			IoUtil.closeQuietly(conn);
		}
	}
	// endregion

	/**
	 * 创建带有字段限制的Entity对象<br>
	 * 此方法读取数据库中对应表的字段列表，加入到Entity中，当Entity被设置内容时，会忽略对应表字段外的所有KEY
	 *
	 * @param ds        数据源
	 * @param tableName 表名
	 * @return Entity对象
	 */
	public static Entity createLimitedEntity(final DataSource ds, final String tableName) {
		final String[] columnNames = getColumnNames(ds, tableName);
		return Entity.of(tableName).setFieldNames(columnNames);
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
	public static Table getTableMeta(final DataSource ds, final String tableName) {
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
	public static Table getTableMeta(final DataSource ds, final String catalog, final String schema, final String tableName) {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			return getTableMeta(conn, catalog, schema, tableName);
		} catch (final SQLException e) {
			throw new DbException(e);
		} finally {
			IoUtil.closeQuietly(conn);
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
	 * @param conn      数据库连接对象，使用结束后不会关闭。
	 * @param tableName 表名
	 * @param catalog   catalog name，{@code null}表示自动获取，见：{@link #getCatalog(Connection)}
	 * @param schema    a schema name pattern，{@code null}表示自动获取，见：{@link #getSchema(Connection)}
	 * @return Table对象
	 * @since 5.8.28
	 */
	public static Table getTableMeta(final Connection conn, String catalog, String schema, final String tableName) {
		final Table table = Table.of(tableName);

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
		final DatabaseMetaDataWrapper metaDataWrapper = DatabaseMetaDataWrapper.of(metaData, catalog, schema);

		// 获取原始表名
		final String pureTableName = metaDataWrapper.getPureTableName(tableName);
		table.setPureTableName(pureTableName);

		// 获得表元数据（表注释）
		table.setRemarks(metaDataWrapper.getRemarks(pureTableName));
		// 获得主键
		table.setPkNames(metaDataWrapper.getPrimaryKeys(pureTableName));
		// 获得列
		metaDataWrapper.fetchColumns(table);
		// 获得索引信息(since 5.7.23)
		final Map<String, IndexInfo> indexInfoMap = metaDataWrapper.getIndexInfo(tableName);
		table.setIndexInfoList(ListUtil.of(indexInfoMap.values()));

		return table;
	}

	/**
	 * 获取catalog，获取失败返回{@code null}
	 *
	 * @param conn {@link Connection} 数据库连接，{@code null}时返回null
	 * @return catalog，获取失败返回{@code null}
	 * @since 5.7.23
	 */
	public static String getCatalog(final Connection conn) {
		if (null == conn) {
			return null;
		}
		try {
			return conn.getCatalog();
		} catch (final SQLException e) {
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
	public static String getSchema(final Connection conn) {
		if (null == conn) {
			return null;
		}
		try {
			return conn.getSchema();
		} catch (final SQLException e) {
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
	public static String getRemarks(final DatabaseMetaData metaData, final String catalog, final String schema, final String tableName) {
		return DatabaseMetaDataWrapper.of(metaData, catalog, schema).getRemarks(tableName);
	}

	/**
	 * 获取指定表的主键列名列表。
	 *
	 * @param metaData  数据库元数据，用于查询主键信息。
	 * @param catalog   数据库目录，用于限定查询范围。
	 * @param schema    数据库模式，用于限定查询范围。
	 * @param tableName 表名，指定要查询主键的表。
	 * @return 主键列名的列表。如果表没有主键，则返回空列表。
	 * @throws DbException 如果查询过程中发生SQLException，将抛出DbException。
	 * @since 5.8.28
	 */
	public static Set<String> getPrimaryKeys(final DatabaseMetaData metaData, final String catalog, final String schema, final String tableName) {
		return DatabaseMetaDataWrapper.of(metaData, catalog, schema).getPrimaryKeys(tableName);
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
		return DatabaseMetaDataWrapper.of(metaData, catalog, schema).getIndexInfo(tableName);
	}

	/**
	 * 判断当前数据库是否为Oracle。
	 *
	 * @param metaData 数据库元数据，用于获取数据库产品名称。
	 * @return 返回true表示当前数据库是Oracle，否则返回false。
	 * @throws DbException 如果获取数据库产品名称时发生SQLException，将抛出DbException。
	 * @since 5.8.28
	 */
	public static boolean isOracle(final DatabaseMetaData metaData) throws DbException {
		try {
			return StrUtil.equalsIgnoreCase("Oracle", metaData.getDatabaseProductName());
		} catch (final SQLException e) {
			throw new DbException(e);
		}
	}
}
