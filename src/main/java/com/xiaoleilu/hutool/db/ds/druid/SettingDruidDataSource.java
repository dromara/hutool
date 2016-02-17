package com.xiaoleilu.hutool.db.ds.druid;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.xiaoleilu.hutool.db.DbUtil;
import com.xiaoleilu.hutool.db.dialect.DialectFactory;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import com.xiaoleilu.hutool.setting.Setting;
import com.xiaoleilu.hutool.util.IoUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * 基于配置文件的Druid数据源
 * @author Looly
 *
 */
public class SettingDruidDataSource extends DruidDataSource {
	private static final long serialVersionUID = 5151196020608949563L;
	private final static Log log = LogFactory.get();

	/** 默认的Druid配置文件路径 */
	public final static String DEFAULT_DRUID_CONFIG_PATH = "config/druid.setting";
	/** 默认的数据库连接配置文件路径 */
	public final static String DEFAULT_DB_CONFIG_PATH = "config/db.setting";
	/** 默认的数据库连接驱动（MySQL） */
	public final static String DEFAULT_DRIVER = DialectFactory.DRIVER_MYSQL;

	/*--------------------------私有变量 start-------------------------------*/
	/** JDBC配置对象 */
	private Setting dbSetting;
	private Setting druidSetting;
	/*--------------------------私有变量 end-------------------------------*/
	
	/**
	 * 构造
	 */
	public SettingDruidDataSource() {
		this(null, null, null);
	}
	
	/**
	 * 构造
	 * @param group 分组
	 */
	public SettingDruidDataSource(String group) {
		this(null, null, group);
	}
	
	/**
	 * 初始化数据库连接配置文件
	 * 
	 * @param druid_setting Druid配置文件
	 * @param db_setting 数据库配置文件
	 * @param group 分组
	 */
	public SettingDruidDataSource(Setting druid_setting, Setting db_setting, String group) {
		init(druid_setting, db_setting, group);
	}

	/**
	 * 初始化数据库连接配置文件
	 * 
	 * @param druidSetting Druid配置文件
	 * @param dbSetting 数据库配置文件
	 */
	public void init(Setting druidSetting, Setting dbSetting, String group) {
		// 初始化数据库连接配置文件
		this.dbSetting = dbSetting;
		if (this.dbSetting == null) {
			try {
				this.dbSetting = new Setting(DEFAULT_DB_CONFIG_PATH, Setting.DEFAULT_CHARSET, true);
			} catch (Exception e) {
				log.info("No default DB config file [{}] found, custom to init it.", DEFAULT_DB_CONFIG_PATH);
			}
		}

		// 初始化Druid配置文件
		this.druidSetting = druidSetting;
		if (this.druidSetting == null) {
			try {
				this.druidSetting = new Setting(DEFAULT_DRUID_CONFIG_PATH, Setting.DEFAULT_CHARSET, true);
			} catch (Exception e) {
				log.info("Druid setting file [{}], custom to init it.", DEFAULT_DRUID_CONFIG_PATH);
			}
		}
		
		initDatasource(group);
	}

	/**
	 * 初始化数据源
	 * 
	 * @param group 数据源分组
	 * @throws ConnException
	 */
	public DataSource initDatasource(String group) {
		if (group == null) {
			group = StrUtil.EMPTY;
		}

		this.setName(group); // 数据源名称为连接名称

		final String jdbcUrl = this.dbSetting.getString("url", group);
		log.debug("JDBC url: {}", jdbcUrl);
		this.setDriverClassName(this.dbSetting.getStringWithDefault("driver", group, DbUtil.identifyDriver(jdbcUrl)));
		this.setUrl(jdbcUrl);
		this.setUsername(this.dbSetting.getString("user", group));
		this.setPassword(this.dbSetting.getString("pass", group));
		
		//注入连接池配置
		final String dsSettingPath = this.dbSetting.getString("ds.setting.path");
		if (StrUtil.isNotBlank(dsSettingPath)) {
			injectSetting(new Setting(dsSettingPath));
		}else{
			injectSetting(druidSetting);
		}
		return this;
	}

	// ------------------------------------------------------------------- Private method start
	private void injectSetting(Setting setting) {
		if(null != setting) {
			setting.toBean(this);
		}
	}
	// ------------------------------------------------------------------- Private method end
	
	@Override
	protected void finalize() throws Throwable {
		IoUtil.close(this);
	}
}
