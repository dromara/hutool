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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Issue2377Test {
	@Test
	public void bytesTest() {
		final Object[] paramArray = new Object[]{1, new byte[]{10, 11}, "报表.xlsx"};
		final String paramsStr = JSONUtil.toJsonStr(paramArray);
		Assertions.assertEquals("[1,[10,11],\"报表.xlsx\"]", paramsStr);

		final List<Object> paramList = JSONUtil.toList(paramsStr, Object.class);

		final String paramBytesStr = JSONUtil.toJsonStr(paramList.get(1));
		Assertions.assertEquals("[10,11]", paramBytesStr);

		final byte[] paramBytes = JSONUtil.toBean(paramBytesStr, byte[].class);
		Assertions.assertArrayEquals((byte[]) paramArray[1], paramBytes);
	}
}
