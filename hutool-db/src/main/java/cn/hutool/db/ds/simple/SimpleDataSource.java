package cn.hutool.db.ds.simple;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.DbRuntimeException;
import cn.hutool.db.dialect.DriverUtil;
import cn.hutool.db.ds.DSFactory;
import cn.hutool.setting.Setting;
import cn.hutool.setting.dialect.Props;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/***
 * 简易数据源，没有使用连接池，仅供测试或打开关闭连接非常少的场合使用！
 * 
 * @author loolly
 *
 */
public class SimpleDataSource extends AbstractDataSource {

	/** 默认的数据库连接配置文件路径 */
	public final static String DEFAULT_DB_CONFIG_PATH = "config/db.setting";

	// -------------------------------------------------------------------- Fields start
	private String driver; // 数据库驱动
	private String url; // jdbc url
	private String user; // 用户名
	private String pass; // 密码

	// 连接配置
	private Properties connProps;
	// -------------------------------------------------------------------- Fields end

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
	 * 
	 * @return {@link SimpleDataSource}
	 */
	synchronized public static SimpleDataSource getDataSource() {
		return new SimpleDataSource();
	}

	// -------------------------------------------------------------------- Constructor start
	/**
	 * 构造
	 */
	public SimpleDataSource() {
		this(null);
	}

	/**
	 * 构造
	 * 
	 * @param group 数据库配置文件中的分组
	 */
	public SimpleDataSource(String group) {
		this(null, group);
	}

	/**
	 * 构造
	 * 
	 * @param setting 数据库配置
	 * @param group 数据库配置文件中的分组
	 */
	public SimpleDataSource(Setting setting, String group) {
		if (null == setting) {
			setting = new Setting(DEFAULT_DB_CONFIG_PATH);
		}
		final Setting config = setting.getSetting(group);
		if (CollectionUtil.isEmpty(config)) {
			throw new DbRuntimeException("No DataSource config for group: [{}]", group);
		}

		init(//
				config.getAndRemoveStr(DSFactory.KEY_ALIAS_URL), //
				config.getAndRemoveStr(DSFactory.KEY_ALIAS_USER), //
				config.getAndRemoveStr(DSFactory.KEY_ALIAS_PASSWORD), //
				config.getAndRemoveStr(DSFactory.KEY_ALIAS_DRIVER)//
		);

		// 其它连接参数
		this.connProps = config.getProps(Setting.DEFAULT_GROUP);
	}

	/**
	 * 构造
	 * 
	 * @param url jdbc url
	 * @param user 用户名
	 * @param pass 密码
	 */
	public SimpleDataSource(String url, String user, String pass) {
		init(url, user, pass);
	}

	/**
	 * 构造
	 * 
	 * @param url jdbc url
	 * @param user 用户名
	 * @param pass 密码
	 * @param driver JDBC驱动类
	 * @since 3.1.2
	 */
	public SimpleDataSource(String url, String user, String pass, String driver) {
		init(url, user, pass, driver);
	}
	// -------------------------------------------------------------------- Constructor end

	/**
	 * 初始化
	 * 
	 * @param url jdbc url
	 * @param user 用户名
	 * @param pass 密码
	 */
	public void init(String url, String user, String pass) {
		init(url, user, pass, null);
	}

	/**
	 * 初始化
	 * 
	 * @param url jdbc url
	 * @param user 用户名
	 * @param pass 密码
	 * @param driver JDBC驱动类，传入空则自动识别驱动类
	 * @since 3.1.2
	 */
	public void init(String url, String user, String pass, String driver) {
		this.driver = StrUtil.isNotBlank(driver) ? driver : DriverUtil.identifyDriver(url);
		try {
			Class.forName(this.driver);
		} catch (ClassNotFoundException e) {
			throw new DbRuntimeException(e, "Get jdbc driver [{}] error!", driver);
		}
		this.url = url;
		this.user = user;
		this.pass = pass;
	}

	// -------------------------------------------------------------------- Getters and Setters start
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

	public Properties getConnProps() {
		return connProps;
	}

	public void setConnProps(Properties connProps) {
		this.connProps = connProps;
	}

	public void addConnProps(String key, String value){
		if(null == this.connProps){
			this.connProps = new Properties();
		}
		this.connProps.setProperty(key, value);
	}
	// -------------------------------------------------------------------- Getters and Setters end

	@Override
	public Connection getConnection() throws SQLException {
		final Props info = new Props();
		if (this.user != null) {
			info.setProperty("user", this.user);
		}
		if (this.pass != null) {
			info.setProperty("password", this.pass);
		}

		// 其它参数
		final Properties connProps = this.connProps;
		if(MapUtil.isNotEmpty(connProps)){
			info.putAll(connProps);
		}

		return DriverManager.getConnection(this.url, info);
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		return DriverManager.getConnection(this.url, username, password);
	}

	@Override
	public void close() {
		// Not need to close;
	}
}
