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

package org.dromara.hutool.core.date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class Issue2612Test {

	@Test
	public void parseTest(){
		Assertions.assertEquals("2022-09-14 23:59:00",
				Objects.requireNonNull(DateUtil.parse("2022-09-14T23:59:00-08:00")).toString());

		Assertions.assertEquals("2022-09-14 23:59:00",
				Objects.requireNonNull(DateUtil.parse("2022-09-14T23:59:00-0800")).toString());
	}
}
