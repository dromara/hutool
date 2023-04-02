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

package org.dromara.hutool.template.engine.rythm;

import org.dromara.hutool.template.Template;
import org.dromara.hutool.template.TemplateConfig;
import org.dromara.hutool.template.TemplateEngine;

import java.util.Properties;

/**
 * Rythm模板引擎<br>
 * 文档：http://rythmengine.org/doc/index
 *
 * @author looly
 *
 */
public class RythmEngine implements TemplateEngine {

	org.rythmengine.RythmEngine engine;

	// --------------------------------------------------------------------------------- Constructor start
	/**
	 * 默认构造
	 */
	public RythmEngine() {}

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
	public org.rythmengine.RythmEngine getRawEngine() {
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
