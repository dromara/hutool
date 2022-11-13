package cn.hutool.core.io;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class LineReaderTest {
	@Test
	public void readLfTest() {
		final LineReader lineReader = new LineReader(ResourceUtil.getUtf8Reader("multi_line.properties"));
		final ArrayList<String> list = ListUtil.of(lineReader);
		Assert.assertEquals(3, list.size());
		Assert.assertEquals("test1", list.get(0));
		Assert.assertEquals("test2=abcd\\e", list.get(1));
		Assert.assertEquals("test3=abc", list.get(2));
	}

	@Test
	public void readCrLfTest() {
		final LineReader lineReader = new LineReader(ResourceUtil.getUtf8Reader("multi_line_crlf.properties"));
		final ArrayList<String> list = ListUtil.of(lineReader);
		Assert.assertEquals(3, list.size());
		Assert.assertEquals("test1", list.get(0));
		Assert.assertEquals("test2=abcd\\e", list.get(1));
		Assert.assertEquals("test3=abc", list.get(2));
	}
}
