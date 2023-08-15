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

package org.dromara.hutool.core.xml;

import javax.xml.parsers.SAXParserFactory;

/**
 * {@link SAXParserFactory} 工具
 *
 * @author looly
 * @since 6.0.0
 */
public class SAXParserFactoryUtil {

	/**
	 * Sax读取器工厂缓存
	 */
	private static volatile SAXParserFactory factory;

	/**
	 * 获取全局{@link SAXParserFactory}<br>
	 * <ul>
	 *     <li>默认不验证</li>
	 *     <li>默认打开命名空间支持</li>
	 * </ul>
	 *
	 * @return {@link SAXParserFactory}
	 */
	public static SAXParserFactory getFactory() {
		if (null == factory) {
			synchronized (SAXParserFactoryUtil.class) {
				if (null == factory) {
					factory = createFactory(false, true);
				}
			}
		}

		return factory;
	}

	/**
	 * 创建{@link SAXParserFactory}
	 *
	 * @param validating     是否验证
	 * @param namespaceAware 是否打开命名空间支持
	 * @return {@link SAXParserFactory}
	 */
	public static SAXParserFactory createFactory(final boolean validating, final boolean namespaceAware) {
		final SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(validating);
		factory.setNamespaceAware(namespaceAware);

		return XXEUtil.disableXXE(factory);
	}
}
