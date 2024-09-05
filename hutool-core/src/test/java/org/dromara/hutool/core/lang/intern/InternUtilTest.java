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

package org.dromara.hutool.core.lang.intern;

import org.dromara.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InternUtilTest {

	/**
	 * 检查规范字符串是否相同
	 */
	@SuppressWarnings("StringOperationCanBeSimplified")
	@Test
	public void weakTest(){
		final Intern<String> intern = InternUtil.ofWeak();
		final String a1 = RandomUtil.randomStringLower(RandomUtil.randomInt(100));
		final String a2 = new String(a1);

		Assertions.assertNotSame(a1, a2);

		Assertions.assertSame(intern.intern(a1), intern.intern(a2));
	}

}
