package looly.github.hutool.db.ds;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import looly.github.hutool.FileUtil;
import looly.github.hutool.Log;
import looly.github.hutool.Setting;
import looly.github.hutool.StrUtil;
import looly.github.hutool.exceptions.SettingException;
import looly.github.hutool.exceptions.UtilException;

import org.slf4j.Logger;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 封装Druid数据源
 * 
 * @author Luxiaolei
 * 
 */
public class DruidDs {
	private static Logger logger = Log.get();
	
	/** 默认的Druid配置文件路径 */
	public final static String DEFAULT_DRUID_CONFIG_PATH = "config/druid.setting";
	/** 默认的数据库连接配置文件路径 */
	public final static String DEFAULT_DB_CONFIG_PATH = "config/db.setting";
	/** 默认的数据库连接驱动（MySQL） */
	public final static String DEFAULT_DRIVER = "com.mysql.jdbc.Driver";

	/*--------------------------私有变量 start-------------------------------*/
	/** JDBC配置对象 */
	private static Setting dbSetting;
	private static Setting druidSetting;

	/** 数据源池 */
	private static Map<String, DruidDataSource> dsMap;
	/*--------------------------私有变量 end-------------------------------*/

	static {
		init(null, null);
	}

	/**
	 * 初始化数据库连接配置文件
	 * 
	 * @param druid_setting Druid配置文件
	 * @param db_setting 数据库配置文件
	 */
	synchronized public static void init(Setting druid_setting, Setting db_setting) {
		dsMap = new HashMap<String, DruidDataSource>();

		// 初始化Druid配置文件
		druidSetting = druid_setting;
		if (druidSetting == null) {
			try {
				druidSetting = new Setting(DEFAULT_DRUID_CONFIG_PATH, Setting.DEFAULT_CHARSET, true);
			}catch(Exception e) {
				logger.info("Druid setting file {} not found.", DEFAULT_DRUID_CONFIG_PATH);
			}
		}
		// 初始化数据库连接配置文件
		dbSetting = db_setting;
		if (dbSetting == null) {
			try {
				dbSetting = new Setting(DEFAULT_DB_CONFIG_PATH, Setting.DEFAULT_CHARSET, true);
			}catch(Exception e) {
				logger.info("No default DB config file {} found, custom to init it.", DEFAULT_DB_CONFIG_PATH);
			}
		}
	}

	/**
	 * 获得一个数据源
	 * 
	 * @param dsName 数据源分组
	 * @throws ConnException
	 */
	synchronized public static DataSource getDataSource(String group) {
		if(dbSetting == null) {
			throw new UtilException("No setting found, please init it!");
		}
		if(group == null) {
			group = StrUtil.EMPTY;
		}

		// 如果已经存在已有数据源（连接池）直接返回
		DruidDataSource existedDataSource = dsMap.get(group);
		if (existedDataSource != null) {
			return existedDataSource;
		}

		// 基本连接信息
		DruidDataSource dds = new DruidDataSource();
		if(druidSetting != null) {
			try {
				// 连接池参数注入
				druidSetting.toObject(dds);
			} catch (SettingException e) {
				throw new UtilException("Read Druid setting error!", e);
			}
		}
		// 基本连接信息
		dds.setName(group); // 数据源名称为连接名称
		dds.setDriverClassName(dbSetting.getStringWithDefault("driver", group, DEFAULT_DRIVER));
		
		String jdbcUrl = dbSetting.getString("url", group);
		Log.debug("JDBC url: {}", jdbcUrl);
		dds.setUrl(jdbcUrl);
		dds.setUsername(dbSetting.getString("user", group));
		dds.setPassword(dbSetting.getString("pass", group));
		
		// 添加到数据源池中，以备下次使用
		dsMap.put(group, dds);
		return dds;
	}

	/**
	 * 获得默认数据源（连接池），链接信息来自于配置文件
	 * 
	 * @return 数据源
	 */
	synchronized public static DataSource getDataSource() {
		return getDataSource(null);
	}

	/**
	 * 获得一个数据库连接池中的连接
	 * 
	 * @param datasource 数据源名称，此名称在配置文件中定义
	 * @param sshName SSH连接名称
	 * @return 连接对象
	 * @throws SQLException
	 * @throws ConnException
	 */
	synchronized public static Connection getConnection(String datasource) throws SQLException {
		return getDataSource(datasource).getConnection();
	}

	/**
	 * 获得一个默认连接池中的连接（此默认连接取决于配置文件）
	 * 
	 * @return 连接对象
	 * @throws SQLException
	 * @throws ConnException
	 */
	synchronized public static Connection getConnection() throws SQLException {
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
		
		DruidDataSource dds = dsMap.get(dsName);
		if (dds != null) {
			FileUtil.close(dds);
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
		Collection<DruidDataSource> values = dsMap.values();
		for (DruidDataSource dds : values) {
			if (dds != null) {
				dds.close();
			}
		}
		dsMap.clear();
	}
}
