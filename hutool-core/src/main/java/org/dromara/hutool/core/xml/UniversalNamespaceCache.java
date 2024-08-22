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

import org.dromara.hutool.core.map.BiMap;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 全局命名空间上下文<br>
 * 见：https://www.ibm.com/developerworks/cn/xml/x-nmspccontext/
 *
 * @author looly
 */
public class UniversalNamespaceCache implements NamespaceContext {
	private static final String DEFAULT_NS = "DEFAULT";
	private final BiMap<String, String> prefixUri = new BiMap<>(new HashMap<>());

	/**
	 * This constructor parses the document and stores all namespaces it can
	 * find. If toplevelOnly is true, only namespaces in the root are used.
	 *
	 * @param node         source Node
	 * @param toplevelOnly restriction of the search to enhance performance
	 */
	public UniversalNamespaceCache(final Node node, final boolean toplevelOnly) {
		examineNode(node.getFirstChild(), toplevelOnly);
	}

	/**
	 * A single node is read, the namespace attributes are extracted and stored.
	 *
	 * @param node            to examine
	 * @param attributesOnly, if true no recursion happens
	 */
	private void examineNode(final Node node, final boolean attributesOnly) {
		final NamedNodeMap attributes = node.getAttributes();
		if (null != attributes) {
			final int length = attributes.getLength();
			for (int i = 0; i < length; i++) {
				final Node attribute = attributes.item(i);
				storeAttribute(attribute);
			}
		}

		if (!attributesOnly) {
			final NodeList childNodes = node.getChildNodes();
			//noinspection ConstantConditions
			if (null != childNodes) {
				Node item;
				final int childLength = childNodes.getLength();
				for (int i = 0; i < childLength; i++) {
					item = childNodes.item(i);
					if (item.getNodeType() == Node.ELEMENT_NODE)
						examineNode(item, false);
				}
			}
		}
	}

	/**
	 * This method looks at an attribute and stores it, if it is a namespace
	 * attribute.
	 *
	 * @param attribute to examine
	 */
	private void storeAttribute(final Node attribute) {
		if (null == attribute) {
			return;
		}
		// examine the attributes in namespace xmlns
		if (XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(attribute.getNamespaceURI())) {
			// Default namespace xmlns="uri goes here"
			if (XMLConstants.XMLNS_ATTRIBUTE.equals(attribute.getNodeName())) {
				prefixUri.put(DEFAULT_NS, attribute.getNodeValue());
			} else {
				// The defined prefixes are stored here
				prefixUri.put(attribute.getLocalName(), attribute.getNodeValue());
			}
		}

	}

	/**
	 * This method is called by XPath. It returns the default namespace, if the
	 * prefix is null or "".
	 *
	 * @param prefix to search for
	 * @return uri
	 */
	@Override
	public String getNamespaceURI(final String prefix) {
		if (prefix == null || XMLConstants.DEFAULT_NS_PREFIX.equals(prefix)) {
			return prefixUri.get(DEFAULT_NS);
		} else {
			return prefixUri.get(prefix);
		}
	}

	/**
	 * This method is not needed in this context, but can be implemented in a
	 * similar way.
	 */
	@Override
	public String getPrefix(final String namespaceURI) {
		return prefixUri.getInverse().get(namespaceURI);
	}

	@Override
	public Iterator<String> getPrefixes(final String namespaceURI) {
		// Not implemented
		return null;
	}

}
