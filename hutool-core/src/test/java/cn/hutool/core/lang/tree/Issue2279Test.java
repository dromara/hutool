package cn.hutool.core.lang.tree;

import cn.hutool.core.collection.ListUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class Issue2279Test {

	@Test
	public void buildSingleTest() {
		List<TestTree> list = ListUtil.of(
				// 模拟数据
				new TestTree(1, 0, 1, 1),
				new TestTree(2, 1, 2, 2),
				new TestTree(3, 1, 3, 3),
				new TestTree(4, 2, 4, 4)
		);

		List<Tree<String>> stringTree = TreeUtil.build(list, "0",
				(object, treeNode) -> {
					treeNode.setId(object.getId());
					treeNode.setName(object.getName());
					treeNode.setParentId(object.getPid());
					treeNode.putExtra("extra1",object.getExtra1());
				}
		);

		final Tree<String> result = stringTree.get(0);
		Assert.assertEquals(2, result.getChildren().size());
	}

	@Data
	static class TestTree {
		private String id;
		private String pid;
		private String name;
		private String extra1;

		public TestTree(int id, int pid, int name, int extra1) {
			this.id = String.valueOf(id);
			this.pid = String.valueOf(pid);
			this.name = String.valueOf(name);
			this.extra1 = String.valueOf(extra1);
		}
	}
}
