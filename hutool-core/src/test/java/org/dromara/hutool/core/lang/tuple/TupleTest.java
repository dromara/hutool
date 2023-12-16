/*
 * Copyright (c) 2023. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.lang.tuple;

import org.dromara.hutool.core.lang.tuple.Tuple;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.TimeZone;

public class TupleTest {

	@Test
	public void hashCodeTest(){
		final Tuple tuple = new Tuple(Locale.getDefault(), TimeZone.getDefault());
		final Tuple tuple2 = new Tuple(Locale.getDefault(), TimeZone.getDefault());
		Assertions.assertEquals(tuple, tuple2);
	}
}
