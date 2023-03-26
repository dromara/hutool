/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.hutool.db.ds;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.map.SafeConcurrentHashMap;
import cn.hutool.core.text.StrUtil;
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
public abstract class AbstractDSFactory implements DSFactory {
	private static final long serialVersionUID = -6407302276272379881L;

	/**
	 * 数据源名
	 */
	protected final String dataSourceName;
	/**
	 * 数据库连接配置文件
	 */
	private final Setting setting;
	/**
	 * 数据源池
	 */
	private final Map<String, DSWrapper> dsMap;

	/**
	 * 构造
	 *
	 * @param dataSourceName  数据源名称
	 * @param dataSourceClass 数据库连接池实现类，用于检测所提供的DataSource类是否存在，当传入的DataSource类不存在时抛出ClassNotFoundException<br>
	 *                        此参数的作用是在detectDSFactory方法自动检测所用连接池时，如果实现类不存在，调用此方法会自动抛出异常，从而切换到下一种连接池的检测。
	 * @param setting         数据库连接配置，如果为{@code null}，则读取全局自定义或默认配置
	 */
	public AbstractDSFactory(final String dataSourceName, final Class<? extends DataSource> dataSourceClass, Setting setting) {
		//此参数的作用是在detectDSFactory方法自动检测所用连接池时，如果实现类不存在，调用此方法会自动抛出异常，从而切换到下一种连接池的检测。
		Assert.notNull(dataSourceClass);

		this.dataSourceName = dataSourceName;

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
	public String getDataSourceName() {
		return this.dataSourceName;
	}

	@Override
	public DataSource getDataSource(String group) {
		if (group == null) {
			group = StrUtil.EMPTY;
		}

		// 如果已经存在已有数据源（连接池）直接返回
		return dsMap.computeIfAbsent(group, this::_createDataSource);
	}

	@Override
	synchronized public void closeDataSource(String group) {
		if (group == null) {
			group = StrUtil.EMPTY;
		}

		final DSWrapper ds = dsMap.get(group);
		if (ds != null) {
			ds.close();
			dsMap.remove(group);
		}
	}

	@Override
	public void close() {
		if (MapUtil.isNotEmpty(dsMap)) {
			final Collection<DSWrapper> values = dsMap.values();
			for (final DSWrapper ds : values) {
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
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final AbstractDSFactory other = (AbstractDSFactory) obj;
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

	/**
	 * 创建新的{@link DataSource}<br>
	 * 子类通过实现此方法，创建一个对接连接池的数据源
	 *
	 * @param jdbcUrl     JDBC连接字符串
	 * @param driver      数据库驱动类名
	 * @param user        用户名
	 * @param pass        密码
	 * @param poolSetting 分组下的连接池配置文件
	 * @return {@link DataSource}
	 */
	protected abstract DataSource createDataSource(String jdbcUrl, String driver, String user, String pass, Setting poolSetting);

	/**
	 * 创建数据源，对于不同连接池名称的的差异做兼容，如用户配置user和username都表示用户名
	 *
	 * @param group 分组
	 * @return {@link DSWrapper} 数据源包装
	 */
	private DSWrapper _createDataSource(String group) {
		if (group == null) {
			group = StrUtil.EMPTY;
		}

		final Setting config = setting.getSetting(group);
		if (MapUtil.isEmpty(config)) {
			throw new DbRuntimeException("No config for group: [{}]", group);
		}

		// 基本信息
		final String url = config.getAndRemove(DSKeys.KEY_ALIAS_URL);
		if (StrUtil.isBlank(url)) {
			throw new DbRuntimeException("No JDBC URL for group: [{}]", group);
		}

		// 移除用户可能误加入的show sql配置项
		// issue#I3VW0R@Gitee
		removeShowSqlParams(config);

		// 自动识别Driver
		String driver = config.getAndRemove(DSKeys.KEY_ALIAS_DRIVER);
		if (StrUtil.isBlank(driver)) {
			driver = DriverUtil.identifyDriver(url);
		}
		final String user = config.getAndRemove(DSKeys.KEY_ALIAS_USER);
		final String pass = config.getAndRemove(DSKeys.KEY_ALIAS_PASSWORD);

		return DSWrapper.wrap(createDataSource(url, driver, user, pass, config), driver);
	}

	/**
	 * 移除配置文件中的Show SQL相关配置项<br>
	 * 此方法用于移除用户配置在分组下的配置项目
	 *
	 * @param setting 配置项
	 * @since 5.7.2
	 */
	private static void removeShowSqlParams(final Setting setting) {
		setting.remove(DSKeys.KEY_SHOW_SQL);
		setting.remove(DSKeys.KEY_FORMAT_SQL);
		setting.remove(DSKeys.KEY_SHOW_PARAMS);
		setting.remove(DSKeys.KEY_SQL_LEVEL);
	}
}
