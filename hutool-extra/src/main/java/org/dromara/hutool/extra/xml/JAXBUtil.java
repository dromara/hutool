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

package org.dromara.hutool.extra.xml;

import org.dromara.hutool.core.exception.HutoolException;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.core.xml.XmlUtil;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.File;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.Charset;

/**
 * JAXB（Java Architecture for XML Binding），根据XML Schema产生Java对象，即实现xml和Bean互转。
 * <p>
 * 相关介绍：
 * <ul>
 *   <li><a href="https://www.cnblogs.com/yanghaolie/p/11110991.html">https://www.cnblogs.com/yanghaolie/p/11110991.html</a></li>
 *   <li><a href="https://my.oschina.net/u/4266515/blog/3330113">https://my.oschina.net/u/4266515/blog/3330113</a></li>
 * </ul>
 *
 * @author dazer
 * @see XmlUtil
 * @since 5.7.3
 */
public class JAXBUtil {

	/**
	 * JavaBean转换成xml
	 * <p>
	 * bean上面用的常用注解
	 *
	 * @param bean Bean对象
	 * @return 输出的XML字符串
	 * @see XmlRootElement {@code @XmlRootElement(name = "school")}
	 * @see XmlElement {@code @XmlElement(name = "school_name", required = true)}
	 * @see XmlElementWrapper {@code @XmlElementWrapper(name="schools")}
	 * @see XmlTransient JAXB "有两个名为 "**" 的属性,类的两个属性具有相同名称 "**""解决方案
	 */
	public static String beanToXml(final Object bean) {
		return beanToXml(bean, CharsetUtil.UTF_8, true);
	}

	/**
	 * JavaBean转换成xml
	 *
	 * @param bean    Bean对象
	 * @param charset 编码 eg: utf-8
	 * @param format  是否格式化输出eg: true
	 * @return 输出的XML字符串
	 */
	public static String beanToXml(final Object bean, final Charset charset, final boolean format) {
		final StringWriter writer;
		try {
			final JAXBContext context = JAXBContext.newInstance(bean.getClass());
			final Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, format);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, charset.name());
			writer = new StringWriter();
			marshaller.marshal(bean, writer);
		} catch (final Exception e) {
			throw new HutoolException("convertToXml 错误：" + e.getMessage(), e);
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
	public static <T> T xmlToBean(final String xml, final Class<T> c) {
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
	public static <T> T xmlToBean(final File file, final Charset charset, final Class<T> c) {
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
	public static <T> T xmlToBean(final Reader reader, final Class<T> c) {
		try {
			final JAXBContext context = JAXBContext.newInstance(c);
			final Unmarshaller unmarshaller = context.createUnmarshaller();
			return (T) unmarshaller.unmarshal(reader);
		} catch (final Exception e) {
			throw new RuntimeException("convertToJava2 错误：" + e.getMessage(), e);
		} finally {
			IoUtil.closeQuietly(reader);
		}
	}
}
