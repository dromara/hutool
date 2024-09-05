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
