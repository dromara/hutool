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

import org.dromara.hutool.core.collection.ListUtil;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 问题反馈解析为null<br>
 * 此问题出在用户定义了String类型的List，parseArray时，Hutool无法知道这个String是普通值还是JSON字符串<br>
 * 就算知道也不能盲目解析，否则可能违背用户的初衷。<br>
 * 因此遇到List中的元素是String的情况下，用户需手动单独parse。
 */
public class IssueI1AU86Test {

	@Test
	public void toListTest() {
		final List<String> redisList = ListUtil.of(
				"{\"updateDate\":1583376342000,\"code\":\"move\",\"id\":1,\"sort\":1,\"name\":\"电影大全\"}",
				"{\"updateDate\":1583378882000,\"code\":\"zy\",\"id\":3,\"sort\":5,\"name\":\"综艺会\"}"
		);

		// 手动parse
		final JSONArray jsonArray = new JSONArray();
		for (final String str : redisList) {
			jsonArray.add(JSONUtil.parse(str));
		}

		final List<Vcc> vccs = jsonArray.toList(Vcc.class);
		for (final Vcc vcc : vccs) {
			Assertions.assertNotNull(vcc);
		}
	}

	@Data
	public static class Vcc implements Serializable {
		private static final long serialVersionUID = 1L;
		private Long id;
		private Date updateDate;
		private String code;
		private String name;
		private Integer sort;
	}
}
