package cn.hutool.core.lang.tree;

import org.junit.Assert;
import org.junit.Test;

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
		List<Tree<Long>> treeItems=TreeUtil.build(all_menu, 0L);

		Tree<Long> tree=treeItems.get(0);
		Tree<Long> searchResult=tree.getNode(3L);

		Assert.assertEquals("module-B", searchResult.getName());
	}
}
