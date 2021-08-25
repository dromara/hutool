package cn.hutool.core.lang.func;

import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

public class LambdaUtilTest {

	@Test
	public void getMethodNameTest(){
		String methodName = LambdaUtil.getMethodName(MyTeacher::getAge);
		Assert.assertEquals("getAge", methodName);
	}

	@Test
	public void getFieldNameTest(){
		String fieldName = LambdaUtil.getFieldName(MyTeacher::getAge);
		Assert.assertEquals("age", fieldName);
	}

	@Data
	static class MyTeacher{
		public String age;
	}
}
