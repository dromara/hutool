package org.dromara.hutool.core.map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MapJoinerTest {

	@Test
	public void joinMapTest(){
		final Dict v1 = Dict.of().set("id", 12).set("name", "张三").set("age", 23);
		final MapJoiner joiner = MapJoiner.of("+", "-");
		joiner.append(v1, null);

		Assertions.assertEquals("id-12+name-张三+age-23", joiner.toString());
	}

	@Test
	public void joinMapWithPredicateTest(){
		final Dict v1 = Dict.of().set("id", 12).set("name", "张三").set("age", 23);
		final MapJoiner joiner = MapJoiner.of("+", "-");
		joiner.append(v1, (entry)->"age".equals(entry.getKey()));

		Assertions.assertEquals("age-23", joiner.toString());
	}
}
