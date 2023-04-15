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
import org.dromara.hutool.core.classloader.ClassLoaderUtil;
import org.dromara.hutool.core.io.resource.ResourceUtil;
import org.dromara.hutool.core.reflect.ConstructorUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.AccessUtil;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Properties;

/**
 * 键值对服务加载器，使用{@link Properties}加载并存储服务
 *
 * @param <S> 服务类型
 * @author looly
 * @since 6.0.0
 */
public class KVServiceLoader<S> extends AbsServiceLoader<S> {

	private static final String PREFIX_HUTOOL = "META-INF/hutool/";

	// region ----- of

	/**
	 * 构建KVServiceLoader
	 *
	 * @param <S>          服务类型
	 * @param serviceClass 服务名称
	 * @return KVServiceLoader
	 */
	public static <S> KVServiceLoader<S> of(final Class<S> serviceClass) {
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
	public static <S> KVServiceLoader<S> of(final Class<S> serviceClass, final ClassLoader classLoader) {
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
	public static <S> KVServiceLoader<S> of(final String pathPrefix, final Class<S> serviceClass,
											final ClassLoader classLoader) {
		return new KVServiceLoader<>(pathPrefix, serviceClass, classLoader, null);
	}
	// endregion

	private Properties serviceProperties;
	private final SimpleCache<String, S> serviceCache;

	/**
	 * 构造
	 *
	 * @param pathPrefix   路径前缀
	 * @param serviceClass 服务名称
	 * @param classLoader  自定义类加载器, {@code null}表示使用默认当前的类加载器
	 * @param charset      编码，默认UTF-8
	 */
	public KVServiceLoader(final String pathPrefix, final Class<S> serviceClass,
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
		final Properties properties = new Properties();
		ResourceUtil.loadAllTo(
			properties,
			pathPrefix + serviceClass.getName(),
			classLoader,
			charset);
		this.serviceProperties = properties;
	}

	@Override
	public int size() {
		return this.serviceProperties.size();
	}

	/**
	 * 获取指定服务的实现类
	 *
	 * @param serviceName 服务名称
	 * @return 服务名称对应的实现类
	 */
	public Class<S> getServiceClass(final String serviceName) {
		return AccessUtil.doPrivileged(() -> getServiceClassUnsafe(serviceName), this.acc);
	}

	/**
	 * 获取指定名称对应的服务，使用缓存，多次调用只返回相同的服务对象
	 *
	 * @param serviceName 服务名称
	 * @return 服务对象
	 */
	public S getService(final String serviceName) {
		return this.serviceCache.get(serviceName, () -> createService(serviceName));
	}

	/**
	 * 创建服务，无缓存
	 *
	 * @param serviceName 服务名称
	 * @return 服务对象
	 */
	private S createService(final String serviceName) {
		return AccessUtil.doPrivileged(() ->
			ConstructorUtil.newInstance(getServiceClassUnsafe(serviceName)), this.acc);
	}

	/**
	 * 获取指定服务的实现类
	 *
	 * @param serviceName 服务名称
	 * @return 服务名称对应的实现类
	 */
	private Class<S> getServiceClassUnsafe(final String serviceName) {
		final String serviceClassName = this.serviceProperties.getProperty(serviceName);
		if (StrUtil.isBlank(serviceClassName)) {
			return null;
		}

		return ClassLoaderUtil.loadClass(serviceClassName);
	}
}
