package cn.hutool.core.codec;

import org.junit.Assert;
import org.junit.Test;

public class RotTest {
	
	@Test
	public void rot13Test() {
		String str = "1f2e9df6131b480b9fdddc633cf24996";
		
		String encode13 = Rot.encode13(str);
		Assert.assertEquals("4s5r2qs9464o713o2sqqqp966ps57229", encode13);
		
		String decode13 = Rot.decode13(encode13);
		Assert.assertEquals(str, decode13);
	}
}
