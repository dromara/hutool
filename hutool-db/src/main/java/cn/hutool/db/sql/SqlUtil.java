package cn.hutool.db.sql;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.DbRuntimeException;
import cn.hutool.db.Entity;
import cn.hutool.db.sql.Condition.LikeType;

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
	 * 创建LIKE语句中的值，创建的结果为：
	 *
	 * <pre>
	 * 1、LikeType.StartWith: %value
	 * 2、LikeType.EndWith: value%
	 * 3、LikeType.Contains: %value%
	 * </pre>
	 * <p>
	 * 如果withLikeKeyword为true，则结果为：
	 *
	 * <pre>
	 * 1、LikeType.StartWith: LIKE %value
	 * 2、LikeType.EndWith: LIKE value%
	 * 3、LikeType.Contains: LIKE %value%
	 * </pre>
	 *
	 * @param value           被查找值
	 * @param likeType        LIKE值类型 {@link LikeType}
	 * @param withLikeKeyword 是否包含LIKE关键字
	 * @return 拼接后的like值
	 */
	public static String buildLikeValue(String value, LikeType likeType, boolean withLikeKeyword) {
		if (null == value) {
			return null;
		}

		StringBuilder likeValue = StrUtil.builder(withLikeKeyword ? "LIKE " : "");
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
	public static String formatSql(String sql) {
		return SqlFormatter.format(sql);
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
	 * @param blob    {@link Blob}
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
	 * 创建Blob对象
	 *
	 * @param conn          {@link Connection}
	 * @param dataStream    数据流，使用完毕后关闭
	 * @param closeAfterUse 使用完毕是否关闭流
	 * @return {@link Blob}
	 * @since 4.5.13
	 */
	public static Blob createBlob(Connection conn, InputStream dataStream, boolean closeAfterUse) {
		Blob blob;
		OutputStream out = null;
		try {
			blob = conn.createBlob();
			out = blob.setBinaryStream(1);
			IoUtil.copy(dataStream, out);
		} catch (SQLException e) {
			throw new DbRuntimeException(e);
		} finally {
			IoUtil.close(out);
			if (closeAfterUse) {
				IoUtil.close(dataStream);
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
	public static Blob createBlob(Connection conn, byte[] data) {
		Blob blob;
		try {
			blob = conn.createBlob();
			blob.setBytes(0, data);
		} catch (SQLException e) {
			throw new DbRuntimeException(e);
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
}
