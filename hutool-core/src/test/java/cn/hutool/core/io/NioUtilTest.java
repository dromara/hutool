package cn.hutool.core.io;

import cn.hutool.core.io.file.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.io.stream.EmptyOutputStream;
import cn.hutool.core.lang.Console;
import cn.hutool.core.text.StrUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class NioUtilTest {
	@Test
	public void copyByNIOTest() {
		final File file = FileUtil.file("hutool.jpg");
		final long size = NioUtil.copyByNIO(ResourceUtil.getStream("hutool.jpg"), EmptyOutputStream.INSTANCE, NioUtil.DEFAULT_MIDDLE_BUFFER_SIZE, null);

		// 确认写出
		Assert.assertEquals(file.length(), size);
		Assert.assertEquals(22807, size);
	}

	@Test
	@Ignore
	public void copyByNIOTest2() {
		final File file = FileUtil.file("d:/test/logo.jpg");
		final BufferedInputStream in = FileUtil.getInputStream(file);
		final BufferedOutputStream out = FileUtil.getOutputStream("d:/test/2logo.jpg");

		final long copySize = IoUtil.copyByNIO(in, out, NioUtil.DEFAULT_BUFFER_SIZE, new StreamProgress() {
			@Override
			public void start() {
				Console.log("start");
			}

			@Override
			public void progress(final long total, final long progressSize) {
				Console.log("{} {}", total, progressSize);
			}

			@Override
			public void finish() {
				Console.log("finish");
			}
		});
		Assert.assertEquals(file.length(), copySize);
	}

	@Test
	public void readUtf8Test() throws IOException {
		final String s = NioUtil.readUtf8(FileChannel.open(FileUtil.file("text.txt").toPath()));
		Assert.assertTrue(StrUtil.isNotBlank(s));
	}
}
