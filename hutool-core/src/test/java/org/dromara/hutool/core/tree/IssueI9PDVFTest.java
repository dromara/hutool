/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
