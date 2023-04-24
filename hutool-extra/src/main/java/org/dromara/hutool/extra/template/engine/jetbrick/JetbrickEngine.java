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

package org.dromara.hutool.extra.template.engine.jetbrick;

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
	public JetbrickEngine() {}

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
