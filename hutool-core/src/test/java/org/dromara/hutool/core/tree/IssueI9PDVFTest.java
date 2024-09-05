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

package org.dromara.hutool.core.tree;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.dromara.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class IssueI9PDVFTest {
	@Test
	public void buildTest() {
		final List<TestList> list = new ArrayList<>();
		list.add(new TestList(1790187987502895123L, "顶级", 0L));
		list.add(new TestList(1790187987502895124L, "子集", 1790187987502895123L));

		final List<MapTree<String>> build = TreeUtil.build(list, "0", (testList, treeNode) -> {
			treeNode.setId(testList.getId().toString());
			treeNode.setName(testList.getName());
			treeNode.setParentId(testList.getParentId().toString());
		});

		Assert.notNull(build);
	}

	@AllArgsConstructor
	@Data
	public static class TestList {
		Long id;
		String name;
		Long parentId;
	}
}
