/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.tree.hierarchy;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * test for {@link HierarchyUtil}
 *
 * @author huangchengxing
 */
class HierarchyUtilTest {

	@Data
	static class Node {
		private String parent;
		private String value;
		private List<Node> children;

		public Node(String parent, String value) {
			this.parent = parent;
			this.value = value;
		}
	}

	private Node root;

	@BeforeEach
	void init() {
		// 根节点
		root = new Node(null, "0");

		// 第二层
		Node first1 = new Node(root.value, "0-1");
		Node first2 = new Node(root.value, "0-2");
		Node first3 = new Node(root.value, "0-3");
		root.setChildren(Arrays.asList(first1, first2, first3));

		// 第三层
		Node second11 = new Node(first1.value, "0-1-1");
		Node second12 = new Node(first1.value, "0-1-2");
		Node second13 = new Node(first1.value, "0-1-3");
		first1.setChildren(Arrays.asList(second11, second12, second13));

		Node second21 = new Node(first2.value, "0-2-1");
		Node second22 = new Node(first2.value, "0-2-2");
		Node second23 = new Node(first2.value, "0-2-3");
		first2.setChildren(Arrays.asList(second21, second22, second23));

		Node second31 = new Node(first3.value, "0-3-1");
		Node second32 = new Node(first3.value, "0-3-2");
		Node second33 = new Node(first3.value, "0-3-3");
		first3.setChildren(Arrays.asList(second31, second32, second33));
	}

	@Test
	void testTraverseByBreadthFirst() {
		// // 按广度优先遍历所有节点
		final List<String> nodes = new ArrayList<>();
		HierarchyUtil.traverseByBreadthFirst(root, HierarchyIteratorUtil.scan(t -> {
			nodes.add(t.getValue());
			return t.getChildren();
		}));
		Assertions.assertEquals(13, nodes.size());
		Assertions.assertEquals(
				Arrays.asList("0", "0-1", "0-2", "0-3", "0-1-1", "0-1-2", "0-1-3", "0-2-1", "0-2-2", "0-2-3", "0-3-1", "0-3-2", "0-3-3"),
				nodes
		);

		// 按广度优先寻找 0-2-3
		final String target = HierarchyUtil.traverseByBreadthFirst(root, HierarchyIteratorUtil.find(Node::getChildren,
				t -> Objects.equals(t.getValue(), "0-2-3") ? t.getValue() : null
		));
		Assertions.assertEquals("0-2-3", target);

		// 按广度优先获取 0-2 的所有子节点
		final List<String> children = HierarchyUtil.traverseByBreadthFirst(root, HierarchyIteratorUtil.collect(Node::getChildren,
				t -> Objects.equals(t.getParent(), "0-2") ? t.getValue() : null
		));
		Assertions.assertEquals(3, children.size());
		Assertions.assertEquals(new ArrayList<>(Arrays.asList("0-2-1", "0-2-2", "0-2-3")), children);
	}

	@Test
	void testTraverseByDepthFirst() {
		// 按深度优先遍历所有节点
		final Set<String> nodes = new LinkedHashSet<>();
		HierarchyUtil.traverseByDepthFirst(root, HierarchyIteratorUtil.scan(t -> {
			nodes.add(t.getValue());
			return t.getChildren();
		}));
		Assertions.assertEquals(13, nodes.size());
		Assertions.assertEquals(
				new LinkedHashSet<>(Arrays.asList("0", "0-1", "0-1-1", "0-1-2", "0-1-3", "0-2", "0-2-1", "0-2-2", "0-2-3", "0-3", "0-3-1", "0-3-2", "0-3-3")),
				nodes
		);

		// 按深度优先寻找 0-2-3
		final String target = HierarchyUtil.traverseByDepthFirst(root, HierarchyIteratorUtil.find(Node::getChildren,
				t -> Objects.equals(t.getValue(), "0-2-3") ? t.getValue() : null
		));
		Assertions.assertEquals("0-2-3", target);

		// 按深度优先获取 0-2 的所有子节点
		final List<String> children = HierarchyUtil.traverseByDepthFirst(root, HierarchyIteratorUtil.collect(Node::getChildren,
				t -> Objects.equals(t.getParent(), "0-2") ? t.getValue() : null
		));
		Assertions.assertEquals(3, children.size());
		Assertions.assertEquals(new ArrayList<>(Arrays.asList("0-2-1", "0-2-2", "0-2-3")), children);
	}

}
