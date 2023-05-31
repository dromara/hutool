/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
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

public class IssueI795INTest {

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
	void getParentsNameTest() {
		final MapTree<Long> tree = TreeUtil.buildSingle(all_menu, 0L);
		final MapTree<Long> chid = tree.getChildren().get(0).getChildren().get(0).getChildren().get(0);
		Assertions.assertEquals("[module-A-menu-1, module-A, root]", chid.getParentsName(true).toString());
	}
}
