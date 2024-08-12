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
	 * 默认使用"com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl"<br>
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
