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

package org.dromara.hutool.crypto.symmetric.fpe;

import org.dromara.hutool.core.util.RandomUtil;
import org.dromara.hutool.crypto.symmetric.FPE;
import org.bouncycastle.crypto.util.BasicAlphabetMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FPETest {

	@Test
	public void ff1Test(){
		// 映射字符表，规定了明文和密文的字符范围
		final BasicAlphabetMapper numberMapper = new BasicAlphabetMapper("A0123456789");
		// 初始化 aes 密钥
		final byte[] keyBytes = RandomUtil.randomBytes(16);

		final FPE fpe = new FPE(FPE.FPEMode.FF1, keyBytes, numberMapper, null);

		// 原始数据
		final String phone = RandomUtil.randomString("A0123456789", 13);
		final String encrypt = fpe.encrypt(phone);
		// 加密后与原密文长度一致
		Assertions.assertEquals(phone.length(), encrypt.length());

		final String decrypt = fpe.decrypt(encrypt);
		Assertions.assertEquals(phone, decrypt);
	}

	@Test
	public void ff3Test(){
		// 映射字符表，规定了明文和密文的字符范围
		final BasicAlphabetMapper numberMapper = new BasicAlphabetMapper("A0123456789");
		// 初始化 aes 密钥
		final byte[] keyBytes = RandomUtil.randomBytes(16);

		final FPE fpe = new FPE(FPE.FPEMode.FF3_1, keyBytes, numberMapper, null);

		// 原始数据
		final String phone = RandomUtil.randomString("A0123456789", 13);
		final String encrypt = fpe.encrypt(phone);
		// 加密后与原密文长度一致
		Assertions.assertEquals(phone.length(), encrypt.length());

		final String decrypt = fpe.decrypt(encrypt);
		Assertions.assertEquals(phone, decrypt);
	}
}
