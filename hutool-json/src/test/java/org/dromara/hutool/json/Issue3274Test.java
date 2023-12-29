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

import lombok.Data;
import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue3274Test {
	@Test
	public void toBeanTest() {
		final JSONObject entries = new JSONObject("{\n" +
			"    \n" +
			"    \"age\": 36,\n" +
			"    \"gender\": \"\",\n" +
			"    \"id\": \"123123123\"\n" +
			"}", JSONConfig.of().setIgnoreError(true));
		final LarkCoreHrPersonal larkCoreHrPersonal = entries.toBean(LarkCoreHrPersonal.class);
		Assertions.assertNotNull(larkCoreHrPersonal);
	}

	@Data
	static class LarkCoreHrPersonal {
		private String id;
		private String age = "";
		private Gender gender;
	}

	@Getter
	enum Gender {
		male("male", "Male", "男"),
		female("female", "Female", "女"),
		other("other", "Other", "其他");
		private final JSONArray display;
		private final String enum_name;

		Gender(final String enum_name, final String en_Us, final String zh_CN) {
			this.enum_name = enum_name;
			this.display = new JSONArray("[{\"lang\": \"en-US\",\"value\": \"" + en_Us + "\"},{\"lang\": \"zh-CN\",\"value\": \"" + zh_CN + "\"}]");
		}
	}
}
