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

package org.dromara.hutool.crypto.provider;

import org.dromara.hutool.core.spi.SpiUtil;
import org.dromara.hutool.crypto.SecureUtil;

import java.security.Provider;

/**
 * 全局单例的{@link Provider}对象<br>
 * 在此类加载时，通过SPI方式查找用户引入的加密库，查找对应的{@link Provider}实现，然后全局创建唯一的{@link Provider}对象<br>
 * 用户依旧可以通过{@link #setUseCustomProvider(boolean)} 方法选择是否使用自定义的Provider。
 *
 * @author looly
 */
public class GlobalProviderFactory {

	private static boolean useCustomProvider = true;
	private static final Provider provider = _createProvider();

	/**
	 * 获取{@link Provider}，无提供方，返回{@code null}表示使用JDK默认
	 *
	 * @return {@link Provider} or {@code null}
	 */
	public static Provider getProvider() {
		return useCustomProvider ? provider : null;
	}

	/**
	 * 设置是否使用自定义的{@link Provider}<br>
	 * 如果设置为false，表示使用JDK默认的Provider
	 *
	 * @param isUseCustomProvider 是否使用自定义{@link Provider}
	 */
	public static void setUseCustomProvider(final boolean isUseCustomProvider) {
		useCustomProvider = isUseCustomProvider;
	}

	/**
	 * 通过SPI方式，创建{@link Provider}，无提供的返回{@code null}
	 *
	 * @return {@link Provider} or {@code null}
	 */
	private static Provider _createProvider() {
		final ProviderFactory factory = SpiUtil.loadFirstAvailable(ProviderFactory.class);
		if (null == factory) {
			// 默认JCE
			return null;
		}

		final Provider provider = factory.create();
		// issue#2631@Github
		SecureUtil.addProvider(provider);
		return provider;
	}
}
