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

package org.dromara.hutool.template.engine.velocity;

import org.dromara.hutool.template.Template;
import org.dromara.hutool.template.TemplateConfig;
import org.dromara.hutool.template.TemplateEngine;
import org.dromara.hutool.text.StrUtil;
import org.dromara.hutool.util.ObjUtil;
import org.apache.velocity.app.Velocity;

/**
 * Velocity模板引擎<br>
 * 见：<a href="http://velocity.apache.org/">http://velocity.apache.org</a>
 *
 * @author looly
 */
public class VelocityEngine implements TemplateEngine {

	private org.apache.velocity.app.VelocityEngine engine;
	private TemplateConfig config;

	// --------------------------------------------------------------------------------- Constructor start

	/**
	 * 默认构造
	 */
	public VelocityEngine() {
	}

	/**
	 * 构造
	 *
	 * @param config 模板配置
	 */
	public VelocityEngine(final TemplateConfig config) {
		init(config);
	}

	/**
	 * 构造
	 *
	 * @param engine {@link org.apache.velocity.app.VelocityEngine}
	 */
	public VelocityEngine(final org.apache.velocity.app.VelocityEngine engine) {
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
	private void init(final org.apache.velocity.app.VelocityEngine engine) {
		this.engine = engine;
	}

	/**
	 * 获取原始的引擎对象
	 *
	 * @return 原始引擎对象
	 * @since 5.5.8
	 */
	@Override
	public org.apache.velocity.app.VelocityEngine getRawEngine() {
		return this.engine;
	}

	@Override
	public Template getTemplate(String resource) {
		if (null == this.engine) {
			init(TemplateConfig.DEFAULT);
		}

		// 目录前缀
		final String root;
		// 自定义编码
		String charsetStr = null;
		if (null != this.config) {
			root = this.config.getPath();
			charsetStr = this.config.getCharsetStr();

			// 修正template目录，在classpath或者web_root模式下，按照配置添加默认前缀
			// 如果用户已经自行添加了前缀，则忽略之
			final TemplateConfig.ResourceMode resourceMode = this.config.getResourceMode();
			if (TemplateConfig.ResourceMode.CLASSPATH == resourceMode
					|| TemplateConfig.ResourceMode.WEB_ROOT == resourceMode) {
				resource = StrUtil.addPrefixIfNot(resource, StrUtil.addSuffixIfNot(root, "/"));
			}
		}

		return VelocityTemplate.wrap(engine.getTemplate(resource, charsetStr));
	}

	/**
	 * 创建引擎
	 *
	 * @param config 模板配置
	 * @return {@link org.apache.velocity.app.VelocityEngine}
	 */
	private static org.apache.velocity.app.VelocityEngine createEngine(TemplateConfig config) {
		if (null == config) {
			config = new TemplateConfig();
		}

		final org.apache.velocity.app.VelocityEngine ve = new org.apache.velocity.app.VelocityEngine();
		// 编码
		final String charsetStr = config.getCharset().toString();
		ve.setProperty(Velocity.INPUT_ENCODING, charsetStr);
		// ve.setProperty(Velocity.OUTPUT_ENCODING, charsetStr);
		ve.setProperty(Velocity.FILE_RESOURCE_LOADER_CACHE, true); // 使用缓存

		// loader
		switch (config.getResourceMode()) {
			case CLASSPATH:
				ve.setProperty("resource.loader.file.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
				break;
			case FILE:
				// path
				final String path = config.getPath();
				if (null != path) {
					ve.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, path);
				}
				break;
			case WEB_ROOT:
				ve.setProperty(Velocity.RESOURCE_LOADERS, "webapp");
				ve.setProperty("webapp.resource.loader.class", "org.apache.velocity.tools.view.servlet.WebappLoader");
				ve.setProperty("webapp.resource.loader.path", ObjUtil.defaultIfNull(config.getPath(), StrUtil.SLASH));
				break;
			case STRING:
				ve.setProperty(Velocity.RESOURCE_LOADERS, "str");
				ve.setProperty("resource.loader.str.class", SimpleStringResourceLoader.class.getName());
				break;
			default:
				break;
		}

		ve.init();
		return ve;
	}
}
