/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
