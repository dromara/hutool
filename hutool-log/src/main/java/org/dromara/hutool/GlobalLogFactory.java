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

package org.dromara.hutool;

import org.dromara.hutool.dialect.commons.ApacheCommonsLogFactory;
import org.dromara.hutool.dialect.console.ConsoleLogFactory;
import org.dromara.hutool.dialect.jdk.JdkLogFactory;
import org.dromara.hutool.dialect.log4j.Log4jLogFactory;
import org.dromara.hutool.dialect.log4j2.Log4j2LogFactory;
import org.dromara.hutool.dialect.slf4j.Slf4jLogFactory;

/**
 * 全局日志工厂类<br>
 * 用于减少日志工厂创建，减少日志库探测
 *
 * @author looly
 * @since 4.0.3
 */
public class GlobalLogFactory {
	private static volatile LogFactory currentLogFactory;
	private static final Object lock = new Object();

	/**
	 * 获取单例日志工厂类，如果不存在创建之
	 *
	 * @return 当前使用的日志工厂
	 */
	public static LogFactory get() {
		if (null == currentLogFactory) {
			synchronized (lock) {
				if (null == currentLogFactory) {
					currentLogFactory = LogFactory.of();
				}
			}
		}
		return currentLogFactory;
	}

	/**
	 * 自定义日志实现
	 *
	 * @see Slf4jLogFactory
	 * @see Log4jLogFactory
	 * @see Log4j2LogFactory
	 * @see ApacheCommonsLogFactory
	 * @see JdkLogFactory
	 * @see ConsoleLogFactory
	 *
	 * @param logFactoryClass 日志工厂类
	 * @return 自定义的日志工厂类
	 */
	public static LogFactory set(final Class<? extends LogFactory> logFactoryClass) {
		try {
			return set(logFactoryClass.newInstance());
		} catch (final Exception e) {
			throw new IllegalArgumentException("Can not instance LogFactory class!", e);
		}
	}

	/**
	 * 自定义日志实现
	 *
	 * @see Slf4jLogFactory
	 * @see Log4jLogFactory
	 * @see Log4j2LogFactory
	 * @see ApacheCommonsLogFactory
	 * @see JdkLogFactory
	 * @see ConsoleLogFactory
	 *
	 * @param logFactory 日志工厂类对象
	 * @return 自定义的日志工厂类
	 */
	public static LogFactory set(final LogFactory logFactory) {
		logFactory.getLog(GlobalLogFactory.class).debug("Custom Use [{}] Logger.", logFactory.name);
		currentLogFactory = logFactory;
		return currentLogFactory;
	}
}
