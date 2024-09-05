/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.core.exception;

import org.dromara.hutool.core.convert.ConvertUtil;
import org.dromara.hutool.core.io.IORuntimeException;
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
		final String s = ConvertUtil.toStr(12);
		final int integer = ConvertUtil.toInt(s);
		Assertions.assertEquals(12, integer);

		final byte[] bytes = ConvertUtil.intToBytes(12);
		final int i = ConvertUtil.bytesToInt(bytes);
		Assertions.assertEquals(12, i);
	}
}
