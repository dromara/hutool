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

package org.dromara.hutool.crypto.digest;

import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.thread.ConcurrencyTester;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * MD5 单元测试
 *
 * @author Looly
 *
 */
public class Md5Test {

	@Test
	public void md5To16Test() {
		final String hex16 = MD5.of().digestHex16("中国");
		Assertions.assertEquals(16, hex16.length());
		Assertions.assertEquals("cb143acd6c929826", hex16);
	}

	@Test
	void md5ThreadSafeTest() {
		final String text = "Hutool md5 test str";
		final ConcurrencyTester tester = new ConcurrencyTester(1000);
		tester.test(()->{
			final String digest = MD5.of().digestHex(text);
			Assertions.assertEquals("8060075dd8df47bac3247438e940a728", digest);
		});
		IoUtil.closeQuietly(tester);
	}

	@Test
	void md5ThreadSafeTest2() {
		final String text = "Hutool md5 test str";
		final ConcurrencyTester tester = new ConcurrencyTester(1000);
		tester.test(()->{
			final String digest = new Digester("MD5").digestHex(text);
			Assertions.assertEquals("8060075dd8df47bac3247438e940a728", digest);
		});
		IoUtil.closeQuietly(tester);
	}
}
