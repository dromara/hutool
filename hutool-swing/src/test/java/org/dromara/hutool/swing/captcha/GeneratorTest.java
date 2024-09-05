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

package org.dromara.hutool.swing.captcha;

import org.dromara.hutool.swing.captcha.generator.MathGenerator;
import org.junit.jupiter.api.Test;

public class GeneratorTest {

	@Test
	public void mathGeneratorTest() {
		final MathGenerator mathGenerator = new MathGenerator();
		for (int i = 0; i < 1000; i++) {
			mathGenerator.verify(mathGenerator.generate(), "0");
		}
	}
}
