package cn.hutool.core.lang.func;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LambdaUtilTest {

	@Test
	public void getMethodNameTest(){
		String methodName = LambdaUtil.getMethodName(MyTeacher::getAge);
		Assertions.assertEquals("getAge", methodName);
	}

	@Test
	public void getFieldNameTest(){
		String fieldName = LambdaUtil.getFieldName(MyTeacher::getAge);
		Assertions.assertEquals("age", fieldName);
	}

	@Data
	static class MyTeacher{
		public String age;
	}
}
