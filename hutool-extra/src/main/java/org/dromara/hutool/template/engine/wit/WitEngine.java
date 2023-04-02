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

package org.dromara.hutool.template.engine.wit;

import org.dromara.hutool.io.file.FileUtil;
import org.dromara.hutool.map.Dict;
import org.dromara.hutool.template.Template;
import org.dromara.hutool.template.TemplateConfig;
import org.dromara.hutool.template.TemplateEngine;
import org.dromara.hutool.template.TemplateException;
import org.febit.wit.Engine;
import org.febit.wit.exceptions.ResourceNotFoundException;
import org.febit.wit.util.Props;

import java.io.File;

/**
 * Wit(http://zqq90.github.io/webit-script/)模板引擎封装
 *
 * @author looly
 */
public class WitEngine implements TemplateEngine {

	private Engine engine;

	// --------------------------------------------------------------------------------- Constructor start
	/**
	 * 默认构造
	 */
	public WitEngine() {}

	/**
	 * 构造
	 *
	 * @param config 模板配置
	 */
	public WitEngine(final TemplateConfig config) {
		init(config);
	}

	/**
	 * 构造
	 *
	 * @param engine {@link Engine}
	 */
	public WitEngine(final Engine engine) {
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
	private void init(final Engine engine){
		this.engine = engine;
	}

	@Override
	public Template getTemplate(final String resource) {
		if(null == this.engine){
			init(TemplateConfig.DEFAULT);
		}
		try {
			return WitTemplate.wrap(engine.getTemplate(resource));
		} catch (final ResourceNotFoundException e) {
			throw new TemplateException(e);
		}
	}

	/**
	 * 获取原始引擎的钩子方法，用于自定义特殊属性，如插件等
	 *
	 * @return {@link Engine}
	 * @since 5.8.7
	 */
	@Override
	public Engine getRawEngine() {
		return this.engine;
	}

	/**
	 * 创建引擎
	 *
	 * @param config 模板配置
	 * @return {@link Engine}
	 */
	private static Engine createEngine(final TemplateConfig config) {
		final Props configProps = Engine.createConfigProps("");
		Dict dict = null;

		if (null != config) {
			dict = Dict.of();
			// 自定义编码
			dict.set("DEFAULT_ENCODING", config.getCharset());

			switch (config.getResourceMode()){
				case CLASSPATH:
					configProps.set("pathLoader.root", config.getPath());
					configProps.set("routeLoader.defaultLoader", "classpathLoader");
					break;
				case STRING:
					configProps.set("routeLoader.defaultLoader", "stringLoader");
					break;
				case FILE:
					configProps.set("pathLoader.root", config.getPath());
					configProps.set("routeLoader.defaultLoader", "fileLoader");
					break;
				case WEB_ROOT:
					final File root = FileUtil.file(FileUtil.getWebRoot(), config.getPath());
					configProps.set("pathLoader.root", FileUtil.getAbsolutePath(root));
					configProps.set("routeLoader.defaultLoader", "fileLoader");
					break;
			}
		}

		return Engine.create(configProps,dict);
	}
}
