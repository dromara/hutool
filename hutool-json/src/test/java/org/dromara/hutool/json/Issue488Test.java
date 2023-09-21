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

import org.dromara.hutool.core.io.resource.ResourceUtil;
import org.dromara.hutool.core.reflect.TypeReference;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Issue488Test {

	@Test
	public void toBeanTest() {
		final String jsonStr = ResourceUtil.readUtf8Str("issue488.json");

		final ResultSuccess<List<EmailAddress>> result = JSONUtil.toBean(jsonStr, JSONConfig.of(),
				new TypeReference<ResultSuccess<List<EmailAddress>>>() {});

		Assertions.assertEquals("https://graph.microsoft.com/beta/$metadata#Collection(microsoft.graph.emailAddress)", result.getContext());

		final List<EmailAddress> adds = result.getValue();
		Assertions.assertEquals("会议室101", adds.get(0).getName());
		Assertions.assertEquals("MeetingRoom101@abc.com", adds.get(0).getAddress());
		Assertions.assertEquals("会议室102", adds.get(1).getName());
		Assertions.assertEquals("MeetingRoom102@abc.com", adds.get(1).getAddress());
		Assertions.assertEquals("会议室103", adds.get(2).getName());
		Assertions.assertEquals("MeetingRoom103@abc.com", adds.get(2).getAddress());
		Assertions.assertEquals("会议室219", adds.get(3).getName());
		Assertions.assertEquals("MeetingRoom219@abc.com", adds.get(3).getAddress());
	}

	@Test
	public void toCollctionBeanTest() {
		final String jsonStr = ResourceUtil.readUtf8Str("issue488Array.json");

		final List<ResultSuccess<List<EmailAddress>>> resultList = JSONUtil.toBean(jsonStr, JSONConfig.of(),
				new TypeReference<List<ResultSuccess<List<EmailAddress>>>>() {});

		final ResultSuccess<List<EmailAddress>> result = resultList.get(0);

		Assertions.assertEquals("https://graph.microsoft.com/beta/$metadata#Collection(microsoft.graph.emailAddress)", result.getContext());

		final List<EmailAddress> adds = result.getValue();
		Assertions.assertEquals("会议室101", adds.get(0).getName());
		Assertions.assertEquals("MeetingRoom101@abc.com", adds.get(0).getAddress());
		Assertions.assertEquals("会议室102", adds.get(1).getName());
		Assertions.assertEquals("MeetingRoom102@abc.com", adds.get(1).getAddress());
		Assertions.assertEquals("会议室103", adds.get(2).getName());
		Assertions.assertEquals("MeetingRoom103@abc.com", adds.get(2).getAddress());
		Assertions.assertEquals("会议室219", adds.get(3).getName());
		Assertions.assertEquals("MeetingRoom219@abc.com", adds.get(3).getAddress());
	}

	@Data
	public static class ResultSuccess<T> {
		private String context;
		private T value;
	}

	@Data
	public static class EmailAddress {
		private String name;
		private String address;
	}
}
