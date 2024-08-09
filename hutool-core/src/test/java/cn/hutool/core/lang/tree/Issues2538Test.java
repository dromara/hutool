package cn.hutool.core.lang.tree;

import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Issues2538Test {

	@Test
	public void issues2538Test() {
		TestBean test1 = new TestBean();
		test1.setId(1);
		test1.setParentId(0);
		test1.setName("1");
		TestBean test2 = new TestBean();
		test2.setId(2);
		test2.setParentId(1);
		test2.setName("1");

		List<TestBean> list = new ArrayList<>();

		list.add(test1);
		list.add(test2);
		// 配置
		TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
		// 自定义属性名 都要默认值的
		treeNodeConfig.setIdKey("id");
		treeNodeConfig.setDeep(Integer.MAX_VALUE);
		treeNodeConfig.setParentIdKey("parentId");

		// 转换器
		List<Tree<Object>> treeNodes = TreeUtil.build(list, 0L,
				treeNodeConfig,
				(treeNode, tree) -> {
					tree.setId(treeNode.getId());
					tree.setParentId(treeNode.getParentId());
					tree.setName(treeNode.getName());
					tree.setWeight(null);
				});

		assertNotNull(treeNodes);

		try {
			// 转换器
			treeNodes = TreeUtil.build(list, "0",
					treeNodeConfig,
					(treeNode, tree) -> {
						tree.setId(treeNode.getId());
						tree.setParentId(treeNode.getParentId());
						tree.setName(treeNode.getName());
						tree.setWeight(null);
					});
		}catch (Exception e) {
			assertEquals(e.getClass(), IllegalArgumentException.class);
		}
	}

	@Data
	public static class TestBean {
		private long id;
		private long parentId;
		private String name;
	}
}
