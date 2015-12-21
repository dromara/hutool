package com.xiaoleilu.hutool.db.ds.pool;

import com.xiaoleilu.hutool.Setting;
import com.xiaoleilu.hutool.db.DbUtil;

/**
 * 数据库配置文件类
 * @author Looly
 *
 */
public class DbSetting {
	/** 默认的数据库连接配置文件路径 */
	public final static String DEFAULT_DB_CONFIG_PATH = "config/db.setting";
	
	private Setting setting;
	
	/**
	 * 构造
	 */
	public DbSetting() {
		this(null);
	}
	
	/**
	 * 构造
	 * @param setting 数据库配置
	 */
	public DbSetting(Setting setting) {
		if(null == setting) {
			this.setting = new Setting(DEFAULT_DB_CONFIG_PATH);
		}
	}
	
	/**
	 * 获得数据库连接信息
	 * @param group 分组
	 * @return 分组
	 */
	public DbConfig getDbConfig(String group){
		DbConfig config = new DbConfig();
		
		//基本信息
		final String jdbcUrl = setting.getString("url", group);
		config.setDriver(setting.getStringWithDefault("driver", group, DbUtil.identifyDriver(jdbcUrl)));
		config.setUrl(jdbcUrl);
		config.setUser(setting.getStr("user", group));
		config.setPass(setting.getStr("pass", group));
		
		//连接池相关信息
		config.setInitialSize(setting.getInt("initialSize", 0));
		config.setMinIdle(setting.getInt("minIdle", 0));
		config.setMaxActive(setting.getInt("maxActive", 8));
		config.setMaxWait(setting.getLong("maxWait", 6000L));
		
		return config;
	}
}
