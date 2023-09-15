package org.dromara.hutool.core.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class Issue3308Test {
	@Test
	void partitionTest() {
		final List<String> list = new ArrayList<>();
		for (int i = 0; i < 100000; i++) {
			list.add("Str"+i);
		}
		final List<List<String>> partition = ListUtil.partition(list, 1000);
		Assertions.assertEquals(100, partition.size());
	}
}
