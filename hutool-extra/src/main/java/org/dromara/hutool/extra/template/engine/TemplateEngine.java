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
