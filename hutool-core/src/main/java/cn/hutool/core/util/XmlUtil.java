package cn.hutool.core.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.BiMap;
import cn.hutool.core.map.MapUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * XML工具类<br>
 * 此工具使用w3c dom工具，不需要依赖第三方包。<br>
 * 工具类封装了XML文档的创建、读取、写出和部分XML操作
 *
 * @author xiaoleilu
 */
public class XmlUtil {

	/**
	 * 在XML中无效的字符 正则
	 */
	public static final String INVALID_REGEX = "[\\x00-\\x08\\x0b-\\x0c\\x0e-\\x1f]";
	/**
	 * 在XML中注释的内容 正则
	 */
	public static final String COMMENT_REGEX = "(?s)<!--.+?-->";
	/**
	 * XML格式化输出默认缩进量
	 */
	public static final int INDENT_DEFAULT = 2;

	/**
	 * 默认的DocumentBuilderFactory实现
	 */
	private static String defaultDocumentBuilderFactory = "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl";

	/**
	 * 是否打开命名空间支持
	 */
	private static boolean namespaceAware = true;
	/**
	 * Sax读取器工厂缓存
	 */
	private static SAXParserFactory factory;

	/**
	 * 禁用默认的DocumentBuilderFactory，禁用后如果有第三方的实现（如oracle的xdb包中的xmlparse），将会自动加载实现。
	 */
	synchronized public static void disableDefaultDocumentBuilderFactory() {
		defaultDocumentBuilderFactory = null;
	}

	/**
	 * 设置是否打开命名空间支持，默认打开
	 *
	 * @param isNamespaceAware 是否命名空间支持
	 * @since 5.3.1
	 */
	synchronized public static void setNamespaceAware(boolean isNamespaceAware) {
		namespaceAware = isNamespaceAware;
	}

	// -------------------------------------------------------------------------------------- Read

	/**
	 * 读取解析XML文件
	 *
	 * @param file XML文件
	 * @return XML文档对象
	 */
	public static Document readXML(File file) {
		Assert.notNull(file, "Xml file is null !");
		if (false == file.exists()) {
			throw new UtilException("File [{}] not a exist!", file.getAbsolutePath());
		}
		if (false == file.isFile()) {
			throw new UtilException("[{}] not a file!", file.getAbsolutePath());
		}

		try {
			file = file.getCanonicalFile();
		} catch (IOException e) {
			// ignore
		}

		BufferedInputStream in = null;
		try {
			in = FileUtil.getInputStream(file);
			return readXML(in);
		} finally {
			IoUtil.close(in);
		}
	}

	/**
	 * 读取解析XML文件<br>
	 * 如果给定内容以“&lt;”开头，表示这是一个XML内容，直接读取，否则按照路径处理<br>
	 * 路径可以为相对路径，也可以是绝对路径，相对路径相对于ClassPath
	 *
	 * @param pathOrContent 内容或路径
	 * @return XML文档对象
	 * @since 3.0.9
	 */
	public static Document readXML(String pathOrContent) {
		if (StrUtil.startWith(pathOrContent, '<')) {
			return parseXml(pathOrContent);
		}
		return readXML(FileUtil.file(pathOrContent));
	}

	/**
	 * 读取解析XML文件<br>
	 * 编码在XML中定义
	 *
	 * @param inputStream XML流
	 * @return XML文档对象
	 * @throws UtilException IO异常或转换异常
	 * @since 3.0.9
	 */
	public static Document readXML(InputStream inputStream) throws UtilException {
		return readXML(new InputSource(inputStream));
	}

	/**
	 * 读取解析XML文件
	 *
	 * @param reader XML流
	 * @return XML文档对象
	 * @throws UtilException IO异常或转换异常
	 * @since 3.0.9
	 */
	public static Document readXML(Reader reader) throws UtilException {
		return readXML(new InputSource(reader));
	}

	/**
	 * 读取解析XML文件<br>
	 * 编码在XML中定义
	 *
	 * @param source {@link InputSource}
	 * @return XML文档对象
	 * @since 3.0.9
	 */
	public static Document readXML(InputSource source) {
		final DocumentBuilder builder = createDocumentBuilder();
		try {
			return builder.parse(source);
		} catch (Exception e) {
			throw new UtilException(e, "Parse XML from stream error!");
		}
	}

	/**
	 * 使用Sax方式读取指定的XML<br>
	 * 如果用户传入的contentHandler为{@link DefaultHandler}，则其接口都会被处理
	 *
	 * @param file         XML源文件,使用后自动关闭
	 * @param contentHandler XML流处理器，用于按照Element处理xml
	 * @since 5.4.4
	 */
	public static void readBySax(File file, ContentHandler contentHandler) {
		InputStream in = null;
		try{
			in = FileUtil.getInputStream(file);
			readBySax(new InputSource(in), contentHandler);
		} finally {
			IoUtil.close(in);
		}
	}

	/**
	 * 使用Sax方式读取指定的XML<br>
	 * 如果用户传入的contentHandler为{@link DefaultHandler}，则其接口都会被处理
	 *
	 * @param reader         XML源Reader,使用后自动关闭
	 * @param contentHandler XML流处理器，用于按照Element处理xml
	 * @since 5.4.4
	 */
	public static void readBySax(Reader reader, ContentHandler contentHandler) {
		try{
			readBySax(new InputSource(reader), contentHandler);
		} finally {
			IoUtil.close(reader);
		}
	}

