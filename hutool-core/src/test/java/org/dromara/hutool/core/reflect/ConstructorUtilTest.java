/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.core.reflect;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.hutool.core.date.Week;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

public class ConstructorUtilTest {
	@Test
	public void noneStaticInnerClassTest() {
		final ReflectTestBeans.NoneStaticClass testAClass = ConstructorUtil.newInstanceIfPossible(ReflectTestBeans.NoneStaticClass.class);
		Assertions.assertNotNull(testAClass);
		Assertions.assertEquals(2, testAClass.getA());
	}

	@Test
	public void newInstanceIfPossibleTest(){
		//noinspection ConstantConditions
		final int intValue = ConstructorUtil.newInstanceIfPossible(int.class);
		Assertions.assertEquals(0, intValue);

		final Integer integer = ConstructorUtil.newInstanceIfPossible(Integer.class);
		Assertions.assertEquals(Integer.valueOf(0), integer);

		final Map<?, ?> map = ConstructorUtil.newInstanceIfPossible(Map.class);
		Assertions.assertNotNull(map);

		final Collection<?> collection = ConstructorUtil.newInstanceIfPossible(Collection.class);
		Assertions.assertNotNull(collection);

		final Week week = ConstructorUtil.newInstanceIfPossible(Week.class);
		Assertions.assertEquals(Week.SUNDAY, week);

		final int[] intArray = ConstructorUtil.newInstanceIfPossible(int[].class);
		Assertions.assertArrayEquals(new int[0], intArray);
	}

	@Test
	void newInstanceTest() {
		final TestBean testBean = ConstructorUtil.newInstance(TestBean.class);
		Assertions.assertNull(testBean.getA());
		Assertions.assertEquals(0, testBean.getB());
	}

	@Test
	void newInstanceAllArgsTest() {
		final TestBean testBean = ConstructorUtil.newInstance(TestBean.class, "aValue", 1);
		Assertions.assertEquals("aValue", testBean.getA());
		Assertions.assertEquals(1, testBean.getB());
	}

	@Test
	void newInstanceHashtableTest() {
		final Hashtable<?, ?> testBean = ConstructorUtil.newInstance(Hashtable.class);
		Assertions.assertNotNull(testBean);
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	private static class TestBean{
		private String a;
		private int b;
	}
}
