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

	@Data
	static class MyTeacher{
		public String age;
	}
}
