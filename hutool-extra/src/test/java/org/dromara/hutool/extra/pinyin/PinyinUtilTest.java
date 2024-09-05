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

package org.dromara.hutool.extra.pinyin;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PinyinUtilTest {

	@Test
	public void getPinyinTest(){
		final String pinyin = PinyinUtil.getPinyin("你好怡", " ");
		Assertions.assertEquals("ni hao yi", pinyin);
	}

	@Test
	public void getFirstLetterTest(){
		final String result = PinyinUtil.getFirstLetter("H是第一个", ", ");
		Assertions.assertEquals("h, s, d, y, g", result);
	}

	@Test
	public void getFirstLetterTest2(){
		final String result = PinyinUtil.getFirstLetter("崞阳", ", ");
		Assertions.assertEquals("g, y", result);
	}
}
