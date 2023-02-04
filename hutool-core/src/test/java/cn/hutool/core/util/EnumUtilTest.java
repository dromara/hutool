package cn.hutool.core.util;

import cn.hutool.core.collection.ListUtil;
import lombok.Getter;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * EnumUtil单元测试
 *
 * @author looly
 */
public class EnumUtilTest {

	@Test
	public void getNamesTest() {
		final List<String> names = EnumUtil.getNames(TestEnum.class);
		Assert.assertEquals(ListUtil.of("TEST1", "TEST2", "TEST3"), names);
	}

	@Test
	public void getFieldValuesTest() {
		final List<Object> types = EnumUtil.getFieldValues(TestEnum.class, "type");
		Assert.assertEquals(ListUtil.of("type1", "type2", "type3"), types);
	}

	@Test
	public void getFieldNamesTest() {
		final List<String> names = EnumUtil.getFieldNames(TestEnum.class);
		Assert.assertTrue(names.contains("type"));
		Assert.assertTrue(names.contains("name"));
	}

	@Test
	public void getByTest() {
		// 枚举中字段互相映射使用
		final TestEnum testEnum = EnumUtil.getBy(TestEnum::ordinal, 1);
		Assert.assertEquals("TEST2", testEnum.name());
	}

	@Test
	public void getByCodeTest() {
		// 通过code获取枚举值
		Assert.assertEquals(TestEnum2.TEST_A, EnumUtil.getByCode(TestEnum2.class, 1001));
		Assert.assertEquals(TestEnum2.TEST_B, EnumUtil.getByCode(TestEnum2.class, 1002));
		Assert.assertEquals(TestEnum2.TEST_C, EnumUtil.getByCode(TestEnum2.class, 1003));
		// 枚举值不存在
		Assert.assertNull(EnumUtil.getByCode(TestEnum2.class, 1004));
		// 缓存已存在的情况
		Assert.assertEquals(TestEnum2.TEST_A, EnumUtil.getByCode(TestEnum2.class, 1001));
		// code为字符串时
		Assert.assertEquals(TestEnum3.TEST_ONE, EnumUtil.getByCode(TestEnum3.class, "one"));
		Assert.assertEquals(TestEnum3.getByCode("two"), EnumUtil.getByCode(TestEnum3.class, "two"));
		Assert.assertEquals(TestEnum3.getByCode("three"), EnumUtil.getByCode(TestEnum3.class, "three"));
		Assert.assertEquals(TestEnum3.getByCode("four"), EnumUtil.getByCode(TestEnum3.class, "four"));
	}

	@Test
	public void getFieldByTest() {
		// 枚举中字段互相映射使用
		final String type = EnumUtil.getFieldBy(TestEnum::getType, Enum::ordinal, 1);
		Assert.assertEquals("type2", type);

		final int ordinal = EnumUtil.getFieldBy(TestEnum::ordinal, Enum::ordinal, 1);
		Assert.assertEquals(1, ordinal);
	}

	@Test
	public void likeValueOfTest() {
		final TestEnum value = EnumUtil.likeValueOf(TestEnum.class, "type2");
		Assert.assertEquals(TestEnum.TEST2, value);
	}

	@Test
	public void getEnumMapTest() {
		final Map<String,TestEnum> enumMap = EnumUtil.getEnumMap(TestEnum.class);
		Assert.assertEquals(TestEnum.TEST1, enumMap.get("TEST1"));
	}

	@Test
	public void getNameFieldMapTest() {
		final Map<String, Object> enumMap = EnumUtil.getNameFieldMap(TestEnum.class, "type");
		assert enumMap != null;
		Assert.assertEquals("type1", enumMap.get("TEST1"));
	}

	public enum TestEnum{
		TEST1("type1"), TEST2("type2"), TEST3("type3");

		TestEnum(final String type) {
			this.type = type;
		}

		private final String type;
		@SuppressWarnings("unused")
		private String name;

		public String getType() {
			return this.type;
		}

		public String getName() {
			return this.name;
		}
	}


	@Getter
	public enum TestEnum2 {
		TEST_A(1001), TEST_B(1002), TEST_C(1003);

		TestEnum2(final Integer code) {
			this.code = code;
		}

		private final Integer code;
	}

	@Getter
	public enum TestEnum3 {
		TEST_ONE("one"), TEST_TWO("two"), TEST_THREE("three");

		TestEnum3(final String code) {
			this.code = code;
		}

		private final String code;

		public static TestEnum3 getByCode(String code) {
			for (TestEnum3 value : values()) {
				if (value.code.equals(code)) {
					return value;
				}
			}
			return null;
		}
	}

}
