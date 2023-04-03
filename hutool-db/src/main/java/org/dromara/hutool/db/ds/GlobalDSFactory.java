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

package org.dromara.hutool.db.ds;

import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.util.RuntimeUtil;
import org.dromara.hutool.log.StaticLog;

/**
 * 全局单例数据源工厂<br>
 * 一般情况下，一个应用默认只使用一种数据库连接池，因此维护一个全局的数据源工厂类减少判断连接池类型造成的性能浪费
 *
 * @author looly
 * @since 4.0.2
 */
public class GlobalDSFactory {

	private static volatile DSFactory factory;
	private static final Object lock = new Object();

	/*
	 * 设置在JVM关闭时关闭所有数据库连接
	 */
	static {
		// JVM关闭时关闭所有连接池
		RuntimeUtil.addShutdownHook(()->{
			if (null != factory) {
				IoUtil.closeQuietly(factory);
				StaticLog.debug("DataSource: [{}] closed.", factory.getDataSourceName());
				factory = null;
			}
		});
	}

	/**
	 * 获取默认的数据源工厂，读取默认数据库配置文件<br>
	 * 此处使用懒加载模式，在第一次调用此方法时才创建默认数据源工厂<br>
	 * 如果想自定义全局的数据源工厂，请在第一次调用此方法前调用{@link #set(DSFactory)} 方法自行定义
	 *
	 * @return 当前使用的数据源工厂
	 */
	public static DSFactory get() {
		if (null == factory) {
			synchronized (lock) {
				if (null == factory) {
					factory = DSUtil.createFactory(null);
				}
			}
		}
		return factory;
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
	 * @param customDSFactory 自定义数据源工厂
	 * @return 自定义的数据源工厂
	 */
	public static DSFactory set(final DSFactory customDSFactory) {
		synchronized (lock) {
			if (null != factory) {
				if (factory.equals(customDSFactory)) {
					return factory;// 数据源工厂不变时返回原数据源工厂
				}
				// 自定义数据源工厂前关闭之前的数据源
				IoUtil.closeQuietly(factory);
			}

			StaticLog.debug("Custom use [{}] DataSource.", customDSFactory.getDataSourceName());
			factory = customDSFactory;
		}
		return factory;
	}
}
