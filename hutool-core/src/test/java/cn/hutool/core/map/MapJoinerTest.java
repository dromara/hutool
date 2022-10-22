package cn.hutool.core.map;

import org.junit.Assert;
import org.junit.Test;

public class MapJoinerTest {

	@Test
	public void joinMapTest(){
		final Dict v1 = Dict.of().set("id", 12).set("name", "张三").set("age", 23);
		final MapJoiner joiner = MapJoiner.of("+", "-");
		joiner.append(v1, null);

		Assert.assertEquals("id-12+name-张三+age-23", joiner.toString());
	}

	@Test
	public void joinMapWithPredicateTest(){
		final Dict v1 = Dict.of().set("id", 12).set("name", "张三").set("age", 23);
		final MapJoiner joiner = MapJoiner.of("+", "-");
		joiner.append(v1, (entry)->"age".equals(entry.getKey()));

		Assert.assertEquals("age-23", joiner.toString());
	}
}
