/*
 * Copyright (c) 2023. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.db.ds;

import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.lang.Singleton;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.map.SafeConcurrentHashMap;
import org.dromara.hutool.core.spi.SpiUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.db.DbRuntimeException;
import org.dromara.hutool.db.GlobalDbConfig;
import org.dromara.hutool.db.driver.DriverUtil;
import org.dromara.hutool.log.LogUtil;
import org.dromara.hutool.setting.Setting;
import org.dromara.hutool.setting.props.Props;

import javax.sql.DataSource;
import java.io.Closeable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 数据源池，用于支持多数据源。<br>
 * 在指定Setting中配置多个数据源时，通过分组（group）区分<br>
 * 每次获得一个数据源则缓存在pool中，确保数据源保持单例状态。
 *
 * @author Looly
 * @since 6.0.0
 */
public class DSPool implements Closeable {

	private static final String CONNECTION_PREFIX = "connection.";

	/**
	 * 获取单例池对象
	 *
	 * @return 数据源池
	 */
	public static DSPool getInstance() {
		return Singleton.get(DSPool.class.getName(), DSPool::new);
	}

	/**
	 * 数据库连接配置文件
	 */
	private final Setting setting;
	/**
	 * 数据源池
	 */
	private final Map<String, DSWrapper> pool;
	/**
	 * 连接工厂
	 */
	private DSFactory factory;

	/**
	 * 构造，通过SPI方式自动获取用户引入的连接池，使用classpath:db.setting
	 */
	public DSPool() {
		this(null);
	}

	/**
	 * 构造，通过SPI方式自动获取用户引入的连接池
	 *
	 * @param setting 数据库配置，支持多数据源，{@code null}表示读取classpath:db.setting
	 */
	public DSPool(final Setting setting) {
		this(setting, null);
	}

	/**
	 * 构造
	 *
	 * @param setting 数据库配置，支持多数据源，{@code null}表示读取classpath:db.setting
	 * @param factory 数据源工厂，用于创建数据源，{@code null}表示使用SPI自动获取
	 */
	public DSPool(final Setting setting, final DSFactory factory) {
		this.setting = null != setting ? setting : GlobalDbConfig.createDbSetting();
		this.factory = null != factory ? factory : SpiUtil.loadFirstAvailable(DSFactory.class);
		this.pool = new SafeConcurrentHashMap<>();
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
	 * 获取数据源名称，用于识别当前使用连接池类型
	 *
	 * @return 数据源名称
	 */
	public String getDataSourceName() {
		return this.factory.getDataSourceName();
	}

	/**
	 * 设置自定义的{@link DSFactory}
	 *
	 * @param factory {@link DSFactory}
	 * @return this
	 */
	public DSPool setFactory(final DSFactory factory) {
		this.factory = factory;
		LogUtil.debug("Custom use [{}] DataSource.", factory.getDataSourceName());
		return this;
	}

	/**
	 * 获取指定分组的数据源，单例获取
	 *
	 * @param group 分组，{@code null}表示默认分组
	 * @return 数据源
	 */
	public DataSource getDataSource(String group) {
		if (group == null) {
			group = StrUtil.EMPTY;
		}

		// 如果已经存在已有数据源（连接池）直接返回
		return pool.computeIfAbsent(group, this::createDSWrapper);
	}

	/**
	 * 关闭指定数据源
	 *
	 * @param group 分组
	 * @return this
	 */
	public DSPool closeDataSource(String group) {
		if (group == null) {
			group = StrUtil.EMPTY;
		}

		// 此处线程安全，任意线程进入一旦remove完成，后续线程调用remove后都为null
		final DSWrapper removed = pool.remove(group);
		if (null != removed) {
			IoUtil.closeQuietly(removed);
		}
		return this;
	}

	@Override
	public void close() {
		final Map<String, DSWrapper> pool = this.pool;
		if (MapUtil.isNotEmpty(pool)) {
			// 此处线程安全，多线程调用可能多次调用clear，不影响
			final Collection<DSWrapper> values = pool.values();
			pool.clear();
			for (final DSWrapper ds : values) {
				ds.close();
			}
		}
	}

	/**
	 * 创建数据源，对于不同连接池名称的的差异做兼容，如用户配置user和username都表示用户名
	 *
	 * @param group 分组，{@code null}表示默认分组
	 * @return {@link DSWrapper} 数据源包装
	 */
	private DSWrapper createDSWrapper(String group) {
		if (group == null) {
			group = StrUtil.EMPTY;
		}

		final Setting subSetting = setting.getSetting(group);
		if (MapUtil.isEmpty(subSetting)) {
			throw new DbRuntimeException("No config for group: [{}]", group);
		}

		final DbConfig dbConfig = toDbConfig(subSetting);

		return DSWrapper.wrap(factory.createDataSource(dbConfig), dbConfig.getDriver());
	}

	/**
	 * {@link Setting}数据库配置 转 {@link DbConfig}
	 *
	 * @param setting {@link Setting}数据库配置
	 * @return {@link DbConfig}
	 */
	private static DbConfig toDbConfig(final Setting setting) {
		// 基本信息
		final String url = setting.getAndRemove(DSKeys.KEY_ALIAS_URL);
		if (StrUtil.isBlank(url)) {
			throw new DbRuntimeException("No JDBC URL!");
		}

		// 移除用户可能误加入的show sql配置项
		// issue#I3VW0R@Gitee
		removeShowSqlParams(setting);

		// 自动识别Driver
		String driver = setting.getAndRemove(DSKeys.KEY_ALIAS_DRIVER);
		if (StrUtil.isBlank(driver)) {
			driver = DriverUtil.identifyDriver(url);
		}

		final DbConfig dbConfig = DbConfig.of()
			.setUrl(url)
			.setDriver(driver)
			.setUser(setting.getAndRemove(DSKeys.KEY_ALIAS_USER))
			.setPass(setting.getAndRemove(DSKeys.KEY_ALIAS_PASSWORD));

		// remarks等连接配置，since 5.3.8
		String connValue;
		for (final String key : DSKeys.KEY_CONN_PROPS) {
			connValue = setting.getAndRemove(key);
			if (StrUtil.isNotBlank(connValue)) {
				dbConfig.addConnProps(key, connValue);
			}
		}

		// 自定义连接属性
		final Props connProps = new Props();
		final Set<String> keys = setting.keySet();
		for (final String key : keys) {
			if (key.startsWith(CONNECTION_PREFIX)) {
				connProps.set(StrUtil.subSuf(key, CONNECTION_PREFIX.length()), setting.remove(key));
			}
		}
		dbConfig.setConnProps(connProps);

		// 池属性
		dbConfig.setPoolProps(setting.toProps());

		return dbConfig;
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
