package cn.hutool.core.lang.func;

import org.junit.Assert;
import org.junit.Test;

import lombok.Data;

public class LambdaUtilTest {

	@Test
	public void getMethodNameTest() {
		String methodName = LambdaUtil.getMethodName(MyTeacher::getAge);
		Assert.assertEquals("getAge", methodName);
	}

	@Test
	public void getFieldNameTest() {
		String fieldName = LambdaUtil.getFieldName(MyTeacher::getAge);
		Assert.assertEquals("age", fieldName);
	}

	@Test
	public void getImplClassTest() {
		// 一般用于封装工具类时减少参数使用
		Class<MyTeacher> aClass = LambdaUtil.getInstantiatedClass(MyTeacher::getAge);
		Assert.assertEquals(MyTeacher.class, aClass);

		// 一般用于封装工具类时减少参数使用
		MyTeacher myTeacher = new MyTeacher();
		Class<MyTeacher> bClass = LambdaUtil.getImplClass(myTeacher::getAge);
		Assert.assertEquals(MyTeacher.class, bClass);
	}

	@Data
	static class MyTeacher {

		public String age;
	}
}
