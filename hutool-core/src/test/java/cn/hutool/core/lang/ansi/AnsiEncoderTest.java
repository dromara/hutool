package cn.hutool.core.lang.ansi;

import cn.hutool.core.lang.Console;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.awt.*;

public class AnsiEncoderTest {

	@Test
	public void encodeTest(){
		final String encode = AnsiEncoder.encode(AnsiColor.GREEN, "Hutool test");
		Assert.assertEquals("\u001B[32mHutool test\u001B[0;39m", encode);
	}

	@Test
	@Ignore
	public void colorfulEncodeTest(){
		String text = "Hutool▀████▀";
		final AnsiColors ansiColors = new AnsiColors(AnsiColors.BitDepth.EIGHT);
		Color[] colorArray = new Color[]{
				Color.BLACK,			Color.BLUE,
				Color.CYAN,				Color.DARK_GRAY,
				Color.GRAY,				Color.GREEN,
				Color.LIGHT_GRAY,		Color.MAGENTA,
				Color.ORANGE,			Color.PINK,
				Color.RED,				Color.WHITE,
				Color.YELLOW
		};
		for (Color foreColor : colorArray) {
			AnsiElement foreElement = ansiColors.findClosest(foreColor).toAnsiElement(AnsiColorWrapper.ForeOrBack.FORE);
			Color backColor = new Color(255 - foreColor.getRed(), 255 - foreColor.getGreen(), 255 - foreColor.getBlue());
			AnsiElement backElement = ansiColors.findClosest(backColor).toAnsiElement(AnsiColorWrapper.ForeOrBack.BACK);
			String encode = AnsiEncoder.encode(foreElement, backElement, text);
			//Console.print( i%2==1?encode+"\n":encode);
		}
	}
}
