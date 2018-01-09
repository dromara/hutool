package cn.hutool.core.io;

import java.nio.ByteBuffer;

import org.junit.Test;

import cn.hutool.core.lang.Console;

public class ByteBufferUtilTest {
	
	@Test
	public void sizeTest() {
		byte[] bytes = "AAABBB".getBytes();
		Console.log(bytes.length);
		ByteBuffer buffer = ByteBuffer.allocate(40);
		buffer.put(bytes);
	}
}
