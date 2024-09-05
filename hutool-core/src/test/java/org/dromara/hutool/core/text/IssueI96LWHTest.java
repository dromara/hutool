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

public class IssueI96LWHTest {

	@Test
	public void replaceByCodePointTest() {
		final String str = "\uD83D\uDC46最上方点击蓝字";

		// 这个方法里\uD83D\uDC46表示一个emoji表情，使用codePoint之后，一个表情表示一个字符，因此按照一个字符对
		Assertions.assertEquals("\uD83D\uDC46最上下点击蓝字", StrUtil.replaceByCodePoint(str, 3, 4, "下"));
		Assertions.assertEquals("\uD83D\uDC46最下方点击蓝字", new StringBuilder(str).replace(3, 4, "下").toString());
	}
}
