package cn.hutool.core.img;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Ignore;
import org.junit.Test;

import cn.hutool.core.io.FileUtil;

public class ImgUtilTest {

	@Test
	@Ignore
	public void scaleTest() {
		ImgUtil.scale(FileUtil.file("d:/face.jpg"), FileUtil.file("d:/face_result.jpg"), 0.5f);
	}
	
	@Test
	@Ignore
	public void scalePngTest() {
		ImgUtil.scale(FileUtil.file("e:/pic/hutool.png"), FileUtil.file("e:/pic/hutool_result.png"), 0.5f);
	}

	@Test
	@Ignore
	public void scaleByWidthAndHeightTest() {
		ImgUtil.scale(FileUtil.file("d:/face.jpg"), FileUtil.file("d:/face_result.jpg"), 100, 800, Color.BLUE);
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
	public void flipTest() throws IOException {
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
		ImgUtil.sliceByRowsAndCols(FileUtil.file("d:/picTest/1.jpg"), FileUtil.file("d:/picTest/dest"), 5, 5);
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
}
