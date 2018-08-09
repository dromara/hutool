package cn.hutool.db.ds;

import javax.sql.DataSource;

import cn.hutool.core.io.resource.NoResourceException;
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
 * 抽象数据源工厂类<br>
 * 通过实现{@link #getDataSource(String)} 方法实现数据源的获取<br>
 * 如果{@link DataSource} 的实现是数据库连接池库，应该在getDataSource调用时创建数据源并缓存
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
	public static final String[] KEY_ALIAS_URL = { "url", "jdbcUrl" };
	/** 别名字段名：用户名 */
	public static final String[] KEY_ALIAS_USER = { "user", "username" };
	/** 别名字段名：密码 */
	public static final String[] KEY_ALIAS_PASSWORD = { "password", "pass" };
	/** 别名字段名：驱动名 */
	public static final String[] KEY_ALIAS_DRIVER = { "driver", "driverClassName" };

	/** 数据源名 */
	protected String dataSourceName;
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
			} catch (NoResourceException e) {
				// 尝试ClassPath下直接读取配置文件
				try {
					setting = new Setting(DEFAULT_DB_SETTING_PATH2, true);
				} catch (NoResourceException e2) {
					throw new NoResourceException("Default db setting [{}] or [{}] in classpath not found !", DEFAULT_DB_SETTING_PATH, DEFAULT_DB_SETTING_PATH2);
				}
			}
		}

		//读取配置，用于SQL打印
		DbUtil.setShowSqlGlobal(setting);

		this.setting = setting;
	}

	/**
	 * 获取配置，用于自定义添加配置项
	 * 
	 * @return Setting
	 * @since 4.0.3
	 */
	public Setting getSetting() {
		return this.setting;
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
		return GlobalDSFactory.get().getDataSource(group);
	}

	/**
	 * 根据Setting获取当前数据源工厂对象
	 * 
	 * @param setting 数据源配置文件
	 * @return 当前使用的数据源工厂
	 * @deprecated 此方法容易引起歧义，应使用{@link #create(Setting)} 方法代替之
	 */
	@Deprecated
	public static DSFactory getCurrentDSFactory(Setting setting) {
		return create(setting);
	}

	/**
	 * 设置全局的数据源工厂<br>
	 * 在项目中存在多个连接池库的情况下，我们希望使用低优先级的库时使用此方法自定义之<br>
	 * 重新定义全局的数据源工厂此方法可在以下两种情况下调用：
	 * 
	 * <pre>
	 * 1. 在get方法调用前调用此方法来自定义全局的数据源工厂
	 * 2. 替换已存在的全局数据源工厂，当已存在时会自动关闭
	 * </pre>
	 * 
	 * @param dsFactory 数据源工厂
	 * @return 自定义的数据源工厂
	 */
	public static DSFactory setCurrentDSFactory(DSFactory dsFactory) {
		return GlobalDSFactory.set(dsFactory);
	}

	/**
	 * 创建数据源实现工厂<br>
	 * 此方法通过“试错”方式查找引入项目的连接池库，按照优先级寻找，一旦寻找到则创建对应的数据源工厂<br>
	 * 连接池优先级：Hikari > Druid > Tomcat > Dbcp > C3p0 > Hutool Pooled
	 * 
	 * @return 日志实现类
	 */
	public static DSFactory create(Setting setting) {
		final DSFactory dsFactory = doCreate(setting);
		log.debug("Use [{}] DataSource As Default", dsFactory.dataSourceName);
		return dsFactory;
	}

	/**
	 * 创建数据源实现工厂<br>
	 * 此方法通过“试错”方式查找引入项目的连接池库，按照优先级寻找，一旦寻找到则创建对应的数据源工厂<br>
	 * 连接池优先级：Hikari > Druid > Tomcat > Dbcp > C3p0 > Hutool Pooled
	 * 
	 * @return 日志实现类
	 * @since 4.1.3
	 */
	private static DSFactory doCreate(Setting setting) {
		try {
			return new HikariDSFactory(setting);
		} catch (NoClassDefFoundError e) {
			// ignore
		}
		try {
			return new DruidDSFactory(setting);
		} catch (NoClassDefFoundError e) {
			// ignore
		}
		try {
			return new TomcatDSFactory(setting);
		} catch (NoClassDefFoundError e) {
			// ignore
		}
		try {
			return new DbcpDSFactory(setting);
		} catch (NoClassDefFoundError e) {
			// ignore
		}
		try {
			return new C3p0DSFactory(setting);
		} catch (NoClassDefFoundError e) {
			// ignore
		}
		return new PooledDSFactory(setting);
	}
	// ------------------------------------------------------------------------- Static end
}
