package org.dromara.hutool.lang;

import org.dromara.hutool.collection.ListUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WeightRandomTest {

	@Test
	public void weightRandomTest() {
		final WeightRandom<String> random = WeightRandom.of();
		random.add("A", 10);
		random.add("B", 50);
		random.add("C", 100);

		final String result = random.next();
		Assertions.assertTrue(ListUtil.of("A", "B", "C").contains(result));
	}
}
