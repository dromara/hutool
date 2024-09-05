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

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.crypto.asymmetric.paillier.PaillierCrypto;
import org.dromara.hutool.crypto.asymmetric.paillier.PaillierKeyPairGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGeneratorSpi;

public class PaillierTest {
	@Test
	void keyPairGeneratorTest() {
		final PaillierKeyPairGenerator generator = PaillierKeyPairGenerator.of();
		final KeyPair keyPair = generator.generateKeyPair();
		Assertions.assertNotNull(keyPair.getPrivate());
		Assertions.assertNotNull(keyPair.getPublic());
	}

	@Test
	void keyPairGeneratorByJceTest() {
		final KeyPairGeneratorSpi generator = PaillierKeyPairGenerator.of();
		final KeyPair keyPair = generator.generateKeyPair();
		Assertions.assertNotNull(keyPair.getPrivate());
		Assertions.assertNotNull(keyPair.getPublic());
	}

	@Test
	void encryptAndDecryptTest() {
		final String text = "Hutool测试字符串";

		final PaillierCrypto crypto = new PaillierCrypto();
		final byte[] encrypt = crypto.encrypt(text, KeyType.PublicKey);

		final byte[] decrypt = crypto.decrypt(encrypt, KeyType.PrivateKey);
		Assertions.assertEquals(text, StrUtil.utf8Str(decrypt));
	}
}
