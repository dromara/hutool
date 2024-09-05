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

package org.dromara.hutool.core.text.dfa;

import org.dromara.hutool.core.collection.ListUtil;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class SensitiveUtilTest {

	@Test
	public void testSensitiveFilter() {
		final List<String> wordList = new ArrayList<>();
		wordList.add("大");
		wordList.add("大土豆");
		wordList.add("土豆");
		wordList.add("刚出锅");
		wordList.add("出锅");
		final TestBean bean = new TestBean();
		bean.setStr("我有一颗$大土^豆，刚出锅的");
		bean.setNum(100);
		SensitiveUtil.init(wordList);
		final String beanStr = SensitiveUtil.sensitiveFilter(bean.getStr(), true, null);
		Assertions.assertEquals("我有一颗$****，***的", beanStr);
	}

	@Data
	public static class TestBean {
		private String str;
		private Integer num;
	}

	@Test
	public void issue2126(){
		SensitiveUtil.init(ListUtil.view("赵", "赵阿", "赵阿三"));

		final String result = SensitiveUtil.sensitiveFilter("赵阿三在做什么。", true, null);
		Assertions.assertEquals("***在做什么。", result);
	}
}
