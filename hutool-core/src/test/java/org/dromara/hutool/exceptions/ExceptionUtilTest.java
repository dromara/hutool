package org.dromara.hutool.exceptions;

import org.dromara.hutool.convert.Convert;
import org.dromara.hutool.io.IORuntimeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * 异常工具单元测试
 *
 * @author looly
 */
public class ExceptionUtilTest {

	@Test
	public void wrapTest() {
		final IORuntimeException e = ExceptionUtil.wrap(new IOException(), IORuntimeException.class);
		Assertions.assertNotNull(e);
	}

	@Test
	public void getRootTest() {
		// 查找入口方法
		final StackTraceElement ele = ExceptionUtil.getRootStackElement();
		Assertions.assertEquals("main", ele.getMethodName());
	}

	@Test
	public void convertTest() {
		// RuntimeException e = new RuntimeException();
		final IOException ioException = new IOException();
		final IllegalArgumentException argumentException = new IllegalArgumentException(ioException);
		final IOException ioException1 = ExceptionUtil.convertFromOrSuppressedThrowable(argumentException, IOException.class, true);
		Assertions.assertNotNull(ioException1);
	}

	@Test
	public void bytesIntConvertTest(){
		final String s = Convert.toStr(12);
		final int integer = Convert.toInt(s);
		Assertions.assertEquals(12, integer);

		final byte[] bytes = Convert.intToBytes(12);
		final int i = Convert.bytesToInt(bytes);
		Assertions.assertEquals(12, i);
	}
}
