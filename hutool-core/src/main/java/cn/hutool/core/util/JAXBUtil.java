package cn.hutool.core.util;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.Charset;

/**
 * JAXB（Java Architecture for XML Binding），根据XML Schema产生Java对象，即实现xml和Bean互转。
 * <p>
 * 相关介绍：
 * <ul>
 *   <li>https://www.cnblogs.com/yanghaolie/p/11110991.html</li>
 *   <li>https://my.oschina.net/u/4266515/blog/3330113</li>
 * </ul>
 *
 * @author dazer
 * @see XmlUtil
 * @since 5.7.3
 */
public class JAXBUtil {

	/**
	 * JavaBean转换成xml
	 *
	 * @param bean Bean对象
	 * @return 输出的XML字符串
	 */
	public static String beanToXml(Object bean) {
		return beanToXml(bean, CharsetUtil.CHARSET_UTF_8, true);
	}

	/**
	 * JavaBean转换成xml
	 *
	 * @param bean    Bean对象
	 * @param charset 编码 eg: utf-8
	 * @param format  是否格式化输出eg: true
	 * @return 输出的XML字符串
	 */
	public static String beanToXml(Object bean, Charset charset, boolean format) {
		StringWriter writer;
		try {
			JAXBContext context = JAXBContext.newInstance(bean.getClass());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, format);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, charset.name());
			writer = new StringWriter();
			marshaller.marshal(bean, writer);
		} catch (Exception e) {
			throw new UtilException("convertToXml 错误：" + e.getMessage(), e);
		}
		return writer.toString();
	}

	/**
	 * xml转换成JavaBean
	 *
	 * @param <T> Bean类型
	 * @param xml XML字符串
	 * @param c   Bean类型
	 * @return bean
	 */
	public static <T> T xmlToBean(String xml, Class<T> c) {
		return xmlToBean(StrUtil.getReader(xml), c);
	}

	/**
	 * XML文件转Bean
	 *
	 * @param file    文件
	 * @param charset 编码
	 * @param c       Bean类
	 * @param <T>     Bean类型
	 * @return Bean
	 */
	public static <T> T xmlToBean(File file, Charset charset, Class<T> c) {
		return xmlToBean(FileUtil.getReader(file, charset), c);
	}

	/**
	 * 从{@link Reader}中读取XML字符串，并转换为Bean
	 *
	 * @param reader {@link Reader}
	 * @param c      Bean类
	 * @param <T>    Bean类型
	 * @return Bean
	 */
	@SuppressWarnings("unchecked")
	public static <T> T xmlToBean(Reader reader, Class<T> c) {
		try {
			JAXBContext context = JAXBContext.newInstance(c);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (T) unmarshaller.unmarshal(reader);
		} catch (Exception e) {
			throw new RuntimeException("convertToJava2 错误：" + e.getMessage(), e);
		} finally {
			IoUtil.close(reader);
		}
	}
}
