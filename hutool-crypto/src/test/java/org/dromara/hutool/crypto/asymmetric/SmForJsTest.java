/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
