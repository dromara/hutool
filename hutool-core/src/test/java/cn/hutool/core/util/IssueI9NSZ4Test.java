package cn.hutool.core.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.junit.Assert;
import org.junit.Test;

public class IssueI9NSZ4Test {

	@Test
	public void getByTest() {
		// AnimalKindInZoo所有枚举结果的getMappedValue结果值中都无AnimalKind.DOG，返回null
		final AnimalKindInZoo by = EnumUtil.getBy(AnimalKindInZoo::getMappedValue, AnimalKind.DOG);
		Assert.assertNull(by);
	}

	@Test
	public void getByTest2() {
		final AnimalKindInZoo by = EnumUtil.getBy(AnimalKindInZoo::getMappedValue, AnimalKind.BIRD);
		Assert.assertEquals(AnimalKindInZoo.BIRD, by);
	}

	/**
	 * 动物类型
	 */
	@Getter
	@ToString
	@AllArgsConstructor
	public enum AnimalKind {

		/**
		 * 猫
		 */
		CAT("cat", "猫"),
		/**
		 * 狗
		 */
		DOG("dog", "狗"),
		/**
		 * 鸟
		 */
		BIRD("bird", "鸟");

		/**
		 * 键
		 */
		private final String key;
		/**
		 * 值
		 */
		private final String value;
	}

	/**
	 * 动物园里的动物类型
	 */
	@Getter
	@ToString
	@AllArgsConstructor
	public enum AnimalKindInZoo {

		/**
		 * 猫
		 */
		CAT("cat", "猫", AnimalKind.CAT),
		/**
		 * 蛇
		 */
		SNAKE("snake", "蛇", null),
		/**
		 * 鸟
		 */
		BIRD("bird", "鸟", AnimalKind.BIRD);

		/**
		 * 键
		 */
		private final String key;
		/**
		 * 值
		 */
		private final String value;
		/**
		 * 映射值
		 */
		private final AnimalKind mappedValue;
	}
}
