package cn.hutool.core.lang;

import cn.hutool.core.util.RandomUtil;
import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;

import java.awt.*;

import static cn.hutool.core.lang.ConsoleColor.*;

public class ConsoleColorTest extends TestCase {

	private static final String TEST_TEXT = "Hello, world!";

	@Test
	public void testColorCards() {
		printStandardColors();
		printBrightColors();
		printDarkColors();
		printLightColors();
		printGrayscaleColors();
	}

	@Test
	public void testColor() {
		ColorEnum color = ColorEnum.random();
		String testText = String.format("Code: %s\t" + TEST_TEXT, color.toString());
		System.out.println(colorText(testText, color));
		System.out.println(colorText(testText + "\t(BRIGHT)", color, true));
		System.out.println(colorBackground(testText, color));
		System.out.println(colorBackground(testText + "\t(BRIGHT)", color, true));
	}

	@Test
	public void test8BitColor() {
		int code = RandomUtil.randomInt(256);
		String testText = String.format("Code:%3d\t" + TEST_TEXT, code);
		System.out.println(color8BitText(testText, code));
		System.out.println(color8BitBackground(testText, code));
	}

	@Test
	public void testRGBColor() {
		int r = RandomUtil.randomInt(256);
		int g = RandomUtil.randomInt(256);
		int b = RandomUtil.randomInt(256);
		Color color = new Color(r, g, b);
		String testText = String.format("RGB:(%3d,%3d,%3d)\t" + TEST_TEXT, r, g, b);
		System.out.println(colorRGBText(testText, color));
		System.out.println(colorRGBBackground(testText, color));
	}

	/**
	 * 测试失败，按wiki所述方法未成功
	 *
	 * @see <a href="https://en.wikipedia.org/wiki/ANSI_escape_code#24-bit">ANSI escape code</a>
	 */
	@Test
	@Ignore
	public void testCMYK() {
		int c = RandomUtil.randomInt(100);
		int m = RandomUtil.randomInt(100);
		int y = RandomUtil.randomInt(100);
		int k = RandomUtil.randomInt(100);

		String testText = String.format("CMY:(%3d,%3d,%3d)\t" + TEST_TEXT, c, m, y);
		String color = String.format("\u001B[48;3;%d;%d;%dm", c, m, y);
		System.out.println(testText);
		System.out.println(color + TEST_TEXT + RESET);

		String testText2 = String.format("CMYK:(%3d,%3d,%3d,%3d)\t" + TEST_TEXT, c, m, y, k);
		String color2 = String.format("\u001B[48;4;%d;%d;%d;%dm", c, m, y, k);
		System.out.println(testText2);
		System.out.println(color2 + TEST_TEXT + RESET);
	}
}
