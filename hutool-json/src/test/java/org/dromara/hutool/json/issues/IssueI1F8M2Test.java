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

import lombok.Data;
import org.dromara.hutool.json.JSONUtil;
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
