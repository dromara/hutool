package org.dromara.hutool.img;

import org.dromara.hutool.io.file.FileTypeUtil;
import org.dromara.hutool.io.file.FileUtil;
import org.dromara.hutool.net.url.URLUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;

public class ImgTest {

	@Test
	@Disabled
	public void cutTest1() {
		Img.from(FileUtil.file("e:/pic/face.jpg")).cut(0, 0, 200).write(FileUtil.file("e:/pic/face_radis.png"));
	}

	@Test
	@Disabled
	public void compressTest() {
		Img.from(FileUtil.file("f:/test/4347273249269e3fb272341acc42d4e.jpg")).setQuality(0.8).write(FileUtil.file("f:/test/test_dest.jpg"));
	}

	@Test
	@Disabled
	public void writeTest() {
		final Img from = Img.from(FileUtil.file("d:/test/81898311-001d6100-95eb-11ea-83c2-a14d7b1010bd.png"));
		ImgUtil.write(from.getImg(), FileUtil.file("d:/test/dest.jpg"));
	}

	@Test
	@Disabled
	public void roundTest() {
		Img.from(FileUtil.file("e:/pic/face.jpg")).round(0.5).write(FileUtil.file("e:/pic/face_round.png"));
	}

	@Test
	@Disabled
	public void pressTextTest() {
		Img.from(FileUtil.file("d:/test/617180969474805871.jpg"))
				.setPositionBaseCentre(false)
				.pressText("版权所有", Color.RED, //
						new Font("黑体", Font.BOLD, 100), //
						0, //
						100, //
						1f)
				.write(FileUtil.file("d:/test/test2_result.png"));
	}


	@Test
	@Disabled
	public void pressTextFullScreenTest() {
		Img.from(FileUtil.file("d:/test/1435859438434136064.jpg"))
				.setTargetImageType(ImgUtil.IMAGE_TYPE_PNG)
				.pressTextFull("版权所有     ", Color.LIGHT_GRAY,
						new Font("黑体", Font.PLAIN, 30),
						4,
						30,
						0.8f)
				.write(FileUtil.file("d:/test/2_result.png"));

	}

	@Test
	@Disabled
	public void pressImgTest() {
		Img.from(FileUtil.file("d:/test/图片1.JPG"))
				.pressImage(ImgUtil.read("d:/test/617180969474805871.jpg"), new Rectangle(0, 0, 800, 800), 1f)
				.write(FileUtil.file("d:/test/pressImg_result.jpg"));
	}

	@Test
	@Disabled
	public void strokeTest() {
		Img.from(FileUtil.file("d:/test/公章3.png"))
				.stroke(null, 2f)
				.write(FileUtil.file("d:/test/stroke_result.png"));
	}

	/**
	 * issue#I49FIU
	 */
	@Test
	@Disabled
	public void scaleTest() {
		final String downloadFile = "d:/test/1435859438434136064.JPG";
		final File file = FileUtil.file(downloadFile);
		final File fileScale = FileUtil.file(downloadFile + ".scale." + FileTypeUtil.getType(file));

		final Image img = ImgUtil.getImage(URLUtil.getURL(file));
		ImgUtil.scale(img, fileScale, 0.8f);
	}

	@Test
	@Disabled
	public void rotateWithBackgroundTest() {
		Img.from(FileUtil.file("d:/test/aaa.jpg"))
				.setBackgroundColor(Color.RED)
				.rotate(45)
				.write(FileUtil.file("d:/test/aaa45.jpg"));
	}
}
