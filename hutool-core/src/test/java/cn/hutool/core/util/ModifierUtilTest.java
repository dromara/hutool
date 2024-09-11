package cn.hutool.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ModifierUtilTest {

	@Test
	public void hasModifierTest() throws NoSuchMethodException {
		Method method = ModifierUtilTest.class.getDeclaredMethod("ddd");
		assertTrue(ModifierUtil.hasModifier(method, ModifierUtil.ModifierType.PRIVATE));
		assertTrue(ModifierUtil.hasModifier(method,
				ModifierUtil.ModifierType.PRIVATE,
				ModifierUtil.ModifierType.STATIC)
		);
	}

	@Test
	public void hasModifierTest2() throws NoSuchMethodException {
		Method method = ModifierUtilTest.class.getDeclaredMethod("ddd");
		assertTrue(ModifierUtil.hasModifier(method, ModifierUtil.ModifierType.PRIVATE));
		assertTrue(ModifierUtil.hasModifier(method,
			ModifierUtil.ModifierType.PRIVATE,
			ModifierUtil.ModifierType.ABSTRACT)
		);
	}

	@Test
	void issueIAQ2U0Test() throws NoSuchMethodException {
		final Method method = ModifierUtilTest.class.getDeclaredMethod("ddd");

		Assertions.assertTrue(ModifierUtil.hasModifier(method,
			ModifierUtil.ModifierType.PRIVATE,
			ModifierUtil.ModifierType.STATIC,
			// 不存在
			ModifierUtil.ModifierType.TRANSIENT
		));

		Assertions.assertFalse(ModifierUtil.hasAllModifiers(method,
			ModifierUtil.ModifierType.PRIVATE,
			ModifierUtil.ModifierType.STATIC,
			// 不存在
			ModifierUtil.ModifierType.TRANSIENT
		));
	}

	private static void ddd() {
	}
}
