/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.map;

import org.dromara.hutool.core.map.multi.Graph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * test for {@link Graph}
 */
public class GraphTest {

	@Test
	public void testPutEdge() {
		final Graph<Integer> graph = new Graph<>();
		graph.putEdge(0, 1);
		graph.putEdge(1, 2);
		graph.putEdge(2, 0);

		Assertions.assertEquals(asSet(1, 2), graph.getValues(0));
		Assertions.assertEquals(asSet(0, 2), graph.getValues(1));
		Assertions.assertEquals(asSet(0, 1), graph.getValues(2));
	}

	@Test
	public void testContainsEdge() {
		// 0 -- 1
		// |    |
		// 3 -- 2
		final Graph<Integer> graph = new Graph<>();
		graph.putEdge(0, 1);
		graph.putEdge(1, 2);
		graph.putEdge(2, 3);
		graph.putEdge(3, 0);

		Assertions.assertTrue(graph.containsEdge(0, 1));
		Assertions.assertTrue(graph.containsEdge(1, 0));

		Assertions.assertTrue(graph.containsEdge(1, 2));
		Assertions.assertTrue(graph.containsEdge(2, 1));

		Assertions.assertTrue(graph.containsEdge(2, 3));
		Assertions.assertTrue(graph.containsEdge(3, 2));

		Assertions.assertTrue(graph.containsEdge(3, 0));
		Assertions.assertTrue(graph.containsEdge(0, 3));

		Assertions.assertFalse(graph.containsEdge(1, 3));
	}

	@Test
	public void removeEdge() {
		final Graph<Integer> graph = new Graph<>();
		graph.putEdge(0, 1);
		Assertions.assertTrue(graph.containsEdge(0, 1));

		graph.removeEdge(0, 1);
		Assertions.assertFalse(graph.containsEdge(0, 1));
	}

	@Test
	public void testContainsAssociation() {
		// 0 -- 1
		// |    |
		// 3 -- 2
		final Graph<Integer> graph = new Graph<>();
		graph.putEdge(0, 1);
		graph.putEdge(1, 2);
		graph.putEdge(2, 3);
		graph.putEdge(3, 0);

		Assertions.assertTrue(graph.containsAssociation(0, 2));
		Assertions.assertTrue(graph.containsAssociation(2, 0));

		Assertions.assertTrue(graph.containsAssociation(1, 3));
		Assertions.assertTrue(graph.containsAssociation(3, 1));

		Assertions.assertFalse(graph.containsAssociation(-1, 1));
		Assertions.assertFalse(graph.containsAssociation(1, -1));
	}

	@Test
	public void testGetAssociationPoints() {
		// 0 -- 1
		// |    |
		// 3 -- 2
		final Graph<Integer> graph = new Graph<>();
		graph.putEdge(0, 1);
		graph.putEdge(1, 2);
		graph.putEdge(2, 3);
		graph.putEdge(3, 0);

		Assertions.assertEquals(asSet(0, 1, 2, 3), graph.getAssociatedPoints(0, true));
		Assertions.assertEquals(asSet(1, 2, 3), graph.getAssociatedPoints(0, false));

		Assertions.assertEquals(asSet(1, 2, 3, 0), graph.getAssociatedPoints(1, true));
		Assertions.assertEquals(asSet(2, 3, 0), graph.getAssociatedPoints(1, false));

		Assertions.assertEquals(asSet(2, 3, 0, 1), graph.getAssociatedPoints(2, true));
		Assertions.assertEquals(asSet(3, 0, 1), graph.getAssociatedPoints(2, false));

		Assertions.assertEquals(asSet(3, 0, 1, 2), graph.getAssociatedPoints(3, true));
		Assertions.assertEquals(asSet(0, 1, 2), graph.getAssociatedPoints(3, false));

		Assertions.assertTrue(graph.getAssociatedPoints(-1, false).isEmpty());
	}

	@Test
	public void testGetAdjacentPoints() {
		// 0 -- 1
		// |    |
		// 3 -- 2
		final Graph<Integer> graph = new Graph<>();
		graph.putEdge(0, 1);
		graph.putEdge(1, 2);
		graph.putEdge(2, 3);
		graph.putEdge(3, 0);

		Assertions.assertEquals(asSet(1, 3), graph.getAdjacentPoints(0));
		Assertions.assertEquals(asSet(2, 0), graph.getAdjacentPoints(1));
		Assertions.assertEquals(asSet(1, 3), graph.getAdjacentPoints(2));
		Assertions.assertEquals(asSet(2, 0), graph.getAdjacentPoints(3));
	}

	@Test
	public void testRemovePoint() {
		// 0 -- 1
		// |    |
		// 3 -- 2
		final Graph<Integer> graph = new Graph<>();
		graph.putEdge(0, 1);
		graph.putEdge(1, 2);
		graph.putEdge(2, 3);
		graph.putEdge(3, 0);

		// 0
		// |
		// 3 -- 2
		graph.removePoint(1);

		Assertions.assertEquals(asSet(3), graph.getAdjacentPoints(0));
		Assertions.assertTrue(graph.getAdjacentPoints(1).isEmpty());
		Assertions.assertEquals(asSet(3), graph.getAdjacentPoints(2));
		Assertions.assertEquals(asSet(2, 0), graph.getAdjacentPoints(3));
	}

	@SafeVarargs
	private static <T> Set<T> asSet(final T... ts) {
		return new LinkedHashSet<>(Arrays.asList(ts));
	}

}
