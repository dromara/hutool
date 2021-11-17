package cn.hutool.core.exceptions;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IORuntimeException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 异常工具单元测试
 *
 * @author looly
 */
public class ExceptionUtilTest {

	@Test
	public void wrapTest() {
		IORuntimeException e = ExceptionUtil.wrap(new IOException(), IORuntimeException.class);
		Assert.assertNotNull(e);
	}

	@Test
	public void getRootTest() {
		// 查找入口方法
		StackTraceElement ele = ExceptionUtil.getRootStackElement();
		Assert.assertEquals("main", ele.getMethodName());
	}

	@Test
	public void convertTest() {
		// RuntimeException e = new RuntimeException();
		IOException ioException = new IOException();
		IllegalArgumentException argumentException = new IllegalArgumentException(ioException);
		IOException ioException1 = ExceptionUtil.convertFromOrSuppressedThrowable(argumentException, IOException.class, true);
		Assert.assertNotNull(ioException1);
	}

	@Test
	public void bytesIntConvertTest(){
		final String s = Convert.toStr(12);
		final int integer = Convert.toInt(s);
		Assert.assertEquals(12, integer);

		final byte[] bytes = Convert.intToBytes(12);
		final int i = Convert.bytesToInt(bytes);
		Assert.assertEquals(12, i);
	}

	@Test
	public void ofTry() {

		//可以包裹住异常，不用立即处理，必须处理的异常也可以包裹住
		List<Integer> collect = Stream.of(5,2,3).map((x) -> ExceptionUtil.ofTry(() -> {
			Thread.sleep(400);
			return x;
		})).map(x -> x.orElse(-1)).collect(Collectors.toList());
		System.out.println(collect);

		//如果抛出异常，就把值替换成-1
		List<Integer> collect1 = Stream.of(5,2,3).map((x) -> ExceptionUtil.ofTry(() -> {
			int i =	1/0;
			return x;
		})).map(x -> x.orElse(-1)).collect(Collectors.toList());
		System.out.println(collect1);

		//如果抛出异常，可以打印出来
		List<Integer> collect2 = Stream.of(5,2,3).map((x) -> ExceptionUtil.ofTry(() -> {
			int i =	1/0;
			return x;
		})).map(x -> {
			x.getException().ifPresent((mess)->{
				System.out.println(mess.toString());
			});
			return x.orElse(-1);
		}).collect(Collectors.toList());

		System.out.println(collect2);
	}
}
