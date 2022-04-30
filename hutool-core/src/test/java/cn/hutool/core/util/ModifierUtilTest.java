package cn.hutool.core.util;

import cn.hutool.core.reflect.ModifierUtil;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

public class ModifierUtilTest {

	@Test
	public void hasModifierTest() throws NoSuchMethodException {
		final Method method = ModifierUtilTest.class.getDeclaredMethod("ddd");
		Assert.assertTrue(ModifierUtil.hasModifier(method, ModifierUtil.ModifierType.PRIVATE));
		Assert.assertTrue(ModifierUtil.hasModifier(method,
				ModifierUtil.ModifierType.PRIVATE,
				ModifierUtil.ModifierType.STATIC)
		);
	}

	private static void ddd() {
	}
}
