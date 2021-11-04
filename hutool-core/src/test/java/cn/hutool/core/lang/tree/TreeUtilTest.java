package cn.hutool.core.lang.tree;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TreeUtilTest {

	static List<TreeNode<String>> nodeList = CollUtil.newArrayList();
	static List<Tree<String>> treeList = new ArrayList<>();

	static {
		// 模拟数据

		nodeList.add(new TreeNode<>("001", "0", "菜单", 0));


		nodeList.add(new TreeNode<>("1", "001", "系统管理", 5));
		nodeList.add(new TreeNode<>("111", "11", "用户添加", 0));
		nodeList.add(new TreeNode<>("11", "1", "用户管理", 222222));

		nodeList.add(new TreeNode<>("2", "001", "店铺管理", 1));
		nodeList.add(new TreeNode<>("21", "2", "商品管理", 44));
		nodeList.add(new TreeNode<>("221", "2", "商品管理2", 2));
		treeList = TreeUtil.build(nodeList, "0", new TreeNodeConfig(), (treeNode, tree) -> {
			tree.setId(treeNode.getId());
			tree.setParentId(treeNode.getParentId());
			tree.setWeight(treeNode.getWeight());
			tree.setName(treeNode.getName());
		});
	}

	@Test
	public void testFindAllChildren() {
		List<String> allChildren = TreeUtil.findAllChildren(treeList, "001");
		Assert.assertArrayEquals(allChildren.toArray(), CollectionUtil.newArrayList("2","221","21","1","11","111").toArray());
	}
}
