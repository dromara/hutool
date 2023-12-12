/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.swing.img;

import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.swing.img.color.ColorUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ImgUtilTest {

	@Test
	@Disabled
	public void scaleTest() {
		ImgUtil.scale(FileUtil.file("e:/pic/test.jpg"), FileUtil.file("e:/pic/test_result.jpg"), 0.8f);
	}

	@Test
	@Disabled
	public void scaleTest2() {
		ImgUtil.scale(
				FileUtil.file("d:/test/2.png"),
				FileUtil.file("d:/test/2_result.jpg"), 600, 337, null);
	}

	@Test
	@Disabled
	public void scalePngTest() {
		ImgUtil.scale(FileUtil.file("f:/test/test.png"), FileUtil.file("f:/test/test_result.png"), 0.5f);
	}

	@Test
	@Disabled
	public void scaleByWidthAndHeightTest() {
		ImgUtil.scale(FileUtil.file("f:/test/aaa.jpg"), FileUtil.file("f:/test/aaa_result.jpg"), 100, 400, Color.BLUE);
	}

	@Test
	@Disabled
	public void cutTest() {
		ImgUtil.cut(FileUtil.file("d:/test/hutool.png"),
			FileUtil.file("d:/test/result.png"),
			new Rectangle(0, 0, 400, 240));
	}

	@Test
	@Disabled
	public void rotateTest() throws IOException {
		final Image image = ImgUtil.rotate(ImageIO.read(FileUtil.file("e:/pic/366466.jpg")), 180);
		ImgUtil.write(image, FileUtil.file("e:/pic/result.png"));
	}

	@Test
	@Disabled
	public void flipTest() {
		ImgUtil.flip(FileUtil.file("d:/logo.png"), FileUtil.file("d:/result.png"));
	}

	@Test
	@Disabled
	public void pressImgTest() {
		ImgUtil.pressImage(
				FileUtil.file("d:/test/1435859438434136064.jpg"),
				FileUtil.file("d:/test/dest.jpg"),
				ImgUtil.read(FileUtil.file("d:/test/qrcodeCustom.png")), 0, 0, 0.9f);
	}

	@Test
	@Disabled
	public void pressTextTest() {
		ImgUtil.pressText(//
				FileUtil.file("d:/test/2.jpg"), //
				FileUtil.file("d:/test/2_result.png"), //
				DisplayText.of("版权所有", Color.RED, new Font("黑体", Font.BOLD, 100), new Point(0, 0), 1f));
	}

	@Test
	@Disabled
	public void pressImageFullScreenTest() {
		ImgUtil.pressImageFull(new File("/Users/imashimaro/Downloads/Background.png"),
				new File("/Users/imashimaro/Downloads/Background_logo.png"),
				new File("/Users/imashimaro/Downloads/logo.png"),
				2,
				30,
				0.5F);

	}

	@Test
	@Disabled
	public void sliceByRowsAndColsTest() {
		ImgUtil.sliceByRowsAndCols(FileUtil.file("d:/test/logo.jpg"), FileUtil.file("d:/test/dest"), ImgUtil.IMAGE_TYPE_JPEG, 1, 5);
	}

	@Test
	@Disabled
	public void sliceByRowsAndColsTest2() {
		ImgUtil.sliceByRowsAndCols(
			FileUtil.file("d:/test/hutool.png"),
			FileUtil.file("d:/test/dest"), ImgUtil.IMAGE_TYPE_PNG, 1, 5);
	}

	@Test
	@Disabled
	public void convertTest() {
		ImgUtil.convert(FileUtil.file("e:/test2.png"), FileUtil.file("e:/test2Convert.jpg"));
	}

	@Test
	@Disabled
	public void writeTest() {
		final byte[] bytes = ImgUtil.toBytes(ImgUtil.read("d:/test/logo_484.png"), "png");
		FileUtil.writeBytes(bytes, "d:/test/result.png");
	}

	@Test
	@Disabled
	public void compressTest() {
		ImgUtil.compress(FileUtil.file("d:/test/dest.png"),
				FileUtil.file("d:/test/1111_target.jpg"), 0.1f);
	}

	@Test
	@Disabled
	public void copyTest() {
		final BufferedImage image = ImgUtil.copyImage(ImgUtil.read("f:/pic/test.png"), BufferedImage.TYPE_INT_RGB);
		ImgUtil.write(image, FileUtil.file("f:/pic/test_dest.jpg"));
	}

	@Test
	public void toHexTest(){
		final String s = ColorUtil.toHex(Color.RED);
		Assertions.assertEquals("#FF0000", s);
	}

	@Test
	@Disabled
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
	@Disabled
	public void getMainColor() throws MalformedURLException {
		final BufferedImage read = ImgUtil.read(new URL("https://pic2.zhimg.com/v2-94f5552f2b142ff575306850c5bab65d_b.png"));
		final String mainColor = ColorUtil.getMainColor(read, new int[]{64,84,116});
		System.out.println(mainColor);
	}

	@Test
	@Disabled
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
	@Disabled
	public void createTransparentImageTest() throws IORuntimeException, IOException {
		ImgUtil.createTransparentImage(
				"版权所有",
				new Font("黑体", Font.BOLD, 50),
				Color.BLACK,
				ImageIO.createImageOutputStream(new File("d:/test/createTransparentImageTest.png"))
		);
	}

	@Test
	@Disabled
	public void issue2765Test() {
		// 利用图片元数据工具读取图片旋转角度信息
		final File file = FileUtil.file("d:/test/204691690-715c29d9-793a-4b29-ab1d-191a741438bb.jpg");
		final int orientation = ImgMetaUtil.getOrientation(file);
		Console.log(orientation);
		Img.from(file)
				.rotate(orientation)
				.write(FileUtil.file("d:/test/aaa.jpg"));
	}
}
