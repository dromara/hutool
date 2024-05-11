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

package org.dromara.hutool.core.reflect.method;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * test for {@link MethodMatcherUtil}
 *
 * @author huangchengxing
 */
class MethodMatcherUtilsTest {

	private Method noneReturnNoArgs;
	private Method noneReturnOneArgs;
	private Method noneReturnTwoArgs;
	private Method noneReturnTwoArgs2;
	private Method returnNoArgs;
	private Method returnOneArgs;
	private Method returnTwoArgs;

	@SneakyThrows
	@BeforeEach
	void init() {
		this.noneReturnNoArgs = MethodMatcherUtilsTest.class.getDeclaredMethod("noneReturnNoArgs");
		this.noneReturnOneArgs = MethodMatcherUtilsTest.class.getDeclaredMethod("noneReturnOneArgs", String.class);
		this.noneReturnTwoArgs = MethodMatcherUtilsTest.class.getDeclaredMethod("noneReturnTwoArgs", String.class, List.class);
		this.noneReturnTwoArgs2 = MethodMatcherUtilsTest.class.getDeclaredMethod("noneReturnTwoArgs", CharSequence.class, Collection.class);
		this.returnNoArgs = MethodMatcherUtilsTest.class.getDeclaredMethod("returnNoArgs");
		this.returnOneArgs = MethodMatcherUtilsTest.class.getDeclaredMethod("returnOneArgs", String.class);
		this.returnTwoArgs = MethodMatcherUtilsTest.class.getDeclaredMethod("returnTwoArgs", String.class, List.class);
	}

