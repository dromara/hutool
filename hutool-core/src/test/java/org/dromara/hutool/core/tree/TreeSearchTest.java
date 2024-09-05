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

public class TreeSearchTest {
	static List<TreeNode<Long>> all_menu=new ArrayList<>();
	static {
		/*
		 * root
		 *    /module-A
		 *    	   /module-A-menu-1
		 *    /module-B
		 *    	   /module-B-menu-1
		 *    	   /module-B-menu-2
		 */
		all_menu.add(new TreeNode<>(1L, 0L, "root", 0L));
		all_menu.add(new TreeNode<>(2L,1L,"module-A",0L));
		all_menu.add(new TreeNode<>(3L,1L,"module-B",0L));
		all_menu.add(new TreeNode<>(4L,2L,"module-A-menu-1",0L));
		all_menu.add(new TreeNode<>(5L,3L,"module-B-menu-1",0L));
		all_menu.add(new TreeNode<>(6L,3L,"module-B-menu-2",0L));
	}

	@Test
	public void searchNode() {
		final List<MapTree<Long>> treeItems= TreeUtil.build(all_menu, 0L);

		final MapTree<Long> tree=treeItems.get(0);
		final MapTree<Long> searchResult=tree.getNode(3L);

		Assertions.assertEquals("module-B", searchResult.getName());
	}
}
