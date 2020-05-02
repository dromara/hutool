package cn.hutool.core.img;

import cn.hutool.core.io.FileUtil;
import org.junit.Ignore;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImgUtilTest {

	@Test
	@Ignore
	public void scaleTest() {
		ImgUtil.scale(FileUtil.file("e:/pic/test.jpg"), FileUtil.file("e:/pic/test_result.jpg"), 0.8f);
	}

	@Test
	@Ignore
	public void scaleTest2() {
		ImgUtil.scale(FileUtil.file("e:/pic/test.jpg"), FileUtil.file("e:/pic/test_result.jpg"), 0.8f);
	}

	@Test
	@Ignore
	public void scalePngTest() {
		ImgUtil.scale(FileUtil.file("f:/test/test.png"), FileUtil.file("f:/test/test_result.png"), 0.5f);
	}

	@Test
	@Ignore
	public void scaleByWidthAndHeightTest() {
		ImgUtil.scale(FileUtil.file("f:/test/aaa.jpg"), FileUtil.file("f:/test/aaa_result.jpg"), 100, 400, Color.BLUE);
	}

	@Test
	@Ignore
	public void cutTest() {
		ImgUtil.cut(FileUtil.file("d:/face.jpg"), FileUtil.file("d:/face_result.jpg"), new Rectangle(200, 200, 100, 100));
	}
	
	@Test
	@Ignore
	public void rotateTest() throws IOException {
		Image image = ImgUtil.rotate(ImageIO.read(FileUtil.file("e:/pic/366466.jpg")), 180);
		ImgUtil.write(image, FileUtil.file("e:/pic/result.png"));
	}

	@Test
	@Ignore
	public void flipTest() {
		ImgUtil.flip(FileUtil.file("d:/logo.png"), FileUtil.file("d:/result.png"));
	}

	@Test
	@Ignore
	public void pressImgTest() {
		ImgUtil.pressImage(FileUtil.file("d:/picTest/1.jpg"), FileUtil.file("d:/picTest/dest.jpg"), ImgUtil.read(FileUtil.file("d:/picTest/1432613.jpg")), 0, 0, 0.1f);
	}

	@Test
	@Ignore
	public void pressTextTest() {
		ImgUtil.pressText(//
				FileUtil.file("e:/pic/face.jpg"), //
				FileUtil.file("e:/pic/test2_result.png"), //
				"版权所有", Color.WHITE, //
				new Font("黑体", Font.BOLD, 100), //
				0, //
				0, //
				0.8f);
	}

	@Test
	@Ignore
	public void sliceByRowsAndColsTest() {
		ImgUtil.sliceByRowsAndCols(FileUtil.file("e:/pic/1.png"), FileUtil.file("e:/pic/dest"), 10, 10);
	}
	
	@Test
	@Ignore
	public void convertTest() {
		ImgUtil.convert(FileUtil.file("e:/test2.png"), FileUtil.file("e:/test2Convert.jpg"));
	}
	
	@Test
	@Ignore
	public void writeTest() {
		ImgUtil.write(ImgUtil.read("e:/test2.png"), FileUtil.file("e:/test2Write.jpg"));
	}
	
	@Test
	@Ignore
	public void compressTest() {
		ImgUtil.compress(FileUtil.file("e:/pic/1111.png"), FileUtil.file("e:/pic/1111_target.jpg"), 0.8f);
	}
	
	@Test
	@Ignore
	public void copyTest() {
		BufferedImage image = ImgUtil.copyImage(ImgUtil.read("f:/pic/test.png"), BufferedImage.TYPE_INT_RGB);
		ImgUtil.write(image, FileUtil.file("f:/pic/test_dest.jpg"));
	}
}
