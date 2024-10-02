package org.dromara.hutool.core.tree;

import lombok.Data;
import org.dromara.hutool.core.tree.parser.NodeParser;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IssueIAUSHRTest {
	@Test
	void buildSingleTest() {
		final List<TestDept> list= new ArrayList<>();
		TestDept sysDept = new TestDept();
		sysDept.setDeptId(1L);
		sysDept.setDeptName("A");
		sysDept.setParentId(null);
		list.add(sysDept);

		sysDept = new TestDept();
		sysDept.setDeptId(2L);
		sysDept.setDeptName("B");
		sysDept.setParentId(1L);
		list.add(sysDept);

		sysDept = new TestDept();
		sysDept.setDeptId(3L);
		sysDept.setDeptName("C");
		sysDept.setParentId(1L);
		list.add(sysDept);

		final TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
		treeNodeConfig.setIdKey("deptId");
		treeNodeConfig.setNameKey("deptName");
		treeNodeConfig.setParentIdKey("parentId");

		final NodeParser<TestDept,Long> nodeParser= (dept, tree) ->
			tree.setId(dept.getDeptId())
				.setParentId(dept.getParentId())
				.setName(dept.getDeptName());

		final MapTree<Long> longTree = TreeUtil.buildSingle(list, 1L, treeNodeConfig, nodeParser);

		assertEquals("A", longTree.getName());
		assertEquals(2, longTree.getChildren().size());
		assertEquals("B", longTree.getChildren().get(0).getName());
		assertEquals("C", longTree.getChildren().get(1).getName());
	}

	@Data
	public static class TestDept {
		private Long deptId;
		private String deptName;
		private Long parentId;
	}
}
