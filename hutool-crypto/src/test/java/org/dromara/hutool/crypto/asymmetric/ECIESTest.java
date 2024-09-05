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

package org.dromara.hutool.crypto.asymmetric;

import org.bouncycastle.jce.spec.IESParameterSpec;
import org.dromara.hutool.core.text.StrUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ECIESTest {

	@Test
	public void eciesTest(){
		final ECIES ecies = new ECIES();
		final IESParameterSpec iesParameterSpec = new IESParameterSpec(null, null, 128);
		ecies.setAlgorithmParameterSpec(iesParameterSpec);

		doTest(ecies, ecies);
	}

	@Test
	public void eciesTest2(){
		final IESParameterSpec iesParameterSpec = new IESParameterSpec(null, null, 128);

		final ECIES ecies = new ECIES();
		ecies.setAlgorithmParameterSpec(iesParameterSpec);

		final byte[] privateKeyBytes = ecies.getPrivateKey().getEncoded();
		final ECIES ecies2 = new ECIES(privateKeyBytes, null);
		ecies2.setAlgorithmParameterSpec(iesParameterSpec);


		doTest(ecies, ecies2);
	}

	/**
	 * 测试用例
	 *
	 * @param cryptoForEncrypt 加密的Crypto
	 * @param cryptoForDecrypt 解密的Crypto
	 */
	private void doTest(final AsymmetricCrypto cryptoForEncrypt, final AsymmetricCrypto cryptoForDecrypt){
		final String textBase = "我是一段特别长的测试";
		final StringBuilder text = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			text.append(textBase);
		}

		// 公钥加密，私钥解密
		final String encryptStr = cryptoForEncrypt.encryptBase64(text.toString(), KeyType.PublicKey);

		final String decryptStr = StrUtil.utf8Str(cryptoForDecrypt.decrypt(encryptStr, KeyType.PrivateKey));
		Assertions.assertEquals(text.toString(), decryptStr);
	}
}
