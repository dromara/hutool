package cn.hutool.core.lang.tree;

import cn.hutool.core.lang.tree.parser.NodeParser;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 如果指定rootId的节点已经存在，直接作为根节点
 */
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

		TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
		treeNodeConfig.setIdKey("deptId");
		treeNodeConfig.setNameKey("deptName");
		treeNodeConfig.setParentIdKey("parentId");
		NodeParser<TestDept,Long> nodeParser= (dept, tree) ->
			tree.setId(dept.getDeptId())
			.setParentId(dept.getParentId())
			.setName(dept.getDeptName());
		Tree<Long> longTree = TreeUtil.buildSingle(list, 1L, treeNodeConfig, nodeParser);

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
