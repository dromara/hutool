package cn.hutool.core.util;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;

/**
 * XML工具类<br>
 * 此工具使用w3c dom工具，不需要依赖第三方包。<br>
 * 工具类封装了XML文档的创建、读取、写出和部分XML操作
 * 
 * @author xiaoleilu
 * 
 */
public class XmlUtil {

	/** 在XML中无效的字符 正则 */
	public final static String INVALID_REGEX = "[\\x00-\\x08\\x0b-\\x0c\\x0e-\\x1f]";

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
		return readXML(new InputSource(StrUtil.getReader(xmlStr)));
	}

	/**
	 * 从XML中读取对象 Reads serialized object from the XML file.
	 * 
	 * @param <T> 对象类型
	 * @param source XML文件
	 * @return 对象
	 * @throws IOException IO异常
	 */
	public static <T> T readObjectFromXml(File source) throws IOException {
		return readObjectFromXml(new InputSource(FileUtil.getInputStream(source)));
	}

	/**
	 * 从XML中读取对象 Reads serialized object from the XML file.
	 * 
	 * @param <T> 对象类型
	 * @param xmlStr XML内容
	 * @return 对象
	 * @throws IOException IO异常
	 * @since 3.2.0
	 */
	public static <T> T readObjectFromXml(String xmlStr) throws IOException {
		return readObjectFromXml(new InputSource(StrUtil.getReader(xmlStr)));
	}

	/**
	 * 从XML中读取对象 Reads serialized object from the XML file.
	 * 
	 * @param <T> 对象类型
	 * @param source {@link InputSource}
	 * @return 对象
	 * @throws IOException IO异常
	 * @since 3.2.0
	 */
	@SuppressWarnings("unchecked")
	public static <T> T readObjectFromXml(InputSource source) throws IOException {
		Object result = null;
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
	 * 字符编码使用XML文档中的编码，获取不到则使用UTF-8
	 * 
	 * @param doc XML文档
	 * @return XML字符串
	 */
	public static String toStr(Document doc) {
		return toStr(doc, true);
	}

	/**
	 * 将XML文档转换为String<br>
	 * 字符编码使用XML文档中的编码，获取不到则使用UTF-8
	 * 
	 * @param doc XML文档
	 * @param isPretty 是否格式化输出
	 * @return XML字符串
	 * @since 3.0.9
	 */
	public static String toStr(Document doc, boolean isPretty) {
		final StringWriter writer = StrUtil.getWriter();
		try {
			write(doc, writer, isPretty);
		} catch (Exception e) {
			throw new UtilException(e, "Trans xml document to string error!");
		}
		return writer.toString();
	}

	/**
	 * 将XML文档转换为String<br>
	 * 字符编码使用XML文档中的编码，获取不到则使用UTF-8
	 * 
	 * @param doc XML文档
	 * @param charset 编码
	 * @param isPretty 是否格式化输出
	 * @return XML字符串
	 * @since 3.0.9
	 */
	public static String toStr(Document doc, String charset, boolean isPretty) {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			write(doc, out, charset, isPretty);
		} catch (Exception e) {
			throw new UtilException(e, "Trans xml document to string error!");
		}
		return out.toString();
	}

	/**
	 * 将XML文档写入到文件<br>
	 * 使用Document中的编码
	 * 
	 * @param doc XML文档
	 * @param absolutePath 文件绝对路径，不存在会自动创建
	 */
	public static void toFile(Document doc, String absolutePath) {
		toFile(doc, absolutePath, null);
	}

	/**
	 * 将XML文档写入到文件<br>
	 * 
	 * @param doc XML文档
	 * @param path 文件路径绝对路径或相对ClassPath路径，不存在会自动创建
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
			write(doc, writer, true);
		} finally {
			IoUtil.close(writer);
		}
	}

	/**
	 * 将XML文档写出
	 * 
	 * @param node {@link Node} XML文档节点或文档本身
	 * @param writer 写出的Writer，Writer决定了输出XML的编码
	 * @param isPretty 是否格式化输出
	 * @since 3.0.9
	 */
	public static void write(Node node, Writer writer, boolean isPretty) {
		transform(new DOMSource(node), new StreamResult(writer), null, isPretty);
	}

	/**
	 * 将XML文档写出
	 * 
	 * @param node {@link Node} XML文档节点或文档本身
	 * @param out 写出的Writer，Writer决定了输出XML的编码
	 * @param charset 编码
	 * @param isPretty 是否格式化输出
	 * @since 4.0.8
	 */
	public static void write(Node node, OutputStream out, String charset, boolean isPretty) {
		transform(new DOMSource(node), new StreamResult(out), charset, isPretty);
	}

	/**
	 * 将XML文档写出
	 * 
	 * @param source 源
	 * @param result 目标
	 * @param charset 编码
	 * @param isPretty 是否格式化输出
	 * @since 4.0.9
	 */
	public static void transform(Source source, Result result, String charset, boolean isPretty) {
		final TransformerFactory factory = TransformerFactory.newInstance();
		try {
			final Transformer xformer = factory.newTransformer();
			xformer.setOutputProperty(OutputKeys.INDENT, isPretty ? "yes" : "no");
			if (StrUtil.isNotBlank(charset)) {
				xformer.setOutputProperty(OutputKeys.ENCODING, charset);
			}
			xformer.transform(source, result);
		} catch (Exception e) {
			throw new UtilException(e, "Trans xml document to string error!");
		}
	}

	// -------------------------------------------------------------------------------------- Create
	/**
	 * 创建XML文档<br>
	 * 创建的XML默认是utf8编码，修改编码的过程是在toStr和toFile方法里，既XML在转为文本的时候才定义编码
	 * 
	 * @return XML文档
	 * @since 4.0.8
	 */
	public static Document createXml() {
		return createDocumentBuilder().newDocument();
	}
	
	/**
	 * 创建 DocumentBuilder
	 * @return DocumentBuilder
	 * @since 4.1.2
	 */
	public static DocumentBuilder createDocumentBuilder() {
		final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		disableXXE(dbf);
		DocumentBuilder builder = null;
		try {
			builder = dbf.newDocumentBuilder();
		} catch (Exception e) {
			throw new UtilException(e, "Create xml document error!");
		}
		return builder;
	}

	/**
	 * 创建XML文档<br>
	 * 创建的XML默认是utf8编码，修改编码的过程是在toStr和toFile方法里，既XML在转为文本的时候才定义编码
	 * 
	 * @param rootElementName 根节点名称
	 * @return XML文档
	 */
	public static Document createXml(String rootElementName) {
		final Document doc = createXml();
		doc.appendChild(doc.createElement(rootElementName));

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
	 * @param element 节点
	 * @param tagName 节点名
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
	 * @param parentEle 父节点，如果指定将返回此节点的所有直接子节点，nul返回所有就节点
	 * @param nodeList NodeList
	 * @return Element列表
	 */
	public static List<Element> transElements(Element parentEle, NodeList nodeList) {
		int length = nodeList.getLength();
		final ArrayList<Element> elements = new ArrayList<Element>(length);
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
	 * @throws IOException IO异常
	 */
	public static void writeObjectAsXml(File dest, Object bean) throws IOException {
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
	 * @param source 资源，可以是Docunent、Node节点等
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
	 * @param source 资源，可以是Docunent、Node节点等
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
	 * @param source 资源，可以是Docunent、Node节点等
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
	 * @param source 资源，可以是Docunent、Node节点等
	 * @param returnType 返回类型，{@link javax.xml.xpath.XPathConstants}
	 * @return 匹配返回类型的值
	 * @since 3.2.0
	 */
	public static Object getByXPath(String expression, Object source, QName returnType) {
		final XPath xPath = createXPath();
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
		final StringBuilder sb = new StringBuilder(string.length());
		for (int i = 0, length = string.length(); i < length; i++) {
			char c = string.charAt(i);
			switch (c) {
			case '&':
				sb.append("&amp;");
				break;
			case '<':
				sb.append("&lt;");
				break;
			case '>':
				sb.append("&gt;");
				break;
			case '"':
				sb.append("&quot;");
				break;
			case '\'':
				sb.append("&apos;");
				break;
			default:
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * XML格式字符串转换为Map
	 *
	 * @param xmlStr XML字符串
	 * @return XML数据转换后的Map
	 * @since 4.0.8
	 */
	public static Map<String, Object> xmlToMap(String xmlStr) {
		return xmlToMap(xmlStr, new HashMap<String, Object>());
	}

	/**
	 * XML格式字符串转换为Map
	 *
	 * @param node XML节点
	 * @return XML数据转换后的Map
	 * @since 4.0.8
	 */
	public static Map<String, Object> xmlToMap(Node node) {
		return xmlToMap(node, new HashMap<String, Object>());
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
	 * @param node XML节点
	 * @param result 结果Map类型
	 * @return XML数据转换后的Map
	 * @since 4.0.8
	 */
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
			if (isElement(childNode)) {
				childEle = (Element) childNode;
				result.put(childEle.getNodeName(), childEle.getTextContent());
			}
		}
		return result;
	}

	/**
	 * 将Map转换为XML格式的字符串
	 *
	 * @param data Map类型数据
	 * @return XML格式的字符串
	 * @since 4.0.8
	 */
	public static String mapToXmlStr(Map<?, ?> data, String rootName) {
		return toStr(mapToXml(data, rootName));
	}

	/**
	 * 将Map转换为XML
	 *
	 * @param data Map类型数据
	 * @return XML
	 * @since 4.0.9
	 */
	public static Document mapToXml(Map<?, ?> data, String rootName) {
		final Document doc = createXml();
		final Element root = appendChild(doc, rootName);

		mapToXml(doc, root, data);
		return doc;
	}

	/**
	 * 给定节点是否为{@link Element} 类型节点
	 * 
	 * @param node 节点
	 * @return 是否为{@link Element} 类型节点
	 * @since 4.0.8
	 */
	public static boolean isElement(Node node) {
		return (null == node) ? false : Node.ELEMENT_NODE == node.getNodeType();
	}

	/**
	 * 在已有节点上创建子节点
	 * 
	 * @param node 节点
	 * @param tagName 标签名
	 * @return 子节点
	 * @since 4.0.9
	 */
	public static Element appendChild(Node node, String tagName) {
		Document doc = (node instanceof Document) ? (Document) node : node.getOwnerDocument();
		Element child = doc.createElement(tagName);
		node.appendChild(child);
		return child;
	}

	// ---------------------------------------------------------------------------------------- Private method start
	/**
	 * 将Map转换为XML格式的字符串
	 *
	 * @param doc {@link Document}
	 * @param element 节点
	 * @param data Map类型数据
	 * @since 4.0.8
	 */
	private static void mapToXml(Document doc, Element element, Map<?, ?> data) {
		Element filedEle;
		Object value;
		for (Entry<?, ?> entry : data.entrySet()) {
			filedEle = doc.createElement(entry.getKey().toString());
			element.appendChild(filedEle);
			value = entry.getValue();
			if (value instanceof Map) {
				mapToXml(doc, filedEle, (Map<?, ?>) value);
				element.appendChild(filedEle);
			} else {
				filedEle.appendChild(doc.createTextNode(value.toString()));
			}
		}
	}
	
	/**
	 * 关闭XXE，避免漏洞攻击<br>
	 * see: https://www.owasp.org/index.php/XML_External_Entity_(XXE)_Prevention_Cheat_Sheet#JAXP_DocumentBuilderFactory.2C_SAXParserFactory_and_DOM4J
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
	// ---------------------------------------------------------------------------------------- Private method end

}
