package cn.hutool.core.io;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.text.StrUtil;

/**
 * BufferUtil单元测试
 *
 * @author looly
 *
 */
public class BufferUtilTest {

	@Test
	public void copyTest() {
		final byte[] bytes = "AAABBB".getBytes();
		final ByteBuffer buffer = ByteBuffer.wrap(bytes);

		final ByteBuffer buffer2 = BufferUtil.copy(buffer, ByteBuffer.allocate(5));
		Assert.assertEquals("AAABB", StrUtil.utf8Str(buffer2));
	}

	@Test
	public void readBytesTest() {
		final byte[] bytes = "AAABBB".getBytes();
		final ByteBuffer buffer = ByteBuffer.wrap(bytes);

		final byte[] bs = BufferUtil.readBytes(buffer, 5);
		Assert.assertEquals("AAABB", StrUtil.utf8Str(bs));
	}

	@Test
	public void readBytes2Test() {
		final byte[] bytes = "AAABBB".getBytes();
		final ByteBuffer buffer = ByteBuffer.wrap(bytes);

		final byte[] bs = BufferUtil.readBytes(buffer, 5);
		Assert.assertEquals("AAABB", StrUtil.utf8Str(bs));
	}

	@Test
	public void readLineTest() {
		final String text = "aa\r\nbbb\ncc";
		final ByteBuffer buffer = ByteBuffer.wrap(text.getBytes());

		// 第一行
		String line = BufferUtil.readLine(buffer, CharsetUtil.UTF_8);
		Assert.assertEquals("aa", line);

		// 第二行
		line = BufferUtil.readLine(buffer, CharsetUtil.UTF_8);
		Assert.assertEquals("bbb", line);

		// 第三行因为没有行结束标志，因此返回null
		line = BufferUtil.readLine(buffer, CharsetUtil.UTF_8);
		Assert.assertNull(line);

		// 读取剩余部分
		Assert.assertEquals("cc", StrUtil.utf8Str(BufferUtil.readBytes(buffer)));
	}
}
