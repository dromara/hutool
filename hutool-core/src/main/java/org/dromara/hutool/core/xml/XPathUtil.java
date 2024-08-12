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
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * {@link XPath}相关工具类
 *
 * @author looly
 * @since 6.0.0
 */
public class XPathUtil {

	/**
	 * 创建XPath<br>
	 * Xpath相关文章：<a href="https://www.ibm.com/developerworks/cn/xml/x-javaxpathapi.html">https://www.ibm.com/developerworks/cn/xml/x-javaxpathapi.html</a>
	 *
	 * @return {@link XPath}
	 * @since 3.2.0
	 */
	public static XPath createXPath() {
		return XPathFactory.newInstance().newXPath();
	}

	/**
	 * 通过XPath方式读取XML节点等信息<br>
	 * Xpath相关文章：<a href="https://www.ibm.com/developerworks/cn/xml/x-javaxpathapi.html">https://www.ibm.com/developerworks/cn/xml/x-javaxpathapi.html</a>
	 *
	 * @param expression XPath表达式
	 * @param source     资源，可以是Docunent、Node节点等
	 * @return 匹配返回类型的值
	 * @since 4.0.9
	 */
	public static Element getElementByXPath(final String expression, final Object source) {
		return (Element) getNodeByXPath(expression, source);
	}

	/**
	 * 通过XPath方式读取XML的NodeList<br>
	 * Xpath相关文章：<a href="https://www.ibm.com/developerworks/cn/xml/x-javaxpathapi.html">https://www.ibm.com/developerworks/cn/xml/x-javaxpathapi.html</a>
	 *
	 * @param expression XPath表达式
	 * @param source     资源，可以是Docunent、Node节点等
	 * @return NodeList
	 * @since 4.0.9
	 */
	public static NodeList getNodeListByXPath(final String expression, final Object source) {
		return (NodeList) getByXPath(expression, source, XPathConstants.NODESET);
	}

	/**
	 * 通过XPath方式读取XML节点等信息<br>
	 * Xpath相关文章：<a href="https://www.ibm.com/developerworks/cn/xml/x-javaxpathapi.html">https://www.ibm.com/developerworks/cn/xml/x-javaxpathapi.html</a>
	 *
	 * @param expression XPath表达式
	 * @param source     资源，可以是Docunent、Node节点等
	 * @return 匹配返回类型的值
	 * @since 4.0.9
	 */
	public static Node getNodeByXPath(final String expression, final Object source) {
		return (Node) getByXPath(expression, source, XPathConstants.NODE);
	}

	/**
	 * 通过XPath方式读取XML节点等信息<br>
	 * Xpath相关文章：<a href="https://www.ibm.com/developerworks/cn/xml/x-javaxpathapi.html">https://www.ibm.com/developerworks/cn/xml/x-javaxpathapi.html</a>
	 *
	 * @param expression XPath表达式
	 * @param source     资源，可以是Docunent、Node节点等
	 * @param returnType 返回类型，{@link javax.xml.xpath.XPathConstants}
	 * @return 匹配返回类型的值
	 * @since 3.2.0
	 */
	public static Object getByXPath(final String expression, final Object source, final QName returnType) {
		NamespaceContext nsContext = null;
		if (source instanceof Node) {
			nsContext = new UniversalNamespaceCache((Node) source, false);
		}
		return getByXPath(expression, source, returnType, nsContext);
	}

	/**
	 * 通过XPath方式读取XML节点等信息<br>
	 * Xpath相关文章：<br>
	 * <a href="https://www.ibm.com/developerworks/cn/xml/x-javaxpathapi.html">https://www.ibm.com/developerworks/cn/xml/x-javaxpathapi.html</a><br>
	 * <a href="https://www.ibm.com/developerworks/cn/xml/x-nmspccontext/">https://www.ibm.com/developerworks/cn/xml/x-nmspccontext/</a>
	 *
	 * @param expression XPath表达式
	 * @param source     资源，可以是Docunent、Node节点等
	 * @param returnType 返回类型，{@link javax.xml.xpath.XPathConstants}
	 * @param nsContext  {@link NamespaceContext}
	 * @return 匹配返回类型的值
	 * @since 5.3.1
	 */
	public static Object getByXPath(final String expression, final Object source, final QName returnType, final NamespaceContext nsContext) {
		final XPath xPath = createXPath();
		if (null != nsContext) {
			xPath.setNamespaceContext(nsContext);
		}
		try {
			if (source instanceof InputSource) {
				return xPath.evaluate(expression, (InputSource) source, returnType);
			} else {
				return xPath.evaluate(expression, source, returnType);
			}
		} catch (final XPathExpressionException e) {
			throw new HutoolException(e);
		}
	}
}
