package org.dromara.hutool.core.tree;

import org.dromara.hutool.core.collection.ListUtil;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Issue2279Test {

	@Test
	public void buildSingleTest() {
		final List<TestTree> list = ListUtil.view(
				// 模拟数据
				new TestTree(1, 0, 1, 1),
				new TestTree(2, 1, 2, 2),
				new TestTree(3, 1, 3, 3),
				new TestTree(4, 2, 4, 4)
		);

		final List<MapTree<String>> stringTree = TreeUtil.build(list, "0",
				(object, treeNode) -> {
					treeNode.setId(object.getId());
					treeNode.setName(object.getName());
					treeNode.setParentId(object.getPid());
					treeNode.putExtra("extra1",object.getExtra1());
				}
		);

		final MapTree<String> result = stringTree.get(0);
		Assertions.assertEquals(2, result.getChildren().size());
	}

	@Data
	static class TestTree {
		private String id;
		private String pid;
		private String name;
		private String extra1;

		public TestTree(final int id, final int pid, final int name, final int extra1) {
			this.id = String.valueOf(id);
			this.pid = String.valueOf(pid);
			this.name = String.valueOf(name);
			this.extra1 = String.valueOf(extra1);
		}
	}
}
