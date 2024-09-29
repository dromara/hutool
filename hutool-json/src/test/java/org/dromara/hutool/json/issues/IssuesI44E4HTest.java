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

package org.dromara.hutool.json.issues;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.dromara.hutool.json.serializer.JSONDeserializer;
import org.dromara.hutool.json.serializer.TypeAdapterManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 测试自定义反序列化
 */
public class IssuesI44E4HTest {

	@Test
	public void deserializerTest(){
		TypeAdapterManager.getInstance().register(TestDto.class, (JSONDeserializer<TestDto>) (json, deserializeType) -> {
			final TestDto testDto = new TestDto();
			testDto.setMd(new AcBizModuleMd("name1", ((JSONObject)json).getStr("md")));
			return testDto;
		});

		final String jsonStr = "{\"md\":\"value1\"}";
		final TestDto testDto = JSONUtil.toBean(jsonStr, TestDto.class);
		Assertions.assertEquals("value1", testDto.getMd().getValue());
	}

	@Getter
	@Setter
	@AllArgsConstructor
	public static class AcBizModuleMd {
		private String name;
		private String value;
		// 值列表
		public static final AcBizModuleMd Value1 = new AcBizModuleMd("value1", "name1");
		public static final AcBizModuleMd Value2 = new AcBizModuleMd("value2", "name2");
		public static final AcBizModuleMd Value3 = new AcBizModuleMd("value3", "name3");
	}

	@Getter
	@Setter
	public static class TestDto {
		private AcBizModuleMd md;
	}
}


