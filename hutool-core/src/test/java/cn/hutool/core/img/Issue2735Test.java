package cn.hutool.core.img;

import cn.hutool.core.io.FileUtil;
import org.junit.Ignore;
import org.junit.Test;

import java.awt.Image;

public class Issue2735Test {

	@Test
	@Ignore
	public void scaleTest() {
		final Img img = Img.from(FileUtil.file("d:/test/hutool.png"))
				.scale(200, 200, Image.SCALE_DEFAULT);

		img.write(FileUtil.file("d:/test/dest3.png"));
	}
}
