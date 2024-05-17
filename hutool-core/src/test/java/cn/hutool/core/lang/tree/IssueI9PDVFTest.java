package cn.hutool.core.lang.tree;

import cn.hutool.core.lang.Assert;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class IssueI9PDVFTest {
	@Test
	public void buildTest() {
		List<TestList> list = new ArrayList<>();
		list.add(new TestList(1790187987502895123L, "顶级", 0L));
		list.add(new TestList(1790187987502895124L, "子集", 1790187987502895123L));

		List<Tree<String>> build = TreeUtil.build(list, "0", (testList, treeNode) -> {
			treeNode.setId(testList.getId().toString());
			treeNode.setName(testList.getName());
			treeNode.setParentId(testList.getParentId().toString());
		});

		Assert.notNull(build);
	}

	@AllArgsConstructor
	@Data
	public static class TestList {
		Long id;
		String name;
		Long parentId;
	}
}
