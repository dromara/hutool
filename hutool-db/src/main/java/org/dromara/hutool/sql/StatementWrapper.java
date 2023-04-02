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

package org.dromara.hutool.sql;

import org.dromara.hutool.lang.func.Wrapper;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * {@link PreparedStatement} 包装类，用于添加拦截方法功能<br>
 * 拦截方法包括：
 *
 * <pre>
 * 1. 提供参数注入
 * 2. 提供SQL打印日志拦截
 * </pre>
 *
 * @author looly
 * @since 4.1.0
 */
public class StatementWrapper implements PreparedStatement, Wrapper<PreparedStatement> {

	private final PreparedStatement rawStatement;

	/**
	 * 构造
	 *
	 * @param rawStatement {@link PreparedStatement}
	 */
	public StatementWrapper(final PreparedStatement rawStatement) {
		this.rawStatement = rawStatement;
	}

	@Override
	public ResultSet executeQuery(final String sql) throws SQLException {
		return rawStatement.executeQuery(sql);
	}

	@Override
	public int executeUpdate(final String sql) throws SQLException {
		return rawStatement.executeUpdate(sql);
	}

	@Override
	public void close() throws SQLException {
		rawStatement.close();
	}

	@Override
	public int getMaxFieldSize() throws SQLException {
		return rawStatement.getMaxFieldSize();
	}

	@Override
	public void setMaxFieldSize(final int max) throws SQLException {
		rawStatement.setMaxFieldSize(max);
	}

	@Override
	public int getMaxRows() throws SQLException {
		return rawStatement.getMaxRows();
	}

	@Override
	public void setMaxRows(final int max) throws SQLException {
		rawStatement.setMaxRows(max);
	}

	@Override
	public void setEscapeProcessing(final boolean enable) throws SQLException {
		rawStatement.setEscapeProcessing(enable);
	}

	@Override
	public int getQueryTimeout() throws SQLException {
		return rawStatement.getQueryTimeout();
	}

	@Override
	public void setQueryTimeout(final int seconds) throws SQLException {
		rawStatement.setQueryTimeout(seconds);
	}

	@Override
	public void cancel() throws SQLException {
		rawStatement.cancel();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		return rawStatement.getWarnings();
	}

	@Override
	public void clearWarnings() throws SQLException {
		rawStatement.clearWarnings();
	}

	@Override
	public void setCursorName(final String name) throws SQLException {
		rawStatement.setCursorName(name);
	}

	@Override
	public boolean execute(final String sql) throws SQLException {
		return rawStatement.execute(sql);
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		return rawStatement.getResultSet();
	}

	@Override
	public int getUpdateCount() throws SQLException {
		return rawStatement.getUpdateCount();
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		return rawStatement.getMoreResults();
	}

	@Override
	public void setFetchDirection(final int direction) throws SQLException {
		rawStatement.setFetchDirection(direction);
	}

	@Override
	public int getFetchDirection() throws SQLException {
		return rawStatement.getFetchDirection();
	}

	@Override
	public void setFetchSize(final int rows) throws SQLException {
		rawStatement.setFetchSize(rows);
	}

	@Override
	public int getFetchSize() throws SQLException {
		return rawStatement.getFetchSize();
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		return rawStatement.getResultSetConcurrency();
	}

	@Override
	public int getResultSetType() throws SQLException {
		return rawStatement.getResultSetType();
	}

	@Override
	public void addBatch(final String sql) throws SQLException {
		rawStatement.addBatch(sql);
	}

	@Override
	public void clearBatch() throws SQLException {
		rawStatement.clearBatch();
	}

	@Override
	public int[] executeBatch() throws SQLException {
		return rawStatement.executeBatch();
	}

	@Override
	public Connection getConnection() throws SQLException {
		return rawStatement.getConnection();
	}

	@Override
	public boolean getMoreResults(final int current) throws SQLException {
		return rawStatement.getMoreResults(current);
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		return rawStatement.getGeneratedKeys();
	}

	@Override
	public int executeUpdate(final String sql, final int autoGeneratedKeys) throws SQLException {
		return rawStatement.executeUpdate(sql, autoGeneratedKeys);
	}

	@Override
	public int executeUpdate(final String sql, final int[] columnIndexes) throws SQLException {
		return rawStatement.executeUpdate(sql, columnIndexes);
	}

	@Override
	public int executeUpdate(final String sql, final String[] columnNames) throws SQLException {
		return rawStatement.executeUpdate(sql, columnNames);
	}

	@Override
	public boolean execute(final String sql, final int autoGeneratedKeys) throws SQLException {
		return rawStatement.execute(sql, autoGeneratedKeys);
	}

