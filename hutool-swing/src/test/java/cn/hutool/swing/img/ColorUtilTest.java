package cn.hutool.swing.img;

import cn.hutool.swing.img.color.ColorUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.Color;

public class ColorUtilTest {

	@Test
	public void getColorTest(){
		final Color blue = ColorUtil.getColor("blue");
		Assertions.assertEquals(Color.BLUE, blue);
	}

	@Test
	public void toCssRgbTest(){
		final String s = ColorUtil.toCssRgb(Color.BLUE);
		Assertions.assertEquals("rgb(0,0,255)", s);
	}

	@Test
	public void toCssRgbaTest(){
		final String s = ColorUtil.toCssRgba(Color.BLUE);
		Assertions.assertEquals("rgba(0,0,255,1.0)", s);
	}
}
