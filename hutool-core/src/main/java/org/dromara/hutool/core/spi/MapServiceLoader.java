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
import org.dromara.hutool.core.io.resource.ResourceUtil;
import org.dromara.hutool.core.reflect.ConstructorUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * 键值对服务加载器，使用{@link Properties}加载并存储服务<br>
 * 服务文件默认位于"META-INF/hutool/"下，文件名为服务接口类全名。
 *
 * <p>
 * 内容类似于：
 * <pre>
 *     # 我是注释
 *     service1 = hutool.service.Service1
 *     service2 = hutool.service.Service2
 * </pre>
 * <p>
 * 通过调用{@link #getService(String)}方法，传入等号前的名称，即可获取对应服务。
 *
 * @param <S> 服务类型
 * @author looly
 * @since 6.0.0
 */
public class MapServiceLoader<S> extends AbsServiceLoader<S> {

	private static final String PREFIX_HUTOOL = "META-INF/hutool/";

	// region ----- of

	/**
	 * 构建KVServiceLoader
	 *
	 * @param <S>          服务类型
	 * @param serviceClass 服务名称
	 * @return KVServiceLoader
	 */
	public static <S> MapServiceLoader<S> of(final Class<S> serviceClass) {
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
	public static <S> MapServiceLoader<S> of(final Class<S> serviceClass, final ClassLoader classLoader) {
		return of(PREFIX_HUTOOL, serviceClass, classLoader);
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
	public static <S> MapServiceLoader<S> of(final String pathPrefix, final Class<S> serviceClass,
											 final ClassLoader classLoader) {
		return new MapServiceLoader<>(pathPrefix, serviceClass, classLoader, null);
	}
	// endregion

	private Properties serviceProperties;
	// key: serviceName, value: service instance
	private final SimpleCache<String, S> serviceCache;

	/**
	 * 构造
	 *
	 * @param pathPrefix   路径前缀
	 * @param serviceClass 服务名称
	 * @param classLoader  自定义类加载器, {@code null}表示使用默认当前的类加载器
	 * @param charset      编码，默认UTF-8
	 */
	public MapServiceLoader(final String pathPrefix, final Class<S> serviceClass,
							final ClassLoader classLoader, final Charset charset) {
		super(pathPrefix, serviceClass, classLoader, charset);

		this.serviceCache = new SimpleCache<>(new HashMap<>());
		load();
	}

	/**
	 * 加载或重新加载全部服务
	 */
	@Override
	public void load() {
		// 解析同名的所有service资源
		// 按照资源加载优先级，先加载和解析的资源优先使用，后加载的同名资源丢弃
		final Properties properties = new Properties();
		ResourceUtil.loadAllTo(
			properties,
			pathPrefix + serviceClass.getName(),
			classLoader,
			charset,
			// 非覆盖模式
			false);
		this.serviceProperties = properties;
	}

	@Override
	public int size() {
		return this.serviceProperties.size();
	}

	@Override
	public List<String> getServiceNames() {
		return ListUtil.view(this.serviceCache.keys());
	}

	@Override
	public Class<S> getServiceClass(final String serviceName) {
		final String serviceClassName = this.serviceProperties.getProperty(serviceName);
		if (StrUtil.isBlank(serviceClassName)) {
			return null;
		}

		return ClassLoaderUtil.loadClass(serviceClassName);
	}

	@Override
	public S getService(final String serviceName) {
		return this.serviceCache.get(serviceName, () -> createService(serviceName));
	}

	@Override
	public Iterator<S> iterator() {
		return new Iterator<S>() {
			private final Iterator<String> nameIter =
				serviceProperties.stringPropertyNames().iterator();

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
	 * 创建服务，无缓存
	 *
	 * @param serviceName 服务名称
	 * @return 服务对象
	 */
	private S createService(final String serviceName) {
		return ConstructorUtil.newInstance(getServiceClass(serviceName));
	}
	// endregion
}
