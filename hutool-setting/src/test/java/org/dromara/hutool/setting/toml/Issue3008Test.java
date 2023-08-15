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

package org.dromara.hutool.setting.toml;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.setting.props.Props;
import org.dromara.hutool.setting.props.PropsUtil;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue3008Test {
	/**
	 * 数组字段追加后生成新的数组，造成赋值丢失<br>
	 * 修复见：BeanUtil.setFieldValue
	 */
	@Test
	public void toBeanTest() {
		final Props props = PropsUtil.get("issue3008");
		final MyUser user = props.toBean(MyUser.class, "person");
		Assertions.assertEquals("[LOL, KFC, COFFE]", ArrayUtil.toString(user.getHobby()));
	}

	@Data
	static class MyUser {
		private String[] hobby;
	}
}
