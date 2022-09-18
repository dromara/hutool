package cn.hutool.swing.img;

import cn.hutool.swing.img.color.ColorUtil;
import org.junit.Assert;
import org.junit.Test;

import java.awt.Color;

public class ColorUtilTest {

	@Test
	public void getColorTest(){
		final Color blue = ColorUtil.getColor("blue");
		Assert.assertEquals(Color.BLUE, blue);
	}

	@Test
	public void toCssRgbTest(){
		final String s = ColorUtil.toCssRgb(Color.BLUE);
		Assert.assertEquals("rgb(0,0,255)", s);
	}

	@Test
	public void toCssRgbaTest(){
		final String s = ColorUtil.toCssRgba(Color.BLUE);
		Assert.assertEquals("rgba(0,0,255,1.0)", s);
	}
}
