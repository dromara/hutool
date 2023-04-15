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

import org.dromara.hutool.core.classloader.ClassLoaderUtil;
import org.dromara.hutool.core.io.resource.ResourceUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.nio.charset.Charset;
import java.util.Properties;

public class KVServiceLoader<S> implements ServiceLoader<S>{

	private final String pathPrefix;
	private final Class<S> serviceClass;
	private final ClassLoader classLoader;
	private final Charset charset;

	private Properties serviceProperties;

	public KVServiceLoader(final String pathPrefix, final Class<S> serviceClass,
						   final ClassLoader classLoader, final Charset charset) {
		this.pathPrefix = pathPrefix;
		this.serviceClass = serviceClass;
		this.classLoader = classLoader;
		this.charset = charset;

		load();
	}

	/**
	 * 加载或重新加载全部服务
	 * @return this
	 */
	public KVServiceLoader<S> load(){
		final Properties properties = new Properties();
		ResourceUtil.loadAllTo(
			properties,
			pathPrefix + serviceClass.getName(),
			classLoader,
			charset);
		this.serviceProperties = properties;
		return this;
	}

	public Class<S> getServiceClass(final String serviceName){
		final String serviceClassName = this.serviceProperties.getProperty(serviceName);
		if(StrUtil.isNotBlank(serviceClassName)){
			return ClassLoaderUtil.loadClass(serviceClassName);
		}

		return null;
	}
}
