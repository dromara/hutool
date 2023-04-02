/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.dialect;

import org.dromara.hutool.io.IORuntimeException;
import org.dromara.hutool.io.resource.Resource;
import org.dromara.hutool.io.resource.ResourceUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * {@link Properties} 资源内容加载工具
 *
 * @author looly
 * @since 6.0.0
 */
public class PropsLoaderUtil {

	/**
	 * 加载配置文件内容到{@link Properties}中<br>
	 * 需要注意的是，如果资源文件的扩展名是.xml，会调用{@link Properties#loadFromXML(InputStream)} 读取。
	 *
	 * @param properties {@link Properties}文件
	 * @param resource   资源
	 * @param charset    编码，对XML无效
	 */
	public static void loadTo(final Properties properties, final Resource resource, final Charset charset) {
		final String filename = resource.getName();
		if (filename != null && filename.endsWith(".xml")) {
			// XML
			try (final InputStream in = resource.getStream()) {
				properties.loadFromXML(in);
			} catch (final IOException e) {
				throw new IORuntimeException(e);
			}
		} else {
			// .properties
			try (final BufferedReader reader = resource.getReader(charset)) {
				properties.load(reader);
			} catch (final IOException e) {
				throw new IORuntimeException(e);
			}
		}
	}

	/**
	 * 加载指定名称的所有配置文件内容到{@link Properties}中
	 *
	 * @param properties   {@link Properties}文件
	 * @param resourceName 资源名，可以是相对classpath的路径，也可以是绝对路径
	 * @param classLoader  {@link ClassLoader}，{@code null}表示使用默认的当前上下文ClassLoader
	 * @param charset      编码，对XML无效
	 */
	public static void loadAllTo(final Properties properties, final String resourceName, final ClassLoader classLoader, final Charset charset) {
		for (final Resource resource : ResourceUtil.getResources(resourceName, classLoader)) {
			loadTo(properties, resource, charset);
		}
	}
}
