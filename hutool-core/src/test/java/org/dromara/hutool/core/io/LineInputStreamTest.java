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

package org.dromara.hutool.core.io;

import org.dromara.hutool.core.io.stream.LineInputStream;
import org.dromara.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class LineInputStreamTest {
	@Test
	public void LineInputStream_ValidInput_ShouldReadLinesCorrectly() {
		final String data = "first line\nsecond line\nthird line\n";
		final InputStream in = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
		final LineInputStream lineInputStream = new LineInputStream(in);
		assertEquals("first line", lineInputStream.readLine(CharsetUtil.UTF_8));
		assertEquals("second line", lineInputStream.readLine(CharsetUtil.UTF_8));
		assertEquals("third line", lineInputStream.readLine(CharsetUtil.UTF_8));
		assertNull(lineInputStream.readLine());  // No more lines
	}

	@Test
	public void LineInputStream_EmptyInput_ShouldReturnNull() {
		final String emptyData = "";
		final InputStream emptyIn = new ByteArrayInputStream(emptyData.getBytes(StandardCharsets.UTF_8));
		final LineInputStream lineInputStream = new LineInputStream(emptyIn);
		assertNull(lineInputStream.readLine());  // No lines in input
	}

	@Test
	public void LineInputStream_NoNewLineAtEnd_ShouldHandleLastLine() {
		final String noNewLineData = "first line\n第二 line\nthird 行";
		final InputStream noNewLineReader = new ByteArrayInputStream(noNewLineData.getBytes(StandardCharsets.UTF_8));
		final LineInputStream lineInputStream = new LineInputStream(noNewLineReader);
		assertEquals("first line", lineInputStream.readLine(CharsetUtil.UTF_8));
		assertEquals("第二 line", lineInputStream.readLine(CharsetUtil.UTF_8));
		assertEquals("third 行", lineInputStream.readLine(CharsetUtil.UTF_8));
		assertNull(lineInputStream.readLine());  // No more lines
	}
}

