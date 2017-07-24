package com.xiaoleilu.hutool.util;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.xiaoleilu.hutool.exceptions.UtilException;
import com.xiaoleilu.hutool.io.FileUtil;
import com.xiaoleilu.hutool.io.IoUtil;
import com.xiaoleilu.hutool.lang.Assert;

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
	
	private XmlUtil() {}

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
			//ignore
		}

		return readXML(new InputSource(file.toURI().toASCIIString()));
	}
	
	/**
	 * 读取解析XML文件<br>
	 * 如果给定内容以“<”开头，表示这是一个XML内容，直接读取，否则按照路径处理<br>
	 * 路径可以为相对路径，也可以是绝对路径，相对路径相对于ClassPath
	 * 
	 * @param pathOrContent 内容或路径
	 * @return XML文档对象
	 * @since 3.0.9
	 */
	public static Document readXML(String pathOrContent) {
		if(StrUtil.startWith(pathOrContent, '<')) {
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
	public static Document readXML(InputStream inputStream) throws UtilException{
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
	public static Document readXML(Reader reader) throws UtilException{
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
		final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			final DocumentBuilder builder = dbf.newDocumentBuilder();
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
	 * 从XML中读取对象
	 * Reads serialized object from the XML file.
	 * @param <T> 对象类型
	 * @param source XML文件
	 * @return 对象
	 * @throws IOException IO异常
	 */
	@SuppressWarnings("unchecked")
	public static <T> T readObjectFromXml(File source) throws IOException {
		Object result = null;
		XMLDecoder xmldec = null;
		try {
			xmldec = new XMLDecoder(FileUtil.getInputStream(source));
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
		return toStr(doc, null);
	}
	
	/**
	 * 将XML文档转换为String<br>
	 * 此方法会修改Document中的字符集
	 * 
	 * @param doc XML文档
	 * @param charset  自定义XML文件的编码，如果为{@code null} 读取XML文档中的编码，否则默认UTF-8
	 * @return XML字符串
	 */
	public static String toStr(Document doc, String charset) {
		return toStr(doc, charset, true);
	}

	/**
	 * 将XML文档转换为String<br>
	 * 此方法会修改Document中的字符集
	 * 
	 * @param doc XML文档
	 * @param charset  自定义XML文件的编码，如果为{@code null} 读取XML文档中的编码，否则默认UTF-8
	 * @return XML字符串
	 * @since 3.0.9
	 */
	public static String toStr(Document doc, String charset, boolean isPretty) {
		final StringWriter writer = StrUtil.getWriter();
		try {
			write(doc, writer, isPretty);
		} catch (Exception e) {
			throw new UtilException(e, "Trans xml document to string error!");
		}
		return writer.toString();
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
	 * @param doc {@link Document} XML文档
	 * @param writer 写出的Writer，Writer决定了输出XML的编码
	 * @param isPretty 是否格式化输出
	 * @since 3.0.9
	 */
	public static void write(Document doc, Writer writer, boolean isPretty) {
		final TransformerFactory factory = TransformerFactory.newInstance();
		try {
			final Transformer xformer = factory.newTransformer();
			xformer.setOutputProperty(OutputKeys.INDENT, isPretty ? "yes" : "no");
			xformer.transform(new DOMSource(doc), new StreamResult(writer));
		} catch (Exception e) {
			throw new UtilException("Trans xml document to string error!", e);
		}
	}

	// -------------------------------------------------------------------------------------- Create
	/**
	 * 创建XML文档<br>
	 * 创建的XML默认是utf8编码，修改编码的过程是在toStr和toFile方法里，既XML在转为文本的时候才定义编码
	 * 
	 * @param rootElementName 根节点名称
	 * @return XML文档
	 */
	public static Document createXml(String rootElementName) {
		final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = dbf.newDocumentBuilder();
		} catch (Exception e) {
			throw new UtilException("Create xml document error!", e);
		}
		final Document doc = builder.newDocument();
		doc.appendChild(doc.createElement(rootElementName));

		return doc;
	}

	// -------------------------------------------------------------------------------------- Function
	/**
	 * 获得XML文档根节点
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
		if (xmlContent == null) return null;
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
	 * 将NodeList转换为Element列表
	 * 
	 * @param parentEle 父节点，如果指定将返回此节点的所有直接子节点，nul返回所有就节点
	 * @param nodeList NodeList
	 * @return Element列表
	 */
	public static List<Element> transElements(Element parentEle, NodeList nodeList) {
		final ArrayList<Element> elements = new ArrayList<Element>();
		int length = nodeList.getLength();
		for (int i = 0; i < length; i++) {
			Element element = (Element) nodeList.item(i);
			if (parentEle == null || element.getParentNode() == parentEle) {
				elements.add(element);
			}
		}

		return elements;
	}

	/**
	 * 将可序列化的对象转换为XML写入文件，已经存在的文件将被覆盖<br>
	 * Writes serializable object to a XML file. Existing file will be overwritten
	 * @param <T> 对象类型
	 * @param dest 目标文件
	 * @param t 对象
	 * @throws IOException IO异常
	 */
	public static <T> void writeObjectAsXml(File dest, T t) throws IOException {
		XMLEncoder xmlenc = null;
		try {
			xmlenc = new XMLEncoder(FileUtil.getOutputStream(dest));
			xmlenc.writeObject(t);
		} finally {
			//关闭XMLEncoder会相应关闭OutputStream
			IoUtil.close(xmlenc);
		}
	}
	// ---------------------------------------------------------------------------------------- Private method start
	// ---------------------------------------------------------------------------------------- Private method end

}
