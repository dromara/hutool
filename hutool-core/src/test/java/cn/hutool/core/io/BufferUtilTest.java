package cn.hutool.core.io;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;

public class BufferUtilTest {
	
	@Test
	public void readBytesTest() {
		byte[] bytes = "AAABBB".getBytes();
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		
		byte[] bs = BufferUtil.readBytes(buffer, 5);
		Assert.assertEquals("AAABB", StrUtil.utf8Str(bs));
	}
	
	@Test
	public void readBytes2Test() {
		byte[] bytes = "AAABBB".getBytes();
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		
		byte[] bs = BufferUtil.readBytes(buffer, 5);
		Assert.assertEquals("AAABB", StrUtil.utf8Str(bs));
	}
	
	@Test
	public void readLineTest() {
		String text = "aaaaaaa\nbbbbbbbbbb\rcccccccc";
		ByteBuffer buffer = ByteBuffer.wrap(text.getBytes());
		String line = BufferUtil.readLine(buffer, CharsetUtil.CHARSET_UTF_8);
		Console.log(line);
	}
}
