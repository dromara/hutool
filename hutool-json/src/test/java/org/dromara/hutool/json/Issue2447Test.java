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

public class Issue2447Test {

	@Test
	public void addIntegerTest() {
		Time time = new Time();
		time.setTime(LocalDateTime.of(1970, 1, 2, 10, 0, 1, 0));
		String timeStr = JSONUtil.toJsonStr(time);
		Assertions.assertEquals(timeStr, "{\"time\":93601000}");
		Assertions.assertEquals(JSONUtil.toBean(timeStr, Time.class).getTime(), time.getTime());
	}

	@Data
	static class Time {
		private LocalDateTime time;

		@Override
		public String toString() {
			return time.toString();
		}
	}
}
