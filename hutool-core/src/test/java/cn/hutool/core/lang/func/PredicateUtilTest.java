package cn.hutool.core.lang.func;

import cn.hutool.core.collection.SetUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cn.hutool.core.lang.func.PredicateUtil.*;

public class PredicateUtilTest {

	@Test
	public void notContainsTest() {
		final Set<String> sets = SetUtil.of("1", "2", "3");
		final List<String> collect = Stream.of("3", "4", "5")
				.filter(negate(sets::contains))
				.collect(Collectors.toList());

		Assert.assertEquals(2, collect.size());
		Assert.assertEquals("4", collect.get(0));
		Assert.assertEquals("5", collect.get(1));
	}

	@Test
	public void andTest() {
		boolean condition = Stream.of(1, 3, 5)
				.allMatch(
						and(
								Objects::nonNull,
								i -> i < 10,
								i -> i % 2 == 1
						)
				);
		Assert.assertTrue(condition);
	}

	@Test
	public void orTest() {
		boolean condition = Stream.of(1, 3, 5)
				.anyMatch(
						or(
								Objects::isNull,
								i -> i > 10,
								i -> i % 2 == 0
						)
				);
		Assert.assertFalse(condition);
	}


}
