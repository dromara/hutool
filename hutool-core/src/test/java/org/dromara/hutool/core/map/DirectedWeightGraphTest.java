package org.dromara.hutool.core.map;

import org.dromara.hutool.core.map.multi.DirectedWeightGraph;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * @author newshiJ
 */
public class DirectedWeightGraphTest {

	@Test
	@Disabled
	public void test1() {
		final DirectedWeightGraph<String> graph = new DirectedWeightGraph<>();
		graph.putEdge("A", "B", 14);
		graph.putEdge("A", "C", 8);
		graph.putEdge("A", "D", 12);

//		graph.putEdge("B", "A", -14);
		graph.putEdge("B", "E", 4);

		graph.putEdge("C", "B", 3);
		graph.putEdge("C", "D", 5);
		graph.putEdge("C", "E", 5);
		graph.putEdge("C", "F", 6);

		graph.putEdge("D", "F", 7);

//		graph.putEdge("E", "B", -14);
		graph.putEdge("E", "G", 4);

		graph.putEdge("G", "B", -9);

		graph.putEdge("F", "G", 2);

		graph.putEdge("X", "Y", 2);

		graph.removePoint("X");

		System.out.println(graph);
		Map<String, DirectedWeightGraph.Path<String>> map = null;
		try {
			map = graph.bestPathMap("A");
			map.forEach((k, v) -> {
				System.out.println(v);
			});
		} catch (final DirectedWeightGraph.NegativeRingException e) {
			e.printStackTrace();
		}

	}
}
