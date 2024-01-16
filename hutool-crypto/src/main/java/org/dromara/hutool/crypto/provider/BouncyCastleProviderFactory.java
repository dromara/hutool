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

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Provider;
import java.security.Security;

/**
 * {@link BouncyCastleProvider} 工厂类
 *
 * @author looly
 * @since 6.0.0
 */
public class BouncyCastleProviderFactory implements ProviderFactory {

	@Override
	public Provider create() {
		// pr#3464，Graalvm打包后注册会导致
		// Trying to verify a provider that was not registered at build time: BC version
		// All providers must be registered and verified in the Native Image builder
		// 因此此处对于已经注册过的Provider，直接复用之
		Provider provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);
		if(null == provider){
			provider = new BouncyCastleProvider();
		}

		return provider;
	}

}
