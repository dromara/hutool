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

package org.dromara.hutool.json;

import org.dromara.hutool.core.bean.BeanUtil;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

/**
 * 测试带毫秒的日期转换
 */
public class IssueI3BS4STest {

	@Test
	public void toBeanTest(){
		final String jsonStr = "{date: '2021-03-17T06:31:33.99'}";
		final Bean1 bean1 = new Bean1();
		BeanUtil.copyProperties(JSONUtil.parseObj(jsonStr), bean1);
		Assertions.assertEquals("2021-03-17T06:31:33.099", bean1.getDate().toString());
	}

	@Data
	public static class Bean1{
		private LocalDateTime date;
	}
}
