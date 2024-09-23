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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * JSON路径单元测试
 *
 * @author looly
 *
 */
public class JSONPathTest {

	@Test
	public void getByPathTest() {
		final String json = "[{\"id\":\"1\",\"name\":\"xingming\"},{\"id\":\"2\",\"name\":\"mingzi\"}]";
		Object value = JSONUtil.parseArray(json).getByPath("[0].name", Object.class);
		Assertions.assertEquals("xingming", value);
		value = JSONUtil.parseArray(json).getByPath("[1].name", Object.class);
		Assertions.assertEquals("mingzi", value);
	}

	@Test
	public void getByPathTest2(){
		final String str = "{'accountId':111}";
		final JSON json = JSONUtil.parse(str);
		final Long accountId = JSONUtil.getByPath(json, "$.accountId", 0L);
		Assertions.assertEquals(111L, accountId.longValue());
	}
}
