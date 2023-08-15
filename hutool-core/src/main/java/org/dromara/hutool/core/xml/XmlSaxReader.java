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
import org.dromara.hutool.core.io.IORuntimeException;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

/**
 * XML SAX方式读取器
 *
 * @author looly
 * @since 6.0.0
 */
public class XmlSaxReader {

	private final SAXParserFactory factory;
	private final InputSource source;

	/**
	 * 创建XmlSaxReader，使用全局{@link SAXParserFactory}
	 *
	 * @param source XML源，可以是文件、流、路径等
	 * @return XmlSaxReader
	 */
	public static XmlSaxReader of(final InputSource source) {
		return of(SAXParserFactoryUtil.getFactory(), source);
	}

	/**
	 * 创建XmlSaxReader
	 *
	 * @param factory {@link SAXParserFactory}
	 * @param source  XML源，可以是文件、流、路径等
	 * @return XmlSaxReader
	 */
	public static XmlSaxReader of(final SAXParserFactory factory, final InputSource source) {
		return new XmlSaxReader(factory, source);
	}

	/**
	 * 构造
	 *
	 * @param factory {@link SAXParserFactory}
	 * @param source  XML源，可以是文件、流、路径等
	 */
	public XmlSaxReader(final SAXParserFactory factory, final InputSource source) {
		this.factory = factory;
		this.source = source;
	}

	/**
	 * 读取内容
	 *
	 * @param contentHandler XML流处理器，用于按照Element处理xml
	 */
	public void read(final ContentHandler contentHandler) {
		final SAXParser parse;
		final XMLReader reader;
		try {
			parse = factory.newSAXParser();
			if (contentHandler instanceof DefaultHandler) {
				parse.parse(source, (DefaultHandler) contentHandler);
				return;
			}

			// 得到解读器
			reader = XXEUtil.disableXXE(parse.getXMLReader());
			reader.setContentHandler(contentHandler);
			reader.parse(source);
		} catch (final ParserConfigurationException | SAXException e) {
			throw new HutoolException(e);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}
}
