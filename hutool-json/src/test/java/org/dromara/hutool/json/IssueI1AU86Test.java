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
