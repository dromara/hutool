package cn.hutool.core.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

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
	private static void ddd() {
	}
}
