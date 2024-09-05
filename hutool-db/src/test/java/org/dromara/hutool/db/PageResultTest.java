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

package org.dromara.hutool.db;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PageResultTest {

	@Test
	public void isLastTest(){
		// 每页2条，共10条，总共5页，第一页是0，最后一页应该是4
		final PageResult<String> result = new PageResult<>(4, 2, 10);
		Assertions.assertTrue(result.isLast());
	}
}
