/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.collection.iter;

import org.dromara.hutool.core.exception.HutoolException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

/**
 * test for {@link LineIter}
 */
public class LineIterTest {

	@Test
	public void testHasNext() {
		final LineIter iter = getItrFromClasspathFile();
		Assertions.assertTrue(iter.hasNext());
	}

	@Test
	public void testNext() {
		final LineIter iter = getItrFromClasspathFile();
		Assertions.assertEquals("is first line", iter.next());
		Assertions.assertEquals("is second line", iter.next());
		Assertions.assertEquals("is third line", iter.next());
	}

	@Test
	public void testRemove() {
		final LineIter iter = getItrFromClasspathFile();
		iter.next();
		Assertions.assertThrows(UnsupportedOperationException.class, iter::remove);
	}

	@Test
	public void testFinish() {
		final LineIter iter = getItrFromClasspathFile();
		iter.finish();
		Assertions.assertThrows(NoSuchElementException.class, iter::next);
	}

	@Test
	public void testClose() throws IOException {
		final URL url = LineIterTest.class.getClassLoader().getResource("text.txt");
		Assertions.assertNotNull(url);
		final FileInputStream inputStream = new FileInputStream(url.getFile());
		final LineIter iter = new LineIter(inputStream, StandardCharsets.UTF_8);
		iter.close();
		Assertions.assertThrows(NoSuchElementException.class, iter::next);
		Assertions.assertThrows(IOException.class, inputStream::read);
	}

	private static LineIter getItrFromClasspathFile() {
		final URL url = LineIterTest.class.getClassLoader().getResource("text.txt");
		Assertions.assertNotNull(url);
		final FileInputStream inputStream;
		try {
			inputStream = new FileInputStream(url.getFile());
		} catch (final FileNotFoundException e) {
			throw new HutoolException(e);
		}
		final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		return new LineIter(bufferedReader);
	}

}
