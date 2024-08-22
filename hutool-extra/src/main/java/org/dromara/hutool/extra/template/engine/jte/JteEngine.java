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

package org.dromara.hutool.extra.template.engine.jte;

import gg.jte.CodeResolver;
import gg.jte.ContentType;
import gg.jte.resolve.DirectoryCodeResolver;
import gg.jte.resolve.ResourceCodeResolver;
import org.dromara.hutool.core.lang.Assert;
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

	private final ContentType contentType = ContentType.Plain;
	private TemplateConfig config = TemplateConfig.DEFAULT;
	private gg.jte.TemplateEngine engine;

	// --------------------------------------------------------------------------------- Constructor start

	/**
	 * 默认构造
	 */
	public JteEngine() {
		// SPI方式加载时检查库是否引入
		Assert.notNull(gg.jte.TemplateEngine.class);
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
		this.engine = createEngine(codeResolver);
	}
	// --------------------------------------------------------------------------------- Constructor end

	@Override
	public TemplateEngine init(final TemplateConfig config) {
		if (config != null) {
			this.config = config;
		}
		createEngine();
		return this;
	}

	@Override
	public Template getTemplate(final String resource) {
		if (TemplateConfig.ResourceMode.STRING.equals(config.getResourceMode())) {
			String path = config.getPath();
			if(!StrUtil.endWithAny(path, ".jte", ".kte")){
				path = StrUtil.addSuffixIfNot(StrUtil.defaultIfEmpty(path, "hutool"), ".jte");
			}
			return new JteTemplate(createEngine(
				new SimpleStringCodeResolver(MapUtil.of(path, resource)))
				, path, config.getCharset());
		} else {
			return new JteTemplate(engine, resource, config.getCharset());
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
				this.engine = createEngine(new ResourceCodeResolver(config.getPath(), JteEngine.class.getClassLoader()));
				break;
			case FILE:
				this.engine = createEngine(new DirectoryCodeResolver(Paths.get(config.getPath())));
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
	 */
	private gg.jte.TemplateEngine createEngine(final CodeResolver codeResolver) {
		return gg.jte.TemplateEngine.create(codeResolver, this.contentType);
	}
}
