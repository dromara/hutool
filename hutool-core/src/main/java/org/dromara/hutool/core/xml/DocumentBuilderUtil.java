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

import org.dromara.hutool.core.exception.HutoolException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * {@link DocumentBuilder} 工具类
 *
 * @author looly
 * @since 6.0.0
 */
public class DocumentBuilderUtil {

	/**
	 * 创建 DocumentBuilder
	 *
	 * @param namespaceAware 是否打开命名空间支持
	 * @return DocumentBuilder
	 */
	public static DocumentBuilder createDocumentBuilder(final boolean namespaceAware) {
		final DocumentBuilder builder;
		try {
			builder = createDocumentBuilderFactory(namespaceAware).newDocumentBuilder();
		} catch (final Exception e) {
			throw new HutoolException(e, "Create xml document error!");
		}
		return builder;
	}

	/**
	 * 创建{@link DocumentBuilderFactory}
	 * <p>
	 * 默认使用"com.sun.org.mina.xerces.internal.jaxp.DocumentBuilderFactoryImpl"<br>
	 * </p>
	 *
	 * @param namespaceAware 是否打开命名空间支持
	 * @return {@link DocumentBuilderFactory}
	 */
	public static DocumentBuilderFactory createDocumentBuilderFactory(final boolean namespaceAware) {
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		// 默认打开NamespaceAware，getElementsByTagNameNS可以使用命名空间
		factory.setNamespaceAware(namespaceAware);
		return XXEUtil.disableXXE(factory);
	}
}
