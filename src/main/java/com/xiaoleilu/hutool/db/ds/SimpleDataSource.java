package com.xiaoleilu.hutool.db.ds;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.xiaoleilu.hutool.Setting;
import com.xiaoleilu.hutool.db.DbUtil;
import com.xiaoleilu.hutool.exceptions.DbRuntimeException;

/***
 * 简易数据源，没有使用连接池，仅供测试或打开关闭连接非常少的场合使用！
 * @author loolly
 *
 */
public class SimpleDataSource implements DataSource, Cloneable{
	
	/** 默认的数据库连接配置文件路径 */
	public final static String DEFAULT_DB_CONFIG_PATH = "config/db.setting";
	
	//-------------------------------------------------------------------- Fields start
	private String driver;		//数据库驱动
	private String url;			//jdbc url
	private String user;			//用户名
	private String pass;			//密码
	//-------------------------------------------------------------------- Fields end
	
	//-------------------------------------------------------------------- Constructor start
	/**
	 * 构造
	 * @param group 数据库配置文件中的分组
	 */
	public SimpleDataSource(String group) {
		this(null, group);
	}
	
	/**
	 * 构造
	 * @param setting 数据库配置
	 * @param group 数据库配置文件中的分组
	 */
	public SimpleDataSource(Setting setting, String group) {
		if(null == setting) {
			setting = new Setting(DEFAULT_DB_CONFIG_PATH);
		}
		init(
				setting.getString("url", group), 
				setting.getString("user", group), 
				setting.getString("pass", group)
		);
	}
	
	/**
	 * 构造
	 * @param url jdbc url
	 * @param user 用户名
	 * @param pass 密码
	 */
	public SimpleDataSource(String url, String user, String pass) {
		init(url, user, pass);
	}
	//-------------------------------------------------------------------- Constructor end
	
	/**
	 * 初始化
	 * @param url jdbc url
	 * @param user 用户名
	 * @param pass 密码
	 */
	public void init(String url, String user, String pass) {
		this.url = url;
		this.user = user;
		this.pass = pass;
		this.driver = DbUtil.identifyDriver(url);
		try {
			Class.forName(this.driver);
		} catch (ClassNotFoundException e) {
			throw new DbRuntimeException(e, "Get jdbc driver from [{}] error!", url);
		}
	}

	//-------------------------------------------------------------------- Getters and Setters start
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	//-------------------------------------------------------------------- Getters and Setters end

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return DriverManager.getLogWriter();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		DriverManager.setLogWriter(out);
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		DriverManager.setLoginTimeout(seconds);
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return DriverManager.getLoginTimeout();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new SQLException("Simple DataSource can't support unwrap method!");
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		throw new SQLException("Simple DataSource can't support isWrapperFor method!");
	}

	@Override
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(this.url, this.user, this.pass);
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		return DriverManager.getConnection(this.url, username, password);
	}

	/**
	 * Support from JDK7
	 */
	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new SQLFeatureNotSupportedException("Simple DataSource can't support getParentLogger method!");
	}
}
