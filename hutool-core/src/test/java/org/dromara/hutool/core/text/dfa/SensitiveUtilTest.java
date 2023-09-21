/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
