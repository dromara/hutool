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

public class IssueI59LW4Test {
	@Test
	public void bytesTest(){
		final JSONObject jsonObject = JSONUtil.ofObj().set("bytes", new byte[]{1});
		Assertions.assertEquals("{\"bytes\":[1]}", jsonObject.toString());

		final byte[] bytes = jsonObject.getBytes("bytes");
		Assertions.assertArrayEquals(new byte[]{1}, bytes);
	}

	@Test
	public void bytesInJSONArrayTest(){
		final JSONArray jsonArray = JSONUtil.ofArray().set(new byte[]{1});
		Assertions.assertEquals("[[1]]", jsonArray.toString());

		final byte[] bytes = jsonArray.getBytes(0);
		Assertions.assertArrayEquals(new byte[]{1}, bytes);
	}
}
