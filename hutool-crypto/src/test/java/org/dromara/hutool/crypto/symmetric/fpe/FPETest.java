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
