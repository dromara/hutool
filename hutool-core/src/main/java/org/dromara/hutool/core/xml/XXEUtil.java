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
import org.dromara.hutool.core.text.StrUtil;
import org.xml.sax.XMLReader;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

/**
 * XXE漏洞修复相关工具类<br>
 * 参考：https://blog.spoock.com/2018/10/23/java-xxe/
 *
 * @author looly
 * @since 6.0.0
 */
public class XXEUtil {

	/**
	 * 关闭XXE，避免漏洞攻击<br>
	 * see: <a href="https://www.owasp.org/index.php/XML_External_Entity_">https://www.owasp.org/index.php/XML_External_Entity_</a>(XXE)_Prevention_Cheat_Sheet#JAXP_DocumentBuilderFactory.2C_SAXParserFactory_and_DOM4J
	 *
	 * @param factory DocumentBuilderFactory
	 * @return DocumentBuilderFactory
	 */
	public static DocumentBuilderFactory disableXXE(final DocumentBuilderFactory factory) {
		try {
			// This is the PRIMARY defense. If DTDs (doctypes) are disallowed, almost all XML entity attacks are prevented
			// Xerces 2 only - http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl
			factory.setFeature(XmlFeatures.DISALLOW_DOCTYPE_DECL, true);
			// If you can't completely disable DTDs, then at least do the following:
			// Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-general-entities
			// Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-general-entities
			// JDK7+ - http://xml.org/sax/features/external-general-entities
			factory.setFeature(XmlFeatures.EXTERNAL_GENERAL_ENTITIES, false);
			// Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-parameter-entities
			// Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-parameter-entities
			// JDK7+ - http://xml.org/sax/features/external-parameter-entities
			factory.setFeature(XmlFeatures.EXTERNAL_PARAMETER_ENTITIES, false);
			// Disable external DTDs as well
			factory.setFeature(XmlFeatures.LOAD_EXTERNAL_DTD, false);
		} catch (final ParserConfigurationException e) {
			// ignore
		}

		// and these as well, per Timothy Morgan's 2014 paper: "XML Schema, DTD, and Entity Attacks"
		factory.setXIncludeAware(false);
		factory.setExpandEntityReferences(false);

		return factory;
	}

	/**
	 * 关闭XEE避免漏洞攻击
	 *
	 * @param factory {@link SAXParserFactory}
	 * @return {@link SAXParserFactory}
	 */
	public static SAXParserFactory disableXXE(final SAXParserFactory factory) {
		try {
			factory.setFeature(XmlFeatures.DISALLOW_DOCTYPE_DECL, true);
			factory.setFeature(XmlFeatures.EXTERNAL_GENERAL_ENTITIES, false);
			factory.setFeature(XmlFeatures.EXTERNAL_PARAMETER_ENTITIES, false);
			factory.setFeature(XmlFeatures.LOAD_EXTERNAL_DTD, false);
		} catch (final Exception ignore) {
			// ignore
		}

		factory.setXIncludeAware(false);

		return factory;
	}

	/**
	 * 关闭XEE避免漏洞攻击
	 *
	 * @param reader {@link XMLReader}
	 * @return {@link XMLReader}
	 */
	public static XMLReader disableXXE(final XMLReader reader) {
		try {
			reader.setFeature(XmlFeatures.DISALLOW_DOCTYPE_DECL, true);
			reader.setFeature(XmlFeatures.EXTERNAL_GENERAL_ENTITIES, false);
			reader.setFeature(XmlFeatures.EXTERNAL_PARAMETER_ENTITIES, false);
			reader.setFeature(XmlFeatures.LOAD_EXTERNAL_DTD, false);
		} catch (final Exception ignore) {
			// ignore
		}

		return reader;
	}

	/**
	 * 关闭XEE避免漏洞攻击
	 *
	 * @param factory {@link TransformerFactory }
	 * @return {@link TransformerFactory }
	 */
	public static TransformerFactory disableXXE(final TransformerFactory factory) {
		try {
			factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		} catch (final TransformerConfigurationException e) {
			throw new HutoolException(e);
		}
		factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, StrUtil.EMPTY);
		factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, StrUtil.EMPTY);
		return factory;
	}

	/**
	 * 关闭XEE避免漏洞攻击
	 *
	 * @param validator {@link Validator }
	 * @return {@link Validator }
	 */
	public static Validator disableXXE(final Validator validator) {
		try {
			validator.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, StrUtil.EMPTY);
			validator.setProperty(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, StrUtil.EMPTY);
		} catch (final Exception ignore) {
			// ignore
		}
		return validator;
	}

	/**
	 * 关闭XEE避免漏洞攻击
	 *
	 * @param factory {@link SAXTransformerFactory}
	 * @return {@link SAXTransformerFactory}
	 */
	public static SAXTransformerFactory disableXXE(final SAXTransformerFactory factory) {
		factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, StrUtil.EMPTY);
		factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, StrUtil.EMPTY);
		return factory;
	}

	/**
	 * 关闭XEE避免漏洞攻击
	 *
	 * @param factory {@link SchemaFactory}
	 * @return {@link SchemaFactory}
	 */
	public static SchemaFactory disableXXE(final SchemaFactory factory) {
		try {
			factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, StrUtil.EMPTY);
			factory.setProperty(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, StrUtil.EMPTY);
		} catch (final Exception ignore) {
			// ignore
		}
		return factory;
	}
}
