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

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.io.resource.ResourceUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Predicate多层过滤
 */
public class IssueI5OMSCTest {

	@Test
	public void filterTest(){
		final JSONObject json = JSONUtil.parseObj(ResourceUtil.readUtf8Str("issueI5OMSC.json"));

		final String s = json.toJSONString(0, (entry) -> {
			final Object key = entry.getKey();
			if(key instanceof String){
				return ListUtil.of("store", "bicycle", "color", "book", "author").contains(key);
			}
			return true;
		});
		Assertions.assertEquals("{\"store\":{\"bicycle\":{\"color\":\"red\"},\"book\":[{\"author\":\"Evelyn Waugh\"},{\"author\":\"Evelyn Waugh02\"}]}}", s);
	}
}
