package org.dromara.hutool.core.tree;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * test for {@link HierarchyIterator}
 *
 * @author huangchengxing
 */
class HierarchyIteratorTest {

	@Test
	void testNoSuchElementException() {
		Node node = createTree();
		// 根节点也被忽略，将会抛出异常
		Assertions.assertThrows(IllegalArgumentException.class, () -> HierarchyIterator.depthFirst(node, node1 -> node1.children, node1 -> false));
		// 忽略所有节点，将会抛出NoSuchElementException
		HierarchyIterator<Node> iterator = HierarchyIterator.depthFirst(node, node1 -> node1.children, node1 -> node1.id.equals("1"));
		Assertions.assertSame(node, iterator.next());
		Assertions.assertThrows(NoSuchElementException.class, iterator::next);
	}

	@Test
	void testDepthFirstWithFilter() {
		Node node = createTree();
		// 按深度度优先遍历树结构，忽略id为1-1的节点与以其为根节点的子树
		List<String> nodes = new ArrayList<>();
		HierarchyIterator<Node> iterator = HierarchyIterator.depthFirst(
			node, node1 -> node1.children, node1 -> !node1.id.equals("1-1")
		);
		while (iterator.hasNext()) {
			Node node1 = iterator.next();
			nodes.add(node1.id);
		}
		Assertions.assertEquals(Arrays.asList("1", "1-2", "1-2-1", "1-2-2"), nodes);
	}

	@Test
	void testDepthFirst() {
		Node node = createTree();
		// 按深度度优先遍历树结构
		List<String> nodes = new ArrayList<>();
		HierarchyIterator<Node> iterator = HierarchyIterator.depthFirst(node, node1 -> node1.children);
		while (iterator.hasNext()) {
			Node node1 = iterator.next();
			nodes.add(node1.id);
		}
		Assertions.assertEquals(Arrays.asList("1", "1-1", "1-1-1", "1-1-2", "1-2", "1-2-1", "1-2-2"), nodes);
	}

	@Test
	void testBreadthFirstWithFilter() {
		Node node = createTree();
		// 按深度度优先遍历树结构，忽略id为1-1的节点与以其为根节点的子树
		List<String> nodes = new ArrayList<>();
		HierarchyIterator<Node> iterator = HierarchyIterator.breadthFirst(
			node, node1 -> node1.children, node1 -> !node1.id.equals("1-1")
		);
		while (iterator.hasNext()) {
			Node node1 = iterator.next();
			nodes.add(node1.id);
		}
		Assertions.assertEquals(Arrays.asList("1", "1-2", "1-2-1", "1-2-2"), nodes);
	}

	@Test
	void testBreadthFirst() {
		Node root = createGraph();
		// 按深度度优先遍历图结构
		List<String> nodes = new ArrayList<>();
		HierarchyIterator<Node> iterator = HierarchyIterator.breadthFirst(root, node -> node.children);
		while (iterator.hasNext()) {
			Node node = iterator.next();
			nodes.add(node.id);
		}
		Assertions.assertEquals(Arrays.asList("1", "4", "2", "3"), nodes);
	}

	private static Node createGraph() {
		// 构建一个包含四个节点的图，每一个节点都有两个相邻节点
		Node node1 = new Node("1");
		Node node2 = new Node("2");
		Node node3 = new Node("3");
		Node node4 = new Node("4");
		node1.children = Arrays.asList(node4, node2);
		node2.children = Arrays.asList(node1, node3);
		node3.children = Arrays.asList(node2, node4);
		node4.children = Arrays.asList(node3, node1);
		return node1;
	}

	private static Node createTree() {
		// 构建一个三层的树，每一个节点都有两个子节点
		return new Node("1", Arrays.asList(
			new Node("1-1", Arrays.asList(
				new Node("1-1-1", null),
				new Node("1-1-2", null)
			)),
			new Node("1-2", Arrays.asList(
				new Node("1-2-1", null),
				new Node("1-2-2", null)
			))
		));
	}

	/**
	 * 节点
	 */
	private static class Node {
		private final String id;
		private List<Node> children;
		private Node(String id, List<Node> children) {
			this.id = id;
			this.children = children;
		}
		public Node(String id) {
			this.id = id;
		}
	}
}
