package cn.hutool.core.lang.tree;

import cn.hutool.core.collection.CollUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用树测试
 *
 * @author liangbaikai
 */
public class TreeTest {
	// 模拟数据
	static List<TreeNode<String>> nodeList = CollUtil.newArrayList();

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
		List<Tree<String>> treeList = TreeUtil.build(nodeList, "0");
		for (Tree<String> tree : treeList) {
			Assert.assertNotNull(tree);
			Assert.assertEquals("0", tree.getParentId());
//			Console.log(tree);
		}

		// 测试通过子节点查找父节点
		final Tree<String> rootNode0 = treeList.get(0);
		final Tree<String> parent = rootNode0.getChildren().get(0).getParent();
		Assert.assertEquals(rootNode0, parent);
	}

	@Test
	public void treeTest() {

		//配置
		TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
		// 自定义属性名 都要默认值的
		treeNodeConfig.setWeightKey("order");
		treeNodeConfig.setIdKey("rid");
		treeNodeConfig.setDeep(2);

		//转换器
		List<Tree<String>> treeNodes = TreeUtil.build(nodeList, "0", treeNodeConfig,
				(treeNode, tree) -> {
					tree.setId(treeNode.getId());
					tree.setParentId(treeNode.getParentId());
					tree.setWeight(treeNode.getWeight());
					tree.setName(treeNode.getName());
					// 扩展属性 ...
					tree.putExtra("extraField", 666);
					tree.putExtra("other", new Object());
				});

		Assert.assertEquals(treeNodes.size(), 2);
	}

	@Test
	public void walkTest(){
		List<String> ids = new ArrayList<>();
		final Tree<String> tree = TreeUtil.buildSingle(nodeList, "0");
		tree.walk((tr)-> ids.add(tr.getId()));

		Assert .assertEquals(7, ids.size());
	}

	@Test
	public void cloneTreeTest(){
		final Tree<String> tree = TreeUtil.buildSingle(nodeList, "0");
		final Tree<String> cloneTree = tree.cloneTree();

		List<String> ids = new ArrayList<>();
		cloneTree.walk((tr)-> ids.add(tr.getId()));

		Assert .assertEquals(7, ids.size());
	}

	@Test
	public void filterTest(){
		// 经过过滤，丢掉"用户添加"节点
		final Tree<String> tree = TreeUtil.buildSingle(nodeList, "0");
		tree.filter((t)->{
			final CharSequence name = t.getName();
			return null != name && name.toString().contains("店铺");
		});

		List<String> ids = new ArrayList<>();
		tree.walk((tr)-> ids.add(tr.getId()));
		Assert .assertEquals(4, ids.size());
	}

	@Test
	public void filterNewTest(){
		final Tree<String> tree = TreeUtil.buildSingle(nodeList, "0");

		// 经过过滤，生成新的树
		Tree<String> newTree = tree.filterNew((t)->{
			final CharSequence name = t.getName();
			return null != name && name.toString().contains("店铺");
		});

		List<String> ids = new ArrayList<>();
		newTree.walk((tr)-> ids.add(tr.getId()));
		Assert .assertEquals(4, ids.size());

		List<String> ids2 = new ArrayList<>();
		tree.walk((tr)-> ids2.add(tr.getId()));
		Assert .assertEquals(7, ids2.size());
	}
}
