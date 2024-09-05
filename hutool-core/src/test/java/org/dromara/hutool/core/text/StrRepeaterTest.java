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

package org.dromara.hutool.core.text;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StrRepeaterTest {

	@Test
	public void repeatByLengthTest() {
		// 如果指定长度非指定字符串的整数倍，截断到固定长度
		final String ab = StrRepeater.of(5).repeatByLength("ab");
		Assertions.assertEquals("ababa", ab);
	}

	@Test
	public void repeatByLengthTest2() {
		// 如果指定长度小于字符串本身的长度，截断之
		final String ab = StrRepeater.of(2).repeatByLength("abcde");
		Assertions.assertEquals("ab", ab);
	}
}
