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

package org.dromara.hutool.extra.template.engine.jte;

import gg.jte.CodeResolver;
import gg.jte.ContentType;
import gg.jte.resolve.DirectoryCodeResolver;
import gg.jte.resolve.ResourceCodeResolver;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.extra.template.Template;
import org.dromara.hutool.extra.template.TemplateConfig;
import org.dromara.hutool.extra.template.engine.TemplateEngine;

import java.nio.file.Paths;

/**
 * jte实现<br>
 * 见：<a href="https://jte.gg/">https://jte.gg/</a>
 *
 * @author dy
 */
public class JteEngine implements TemplateEngine {

	private TemplateConfig config = TemplateConfig.DEFAULT;
	private gg.jte.TemplateEngine engine;
	private final ContentType contentType = ContentType.Plain;

	// --------------------------------------------------------------------------------- Constructor start

	/**
	 * 默认构造
	 */
	public JteEngine() {
	}

	/**
	 * 构造
	 *
	 * @param config 模板配置
	 */
	public JteEngine(final TemplateConfig config) {
		this.config = config;
		createEngine();
	}

	/**
	 * 构造
	 *
	 * @param codeResolver {@link CodeResolver}
	 */
	public JteEngine(final CodeResolver codeResolver) {
		createEngine(codeResolver, contentType);
	}
	// --------------------------------------------------------------------------------- Constructor end

	@Override
	public TemplateEngine init(TemplateConfig config) {
		if (config != null) {
			this.config = config;
		}
		createEngine();
		return this;
	}

	@Override
	public Template getTemplate(String resource) {
		if (TemplateConfig.ResourceMode.STRING.equals(config.getResourceMode())) {
			if (!StrUtil.endWithAny(config.getPath(), ".jte", ".kte")) {
				throw new RuntimeException("路径path需以.jte/.kte结尾");
			}
			createEngine(new SimpleStringCodeResolver(MapUtil.of(config.getPath(), resource)), contentType);
			return new JteTemplate(engine, config.getPath());
		} else {
			return new JteTemplate(engine, resource);
		}
	}

	@Override
	public gg.jte.TemplateEngine getRaw() {
		return this.engine;
	}

	/**
	 * 创建引擎 {@link gg.jte.TemplateEngine }
	 */
	private void createEngine() {
		switch (config.getResourceMode()) {
			case CLASSPATH:
				createEngine(new ResourceCodeResolver(config.getPath(), JteEngine.class.getClassLoader()), contentType);
				break;
			case FILE:
				createEngine(new DirectoryCodeResolver(Paths.get(config.getPath())), contentType);
				break;
			case STRING:
				// 这里无法直接创建引擎
				break;
			default:
				break;
		}
	}

	/**
	 * 创建引擎 {@link gg.jte.TemplateEngine }
	 *
	 * @param codeResolver CodeResolver
	 * @param contentType  ContentType
	 */
	private void createEngine(CodeResolver codeResolver, ContentType contentType) {
		this.engine = gg.jte.TemplateEngine.create(codeResolver, contentType);
	}
}
