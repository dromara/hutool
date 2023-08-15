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

package org.dromara.hutool.extra.template.engine.velocity;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;
import org.apache.velocity.util.ExtProperties;

import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.util.CharsetUtil;

/**
 * {@link ResourceLoader} 的字符串实现形式<br>
 * 用于直接获取字符串模板
 *
 * @author looly
 *
 */
public class SimpleStringResourceLoader extends ResourceLoader {

	@Override
	public void init(final ExtProperties configuration) {
	}

	/**
	 * 获取资源流
	 *
	 * @param source 字符串模板
	 * @return 流
	 * @throws ResourceNotFoundException 资源未找到
	 */
	public InputStream getResourceStream(final String source) throws ResourceNotFoundException {
		return IoUtil.toStream(source, CharsetUtil.UTF_8);
	}

	@Override
	public Reader getResourceReader(final String source, final String encoding) throws ResourceNotFoundException {
		return new StringReader(source);
	}

	@Override
	public boolean isSourceModified(final Resource resource) {
		return false;
	}

	@Override
	public long getLastModified(final Resource resource) {
		return 0;
	}

}
