package org.dromara.hutool.io;

import org.dromara.hutool.text.StrUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FastStringWriterTest {

	@SuppressWarnings("resource")
	@Test
	public void writeTest() {
		final FastStringWriter fastStringWriter = new FastStringWriter(IoUtil.DEFAULT_BUFFER_SIZE);
		fastStringWriter.write(StrUtil.repeat("hutool", 2));

		Assertions.assertEquals("hutoolhutool", fastStringWriter.toString());
	}
}
