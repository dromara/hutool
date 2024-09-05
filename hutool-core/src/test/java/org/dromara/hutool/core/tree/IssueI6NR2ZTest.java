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
