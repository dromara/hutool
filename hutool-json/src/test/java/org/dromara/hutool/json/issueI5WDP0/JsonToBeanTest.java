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

package org.dromara.hutool.json.issueI5WDP0;

import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsonToBeanTest {

	@Test
	void toBeanTest() {
		final String jsonStr = "{\"code\": \"201\", \"status\": \"ok\"}";
		final ERPProduct bean = JSONUtil.toBean(jsonStr, ERPProduct.class);
		Assertions.assertNotNull(bean);
		Assertions.assertEquals("201", bean.getCode());
		Assertions.assertEquals("ok", bean.getStatus());
	}
}
