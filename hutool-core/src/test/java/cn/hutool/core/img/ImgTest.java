package cn.hutool.core.img;

import cn.hutool.core.io.FileUtil;
import org.junit.Ignore;
import org.junit.Test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;

public class ImgTest {
	
	@Test
	@Ignore
	public void cutTest1() {
		Img.from(FileUtil.file("e:/pic/face.jpg")).cut(0, 0, 200).write(FileUtil.file("e:/pic/face_radis.png"));
	}
	
	@Test
	@Ignore
	public void compressTest() {
		Img.from(FileUtil.file("f:/test/4347273249269e3fb272341acc42d4e.jpg")).setQuality(0.8).write(FileUtil.file("f:/test/test_dest.jpg"));
	}

	@Test
	@Ignore
	public void writeTest() {
		final Img from = Img.from(FileUtil.file("d:/test/81898311-001d6100-95eb-11ea-83c2-a14d7b1010bd.png"));
		ImgUtil.write(from.getImg(), FileUtil.file("d:/test/dest.jpg"));
	}
	
	@Test
	@Ignore
	public void roundTest() {
		Img.from(FileUtil.file("e:/pic/face.jpg")).round(0.5).write(FileUtil.file("e:/pic/face_round.png"));
	}

	@Test
	@Ignore
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
	@Ignore
	public void pressImgTest(){
		Img.from(FileUtil.file("d:/test/图片1.JPG"))
				.pressImage(ImgUtil.read("d:/test/617180969474805871.jpg"), new Rectangle(0, 0, 800, 800), 1f)
				.write(FileUtil.file("d:/test/pressImg_result.jpg"));
	}

	@Test
	@Ignore
	public void strokeTest(){
		Img.from(FileUtil.file("d:/test/公章3.png"))
				.stroke(null, 2f)
				.write(FileUtil.file("d:/test/stroke_result.png"));
	}
}
