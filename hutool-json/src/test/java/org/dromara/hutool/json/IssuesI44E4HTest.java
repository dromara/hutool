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

import org.dromara.hutool.json.serialize.GlobalSerializeMapping;
import org.dromara.hutool.json.serialize.JSONDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 测试自定义反序列化
 */
public class IssuesI44E4HTest {

	@Test
	public void deserializerTest(){
		GlobalSerializeMapping.putDeserializer(TestDto.class, (JSONDeserializer<TestDto>) json -> {
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


