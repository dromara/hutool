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

package org.dromara.hutool.core.tree;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class IssueI6NR2ZTest {
	@Test
	public void getNodeTest() {
		final List<TreeNode<Integer>> list = new ArrayList<>();

		final TreeNode<Integer> treeNode1 = new TreeNode<>();
		treeNode1.setId(1);
		treeNode1.setParentId(0);
		list.add(treeNode1);

		final TreeNode<Integer> treeNode2 = new TreeNode<>();
		treeNode2.setId(2);
		treeNode2.setParentId(1);
		list.add(treeNode2);

		final TreeNode<Integer> treeNode3 = new TreeNode<>();
		treeNode3.setId(3);
		treeNode3.setParentId(1);
		list.add(treeNode3);

		final TreeNode<Integer> treeNode4 = new TreeNode<>();
		treeNode4.setId(21);
		treeNode4.setParentId(2);
		list.add(treeNode4);

		final TreeNode<Integer> treeNode5 = new TreeNode<>();
		treeNode5.setId(31);
		treeNode5.setParentId(3);
		list.add(treeNode5);

		final TreeNode<Integer> treeNode6 = new TreeNode<>();
		treeNode6.setId(211);
		treeNode6.setParentId(21);
		list.add(treeNode6);

		final List<MapTree<Integer>> build = TreeUtil.build(list);
		final MapTree<Integer> node = TreeUtil.getNode(build.get(0), 31);
		Assertions.assertNotNull(node);
	}
}
