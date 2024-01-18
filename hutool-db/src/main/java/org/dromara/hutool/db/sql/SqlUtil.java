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

package org.dromara.hutool.db.sql;

import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.db.DbException;
import org.dromara.hutool.db.Entity;
import org.dromara.hutool.db.sql.Condition.LikeType;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.RowId;
import java.sql.SQLException;
import java.util.List;
import java.util.Map.Entry;

/**
 * SQL相关工具类，包括相关SQL语句拼接等
 *
 * @author looly
 * @since 4.0.10
 */
public class SqlUtil {

	/**
	 * 构件相等条件的where语句<br>
	 * 如果没有条件语句，泽返回空串，表示没有条件
	 *
	 * @param entity      条件实体
	 * @param paramValues 条件值得存放List
	 * @return 带where关键字的SQL部分
	 */
	public static String buildEqualsWhere(final Entity entity, final List<Object> paramValues) {
		if (null == entity || entity.isEmpty()) {
			return StrUtil.EMPTY;
		}

		final StringBuilder sb = new StringBuilder(" WHERE ");
		boolean isNotFirst = false;
		for (final Entry<String, Object> entry : entity.entrySet()) {
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
	public static Condition[] buildConditions(final Entity entity) {
		if (null == entity || entity.isEmpty()) {
			return null;
		}

		final Condition[] conditions = new Condition[entity.size()];
		int i = 0;
		Object value;
		for (final Entry<String, Object> entry : entity.entrySet()) {
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
	 * 创建LIKE语句中的值，创建的结果为：
	 *
	 * <pre>
	 * 1、LikeType.StartWith: '%value'
	 * 2、LikeType.EndWith: 'value%'
	 * 3、LikeType.Contains: '%value%'
	 * </pre>
	 * <p>
	 * 如果withLikeKeyword为true，则结果为：
	 *
	 * <pre>
	 * 1、LikeType.StartWith: LIKE '%value'
	 * 2、LikeType.EndWith: LIKE 'value%'
	 * 3、LikeType.Contains: LIKE '%value%'
	 * </pre>
	 *
	 * @param value           被查找值
	 * @param likeType        LIKE值类型 {@link LikeType}
	 * @param withLikeKeyword 是否包含LIKE关键字
	 * @return 拼接后的like值
	 */
	public static String buildLikeValue(final String value, final LikeType likeType, final boolean withLikeKeyword) {
		if (null == value) {
			return null;
		}

		final StringBuilder likeValue = StrUtil.builder(withLikeKeyword ? "LIKE " : "");
		switch (likeType) {
			case StartWith:
				likeValue.append(value).append('%');
				break;
			case EndWith:
				likeValue.append('%').append(value);
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
	 * 格式化SQL
	 *
	 * @param sql SQL
	 * @return 格式化后的SQL
	 */
	public static String formatSql(final String sql) {
		return SqlFormatter.format(sql);
	}

	/**
	 * 将RowId转为字符串
	 *
	 * @param rowId RowId
	 * @return RowId字符串
	 */
	public static String rowIdToString(final RowId rowId) {
		return StrUtil.str(rowId.getBytes(), CharsetUtil.ISO_8859_1);
	}

	/**
	 * Clob字段值转字符串
	 *
	 * @param clob {@link Clob}
	 * @return 字符串
	 * @since 3.0.6
	 */
	public static String clobToStr(final Clob clob) {
		Reader reader = null;
		try {
			reader = clob.getCharacterStream();
			return IoUtil.read(reader);
		} catch (final SQLException e) {
			throw new DbException(e);
		} finally {
			IoUtil.closeQuietly(reader);
		}
	}

	/**
	 * Blob字段值转字符串
	 *
	 * @param blob    {@link Blob}
	 * @param charset 编码
	 * @return 字符串
	 * @since 3.0.6
	 */
	public static String blobToStr(final Blob blob, final Charset charset) {
		InputStream in = null;
		try {
			in = blob.getBinaryStream();
			return IoUtil.read(in, charset);
		} catch (final SQLException e) {
			throw new DbException(e);
		} finally {
			IoUtil.closeQuietly(in);
		}
	}

	/**
	 * 创建Blob对象
	 *
	 * @param conn          {@link Connection}
	 * @param dataStream    数据流，使用完毕后关闭
	 * @param closeAfterUse 使用完毕是否关闭流
	 * @return {@link Blob}
	 * @since 4.5.13
	 */
	public static Blob createBlob(final Connection conn, final InputStream dataStream, final boolean closeAfterUse) {
		Blob blob;
		OutputStream out = null;
		try {
			blob = conn.createBlob();
			out = blob.setBinaryStream(1);
			IoUtil.copy(dataStream, out);
		} catch (final SQLException e) {
			throw new DbException(e);
		} finally {
			IoUtil.closeQuietly(out);
			if (closeAfterUse) {
				IoUtil.closeQuietly(dataStream);
			}
		}
		return blob;
	}

	/**
	 * 创建Blob对象
	 *
	 * @param conn {@link Connection}
	 * @param data 数据
	 * @return {@link Blob}
	 * @since 4.5.13
	 */
	public static Blob createBlob(final Connection conn, final byte[] data) {
		final Blob blob;
		try {
			blob = conn.createBlob();
			blob.setBytes(0, data);
		} catch (final SQLException e) {
			throw new DbException(e);
		}
		return blob;
	}

	/**
	 * 转换为{@link java.sql.Date}
	 *
	 * @param date {@link java.util.Date}
	 * @return {@link java.sql.Date}
	 * @since 3.1.2
	 */
	public static java.sql.Date toSqlDate(final java.util.Date date) {
		return new java.sql.Date(date.getTime());
	}

	/**
	 * 转换为{@link java.sql.Timestamp}
	 *
	 * @param date {@link java.util.Date}
	 * @return {@link java.sql.Timestamp}
	 * @since 3.1.2
	 */
	public static java.sql.Timestamp toSqlTimestamp(final java.util.Date date) {
		return new java.sql.Timestamp(date.getTime());
	}
}
