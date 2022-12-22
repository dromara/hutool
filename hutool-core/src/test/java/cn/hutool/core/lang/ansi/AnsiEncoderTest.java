package cn.hutool.core.lang.ansi;

import cn.hutool.core.lang.Console;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.awt.Color;

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
		for (int i = 0; i < colorArray.length; i++) {
			Color foreColor = colorArray[i];
			AnsiElement foreElement = ansiColors.findClosest(foreColor).toAnsiElement(ForeOrBack.FORE);
			Color backColor = new Color(255 - foreColor.getRed(), 255 - foreColor.getGreen(), 255 - foreColor.getBlue());
			AnsiElement backElement = ansiColors.findClosest(backColor).toAnsiElement(ForeOrBack.BACK);
			String encode = AnsiEncoder.encode(foreElement, backElement, text);
			Console.print( i%2==1?encode+"\n":encode);
		}
	}

	@Test
	public void colorMappingTest(){
		String text4 = "RGB:({},{},{})--4bit ";
		String text8 = "RGB:({},{},{})--8bit ";
		final AnsiColors ansiColors4Bit = new AnsiColors(AnsiColors.BitDepth.FOUR);
		final AnsiColors ansiColors8Bit = new AnsiColors(AnsiColors.BitDepth.EIGHT);
		int count = 0;
		int from = 100000;
		int until = 120000;
		for (int r = 0; r < 256; r++) {
			if (count>until)break;
			for (int g = 0; g < 256; g++) {
				if (count>until)break;
				for (int b = 0; b < 256; b++) {
					count++;
					if (count<from)continue;
					if (count>until)break;
					AnsiElement backElement4bit = ansiColors4Bit.findClosest(new Color(r,g,b)).toAnsiElement(ForeOrBack.BACK);
					AnsiElement backElement8bit = ansiColors8Bit.findClosest(new Color(r,g,b)).toAnsiElement(ForeOrBack.BACK);
					String encode4 = AnsiEncoder.encode( backElement4bit,text4);
					String encode8 = AnsiEncoder.encode( backElement8bit,text8);
					//Console.log(StrUtil.format(encode4,r,g,b)+StrUtil.format(encode8,r,g,b));
				}
			}
		}
	}
}
