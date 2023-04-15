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

package org.dromara.hutool.core.spi;

import org.dromara.hutool.core.cache.SimpleCache;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.resource.MultiResource;
import org.dromara.hutool.core.io.resource.Resource;
import org.dromara.hutool.core.io.resource.ResourceUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 列表类型的服务加载器，用于替换JDK提供的{@link java.util.ServiceLoader}
 *
 * @param <S> 服务类型
 * @author looly
 * @since 6.0.0
 */
public class ListServiceLoader<S> extends AbsServiceLoader<S> {

	private static final String PREFIX_SERVICES = "META-INF/services/";

	// region ----- of

	/**
	 * 构建KVServiceLoader
	 *
	 * @param <S>          服务类型
	 * @param serviceClass 服务名称
	 * @return KVServiceLoader
	 */
	public static <S> ListServiceLoader<S> of(final Class<S> serviceClass) {
		return of(serviceClass, null);
	}

	/**
	 * 构建KVServiceLoader
	 *
	 * @param <S>          服务类型
	 * @param serviceClass 服务名称
	 * @param classLoader  自定义类加载器, {@code null}表示使用默认当前的类加载器
	 * @return KVServiceLoader
	 */
	public static <S> ListServiceLoader<S> of(final Class<S> serviceClass, final ClassLoader classLoader) {
		return of(PREFIX_SERVICES, serviceClass, classLoader);
	}

	/**
	 * 构建KVServiceLoader
	 *
	 * @param <S>          服务类型
	 * @param pathPrefix   路径前缀
	 * @param serviceClass 服务名称
	 * @param classLoader  自定义类加载器, {@code null}表示使用默认当前的类加载器
	 * @return KVServiceLoader
	 */
	public static <S> ListServiceLoader<S> of(final String pathPrefix, final Class<S> serviceClass,
											  final ClassLoader classLoader) {
		return new ListServiceLoader<>(pathPrefix, serviceClass, classLoader, null);
	}
	// endregion

	private final List<String> serviceNames;
	private final SimpleCache<String, S> serviceCache;

	/**
	 * 构造
	 *
	 * @param pathPrefix   路径前缀
	 * @param serviceClass 服务名称
	 * @param classLoader  自定义类加载器, {@code null}表示使用默认当前的类加载器
	 * @param charset      编码，默认UTF-8
	 */
	public ListServiceLoader(final String pathPrefix, final Class<S> serviceClass,
							 final ClassLoader classLoader, final Charset charset) {
		super(pathPrefix, serviceClass, classLoader, charset);
		this.serviceNames = new ArrayList<>();
		this.serviceCache = new SimpleCache<>(new HashMap<>());

		load();
	}

	@Override
	public void load() {
		// 解析同名的所有service资源
		final MultiResource resources = ResourceUtil.getResources(
			pathPrefix + serviceClass.getName(),
			this.classLoader);
		for (final Resource resource : resources) {
			parse(resource);
		}
	}

	@Override
	public int size() {
		return this.serviceNames.size();
	}

	/**
	 * 解析一个资源，一个资源对应一个service文件
	 *
	 * @param resource 资源
	 */
	private void parse(final Resource resource) {
		try (final BufferedReader reader = resource.getReader(this.charset)) {
			int lc = 1;
			while (lc >= 0) {
				lc = parseLine(resource, reader, lc);
			}
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 解析一行
	 *
	 * @param resource 资源
	 * @param reader   {@link BufferedReader}
	 * @param lineNo   行号
	 * @return 下一个行号，-1表示读取完毕
	 * @throws IOException IO异常
	 */
	private int parseLine(final Resource resource, final BufferedReader reader, final int lineNo)
		throws IOException {
		String line = reader.readLine();
		if (line == null) {
			// 结束
			return -1;
		}
		final int ci = line.indexOf('#');
		if (ci >= 0) {
			// 截取去除注释部分
			// 当注释单独成行，则此行长度为0，跳过，读取下一行
			line = line.substring(0, ci);
		}
		line = StrUtil.trim(line);
		if (line.length() > 0) {
			// 检查行
			checkLine(resource, lineNo, line);
			// 不覆盖模式
			final List<String> names = this.serviceNames;
			if (!serviceCache.containsKey(line) && !names.contains(line)) {
				names.add(line);
			}
		}
		return lineNo + 1;
	}

	private void checkLine(final Resource resource, final int lineNo, final String line) {
		if (StrUtil.containsBlank(line)) {
			fail(resource, lineNo, "Illegal configuration-file syntax");
		}
		int cp = line.codePointAt(0);
		if (!Character.isJavaIdentifierStart(cp)) {
			fail(resource, lineNo, "Illegal provider-class name: " + line);
		}
		final int n = line.length();
		for (int i = Character.charCount(cp); i < n; i += Character.charCount(cp)) {
			cp = line.codePointAt(i);
			if (!Character.isJavaIdentifierPart(cp) && (cp != '.')) {
				fail(resource, lineNo, "Illegal provider-class name: " + line);
			}
		}
	}

	private void fail(final Resource resource, final int line, final String msg) {
		throw new SPIException(this.serviceClass + ":" + resource.getUrl() + ":" + line + ": " + msg);
	}

}
