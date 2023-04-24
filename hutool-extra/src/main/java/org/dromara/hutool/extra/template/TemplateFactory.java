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

package org.dromara.hutool.extra.template;

import org.dromara.hutool.core.lang.Singleton;
import org.dromara.hutool.core.reflect.ConstructorUtil;
import org.dromara.hutool.core.spi.SpiUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.extra.template.engine.TemplateEngine;
import org.dromara.hutool.log.StaticLog;

/**
 * 简单模板殷勤工厂，用于根据用户引入的模板引擎jar，自动创建对应的模板引擎对象
 *
 * @author looly
 */
public class TemplateFactory {

	/**
	 * 根据用户引入的模板引擎jar，自动创建对应的模板引擎对象<br>
	 * 获得的是单例的TemplateEngine
	 *
	 * @return 单例的TemplateEngine
	 */
	public static TemplateEngine getEngine() {
		return Singleton.get(TemplateEngine.class.getName(), TemplateFactory::createEngine);
	}

	/**
	 * 根据用户引入的模板引擎jar，自动创建对应的模板引擎对象<br>
	 * 推荐创建的引擎单例使用，此方法每次调用会返回新的引擎
	 *
	 * @return {@link TemplateEngine}
	 * @since 5.3.3
	 */
	public static TemplateEngine createEngine() {
		return createEngine(new TemplateConfig());
	}

	/**
	 * 根据用户引入的模板引擎jar，自动创建对应的模板引擎对象<br>
	 * 推荐创建的引擎单例使用，此方法每次调用会返回新的引擎
	 *
	 * @param config 模板配置，包括编码、模板文件path等信息
	 * @return {@link TemplateEngine}
	 */
	public static TemplateEngine createEngine(final TemplateConfig config) {
		final TemplateEngine engine = doCreate(config);
		StaticLog.debug("Use [{}] Engine As Default.", StrUtil.removeSuffix(engine.getClass().getSimpleName(), "Engine"));
		return engine;
	}

	/**
	 * 根据用户引入的模板引擎jar，自动创建对应的模板引擎对象<br>
	 * 推荐创建的引擎单例使用，此方法每次调用会返回新的引擎
	 *
	 * @param config 模板配置，包括编码、模板文件path等信息
	 * @return {@link TemplateEngine}
	 */
	private static TemplateEngine doCreate(final TemplateConfig config) {
		final Class<? extends TemplateEngine> customEngineClass = config.getCustomEngine();
		final TemplateEngine engine;
		if (null != customEngineClass) {
			// 自定义模板引擎
			engine = ConstructorUtil.newInstance(customEngineClass);
		} else {
			// SPI引擎查找
			engine = SpiUtil.loadFirstAvailable(TemplateEngine.class);
		}
		if (null != engine) {
			return engine.init(config);
		}

		throw new TemplateException("No template found! Please add one of template jar to your project !");
	}
}
