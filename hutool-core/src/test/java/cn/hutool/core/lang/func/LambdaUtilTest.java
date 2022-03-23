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
		Class<MyTeacher> aClass = LambdaUtil.getImplClass(MyTeacher::getAge);
		Assert.assertEquals(MyTeacher.class, aClass);

		MyTeacher myTeacher = new MyTeacher();
		Class<MyTeacher> bClass = LambdaUtil.getImplClass(myTeacher::getAge);
		Assert.assertEquals(MyTeacher.class, bClass);
	}

	@Data
	static class MyTeacher {

		public String age;
	}
}
