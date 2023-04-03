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

package org.dromara.hutool.extra.template.engine.thymeleaf;

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
import org.dromara.hutool.extra.template.TemplateEngine;

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
	public org.thymeleaf.TemplateEngine getRawEngine() {
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
