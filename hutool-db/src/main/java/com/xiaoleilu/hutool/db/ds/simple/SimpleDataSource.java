package com.xiaoleilu.hutool.db.ds.simple;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.xiaoleilu.hutool.db.DbRuntimeException;
import com.xiaoleilu.hutool.db.DbUtil;
import com.xiaoleilu.hutool.setting.Setting;

/***
 * 简易数据源，没有使用连接池，仅供测试或打开关闭连接非常少的场合使用！
 * @author loolly
 *
 */
public class SimpleDataSource extends AbstractDataSource{
	
	/** 默认的数据库连接配置文件路径 */
	public final static String DEFAULT_DB_CONFIG_PATH = "config/db.setting";
	
	//-------------------------------------------------------------------- Fields start
	private String driver;		//数据库驱动
	private String url;			//jdbc url
	private String user;			//用户名
	private String pass;			//密码
	//-------------------------------------------------------------------- Fields end
	
	/**
	 * 获得一个数据源
	 * 
	 * @param group 数据源分组
	 * @return {@link SimpleDataSource}
	 */
	synchronized public static SimpleDataSource getDataSource(String group) {
		return new SimpleDataSource(group);
	}
	
	/**
	 * 获得一个数据源，无分组
	 * @return {@link SimpleDataSource}
	 */
	synchronized public static SimpleDataSource getDataSource() {
		return new SimpleDataSource();
	}
	
	//-------------------------------------------------------------------- Constructor start
	/**
	 * 构造
	 */
	public SimpleDataSource() {
		this(null);
	}
	
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
				setting.getByGroup("url", group), 
				setting.getByGroup("user", group), 
				setting.getByGroup("pass", group)
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
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(this.url, this.user, this.pass);
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		return DriverManager.getConnection(this.url, username, password);
	}

	@Override
	public void close() throws IOException {
		//Not need to close;
	}
}
