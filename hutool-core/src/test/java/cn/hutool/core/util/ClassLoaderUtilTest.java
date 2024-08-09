package cn.hutool.core.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ClassLoaderUtilTest {

	@Test
	public void loadClassTest() {
		String name = ClassLoaderUtil.loadClass("java.lang.Thread.State").getName();
		assertEquals("java.lang.Thread$State", name);

		name = ClassLoaderUtil.loadClass("java.lang.Thread$State").getName();
		assertEquals("java.lang.Thread$State", name);
	}
}
