package com.xiaoleilu.hutool.db.ds.hikari;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.xiaoleilu.hutool.db.DbUtil;
import com.xiaoleilu.hutool.exceptions.UtilException;
import com.xiaoleilu.hutool.io.IoUtil;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.StaticLog;
import com.xiaoleilu.hutool.setting.Setting;
import com.xiaoleilu.hutool.util.CollectionUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * HikariCP数据源
 * @author Looly
 *
 */
public class HikariDS {
	private final static Log log = StaticLog.get();
	
	/** 默认的Druid配置文件路径 */
	public final static String DEFAULT_HIKARI_CONFIG_PATH = "config/hikari.setting";
	/** 默认的数据库连接配置文件路径 */
	public final static String DEFAULT_DB_CONFIG_PATH = "config/db.setting";
	
	static {
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				HikariDS.closeAll();
			}
		});
	}
	
	/*--------------------------私有变量 start-------------------------------*/
	/** JDBC配置对象 */
	private static Setting _dbSetting;
	private static Setting _poolSetting;

	/** 数据源池 */
	private static Map<String, HikariDataSource> dsMap;
	/*--------------------------私有变量 end-------------------------------*/

	/**
	 * 初始化数据库连接配置文件
	 * 
	 * @param poolSetting Druid配置文件
	 * @param dbSetting 数据库配置文件
	 */
	synchronized public static void init(Setting dbSetting, Setting poolSetting) {
		dsMap = new HashMap<>();

		if (dbSetting == null) {
			try {
				dbSetting = new Setting(DEFAULT_DB_CONFIG_PATH, true);
			}catch(Exception e) {
				log.info("Druid setting file [{}] not found.", DEFAULT_DB_CONFIG_PATH);
			}
		}
		_dbSetting = dbSetting;
		
		// 初始化数据库连接配置文件
		if (poolSetting == null) {
			try {
				poolSetting = new Setting(DEFAULT_HIKARI_CONFIG_PATH, true);
			}catch(Exception e) {
				log.info("No default Hikari config file [{}] found, custom to init it.", DEFAULT_HIKARI_CONFIG_PATH);
			}
		}
		_poolSetting = poolSetting;
	}

	/**
	 * 获得一个数据源
	 * 
	 * @param group 数据源分组
	 * @throws ConnException
	 */
	synchronized public static HikariDataSource getDataSource(String group) {
		if(null == dsMap) {
			//如果用户未指定配置文件，使用默认
			init(null, null);
		}
		
		if(_dbSetting == null) {
			throw new UtilException("No setting found, please init it!");
		}
		if(group == null) {
			group = StrUtil.EMPTY;
		}

		// 如果已经存在已有数据源（连接池）直接返回
		final HikariDataSource existedDataSource = dsMap.get(group);
		if (existedDataSource != null) {
			return existedDataSource;
		}

		HikariConfig hikariConfig = new HikariConfig(_poolSetting.getProperties(group));
		final String jdbcUrl = _dbSetting.getString("url", group);
		log.debug("JDBC url: {}", jdbcUrl);
		hikariConfig.setJdbcUrl(jdbcUrl);
		hikariConfig.setDataSourceClassName(_dbSetting.getStr("dataSourceClassName"));
		hikariConfig.setDriverClassName(_dbSetting.getStringWithDefault("driver", group, DbUtil.identifyDriver(jdbcUrl)));
		hikariConfig.setUsername(_dbSetting.getString("user", group));
		hikariConfig.setPassword(_dbSetting.getString("pass", group));
		
		final HikariDataSource dds = new HikariDataSource(hikariConfig);
		
		// 添加到数据源池中，以备下次使用
		dsMap.put(group, dds);
		return dds;
	}

	/**
	 * 获得默认数据源（连接池），链接信息来自于配置文件
	 * 
	 * @return 数据源
	 */
	public static HikariDataSource getDataSource() {
		return getDataSource(null);
	}

	/**
	 * 获得一个数据库连接池中的连接
	 * 
	 * @param datasource 数据源名称，此名称在配置文件中定义
	 * @return 连接对象
	 * @throws SQLException
	 * @throws ConnException
	 */
	public static Connection getConnection(String datasource) throws SQLException {
		return getDataSource(datasource).getConnection();
	}

	/**
	 * 获得一个默认连接池中的连接（此默认连接取决于配置文件）
	 * 
	 * @return 连接对象
	 * @throws SQLException
	 * @throws ConnException
	 */
	public static Connection getConnection() throws SQLException {
		return getConnection(null);
	}

	/**
	 * 关闭数据源
	 * 
	 * @param dsName 数据源名称
	 */
	synchronized public static void closeDs(String dsName) {
		if(dsName == null) {
			dsName = StrUtil.EMPTY;
		}
		
		HikariDataSource dds = dsMap.get(dsName);
		if (dds != null) {
			IoUtil.close(dds);
			dsMap.remove(dsName);
		}
	}

	/**
	 * 关闭默认数据源
	 */
	synchronized public static void closeDs() {
		closeDs(null);
	}

	/**
	 * 关闭所有连接池
	 */
	synchronized public static void closeAll() {
		if(CollectionUtil.isNotEmpty(dsMap)){
			Collection<HikariDataSource> values = dsMap.values();
			for (HikariDataSource ds : values) {
				IoUtil.close(ds);
			}
			dsMap.clear();
			dsMap = null;
		}
	}
}
