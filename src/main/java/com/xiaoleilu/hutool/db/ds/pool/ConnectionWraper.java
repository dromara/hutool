package com.xiaoleilu.hutool.db.ds.pool;

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
public abstract class ConnectionWraper implements Connection{
	
	protected Connection realConn;//真正的连接

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return realConn.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return realConn.isWrapperFor(iface);
	}

	@Override
	public Statement createStatement() throws SQLException {
		return realConn.createStatement();
	}

	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return realConn.prepareStatement(sql);
	}

	@Override
	public CallableStatement prepareCall(String sql) throws SQLException {
		return realConn.prepareCall(sql);
	}

	@Override
	public String nativeSQL(String sql) throws SQLException {
		return realConn.nativeSQL(sql);
	}

	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		realConn.setAutoCommit(autoCommit);
	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		return realConn.getAutoCommit();
	}

	@Override
	public void commit() throws SQLException {
		realConn.commit();
	}

	@Override
	public void rollback() throws SQLException {
		realConn.rollback();
	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		return realConn.getMetaData();
	}

	@Override
	public void setReadOnly(boolean readOnly) throws SQLException {
		realConn.setReadOnly(readOnly);
	}

	@Override
	public boolean isReadOnly() throws SQLException {
		return realConn.isReadOnly();
	}

	@Override
	public void setCatalog(String catalog) throws SQLException {
		realConn.setCatalog(catalog);
	}

	@Override
	public String getCatalog() throws SQLException {
		return realConn.getCatalog();
	}

	@Override
	public void setTransactionIsolation(int level) throws SQLException {
		realConn.setTransactionIsolation(level);
	}

	@Override
	public int getTransactionIsolation() throws SQLException {
		return realConn.getTransactionIsolation();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		return realConn.getWarnings();
	}

	@Override
	public void clearWarnings() throws SQLException {
		realConn.clearWarnings();
	}

	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
		return realConn.createStatement(resultSetType, resultSetConcurrency);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		return realConn.prepareStatement(sql, resultSetType, resultSetConcurrency);
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		return realConn.prepareCall(sql, resultSetType, resultSetConcurrency);
	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return realConn.getTypeMap();
	}

	@Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		realConn.setTypeMap(map);
	}

	@Override
	public void setHoldability(int holdability) throws SQLException {
		realConn.setHoldability(holdability);
	}

	@Override
	public int getHoldability() throws SQLException {
		return realConn.getHoldability();
	}

	@Override
	public Savepoint setSavepoint() throws SQLException {
		return realConn.setSavepoint();
	}

	@Override
	public Savepoint setSavepoint(String name) throws SQLException {
		return realConn.setSavepoint(name);
	}

	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		realConn.rollback(savepoint);
	}

	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		realConn.releaseSavepoint(savepoint);
	}

	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		return realConn.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		return realConn.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		return realConn.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
		return realConn.prepareStatement(sql, autoGeneratedKeys);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
		return realConn.prepareStatement(sql, columnIndexes);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
		return realConn.prepareStatement(sql, columnNames);
	}

	@Override
	public Clob createClob() throws SQLException {
		return realConn.createClob();
	}

	@Override
	public Blob createBlob() throws SQLException {
		return realConn.createBlob();
	}

	@Override
	public NClob createNClob() throws SQLException {
		return realConn.createNClob();
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		return realConn.createSQLXML();
	}

	@Override
	public boolean isValid(int timeout) throws SQLException {
		return realConn.isValid(timeout);
	}

	@Override
	public void setClientInfo(String name, String value) throws SQLClientInfoException {
		realConn.setClientInfo(name, value);
	}

	@Override
	public void setClientInfo(Properties properties) throws SQLClientInfoException {
		realConn.setClientInfo(properties);
	}

	@Override
	public String getClientInfo(String name) throws SQLException {
		return realConn.getClientInfo(name);
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		return realConn.getClientInfo();
	}

	@Override
	public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
		return realConn.createArrayOf(typeName, elements);
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
		return realConn.createStruct(typeName, attributes);
	}

	@Override
	public void setSchema(String schema) throws SQLException {
		realConn.setSchema(schema);
	}

	@Override
	public String getSchema() throws SQLException {
		return realConn.getSchema();
	}

	@Override
	public void abort(Executor executor) throws SQLException {
		realConn.abort(executor);
	}

	@Override
	public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
		realConn.setNetworkTimeout(executor, milliseconds);
	}

	@Override
	public int getNetworkTimeout() throws SQLException {
		return realConn.getNetworkTimeout();
	}

	/**
	 * @return 实际的连接对象
	 */
	public Connection getRealConnection(){
		return this.realConn;
	}
}