	/**
	 * 使用Sax方式读取指定的XML<br>
	 * 如果用户传入的contentHandler为{@link DefaultHandler}，则其接口都会被处理
	 *
	 * @param source         XML源流,使用后自动关闭
	 * @param contentHandler XML流处理器，用于按照Element处理xml
	 * @since 5.4.4
	 */
	public static void readBySax(InputStream source, ContentHandler contentHandler) {
		try{
			readBySax(new InputSource(source), contentHandler);
		} finally {
			IoUtil.close(source);
		}
	}

	/**
	 * 使用Sax方式读取指定的XML<br>
	 * 如果用户传入的contentHandler为{@link DefaultHandler}，则其接口都会被处理
	 *
	 * @param source         XML源，可以是文件、流、路径等
	 * @param contentHandler XML流处理器，用于按照Element处理xml
	 * @since 5.4.4
	 */
	public static void readBySax(InputSource source, ContentHandler contentHandler) {
		// 1.获取解析工厂
		if (null == factory) {
			factory = SAXParserFactory.newInstance();
			factory.setValidating(false);
			factory.setNamespaceAware(namespaceAware);
		}
		// 2.从解析工厂获取解析器
		final SAXParser parse;
		XMLReader reader;
		try {
			parse = factory.newSAXParser();
			if (contentHandler instanceof DefaultHandler) {
				parse.parse(source, (DefaultHandler) contentHandler);
				return;
			}

			// 3.得到解读器
			reader = parse.getXMLReader();
			reader.setContentHandler(contentHandler);
			reader.parse(source);
		} catch (ParserConfigurationException | SAXException e) {
			throw new UtilException(e);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 将String类型的XML转换为XML文档
	 *
	 * @param xmlStr XML字符串
	 * @return XML文档
	 */
	public static Document parseXml(String xmlStr) {
		if (StrUtil.isBlank(xmlStr)) {
			throw new IllegalArgumentException("XML content string is empty !");
		}
		xmlStr = cleanInvalid(xmlStr);
		return readXML(StrUtil.getReader(xmlStr));
	}

	/**
	 * 从XML中读取对象 Reads serialized object from the XML file.
	 *
	 * @param <T>    对象类型
	 * @param source XML文件
	 * @return 对象
	 */
	public static <T> T readObjectFromXml(File source) {
		return readObjectFromXml(new InputSource(FileUtil.getInputStream(source)));
	}

	/**
	 * 从XML中读取对象 Reads serialized object from the XML file.
	 *
	 * @param <T>    对象类型
	 * @param xmlStr XML内容
	 * @return 对象
	 * @since 3.2.0
	 */
	public static <T> T readObjectFromXml(String xmlStr) {
		return readObjectFromXml(new InputSource(StrUtil.getReader(xmlStr)));
	}

	/**
	 * 从XML中读取对象 Reads serialized object from the XML file.
	 *
	 * @param <T>    对象类型
	 * @param source {@link InputSource}
	 * @return 对象
	 * @since 3.2.0
	 */
	@SuppressWarnings("unchecked")
	public static <T> T readObjectFromXml(InputSource source) {
		Object result;
		XMLDecoder xmldec = null;
		try {
			xmldec = new XMLDecoder(source);
			result = xmldec.readObject();
		} finally {
			IoUtil.close(xmldec);
		}
		return (T) result;
	}

	// -------------------------------------------------------------------------------------- Write

	/**
	 * 将XML文档转换为String<br>
	 * 字符编码使用XML文档中的编码，获取不到则使用UTF-8<br>
	 * 默认非格式化输出，若想格式化请使用{@link #format(Document)}
	 *
	 * @param doc XML文档
	 * @return XML字符串
	 * @since 5.4.5
	 */
	public static String toStr(Node doc) {
		return toStr(doc, false);
	}

	/**
	 * 将XML文档转换为String<br>
	 * 字符编码使用XML文档中的编码，获取不到则使用UTF-8<br>
	 * 默认非格式化输出，若想格式化请使用{@link #format(Document)}
	 *
	 * @param doc XML文档
	 * @return XML字符串
	 */
	public static String toStr(Document doc) {
		return toStr((Node)doc);
	}

	/**
	 * 将XML文档转换为String<br>
	 * 字符编码使用XML文档中的编码，获取不到则使用UTF-8
	 *
	 * @param doc      XML文档
	 * @param isPretty 是否格式化输出
	 * @return XML字符串
	 * @since 5.4.5
	 */
	public static String toStr(Node doc, boolean isPretty) {
		return toStr(doc, CharsetUtil.UTF_8, isPretty);
	}

	/**
	 * 将XML文档转换为String<br>
	 * 字符编码使用XML文档中的编码，获取不到则使用UTF-8
	 *
	 * @param doc      XML文档
	 * @param isPretty 是否格式化输出
	 * @return XML字符串
	 * @since 3.0.9
	 */
	public static String toStr(Document doc, boolean isPretty) {
		return toStr((Node)doc, isPretty);
	}

	/**
	 * 将XML文档转换为String<br>
	 * 字符编码使用XML文档中的编码，获取不到则使用UTF-8
	 *
	 * @param doc      XML文档
	 * @param charset  编码
	 * @param isPretty 是否格式化输出
	 * @return XML字符串
	 * @since 5.4.5
	 */
	public static String toStr(Node doc, String charset, boolean isPretty) {
		return toStr(doc, charset, isPretty, false);
	}

	/**
	 * 将XML文档转换为String<br>
	 * 字符编码使用XML文档中的编码，获取不到则使用UTF-8
	 *
	 * @param doc      XML文档
	 * @param charset  编码
	 * @param isPretty 是否格式化输出
	 * @return XML字符串
	 * @since 3.0.9
	 */
	public static String toStr(Document doc, String charset, boolean isPretty) {
		return toStr((Node)doc, charset, isPretty);
	}

	/**
	 * 将XML文档转换为String<br>
	 * 字符编码使用XML文档中的编码，获取不到则使用UTF-8
	 *
	 * @param doc                XML文档
	 * @param charset            编码
	 * @param isPretty           是否格式化输出
	 * @param omitXmlDeclaration 是否输出 xml Declaration
	 * @return XML字符串
	 * @since 5.1.2
	 */
	public static String toStr(Node doc, String charset, boolean isPretty, boolean omitXmlDeclaration) {
		final StringWriter writer = StrUtil.getWriter();
		try {
			write(doc, writer, charset, isPretty ? INDENT_DEFAULT : 0, omitXmlDeclaration);
		} catch (Exception e) {
			throw new UtilException(e, "Trans xml document to string error!");
		}
		return writer.toString();
	}

	/**
	 * 格式化XML输出
	 *
	 * @param doc {@link Document} XML文档
	 * @return 格式化后的XML字符串
	 * @since 4.4.5
	 */
	public static String format(Document doc) {
		return toStr(doc, true);
	}

	/**
	 * 格式化XML输出
	 *
	 * @param xmlStr XML字符串
	 * @return 格式化后的XML字符串
	 * @since 4.4.5
	 */
	public static String format(String xmlStr) {
		return format(parseXml(xmlStr));
	}

	/**
	 * 将XML文档写入到文件<br>
	 * 使用Document中的编码
	 *
	 * @param doc          XML文档
	 * @param absolutePath 文件绝对路径，不存在会自动创建
	 */
	public static void toFile(Document doc, String absolutePath) {
		toFile(doc, absolutePath, null);
	}

	/**
	 * 将XML文档写入到文件<br>
	 *
	 * @param doc     XML文档
	 * @param path    文件路径绝对路径或相对ClassPath路径，不存在会自动创建
	 * @param charset 自定义XML文件的编码，如果为{@code null} 读取XML文档中的编码，否则默认UTF-8
	 */
	public static void toFile(Document doc, String path, String charset) {
		if (StrUtil.isBlank(charset)) {
			charset = doc.getXmlEncoding();
		}
		if (StrUtil.isBlank(charset)) {
			charset = CharsetUtil.UTF_8;
		}

		BufferedWriter writer = null;
		try {
			writer = FileUtil.getWriter(path, charset, false);
			write(doc, writer, charset, INDENT_DEFAULT);
		} finally {
			IoUtil.close(writer);
		}
	}

	/**
	 * 将XML文档写出
	 *
	 * @param node    {@link Node} XML文档节点或文档本身
	 * @param writer  写出的Writer，Writer决定了输出XML的编码
	 * @param charset 编码
	 * @param indent  格式化输出中缩进量，小于1表示不格式化输出
	 * @since 3.0.9
	 */
	public static void write(Node node, Writer writer, String charset, int indent) {
		transform(new DOMSource(node), new StreamResult(writer), charset, indent);
	}

	/**
	 * 将XML文档写出
	 *
	 * @param node               {@link Node} XML文档节点或文档本身
	 * @param writer             写出的Writer，Writer决定了输出XML的编码
	 * @param charset            编码
	 * @param indent             格式化输出中缩进量，小于1表示不格式化输出
	 * @param omitXmlDeclaration 是否输出 xml Declaration
	 * @since 5.1.2
	 */
	public static void write(Node node, Writer writer, String charset, int indent, boolean omitXmlDeclaration) {
		transform(new DOMSource(node), new StreamResult(writer), charset, indent, omitXmlDeclaration);
	}

	/**
	 * 将XML文档写出
	 *
	 * @param node    {@link Node} XML文档节点或文档本身
	 * @param out     写出的Writer，Writer决定了输出XML的编码
	 * @param charset 编码
	 * @param indent  格式化输出中缩进量，小于1表示不格式化输出
	 * @since 4.0.8
	 */
	public static void write(Node node, OutputStream out, String charset, int indent) {
		transform(new DOMSource(node), new StreamResult(out), charset, indent);
	}

	/**
	 * 将XML文档写出
	 *
	 * @param node               {@link Node} XML文档节点或文档本身
	 * @param out                写出的Writer，Writer决定了输出XML的编码
	 * @param charset            编码
	 * @param indent             格式化输出中缩进量，小于1表示不格式化输出
	 * @param omitXmlDeclaration 是否输出 xml Declaration
	 * @since 5.1.2
	 */
	public static void write(Node node, OutputStream out, String charset, int indent, boolean omitXmlDeclaration) {
		transform(new DOMSource(node), new StreamResult(out), charset, indent, omitXmlDeclaration);
	}

	/**
	 * 将XML文档写出<br>
	 * 格式化输出逻辑参考：https://stackoverflow.com/questions/139076/how-to-pretty-print-xml-from-java
	 *
	 * @param source  源
	 * @param result  目标
	 * @param charset 编码
	 * @param indent  格式化输出中缩进量，小于1表示不格式化输出
	 * @since 4.0.9
	 */
	public static void transform(Source source, Result result, String charset, int indent) {
		transform(source, result, charset, indent, false);
	}

	/**
	 * 将XML文档写出<br>
	 * 格式化输出逻辑参考：https://stackoverflow.com/questions/139076/how-to-pretty-print-xml-from-java
	 *
	 * @param source             源
	 * @param result             目标
	 * @param charset            编码
	 * @param indent             格式化输出中缩进量，小于1表示不格式化输出
	 * @param omitXmlDeclaration 是否输出 xml Declaration
	 * @since 5.1.2
	 */
	public static void transform(Source source, Result result, String charset, int indent, boolean omitXmlDeclaration) {
		final TransformerFactory factory = TransformerFactory.newInstance();
		try {
			final Transformer xformer = factory.newTransformer();
			if (indent > 0) {
				xformer.setOutputProperty(OutputKeys.INDENT, "yes");
				xformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(indent));
			}
			if (StrUtil.isNotBlank(charset)) {
				xformer.setOutputProperty(OutputKeys.ENCODING, charset);
			}
			if (omitXmlDeclaration) {
				xformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			}
			xformer.transform(source, result);
		} catch (Exception e) {
			throw new UtilException(e, "Trans xml document to string error!");
		}
	}

	// -------------------------------------------------------------------------------------- Create

	/**
	 * 创建XML文档<br>
	 * 创建的XML默认是utf8编码，修改编码的过程是在toStr和toFile方法里，即XML在转为文本的时候才定义编码
	 *
	 * @return XML文档
	 * @since 4.0.8
	 */
	public static Document createXml() {
		return createDocumentBuilder().newDocument();
	}

	/**
	 * 创建 DocumentBuilder
	 *
	 * @return DocumentBuilder
	 * @since 4.1.2
	 */
	public static DocumentBuilder createDocumentBuilder() {
		DocumentBuilder builder;
		try {
			builder = createDocumentBuilderFactory().newDocumentBuilder();
		} catch (Exception e) {
			throw new UtilException(e, "Create xml document error!");
		}
		return builder;
	}

	/**
	 * 创建{@link DocumentBuilderFactory}
	 * <p>
	 * 默认使用"com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl"<br>
	 * 如果使用第三方实现，请调用{@link #disableDefaultDocumentBuilderFactory()}
	 * </p>
	 *
	 * @return {@link DocumentBuilderFactory}
	 */
	public static DocumentBuilderFactory createDocumentBuilderFactory() {
		final DocumentBuilderFactory factory;
		if (StrUtil.isNotEmpty(defaultDocumentBuilderFactory)) {
			factory = DocumentBuilderFactory.newInstance(defaultDocumentBuilderFactory, null);
		} else {
			factory = DocumentBuilderFactory.newInstance();
		}
		// 默认打开NamespaceAware，getElementsByTagNameNS可以使用命名空间
		factory.setNamespaceAware(namespaceAware);
		return disableXXE(factory);
	}

	/**
	 * 创建XML文档<br>
	 * 创建的XML默认是utf8编码，修改编码的过程是在toStr和toFile方法里，即XML在转为文本的时候才定义编码
	 *
	 * @param rootElementName 根节点名称
	 * @return XML文档
	 */
	public static Document createXml(String rootElementName) {
		return createXml(rootElementName, null);
	}

	/**
	 * 创建XML文档<br>
	 * 创建的XML默认是utf8编码，修改编码的过程是在toStr和toFile方法里，即XML在转为文本的时候才定义编码
	 *
	 * @param rootElementName 根节点名称
	 * @param namespace       命名空间，无则传null
	 * @return XML文档
	 * @since 5.0.4
	 */
	public static Document createXml(String rootElementName, String namespace) {
		final Document doc = createXml();
		doc.appendChild(null == namespace ? doc.createElement(rootElementName) : doc.createElementNS(namespace, rootElementName));
		return doc;
	}

	// -------------------------------------------------------------------------------------- Function

	/**
	 * 获得XML文档根节点
	 *
	 * @param doc {@link Document}
	 * @return 根节点
	 * @see Document#getDocumentElement()
	 * @since 3.0.8
	 */
	public static Element getRootElement(Document doc) {
		return (null == doc) ? null : doc.getDocumentElement();
	}

	/**
	 * 获取节点所在的Document
	 *
	 * @param node 节点
	 * @return {@link Document}
	 * @since 5.3.0
	 */
	public static Document getOwnerDocument(Node node) {
		return (node instanceof Document) ? (Document) node : node.getOwnerDocument();
	}

	/**
	 * 去除XML文本中的无效字符
	 *
	 * @param xmlContent XML文本
	 * @return 当传入为null时返回null
	 */
	public static String cleanInvalid(String xmlContent) {
		if (xmlContent == null) {
			return null;
		}
		return xmlContent.replaceAll(INVALID_REGEX, "");
	}

	/**
	 * 去除XML文本中的注释内容
	 *
	 * @param xmlContent XML文本
	 * @return 当传入为null时返回null
	 * @since 5.4.5
	 */
	public static String cleanComment(String xmlContent) {
		if (xmlContent == null) {
			return null;
		}
		return xmlContent.replaceAll(COMMENT_REGEX, StrUtil.EMPTY);
	}

	/**
	 * 根据节点名获得子节点列表
	 *
	 * @param element 节点
	 * @param tagName 节点名，如果节点名为空（null或blank），返回所有子节点
	 * @return 节点列表
	 */
	public static List<Element> getElements(Element element, String tagName) {
		final NodeList nodeList = StrUtil.isBlank(tagName) ? element.getChildNodes() : element.getElementsByTagName(tagName);
		return transElements(element, nodeList);
	}

	/**
	 * 根据节点名获得第一个子节点
	 *
	 * @param element 节点
	 * @param tagName 节点名
	 * @return 节点
	 */
	public static Element getElement(Element element, String tagName) {
		final NodeList nodeList = element.getElementsByTagName(tagName);
		if (nodeList == null || nodeList.getLength() < 1) {
			return null;
		}
		int length = nodeList.getLength();
		for (int i = 0; i < length; i++) {
			Element childEle = (Element) nodeList.item(i);
			if (childEle == null || childEle.getParentNode() == element) {
				return childEle;
			}
		}
		return null;
	}

	/**
	 * 根据节点名获得第一个子节点
	 *
	 * @param element 节点
	 * @param tagName 节点名
	 * @return 节点中的值
	 */
	public static String elementText(Element element, String tagName) {
		Element child = getElement(element, tagName);
		return child == null ? null : child.getTextContent();
	}

	/**
	 * 根据节点名获得第一个子节点
	 *
	 * @param element      节点
	 * @param tagName      节点名
	 * @param defaultValue 默认值
	 * @return 节点中的值
	 */
	public static String elementText(Element element, String tagName, String defaultValue) {
		Element child = getElement(element, tagName);
		return child == null ? defaultValue : child.getTextContent();
	}

	/**
	 * 将NodeList转换为Element列表
	 *
	 * @param nodeList NodeList
	 * @return Element列表
	 */
	public static List<Element> transElements(NodeList nodeList) {
		return transElements(null, nodeList);
	}

	/**
	 * 将NodeList转换为Element列表<br>
	 * 非Element节点将被忽略
	 *
	 * @param parentEle 父节点，如果指定将返回此节点的所有直接子节点，null返回所有就节点
	 * @param nodeList  NodeList
	 * @return Element列表
	 */
	public static List<Element> transElements(Element parentEle, NodeList nodeList) {
		int length = nodeList.getLength();
		final ArrayList<Element> elements = new ArrayList<>(length);
		Node node;
		Element element;
		for (int i = 0; i < length; i++) {
			node = nodeList.item(i);
			if (Node.ELEMENT_NODE == node.getNodeType()) {
				element = (Element) nodeList.item(i);
				if (parentEle == null || element.getParentNode() == parentEle) {
					elements.add(element);
				}
			}
		}

		return elements;
	}

	/**
	 * 将可序列化的对象转换为XML写入文件，已经存在的文件将被覆盖<br>
	 * Writes serializable object to a XML file. Existing file will be overwritten
	 *
	 * @param dest 目标文件
	 * @param bean 对象
	 */
	public static void writeObjectAsXml(File dest, Object bean) {
		XMLEncoder xmlenc = null;
		try {
			xmlenc = new XMLEncoder(FileUtil.getOutputStream(dest));
			xmlenc.writeObject(bean);
		} finally {
			// 关闭XMLEncoder会相应关闭OutputStream
			IoUtil.close(xmlenc);
		}
	}

	/**
	 * 创建XPath<br>
	 * Xpath相关文章：https://www.ibm.com/developerworks/cn/xml/x-javaxpathapi.html
	 *
	 * @return {@link XPath}
	 * @since 3.2.0
	 */
	public static XPath createXPath() {
		return XPathFactory.newInstance().newXPath();
	}

	/**
	 * 通过XPath方式读取XML节点等信息<br>
	 * Xpath相关文章：https://www.ibm.com/developerworks/cn/xml/x-javaxpathapi.html
	 *
	 * @param expression XPath表达式
	 * @param source     资源，可以是Docunent、Node节点等
	 * @return 匹配返回类型的值
	 * @since 4.0.9
	 */
	public static Element getElementByXPath(String expression, Object source) {
		return (Element) getNodeByXPath(expression, source);
	}

	/**
	 * 通过XPath方式读取XML的NodeList<br>
	 * Xpath相关文章：https://www.ibm.com/developerworks/cn/xml/x-javaxpathapi.html
	 *
	 * @param expression XPath表达式
	 * @param source     资源，可以是Docunent、Node节点等
	 * @return NodeList
	 * @since 4.0.9
	 */
	public static NodeList getNodeListByXPath(String expression, Object source) {
		return (NodeList) getByXPath(expression, source, XPathConstants.NODESET);
	}

	/**
	 * 通过XPath方式读取XML节点等信息<br>
	 * Xpath相关文章：https://www.ibm.com/developerworks/cn/xml/x-javaxpathapi.html
	 *
	 * @param expression XPath表达式
	 * @param source     资源，可以是Docunent、Node节点等
	 * @return 匹配返回类型的值
	 * @since 4.0.9
	 */
	public static Node getNodeByXPath(String expression, Object source) {
		return (Node) getByXPath(expression, source, XPathConstants.NODE);
	}

	/**
	 * 通过XPath方式读取XML节点等信息<br>
	 * Xpath相关文章：https://www.ibm.com/developerworks/cn/xml/x-javaxpathapi.html
	 *
	 * @param expression XPath表达式
	 * @param source     资源，可以是Docunent、Node节点等
	 * @param returnType 返回类型，{@link javax.xml.xpath.XPathConstants}
	 * @return 匹配返回类型的值
	 * @since 3.2.0
	 */
	public static Object getByXPath(String expression, Object source, QName returnType) {
		NamespaceContext nsContext = null;
		if (source instanceof Node) {
			nsContext = new UniversalNamespaceCache((Node) source, false);
		}
		return getByXPath(expression, source, returnType, nsContext);
	}

	/**
	 * 通过XPath方式读取XML节点等信息<br>
	 * Xpath相关文章：<br>
	 * https://www.ibm.com/developerworks/cn/xml/x-javaxpathapi.html<br>
	 * https://www.ibm.com/developerworks/cn/xml/x-nmspccontext/
	 *
	 * @param expression XPath表达式
	 * @param source     资源，可以是Docunent、Node节点等
	 * @param returnType 返回类型，{@link javax.xml.xpath.XPathConstants}
	 * @param nsContext  {@link NamespaceContext}
	 * @return 匹配返回类型的值
	 * @since 5.3.1
	 */
	public static Object getByXPath(String expression, Object source, QName returnType, NamespaceContext nsContext) {
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
		} catch (XPathExpressionException e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 转义XML特殊字符:
	 *
	 * <pre>
	 * &amp; (ampersand) 替换为 &amp;amp;
	 * &lt; (小于) 替换为 &amp;lt;
	 * &gt; (大于) 替换为 &amp;gt;
	 * &quot; (双引号) 替换为 &amp;quot;
	 * </pre>
	 *
	 * @param string 被替换的字符串
	 * @return 替换后的字符串
	 * @since 4.0.8
	 */
	public static String escape(String string) {
		return EscapeUtil.escape(string);
	}

	/**
	 * 反转义XML特殊字符:
	 *
	 * @param string 被替换的字符串
	 * @return 替换后的字符串
	 * @see EscapeUtil#unescape(String)
	 * @since 5.0.6
	 */
	public static String unescape(String string) {
		return EscapeUtil.unescape(string);
	}

	/**
	 * XML格式字符串转换为Map
	 *
	 * @param xmlStr XML字符串
	 * @return XML数据转换后的Map
	 * @since 4.0.8
	 */
	public static Map<String, Object> xmlToMap(String xmlStr) {
		return xmlToMap(xmlStr, new HashMap<>());
	}

	/**
	 * XML转Java Bean
	 *
	 * @param <T>  bean类型
	 * @param node XML节点
	 * @param bean bean类
	 * @return bean
	 * @since 5.2.4
	 */
	public static <T> T xmlToBean(Node node, Class<T> bean) {
		final Map<String, Object> map = xmlToMap(node);
		if (null != map && map.size() == 1) {
			return BeanUtil.toBean(map.get(bean.getSimpleName()), bean);
		}
		return BeanUtil.toBean(map, bean);
	}

	/**
	 * XML格式字符串转换为Map
	 *
	 * @param node XML节点
	 * @return XML数据转换后的Map
	 * @since 4.0.8
	 */
	public static Map<String, Object> xmlToMap(Node node) {
		return xmlToMap(node, new HashMap<>());
	}

	/**
	 * XML格式字符串转换为Map<br>
	 * 只支持第一级别的XML，不支持多级XML
	 *
	 * @param xmlStr XML字符串
	 * @param result 结果Map类型
	 * @return XML数据转换后的Map
	 * @since 4.0.8
	 */
	public static Map<String, Object> xmlToMap(String xmlStr, Map<String, Object> result) {
		final Document doc = parseXml(xmlStr);
		final Element root = getRootElement(doc);
		root.normalize();

		return xmlToMap(root, result);
	}

	/**
	 * XML节点转换为Map
	 *
	 * @param node   XML节点
	 * @param result 结果Map类型
	 * @return XML数据转换后的Map
	 * @since 4.0.8
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> xmlToMap(Node node, Map<String, Object> result) {
		if (null == result) {
			result = new HashMap<>();
		}
		final NodeList nodeList = node.getChildNodes();
		final int length = nodeList.getLength();
		Node childNode;
		Element childEle;
		for (int i = 0; i < length; ++i) {
			childNode = nodeList.item(i);
			if (false == isElement(childNode)) {
				continue;
			}

			childEle = (Element) childNode;
			final Object value = result.get(childEle.getNodeName());
			Object newValue;
			if (childEle.hasChildNodes()) {
				// 子节点继续递归遍历
				final Map<String, Object> map = xmlToMap(childEle);
				if (MapUtil.isNotEmpty(map)) {
					newValue = map;
				} else {
					newValue = childEle.getTextContent();
				}
			} else {
				newValue = childEle.getTextContent();
			}


			if (null != newValue) {
				if (null != value) {
					if (value instanceof List) {
						((List<Object>) value).add(newValue);
					} else {
						result.put(childEle.getNodeName(), CollUtil.newArrayList(value, newValue));
					}
				} else {
					result.put(childEle.getNodeName(), newValue);
				}
			}
		}
		return result;
	}

	/**
	 * 将Map转换为XML格式的字符串
	 *
	 * @param data Map类型数据
	 * @return XML格式的字符串
	 * @since 5.1.2
	 */
	public static String mapToXmlStr(Map<?, ?> data) {
		return toStr(mapToXml(data, "xml"));
	}

	/**
	 * 将Map转换为XML格式的字符串
	 *
	 * @param data               Map类型数据
	 * @param omitXmlDeclaration 是否输出 xml Declaration
	 * @return XML格式的字符串
	 * @since 5.1.2
	 */
	public static String mapToXmlStr(Map<?, ?> data, boolean omitXmlDeclaration) {
		return toStr(mapToXml(data, "xml"), CharsetUtil.UTF_8, false, omitXmlDeclaration);
	}

	/**
	 * 将Map转换为XML格式的字符串
	 *
	 * @param data     Map类型数据
	 * @param rootName 根节点名
	 * @return XML格式的字符串
	 * @since 4.0.8
	 */
	public static String mapToXmlStr(Map<?, ?> data, String rootName) {
		return toStr(mapToXml(data, rootName));
	}

	/**
	 * 将Map转换为XML格式的字符串
	 *
	 * @param data      Map类型数据
	 * @param rootName  根节点名
	 * @param namespace 命名空间，可以为null
	 * @return XML格式的字符串
	 * @since 5.0.4
	 */
	public static String mapToXmlStr(Map<?, ?> data, String rootName, String namespace) {
		return toStr(mapToXml(data, rootName, namespace));
	}

	/**
	 * 将Map转换为XML格式的字符串
	 *
	 * @param data               Map类型数据
	 * @param rootName           根节点名
	 * @param namespace          命名空间，可以为null
	 * @param omitXmlDeclaration 是否输出 xml Declaration
	 * @return XML格式的字符串
	 * @since 5.1.2
	 */
	public static String mapToXmlStr(Map<?, ?> data, String rootName, String namespace, boolean omitXmlDeclaration) {
		return toStr(mapToXml(data, rootName, namespace), CharsetUtil.UTF_8, false, omitXmlDeclaration);
	}

	/**
	 * 将Map转换为XML格式的字符串
	 *
	 * @param data               Map类型数据
	 * @param rootName           根节点名
	 * @param namespace          命名空间，可以为null
	 * @param isPretty           是否格式化输出
	 * @param omitXmlDeclaration 是否输出 xml Declaration
	 * @return XML格式的字符串
	 * @since 5.1.2
	 */
	public static String mapToXmlStr(Map<?, ?> data, String rootName, String namespace, boolean isPretty, boolean omitXmlDeclaration) {
		return toStr(mapToXml(data, rootName, namespace), CharsetUtil.UTF_8, isPretty);
	}

	/**
	 * 将Map转换为XML格式的字符串
	 *
	 * @param data               Map类型数据
	 * @param rootName           根节点名
	 * @param namespace          命名空间，可以为null
	 * @param charset            编码
	 * @param isPretty           是否格式化输出
	 * @param omitXmlDeclaration 是否输出 xml Declaration
	 * @return XML格式的字符串
	 * @since 5.1.2
	 */
	public static String mapToXmlStr(Map<?, ?> data, String rootName, String namespace, String charset, boolean isPretty, boolean omitXmlDeclaration) {
		return toStr(mapToXml(data, rootName, namespace), charset, isPretty, omitXmlDeclaration);
	}

	/**
	 * 将Map转换为XML
	 *
	 * @param data     Map类型数据
	 * @param rootName 根节点名
	 * @return XML
	 * @since 4.0.9
	 */
	public static Document mapToXml(Map<?, ?> data, String rootName) {
		return mapToXml(data, rootName, null);
	}

	/**
	 * 将Map转换为XML
	 *
	 * @param data      Map类型数据
	 * @param rootName  根节点名
	 * @param namespace 命名空间，可以为null
	 * @return XML
	 * @since 5.0.4
	 */
	public static Document mapToXml(Map<?, ?> data, String rootName, String namespace) {
		final Document doc = createXml();
		final Element root = appendChild(doc, rootName, namespace);

		appendMap(doc, root, data);
		return doc;
	}

	/**
	 * 将Bean转换为XML
	 *
	 * @param bean Bean对象
	 * @return XML
	 * @since 5.3.4
	 */
	public static Document beanToXml(Object bean) {
		return beanToXml(bean, null);
	}

	/**
	 * 将Bean转换为XML
	 *
	 * @param bean      Bean对象
	 * @param namespace 命名空间，可以为null
	 * @return XML
	 * @since 5.2.4
	 */
	public static Document beanToXml(Object bean, String namespace) {
		if (null == bean) {
			return null;
		}
		return mapToXml(BeanUtil.beanToMap(bean), bean.getClass().getSimpleName(), namespace);
	}

	/**
	 * 给定节点是否为{@link Element} 类型节点
	 *
	 * @param node 节点
	 * @return 是否为{@link Element} 类型节点
	 * @since 4.0.8
	 */
	public static boolean isElement(Node node) {
		return (null != node) && Node.ELEMENT_NODE == node.getNodeType();
	}

	/**
	 * 在已有节点上创建子节点
	 *
	 * @param node    节点
	 * @param tagName 标签名
	 * @return 子节点
	 * @since 4.0.9
	 */
	public static Element appendChild(Node node, String tagName) {
		return appendChild(node, tagName, null);
	}

	/**
	 * 在已有节点上创建子节点
	 *
	 * @param node      节点
	 * @param tagName   标签名
	 * @param namespace 命名空间，无传null
	 * @return 子节点
	 * @since 5.0.4
	 */
	public static Element appendChild(Node node, String tagName, String namespace) {
		final Document doc = getOwnerDocument(node);
		final Element child = (null == namespace) ? doc.createElement(tagName) : doc.createElementNS(namespace, tagName);
		node.appendChild(child);
		return child;
	}

	/**
	 * 创建文本子节点
	 *
	 * @param node 节点
	 * @param text 文本
	 * @return 子节点
	 * @since 5.3.0
	 */
	public static Node appendText(Node node, CharSequence text) {
		return appendText(getOwnerDocument(node), node, text);
	}
	// ---------------------------------------------------------------------------------------- Private method start

	/**
	 * 追加数据子节点，可以是Map、集合、文本
	 *
	 * @param doc  {@link Document}
	 * @param node 节点
	 * @param data 数据
	 */
	@SuppressWarnings("rawtypes")
	private static void append(Document doc, Node node, Object data) {
		if (data instanceof Map) {
			// 如果值依旧为map，递归继续
			appendMap(doc, node, (Map) data);
		} else if (data instanceof Iterator) {
			// 如果值依旧为map，递归继续
			appendIterator(doc, node, (Iterator) data);
		} else if (data instanceof Iterable) {
			// 如果值依旧为map，递归继续
			appendIterator(doc, node, ((Iterable) data).iterator());
		} else {
			appendText(doc, node, data.toString());
		}
	}

	/**
	 * 追加Map数据子节点
	 *
	 * @param doc  {@link Document}
	 * @param node 当前节点
	 * @param data Map类型数据
	 * @since 4.0.8
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	private static void appendMap(Document doc, Node node, Map data) {
		data.forEach((key, value) -> {
			if (null != key) {
				final Element child = appendChild(node, key.toString());
				if (null != value) {
					append(doc, child, value);
				}
			}
		});
	}

	/**
	 * 追加集合节点
	 *
	 * @param doc  {@link Document}
	 * @param node 节点
	 * @param data 数据
	 */
	@SuppressWarnings("rawtypes")
	private static void appendIterator(Document doc, Node node, Iterator data) {
		final Node parentNode = node.getParentNode();
		boolean isFirst = true;
		Object eleData;
		while (data.hasNext()) {
			eleData = data.next();
			if (isFirst) {
				append(doc, node, eleData);
				isFirst = false;
			} else {
				final Node cloneNode = node.cloneNode(false);
				parentNode.appendChild(cloneNode);
				append(doc, cloneNode, eleData);
			}
		}
	}

	/**
	 * 追加文本节点
	 *
	 * @param doc  {@link Document}
	 * @param node 节点
	 * @param text 文本内容
	 * @return 增加的子节点，即Text节点
	 * @since 5.3.0
	 */
	private static Node appendText(Document doc, Node node, CharSequence text) {
		return node.appendChild(doc.createTextNode(StrUtil.str(text)));
	}

	/**
	 * 关闭XXE，避免漏洞攻击<br>
	 * see: https://www.owasp.org/index.php/XML_External_Entity_(XXE)_Prevention_Cheat_Sheet#JAXP_DocumentBuilderFactory.2C_SAXParserFactory_and_DOM4J
	 *
	 * @param dbf DocumentBuilderFactory
	 * @return DocumentBuilderFactory
	 */
	private static DocumentBuilderFactory disableXXE(DocumentBuilderFactory dbf) {
		String feature;
		try {
			// This is the PRIMARY defense. If DTDs (doctypes) are disallowed, almost all XML entity attacks are prevented
			// Xerces 2 only - http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl
			feature = "http://apache.org/xml/features/disallow-doctype-decl";
			dbf.setFeature(feature, true);
			// If you can't completely disable DTDs, then at least do the following:
			// Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-general-entities
			// Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-general-entities
			// JDK7+ - http://xml.org/sax/features/external-general-entities
			feature = "http://xml.org/sax/features/external-general-entities";
			dbf.setFeature(feature, false);
			// Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-parameter-entities
			// Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-parameter-entities
			// JDK7+ - http://xml.org/sax/features/external-parameter-entities
			feature = "http://xml.org/sax/features/external-parameter-entities";
			dbf.setFeature(feature, false);
			// Disable external DTDs as well
			feature = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
			dbf.setFeature(feature, false);
			// and these as well, per Timothy Morgan's 2014 paper: "XML Schema, DTD, and Entity Attacks"
			dbf.setXIncludeAware(false);
			dbf.setExpandEntityReferences(false);
		} catch (ParserConfigurationException e) {
			// ignore
		}
		return dbf;
	}

	/**
	 * 全局命名空间上下文<br>
	 * 见：https://www.ibm.com/developerworks/cn/xml/x-nmspccontext/
	 */
	public static class UniversalNamespaceCache implements NamespaceContext {
		private static final String DEFAULT_NS = "DEFAULT";
		private final BiMap<String, String> prefixUri = new BiMap<>(new HashMap<>());

		/**
		 * This constructor parses the document and stores all namespaces it can
		 * find. If toplevelOnly is true, only namespaces in the root are used.
		 *
		 * @param node         source Node
		 * @param toplevelOnly restriction of the search to enhance performance
		 */
		public UniversalNamespaceCache(Node node, boolean toplevelOnly) {
			examineNode(node.getFirstChild(), toplevelOnly);
		}

		/**
		 * A single node is read, the namespace attributes are extracted and stored.
		 *
		 * @param node            to examine
		 * @param attributesOnly, if true no recursion happens
		 */
		private void examineNode(Node node, boolean attributesOnly) {
			final NamedNodeMap attributes = node.getAttributes();
			if (null != attributes) {
				for (int i = 0; i < attributes.getLength(); i++) {
					Node attribute = attributes.item(i);
					storeAttribute(attribute);
				}
			}

			if (false == attributesOnly) {
				final NodeList childNodes = node.getChildNodes();
				if (null != childNodes) {
					Node item;
					for (int i = 0; i < childNodes.getLength(); i++) {
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
		private void storeAttribute(Node attribute) {
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
		public String getNamespaceURI(String prefix) {
			if (prefix == null || prefix.equals(XMLConstants.DEFAULT_NS_PREFIX)) {
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
		public String getPrefix(String namespaceURI) {
			return prefixUri.getInverse().get(namespaceURI);
		}

		@Override
		public Iterator<String> getPrefixes(String namespaceURI) {
			// Not implemented
			return null;
		}

	}
	// ---------------------------------------------------------------------------------------- Private method end

}
