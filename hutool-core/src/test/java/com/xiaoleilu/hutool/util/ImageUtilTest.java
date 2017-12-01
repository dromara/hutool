package com.xiaoleilu.hutool.util;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Ignore;
import org.junit.Test;

import com.xiaoleilu.hutool.io.FileUtil;
import com.xiaoleilu.hutool.util.ImageUtil;

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
		BufferedImage image = ImageUtil.rotate(ImageIO.read(FileUtil.file("d:/logo.png")), 45);
		ImageUtil.write(image, FileUtil.file("d:/result.png"));
	}
	
	@Test
	@Ignore
	public void flipTest() throws IOException {
		ImageUtil.flip(FileUtil.file("d:/logo.png"), FileUtil.file("d:/result.png"));
	}
}
