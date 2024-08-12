/*
 * Copyright (c) 2013-2024 Hutool Team.
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

package org.dromara.hutool.extra.template.engine;

import org.dromara.hutool.core.lang.wrapper.Wrapper;
import org.dromara.hutool.extra.template.Template;
import org.dromara.hutool.extra.template.TemplateConfig;

/**
 * 引擎接口，通过实现此接口从而使用对应的模板引擎
 *
 * @author looly
 */
public interface TemplateEngine extends Wrapper<Object> {

	/**
	 * 使用指定配置文件初始化模板引擎
	 *
	 * @param config 配置文件
	 * @return this
	 */
	TemplateEngine init(TemplateConfig config);

	/**
	 * 获取模板
	 *
	 * @param resource 资源，根据实现不同，此资源可以是模板本身，也可以是模板的相对路径
	 * @return 模板实现
	 */
	Template getTemplate(String resource);
}
