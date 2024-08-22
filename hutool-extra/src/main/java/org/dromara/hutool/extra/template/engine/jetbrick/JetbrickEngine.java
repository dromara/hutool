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

package org.dromara.hutool.extra.template.engine.jetbrick;

import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.extra.template.Template;
import org.dromara.hutool.extra.template.TemplateConfig;
import org.dromara.hutool.extra.template.engine.TemplateEngine;
import jetbrick.template.JetEngine;

import java.util.Properties;

/**
 * Jetbrick模板引擎封装<br>
 * 见：https://github.com/subchen/jetbrick-template-2x
 *
 * @author looly
 * @since 5.7.21
 */
public class JetbrickEngine implements TemplateEngine {

	private JetEngine engine;

	// --------------------------------------------------------------------------------- Constructor start
	/**
	 * 默认构造
	 */
	public JetbrickEngine() {
		// SPI方式加载时检查库是否引入
		Assert.notNull(JetbrickEngine.class);
	}

	/**
	 * 构造
	 *
	 * @param config 模板配置
	 */
	public JetbrickEngine(final TemplateConfig config) {
		init(config);
	}

	/**
	 * 构造
	 *
	 * @param engine {@link JetEngine}
	 */
	public JetbrickEngine(final JetEngine engine) {
		init(engine);
	}
	// --------------------------------------------------------------------------------- Constructor end


	@Override
	public TemplateEngine init(final TemplateConfig config) {
		init(createEngine(config));
		return this;
	}

	/**
	 * 初始化引擎
	 * @param engine 引擎
	 */
	private void init(final JetEngine engine){
		this.engine = engine;
	}

	@Override
	public Template getTemplate(final String resource) {
		if(null == this.engine){
			init(TemplateConfig.DEFAULT);
		}
		return JetbrickTemplate.wrap(engine.getTemplate(resource));
	}

	/**
	 * 获取原始引擎的钩子方法，用于自定义特殊属性，如插件等
	 *
	 * @return {@link JetEngine}
	 * @since 5.8.7
	 */
	@Override
	public JetEngine getRaw() {
		return this.engine;
	}

	/**
	 * 创建引擎
	 *
	 * @param config 模板配置
	 * @return {@link JetEngine}
	 */
	private static JetEngine createEngine(TemplateConfig config) {
		if (null == config) {
			config = TemplateConfig.DEFAULT;
		}

		final Properties props = new Properties();
		props.setProperty("jetx.input.encoding", config.getCharsetStr());
		props.setProperty("jetx.output.encoding", config.getCharsetStr());
		props.setProperty("jetx.template.loaders", "$loader");

		switch (config.getResourceMode()){
			case CLASSPATH:
				props.setProperty("$loader", "jetbrick.template.loader.ClasspathResourceLoader");
				props.setProperty("$loader.root", config.getPath());
				break;
			case FILE:
				props.setProperty("$loader", "jetbrick.template.loader.FileSystemResourceLoader");
				props.setProperty("$loader.root", config.getPath());
				break;
			case WEB_ROOT:
				props.setProperty("$loader", "jetbrick.template.loader.ServletResourceLoader");
				props.setProperty("$loader.root", config.getPath());
				break;
			case STRING:
				props.setProperty("$loader", "org.dromara.hutool.extra.template.engine.jetbrick.loader.StringResourceLoader");
				props.setProperty("$loader.charset", config.getCharsetStr());
				break;
			default:
				// 默认
				return JetEngine.create();
		}

		return JetEngine.create(props);
	}
}
