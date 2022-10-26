package cn.hutool.core.io;

import cn.hutool.core.text.StrUtil;
import org.junit.Assert;
import org.junit.Test;

public class FastStringWriterTest {

	@SuppressWarnings("resource")
	@Test
	public void writeTest() {
		final FastStringWriter fastStringWriter = new FastStringWriter(IoUtil.DEFAULT_BUFFER_SIZE);
		fastStringWriter.write(StrUtil.repeat("hutool", 2));

		Assert.assertEquals("hutoolhutool", fastStringWriter.toString());
	}
}
