package cn.hutool.core.img;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ImgUtilTest {

	@Test
	@Ignore
	public void scaleTest() {
		ImgUtil.scale(FileUtil.file("e:/pic/test.jpg"), FileUtil.file("e:/pic/test_result.jpg"), 0.8f);
	}

	@Test
	@Ignore
	public void scaleTest2() {
		ImgUtil.scale(
				FileUtil.file("d:/test/2.png"),
				FileUtil.file("d:/test/2_result.jpg"), 600, 337, null);
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
		ImgUtil.pressImage(
				FileUtil.file("d:/test/1435859438434136064.jpg"),
				FileUtil.file("d:/test/dest.jpg"),
				ImgUtil.read(FileUtil.file("d:/test/qrcodeCustom.png")), 0, 0, 0.9f);
	}

	@Test
	@Ignore
	public void pressTextTest() {
		ImgUtil.pressText(//
				FileUtil.file("d:/test/2.jpg"), //
				FileUtil.file("d:/test/2_result.png"), //
				"版权所有", Color.RED, //
				new Font("黑体", Font.BOLD, 100), //
				0, //
				0, //
				1f);
	}

	@Test
	@Ignore
	public void sliceByRowsAndColsTest() {
		ImgUtil.sliceByRowsAndCols(FileUtil.file("d:/temp/2.png"), FileUtil.file("d:/temp/slice/png"),ImgUtil.IMAGE_TYPE_PNG, 1, 5);
	}

	@Test
	@Ignore
	public void convertTest() {
		ImgUtil.convert(FileUtil.file("e:/test2.png"), FileUtil.file("e:/test2Convert.jpg"));
	}

	@Test
	@Ignore
	public void writeTest() {
		final byte[] bytes = ImgUtil.toBytes(ImgUtil.read("d:/test/logo_484.png"), "png");
		FileUtil.writeBytes(bytes, "d:/test/result.png");
	}

	@Test
	@Ignore
	public void compressTest() {
		ImgUtil.compress(FileUtil.file("d:/test/dest.png"),
				FileUtil.file("d:/test/1111_target.jpg"), 0.1f);
	}

	@Test
	@Ignore
	public void copyTest() {
		BufferedImage image = ImgUtil.copyImage(ImgUtil.read("f:/pic/test.png"), BufferedImage.TYPE_INT_RGB);
		ImgUtil.write(image, FileUtil.file("f:/pic/test_dest.jpg"));
	}

	@Test
	public void toHexTest(){
		final String s = ImgUtil.toHex(Color.RED);
		Assert.assertEquals("#FF0000", s);
	}

	@Test
	@Ignore
	public void backgroundRemovalTest() {
		// 图片 背景 换成 透明的
		ImgUtil.backgroundRemoval(
				"d:/test/617180969474805871.jpg",
				"d:/test/2.jpg", 10);

		// 图片 背景 换成 红色的
		ImgUtil.backgroundRemoval(new File(
				"d:/test/617180969474805871.jpg"),
				new File("d:/test/3.jpg"),
				new Color(200, 0, 0), 10);
	}

	@Test
	public void getMainColor() throws MalformedURLException {
		BufferedImage read = ImgUtil.read(new URL("https://pic2.zhimg.com/v2-94f5552f2b142ff575306850c5bab65d_b.png"));
		String mainColor = ImgUtil.getMainColor(read, new int[]{64,84,116});
		System.out.println(mainColor);
	}

	@Test
	@Ignore
	public void createImageTest() throws IORuntimeException, IOException {
		ImgUtil.createImage(
				"版权所有",
				new Font("黑体", Font.BOLD, 50),
				Color.WHITE,
				Color.BLACK,
				ImageIO.createImageOutputStream(new File("d:/test/createImageTest.png"))
		);
	}

	@Test
	@Ignore
	public void createTransparentImageTest() throws IORuntimeException, IOException {
		ImgUtil.createTransparentImage(
				"版权所有",
				new Font("黑体", Font.BOLD, 50),
				Color.BLACK,
				ImageIO.createImageOutputStream(new File("d:/test/createTransparentImageTest.png"))
		);
	}

}
