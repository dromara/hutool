package cn.hutool.db.ds;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.map.SafeConcurrentHashMap;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.DbRuntimeException;
import cn.hutool.db.DbUtil;
import cn.hutool.db.GlobalDbConfig;
import cn.hutool.db.dialect.DriverUtil;
import cn.hutool.setting.Setting;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.Map;

/**
 * 抽象数据源工厂<br>
 * 此工厂抽象类用于实现数据源的缓存，当用户多次调用{@link #getDataSource(String)} 时，工厂只需创建一次即可。<br>
 * 数据源是与配置文件中的分组相关的，每个分组的数据源相互独立，也就是每个分组的数据源是单例存在的。
 *
 * @author looly
 */
public abstract class AbstractDSFactory extends DSFactory {
	private static final long serialVersionUID = -6407302276272379881L;

	/**
	 * 数据库连接配置文件
	 */
	private final Setting setting;
	/**
	 * 数据源池
	 */
	private final Map<String, DataSourceWrapper> dsMap;

	/**
	 * 构造
	 *
	 * @param dataSourceName  数据源名称
	 * @param dataSourceClass 数据库连接池实现类，用于检测所提供的DataSource类是否存在，当传入的DataSource类不存在时抛出ClassNotFoundException<br>
	 *                        此参数的作用是在detectDSFactory方法自动检测所用连接池时，如果实现类不存在，调用此方法会自动抛出异常，从而切换到下一种连接池的检测。
	 * @param setting         数据库连接配置，如果为{@code null}，则读取全局自定义或默认配置
	 */
	public AbstractDSFactory(String dataSourceName, Class<? extends DataSource> dataSourceClass, Setting setting) {
		super(dataSourceName);
		//此参数的作用是在detectDSFactory方法自动检测所用连接池时，如果实现类不存在，调用此方法会自动抛出异常，从而切换到下一种连接池的检测。
		Assert.notNull(dataSourceClass);

		if (null == setting) {
			setting = GlobalDbConfig.createDbSetting();
		}

		// 读取配置，用于SQL打印
		DbUtil.setShowSqlGlobal(setting);

		this.setting = setting;
		this.dsMap = new SafeConcurrentHashMap<>();
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

	@Override
	synchronized public DataSource getDataSource(String group) {
		if (group == null) {
			group = StrUtil.EMPTY;
		}

		// 如果已经存在已有数据源（连接池）直接返回
		final DataSourceWrapper existedDataSource = dsMap.get(group);
		if (existedDataSource != null) {
			return existedDataSource;
		}

		final DataSourceWrapper ds = createDataSource(group);
		// 添加到数据源池中，以备下次使用
		dsMap.put(group, ds);
		return ds;
	}

	/**
	 * 创建数据源
	 *
	 * @param group 分组
	 * @return {@link DataSourceWrapper} 数据源包装
	 */
	private DataSourceWrapper createDataSource(String group) {
		if (group == null) {
			group = StrUtil.EMPTY;
		}

		final Setting config = setting.getSetting(group);
		if (MapUtil.isEmpty(config)) {
			throw new DbRuntimeException("No config for group: [{}]", group);
		}

		// 基本信息
		final String url = config.getAndRemoveStr(KEY_ALIAS_URL);
		if (StrUtil.isBlank(url)) {
			throw new DbRuntimeException("No JDBC URL for group: [{}]", group);
		}

		// 移除用户可能误加入的show sql配置项
		// issue#I3VW0R@Gitee
		DbUtil.removeShowSqlParams(config);

		// 自动识别Driver
		String driver = config.getAndRemoveStr(KEY_ALIAS_DRIVER);
		if (StrUtil.isBlank(driver)) {
			driver = DriverUtil.identifyDriver(url);
		}
		final String user = config.getAndRemoveStr(KEY_ALIAS_USER);
		final String pass = config.getAndRemoveStr(KEY_ALIAS_PASSWORD);

		return DataSourceWrapper.wrap(createDataSource(url, driver, user, pass, config), driver);
	}

	/**
	 * 创建新的{@link DataSource}<br>
	 *
	 * @param jdbcUrl     JDBC连接字符串
	 * @param driver      数据库驱动类名
	 * @param user        用户名
	 * @param pass        密码
	 * @param poolSetting 分组下的连接池配置文件
	 * @return {@link DataSource}
	 */
	protected abstract DataSource createDataSource(String jdbcUrl, String driver, String user, String pass, Setting poolSetting);

	@Override
	public void close(String group) {
		if (group == null) {
			group = StrUtil.EMPTY;
		}

		DataSourceWrapper ds = dsMap.get(group);
		if (ds != null) {
			ds.close();
			dsMap.remove(group);
		}
	}

	@Override
	public void destroy() {
		if (MapUtil.isNotEmpty(dsMap)) {
			Collection<DataSourceWrapper> values = dsMap.values();
			for (DataSourceWrapper ds : values) {
				ds.close();
			}
			dsMap.clear();
		}
	}

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
		AbstractDSFactory other = (AbstractDSFactory) obj;
		if (dataSourceName == null) {
			if (other.dataSourceName != null) {
				return false;
			}
		} else if (!dataSourceName.equals(other.dataSourceName)) {
			return false;
		}
		if (setting == null) {
			return other.setting == null;
		} else {
			return setting.equals(other.setting);
		}
	}
}
