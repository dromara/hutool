package cn.hutool.core.lang;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AssertTest {

	@Test
	public void isNullTest() {
		String a = null;
		cn.hutool.core.lang.Assert.isNull(a);
	}

	@Test
	public void notNullTest() {
		String a = null;
		cn.hutool.core.lang.Assert.isNull(a);
	}

	@Test
	public void isTrueTest() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			int i = 0;
			cn.hutool.core.lang.Assert.isTrue(i > 0, IllegalArgumentException::new);
		});
	}

	@Test
	public void isTrueTest2() {
		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
			int i = -1;
			cn.hutool.core.lang.Assert.isTrue(i >= 0, IndexOutOfBoundsException::new);
		});
	}

	@Test
	public void isTrueTest3() {
		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
			int i = -1;
			Assert.isTrue(i > 0, () -> new IndexOutOfBoundsException("relation message to return"));
		});
	}
}
