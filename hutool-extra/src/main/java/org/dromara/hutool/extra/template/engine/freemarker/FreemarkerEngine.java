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

package org.dromara.hutool.extra.template.engine.freemarker;

import org.dromara.hutool.core.classloader.ClassLoaderUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.extra.template.Template;
import org.dromara.hutool.extra.template.TemplateConfig;
import org.dromara.hutool.extra.template.engine.TemplateEngine;
import org.dromara.hutool.extra.template.TemplateException;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;

import java.io.IOException;

/**
 * FreeMarker模板引擎封装<br>
 * 见：<a href="https://freemarker.apache.org/">https://freemarker.apache.org/</a>
 *
 * @author looly
 */
public class FreemarkerEngine implements TemplateEngine {

	private Configuration cfg;

	// --------------------------------------------------------------------------------- Constructor start

	/**
	 * 默认构造
	 */
	public FreemarkerEngine() {
	}

	/**
	 * 构造
	 *
	 * @param config 模板配置
	 */
	public FreemarkerEngine(final TemplateConfig config) {
		init(config);
	}

	/**
	 * 构造
	 *
	 * @param freemarkerCfg {@link Configuration}
	 */
	public FreemarkerEngine(final Configuration freemarkerCfg) {
		init(freemarkerCfg);
	}
	// --------------------------------------------------------------------------------- Constructor end

	@Override
	public TemplateEngine init(TemplateConfig config) {
		if (null == config) {
			config = TemplateConfig.DEFAULT;
		}
		init(createCfg(config));
		return this;
	}

	/**
	 * 初始化引擎
	 *
	 * @param freemarkerCfg Configuration
	 */
	private void init(final Configuration freemarkerCfg) {
		this.cfg = freemarkerCfg;
	}

	@Override
	public Template getTemplate(final String resource) {
		if (null == this.cfg) {
			init(TemplateConfig.DEFAULT);
		}
		try {
			return FreemarkerTemplate.wrap(this.cfg.getTemplate(resource));
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		} catch (final Exception e) {
			throw new TemplateException(e);
		}
	}

	/**
	 * 获取原始引擎的钩子方法，用于自定义特殊属性，如插件等
	 *
	 * @return {@link Configuration}
	 * @since 5.8.7
	 */
	@Override
	public Configuration getRaw() {
		return this.cfg;
	}

	/**
	 * 创建配置项
	 *
	 * @param config 模板配置
	 * @return {@link Configuration }
	 */
	private static Configuration createCfg(TemplateConfig config) {
		if (null == config) {
			config = new TemplateConfig();
		}

		final Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
		cfg.setLocalizedLookup(false);
		cfg.setDefaultEncoding(config.getCharset().toString());

		switch (config.getResourceMode()) {
			case CLASSPATH:
				cfg.setTemplateLoader(new ClassTemplateLoader(ClassLoaderUtil.getClassLoader(), config.getPath()));
				break;
			case FILE:
				try {
					cfg.setTemplateLoader(new FileTemplateLoader(FileUtil.file(config.getPath())));
				} catch (final IOException e) {
					throw new IORuntimeException(e);
				}
				break;
			case WEB_ROOT:
				try {
					cfg.setTemplateLoader(new FileTemplateLoader(FileUtil.file(FileUtil.getWebRoot(), config.getPath())));
				} catch (final IOException e) {
					throw new IORuntimeException(e);
				}
				break;
			case STRING:
				cfg.setTemplateLoader(new SimpleStringTemplateLoader());
				break;
			default:
				break;
		}

		return cfg;
	}
}
