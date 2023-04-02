package org.dromara.hutool.tree;

import org.dromara.hutool.collection.ListUtil;
import org.dromara.hutool.lang.Console;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用树测试
 *
 * @author liangbaikai
 */
public class TreeTest {
	// 模拟数据
	static List<TreeNode<String>> nodeList = ListUtil.of();

	static {
		// 模拟数据
		nodeList.add(new TreeNode<>("1", "0", "系统管理", 5));
		nodeList.add(new TreeNode<>("111", "11", "用户添加", 0));
		nodeList.add(new TreeNode<>("11", "1", "用户管理", 222222));

		nodeList.add(new TreeNode<>("2", "0", "店铺管理", 1));
		nodeList.add(new TreeNode<>("21", "2", "商品管理", 44));
		nodeList.add(new TreeNode<>("221", "2", "商品管理2", 2));
	}


	@Test
	public void sampleTreeTest() {
		final List<MapTree<String>> treeList = TreeUtil.build(nodeList, "0");
		for (final MapTree<String> tree : treeList) {
			Assertions.assertNotNull(tree);
			Assertions.assertEquals("0", tree.getParentId());
//			Console.log(tree);
		}

		// 测试通过子节点查找父节点
		final MapTree<String> rootNode0 = treeList.get(0);
		final MapTree<String> parent = rootNode0.getChildren().get(0).getParent();
		Assertions.assertEquals(rootNode0, parent);
	}

	@Test
	public void treeTest() {

		//配置
		final TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
		// 自定义属性名 都要默认值的
		treeNodeConfig.setWeightKey("order");
		treeNodeConfig.setIdKey("rid");
		treeNodeConfig.setDeep(2);

		//转换器
		final List<MapTree<String>> treeNodes = TreeUtil.build(nodeList, "0", treeNodeConfig,
				(treeNode, tree) -> {
					tree.setId(treeNode.getId());
					tree.setParentId(treeNode.getParentId());
					tree.setWeight(treeNode.getWeight());
					tree.setName(treeNode.getName());
					// 扩展属性 ...
					tree.putExtra("extraField", 666);
					tree.putExtra("other", new Object());
				});

		Assertions.assertEquals(treeNodes.size(), 2);
	}

	@Test
	public void walkTest(){
		final List<String> ids = new ArrayList<>();
		final MapTree<String> tree = TreeUtil.buildSingle(nodeList, "0");
		tree.walk((tr)-> ids.add(tr.getId()));

		Assertions.assertEquals(7, ids.size());
	}

	@Test
	public void walkBroadFirstTest(){
		final List<String> ids = new ArrayList<>();
		final MapTree<String> tree = TreeUtil.buildSingle(nodeList, "0");
		Console.log(tree);
		tree.walk((tr)-> ids.add(tr.getId()), true);

		Assertions.assertEquals(7, ids.size());
	}

	@Test
	public void cloneTreeTest(){
		final MapTree<String> tree = TreeUtil.buildSingle(nodeList, "0");
		final MapTree<String> cloneTree = tree.cloneTree();

		final List<String> ids = new ArrayList<>();
		cloneTree.walk((tr)-> ids.add(tr.getId()));

		Assertions.assertEquals(7, ids.size());
	}

	@Test
	public void filterTest(){
		// 经过过滤，丢掉"用户添加"节点
		final MapTree<String> tree = TreeUtil.buildSingle(nodeList, "0");
		tree.filter((t)->{
			final CharSequence name = t.getName();
			return null != name && name.toString().contains("店铺");
		});

		final List<String> ids = new ArrayList<>();
		tree.walk((tr)-> ids.add(tr.getId()));
		Assertions.assertEquals(4, ids.size());
	}

	@Test
	public void filterNewTest(){
		final MapTree<String> tree = TreeUtil.buildSingle(nodeList, "0");

		// 经过过滤，生成新的树
		final MapTree<String> newTree = tree.filterNew((t)->{
			final CharSequence name = t.getName();
			return null != name && name.toString().contains("店铺");
		});

		final List<String> ids = new ArrayList<>();
		newTree.walk((tr)-> ids.add(tr.getId()));
		Assertions.assertEquals(4, ids.size());

		final List<String> ids2 = new ArrayList<>();
		tree.walk((tr)-> ids2.add(tr.getId()));
		Assertions.assertEquals(7, ids2.size());
	}
}
