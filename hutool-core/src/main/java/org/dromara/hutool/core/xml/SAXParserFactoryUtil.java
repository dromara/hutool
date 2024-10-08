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

package org.dromara.hutool.core.xml;

import org.dromara.hutool.core.lang.loader.LazyFunLoader;
import org.dromara.hutool.core.lang.loader.Loader;

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
	private static final Loader<SAXParserFactory> factoryLoader = LazyFunLoader.of(()->createFactory(false, true));

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
		return factoryLoader.get();
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
