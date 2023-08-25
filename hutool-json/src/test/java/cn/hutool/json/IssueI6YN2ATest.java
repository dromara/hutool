/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.hutool.json;

import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.TypeReference;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

public class IssueI6YN2ATest {

	@Test
	public void toBeanTest() {
		final String str = "{\"conditions\":{\"user\":\"test\",\"sex\":\"男\"},\"headers\":{\"name\":\"姓名\",\"age\":\"年龄\",\"email\":\"邮箱\",\"number\":\"号码\",\"pwd\":\"密码\"}}";
		final JSONObject jsonObject = JSONUtil.parseObj(str);

		final PageQuery<?> bean = jsonObject.toBean(PageQuery.class);
		Assert.assertEquals("{name=姓名, age=年龄, email=邮箱, number=号码, pwd=密码}", bean.headers.toString());
	}

	@Data
	public static class PageQuery<T> {
		/**
		 * 要导出的字段
		 */
		private Map<String, String> headers = new LinkedHashMap<>();
		private T conditions;

		@Override
		public String toString() {
			return JSONUtil.parse(this, JSONConfig.create()).toJSONString(0);
		}
	}

	@Data
	public static class User {
		private String name;
		private String sex;
	}
}
