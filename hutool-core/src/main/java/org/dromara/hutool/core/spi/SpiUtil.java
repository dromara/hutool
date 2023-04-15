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

import java.util.Iterator;

/**
 * 服务提供接口SPI（Service Provider interface）相关工具类
 *
 * @author looly
 * @since 6.0.0
 */
public class SpiUtil {

	/**
	 * 加载第一个可用服务，如果用户定义了多个接口实现类，只获取第一个不报错的服务
	 *
	 * @param <S>   服务类型
	 * @param clazz 服务接口
	 * @return 第一个服务接口实现对象，无实现返回{@code null}
	 */
	public static <S> S loadFirstAvailable(final Class<S> clazz) {
		return loadFirstAvailable(loadList(clazz));
	}

	/**
	 * 加载第一个可用服务，如果用户定义了多个接口实现类，只获取第一个不报错的服务
	 *
	 * @param <S>           服务类型
	 * @param serviceLoader {@link ServiceLoader}
	 * @return 第一个服务接口实现对象，无实现返回{@code null}
	 */
	public static <S> S loadFirstAvailable(final ServiceLoader<S> serviceLoader) {
		final Iterator<S> iterator = serviceLoader.iterator();
		while (iterator.hasNext()) {
			try {
				return iterator.next();
			} catch (final Throwable ignore) {
				// ignore
			}
		}
		return null;
	}

	/**
	 * 加载服务
	 *
	 * @param <T>   接口类型
	 * @param clazz 服务接口
	 * @return 服务接口实现列表
	 */
	public static <T> ServiceLoader<T> loadList(final Class<T> clazz) {
		return loadList(clazz, null);
	}

	/**
	 * 加载服务
	 *
	 * @param <T>    接口类型
	 * @param clazz  服务接口
	 * @param loader {@link ClassLoader}
	 * @return 服务接口实现列表
	 */
	public static <T> ServiceLoader<T> loadList(final Class<T> clazz, final ClassLoader loader) {
		return ListServiceLoader.of(clazz, loader);
	}
}
