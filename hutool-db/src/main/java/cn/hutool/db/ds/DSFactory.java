package cn.hutool.db.ds;

import javax.sql.DataSource;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.DbUtil;
import cn.hutool.db.ds.c3p0.C3p0DSFactory;
import cn.hutool.db.ds.dbcp.DbcpDSFactory;
import cn.hutool.db.ds.druid.DruidDSFactory;
import cn.hutool.db.ds.hikari.HikariDSFactory;
import cn.hutool.db.ds.pooled.PooledDSFactory;
import cn.hutool.db.ds.tomcat.TomcatDSFactory;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.hutool.setting.Setting;

/**
 * 数据源工厂类
 * 
 * @author Looly
 *
 */
public abstract class DSFactory {
	private static final Log log = LogFactory.get();

	/** 数据库配置文件可选路径1 */
	protected static final String DEFAULT_DB_SETTING_PATH = "config/db.setting";
	/** 数据库配置文件可选路径2 */
	protected static final String DEFAULT_DB_SETTING_PATH2 = "db.setting";
	
	/** 别名字段名：URL */
	public static final String[] KEY_ALIAS_URL = {"url", "jdbcUrl"};
	/** 别名字段名：用户名 */
	public static final String[] KEY_ALIAS_USER = {"user", "username"};
	/** 别名字段名：密码 */
	public static final String[] KEY_ALIAS_PASSWORD = {"password", "pass"};
	/** 别名字段名：驱动名 */
	public static final String[] KEY_ALIAS_DRIVER = {"driver", "driverClassName"};

	/** 数据源名 */
	private String dataSourceName;
	/** 数据库连接配置文件 */
	protected Setting setting;

	/**
	 * 构造
	 * 
	 * @param dataSourceName 数据源名称
	 * @param dataSourceClass 数据库连接池实现类，用于检测所提供的DataSource类是否存在，当传入的DataSource类不存在时抛出ClassNotFoundException<br>
	 *            此参数的作用是在detectDSFactory方法自动检测所用连接池时，如果实现类不存在，调用此方法会自动抛出异常，从而切换到下一种连接池的检测。
	 * @param setting 数据库连接配置
	 */
	public DSFactory(String dataSourceName, Class<? extends DataSource> dataSourceClass, Setting setting) {
		this.dataSourceName = dataSourceName;
		if (null == setting) {
			try {
				setting = new Setting(DEFAULT_DB_SETTING_PATH, true);
			} catch (IORuntimeException e) {
				//尝试ClassPath下直接读取配置问津
				setting = new Setting(DEFAULT_DB_SETTING_PATH2, true);
			}
		}
		this.setting = setting;
		
		//初始化SQL显示
		if(null != this.setting) {
			final boolean isShowSql = Convert.toBool(this.setting.remove("showSql"), false);
			final boolean isFormatSql = Convert.toBool(this.setting.remove("formatSql"), false);
			DbUtil.setShowSqlGlobal(isShowSql, isFormatSql);
		}
	}

	/**
	 * 获得默认数据源
	 * 
	 * @return 数据源
	 */
	public DataSource getDataSource() {
		return getDataSource(StrUtil.EMPTY);
	}

	/**
	 * 获得分组对应数据源
	 * 
	 * @param group 分组名
	 * @return 数据源
	 */
	public abstract DataSource getDataSource(String group);

	/**
	 * 关闭默认数据源（空组）
	 */
	public void close() {
		close(StrUtil.EMPTY);
	}

	/**
	 * 关闭对应数据源
	 * 
	 * @param group 分组
	 */
	public abstract void close(String group);

	/**
	 * 销毁工厂类，关闭所有数据源
	 */
	public abstract void destroy();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataSourceName == null) ? 0 : dataSourceName.hashCode());
		result = prime * result + ((setting == null) ? 0 : setting.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DSFactory other = (DSFactory) obj;
		if (dataSourceName == null) {
			if (other.dataSourceName != null) {
				return false;
			}
		} else if (!dataSourceName.equals(other.dataSourceName)) {
			return false;
		}
		if (setting == null) {
			if (other.setting != null) {
				return false;
			}
		} else if (!setting.equals(other.setting)) {
			return false;
		}
		return true;
	}

	// ------------------------------------------------------------------------- Static start
	// JVM关闭是关闭所有连接池
	static {
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				if (null != currentDSFactory) {
					currentDSFactory.destroy();
					log.debug("DataSource: [{}] destroyed.", currentDSFactory.dataSourceName);
				}
			}
		});
	}

	private static DSFactory currentDSFactory;
	private static final Object lock = new Object();

	/**
	 * 获得数据源<br>
	 * 使用默认配置文件的无分组配置
	 * 
	 * @return 数据源
	 */
	public static DataSource get() {
		return get(null);
	}

	/**
	 * 获得数据源
	 * 
	 * @param group 配置文件中对应的分组
	 * @return 数据源
	 */
	public static DataSource get(String group) {
		return get(null, group);
	}

	/**
	 * 获得数据源
	 * 
	 * @param dbSetting 数据库配置文件，如果为<code>null</code>，查找默认的配置文件
	 * @param group 配置文件中对应的分组
	 * @return 数据源
	 */
	public static DataSource get(Setting dbSetting, String group) {
		return getCurrentDSFactory(dbSetting).getDataSource(group);
	}

	/**
	 * @param setting 数据源配置文件
	 * @return 当前使用的数据源工厂
	 */
	public static DSFactory getCurrentDSFactory(Setting setting) {
		if (null == currentDSFactory) {
			synchronized (lock) {
				if (null == currentDSFactory) {
					currentDSFactory = detectDSFactory(setting);
				}
			}
		}
		return currentDSFactory;
	}

	/**
	 * @param dsFactory 数据源工厂
	 * @return 自定义的数据源工厂
	 */
	synchronized public static DSFactory setCurrentDSFactory(DSFactory dsFactory) {
		if (null != currentDSFactory) {
			if (currentDSFactory.equals(dsFactory)) {
				return currentDSFactory;// 数据源不变时返回原数据源
			}
			// 自定义数据源工厂前关闭之前的数据源
			currentDSFactory.destroy();
		}

		log.debug("Custom use [{}] datasource.", dsFactory.dataSourceName);
		currentDSFactory = dsFactory;
		return currentDSFactory;
	}

	/**
	 * 决定数据源实现工厂<br>
	 * 连接池优先级：Hikari > Druid > Tomcat > Dbcp > C3p0 > Hutool Pooled
	 * 
	 * @return 日志实现类
	 */
	private static DSFactory detectDSFactory(Setting setting) {
		DSFactory dsFactory;
		try {
			dsFactory = new HikariDSFactory(setting);
		} catch (NoClassDefFoundError e1) {
			try {
				dsFactory = new DruidDSFactory(setting);
			} catch (NoClassDefFoundError e2) {
				try {
					dsFactory = new TomcatDSFactory(setting);
				} catch (NoClassDefFoundError e3) {
					try {
						dsFactory = new DbcpDSFactory(setting);
					} catch (NoClassDefFoundError e4) {
						try {
							dsFactory = new C3p0DSFactory(setting);
						} catch (NoClassDefFoundError e5) {
							// 默认使用Hutool实现的简易连接池
							dsFactory = new PooledDSFactory(setting);
						}
					}
				}
			}
		}
		log.debug("Use [{}] DataSource As Default", dsFactory.dataSourceName);
		return dsFactory;
	}
	// ------------------------------------------------------------------------- Static end
}
