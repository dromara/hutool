package cn.hutool.core.io;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.io.stream.EmptyOutputStream;
import cn.hutool.core.text.StrUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.channels.FileChannel;

public class NioUtilTest {
	@Test
	public void copyByNIOTest() {
		final long size = NioUtil.copyByNIO(ResourceUtil.getStream("hutool.jpg"), EmptyOutputStream.INSTANCE, NioUtil.DEFAULT_MIDDLE_BUFFER_SIZE, null);
		Assert.assertEquals(22807, size);
	}

	@Test
	public void readUtf8Test() throws IOException {
		final String s = NioUtil.readUtf8(FileChannel.open(FileUtil.file("text.txt").toPath()));
		Assert.assertTrue(StrUtil.isNotBlank(s));
	}
}
