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

package org.dromara.hutool.core.collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.dromara.hutool.core.collection.set.UniqueKeySet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class UniqueKeySetTest {

	@Test
	public void addTest(){
		final Set<UniqueTestBean> set = new UniqueKeySet<>(UniqueTestBean::getId);
		set.add(new UniqueTestBean("id1", "张三", "地球"));
		set.add(new UniqueTestBean("id2", "李四", "火星"));
		// id重复，替换之前的元素
		set.add(new UniqueTestBean("id2", "王五", "木星"));

		// 后两个ID重复
		Assertions.assertEquals(2, set.size());
	}

	@Data
	@AllArgsConstructor
	static class UniqueTestBean{
		private String id;
		private String name;
		private String address;
	}
}
