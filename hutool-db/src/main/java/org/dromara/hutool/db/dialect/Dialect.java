/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.db.dialect;

import org.dromara.hutool.db.DbException;
import org.dromara.hutool.db.Entity;
import org.dromara.hutool.db.Page;
import org.dromara.hutool.db.sql.Order;
import org.dromara.hutool.db.sql.Query;
import org.dromara.hutool.db.sql.SqlBuilder;
import org.dromara.hutool.db.sql.QuoteWrapper;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * SQL方言，不同的数据库由于在某些SQL上有所区别，故为每种数据库配置不同的方言。<br>
 * 由于不同数据库间SQL语句的差异，导致无法统一拼接SQL，<br>
 * Dialect接口旨在根据不同的数据库，使用不同的方言实现类，来拼接对应的SQL，并将SQL和参数放入PreparedStatement中
 *
 * @author loolly
 */
public interface Dialect extends Serializable {

	/**
	 * @return 包装器
	 */
	QuoteWrapper getWrapper();

	/**
	 * 设置包装器
	 *
	 * @param quoteWrapper 包装器
	 */
	void setWrapper(QuoteWrapper quoteWrapper);

	// -------------------------------------------- Execute

	/**
	 * 构建用于插入的{@link PreparedStatement}<br>
	 * 用户实现需按照数据库方言格式，将{@link Entity}转换为带有占位符的SQL语句及参数列表
	 *
	 * @param returnGeneratedKey 是否返回自增主键
	 * @param conn   数据库连接对象
	 * @param entity 数据实体类（包含表名）
	 * @return PreparedStatement
	 * @throws DbException SQL执行异常
	 */
	PreparedStatement psForInsert(boolean returnGeneratedKey, Connection conn, Entity entity) throws DbException;

	/**
	 * 构建用于批量插入的PreparedStatement<br>
	 * 用户实现需按照数据库方言格式，将{@link Entity}转换为带有占位符的SQL语句及参数列表
	 *
	 * @param conn     数据库连接对象
	 * @param entities 数据实体，实体的结构必须全部一致，否则插入结果将不可预知
	 * @return PreparedStatement
	 * @throws DbException SQL执行异常
	 */
	PreparedStatement psForInsertBatch(Connection conn, Entity... entities) throws DbException;

	/**
	 * 构建用于删除的{@link PreparedStatement}<br>
	 * 用户实现需按照数据库方言格式，将{@link Query}转换为带有占位符的SQL语句及参数列表<br>
	 * {@link Query}中包含了删除所需的表名、查询条件等信息，可借助SqlBuilder完成SQL语句生成。
	 *
	 * @param conn  数据库连接对象
	 * @param query 查找条件（包含表名）
	 * @return PreparedStatement
	 * @throws DbException SQL执行异常
	 */
	PreparedStatement psForDelete(Connection conn, Query query) throws DbException;

	/**
	 * 构建用于更新的{@link PreparedStatement}<br>
	 * 用户实现需按照数据库方言格式，将{@link Entity}配合{@link Query}转换为带有占位符的SQL语句及参数列表<br>
	 * 其中{@link Entity}中包含需要更新的数据信息，{@link Query}包含更新的查找条件信息。
	 *
	 * @param conn   数据库连接对象
	 * @param entity 数据实体类（包含表名）
	 * @param query  查找条件（包含表名）
	 * @return PreparedStatement
	 * @throws DbException SQL执行异常
	 */
	PreparedStatement psForUpdate(Connection conn, Entity entity, Query query) throws DbException;

	// -------------------------------------------- Query

	/**
	 * 构建用于获取多条记录的{@link PreparedStatement}<br>
	 * 用户实现需按照数据库方言格式，将{@link Query}转换为带有占位符的SQL语句及参数列表<br>
	 * {@link Query}中包含了查询所需的表名、查询条件等信息，可借助SqlBuilder完成SQL语句生成。
	 *
	 * @param conn  数据库连接对象
	 * @param query 查询条件（包含表名）
	 * @return PreparedStatement
	 * @throws DbException SQL执行异常
	 */
	PreparedStatement psForFind(Connection conn, Query query) throws DbException;

