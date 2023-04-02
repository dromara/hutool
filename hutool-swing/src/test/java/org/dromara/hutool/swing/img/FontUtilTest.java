package org.dromara.hutool.swing.img;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.Font;

public class FontUtilTest {

	@Test
	public void createFontTest(){
		final Font font = FontUtil.createFont();
		Assertions.assertNotNull(font);
	}
}