	@Override
	public boolean execute(final String sql, final int[] columnIndexes) throws SQLException {
		return rawStatement.execute(sql, columnIndexes);
	}

	@Override
	public boolean execute(final String sql, final String[] columnNames) throws SQLException {
		return rawStatement.execute(sql, columnNames);
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		return rawStatement.getResultSetHoldability();
	}

	@Override
	public boolean isClosed() throws SQLException {
		return rawStatement.isClosed();
	}

	@Override
	public void setPoolable(final boolean poolable) throws SQLException {
		rawStatement.setPoolable(poolable);
	}

	@Override
	public boolean isPoolable() throws SQLException {
		return rawStatement.isPoolable();
	}

	@Override
	public void closeOnCompletion() throws SQLException {
		rawStatement.closeOnCompletion();
	}

	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		return rawStatement.isCloseOnCompletion();
	}

	@Override
	public <T> T unwrap(final Class<T> iface) throws SQLException {
		return rawStatement.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(final Class<?> iface) throws SQLException {
		return rawStatement.isWrapperFor(iface);
	}

	@Override
	public ResultSet executeQuery() throws SQLException {
		return rawStatement.executeQuery();
	}

	@Override
	public int executeUpdate() throws SQLException {
		return rawStatement.executeUpdate();
	}

	@Override
	public void setNull(final int parameterIndex, final int sqlType) throws SQLException {
		rawStatement.setNull(parameterIndex, sqlType);
	}

	@Override
	public void setBoolean(final int parameterIndex, final boolean x) throws SQLException {
		rawStatement.setBoolean(parameterIndex, x);
	}

	@Override
	public void setByte(final int parameterIndex, final byte x) throws SQLException {
		rawStatement.setByte(parameterIndex, x);
	}

	@Override
	public void setShort(final int parameterIndex, final short x) throws SQLException {
		rawStatement.setShort(parameterIndex, x);
	}

	@Override
	public void setInt(final int parameterIndex, final int x) throws SQLException {
		rawStatement.setInt(parameterIndex, x);
	}

	@Override
	public void setLong(final int parameterIndex, final long x) throws SQLException {
		rawStatement.setLong(parameterIndex, x);
	}

	@Override
	public void setFloat(final int parameterIndex, final float x) throws SQLException {
		rawStatement.setFloat(parameterIndex, x);
	}

	@Override
	public void setDouble(final int parameterIndex, final double x) throws SQLException {
		rawStatement.setDouble(parameterIndex, x);
	}

	@Override
	public void setBigDecimal(final int parameterIndex, final BigDecimal x) throws SQLException {
		rawStatement.setBigDecimal(parameterIndex, x);
	}

	@Override
	public void setString(final int parameterIndex, final String x) throws SQLException {
		rawStatement.setString(parameterIndex, x);
	}

	@Override
	public void setBytes(final int parameterIndex, final byte[] x) throws SQLException {
		rawStatement.setBytes(parameterIndex, x);
	}

	@Override
	public void setDate(final int parameterIndex, final Date x) throws SQLException {
		rawStatement.setDate(parameterIndex, x);
	}

	@Override
	public void setTime(final int parameterIndex, final Time x) throws SQLException {
		rawStatement.setTime(parameterIndex, x);
	}

	@Override
	public void setTimestamp(final int parameterIndex, final Timestamp x) throws SQLException {
		rawStatement.setTimestamp(parameterIndex, x);
	}

	@Override
	public void setAsciiStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
		rawStatement.setAsciiStream(parameterIndex, x, length);
	}

	@Override
	@Deprecated
	public void setUnicodeStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
		rawStatement.setUnicodeStream(parameterIndex, x, length);
	}

	@Override
	public void setBinaryStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
		rawStatement.setBinaryStream(parameterIndex, x, length);
	}

	@Override
	public void clearParameters() throws SQLException {
		rawStatement.clearParameters();
	}

	@Override
	public void setObject(final int parameterIndex, final Object x, final int targetSqlType) throws SQLException {
		rawStatement.setObject(parameterIndex, x, targetSqlType, targetSqlType);
	}

	@Override
	public void setObject(final int parameterIndex, final Object x) throws SQLException {
		rawStatement.setObject(parameterIndex, x);
	}

	@Override
	public boolean execute() throws SQLException {
		return rawStatement.execute();
	}

	@Override
	public void addBatch() throws SQLException {
		rawStatement.addBatch();
	}

	@Override
	public void setCharacterStream(final int parameterIndex, final Reader reader, final int length) throws SQLException {
		rawStatement.setCharacterStream(parameterIndex, reader, length);
	}

	@Override
	public void setRef(final int parameterIndex, final Ref x) throws SQLException {
		rawStatement.setRef(parameterIndex, x);
	}

	@Override
	public void setBlob(final int parameterIndex, final Blob x) throws SQLException {
		rawStatement.setBlob(parameterIndex, x);
	}

	@Override
	public void setClob(final int parameterIndex, final Clob x) throws SQLException {
		rawStatement.setClob(parameterIndex, x);
	}

	@Override
	public void setArray(final int parameterIndex, final Array x) throws SQLException {
		rawStatement.setArray(parameterIndex, x);
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		return rawStatement.getMetaData();
	}

	@Override
	public void setDate(final int parameterIndex, final Date x, final Calendar cal) throws SQLException {
		rawStatement.setDate(parameterIndex, x, cal);
	}

	@Override
	public void setTime(final int parameterIndex, final Time x, final Calendar cal) throws SQLException {
		rawStatement.setTime(parameterIndex, x, cal);
	}

	@Override
	public void setTimestamp(final int parameterIndex, final Timestamp x, final Calendar cal) throws SQLException {
		rawStatement.setTimestamp(parameterIndex, x, cal);
	}

	@Override
	public void setNull(final int parameterIndex, final int sqlType, final String typeName) throws SQLException {
		rawStatement.setNull(parameterIndex, sqlType, typeName);
	}

	@Override
	public void setURL(final int parameterIndex, final URL x) throws SQLException {
		rawStatement.setURL(parameterIndex, x);
	}

	@Override
	public ParameterMetaData getParameterMetaData() throws SQLException {
		return rawStatement.getParameterMetaData();
	}

	@Override
	public void setRowId(final int parameterIndex, final RowId x) throws SQLException {
		rawStatement.setRowId(parameterIndex, x);
	}

	@Override
	public void setNString(final int parameterIndex, final String value) throws SQLException {
		rawStatement.setNString(parameterIndex, value);
	}

	@Override
	public void setNCharacterStream(final int parameterIndex, final Reader value, final long length) throws SQLException {
		rawStatement.setCharacterStream(parameterIndex, value, length);
	}

	@Override
	public void setNClob(final int parameterIndex, final NClob value) throws SQLException {
		rawStatement.setNClob(parameterIndex, value);
	}

	@Override
	public void setClob(final int parameterIndex, final Reader reader, final long length) throws SQLException {
		rawStatement.setClob(parameterIndex, reader, length);
	}

	@Override
	public void setBlob(final int parameterIndex, final InputStream inputStream, final long length) throws SQLException {
		rawStatement.setBlob(parameterIndex, inputStream, length);
	}

	@Override
	public void setNClob(final int parameterIndex, final Reader reader, final long length) throws SQLException {
		rawStatement.setNClob(parameterIndex, reader, length);
	}

	@Override
	public void setSQLXML(final int parameterIndex, final SQLXML xmlObject) throws SQLException {
		rawStatement.setSQLXML(parameterIndex, xmlObject);
	}

	@Override
	public void setObject(final int parameterIndex, final Object x, final int targetSqlType, final int scaleOrLength) throws SQLException {
		rawStatement.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
	}

	@Override
	public void setAsciiStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {
		rawStatement.setAsciiStream(parameterIndex, x, length);
	}

	@Override
	public void setBinaryStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {
		rawStatement.setBinaryStream(parameterIndex, x, length);
	}

	@Override
	public void setCharacterStream(final int parameterIndex, final Reader reader, final long length) throws SQLException {
		rawStatement.setCharacterStream(parameterIndex, reader, length);
	}

	@Override
	public void setAsciiStream(final int parameterIndex, final InputStream x) throws SQLException {
		rawStatement.setAsciiStream(parameterIndex, x);
	}

	@Override
	public void setBinaryStream(final int parameterIndex, final InputStream x) throws SQLException {
		rawStatement.setBinaryStream(parameterIndex, x);
	}

	@Override
	public void setCharacterStream(final int parameterIndex, final Reader reader) throws SQLException {
		rawStatement.setCharacterStream(parameterIndex, reader);
	}

	@Override
	public void setNCharacterStream(final int parameterIndex, final Reader value) throws SQLException {
		rawStatement.setNCharacterStream(parameterIndex, value);
	}

	@Override
	public void setClob(final int parameterIndex, final Reader reader) throws SQLException {
		rawStatement.setClob(parameterIndex, reader);
	}

	@Override
	public void setBlob(final int parameterIndex, final InputStream inputStream) throws SQLException {
		rawStatement.setBlob(parameterIndex, inputStream);
	}

	@Override
	public void setNClob(final int parameterIndex, final Reader reader) throws SQLException {
		rawStatement.setNClob(parameterIndex, reader);
	}

	@Override
	public PreparedStatement getRaw() {
		return rawStatement;
	}
}
