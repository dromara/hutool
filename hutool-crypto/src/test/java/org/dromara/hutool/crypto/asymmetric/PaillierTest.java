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

package org.dromara.hutool.crypto.asymmetric;

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.crypto.asymmetric.paillier.PaillierCrypto;
import org.dromara.hutool.crypto.asymmetric.paillier.PaillierKeyPairGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class PaillierTest {
	@Test
	void keyPairGeneratorTest() {
		final PaillierKeyPairGenerator generator = PaillierKeyPairGenerator.of();
		final KeyPair keyPair = generator.generateKeyPair();
		Assertions.assertNotNull(keyPair.getPrivate());
		Assertions.assertNotNull(keyPair.getPublic());
	}

	@Test
	void keyPairGeneratorByJceTest() throws NoSuchAlgorithmException {
		final KeyPairGenerator generator = KeyPairGenerator.getInstance("Paillier");
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
