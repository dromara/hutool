package org.dromara.hutool.core.tuple;

import org.junit.jupiter.api.Test;

/**
 * {@link ImmutableTriple} 三元组单元测试
 * {@link MutableTriple} 三元组单元测试
 *
 * @author kirno7
 */
public class TripleTest {
	@Test
	public void tripleTest() {
		MutableTriple<String, String, String> mutableTriple = MutableTriple.of("1", "1", "1");
		System.out.println(mutableTriple);
		mutableTriple.setLeft("2");
		mutableTriple.setMiddle("2");
		mutableTriple.setRight("2");
		System.out.println(mutableTriple);
		ImmutableTriple<String, String, String> immutableTriple = ImmutableTriple.of("3", "3", "3");
		System.out.println(immutableTriple);
	}
}
