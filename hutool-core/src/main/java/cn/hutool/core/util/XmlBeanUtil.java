package cn.hutool.core.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * xml和Bean互转工具类，不依赖第三方库；需要使用到：javax.xml 包
 * @author dazer
 * 相关介绍：
 * <ul>
 * <li><a href=' java实体 和 xml相互转换'>https://www.cnblogs.com/yanghaolie/p/11110991.html</a></li>
 * <li><a href=' https://my.oschina.net/u/4266515/blog/3330113<'>JAXB "有两个名为 "**" 的属性,类的两个属性具有相同名称 "**""解决方案</a>/li>
 * </ul>
 */
public class XmlBeanUtil {
	/**
	 * JavaBean转换成xml
	 *
	 * @param obj
	 * @param encoding eg: utf-8
	 * @param format eg: true
	 * @return
	 */
	public static String convertToXml(Object obj, String encoding, boolean format) {
		String result = null;
		StringWriter writer = null;
		try {
			JAXBContext context = JAXBContext.newInstance(obj.getClass());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, format);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
			writer = new StringWriter();
			marshaller.marshal(obj, writer);
			result = writer.toString();
		} catch (Exception e) {
			throw new RuntimeException("convertToXml 错误：" + e.getMessage(), e);
		} finally {
			if (writer != null){
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public static String convertToXml(Object obj) {
		return convertToXml(obj, StandardCharsets.UTF_8.toString(), true);
	}

	/**
	 * xml转换成JavaBean
	 *
	 * @param xml
	 * @param c
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T convertToJava(String xml, Class<T> c) {
		if (xml == null || "".equals(xml))
			return null;
		T t = null;
		StringReader reader = null;
		try {
			JAXBContext context = JAXBContext.newInstance(c);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			reader = new StringReader(xml);
			t = (T) unmarshaller.unmarshal(reader);
		} catch (Exception e) {
			throw new RuntimeException("convertToJava1 错误：" + e.getMessage(), e);
		} finally {
			if (reader != null)
				reader.close();
		}
		return t;
	}

	@SuppressWarnings("unchecked")
	public static <T> T convertToJava(File filePath, Class<T> c) throws IOException {
		if (!filePath.exists())
			return null;
		T t = null;
		FileReader reader = null;
		try {
			JAXBContext context = JAXBContext.newInstance(c);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			reader = new FileReader(filePath);
			t = (T) unmarshaller.unmarshal(reader);
		} catch (Exception e) {
			throw new RuntimeException("convertToJava2 错误：" + e.getMessage(), e);
		} finally {
			if (reader != null)
				reader.close();
		}
		return t;
	}
}
