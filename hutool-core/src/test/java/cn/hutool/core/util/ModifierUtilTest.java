package cn.hutool.core.util;

import cn.hutool.core.reflect.FieldUtil;
import cn.hutool.core.reflect.ModifierUtil;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

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

	@Test
	public void setFinalFieldValueTest() {
		final String fieldName = "DIALECTS";
		final List<Number> dialects =
				Arrays.asList(
						1,
						2,
						3,
						99
				);
		final Field field = FieldUtil.getField(JdbcDialects.class, fieldName);
		ModifierUtil.removeFinalModify(field);
		FieldUtil.setFieldValue(JdbcDialects.class, fieldName, dialects);

		Assert.assertEquals(dialects, FieldUtil.getFieldValue(JdbcDialects.class, fieldName));
	}

	@SuppressWarnings("unused")
	public static class JdbcDialects {
		private static final List<Number> DIALECTS =
				Arrays.asList(1L, 2L, 3L);
	}
}
