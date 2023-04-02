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

package org.dromara.hutool.core.convert.impl;

import org.dromara.hutool.core.convert.AbstractConverter;

import java.io.File;
import java.net.URI;
import java.net.URL;

/**
 * URL对象转换器
 * @author Looly
 *
 */
public class URLConverter extends AbstractConverter{
	private static final long serialVersionUID = 1L;

	@Override
	protected URL convertInternal(final Class<?> targetClass, final Object value) {
		try {
			if(value instanceof File){
				return ((File)value).toURI().toURL();
			}

			if(value instanceof URI){
				return ((URI)value).toURL();
			}
			return new URL(convertToStr(value));
		} catch (final Exception e) {
			// Ignore Exception
		}
		return null;
	}

}
