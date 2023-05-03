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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * test for {@link HierarchyUtil}
 *
 * @author huangchengxing
 */
class HierarchyUtilTest {

	private Map<String, String> tree;

	@BeforeEach
	void init() {
		tree = new LinkedHashMap<>();

		// 根节点
		tree.put("0", null);

		// 第一层
		tree.put("0-1", "0");
		tree.put("0-2", "0");
		tree.put("0-3", "0");

		// 第三层
		tree.put("0-1-1", "0-1");
		tree.put("0-1-2", "0-1");
		tree.put("0-1-3", "0-1");

		tree.put("0-2-1", "0-2");
		tree.put("0-2-2", "0-2");
		tree.put("0-2-3", "0-2");

		tree.put("0-3-1", "0-3");
		tree.put("0-3-2", "0-3");
		tree.put("0-3-3", "0-3");
	}

	@Test
	void testTraverseByBreadthFirst() {
		// 按广度优先遍历所有节点
		final Set<String> nodes = new LinkedHashSet<>();
		HierarchyUtil.traverseByBreadthFirst("0", HierarchyIteratorUtil.scan(t -> {
			nodes.add(t);
			return tree.entrySet().stream()
				.filter(e -> Objects.equals(t, e.getValue()))
				.map(Map.Entry::getKey)
				.collect(Collectors.toList());
		}));
		Assertions.assertEquals(13, nodes.size());
		Assertions.assertEquals(
			new LinkedHashSet<>(Arrays.asList("0", "0-1", "0-2", "0-3", "0-1-1", "0-1-2", "0-1-3", "0-2-1", "0-2-2", "0-2-3", "0-3-1", "0-3-2", "0-3-3")),
			nodes
		);

		// 按广度优先寻找 0-2-3
		final String target = HierarchyUtil.traverseByBreadthFirst("0", HierarchyIteratorUtil.find(parentFinder(),
			t -> Objects.equals(t, "0-2-3") ? t : null
		));
		Assertions.assertEquals("0-2-3", target);

		// 按广度优先获取 0-2 的所有子节点
		final List<String> children = HierarchyUtil.traverseByBreadthFirst(
			"0", HierarchyIteratorUtil.collect(parentFinder(),
				t -> Objects.equals(tree.get(t), "0-2") ? t : null
			)
		);
		Assertions.assertEquals(3, children.size());
		Assertions.assertEquals(new ArrayList<>(Arrays.asList("0-2-1", "0-2-2", "0-2-3")), children);
	}

	@Test
	void testTraverseByDepthFirst() {
		// 按深度优先遍历所有节点
		final Set<String> nodes = new LinkedHashSet<>();
		HierarchyUtil.traverseByDepthFirst("0", HierarchyIteratorUtil.scan(t -> {
			nodes.add(t);
			return tree.entrySet().stream()
				.filter(e -> Objects.equals(t, e.getValue()))
				.map(Map.Entry::getKey)
				.collect(Collectors.toList());
		}));
		Assertions.assertEquals(13, nodes.size());
		Assertions.assertEquals(
			new LinkedHashSet<>(Arrays.asList("0", "0-1", "0-1-1", "0-1-2", "0-1-3", "0-2", "0-2-1", "0-2-2", "0-2-3", "0-3", "0-3-1", "0-3-2", "0-3-3")),
			nodes
		);

		// 按深度优先寻找 0-2-3
		final String target = HierarchyUtil.traverseByDepthFirst("0", HierarchyIteratorUtil.find(parentFinder(),
			t -> Objects.equals(t, "0-2-3") ? t : null
		));
		Assertions.assertEquals("0-2-3", target);

		// 按深度优先获取 0-2 的所有子节点
		final List<String> children = HierarchyUtil.traverseByDepthFirst(
			"0", HierarchyIteratorUtil.collect(parentFinder(),
				t -> Objects.equals(tree.get(t), "0-2") ? t : null
			)
		);
		Assertions.assertEquals(3, children.size());
		Assertions.assertEquals(new ArrayList<>(Arrays.asList("0-2-1", "0-2-2", "0-2-3")), children);
	}

	private Function<String, Collection<String>> parentFinder() {
		return t -> tree.entrySet()
			.stream()
			.filter(e -> Objects.equals(t, e.getValue()))
			.map(Map.Entry::getKey)
			.collect(Collectors.toList());
	}
}
