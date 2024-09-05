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

public class RotTest {

	@Test
	public void rot13Test() {
		final String str = "1f2e9df6131b480b9fdddc633cf24996";

		final String encode13 = Rot.encode13(str);
		Assertions.assertEquals("4s5r2qs9464o713o2sqqqp966ps57229", encode13);

		final String decode13 = Rot.decode13(encode13);
		Assertions.assertEquals(str, decode13);
	}
}
