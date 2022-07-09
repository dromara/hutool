package cn.hutool.core.lang.func;

import cn.hutool.core.collection.SetUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PredicateUtilTest {

	@Test
	public void notContainsTest(){
		final Set<String> sets = SetUtil.of("1", "2", "3");
		final List<String> collect = Stream.of("3", "4", "5")
				.filter(PredicateUtil.negate(sets::contains))
				.collect(Collectors.toList());

		Assert.assertEquals(2, collect.size());
		Assert.assertEquals("4", collect.get(0));
		Assert.assertEquals("5", collect.get(1));
	}
}
