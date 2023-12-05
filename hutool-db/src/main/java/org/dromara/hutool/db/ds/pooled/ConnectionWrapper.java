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

package org.dromara.hutool.db.ds.pooled;

import org.dromara.hutool.core.lang.wrapper.Wrapper;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * 连接包装，用于丰富功能
 * @author Looly
 *
 */
public abstract class ConnectionWrapper implements Connection, Wrapper<Connection> {

	protected Connection raw;//真正的连接

	@Override
	public <T> T unwrap(final Class<T> iface) throws SQLException {
		return raw.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(final Class<?> iface) throws SQLException {
		return raw.isWrapperFor(iface);
	}

	@Override
	public Statement createStatement() throws SQLException {
		return raw.createStatement();
	}

	@Override
	public PreparedStatement prepareStatement(final String sql) throws SQLException {
		return raw.prepareStatement(sql);
	}

	@Override
	public CallableStatement prepareCall(final String sql) throws SQLException {
		return raw.prepareCall(sql);
	}

	@Override
	public String nativeSQL(final String sql) throws SQLException {
		return raw.nativeSQL(sql);
	}

	@Override
	public void setAutoCommit(final boolean autoCommit) throws SQLException {
		raw.setAutoCommit(autoCommit);
	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		return raw.getAutoCommit();
	}

	@Override
	public void commit() throws SQLException {
		raw.commit();
	}

	@Override
	public void rollback() throws SQLException {
		raw.rollback();
	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		return raw.getMetaData();
	}

	@Override
	public void setReadOnly(final boolean readOnly) throws SQLException {
		raw.setReadOnly(readOnly);
	}

	@Override
	public boolean isReadOnly() throws SQLException {
		return raw.isReadOnly();
	}

	@Override
	public void setCatalog(final String catalog) throws SQLException {
		raw.setCatalog(catalog);
	}

	@Override
	public String getCatalog() throws SQLException {
		return raw.getCatalog();
	}

	@Override
	public void setTransactionIsolation(final int level) throws SQLException {
		raw.setTransactionIsolation(level);
	}

	@Override
	public int getTransactionIsolation() throws SQLException {
		return raw.getTransactionIsolation();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		return raw.getWarnings();
	}

	@Override
	public void clearWarnings() throws SQLException {
		raw.clearWarnings();
	}

	@Override
	public Statement createStatement(final int resultSetType, final int resultSetConcurrency) throws SQLException {
		return raw.createStatement(resultSetType, resultSetConcurrency);
	}

	@Override
	public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
		return raw.prepareStatement(sql, resultSetType, resultSetConcurrency);
	}

	@Override
	public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
		return raw.prepareCall(sql, resultSetType, resultSetConcurrency);
	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return raw.getTypeMap();
	}

	@Override
	public void setTypeMap(final Map<String, Class<?>> map) throws SQLException {
		raw.setTypeMap(map);
	}

	@Override
	public void setHoldability(final int holdability) throws SQLException {
		raw.setHoldability(holdability);
	}

	@Override
	public int getHoldability() throws SQLException {
		return raw.getHoldability();
	}

	@Override
	public Savepoint setSavepoint() throws SQLException {
		return raw.setSavepoint();
	}

	@Override
	public Savepoint setSavepoint(final String name) throws SQLException {
		return raw.setSavepoint(name);
	}

	@Override
	public void rollback(final Savepoint savepoint) throws SQLException {
		raw.rollback(savepoint);
	}

	@Override
	public void releaseSavepoint(final Savepoint savepoint) throws SQLException {
		raw.releaseSavepoint(savepoint);
	}

	@Override
	public Statement createStatement(final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
		return raw.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	@Override
	public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
		return raw.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	@Override
	public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
		return raw.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	@Override
	public PreparedStatement prepareStatement(final String sql, final int autoGeneratedKeys) throws SQLException {
		return raw.prepareStatement(sql, autoGeneratedKeys);
	}

	@Override
	public PreparedStatement prepareStatement(final String sql, final int[] columnIndexes) throws SQLException {
		return raw.prepareStatement(sql, columnIndexes);
	}

	@Override
	public PreparedStatement prepareStatement(final String sql, final String[] columnNames) throws SQLException {
		return raw.prepareStatement(sql, columnNames);
	}

	@Override
	public Clob createClob() throws SQLException {
		return raw.createClob();
	}

	@Override
	public Blob createBlob() throws SQLException {
		return raw.createBlob();
	}

	@Override
	public NClob createNClob() throws SQLException {
		return raw.createNClob();
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		return raw.createSQLXML();
	}

	@Override
	public boolean isValid(final int timeout) throws SQLException {
		return raw.isValid(timeout);
	}

	@Override
	public void setClientInfo(final String name, final String value) throws SQLClientInfoException {
		raw.setClientInfo(name, value);
	}

	@Override
	public void setClientInfo(final Properties properties) throws SQLClientInfoException {
		raw.setClientInfo(properties);
	}

	@Override
	public String getClientInfo(final String name) throws SQLException {
		return raw.getClientInfo(name);
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		return raw.getClientInfo();
	}

	@Override
	public Array createArrayOf(final String typeName, final Object[] elements) throws SQLException {
		return raw.createArrayOf(typeName, elements);
	}

	@Override
	public Struct createStruct(final String typeName, final Object[] attributes) throws SQLException {
		return raw.createStruct(typeName, attributes);
	}

	@Override
	public void setSchema(final String schema) throws SQLException {
		raw.setSchema(schema);
	}

	@Override
	public String getSchema() throws SQLException {
		return raw.getSchema();
	}

	@Override
	public void abort(final Executor executor) throws SQLException {
		raw.abort(executor);
	}

	@Override
	public void setNetworkTimeout(final Executor executor, final int milliseconds) throws SQLException {
		raw.setNetworkTimeout(executor, milliseconds);
	}

	@Override
	public int getNetworkTimeout() throws SQLException {
		return raw.getNetworkTimeout();
	}

	@Override
	public Connection getRaw(){
		return this.raw;
	}
}
