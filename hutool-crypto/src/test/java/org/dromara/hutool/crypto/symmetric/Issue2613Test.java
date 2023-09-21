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

package org.dromara.hutool.crypto.symmetric;

import org.dromara.hutool.crypto.Padding;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue2613Test {

	@Test
	public void aesGcmTest(){
		final AES aes = new AES("GCM", Padding.NoPadding.name(),
				"1234567890123456".getBytes(),
				"1234567890123456".getBytes());
		final String encryptHex = aes.encryptHex("123456");

		final String s = aes.decryptStr(encryptHex);
		Assertions.assertEquals("123456", s);
	}
}
