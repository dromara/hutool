/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.crypto;

import org.dromara.hutool.crypto.provider.GlobalProviderFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class KeyUtilTest {

	/**
	 * 测试关闭BouncyCastle支持时是否会正常抛出异常，即关闭是否有效
	 */
	@Test
	@Disabled
	public void generateKeyPairTest() {
		Assertions.assertThrows(CryptoException.class, ()->{
			GlobalProviderFactory.setUseCustomProvider(false);
			final KeyPair pair = KeyUtil.generateKeyPair("SM2");
			Assertions.assertNotNull(pair);
		});
	}

	@Test
	public void getRSAPublicKeyTest(){
		final KeyPair keyPair = KeyUtil.generateKeyPair("RSA");
		final PrivateKey aPrivate = keyPair.getPrivate();
		final PublicKey rsaPublicKey = KeyUtil.getRSAPublicKey(aPrivate);
		Assertions.assertEquals(rsaPublicKey, keyPair.getPublic());
	}

	/**
	 * 测试EC和ECIES算法生成的KEY是一致的
	 */
	@Test
	public void generateECIESKeyTest(){
		final KeyPair ecies = KeyUtil.generateKeyPair("ECIES");
		Assertions.assertNotNull(ecies.getPrivate());
		Assertions.assertNotNull(ecies.getPublic());

		final byte[] privateKeyBytes = ecies.getPrivate().getEncoded();

		final PrivateKey privateKey = KeyUtil.generatePrivateKey("EC", privateKeyBytes);
		Assertions.assertEquals(ecies.getPrivate(), privateKey);
	}

	@Test
	public void generateDHTest(){
		final KeyPair dh = KeyUtil.generateKeyPair("DH");
		Assertions.assertNotNull(dh.getPrivate());
		Assertions.assertNotNull(dh.getPublic());

		final byte[] privateKeyBytes = dh.getPrivate().getEncoded();

		final PrivateKey privateKey = KeyUtil.generatePrivateKey("DH", privateKeyBytes);
		Assertions.assertEquals(dh.getPrivate(), privateKey);
	}

	@Test
	public void generateSm4KeyTest(){
		// https://github.com/dromara/hutool/issues/2150
		Assertions.assertEquals(16, KeyUtil.generateKey("sm4").getEncoded().length);
		Assertions.assertEquals(32, KeyUtil.generateKey("sm4", 256).getEncoded().length);
	}
}
