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

package org.dromara.hutool.extra.template.engine.beetl;

import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.extra.template.Template;
import org.dromara.hutool.extra.template.TemplateConfig;
import org.dromara.hutool.extra.template.engine.TemplateEngine;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.ResourceLoader;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.beetl.core.resource.CompositeResourceLoader;
import org.beetl.core.resource.FileResourceLoader;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.beetl.core.resource.WebAppResourceLoader;

import java.io.IOException;

/**
 * Beetl模板引擎封装
 *
 * @author looly
 */
public class BeetlEngine implements TemplateEngine {

	private GroupTemplate engine;

	// --------------------------------------------------------------------------------- Constructor start
	/**
	 * 默认构造
	 */
	public BeetlEngine() {}

	/**
	 * 构造
	 *
	 * @param config 模板配置
	 */
	public BeetlEngine(final TemplateConfig config) {
		init(config);
	}

	/**
	 * 构造
	 *
	 * @param engine {@link GroupTemplate}
	 */
	public BeetlEngine(final GroupTemplate engine) {
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
	private void init(final GroupTemplate engine){
		this.engine = engine;
	}

	@Override
	public Template getTemplate(final String resource) {
		if(null == this.engine){
			init(TemplateConfig.DEFAULT);
		}
		return BeetlTemplate.wrap(engine.getTemplate(resource));
	}

	/**
	 * 获取原始引擎的钩子方法，用于自定义特殊属性，如插件等
	 *
	 * @return {@link GroupTemplate}
	 * @since 5.8.7
	 */
	@Override
	public GroupTemplate getRawEngine() {
		return this.engine;
	}

	/**
	 * 创建引擎
	 *
	 * @param config 模板配置
	 * @return {@link GroupTemplate}
	 */
	private static GroupTemplate createEngine(TemplateConfig config) {
		if (null == config) {
			config = TemplateConfig.DEFAULT;
		}

		switch (config.getResourceMode()) {
		case CLASSPATH:
			return createGroupTemplate(new ClasspathResourceLoader(config.getPath(), config.getCharsetStr()));
		case FILE:
			return createGroupTemplate(new FileResourceLoader(config.getPath(), config.getCharsetStr()));
		case WEB_ROOT:
			return createGroupTemplate(new WebAppResourceLoader(config.getPath(), config.getCharsetStr()));
		case STRING:
			return createGroupTemplate(new StringTemplateResourceLoader());
		case COMPOSITE:
			//TODO 需要定义复合资源加载器
			return createGroupTemplate(new CompositeResourceLoader());
		default:
			return new GroupTemplate();
		}
	}

	/**
	 * 创建自定义的模板组 {@link GroupTemplate}，配置文件使用全局默认<br>
	 * 此时自定义的配置文件可在ClassPath中放入beetl.properties配置
	 *
	 * @param loader {@link ResourceLoader}，资源加载器
	 * @return {@link GroupTemplate}
	 * @since 3.2.0
	 */
	private static GroupTemplate createGroupTemplate(final ResourceLoader<?> loader) {
		try {
			return createGroupTemplate(loader, Configuration.defaultConfiguration());
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 创建自定义的 {@link GroupTemplate}
	 *
	 * @param loader {@link ResourceLoader}，资源加载器
	 * @param conf {@link Configuration} 配置文件
	 * @return {@link GroupTemplate}
	 */
	private static GroupTemplate createGroupTemplate(final ResourceLoader<?> loader, final Configuration conf) {
		return new GroupTemplate(loader, conf);
	}
}
