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

package cn.hutool.crypto;

import java.security.Provider;

/**
 * 全局单例的 org.bouncycastle.jce.provider.BouncyCastleProvider 对象
 *
 * @author looly
 */
public enum GlobalBouncyCastleProvider {
	/**
	 * 单例对象
	 */
	INSTANCE;

	private Provider provider;
	private static boolean useBouncyCastle = true;

	GlobalBouncyCastleProvider() {
		try {
			this.provider = ProviderFactory.createBouncyCastleProvider();
		} catch (final NoClassDefFoundError | NoSuchMethodError  e) {
			// ignore
		}
	}

	/**
	 * 获取{@link Provider}
	 *
	 * @return {@link Provider}
	 */
	public Provider getProvider() {
		return useBouncyCastle ? this.provider : null;
	}

	/**
	 * 设置是否使用Bouncy Castle库<br>
	 * 如果设置为false，表示强制关闭Bouncy Castle而使用JDK
	 *
	 * @param isUseBouncyCastle 是否使用BouncyCastle库
	 * @since 4.5.2
	 */
	public static void setUseBouncyCastle(final boolean isUseBouncyCastle) {
		useBouncyCastle = isUseBouncyCastle;
	}
}
