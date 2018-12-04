package cn.hutool.core.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Ignore;
import org.junit.Test;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ImageUtil;

public class ImageUtilTest {

	@Test
	@Ignore
	public void scaleTest() {
		ImageUtil.scale(FileUtil.file("d:/face.jpg"), FileUtil.file("d:/face_result.jpg"), 0.5f);
	}

	@Test
	@Ignore
	public void scaleByWidthAndHeightTest() {
		ImageUtil.scale(FileUtil.file("d:/face.jpg"), FileUtil.file("d:/face_result.jpg"), 100, 800, Color.BLUE);
	}

	@Test
	@Ignore
	public void cutTest() {
		ImageUtil.cut(FileUtil.file("d:/face.jpg"), FileUtil.file("d:/face_result.jpg"), new Rectangle(200, 200, 100, 100));
	}
	
	@Test
	@Ignore
	public void rotateTest() throws IOException {
		BufferedImage image = ImageUtil.rotate(ImageIO.read(FileUtil.file("e:/line.png")), 45);
		ImageUtil.write(image, FileUtil.file("e:/result.png"));
	}

	@Test
	@Ignore
	public void flipTest() throws IOException {
		ImageUtil.flip(FileUtil.file("d:/logo.png"), FileUtil.file("d:/result.png"));
	}

	@Test
	@Ignore
	public void pressImgTest() {
		ImageUtil.pressImage(FileUtil.file("d:/picTest/1.jpg"), FileUtil.file("d:/picTest/dest.jpg"), ImageUtil.read(FileUtil.file("d:/picTest/1432613.jpg")), 0, 0, 0.1f);
	}

	@Test
	@Ignore
	public void pressTextTest() {
		ImageUtil.pressText(//
				FileUtil.file("e:/face.jpg"), //
				FileUtil.file("e:/test2_result.png"), //
				"版权所有", Color.WHITE, //
				new Font("黑体", Font.BOLD, 100), //
				0, //
				0, //
				0.8f);
	}

	@Test
	@Ignore
	public void sliceByRowsAndColsTest() {
		ImageUtil.sliceByRowsAndCols(FileUtil.file("d:/picTest/1.jpg"), FileUtil.file("d:/picTest/dest"), 5, 5);
	}
	
	@Test
	@Ignore
	public void convertTest() {
		ImageUtil.convert(FileUtil.file("e:/test2.png"), FileUtil.file("e:/test2Convert.jpg"));
	}
	
	@Test
	@Ignore
	public void writeTest() {
		ImageUtil.write(ImageUtil.read("e:/test2.png"), FileUtil.file("e:/test2Write.jpg"));
	}
}
