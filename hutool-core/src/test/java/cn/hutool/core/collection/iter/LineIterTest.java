package cn.hutool.core.collection.iter;

import org.junit.Assert;
import org.junit.Test;

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
		Assert.assertTrue(iter.hasNext());
	}

	@Test
	public void testNext() {
		LineIter iter = getItrFromClasspathFile();
		Assert.assertEquals("is first line", iter.next());
		Assert.assertEquals("is second line", iter.next());
		Assert.assertEquals("is third line", iter.next());
	}

	@Test
	public void testRemove() {
		LineIter iter = getItrFromClasspathFile();
		iter.next();
		Assert.assertThrows(UnsupportedOperationException.class, iter::remove);
	}

	@Test
	public void testFinish() {
		LineIter iter = getItrFromClasspathFile();
		iter.finish();
		Assert.assertThrows(NoSuchElementException.class, iter::next);
	}

	@Test
	public void testClose() throws IOException {
		URL url = LineIterTest.class.getClassLoader().getResource("text.txt");
		Assert.assertNotNull(url);
		FileInputStream inputStream = new FileInputStream(url.getFile());
		LineIter iter = new LineIter(inputStream, StandardCharsets.UTF_8);
		iter.close();
		Assert.assertThrows(NoSuchElementException.class, iter::next);
		Assert.assertThrows(IOException.class, inputStream::read);
	}

	private static LineIter getItrFromClasspathFile() {
		URL url = LineIterTest.class.getClassLoader().getResource("text.txt");
		Assert.assertNotNull(url);
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
