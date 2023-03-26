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

package cn.hutool.extra.template.engine.freemarker;

import freemarker.cache.TemplateLoader;

import java.io.Reader;
import java.io.StringReader;

/**
 * {@link TemplateLoader} 字符串实现形式<br>
 * 用于直接获取字符串模板
 *
 * @author looly
 * @since 4.3.3
 */
public class SimpleStringTemplateLoader implements TemplateLoader {

	@Override
	public Object findTemplateSource(final String name) {
		return name;
	}

	@Override
	public long getLastModified(final Object templateSource) {
		return System.currentTimeMillis();
	}

	@Override
	public Reader getReader(final Object templateSource, final String encoding) {
		return new StringReader((String) templateSource);
	}

	@Override
	public void closeTemplateSource(final Object templateSource) {
		// ignore
	}

}