	@Test
	void testForName() {
		final Predicate<Method> methodMatcher = MethodMatcherUtil.forName("noneReturnNoArgs");
		Assertions.assertTrue(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
	}

	@Test
	void forNameIgnoreCase() {
		Predicate<Method> methodMatcher = MethodMatcherUtil.forNameIgnoreCase("noneReturnNoArgs");
		Assertions.assertTrue(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
		// if name is upper case, it will be ignored
		methodMatcher = MethodMatcherUtil.forNameIgnoreCase("NONERETURNNOARGS");
		Assertions.assertTrue(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
	}

	@Test
	void forNoneReturnType() {
		final Predicate<Method> methodMatcher = MethodMatcherUtil.forNoneReturnType();
		Assertions.assertTrue(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(returnNoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));
		Assertions.assertFalse(methodMatcher.test(returnTwoArgs));
	}

	@Test
	void forReturnType() {
		final Predicate<Method> methodMatcher = MethodMatcherUtil.forReturnType(Collection.class);
		Assertions.assertFalse(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertTrue(methodMatcher.test(returnNoArgs));
		Assertions.assertTrue(methodMatcher.test(returnOneArgs));
		Assertions.assertTrue(methodMatcher.test(returnTwoArgs));
	}

	@Test
	void forStrictReturnType() {
		Predicate<Method> methodMatcher = MethodMatcherUtil.forStrictReturnType(Collection.class);
		Assertions.assertFalse(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(returnNoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));
		Assertions.assertFalse(methodMatcher.test(returnTwoArgs));
		// only match return type is strict equal to parameter type
		methodMatcher = MethodMatcherUtil.forStrictReturnType(List.class);
		Assertions.assertFalse(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertTrue(methodMatcher.test(returnNoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));
		Assertions.assertTrue(methodMatcher.test(returnTwoArgs));
	}

	@Test
	void forParameterCount() {
		final Predicate<Method> methodMatcher = MethodMatcherUtil.forParameterCount(2);
		Assertions.assertFalse(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnTwoArgs));
	}

	@Test
	void forMostSpecificParameterTypes() {
		// match none args method
		Predicate<Method> methodMatcher = MethodMatcherUtil.forMostSpecificParameterTypes();
		Assertions.assertFalse(methodMatcher.test(returnTwoArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnNoArgs));

		// match all args types
		methodMatcher = MethodMatcherUtil.forMostSpecificParameterTypes(null, null);
		Assertions.assertTrue(methodMatcher.test(returnTwoArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));

		// match first arg type
		methodMatcher = MethodMatcherUtil.forMostSpecificParameterTypes(CharSequence.class, null);
		Assertions.assertTrue(methodMatcher.test(returnTwoArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));

		// match second arg type
		methodMatcher = MethodMatcherUtil.forMostSpecificParameterTypes(null, Collection.class);
		Assertions.assertTrue(methodMatcher.test(returnTwoArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));

		// match two arg type
		methodMatcher = MethodMatcherUtil.forMostSpecificParameterTypes(CharSequence.class, Collection.class);
		Assertions.assertTrue(methodMatcher.test(returnTwoArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));
	}

	@Test
	void forMostSpecificStrictParameterTypes() {
		// match none args method
		Predicate<Method> methodMatcher = MethodMatcherUtil.forMostSpecificStrictParameterTypes();
		Assertions.assertFalse(methodMatcher.test(returnTwoArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnNoArgs));

		// match all args types
		methodMatcher = MethodMatcherUtil.forMostSpecificStrictParameterTypes(null, null);
		Assertions.assertTrue(methodMatcher.test(returnTwoArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));

		// match first arg type
		methodMatcher = MethodMatcherUtil.forMostSpecificStrictParameterTypes(CharSequence.class, null);
		Assertions.assertFalse(methodMatcher.test(returnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));
		methodMatcher = MethodMatcherUtil.forMostSpecificStrictParameterTypes(String.class, null);
		Assertions.assertTrue(methodMatcher.test(returnTwoArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));

		// match second arg type
		methodMatcher = MethodMatcherUtil.forMostSpecificStrictParameterTypes(null, Collection.class);
		Assertions.assertFalse(methodMatcher.test(returnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));

		// match two arg type
		methodMatcher = MethodMatcherUtil.forMostSpecificStrictParameterTypes(CharSequence.class, Collection.class);
		Assertions.assertFalse(methodMatcher.test(returnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));
	}

	@Test
	void forParameterTypes() {
		Predicate<Method> methodMatcher = MethodMatcherUtil.forParameterTypes();
		Assertions.assertTrue(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertTrue(methodMatcher.test(returnNoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));
		Assertions.assertFalse(methodMatcher.test(returnTwoArgs));
		// match parameter types is empty
		methodMatcher = MethodMatcherUtil.forParameterTypes(CharSequence.class, Collection.class);
		Assertions.assertFalse(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(returnNoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));
		Assertions.assertTrue(methodMatcher.test(returnTwoArgs));
	}

	@Test
	void forStrictParameterTypes() {
		Predicate<Method> methodMatcher = MethodMatcherUtil.forStrictParameterTypes();
		Assertions.assertTrue(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertTrue(methodMatcher.test(returnNoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));
		Assertions.assertFalse(methodMatcher.test(returnTwoArgs));

		// cannot match assignable parameter types
		methodMatcher = MethodMatcherUtil.forStrictParameterTypes(CharSequence.class, Collection.class);
		Assertions.assertFalse(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(returnNoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));
		Assertions.assertFalse(methodMatcher.test(returnTwoArgs));

		// only match parameter types is strict equal to parameter type
		methodMatcher = MethodMatcherUtil.forStrictParameterTypes(String.class, List.class);
		Assertions.assertFalse(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(returnNoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));
		Assertions.assertTrue(methodMatcher.test(returnTwoArgs));
	}

	@Test
	void noneMatch() {
		Predicate<Method> methodMatcher = MethodMatcherUtil.noneMatch();
		Assertions.assertTrue(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertTrue(methodMatcher.test(returnNoArgs));
		Assertions.assertTrue(methodMatcher.test(returnOneArgs));
		Assertions.assertTrue(methodMatcher.test(returnTwoArgs));

		// combine with other matchers
		methodMatcher = MethodMatcherUtil.noneMatch(
			MethodMatcherUtil.forName("noneReturnNoArgs"),
			MethodMatcherUtil.forReturnType(Collection.class)
		);
		Assertions.assertFalse(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnOneArgs));
	}

	@Test
	void anyMatch() {
		Predicate<Method> methodMatcher = MethodMatcherUtil.anyMatch();
		Assertions.assertFalse(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(returnNoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));
		Assertions.assertFalse(methodMatcher.test(returnTwoArgs));

		// combine with other matchers
		methodMatcher = MethodMatcherUtil.anyMatch(
			MethodMatcherUtil.forName("noneReturnNoArgs"),
			MethodMatcherUtil.forReturnType(Collection.class)
		);
		Assertions.assertTrue(methodMatcher.test(returnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertTrue(methodMatcher.test(returnTwoArgs));
	}

	@Test
	void allMatch() {
		Predicate<Method> methodMatcher = MethodMatcherUtil.allMatch();
		Assertions.assertTrue(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertTrue(methodMatcher.test(returnNoArgs));
		Assertions.assertTrue(methodMatcher.test(returnOneArgs));
		Assertions.assertTrue(methodMatcher.test(returnTwoArgs));

		// combine with other matchers
		methodMatcher = MethodMatcherUtil.allMatch(
			MethodMatcherUtil.forName("noneReturnNoArgs"),
			MethodMatcherUtil.forReturnType(Collection.class)
		);
		Assertions.assertFalse(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(returnNoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));
		Assertions.assertFalse(methodMatcher.test(returnTwoArgs));
	}

	@Test
	void isPublic() {
		final Predicate<Method> methodMatcher = MethodMatcherUtil.isPublic();
		Assertions.assertFalse(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertTrue(methodMatcher.test(returnNoArgs));
		Assertions.assertTrue(methodMatcher.test(returnOneArgs));
		Assertions.assertTrue(methodMatcher.test(returnTwoArgs));
	}

	@Test
	void isStatic() {
		final Predicate<Method> methodMatcher = MethodMatcherUtil.isStatic();
		Assertions.assertFalse(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(returnNoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));
		Assertions.assertFalse(methodMatcher.test(returnTwoArgs));
	}

	@Test
	void isPublicStatic() {
		final Predicate<Method> methodMatcher = MethodMatcherUtil.isPublicStatic();
		Assertions.assertFalse(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(returnNoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));
		Assertions.assertFalse(methodMatcher.test(returnTwoArgs));
	}

	@Test
	void forModifiers() {
		final Predicate<Method> methodMatcher = MethodMatcherUtil.forModifiers(Modifier.PUBLIC, Modifier.STATIC);
		Assertions.assertFalse(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(returnNoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));
		Assertions.assertFalse(methodMatcher.test(returnTwoArgs));
	}

	@Test
	void forNameAndParameterTypes() {
		final Predicate<Method> methodMatcher = MethodMatcherUtil.forNameAndParameterTypes("noneReturnTwoArgs", CharSequence.class, List.class);
		Assertions.assertFalse(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(returnNoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));
		Assertions.assertFalse(methodMatcher.test(returnTwoArgs));
	}

	@Test
	void forNameAndStrictParameterTypes() {
		Predicate<Method> methodMatcher = MethodMatcherUtil.forNameAndStrictParameterTypes("noneReturnTwoArgs", CharSequence.class, List.class);
		Assertions.assertFalse(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(returnNoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));
		Assertions.assertFalse(methodMatcher.test(returnTwoArgs));

		methodMatcher = MethodMatcherUtil.forNameAndStrictParameterTypes("returnTwoArgs", String.class, List.class);
		Assertions.assertFalse(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(returnNoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));
		Assertions.assertTrue(methodMatcher.test(returnTwoArgs));
	}

	@Test
	void forNameIgnoreCaseAndParameterTypes() {
		Predicate<Method> methodMatcher = MethodMatcherUtil.forNameIgnoreCaseAndParameterTypes("NONEReturnTWOArgs", CharSequence.class, List.class);
		Assertions.assertFalse(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(returnNoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));
		Assertions.assertFalse(methodMatcher.test(returnTwoArgs));

		methodMatcher = MethodMatcherUtil.forNameIgnoreCaseAndParameterTypes("ReturnTWOArgs", String.class, List.class);
		Assertions.assertFalse(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(returnNoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));
		Assertions.assertTrue(methodMatcher.test(returnTwoArgs));
	}

	@Test
	void forNameIgnoreCaseAndStrictParameterTypes() {
		Predicate<Method> methodMatcher = MethodMatcherUtil.forNameIgnoreCaseAndStrictParameterTypes("NONEReturnTWOArgs", CharSequence.class, List.class);
		Assertions.assertFalse(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(returnNoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));
		Assertions.assertFalse(methodMatcher.test(returnTwoArgs));

		methodMatcher = MethodMatcherUtil.forNameIgnoreCaseAndStrictParameterTypes("ReturnTWOArgs", String.class, List.class);
		Assertions.assertFalse(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(returnNoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));
		Assertions.assertTrue(methodMatcher.test(returnTwoArgs));
	}

	@Test
	void forStrictMethodSignature() {
		Predicate<Method> methodMatcher = MethodMatcherUtil.forStrictMethodSignature("noneReturnTwoArgs", null, CharSequence.class, Collection.class);
		Assertions.assertFalse(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnTwoArgs2));
		Assertions.assertFalse(methodMatcher.test(returnNoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));
		Assertions.assertFalse(methodMatcher.test(returnTwoArgs));

		methodMatcher = MethodMatcherUtil.forStrictMethodSignature("noneReturnTwoArgs", null, String.class, List.class);
		Assertions.assertFalse(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnTwoArgs2));
		Assertions.assertFalse(methodMatcher.test(returnNoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));
		Assertions.assertFalse(methodMatcher.test(returnTwoArgs));
	}

	@Test
	void forStrictMethodSignatureWithMethod() {
		Predicate<Method> methodMatcher = MethodMatcherUtil.forStrictMethodSignature(noneReturnTwoArgs);
		Assertions.assertFalse(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(returnNoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));
		Assertions.assertFalse(methodMatcher.test(returnTwoArgs));

		methodMatcher = MethodMatcherUtil.forStrictMethodSignature(returnTwoArgs);
		Assertions.assertFalse(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(returnNoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));
		Assertions.assertTrue(methodMatcher.test(returnTwoArgs));
	}

	@Test
	void forMethodSignatureWithMethod() {
		final Predicate<Method> methodMatcher = MethodMatcherUtil.forMethodSignature(noneReturnTwoArgs2);
		Assertions.assertFalse(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(returnNoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));
		Assertions.assertFalse(methodMatcher.test(returnTwoArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnTwoArgs2));
	}

	@Test
	void forMethodSignature() {
		final Predicate<Method> methodMatcher = MethodMatcherUtil.forMethodSignature(
			"noneReturnTwoArgs", null, CharSequence.class, Collection.class
		);
		Assertions.assertFalse(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(returnNoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));
		Assertions.assertFalse(methodMatcher.test(returnTwoArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnTwoArgs2));
	}

	@Test
	@SneakyThrows
	void forGetterMethodWithField() {
		final Field nameField = Foo.class.getDeclaredField("name");
		Predicate<Method> methodMatcher = MethodMatcherUtil.forGetterMethod(nameField);
		final Method getName = Foo.class.getMethod("getName");
		Assertions.assertTrue(methodMatcher.test(getName));

		final Field flagField = Foo.class.getDeclaredField("flag");
		methodMatcher = MethodMatcherUtil.forGetterMethod(flagField);
		final Method isFlag = Foo.class.getMethod("isFlag");
		Assertions.assertTrue(methodMatcher.test(isFlag));

		final Field objectField = Foo.class.getDeclaredField("object");
		methodMatcher = MethodMatcherUtil.forGetterMethod(objectField);
		final Method object = Foo.class.getMethod("object");
		Assertions.assertTrue(methodMatcher.test(object));
	}

	@Test
	@SneakyThrows
	void forGetterMethod() {
		Predicate<Method> methodMatcher = MethodMatcherUtil.forGetterMethod("name", String.class);
		final Method getName = Foo.class.getMethod("getName");
		Assertions.assertTrue(methodMatcher.test(getName));

		methodMatcher = MethodMatcherUtil.forGetterMethod("flag", boolean.class);
		final Method isFlag = Foo.class.getMethod("isFlag");
		Assertions.assertTrue(methodMatcher.test(isFlag));

		methodMatcher = MethodMatcherUtil.forGetterMethod("object", Object.class);
		final Method object = Foo.class.getMethod("object");
		Assertions.assertTrue(methodMatcher.test(object));
	}

	@Test
	@SneakyThrows
	void forSetterMethodWithField() {
		final Field nameField = Foo.class.getDeclaredField("name");
		Predicate<Method> methodMatcher = MethodMatcherUtil.forSetterMethod(nameField);
		final Method setName = Foo.class.getMethod("setName", String.class);
		Assertions.assertTrue(methodMatcher.test(setName));

		final Field flagField = Foo.class.getDeclaredField("flag");
		methodMatcher = MethodMatcherUtil.forSetterMethod(flagField);
		final Method setFlag = Foo.class.getMethod("setFlag", boolean.class);
		Assertions.assertTrue(methodMatcher.test(setFlag));

		final Field objectField = Foo.class.getDeclaredField("object");
		methodMatcher = MethodMatcherUtil.forSetterMethod(objectField);
		final Method object = Foo.class.getMethod("object", Object.class);
		Assertions.assertTrue(methodMatcher.test(object));
	}

	@Test
	@SneakyThrows
	void forSetterMethod() {
		Predicate<Method> methodMatcher = MethodMatcherUtil.forSetterMethod("name", String.class);
		final Method setName = Foo.class.getMethod("setName", String.class);
		Assertions.assertTrue(methodMatcher.test(setName));

		methodMatcher = MethodMatcherUtil.forSetterMethod("flag", boolean.class);
		final Method setFlag = Foo.class.getMethod("setFlag", boolean.class);
		Assertions.assertTrue(methodMatcher.test(setFlag));

		methodMatcher = MethodMatcherUtil.forSetterMethod("object", Object.class);
		final Method object = Foo.class.getMethod("object", Object.class);
		Assertions.assertTrue(methodMatcher.test(object));
	}

	@Test
	@SneakyThrows
	void hasDeclaredAnnotation() {
		final Predicate<Method> methodMatcher = MethodMatcherUtil.hasDeclaredAnnotation(GrandParentAnnotation.class);
		Assertions.assertFalse(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("annotatedByChildAnnotation")));
		Assertions.assertFalse(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("annotatedByParentAnnotation")));
		Assertions.assertTrue(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("annotatedByGrandParentAnnotation")));
		Assertions.assertFalse(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("noneAnnotated")));
	}

	@Test
	@SneakyThrows
	void hasAnnotation() {
		Predicate<Method> methodMatcher = MethodMatcherUtil.hasAnnotation(GrandParentAnnotation.class);
		Assertions.assertTrue(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("annotatedByChildAnnotation")));
		Assertions.assertTrue(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("annotatedByParentAnnotation")));
		Assertions.assertTrue(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("annotatedByGrandParentAnnotation")));
		Assertions.assertFalse(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("noneAnnotated")));

		methodMatcher = MethodMatcherUtil.hasAnnotation(ParentAnnotation.class);
		Assertions.assertTrue(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("annotatedByChildAnnotation")));
		Assertions.assertTrue(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("annotatedByParentAnnotation")));
		Assertions.assertFalse(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("annotatedByGrandParentAnnotation")));
		Assertions.assertFalse(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("noneAnnotated")));
	}

	@Test
	@SneakyThrows
	void hasAnnotationOnDeclaringClass() {
		Predicate<Method> methodMatcher = MethodMatcherUtil.hasAnnotationOnDeclaringClass(GrandParentAnnotation.class);
		Assertions.assertTrue(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("annotatedByChildAnnotation")));
		Assertions.assertTrue(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("annotatedByParentAnnotation")));
		Assertions.assertTrue(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("annotatedByGrandParentAnnotation")));
		Assertions.assertTrue(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("noneAnnotated")));

		methodMatcher = MethodMatcherUtil.hasAnnotationOnDeclaringClass(ParentAnnotation.class);
		Assertions.assertTrue(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("annotatedByChildAnnotation")));
		Assertions.assertTrue(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("annotatedByParentAnnotation")));
		Assertions.assertTrue(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("annotatedByGrandParentAnnotation")));
		Assertions.assertTrue(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("noneAnnotated")));

		methodMatcher = MethodMatcherUtil.hasAnnotationOnDeclaringClass(ChildAnnotation.class);
		Assertions.assertFalse(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("annotatedByChildAnnotation")));
		Assertions.assertFalse(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("annotatedByParentAnnotation")));
		Assertions.assertFalse(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("annotatedByGrandParentAnnotation")));
		Assertions.assertFalse(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("noneAnnotated")));
	}

	@Test
	@SneakyThrows
	void hasAnnotationOnMethodOrDeclaringClass() {
		Predicate<Method> methodMatcher = MethodMatcherUtil.hasAnnotationOnMethodOrDeclaringClass(GrandParentAnnotation.class);
		Assertions.assertTrue(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("annotatedByChildAnnotation")));
		Assertions.assertTrue(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("annotatedByParentAnnotation")));
		Assertions.assertTrue(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("annotatedByGrandParentAnnotation")));
		Assertions.assertTrue(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("noneAnnotated")));

		methodMatcher = MethodMatcherUtil.hasAnnotationOnMethodOrDeclaringClass(ParentAnnotation.class);
		Assertions.assertTrue(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("annotatedByChildAnnotation")));
		Assertions.assertTrue(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("annotatedByParentAnnotation")));
		Assertions.assertTrue(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("annotatedByGrandParentAnnotation")));
		Assertions.assertTrue(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("noneAnnotated")));

		methodMatcher = MethodMatcherUtil.hasAnnotationOnMethodOrDeclaringClass(ChildAnnotation.class);
		Assertions.assertTrue(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("annotatedByChildAnnotation")));
		Assertions.assertFalse(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("annotatedByParentAnnotation")));
		Assertions.assertFalse(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("annotatedByGrandParentAnnotation")));
		Assertions.assertFalse(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("noneAnnotated")));
	}

	@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	private @interface GrandParentAnnotation {}

	@GrandParentAnnotation
	@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	private @interface ParentAnnotation {}

	@ParentAnnotation
	@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	private @interface ChildAnnotation {}

	@ParentAnnotation
	private static class AnnotatedClass {

		@ChildAnnotation
		private void annotatedByChildAnnotation() { }
		@ParentAnnotation
		private static void annotatedByParentAnnotation() { }
		@GrandParentAnnotation
		public static void annotatedByGrandParentAnnotation() { }
		public static void noneAnnotated() { }
	}

	private static class Foo {
		@Setter
		@Getter
		private String name;
		@Setter
		@Getter
		private boolean flag;
		private Object object;
		public void setName(final String name, final Void none) { }

		public Object object() {
			return object;
		}

		public Foo object(final Object object) {
			this.object = object;
			return this;
		}
	}

	private void noneReturnNoArgs() { }
	private static void noneReturnOneArgs(final String arg1) { }
	public static void noneReturnTwoArgs(final String arg1, final List<String> stringList) { }
	public static void noneReturnTwoArgs(final CharSequence arg1, final Collection<String> stringList) { }
	public List<String> returnNoArgs() { return null; }
	public Set<String> returnOneArgs(final String arg1) { return null; }
	public List<String> returnTwoArgs(final String arg1, final List<String> stringList) { return null; }
}
