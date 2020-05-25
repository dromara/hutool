package cn.hutool.core.img;

import org.junit.Assert;
import org.junit.Test;

import java.awt.Font;

public class FontUtilTest {

	@Test
	public void createFontTest(){
		final Font font = FontUtil.createFont();
		Assert.assertNotNull(font);
	}
}
