/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.extra.template.engine;

import org.dromara.hutool.core.lang.Singleton;
import org.dromara.hutool.core.reflect.ConstructorUtil;
import org.dromara.hutool.core.spi.SpiUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.extra.template.TemplateConfig;
import org.dromara.hutool.extra.template.TemplateException;
import org.dromara.hutool.log.LogUtil;

/**
 * 简单模板引擎工厂，用于根据用户引入的模板引擎jar，自动创建对应的模板引擎对象<br>
 * 使用简单工厂（Simple Factory）模式
 *
 * @author looly
 */
public class TemplateEngineFactory {

	/**
	 * 根据用户引入的模板引擎jar，自动创建对应的模板引擎对象<br>
	 * 获得的是单例的TemplateEngine
	 *
	 * @return 单例的TemplateEngine
	 */
	public static TemplateEngine getEngine() {
		final TemplateEngine engine = Singleton.get(TemplateEngine.class.getName(), TemplateEngineFactory::createEngine);
		LogUtil.debug("Use [{}] Template Engine As Default.", StrUtil.removeSuffix(engine.getClass().getSimpleName(), "Engine"));
		return engine;
	}

	/**
	 * 根据用户引入的模板引擎jar，自动创建对应的模板引擎对象<br>
	 * 推荐创建的引擎单例使用，此方法每次调用会返回新的引擎
	 *
	 * @return {@link TemplateEngine}
	 * @since 5.3.3
	 */
	public static TemplateEngine createEngine() {
		return createEngine(TemplateConfig.DEFAULT);
	}

	/**
	 * 根据用户引入的模板引擎jar，自动创建对应的模板引擎对象<br>
	 * 推荐创建的引擎单例使用，此方法每次调用会返回新的引擎
	 *
	 * @param config 模板配置，包括编码、模板文件path等信息
	 * @return {@link TemplateEngine}
	 */
	public static TemplateEngine createEngine(final TemplateConfig config) {
		return doCreateEngine(config);
	}

	/**
	 * 根据用户引入的模板引擎jar，自动创建对应的模板引擎对象<br>
	 * 推荐创建的引擎单例使用，此方法每次调用会返回新的引擎
	 *
	 * @param config 模板配置，包括编码、模板文件path等信息
	 * @return {@link TemplateEngine}
	 */
	private static TemplateEngine doCreateEngine(final TemplateConfig config) {
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
