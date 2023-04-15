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

import org.dromara.hutool.core.text.StrUtil;

import java.nio.charset.Charset;
import java.security.AccessControlContext;
import java.security.AccessController;

/**
 * 抽象服务加载器，提供包括路径前缀、服务类、类加载器、编码、安全相关持有
 *
 * @param <S> 服务类型
 * @author looly
 * @since 6.0.0
 */
public abstract class AbsServiceLoader<S> implements ServiceLoader<S> {

	protected final String pathPrefix;
	protected final Class<S> serviceClass;
	protected final ClassLoader classLoader;
	protected final Charset charset;
	protected final AccessControlContext acc;

	/**
	 * 构造
	 *
	 * @param pathPrefix   路径前缀
	 * @param serviceClass 服务名称
	 * @param classLoader  自定义类加载器, {@code null}表示使用默认当前的类加载器
	 * @param charset      编码，默认UTF-8
	 */
	public AbsServiceLoader(final String pathPrefix, final Class<S> serviceClass,
							final ClassLoader classLoader, final Charset charset) {
		this.pathPrefix = StrUtil.addSuffixIfNot(pathPrefix, StrUtil.SLASH);
		this.serviceClass = serviceClass;
		this.classLoader = classLoader;
		this.charset = charset;
		this.acc = (System.getSecurityManager() != null) ? AccessController.getContext() : null;
	}
}
