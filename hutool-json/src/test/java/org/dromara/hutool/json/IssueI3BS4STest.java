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
		Assertions.assertEquals("2021-03-17T06:31:33.990", bean1.getDate().toString());
	}

	@Data
	public static class Bean1{
		private LocalDateTime date;
	}
}
