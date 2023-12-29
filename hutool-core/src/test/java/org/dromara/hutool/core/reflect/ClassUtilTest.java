/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.reflect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * test for {@link ClassUtil}
 *
 * @author huangchengxing
 */
class ClassUtilTest {

	@Test
	void testGetSuperClasses() {
		// if root is null
		List<Class<?>> superclasses = ClassUtil.getSuperClasses(null);
		Assertions.assertEquals(0, superclasses.size());
		// if root not null
		superclasses = ClassUtil.getSuperClasses(Child.class);
		Assertions.assertEquals(3, superclasses.size());
		Assertions.assertEquals(Parent.class, superclasses.get(0));
		Assertions.assertEquals(GrandParent.class, superclasses.get(1));
		Assertions.assertEquals(Object.class, superclasses.get(2));
	}

	@Test
	void testGetInterface() {
		// if root is null
		List<Class<?>> interfaces = ClassUtil.getInterfaces(null);
		Assertions.assertEquals(0, interfaces.size());
		// if root not null
		interfaces = ClassUtil.getInterfaces(Child.class);
		Assertions.assertEquals(4, interfaces.size());
		Assertions.assertEquals(Mother.class, interfaces.get(0));
		Assertions.assertEquals(Father.class, interfaces.get(1));
		Assertions.assertEquals(GrandMother.class, interfaces.get(2));
		Assertions.assertEquals(GrandFather.class, interfaces.get(3));
	}

	@Test
	void testTraverseTypeHierarchy() {
		// collect all superclass of child by bfs (include child)
		final List<Class<?>> superclasses = new ArrayList<>();
		ClassUtil.traverseTypeHierarchy(
			Child.class, t -> !t.isInterface(), superclasses::add, true
		);
		Assertions.assertEquals(4, superclasses.size());
		Assertions.assertEquals(Child.class, superclasses.get(0));
		Assertions.assertEquals(Parent.class, superclasses.get(1));
		Assertions.assertEquals(GrandParent.class, superclasses.get(2));
		Assertions.assertEquals(Object.class, superclasses.get(3));

		// collect all superclass of child by bfs (exclude child)
		superclasses.clear();
		ClassUtil.traverseTypeHierarchy(
			Child.class, t -> !t.isInterface(), superclasses::add, false
		);
		Assertions.assertEquals(3, superclasses.size());
		Assertions.assertEquals(Parent.class, superclasses.get(0));
		Assertions.assertEquals(GrandParent.class, superclasses.get(1));
		Assertions.assertEquals(Object.class, superclasses.get(2));
	}

	@Test
	void testTraverseTypeHierarchyWithTerminator() {
		// collect all superclass of child until Parent by bfs (include child)
		final List<Class<?>> superclasses = new ArrayList<>();
		ClassUtil.traverseTypeHierarchyWhile(
			Child.class, t -> !t.isInterface(), t -> {
				if (!Objects.equals(t, GrandParent.class)) {
					superclasses.add(t);
					return true;
				}
				return false;
			}
		);
		Assertions.assertEquals(2, superclasses.size());
		Assertions.assertEquals(Child.class, superclasses.get(0));
		Assertions.assertEquals(Parent.class, superclasses.get(1));

		// collect all class of child until GrandMother or GrandFather by bfs (include child)
		superclasses.clear();
		ClassUtil.traverseTypeHierarchyWhile(
			Child.class, t -> {
				if (!Objects.equals(t, GrandMother.class) && !Objects.equals(t, GrandFather.class)) {
					superclasses.add(t);
					return true;
				}
				return false;
			}
		);
		Assertions.assertEquals(6, superclasses.size());
		Assertions.assertEquals(Child.class, superclasses.get(0));
		Assertions.assertEquals(Parent.class, superclasses.get(1));
		Assertions.assertEquals(GrandParent.class, superclasses.get(2));
		Assertions.assertEquals(Mother.class, superclasses.get(3));
		Assertions.assertEquals(Father.class, superclasses.get(4));
		Assertions.assertEquals(Object.class, superclasses.get(5));
	}

	private interface Mother {}

	private interface Father {}

	private interface GrandMother extends Mother {}

	private interface GrandFather extends Father {}

	private static class GrandParent implements GrandMother, GrandFather {}
	private static class Parent extends GrandParent implements Mother, Father {}

	private static class Child extends Parent {}
}
