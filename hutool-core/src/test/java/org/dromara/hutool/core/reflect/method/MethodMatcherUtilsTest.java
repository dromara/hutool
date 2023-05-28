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
		MethodMatcher methodMatcher = MethodMatcherUtil.forName("noneReturnNoArgs");
		Assertions.assertTrue(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
	}

	@Test
	void forNameIgnoreCase() {
		MethodMatcher methodMatcher = MethodMatcherUtil.forNameIgnoreCase("noneReturnNoArgs");
		Assertions.assertTrue(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
		// if name is upper case, it will be ignored
		methodMatcher = MethodMatcherUtil.forNameIgnoreCase("NONERETURNNOARGS");
		Assertions.assertTrue(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
	}

	@Test
	void forNoneReturnType() {
		MethodMatcher methodMatcher = MethodMatcherUtil.forNoneReturnType();
		Assertions.assertTrue(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(returnNoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));
		Assertions.assertFalse(methodMatcher.test(returnTwoArgs));
	}

	@Test
	void forReturnType() {
		MethodMatcher methodMatcher = MethodMatcherUtil.forReturnType(Collection.class);
		Assertions.assertFalse(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertTrue(methodMatcher.test(returnNoArgs));
		Assertions.assertTrue(methodMatcher.test(returnOneArgs));
		Assertions.assertTrue(methodMatcher.test(returnTwoArgs));
	}

	@Test
	void forStrictReturnType() {
		MethodMatcher methodMatcher = MethodMatcherUtil.forStrictReturnType(Collection.class);
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
		MethodMatcher methodMatcher = MethodMatcherUtil.forParameterCount(2);
		Assertions.assertFalse(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnTwoArgs));
	}

	@Test
	void forMostSpecificParameterTypes() {
		// match none args method
		MethodMatcher methodMatcher = MethodMatcherUtil.forMostSpecificParameterTypes();
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
		MethodMatcher methodMatcher = MethodMatcherUtil.forMostSpecificStrictParameterTypes();
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
		MethodMatcher methodMatcher = MethodMatcherUtil.forParameterTypes();
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
		MethodMatcher methodMatcher = MethodMatcherUtil.forStrictParameterTypes();
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
		MethodMatcher methodMatcher = MethodMatcherUtil.noneMatch();
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
		MethodMatcher methodMatcher = MethodMatcherUtil.anyMatch();
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
		MethodMatcher methodMatcher = MethodMatcherUtil.allMatch();
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
		MethodMatcher methodMatcher = MethodMatcherUtil.isPublic();
		Assertions.assertFalse(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertTrue(methodMatcher.test(returnNoArgs));
		Assertions.assertTrue(methodMatcher.test(returnOneArgs));
		Assertions.assertTrue(methodMatcher.test(returnTwoArgs));
	}

	@Test
	void isStatic() {
		MethodMatcher methodMatcher = MethodMatcherUtil.isStatic();
		Assertions.assertFalse(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(returnNoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));
		Assertions.assertFalse(methodMatcher.test(returnTwoArgs));
	}

	@Test
	void isPublicStatic() {
		MethodMatcher methodMatcher = MethodMatcherUtil.isPublicStatic();
		Assertions.assertFalse(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(returnNoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));
		Assertions.assertFalse(methodMatcher.test(returnTwoArgs));
	}

	@Test
	void forModifiers() {
		MethodMatcher methodMatcher = MethodMatcherUtil.forModifiers(Modifier.PUBLIC, Modifier.STATIC);
		Assertions.assertFalse(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(returnNoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));
		Assertions.assertFalse(methodMatcher.test(returnTwoArgs));
	}

	@Test
	void forNameAndParameterTypes() {
		MethodMatcher methodMatcher = MethodMatcherUtil.forNameAndParameterTypes("noneReturnTwoArgs", CharSequence.class, List.class);
		Assertions.assertFalse(methodMatcher.test(noneReturnNoArgs));
		Assertions.assertFalse(methodMatcher.test(noneReturnOneArgs));
		Assertions.assertTrue(methodMatcher.test(noneReturnTwoArgs));
		Assertions.assertFalse(methodMatcher.test(returnNoArgs));
		Assertions.assertFalse(methodMatcher.test(returnOneArgs));
		Assertions.assertFalse(methodMatcher.test(returnTwoArgs));
	}

	@Test
	void forNameAndStrictParameterTypes() {
		MethodMatcher methodMatcher = MethodMatcherUtil.forNameAndStrictParameterTypes("noneReturnTwoArgs", CharSequence.class, List.class);
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
		MethodMatcher methodMatcher = MethodMatcherUtil.forNameIgnoreCaseAndParameterTypes("NONEReturnTWOArgs", CharSequence.class, List.class);
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
		MethodMatcher methodMatcher = MethodMatcherUtil.forNameIgnoreCaseAndStrictParameterTypes("NONEReturnTWOArgs", CharSequence.class, List.class);
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
		MethodMatcher methodMatcher = MethodMatcherUtil.forStrictMethodSignature("noneReturnTwoArgs", null, CharSequence.class, Collection.class);
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
		MethodMatcher methodMatcher = MethodMatcherUtil.forStrictMethodSignature(noneReturnTwoArgs);
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
		MethodMatcher methodMatcher = MethodMatcherUtil.forMethodSignature(noneReturnTwoArgs2);
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
		MethodMatcher methodMatcher = MethodMatcherUtil.forMethodSignature(
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
		Field nameField = Foo.class.getDeclaredField("name");
		MethodMatcher methodMatcher = MethodMatcherUtil.forGetterMethod(nameField);
		Method getName = Foo.class.getMethod("getName");
		Assertions.assertTrue(methodMatcher.test(getName));

		Field flagField = Foo.class.getDeclaredField("flag");
		methodMatcher = MethodMatcherUtil.forGetterMethod(flagField);
		Method isFlag = Foo.class.getMethod("isFlag");
		Assertions.assertTrue(methodMatcher.test(isFlag));

		Field objectField = Foo.class.getDeclaredField("object");
		methodMatcher = MethodMatcherUtil.forGetterMethod(objectField);
		Method object = Foo.class.getMethod("object");
		Assertions.assertTrue(methodMatcher.test(object));
	}

	@Test
	@SneakyThrows
	void forGetterMethod() {
		MethodMatcher methodMatcher = MethodMatcherUtil.forGetterMethod("name", String.class);
		Method getName = Foo.class.getMethod("getName");
		Assertions.assertTrue(methodMatcher.test(getName));

		methodMatcher = MethodMatcherUtil.forGetterMethod("flag", boolean.class);
		Method isFlag = Foo.class.getMethod("isFlag");
		Assertions.assertTrue(methodMatcher.test(isFlag));

		methodMatcher = MethodMatcherUtil.forGetterMethod("object", Object.class);
		Method object = Foo.class.getMethod("object");
		Assertions.assertTrue(methodMatcher.test(object));
	}

	@Test
	@SneakyThrows
	void forSetterMethodWithField() {
		Field nameField = Foo.class.getDeclaredField("name");
		MethodMatcher methodMatcher = MethodMatcherUtil.forSetterMethod(nameField);
		Method setName = Foo.class.getMethod("setName", String.class);
		Assertions.assertTrue(methodMatcher.test(setName));

		Field flagField = Foo.class.getDeclaredField("flag");
		methodMatcher = MethodMatcherUtil.forSetterMethod(flagField);
		Method setFlag = Foo.class.getMethod("setFlag", boolean.class);
		Assertions.assertTrue(methodMatcher.test(setFlag));

		Field objectField = Foo.class.getDeclaredField("object");
		methodMatcher = MethodMatcherUtil.forSetterMethod(objectField);
		Method object = Foo.class.getMethod("object", Object.class);
		Assertions.assertTrue(methodMatcher.test(object));
	}

	@Test
	@SneakyThrows
	void forSetterMethod() {
		MethodMatcher methodMatcher = MethodMatcherUtil.forSetterMethod("name", String.class);
		Method setName = Foo.class.getMethod("setName", String.class);
		Assertions.assertTrue(methodMatcher.test(setName));

		methodMatcher = MethodMatcherUtil.forSetterMethod("flag", boolean.class);
		Method setFlag = Foo.class.getMethod("setFlag", boolean.class);
		Assertions.assertTrue(methodMatcher.test(setFlag));

		methodMatcher = MethodMatcherUtil.forSetterMethod("object", Object.class);
		Method object = Foo.class.getMethod("object", Object.class);
		Assertions.assertTrue(methodMatcher.test(object));
	}

	@Test
	@SneakyThrows
	void hasDeclaredAnnotation() {
		MethodMatcher methodMatcher = MethodMatcherUtil.hasDeclaredAnnotation(GrandParentAnnotation.class);
		Assertions.assertFalse(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("annotatedByChildAnnotation")));
		Assertions.assertFalse(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("annotatedByParentAnnotation")));
		Assertions.assertTrue(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("annotatedByGrandParentAnnotation")));
		Assertions.assertFalse(methodMatcher.test(AnnotatedClass.class.getDeclaredMethod("noneAnnotated")));
	}

	@Test
	@SneakyThrows
	void hasAnnotation() {
		MethodMatcher methodMatcher = MethodMatcherUtil.hasAnnotation(GrandParentAnnotation.class);
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
		MethodMatcher methodMatcher = MethodMatcherUtil.hasAnnotationOnDeclaringClass(GrandParentAnnotation.class);
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
		MethodMatcher methodMatcher = MethodMatcherUtil.hasAnnotationOnMethodOrDeclaringClass(GrandParentAnnotation.class);
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
		public void setName(String name, Void none) { }

		public Object object() {
			return object;
		}

		public Foo object(Object object) {
			this.object = object;
			return this;
		}
	}

	private void noneReturnNoArgs() { }
	private static void noneReturnOneArgs(String arg1) { }
	public static void noneReturnTwoArgs(String arg1, List<String> stringList) { }
	public static void noneReturnTwoArgs(CharSequence arg1, Collection<String> stringList) { }
	public List<String> returnNoArgs() { return null; }
	public Set<String> returnOneArgs(String arg1) { return null; }
	public List<String> returnTwoArgs(String arg1, List<String> stringList) { return null; }
}
