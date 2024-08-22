/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.core.spi;

import org.dromara.hutool.core.text.StrUtil;

import java.nio.charset.Charset;

/**
 * 抽象服务加载器，提供包括路径前缀、服务类、类加载器、编码、安全相关持有
 *
 * @param <S> 服务类型
 * @author looly
 * @since 6.0.0
 */
public abstract class AbsServiceLoader<S> implements ServiceLoader<S> {

	/**
	 * 路径前缀
	 */
	protected final String pathPrefix;
	/**
	 * 服务类
	 */
	protected final Class<S> serviceClass;
	/**
	 * 自定义类加载器
	 */
	protected final ClassLoader classLoader;
	/**
	 * 编码
	 */
	protected final Charset charset;

	/**
	 * 构造
	 *
	 * @param pathPrefix   路径前缀
	 * @param serviceClass 服务类
	 * @param classLoader  自定义类加载器, {@code null}表示使用默认当前的类加载器
	 * @param charset      编码，默认UTF-8
	 */
	public AbsServiceLoader(final String pathPrefix, final Class<S> serviceClass,
							final ClassLoader classLoader, final Charset charset) {
		this.pathPrefix = StrUtil.addSuffixIfNot(pathPrefix, StrUtil.SLASH);
		this.serviceClass = serviceClass;
		this.classLoader = classLoader;
		this.charset = charset;
	}
}
