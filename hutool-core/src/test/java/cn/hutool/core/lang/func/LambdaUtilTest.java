package cn.hutool.core.lang.func;

import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LambdaUtilTest {

	@Test
	public void getMethodNameTest(){
		String methodName = LambdaUtil.getMethodName(MyTeacher::getAge);
		Assert.assertEquals("getAge", methodName);
	}

	@Test
	public void getFieldNameTest() {
		String fieldName1 = LambdaUtil.getFieldName(MyTeacher::getAge);
		Assert.assertEquals("age", fieldName1);

		String fieldName2 = LambdaUtil.getFieldName(MyTeacher::isVip);
		Assert.assertEquals("vip", fieldName2);
	}

	@Test
	public void raiseTest() {
		Assert.assertThrows("受检异常", Exception.class, () -> LambdaUtil.raise(new Exception("受检异常")));
	}


	@Test
	public void raiseWrapperTest() {
		File[] files = new File[]{new File("/test.txt"), new File("/test2.txt")};

		Assert.assertThrows("文件不存在", FileNotFoundException.class, () -> {
			// 在lambda表达式中绕过了对受检异常的处理
			List<String> contents = Arrays.stream(files)
					.map(file -> LambdaUtil.raise(() -> LambdaUtilTest.this.readFile(file)))
					.collect(Collectors.toList());

		});
	}


	@Test
	public void raiseWrapperNoReturnTest() {
		File[] files = new File[]{new File("/test.txt"), new File("/test2.txt")};

		Assert.assertThrows("文件不存在", FileNotFoundException.class, () -> {
			// 在lambda表达式中绕过了对受检异常的处理
			Arrays.stream(files).forEach(file -> LambdaUtil.raise(() -> LambdaUtilTest.this.saveFile(file)));
		});
	}



	@Data
	static class MyTeacher{
		public String age;
		public boolean vip;
	}

	/**
	 * 有返回值的会抛受检异常的方法
	 */
	private String readFile(File file) throws IOException {
		throw new FileNotFoundException("文件不存在");
	}

	/**
	 * 没有返回值的会抛受检异常的方法
	 */
	private void saveFile(File file) throws IOException {
		throw new FileNotFoundException("文件不存在");
	}
}
