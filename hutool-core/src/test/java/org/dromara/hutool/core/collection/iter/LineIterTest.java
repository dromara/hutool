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