	/**
	 * 构建用于分页查询的{@link PreparedStatement}<br>
	 * 用户实现需按照数据库方言格式，将{@link Query}转换为带有占位符的SQL语句及参数列表<br>
	 * {@link Query}中包含了分页查询所需的表名、查询条件、分页等信息，可借助SqlBuilder完成SQL语句生成。
	 *
	 * @param conn  数据库连接对象
	 * @param query 查询条件（包含表名）
	 * @return PreparedStatement
	 * @throws DbException SQL执行异常
	 */
	PreparedStatement psForPage(Connection conn, Query query) throws DbException;

	/**
	 * 构建用于分页查询的{@link PreparedStatement}<br>
	 * 可以在此方法中使用{@link SqlBuilder#orderBy(Order...)}方法加入排序信息，
	 * 排序信息通过{@link Page#getOrders()}获取
	 *
	 * @param conn       数据库连接对象
	 * @param sqlBuilder SQL构建器，可以使用{@link SqlBuilder#of(CharSequence)} 包装普通SQL
	 * @param page       分页对象
	 * @return PreparedStatement
	 * @throws DbException SQL执行异常
	 * @since 5.5.3
	 */
	PreparedStatement psForPage(Connection conn, SqlBuilder sqlBuilder, Page page) throws DbException;

	/**
	 * 构建用于查询行数的{@link PreparedStatement}<br>
	 * 用户实现需按照数据库方言格式，将{@link Query}转换为带有占位符的SQL语句及参数列表<br>
	 * {@link Query}中包含了表名、查询条件等信息，可借助SqlBuilder完成SQL语句生成。
	 *
	 * @param conn  数据库连接对象
	 * @param query 查询条件（包含表名）
	 * @return PreparedStatement
	 * @throws DbException SQL执行异常
	 */
	default PreparedStatement psForCount(final Connection conn, final Query query) throws DbException {
		return psForCount(conn, SqlBuilder.of().query(query));
	}

	/**
	 * 构建用于查询行数的{@link PreparedStatement}<br>
	 * 用户实现需按照数据库方言格式，将{@link Query}转换为带有占位符的SQL语句及参数列表<br>
	 * {@link Query}中包含了表名、查询条件等信息，可借助SqlBuilder完成SQL语句生成。
	 *
	 * @param conn       数据库连接对象
	 * @param sqlBuilder 查询语句，应该包含分页等信息
	 * @return PreparedStatement
	 * @throws DbException SQL执行异常
	 * @since 5.7.2
	 */
	default PreparedStatement psForCount(final Connection conn, SqlBuilder sqlBuilder) throws DbException {
		// https://gitee.com/dromara/hutool/issues/I713XQ
		// 为了兼容informix等数据库，此处使用count(*)而非count(1)
		sqlBuilder = sqlBuilder
				.insertPreFragment("SELECT count(*) from(")
				// issue#I3IJ8X@Gitee，在子查询时需设置单独别名，此处为了防止和用户的表名冲突，使用自定义的较长别名
				.append(") hutool_alias_count_");
		return psForPage(conn, sqlBuilder, null);
	}

	/**
	 * 构建用于upsert的{@link PreparedStatement}<br>
	 * 方言实现需实现此默认方法，如果没有实现，抛出{@link SQLException}
	 *
	 * @param conn   数据库连接对象
	 * @param entity 数据实体类（包含表名）
	 * @param keys   查找字段，某些数据库此字段必须，如H2，某些数据库无需此字段，如MySQL（通过主键）
	 * @return PreparedStatement
	 * @throws DbException SQL执行异常，或方言数据不支持此操作
	 * @since 5.7.20
	 */
	default PreparedStatement psForUpsert(final Connection conn, final Entity entity, final String... keys) throws DbException {
		throw new DbException("Unsupported upsert operation of " + dialectName());
	}


	/**
	 * 方言名
	 *
	 * @return 方言名
	 * @since 5.5.3
	 */
	String dialectName();
}
