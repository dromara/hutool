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

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JSONSupportTest {

	/**
	 * https://github.com/dromara/hutool/issues/1779
	 * 在JSONSupport的JSONBeanParse中，如果使用json.toBean，会导致JSONBeanParse.parse方法反复递归调用，最终栈溢出<br>
	 * 因此parse方法默认实现必须避开JSONBeanParse.parse调用。
	 */
	@Test
	public void parseTest() {
		final String jsonstr = "{\n" +
				"    \"location\": \"https://hutool.cn\",\n" +
				"    \"message\": \"这是一条测试消息\",\n" +
				"    \"requestId\": \"123456789\",\n" +
				"    \"traceId\": \"987654321\"\n" +
				"}";


		final TestBean testBean = JSONUtil.toBean(jsonstr, TestBean.class);
		Assertions.assertEquals("https://hutool.cn", testBean.getLocation());
		Assertions.assertEquals("这是一条测试消息", testBean.getMessage());
		Assertions.assertEquals("123456789", testBean.getRequestId());
		Assertions.assertEquals("987654321", testBean.getTraceId());
	}

	@EqualsAndHashCode(callSuper = true)
	@Data
	static class TestBean  extends JSONSupport{

		private String location;

		private String message;

		private String requestId;

		private String traceId;

	}
}
