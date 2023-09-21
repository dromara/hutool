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

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI50EGGTest {

	@Test
	public void toBeanTest(){
		final String data = "{\"return_code\": 1, \"return_msg\": \"成功\", \"return_data\" : null}";
		final ApiResult<?> apiResult = JSONUtil.toBean(data, JSONConfig.of().setIgnoreCase(true), ApiResult.class);
		Assertions.assertEquals(1, apiResult.getReturn_code());
	}

	@Data
	@AllArgsConstructor
	static class ApiResult<T>{
		private long Return_code;
		private String Return_msg;
		private T Return_data;
	}
}
