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

/**
 * https://gitee.com/dromara/hutool/issues/I4RBZ4
 */
public class IssueI4RBZ4Test {

	@Test
	public void sortTest(){
		final String jsonStr = "{\"id\":\"123\",\"array\":[1,2,3],\"outNum\":356,\"body\":{\"ava1\":\"20220108\",\"use\":1,\"ava2\":\"20230108\"},\"name\":\"John\"}";

		final JSONObject jsonObject = JSONUtil.parseObj(jsonStr, JSONConfig.of().setNatureKeyComparator());
		Assertions.assertEquals("{\"array\":[1,2,3],\"body\":{\"ava1\":\"20220108\",\"ava2\":\"20230108\",\"use\":1},\"id\":\"123\",\"name\":\"John\",\"outNum\":356}", jsonObject.toString());
	}
}
