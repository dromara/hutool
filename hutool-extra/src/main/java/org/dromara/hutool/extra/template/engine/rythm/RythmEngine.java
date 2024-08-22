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

package org.dromara.hutool.extra.template.engine.rythm;

import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.extra.template.Template;
import org.dromara.hutool.extra.template.TemplateConfig;
import org.dromara.hutool.extra.template.engine.TemplateEngine;

import java.util.Properties;

/**
 * Rythm模板引擎<br>
 * 文档：http://rythmengine.org/doc/index
 *
 * @author looly
 *
 */
public class RythmEngine implements TemplateEngine {

	private org.rythmengine.RythmEngine engine;

	// --------------------------------------------------------------------------------- Constructor start
	/**
	 * 默认构造
	 */
	public RythmEngine() {
		// SPI方式加载时检查库是否引入
		Assert.notNull(org.rythmengine.RythmEngine.class);
	}

	/**
	 * 构造
	 *
	 * @param config 模板配置
	 */
	public RythmEngine(final TemplateConfig config) {
		init(config);
	}

	/**
	 * 构造
	 *
	 * @param engine {@link org.rythmengine.RythmEngine}
	 */
	public RythmEngine(final org.rythmengine.RythmEngine engine) {
		init(engine);
	}
	// --------------------------------------------------------------------------------- Constructor end

	@Override
	public TemplateEngine init(TemplateConfig config) {
		if(null == config){
			config = TemplateConfig.DEFAULT;
		}
		init(createEngine(config));
		return this;
	}

	/**
	 * 初始化引擎
	 * @param engine 引擎
	 */
	private void init(final org.rythmengine.RythmEngine engine){
		this.engine = engine;
	}

	@Override
	public Template getTemplate(final String resource) {
		if(null == this.engine){
			init(TemplateConfig.DEFAULT);
		}
		return RythmTemplate.wrap(this.engine.getTemplate(resource));
	}

	/**
	 * 获取原始引擎的钩子方法，用于自定义特殊属性，如插件等
	 *
	 * @return {@link org.rythmengine.RythmEngine}
	 * @since 5.8.7
	 */
	@Override
	public org.rythmengine.RythmEngine getRaw() {
		return this.engine;
	}

	/**
	 * 创建引擎
	 *
	 * @param config 模板配置
	 * @return {@link org.rythmengine.RythmEngine}
	 */
	private static org.rythmengine.RythmEngine createEngine(TemplateConfig config) {
		if (null == config) {
			config = new TemplateConfig();
		}

		final Properties props = new Properties();
		final String path = config.getPath();
		if (null != path) {
			props.put("home.template", path);
		}

		return new org.rythmengine.RythmEngine(props);
	}
}
