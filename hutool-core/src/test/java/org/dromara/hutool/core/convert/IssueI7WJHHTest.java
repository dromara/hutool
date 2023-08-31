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

package org.dromara.hutool.core.convert;

import org.dromara.hutool.core.lang.Opt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class IssueI7WJHHTest {
	@Test
	public void toIntTest() {
		final Optional<Integer> optional = Optional.of(1);
		final Integer integer = Convert.toInt(optional);

		Assertions.assertEquals(Integer.valueOf(1), integer);
	}

	@Test
	public void toIntTest2() {
		final Opt<Integer> optional = Opt.of(1);
		final Integer integer = Convert.toInt(optional);

		Assertions.assertEquals(Integer.valueOf(1), integer);
	}
}
