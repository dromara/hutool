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

package org.dromara.hutool.extra.template.engine.thymeleaf;

import org.dromara.hutool.core.lang.Assert;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.DefaultTemplateResolver;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.extra.template.Template;
import org.dromara.hutool.extra.template.TemplateConfig;
import org.dromara.hutool.extra.template.engine.TemplateEngine;

/**
 * Thymeleaf模板引擎实现
 *
 * @author looly
 * @since 4.1.11
 */
public class ThymeleafEngine implements TemplateEngine {

	org.thymeleaf.TemplateEngine engine;
	TemplateConfig config;

	// --------------------------------------------------------------------------------- Constructor start

	/**
	 * 默认构造
	 */
	public ThymeleafEngine() {
		// SPI方式加载时检查库是否引入
		Assert.notNull(org.thymeleaf.TemplateEngine.class);
	}

	/**
	 * 构造
	 *
	 * @param config 模板配置
	 */
	public ThymeleafEngine(final TemplateConfig config) {
		init(config);
	}

	/**
	 * 构造
	 *
	 * @param engine {@link org.thymeleaf.TemplateEngine}
	 */
	public ThymeleafEngine(final org.thymeleaf.TemplateEngine engine) {
		init(engine);
	}
	// --------------------------------------------------------------------------------- Constructor end

	@Override
	public TemplateEngine init(TemplateConfig config) {
		if (null == config) {
			config = TemplateConfig.DEFAULT;
		}
		this.config = config;
		init(createEngine(config));
		return this;
	}

	/**
	 * 初始化引擎
	 *
	 * @param engine 引擎
	 */
	private void init(final org.thymeleaf.TemplateEngine engine) {
		this.engine = engine;
	}

	@Override
	public Template getTemplate(final String resource) {
		if (null == this.engine) {
			init(TemplateConfig.DEFAULT);
		}
		return ThymeleafTemplate.wrap(this.engine, resource, (null == this.config) ? null : this.config.getCharset());
	}

	/**
	 * 获取原始引擎的钩子方法，用于自定义特殊属性，如插件等
	 *
	 * @return {@link org.thymeleaf.TemplateEngine}
	 * @since 5.8.7
	 */
	@Override
	public org.thymeleaf.TemplateEngine getRaw() {
		return this.engine;
	}

	/**
	 * 创建引擎
	 *
	 * @param config 模板配置
	 * @return {@link TemplateEngine}
	 */
	private static org.thymeleaf.TemplateEngine createEngine(TemplateConfig config) {
		if (null == config) {
			config = new TemplateConfig();
		}

		final ITemplateResolver resolver;
		switch (config.getResourceMode()) {
			case CLASSPATH:
				final ClassLoaderTemplateResolver classLoaderResolver = new ClassLoaderTemplateResolver();
				classLoaderResolver.setCharacterEncoding(config.getCharsetStr());
				classLoaderResolver.setTemplateMode(TemplateMode.HTML);
				classLoaderResolver.setPrefix(StrUtil.addSuffixIfNot(config.getPath(), "/"));
				resolver = classLoaderResolver;
				break;
			case FILE:
				final FileTemplateResolver fileResolver = new FileTemplateResolver();
				fileResolver.setCharacterEncoding(config.getCharsetStr());
				fileResolver.setTemplateMode(TemplateMode.HTML);
				fileResolver.setPrefix(StrUtil.addSuffixIfNot(config.getPath(), "/"));
				resolver = fileResolver;
				break;
			case WEB_ROOT:
				final FileTemplateResolver webRootResolver = new FileTemplateResolver();
				webRootResolver.setCharacterEncoding(config.getCharsetStr());
				webRootResolver.setTemplateMode(TemplateMode.HTML);
				webRootResolver.setPrefix(StrUtil.addSuffixIfNot(FileUtil.getAbsolutePath(FileUtil.file(FileUtil.getWebRoot(), config.getPath())), "/"));
				resolver = webRootResolver;
				break;
			case STRING:
				resolver = new StringTemplateResolver();
				break;
			default:
				resolver = new DefaultTemplateResolver();
				break;
		}

		final org.thymeleaf.TemplateEngine engine = new org.thymeleaf.TemplateEngine();
		engine.setTemplateResolver(resolver);
		return engine;
	}
}
