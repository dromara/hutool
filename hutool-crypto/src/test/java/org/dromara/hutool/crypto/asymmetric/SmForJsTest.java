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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 单元测试来自：
 * https://github.com/AllenDengMs/sm2_encrypt/blob/main/src/main/java/demo/SmTest.java
 */
public class SmForJsTest {

	@Test
	void name() {
		final String data = "Test Data";

		// 配合JS版使用：https://github.com/JuneAndGreen/sm-crypto
		final String publicKeyFromJS = "04c7a0c75f752a3a65498f7d3b6cab912d0cbe72aa6807ee675a1dd14f3149fe416e9a9c31e4d032a6cc9585b62f1d2a98f2090187ea83b24e8a4ab881a5424383";
		final String privateKeyFromJS = "8c30bed6088fa995e10db01700b5fb22591757aca1dea9fc20b8ecf89bb68938";

		final SM2 sm2 = new SM2(privateKeyFromJS, publicKeyFromJS);
		final String s = sm2.encryptBase64(data, KeyType.PublicKey);
		final String s1 = sm2.decryptStr(s, KeyType.PrivateKey);

		assertEquals(data, s1);
	}
}
