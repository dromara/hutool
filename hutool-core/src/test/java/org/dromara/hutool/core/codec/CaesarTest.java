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

package org.dromara.hutool.core.codec;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CaesarTest {

	@Test
	public void caesarTest() {
		final String str = "1f2e9df6131b480b9fdddc633cf24996";

		final String encode = Caesar.encode(str, 3);
		Assertions.assertEquals("1H2G9FH6131D480D9HFFFE633EH24996", encode);

		final String decode = Caesar.decode(encode, 3);
		Assertions.assertEquals(str, decode);
	}
}
