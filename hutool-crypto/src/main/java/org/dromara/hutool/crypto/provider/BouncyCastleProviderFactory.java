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

package org.dromara.hutool.crypto.provider;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.dromara.hutool.core.lang.Assert;

import java.security.Provider;
import java.security.Security;

/**
 * {@link BouncyCastleProvider} 工厂类
 *
 * @author looly
 * @since 6.0.0
 */
public class BouncyCastleProviderFactory implements ProviderFactory {

	/**
	 * 构造
	 */
	public BouncyCastleProviderFactory(){
		// SPI方式加载时检查BC库是否引入
		Assert.notNull(BouncyCastleProvider.class);
	}

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
