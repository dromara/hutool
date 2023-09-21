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

package org.dromara.hutool.core.spi;

import org.dromara.hutool.core.cache.SimpleCache;
import org.dromara.hutool.core.classloader.ClassLoaderUtil;
import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.resource.MultiResource;
import org.dromara.hutool.core.io.resource.Resource;
import org.dromara.hutool.core.io.resource.ResourceUtil;
import org.dromara.hutool.core.reflect.ConstructorUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 列表类型的服务加载器，用于替换JDK提供的{@link java.util.ServiceLoader}<br>
 * 相比JDK，增加了：
 * <ul>
 *     <li>可选服务存储位置（默认位于META-INF/services/）。</li>
 *     <li>可自定义编码。</li>
 *     <li>可自定义加载指定的服务实例。</li>
 *     <li>可自定义加载指定的服务类，由用户决定如何实例化（如传入自定义构造参数等）。</li>
 *     <li>提供更加灵活的服务加载机制，当选择加载指定服务时，其它服务无需加载。</li>
 * </ul>
 *
 * <p>
 * 服务文件默认位于"META-INF/services/"下，文件名为服务接口类全名。内容类似于：
 * <pre>
 *     # 我是注释
 *     hutool.service.Service1
 *     hutool.service.Service2
 * </pre>
 * <p>
 * 通过调用{@link #getService(int)}方法，传入序号，即可获取对应服务。
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
	// key: className, value: service instance
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
		// 按照资源加载优先级，先加载和解析的资源优先使用，后加载的同名资源丢弃
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

	@Override
	public List<String> getServiceNames(){
		return ListUtil.view(this.serviceNames);
	}

	/**
	 * 获取指定服务的实现类
	 *
	 * @param index 服务名称
	 * @return 服务名称对应的实现类
	 */
	public Class<S> getServiceClass(final int index) {
		final String serviceClassName = this.serviceNames.get(index);
		if (StrUtil.isBlank(serviceClassName)) {
			return null;
		}

		return getServiceClass(serviceClassName);
	}

	@Override
	public Class<S> getServiceClass(final String serviceName) {
		return ClassLoaderUtil.loadClass(serviceName);
	}

	/**
	 * 获取指定序号对应的服务，使用缓存，多次调用只返回相同的服务对象
	 *
	 * @param index 服务名称
	 * @return 服务对象
	 */
	public S getService(final int index) {
		final String serviceClassName = this.serviceNames.get(index);
		if (null == serviceClassName) {
			return null;
		}
		return getService(serviceClassName);
	}

	@Override
	public S getService(final String serviceName) {
		return this.serviceCache.get(serviceName, () -> createService(serviceName));
	}

	@Override
	public Iterator<S> iterator() {
		return new Iterator<S>() {
			private final Iterator<String> nameIter = serviceNames.iterator();

			@Override
			public boolean hasNext() {
				return nameIter.hasNext();
			}

			@Override
			public S next() {
				return getService(nameIter.next());
			}
		};
	}

	// region ----- private methods

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
		if (!line.isEmpty()) {
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

	/**
	 * 检查行
	 *
	 * @param resource 资源
	 * @param lineNo   行号
	 * @param line     行内容
	 */
	private void checkLine(final Resource resource, final int lineNo, final String line) {
		if (StrUtil.containsBlank(line)) {
			// 类中不允许空白符
			fail(resource, lineNo, "Illegal configuration-file syntax");
		}
		int cp = line.codePointAt(0);
		if (!Character.isJavaIdentifierStart(cp)) {
			// 非Java合法标识符
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

	/**
	 * 抛出异常
	 *
	 * @param resource 资源
	 * @param lineNo   行号
	 * @param msg      消息
	 */
	private void fail(final Resource resource, final int lineNo, final String msg) {
		throw new SPIException(this.serviceClass + ":" + resource.getUrl() + ":" + lineNo + ": " + msg);
	}

	/**
	 * 创建服务，无缓存
	 *
	 * @param serviceClassName 服务类名称
	 * @return 服务对象
	 */
	private S createService(final String serviceClassName) {
		return ConstructorUtil.newInstance(ClassLoaderUtil.loadClass(serviceClassName));
	}
	// endregion
}
