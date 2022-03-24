package cn.hutool.core.lang.func;

import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

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
		// 类方法引用，相当于获取的方法引用是：MyTeacher.getAge(this)
		// 因此此处会匹配到Func1，其参数就是this
		Class<MyTeacher> aClass = LambdaUtil.getImplClass(MyTeacher::getAge);
		Assert.assertEquals(MyTeacher.class, aClass);

		MyTeacher myTeacher = new MyTeacher();

		// 对象方法引用，因为已经有了对象，因此此处引用相当于获取：myTeacher.getAge()
		aClass = LambdaUtil.getImplClass(myTeacher::getAge);
		Assert.assertEquals(MyTeacher.class, aClass);

		// 静态方法引用，相当于获取：MyTeader.takeAge
		aClass = LambdaUtil.getImplClass(MyTeacher::takeAge);
		Assert.assertEquals(MyTeacher.class, aClass);
	}

	@Data
	static class MyTeacher {

		public static String takeAge(){
			return new MyTeacher().getAge();
		}

		public String age;
	}
}
