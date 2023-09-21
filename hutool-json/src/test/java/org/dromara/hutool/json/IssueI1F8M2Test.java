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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

/**
 * https://gitee.com/loolly/dashboard/issues?id=I1F8M2
 */
public class IssueI1F8M2Test {
	@Test
	public void toBeanTest() {
		final String jsonStr = "{\"eventType\":\"fee\",\"fwdAlertingTime\":\"2020-04-22 16:34:13\",\"fwdAnswerTime\":\"\"}";
		final Param param = JSONUtil.toBean(jsonStr, Param.class);
		Assertions.assertEquals("2020-04-22T16:34:13", param.getFwdAlertingTime().toString());
		Assertions.assertNull(param.getFwdAnswerTime());
	}

	// Param类的字段
	@Data
	static class Param {
		/**
		 * fee表示话单事件
		 */
		private String eventType;
		/**
		 * 转接呼叫后振铃时间
		 */
		private LocalDateTime fwdAlertingTime;
		/**
		 * 转接呼叫后应答时间
		 */
		private LocalDateTime fwdAnswerTime;

	}
}
