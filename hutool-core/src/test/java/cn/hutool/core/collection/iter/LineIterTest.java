package cn.hutool.core.collection.iter;

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
		LineIter iter = getItrFromClasspathFile();
		Assertions.assertTrue(iter.hasNext());
	}

	@Test
	public void testNext() {
		LineIter iter = getItrFromClasspathFile();
		Assertions.assertEquals("is first line", iter.next());
		Assertions.assertEquals("is second line", iter.next());
		Assertions.assertEquals("is third line", iter.next());
	}

	@Test
	public void testRemove() {
		LineIter iter = getItrFromClasspathFile();
		iter.next();
		Assertions.assertThrows(UnsupportedOperationException.class, iter::remove);
	}

	@Test
	public void testFinish() {
		LineIter iter = getItrFromClasspathFile();
		iter.finish();
		Assertions.assertThrows(NoSuchElementException.class, iter::next);
	}

	@Test
	public void testClose() throws IOException {
		URL url = LineIterTest.class.getClassLoader().getResource("text.txt");
		Assertions.assertNotNull(url);
		FileInputStream inputStream = new FileInputStream(url.getFile());
		LineIter iter = new LineIter(inputStream, StandardCharsets.UTF_8);
		iter.close();
		Assertions.assertThrows(NoSuchElementException.class, iter::next);
		Assertions.assertThrows(IOException.class, inputStream::read);
	}

	private static LineIter getItrFromClasspathFile() {
		URL url = LineIterTest.class.getClassLoader().getResource("text.txt");
		Assertions.assertNotNull(url);
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(url.getFile());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		return new LineIter(bufferedReader);
	}

}
